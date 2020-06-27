package yangc.aop.creator.impl;

import java.util.ArrayList;
import java.util.List;

import yangc.aop.advisor.Advisor;
import yangc.aop.advisor.AdvisorRegistry;
import yangc.aop.advisor.PointCutAdvisor;
import yangc.aop.creator.AopFactory;
import yangc.bean.aware.BeanFactoryAware;
import yangc.bean.factory.BeanFactory;
import yangc.bean.postProcessor.AopPostProcessor;

public class AopProxyCreator implements AopPostProcessor, BeanFactoryAware, AdvisorRegistry {
	private BeanFactory factory;
	private List<Advisor> advisors;

	public AopProxyCreator() {
		advisors = new ArrayList<Advisor>();
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerAdvisor(Advisor advisor) {
		advisors.add(advisor);
		System.out.println("Registering advisor...");

	}

	@Override
	public List<Advisor> getAdvisors() {
		return advisors;
	}

	@Override
	public void setBeanFactory(BeanFactory factory) {
		this.factory = factory;

	}

	@Override
	public Object postProcessWeaving(Object bean, String beanName) throws Exception {
		List<Advisor> matchAdvisors = getMatchAdvisor(bean);
		if (!matchAdvisors.isEmpty()) {
			bean = AopFactory.createProxyInstance().createAopProxyInstance(bean, matchAdvisors, factory, beanName)
					.getProxy();
		}
		return bean;
	}

	private List<Advisor> getMatchAdvisor(Object bean) throws Exception {
		List<Advisor> matchAdvisors = new ArrayList<>();

		if (advisors.isEmpty() || bean == null) {
			return matchAdvisors;
		}

		Class<?> clazz = bean.getClass();
		System.out.println("Matching advisors for " + clazz + "...");
		for (Advisor advisor : advisors) {
			System.out.print("Matching " + advisor + "... ");
			if (advisor instanceof PointCutAdvisor) {
				if (((PointCutAdvisor) advisor).getPointCutResolver().matchesClass(clazz, advisor.getExpression())) {
					matchAdvisors.add(advisor);
					System.out.print("Matched!");
				}
			}
			System.out.println();
		}
		return matchAdvisors;
	}

}
