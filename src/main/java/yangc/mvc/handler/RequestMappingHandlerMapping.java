package yangc.mvc.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import yangc.bean.aware.ApplicationContextAware;
import yangc.context.app.ApplicationContext;
import yangc.mvc.annotation.Controller;
import yangc.mvc.annotation.RequestMapping;
import yangc.mvc.controller.RequestMappingInfo;

public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware, InitializingBean {
	private ApplicationContext context;
	private List<RequestMappingInfo> requestMappingInfos = new ArrayList<>();
	private Map<String, List<RequestMappingInfo>> urlMaps = new ConcurrentHashMap<>();

	@Override
	public RequestMappingInfo getHandler(HttpServletRequest request) {
		System.out.println("Getting handler for " + request);
		String url = request.getServletPath();
		for (RequestMappingInfo info : urlMaps.get(url)) {
			System.out.println("Found mapping info: " + info);
			if (info.isMatch(request)) {
				return info;
			}
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public void afterPropertiesSet() {
		List<String> beanNameForType = context.getBeanNameForType(Object.class);
		System.out.println("Got bean names: ");
		for (String beanName : beanNameForType) {
			System.out.println(beanName);
		}
		System.out.println("End of bean names.");
		for (String beanName : beanNameForType) {
			Class<?> type = context.getType(beanName);
			if (isController(type)) {
				detectHandlerMethod(type, beanName);
			}
		}
	}

	private void detectHandlerMethod(Class<?> clazz, String beanName) {
		System.out.println("Detecting handler method for " + beanName);
		Object bean = null;
		try {
			bean = context.getBean(beanName);
		} catch (Exception e) {
			System.out.println("Controller not registered as bean: " + beanName);
			e.printStackTrace();
		}
		Method[] methods = clazz.getMethods();
		System.out.println("Found methods : " + methods[0].getName() + " for " + beanName);
		System.out.println("Got bean: " + bean);
		for (Method method : methods) {
			if (method.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
				String path = requestMapping.path();
				RequestMappingInfo info = new RequestMappingInfo();
				info.setBeanName(beanName);
				info.setMethod(method);
				info.setMethodRequestMapping(requestMapping);
				info.setHandler(bean);
				if (!urlMaps.containsKey(path)) {
					urlMaps.put(path, new ArrayList<>());
				}
				System.out.println("Putting mappingInfo for " + path);
				urlMaps.get(path).add(info);
				requestMappingInfos.add(info);
			}
		}
	}

	private boolean isController(Class<?> clazz) {
		return clazz.isAnnotationPresent(Controller.class);
	}

}
