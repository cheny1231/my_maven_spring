package yangc.aop.pointcut;

import java.lang.reflect.Method;

public interface PointCut {
	boolean matchesClass(Class<?> targetClass, String expression) throws Exception;

	boolean matchesMethod(Class<?> targetClass, Method method, String expression) throws Exception;
}
