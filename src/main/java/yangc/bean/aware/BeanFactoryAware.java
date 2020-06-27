package yangc.bean.aware;

import yangc.bean.factory.BeanFactory;

public interface BeanFactoryAware extends Aware {
	void setBeanFactory(BeanFactory factory);
}
