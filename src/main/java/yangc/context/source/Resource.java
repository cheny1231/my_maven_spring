package yangc.context.source;

import java.io.File;

public interface Resource extends InputStreamSource {
	boolean isReadable();

	boolean exists();

	File getFile();
}
