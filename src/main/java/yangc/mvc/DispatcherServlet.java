package yangc.mvc;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yangc.context.app.ApplicationContext;
import yangc.context.app.impl.AnnotationApplicationContext;
import yangc.mvc.annotation.RequestParam;
import yangc.mvc.controller.RequestMappingInfo;
import yangc.mvc.handler.HandlerAdapter;
import yangc.mvc.handler.HandlerMapping;
import yangc.mvc.handler.RequestMappingHandlerAdaptor;
import yangc.mvc.handler.RequestMappingHandlerMapping;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = -999413648395740572L;

	private ApplicationContext context;
	private List<HandlerMapping> handlerMappings;
	private List<HandlerAdapter> handlerAdapters;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			context = new AnnotationApplicationContext("classpath:applicationContext.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}

		initHandlerAdapter();
		initHandlerMapping();
	}

	private void initHandlerMapping() {
		handlerMappings = new ArrayList<>();
		RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
		mapping.setApplicationContext(context);
		mapping.afterPropertiesSet();
		handlerMappings.add(mapping);

	}

	private void initHandlerAdapter() {
		handlerAdapters = new ArrayList<>();
		RequestMappingHandlerAdaptor adaptor = new RequestMappingHandlerAdaptor();

	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doDispatcher(req, resp);
	}

	private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		RequestMappingInfo info = getMappinginfo(req);
		if (info == null) {
			noHandlerFound(req, resp);
		}
		Object[] args = parseArgs(req, resp, info.getMethod());
		try {
			info.getMethod().invoke(info.getHandler(), args);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	private RequestMappingInfo getMappinginfo(HttpServletRequest req) {
		for (HandlerMapping mapping : handlerMappings) {
			RequestMappingInfo handler = mapping.getHandler(req);
			if (handler != null) {
				return handler;
			}
		}
		return null;
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
