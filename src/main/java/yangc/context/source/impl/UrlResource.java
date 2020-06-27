package yangc.context.source.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import yangc.context.source.Resource;

public class UrlResource implements Resource {
	private URL url;

	public UrlResource(String path) throws MalformedURLException {
		url = new URL(path);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}

	@Override
	public boolean isReadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return url != null;
	}

	@Override
	public File getFile() {
		return null;
	}

}
