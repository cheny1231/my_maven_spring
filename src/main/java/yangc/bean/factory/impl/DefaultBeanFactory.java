package yangc.bean.factory.impl;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import yangc.bean.definition.BeanDefinition;
import yangc.bean.definition.BeanDefinitionRegistry;
import yangc.bean.factory.BeanFactory;
import yangc.bean.postProcessor.AopPostProcessor;
import yangc.bean.reference.BeanReference;

public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry, Closeable {
//	private Log log = LogFactory.getLog(this.getClass());

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
	private Map<String, Object> beanMap = new ConcurrentHashMap<>();

	private ThreadLocal<Set<String>> initializedBeans = new ThreadLocal<>();

	List<AopPostProcessor> postProcessors = new ArrayList<>();

	@Override
	public void register(BeanDefinition bd, String beanName) {
		assert beanName != null;
		assert bd != null;
		if (beanDefinitionMap.containsKey(beanName)) {
			System.out.println(beanName + "已经存在");
			return;
		}
		if (!bd.validate()) {
			System.out.println(beanName + "不合法");
			return;
		}
		System.out.println("Registering " + beanName);
		beanDefinitionMap.put(beanName, bd);
	}

	@Override
	public BeanDefinition getBeanDefinition(String beanName) {
		if (!beanDefinitionMap.containsKey(beanName)) {
			System.out.println(beanName + "不存在");
		}
		return beanDefinitionMap.get(beanName);
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return beanDefinitionMap.containsKey(beanName);
	}

	@Override
	public Object getBean(String beanName) throws Exception {
		return doGetBean(beanName);
	}

	public Object doGetBean(String beanName) throws Exception {
		if (!beanMap.containsKey(beanName)) {
			System.out.println(beanName + "不存在");
		}

		Object instance = beanMap.get(beanName);

		if (instance != null) {
			return instance;
		}

		if (!beanDefinitionMap.containsKey(beanName)) {
			System.out.println("Bean definition [" + beanName + "] does not exist");
			System.out.println("R U looking for:");
			for (String key : beanDefinitionMap.keySet()) {
				System.out.println(key);
			}
		}

		Set<String> beans = initializedBeans.get();
		if (beans == null) {
			beans = new HashSet<>();
			initializedBeans.set(beans);
		}
		// detect circular dependency
		if (beans.contains(beanName)) {
			throw new Exception("Found circular dependency");
		}

		beans.add(beanName);

		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		Class<?> beanClass = beanDefinition.getBeanClass();

		if (beanClass != null) {
			instance = createBeanByConstruct(beanDefinition);
			if (instance == null) {
				instance = createBeanByFactoryMethod(beanDefinition);
			}
		} else if (beanDefinition.getStaticCreateBeanMethod() != null) {
			instance = createBeanByFactoryMethod(beanDefinition);
		}

		doInit(beanDefinition, instance);

		parsePropertyValues(beanDefinition, instance);

		beans.remove(beanName);

		instance = applyAopBeanPostProcessor(instance, beanName);

		if (instance != null && beanDefinition.isSingleton()) {
			beanMap.put(beanName, instance);
		}

		return instance;
	}

	private void doInit(BeanDefinition beanDefinition, Object instance) {
		Class<?> beanClass = instance.getClass();
		if (beanDefinition.getBeanInitMethod() != null) {
			try {
				Method method = beanClass.getMethod(beanDefinition.getBeanInitMethod(), null);
				method.invoke(instance, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Object createBeanByConstruct(BeanDefinition bd) {
		Object instance = null;
		try {
			Object[] constructorArgs = parseConstructorArgs(bd.getConstructorArgument());
			Constructor<?> constructor = matchConstructor(bd, constructorArgs);
			if (constructor != null) {
				instance = constructor.newInstance(constructorArgs);
			} else {
				instance = bd.getBeanClass().newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private Object createBeanByFactoryMethod(BeanDefinition beanDefinition) {
		Object instance = null;
		try {
			Object fatory = doGetBean(beanDefinition.getBeanFactory());
			Method method = fatory.getClass().getMethod(beanDefinition.getCreateBeanMethod());
			instance = method.invoke(fatory, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return instance;
	}

	private Object[] parseConstructorArgs(List<?> constructorArgs) throws Exception {
		if (constructorArgs == null || constructorArgs.isEmpty()) {
			return null;
		}

		Object[] args = new Object[constructorArgs.size()];
		for (int i = 0; i < constructorArgs.size(); i++) {
			Object arg = constructorArgs.get(i);
			Object value = null;
			if (arg instanceof BeanReference) {
				String beanName = ((BeanReference) arg).getBeanName();
				value = doGetBean(beanName);
			} else if (arg instanceof List) {
				value = parseListArg((List) arg);
			} else if (arg instanceof Map) {
				// 处理map
			} else if (arg instanceof Properties) {
				// 处理属性文件
			} else {
				value = arg;
			}
			args[i] = value;
		}
		return args;
	}

	private Constructor<?> matchConstructor(BeanDefinition bd, Object[] args) throws Exception {
		if (args == null) {
			return bd.getBeanClass().getConstructor(null);
		}
		if (bd.getConstructor() != null) {
			return bd.getConstructor();
		}

		int len = args.length;
		Class[] param = new Class[len];
		for (int i = 0; i < len; i++) {
			param[i] = args[i].getClass();
		}
		Constructor constructor = null;
		try {
			constructor = bd.getBeanClass().getConstructor(param);
		} catch (NullPointerException e) {
			// do nothing, continue
		}
		if (constructor != null) {
			return constructor;
		}

		List<Constructor> afterFirstFilter = new ArrayList<>();
		Constructor[] constructors = bd.getBeanClass().getConstructors();
		for (Constructor cons : constructors) {
			if (cons.getParameterCount() == len) {
				afterFirstFilter.add(cons);
			}
		}
		if (afterFirstFilter.size() == 1) {
			return afterFirstFilter.get(0);
		}

		if (afterFirstFilter.isEmpty()) {
			System.out.println("不存在对应构造函数：" + args);
			throw new Exception("No matching constructor: " + args);
		}

		boolean isMatch = true;
		for (int i = 0; i < afterFirstFilter.size(); i++) {
			Class[] types = afterFirstFilter.get(i).getParameterTypes();
			for (int j = 0; j < types.length; j++) {
				if (!types[j].isAssignableFrom(args[j].getClass())) {
					isMatch = false;
					break;
				}
			}
			if (isMatch) {
				if (bd.isPrototype()) {
					bd.setConstructor(afterFirstFilter.get(i));
				}
				return afterFirstFilter.get(i);
			}
		}
		throw new Exception("No matching constructor: " + args);
	}

	private List parseListArg(List arg) throws Exception {
		List param = new LinkedList<>();
		for (Object value : arg) {
			Object res = new Object();
			if (value instanceof BeanReference) {
				String beanName = ((BeanReference) value).getBeanName();
				res = doGetBean(beanName);
			} else if (value instanceof List) {
				res = parseListArg((List) value);
			} else if (value instanceof Map) {
				// 处理map
			} else if (value instanceof Properties) {
				// 处理属性文件
			} else {
				res = value;
			}
			param.add(res);
		}
		return param;
	}

	private void parsePropertyValues(BeanDefinition bd, Object instance) throws Exception {
		Map<String, Object> propertyKeyValue = bd.getPropertyKeyValue();
		if (propertyKeyValue == null || propertyKeyValue.isEmpty()) {
			return;
		}
		Class<?> instanceClass = instance.getClass();
		for (Map.Entry<String, Object> entry : propertyKeyValue.entrySet()) {
			Field field = instanceClass.getDeclaredField(entry.getKey());
			field.setAccessible(true);
			Object arg = entry.getValue();
			Object value = null;
			if (arg instanceof BeanReference) {
				String beanName = ((BeanReference) arg).getBeanName();
				value = this.doGetBean(beanName);
			} else if (arg instanceof List) {
				value = parseListArg((List) arg);
			} else if (arg instanceof Map) {
				// 处理map
			} else if (arg instanceof Properties) {
				// 处理属性文件
			} else {
				value = arg;
			}
			field.set(instance, value);
		}
	}

	@Override
	public void close() throws IOException {
		Set<Map.Entry<String, BeanDefinition>> entries = beanDefinitionMap.entrySet();
		for (Map.Entry<String, BeanDefinition> entry : entries) {
			BeanDefinition beanDefinition = entry.getValue();
			String destroyMethodName = beanDefinition.getBeanDestroyMethodName();
			try {
				Method method = beanDefinition.getBeanClass().getMethod(destroyMethodName, null);
				method.invoke(beanDefinition.getBeanClass(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void registerBeanPostProcessor(AopPostProcessor processor) {
		postProcessors.add(processor);
		System.out.println("Registering processor " + processor);
	}

	private Object applyAopBeanPostProcessor(Object instance, String beanName) throws Exception {
		System.out.println("Applying " + postProcessors.size() + " processors to " + beanName);
		for (AopPostProcessor processor : postProcessors) {
			instance = processor.postProcessWeaving(instance, beanName);
		}
		return instance;
	}

	@Override
	public List<String> getBeanNameForType(Class<?> clazz) {
		List<String> beanNames = new LinkedList<>();

		for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
			Class<?> beanClass = entry.getValue().getBeanClass();
			if (clazz.isAssignableFrom(beanClass)) {
				beanNames.add(entry.getKey());
			}
		}

		return beanNames;
	}

	@Override
	public Class<?> getType(String beanName) {
		if (beanDefinitionMap.containsKey(beanName)) {
			return beanDefinitionMap.get(beanName).getBeanClass();
		}
		return null;
	}

}
