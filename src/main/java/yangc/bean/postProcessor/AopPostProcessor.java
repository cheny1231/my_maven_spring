package yangc.bean.postProcessor;

public interface AopPostProcessor extends BeanPostProcessor {
	Object postProcessWeaving(Object bean, String beanName) throws Exception;
}
