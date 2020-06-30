package yangc.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
	void render(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model);
}
