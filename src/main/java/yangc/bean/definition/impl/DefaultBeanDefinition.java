package yangc.bean.definition.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import yangc.bean.definition.BeanDefinition;

public class DefaultBeanDefinition implements BeanDefinition {
	private Class<?> clazz;
	private String beanFactoryName;
	private String createBeanMethodName;
	private String staticCreateBeanMethodName;
	private String beanInitMethodName;
	private String beanDestroyMethodName;
	private boolean isSingleton;
	private List<?> constructorArgs;
	private Method factoryMethod;
	private Constructor constructor;
	private Map<String, Object> properties;

	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getBeanFactoryName() {
		return beanFactoryName;
	}

	public void setBeanFactoryName(String beanFactoryName) {
		this.beanFactoryName = beanFactoryName;
	}

	public String getCreateBeanMethodName() {
		return createBeanMethodName;
	}

	public void setCreateBeanMethodName(String createBeanMethodName) {
		this.createBeanMethodName = createBeanMethodName;
	}

	public String getStaticCreateBeanMethodName() {
		return staticCreateBeanMethodName;
	}

	public void setStaticCreateBeanMethodName(String staticCreateBeanMethodName) {
		this.staticCreateBeanMethodName = staticCreateBeanMethodName;
	}

	public String getBeanInitMethodName() {
		return beanInitMethodName;
	}

	public void setBeanInitMethodName(String beanInitMethodName) {
		this.beanInitMethodName = beanInitMethodName;
	}

	public void setBeanDestroyMethodName(String beanDestroyMethodName) {
		this.beanDestroyMethodName = beanDestroyMethodName;
	}

	@Override
	public Class<?> getBeanClass() {
		return clazz;
	}

	@Override
	public String getBeanFactory() {
		return beanFactoryName;
	}

	@Override
	public String getCreateBeanMethod() {
		return createBeanMethodName;
	}

	@Override
	public String getStaticCreateBeanMethod() {
		return staticCreateBeanMethodName;
	}

	@Override
	public String getBeanInitMethod() {
		return beanInitMethodName;
	}

	@Override
	public String getScope() {
		return isSingleton ? BeanDefinition.Singleton : BeanDefinition.Prototype;
	}

	@Override
	public boolean isSingleton() {
		return isSingleton;
	}

	@Override
	public boolean isPrototype() {
		return !isSingleton;
	}

	@Override
	public String getBeanDestroyMethodName() {
		return beanDestroyMethodName;
	}

	@Override
	public List<?> getConstructorArgument() {
		return constructorArgs;
	}

	@Override
	public void setConstructorArgument(List<?> args) {
		constructorArgs = args;
	}

	@Override
	public Constructor<?> getConstructor() {
		return constructor;
	}

	@Override
	public void setConstructor(Constructor<?> constructor) {
		this.constructor = constructor;
	}

	@Override
	public Method getFacoryMethod() {
		return factoryMethod;
	}

	@Override
	public void setFactoryMethod(Method method) {
		this.factoryMethod = method;
	}

	@Override
	public Map<String, Object> getPropertyKeyValue() {
		return properties;
	}

	@Override
	public void setPropertyKeyValue(Map<String, Object> properties) {
		this.properties = properties;

	}

}
