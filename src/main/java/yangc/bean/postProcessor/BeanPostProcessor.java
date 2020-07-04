package yangc.bean.postProcessor;

public interface BeanPostProcessor {
	Object postProcessAfterInitialization(Object bean, String beanName) throws Exception;
}
