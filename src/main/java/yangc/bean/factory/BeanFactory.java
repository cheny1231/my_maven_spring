package yangc.bean.factory;

import java.util.List;

import yangc.bean.postProcessor.AopPostProcessor;

public interface BeanFactory {
	Object getBean(String beanName) throws Exception;

	void registerBeanPostProcessor(AopPostProcessor processor);

	List<String> getBeanNameForType(Class<?> clazz);

	Class<?> getType(String beanName);
}
