package yangc.context.source;

import java.io.InputStream;

public interface InputStreamSource {
	InputStream getInputStream() throws Exception;
}
