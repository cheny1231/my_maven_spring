package yangc.aop.proxy.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import yangc.aop.advisor.Advisor;
import yangc.aop.proxy.AopProxy;
import yangc.bean.definition.BeanDefinition;
import yangc.bean.factory.BeanFactory;
import yangc.bean.factory.impl.DefaultBeanFactory;
import yangc.utils.AopUtils;

public class CglibDynamicProxy implements AopProxy, MethodInterceptor {

	private Enhancer enhancer = new Enhancer();
	private Object target;
	private List<Advisor> advisors;
	private BeanFactory factory;
	private String beanName;

	public CglibDynamicProxy(Object target, List<Advisor> advisors, BeanFactory factory, String beanName) {
		System.out.println("Creating CglibDynamicProxy for " + advisors.size() + " advisors...");
		this.target = target;
		this.advisors = advisors;
		this.factory = factory;
		this.beanName = beanName;
	}

	@Override
	public Object getProxy() {
		return getProxy(target.getClass().getClassLoader());
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		System.out.println("Creating cglib proxy for " + target);

		enhancer.setSuperclass(target.getClass());
		enhancer.setClassLoader(classLoader);
		enhancer.setInterfaces(target.getClass().getInterfaces());
		enhancer.setCallback(this);

		Constructor<?>[] constructors = target.getClass().getConstructors();
		if (constructors.length > 0) {
			BeanDefinition bd = ((DefaultBeanFactory) factory).getBeanDefinition(beanName);
			if (bd.getConstructor() != null) {
				return enhancer.create(bd.getConstructor().getParameterTypes(), bd.getConstructorArgument().toArray());
			}
		}
		return enhancer.create();
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		System.out.println("Invoking CglibDynamicProxy for " + method + "...");
		return AopUtils.applyAdvice(target, proxy, advisors, args, method, factory);
	}

}
