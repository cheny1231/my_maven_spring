package yangc.context.reader.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import yangc.aop.advisor.impl.RegexMatchAdvisor;
import yangc.aop.creator.impl.AopProxyCreator;
import yangc.aop.pointcut.impl.RegexExpressionPointCutResolver;
import yangc.bean.definition.BeanDefinitionRegistry;
import yangc.bean.definition.impl.DefaultBeanDefinition;
import yangc.context.annotation.Component;
import yangc.context.annotation.Controller;
import yangc.context.annotation.PointCut;
import yangc.context.source.Resource;

public class AnnotationDefinitionReader extends AbstractDefinitionReader {
	public static final String SCAN_PACKAGE = "scanPackage";
	private Properties config = new Properties();

	public AnnotationDefinitionReader(BeanDefinitionRegistry registry, AopProxyCreator creator) {
		super(registry, creator);
	}

	@Override
	public void loadDefinition(Resource resource) throws Exception {
		parse(resource);
	}

	@Override
	public void loadDefinition(Resource... resources) throws Exception {
		for (Resource resource : resources) {
			parse(resource);
		}
	}

	private void parse(Resource resource) throws Exception {
		InputStream inputStream = resource.getInputStream();
		config.load(inputStream);
		String packageName = config.getProperty(SCAN_PACKAGE);
		doRegister(packageName);
	}

	private void doRegister(String packageName) throws Exception {
		URL url = getClass().getClassLoader().getResource("./" + packageName.replaceAll("\\.", "/"));
		File dir = new File(url.getFile());
		// find all the java file
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				doRegister(packageName + "." + file.getName());
			} else {
				String className = packageName + "." + file.getName().replaceAll(".class", "").trim();
				Class<?> clazz = Class.forName(className);
				String id = generateId(clazz);
				if (id != null) {
					registerBeanDefinition(clazz, id);
					checkAndRegisterAdvice(clazz, id);
				}

			}
		}
	}

	private void registerBeanDefinition(Class<?> clazz, String id) {
		DefaultBeanDefinition bd = new DefaultBeanDefinition();
		bd.setClazz(clazz);
		registry.register(bd, id);
	}

	private void checkAndRegisterAdvice(Class<?> clazz, String id) {
		if (clazz.isAnnotationPresent(PointCut.class)) {
			PointCut pointCut = (PointCut) clazz.getAnnotation(PointCut.class);
			String value = pointCut.value();
			creator.registerAdvisor(new RegexMatchAdvisor(id, value, new RegexExpressionPointCutResolver()));
		}
	}

	private String generateId(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Controller.class)) {
			return clazz.getName();
		}
		if (clazz.isAnnotationPresent(Component.class)) {
			Component component = (Component) clazz.getAnnotation(Component.class);
			String value = component.value();
			if (value.isEmpty()) {
				value = clazz.getName();
			}
			return value;
		}
		return null;
	}

}
