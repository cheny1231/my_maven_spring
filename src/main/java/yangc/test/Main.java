package yangc.test;

import yangc.aop.advisor.impl.RegexMatchAdvisor;
import yangc.aop.creator.impl.AopProxyCreator;
import yangc.aop.pointcut.impl.RegexExpressionPointCutResolver;
import yangc.bean.definition.impl.DefaultBeanDefinition;
import yangc.bean.factory.impl.DefaultBeanFactory;
import yangc.context.app.ApplicationContext;
import yangc.context.app.impl.AnnotationApplicationContext;

public class Main {
	static DefaultBeanFactory factory = new DefaultBeanFactory();

	public static void main(String[] args) throws Exception {
		testAnnotation();
	}

	private static void testAnnotation() throws Exception {
		ApplicationContext applicationContext = new AnnotationApplicationContext(
				"classpath:applicationContext.properties");
		User user = (User) applicationContext.getBean("yangc.test.User");
		user.sayHello();
	}

	private static void testAop() throws Exception {
		DefaultBeanDefinition bd = new DefaultBeanDefinition();
		bd.setClazz(User.class);
		bd.setSingleton(true);
		bd.setBeanDestroyMethodName("destroy");
		bd.setBeanFactoryName("UserFactory");
		bd.setBeanInitMethodName("init");
		bd.setCreateBeanMethodName("createMethod");
		bd.setStaticCreateBeanMethodName("staticCreateMethod");
		factory.register(bd, "user");

		bd = new DefaultBeanDefinition();
		bd.setClazz(MyBeforeAdvice.class);
		factory.register(bd, "myBeforeAdvice");

		bd = new DefaultBeanDefinition();
		bd.setClazz(MyAroundAdvice.class);
		factory.register(bd, "myAroundAdvice");

		bd = new DefaultBeanDefinition();
		bd.setClazz(MyAfterAdvice.class);
		factory.register(bd, "myAfterAdvice");

		AopProxyCreator apc = new AopProxyCreator();
		apc.setBeanFactory(factory);
		factory.registerBeanPostProcessor(apc);

		apc.registerAdvisor(new RegexMatchAdvisor("myBeforeAdvice", "execution(* yangc.test.User.*())",
				new RegexExpressionPointCutResolver()));
		apc.registerAdvisor(new RegexMatchAdvisor("myAroundAdvice", "execution(* yangc.test.User.*())",
				new RegexExpressionPointCutResolver()));
		apc.registerAdvisor(new RegexMatchAdvisor("myAfterAdvice", "execution(* yangc.test.User.*())",
				new RegexExpressionPointCutResolver()));

		User user = (User) factory.doGetBean("user");
		user.sayHello();
	}

}
