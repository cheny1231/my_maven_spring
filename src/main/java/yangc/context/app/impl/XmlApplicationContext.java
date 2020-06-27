package yangc.context.app.impl;

import java.util.ArrayList;
import java.util.List;

import yangc.bean.definition.BeanDefinitionRegistry;
import yangc.context.reader.impl.XmlBeanDefinitionReader;
import yangc.context.source.Resource;

public class XmlApplicationContext extends AbstractApplicationContext {
	private List<Resource> resources;

	public XmlApplicationContext(String loc) throws Exception {
		resources = new ArrayList<>();
		resources.add(getResource(loc));
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) factory, creator);
		Resource[] resourcesArray = (Resource[]) resources.toArray();
		reader.loadDefinition(resourcesArray);
	}
}
