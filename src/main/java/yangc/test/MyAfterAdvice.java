package yangc.test;

import java.lang.reflect.Method;

import yangc.aop.advice.AfterAdvice;

public class MyAfterAdvice implements AfterAdvice {

	@Override
	public void after(Method method, Object[] args, Object target, Object returnValue) {
		System.out.println("Say more after Hello!");
	}

}
