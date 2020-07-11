package yangc.test;

import java.lang.reflect.Method;

import yangc.aop.advice.BeforeAdvice;
import yangc.context.annotation.Component;
import yangc.context.annotation.PointCut;

@PointCut("execution(* yangc.test.UserInterface.*())")
@Component
public class MyBeforeAdvice implements BeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) {
		System.out.println("Say something before Hello world...");
	}

}
