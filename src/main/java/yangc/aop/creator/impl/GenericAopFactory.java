package yangc.aop.creator.impl;

import java.util.List;

import yangc.aop.advisor.Advisor;
import yangc.aop.creator.AopFactory;
import yangc.aop.proxy.AopProxy;
import yangc.aop.proxy.impl.CglibDynamicProxy;
import yangc.aop.proxy.impl.JDKDynamicProxy;
import yangc.bean.factory.BeanFactory;

public class GenericAopFactory implements AopFactory {

	@Override
	public AopProxy createAopProxyInstance(Object target, List<Advisor> advisors, BeanFactory factory,
			String beanName) {
		boolean res = judgeUseWhichProxyMode(target);
		if (res) {
			return new JDKDynamicProxy(target, advisors, factory);
		} else {
			return new CglibDynamicProxy(target, advisors, factory, beanName);
		}
	}

}
