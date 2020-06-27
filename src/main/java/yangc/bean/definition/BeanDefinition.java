package yangc.bean.definition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface BeanDefinition {
	String Singleton = "Singleton";
	String Prototype = "Prototype";

	Class getBeanClass();

	String getBeanFactory();

	String getCreateBeanMethod();

	String getStaticCreateBeanMethod();

	String getBeanInitMethod();

	String getScope();

	String getBeanDestroyMethodName();

	boolean isSingleton();

	boolean isPrototype();

	List<?> getConstructorArgument();

	void setConstructorArgument(List<?> args);

	Constructor<?> getConstructor();

	void setConstructor(Constructor<?> constructor);

	Method getFacoryMethod();

	void setFactoryMethod(Method method);

	Map<String, Object> getPropertyKeyValue();

	void setPropertyKeyValue(Map<String, Object> properties);

	default boolean validate() {
		if (getBeanClass() == null) {
			if (getBeanFactory() == null && getCreateBeanMethod() == null) {
				return false;
			}
		}
		return true;
	}

}
