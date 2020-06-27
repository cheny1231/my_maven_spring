package yangc.aop.creator;

import java.util.List;

import yangc.aop.advisor.Advisor;
import yangc.aop.creator.impl.GenericAopFactory;
import yangc.aop.proxy.AopProxy;
import yangc.bean.factory.BeanFactory;

public interface AopFactory {
	AopProxy createAopProxyInstance(Object target, List<Advisor> advisors, BeanFactory factory, String beanName);

	default boolean judgeUseWhichProxyMode(Object target) {
		Class<?>[] interfaces = target.getClass().getInterfaces();
		// 类实现接口用jdk, 否则cglib
		return interfaces.length > 0;
	}

	static AopFactory createProxyInstance() {
		return new GenericAopFactory();
	}
}
