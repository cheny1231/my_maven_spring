package yangc.aop.proxy;

import java.util.ArrayList;
import java.util.List;

import yangc.aop.advisor.Advisor;
import yangc.aop.advisor.PointCutAdvisor;

public interface AopProxy {
	Object getProxy();

	Object getProxy(ClassLoader classLoader);

	default List<Advisor> getMatchAdvisors(Class<?> clazz, List<Advisor> advisors) throws Exception {
		List<Advisor> matches = new ArrayList<Advisor>();

		for (Advisor advisor : advisors) {
			if (advisor instanceof PointCutAdvisor) {
				PointCutAdvisor pointCutAdvisor = (PointCutAdvisor) advisor;
				if (pointCutAdvisor.getPointCutResolver().matchesClass(clazz, pointCutAdvisor.getExpression())) {
					matches.add(advisor);
				}
			}
		}

		return matches;
	}
}
