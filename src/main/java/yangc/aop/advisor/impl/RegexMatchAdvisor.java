package yangc.aop.advisor.impl;

import yangc.aop.advisor.PointCutAdvisor;
import yangc.aop.pointcut.PointCut;

public class RegexMatchAdvisor implements PointCutAdvisor {

	private String adviceName;
	private String expression;
	private PointCut pointCut;

	public RegexMatchAdvisor(String adviceName, String expression, PointCut pointCut) {
		this.adviceName = adviceName;
		this.expression = expression;
		this.pointCut = pointCut;
	}

	@Override
	public PointCut getPointCutResolver() {
		return pointCut;
	}

	@Override
	public String getAdviceBeanName() {
		return adviceName;
	}

	@Override
	public String getExpression() {
		return expression;
	}

}
