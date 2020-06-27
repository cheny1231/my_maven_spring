package yangc.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import yangc.aop.advice.AroundAdvice;

public class MyAroundAdvice implements AroundAdvice {

	@Override
	public Object around(Method method, Object[] args, Object target)
			throws InvocationTargetException, IllegalAccessError {
		Object res = null;
		try {
			System.out.println("Before around...");
			res = method.invoke(target, args);
			System.out.println("After around...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
