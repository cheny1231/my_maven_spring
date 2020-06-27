package yangc.context.reader.impl;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yangc.aop.creator.impl.AopProxyCreator;
import yangc.bean.definition.BeanDefinitionRegistry;
import yangc.bean.definition.impl.DefaultBeanDefinition;
import yangc.context.source.Resource;

public class XmlBeanDefinitionReader extends AbstractDefinitionReader {

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, AopProxyCreator creator) {
		super(registry, creator);
	}

	@Override
	public void loadDefinition(Resource resource) throws Exception {
		parse(resource);
	}

	@Override
	public void loadDefinition(Resource... resources) throws Exception {
		for (Resource resource : resources) {
			parse(resource);
		}
	}

	private void parse(Resource resource) throws Exception {
		InputStream inputStream = resource.getInputStream();
		// get document object
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element rootElement = document.getRootElement();
		// parse
		List<Element> elements = rootElement.elements();
		for (Element element : elements) {
			List<Attribute> attrs = element.attributes();
			DefaultBeanDefinition bd = new DefaultBeanDefinition();
			for (Attribute attr : attrs) {
				String data = (String) attr.getData();
				// class tag
				if ("class".equals(attr.getName())) {
					Class<?> clazz = Class.forName(data);
					bd.setClazz(clazz);
				} else if ("id".equals(attr.getName())) {
					registry.register(bd, data);
				} else {
					// TODO: other tags
				}
			}
		}

	}

}
