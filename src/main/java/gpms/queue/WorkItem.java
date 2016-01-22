package gpms.queue;

public interface WorkItem {
	public boolean process() throws Exception;
}
