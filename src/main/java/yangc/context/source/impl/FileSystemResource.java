package yangc.context.source.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import yangc.context.source.Resource;

public class FileSystemResource implements Resource {

	private File file;

	public FileSystemResource(String path) {
		this.file = new File(path);
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	@Override
	public boolean isReadable() {
		return file.canRead();
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public File getFile() {
		return file;
	}

}
