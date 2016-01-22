package gpms.queue;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessingFactory {

	static ConcurrentHashMap<String, TaskQueue> map = new ConcurrentHashMap<String, TaskQueue>();

	public static void destroy(String queueName) {
		System.out.println("Destroying the TaskQueue with name: " + queueName);
		if (map.containsKey(queueName)) {
			TaskQueue queue = map.remove(queueName);
			queue.destroy();
		}
	}

	public static void create(String queueName, Properties properties) {
		if (!map.containsKey(queueName)) {
			TaskQueue queue = TaskQueue.create(properties);
			map.putIfAbsent(queueName, queue);
			System.out.println("Created the TaskQueue with name: " + queueName);
		}
	}

	public static TaskQueue getTaskQueue(String queueName) {
		if (map.containsKey(queueName)) {
			return map.get(queueName);
		} else {
			return null;
		}
	}

}
