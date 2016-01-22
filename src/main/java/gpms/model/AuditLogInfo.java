package gpms.model;

import java.util.Date;

public class AuditLogInfo implements Comparable<AuditLogInfo> {
	private int rowTotal;
	private String userName = new String();
	private String userFullName = new String();
	private String action = new String();
	private Date activityDate = new Date();

	public AuditLogInfo() {
	}

	public int getRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(int rowTotal) {
		this.rowTotal = rowTotal;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	@Override
	public int compareTo(AuditLogInfo o) {
		if (getActivityDate() == null || o.getActivityDate() == null)
			return 0;
		return o.getActivityDate().compareTo(getActivityDate()); // Descending
	}

}
