package yangc.aop.advisor;

import yangc.aop.pointcut.PointCut;

public interface PointCutAdvisor extends Advisor {
	PointCut getPointCutResolver();
}
