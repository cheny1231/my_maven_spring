package yangc.bean.postProcessor;

import yangc.bean.factory.BeanFactory;

public interface AnnotationBeanPostProcessor extends BeanPostProcessor {
	void setBeanFactory(BeanFactory factory);
}
