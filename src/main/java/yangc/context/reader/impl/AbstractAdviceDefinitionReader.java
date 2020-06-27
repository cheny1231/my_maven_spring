package yangc.context.reader.impl;

import yangc.aop.creator.impl.AopProxyCreator;
import yangc.context.reader.AdviceDefinitionReader;

public abstract class AbstractAdviceDefinitionReader implements AdviceDefinitionReader {
	protected AopProxyCreator creator;

	public AbstractAdviceDefinitionReader(AopProxyCreator creator) {
		this.creator = creator;
	}
}
