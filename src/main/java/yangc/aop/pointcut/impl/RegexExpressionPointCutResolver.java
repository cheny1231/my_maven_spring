package yangc.aop.pointcut.impl;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import yangc.aop.pointcut.RegexExpressionPointCut;
import yangc.utils.RegexMatchUtils;

public class RegexExpressionPointCutResolver implements RegexExpressionPointCut {

	@Override
	public boolean matchesClass(Class<?> targetClass, String expression) throws Exception {
		expression.replace(".", "\\.");
		expression.replace("*", ".*");

		String className = RegexMatchUtils.matchClassName(expression);

		String name = targetClass.getName();
		if (Pattern.matches(className, name)) {
			return true;
		}
		Class<?>[] interfaces = targetClass.getInterfaces();
		for (Class<?> interfaze : interfaces) {
			if (Pattern.matches(className, interfaze.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean matchesMethod(Class<?> targetClass, Method method, String expression) throws Exception {
		if (!matchesClass(targetClass, expression)) {
			return false;
		}
		String matchName = RegexMatchUtils.matchMethodName(expression);
		String methodName = method.getName();
		if ("*".equals(matchName)) {
			return true;
		}
		return Pattern.matches(matchName, methodName);
	}

}
