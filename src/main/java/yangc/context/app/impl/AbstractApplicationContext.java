package yangc.context.app.impl;

import java.util.List;

import yangc.aop.creator.impl.AopProxyCreator;
import yangc.bean.factory.BeanFactory;
import yangc.bean.factory.impl.DefaultBeanFactory;
import yangc.bean.postProcessor.BeanPostProcessor;
import yangc.bean.postProcessor.impl.AutowiredAnnotationBeanPostProcessor;
import yangc.context.app.ApplicationContext;
import yangc.context.source.Resource;
import yangc.context.source.ResourceFactory;
import yangc.context.source.impl.ClassPathResource;
import yangc.context.source.impl.FileSystemResource;
import yangc.context.source.impl.UrlResource;

public abstract class AbstractApplicationContext implements ApplicationContext, ResourceFactory {
	protected BeanFactory factory;
	protected AopProxyCreator creator;
	protected AutowiredAnnotationBeanPostProcessor autowiredProcessor;

	public AbstractApplicationContext() {
		this.factory = new DefaultBeanFactory();
		this.creator = new AopProxyCreator();
		this.autowiredProcessor = new AutowiredAnnotationBeanPostProcessor();
		creator.setBeanFactory(factory);
		autowiredProcessor.setBeanFactory(factory);
		factory.registerBeanPostProcessor(creator);
		factory.registerBeanPostProcessor(autowiredProcessor);
	}

	@Override
	public Object getBean(String beanName) throws Exception {
		return factory.getBean(beanName);
	}

	@Override
	public void registerBeanPostProcessor(BeanPostProcessor processor) {
		factory.registerBeanPostProcessor(processor);
	}

	@Override
	public Resource getResource(String locs) throws Exception {
		if (locs.contains(":")) {
			String[] split = locs.split(":");
			StringBuilder sb = new StringBuilder();
			sb.append(split[1]);
			for (int i = 2; i < split.length; i++) {
				sb.append(":" + split[i]);
			}
			switch (split[0]) {
			case "url":
				return new UrlResource(sb.toString());
			case "classpath":
				return new ClassPathResource(null, sb.toString(), null);
			case "file":
				return new FileSystemResource(sb.toString());
			default:
				throw new IllegalArgumentException("Malformed config xml file");
			}
		}
		return null;
	}

	@Override
	public List<String> getBeanNameForType(Class<?> clazz) {
		return factory.getBeanNameForType(clazz);
	}

	@Override
	public Class<?> getType(String beanName) {
		return factory.getType(beanName);
	}
}
