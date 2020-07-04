package yangc.mvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yangc.mvc.controller.RequestMappingInfo;

public interface HandlerAdaptor {
	void setRequestMappingInfo(RequestMappingInfo info);

	void handle(HttpServletRequest request, HttpServletResponse response);

	boolean isMatch(HttpServletRequest request);
}
