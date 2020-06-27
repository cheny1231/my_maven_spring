package yangc.context.reader;

import yangc.context.source.Resource;

public interface DefinitionReader {
	void loadDefinition(Resource resource) throws Exception;

	void loadDefinition(Resource... resources) throws Exception;
}
