package yangc.bean.aware;

import yangc.context.app.ApplicationContext;

public interface ApplicationContextAware {
	void setApplicationContext(ApplicationContext context);
}
