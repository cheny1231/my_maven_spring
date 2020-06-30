package yangc.mvc.handler;

import javax.servlet.http.HttpServletRequest;

import yangc.mvc.controller.RequestMappingInfo;

public interface HandlerMapping {
	RequestMappingInfo getHandler(HttpServletRequest request);
}
