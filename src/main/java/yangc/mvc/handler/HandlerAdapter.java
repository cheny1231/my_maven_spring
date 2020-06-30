package yangc.mvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
	Object handler(HttpServletRequest request, HttpServletResponse response);

	boolean isMatch(HttpServletRequest request);
}
