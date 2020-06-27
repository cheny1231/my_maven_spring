package yangc.context.reader.impl;

import yangc.aop.creator.impl.AopProxyCreator;
import yangc.bean.definition.BeanDefinitionRegistry;
import yangc.context.reader.AdviceDefinitionReader;
import yangc.context.reader.BeanDefinitionReader;

public abstract class AbstractDefinitionReader implements BeanDefinitionReader, AdviceDefinitionReader {
	protected BeanDefinitionRegistry registry;
	protected AopProxyCreator creator;

	public AbstractDefinitionReader(BeanDefinitionRegistry registry, AopProxyCreator creator) {
		this.registry = registry;
		this.creator = creator;
	}
}
