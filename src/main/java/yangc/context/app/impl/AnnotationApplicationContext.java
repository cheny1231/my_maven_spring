package yangc.context.app.impl;

import yangc.bean.definition.BeanDefinitionRegistry;
import yangc.context.reader.impl.AnnotationDefinitionReader;

public class AnnotationApplicationContext extends AbstractApplicationContext {
	public AnnotationApplicationContext(String location) throws Exception {
		AnnotationDefinitionReader reader = new AnnotationDefinitionReader((BeanDefinitionRegistry) factory, creator);
		reader.loadDefinition(getResource(location));
	}
}
