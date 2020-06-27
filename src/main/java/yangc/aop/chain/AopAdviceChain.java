package yangc.aop.chain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import yangc.aop.advice.Advice;
import yangc.aop.advice.AfterAdvice;
import yangc.aop.advice.AroundAdvice;
import yangc.aop.advice.BeforeAdvice;

public class AopAdviceChain {
	private List<Advice> advices;
	private Method method;
	private Method nextMethod;
	private Object target;
	private Object[] args;

	private int index = 0;

	public AopAdviceChain(List<Advice> advices, Method method, Object target, Object[] args) {
		System.out.println("Creating AdviceChain...");
		try {
			nextMethod = AopAdviceChain.class.getMethod("invoke", null);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		this.advices = advices;
		this.method = method;
		this.target = target;
		this.args = args;
	}

	public Object invoke() throws InvocationTargetException, IllegalAccessException {
		if (index < advices.size()) {
			Advice advice = advices.get(index++);
			System.out.println("Advice chain invoked for " + advice + "!");
			if (advice instanceof BeforeAdvice) {
				((BeforeAdvice) advice).before(method, args, target);
			} else if (advice instanceof AroundAdvice) {
				return ((AroundAdvice) advice).around(nextMethod, null, this);
			} else if (advice instanceof AfterAdvice) {
				Object res = this.invoke();
				((AfterAdvice) advice).after(method, args, target, res);
				return res;
			}
			return this.invoke();
		} else {
			return method.invoke(target, args);
		}

	}
}
