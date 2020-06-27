package yangc.context.source;

public interface ResourceFactory {
	Resource getResource(String locations) throws Exception;
}
