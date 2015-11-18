package gpms.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker implements Runnable {

	private ConcurrentLinkedQueue<WorkItem> workQueue = null;
	public boolean bRunning = true;

	public Worker(ConcurrentLinkedQueue<WorkItem> queue) {
		workQueue = queue;
	}

	@Override
	public void run() {
		while (bRunning) {
			long size = workQueue.size();
			if (size > 0) {
				System.out.println(System.currentTimeMillis()
						+ "\t Work queue size:" + size);
			}
			WorkItem item = workQueue.poll();
			if (item != null) {
				try {
					item.process();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.currentThread();
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stop() {
		bRunning = false;
	}

}
