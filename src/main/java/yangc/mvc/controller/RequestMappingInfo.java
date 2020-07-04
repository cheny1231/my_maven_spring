package yangc.mvc.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import yangc.mvc.annotation.RequestMapping;

public class RequestMappingInfo {
	private RequestMapping methodRequestMapping;
	private String beanName;
	private Method method;
	private Object handler;

	public RequestMapping getMethodRequestMapping() {
		return methodRequestMapping;
	}

	public void setMethodRequestMapping(RequestMapping methodRequestMapping) {
		this.methodRequestMapping = methodRequestMapping;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getHandler() {
		return handler;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}

	public boolean isMatch(HttpServletRequest request) {
		if (!request.getMethod().equals(methodRequestMapping.method().name())) {
			return false;
		}
		if (!request.getServletPath().equals(methodRequestMapping.path())) {
			return false;
		}
		String queryString = request.getQueryString();
		if (queryString == null) {
			return method.getParameterCount() == 0;
		}
		String[] queries = queryString.split("&");
		if (queries.length != method.getParameterCount()) {
			return false;
		}
		return true;
	}
}
