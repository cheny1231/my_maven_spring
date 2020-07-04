package yangc.context.source.impl;

import java.io.File;
import java.io.InputStream;

import yangc.context.source.Resource;

public class ClassPathResource implements Resource {
	private ClassLoader classLoader;
	private String path;
	private Class clazz;

	public ClassPathResource(ClassLoader classLoader, String path, Class clazz) {
		this.classLoader = classLoader;
		this.path = path;
		this.clazz = clazz;
	}

	@Override
	public InputStream getInputStream() {
		if (path != null && !path.isEmpty()) {
			if (clazz != null) {
				return clazz.getResourceAsStream(path);
			}
			if (classLoader != null) {
				return classLoader.getResourceAsStream(path);
			}
			System.out.println("getting resourse as stream: " + path);
			return this.getClass().getClassLoader().getResourceAsStream(path);
		}
		return null;
	}

	@Override
	public boolean isReadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists() {
		if (path != null && !path.isEmpty()) {
			if (clazz != null) {
				return clazz.getResource(path) != null;
			}
			if (classLoader != null) {
				return classLoader.getResource(path) != null;
			}
			return this.getClass().getResource(path) != null;
		}
		return false;
	}

	@Override
	public File getFile() {
		return null;
	}

}
