package yangc.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import yangc.aop.advice.Advice;
import yangc.aop.advisor.Advisor;
import yangc.aop.advisor.PointCutAdvisor;
import yangc.aop.chain.AopAdviceChain;
import yangc.bean.factory.BeanFactory;

public class AopUtils {
	public static Object applyAdvice(Object target, Object proxy, List<Advisor> advisors, Object[] args, Method method,
			BeanFactory factory) throws Exception {
		List<Advice> advices = getMatchedMethodAdvice(method, advisors, target.getClass(), factory);
		System.out.println("Got " + advices.size() + " advices...");
		if (advices.isEmpty()) {
			return method.invoke(target, args);
		}
		return new AopAdviceChain(advices, method, target, args).invoke();
	}

	public static List<Advice> getMatchedMethodAdvice(Method method, List<Advisor> advisors, Class<?> clazz,
			BeanFactory factory) throws Exception {
		if (advisors == null || advisors.isEmpty()) {
			return null;
		}
		List<Advice> advices = new ArrayList<Advice>();
		for (Advisor advisor : advisors) {
			System.out.println("Matching advice for advisor " + advisor + "...");
			if (advisor instanceof PointCutAdvisor) {
				PointCutAdvisor pointCutAdvisor = (PointCutAdvisor) advisor;
				if (pointCutAdvisor.getPointCutResolver().matchesMethod(clazz, method, advisor.getExpression())) {
					advices.add((Advice) factory.getBean(advisor.getAdviceBeanName()));
					System.out.println(advisor.getAdviceBeanName() + " Matched!");
				}
			}
		}
		return advices;
	}
}
