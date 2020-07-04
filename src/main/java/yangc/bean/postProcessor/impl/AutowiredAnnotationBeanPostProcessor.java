package yangc.bean.postProcessor.impl;

import java.lang.reflect.Field;
import java.util.List;

import yangc.bean.factory.BeanFactory;
import yangc.bean.postProcessor.AnnotationBeanPostProcessor;
import yangc.context.annotation.Autowired;

public class AutowiredAnnotationBeanPostProcessor implements AnnotationBeanPostProcessor {
	private BeanFactory factory;

	@Override
	public void setBeanFactory(BeanFactory factory) {
		this.factory = factory;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		Class<?> beanType = factory.getType(beanName);
		System.out.println("Processing autowired annotation for " + beanType);
		Field[] fields = beanType.getDeclaredFields();
		System.out.println("Found fields " + fields.length);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Autowired.class)) {
				System.out.println("Field " + field + " has @Autowired!");
				List<String> fieldBeanNames = factory.getBeanNameForType(field.getType());
				if (fieldBeanNames.size() != 1) {
					throw new Exception("Not able to find a single matching autowired component");
				}
				Object fieldBean = factory.getBean(fieldBeanNames.get(0));
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				field.set(bean, fieldBean);
			}
		}
		return bean;
	}

}
