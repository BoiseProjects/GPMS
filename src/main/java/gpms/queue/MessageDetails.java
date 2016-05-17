package gpms.queue;

public class MessageDetails implements WorkItem {

	@Override
	public boolean process() throws Exception {
		return false;
	}
	// boolean isProcessed = false;
	//
	// private DataModel dm = null;
	//
	// String tweet = null;
	// int msg_id = 0;
	//
	// public MessageDetails(int msg_id) {
	// this.dm = new DataModel();
	// this.msg_id = msg_id;
	// }
	//
	// @Override
	// public boolean process() throws Exception {
	// tweet = dm.getTweetDetailByID(msg_id);
	// isProcessed = true;
	// return isProcessed;
	// }
	//
	// public boolean isCompleted() {
	// return isProcessed;
	// }
	//
	// public String getResponse() {
	// return tweet;
	// }

}
