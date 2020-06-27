package yangc.bean.factory;

import yangc.bean.postProcessor.AopPostProcessor;

public interface BeanFactory {
	Object getBean(String beanName) throws Exception;

	void registerBeanPostProcessor(AopPostProcessor processor);
}
