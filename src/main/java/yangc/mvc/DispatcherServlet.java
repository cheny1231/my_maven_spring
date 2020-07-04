package yangc.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yangc.context.app.ApplicationContext;
import yangc.context.app.impl.AnnotationApplicationContext;
import yangc.mvc.controller.RequestMappingInfo;
import yangc.mvc.handler.HandlerAdaptor;
import yangc.mvc.handler.HandlerMapping;
import yangc.mvc.handler.RequestMappingHandlerAdaptor;
import yangc.mvc.handler.RequestMappingHandlerMapping;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = -999413648395740572L;

	private ApplicationContext context;
	private List<HandlerMapping> handlerMappings;
	private List<HandlerAdaptor> handlerAdaptors;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			context = new AnnotationApplicationContext("classpath:ApplicationContext.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}

		initHandlerAdapter();
		initHandlerMapping();
	}

	private void initHandlerMapping() {
		System.out.println("Initing handler mapping...");
		handlerMappings = new ArrayList<>();
		RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
		mapping.setApplicationContext(context);
		mapping.afterPropertiesSet();
		handlerMappings.add(mapping);

	}

	private void initHandlerAdapter() {
		handlerAdaptors = new ArrayList<>();
		RequestMappingHandlerAdaptor adaptor = new RequestMappingHandlerAdaptor();
		handlerAdaptors.add(adaptor);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doDispatcher(req, resp);
	}

	private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println("Dispatching request: " + req);
		RequestMappingInfo info = getMappinginfo(req);
		HandlerAdaptor adaptor = this.getAdaptor(req);
		if (info == null || adaptor == null) {
			noHandlerFound(req, resp);
		}
		adaptor.setRequestMappingInfo(info);
		adaptor.handle(req, resp);
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

	private HandlerAdaptor getAdaptor(HttpServletRequest req) {
		for (HandlerAdaptor adaptor : handlerAdaptors) {
			if (adaptor.isMatch(req)) {
				return adaptor;
			}
		}
		return null;
	}

}
