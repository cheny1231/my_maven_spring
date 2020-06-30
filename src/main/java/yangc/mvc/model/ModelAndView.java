package yangc.mvc.model;

import java.util.Map;

import yangc.mvc.view.View;

public class ModelAndView {
	private Map<String, Object> model;
	private Object view;
	private HttpStatus status;

	public ModelAndView() {

	}

	public ModelAndView(Map<String, Object> model, View view) {
		super();
		this.model = model;
		this.view = view;
	}

	public ModelAndView(View view, HttpStatus status) {
		super();
		this.view = view;
		this.status = status;
	}

	public ModelAndView(Map<String, Object> model, View view, HttpStatus status) {
		super();
		this.model = model;
		this.view = view;
		this.status = status;
	}

	public ModelAndView(Map<String, Object> model, String view) {
		super();
		this.model = model;
		this.view = view;
	}

	public ModelAndView(String view, HttpStatus status) {
		super();
		this.view = view;
		this.status = status;
	}

	public ModelAndView(View view) {
		this.view = view;
	}

	public ModelAndView(String viewName) {
		this.view = viewName;
	}

	public boolean hasView() {
		return view != null;
	}

	public void setAttr(String key, Object value) {
		model.put(key, value);
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public Object getView() {
		return view;
	}

	public void setView(Object view) {
		this.view = view;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
