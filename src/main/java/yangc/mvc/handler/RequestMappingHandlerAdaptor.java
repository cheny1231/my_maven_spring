package yangc.mvc.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yangc.mvc.annotation.RequestMethod;
import yangc.mvc.annotation.RequestParam;
import yangc.mvc.controller.RequestMappingInfo;

public class RequestMappingHandlerAdaptor implements HandlerAdaptor {
	private RequestMappingInfo info;

	@Override
	public void setRequestMappingInfo(RequestMappingInfo info) {
		this.info = info;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Object[] args = parseArgs(request, response, info.getMethod());
		Object value = null;
		try {
			value = info.getMethod().invoke(info.getHandler(), args);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		try {
			Class<?> returnType = info.getMethod().getReturnType();
			if (returnType == String.class && value != null) {
				PrintWriter pw = response.getWriter();
				pw.write((String) value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isMatch(HttpServletRequest request) {
		return request.getMethod() == RequestMethod.GET.name();
	}

	private Object[] parseArgs(HttpServletRequest req, HttpServletResponse resp, Method method) {
		Class<?>[] paramClasses = method.getParameterTypes();
		Object[] args = new Object[paramClasses.length];
		int args_index = 0;
		for (int i = 0; i < paramClasses.length; i++) {
			if (ServletRequest.class.isAssignableFrom(paramClasses[i])) {
				args[args_index++] = req;
			}
			if (ServletResponse.class.isAssignableFrom(paramClasses[i])) {
				args[args_index++] = resp;
			}
			Annotation[] paramAns = method.getParameterAnnotations()[i];
			if (paramAns.length > 0) {
				for (Annotation annotation : paramAns) {
					if (RequestParam.class.isAssignableFrom(annotation.getClass())) {
						RequestParam rp = (RequestParam) annotation;
						args[args_index++] = req.getParameter(rp.value());
					}
				}
			}
		}
		return args;
	}

}
