package yangc.aop.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import yangc.aop.advisor.Advisor;
import yangc.aop.proxy.AopProxy;
import yangc.bean.factory.BeanFactory;
import yangc.utils.AopUtils;

public class JDKDynamicProxy implements AopProxy, InvocationHandler {
	private Object target;
	private List<Advisor> advisors;
	private BeanFactory factory;

	public JDKDynamicProxy(Object target, List<Advisor> advisors, BeanFactory factory) {
		this.target = target;
		this.advisors = advisors;
		this.factory = factory;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("Invoking JDKDynamicProxy for " + method + "...");
		return AopUtils.applyAdvice(target, proxy, getMatchAdvisors(target.getClass(), advisors), args, method,
				factory);
	}

	@Override
	public Object getProxy() {
		return getProxy(target.getClass().getClassLoader());
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		System.out.println("Creating JDK proxy for " + target);
		return Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), this);
	}

}
