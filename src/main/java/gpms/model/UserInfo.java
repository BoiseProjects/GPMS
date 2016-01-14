package gpms.model;

import java.util.Date;

public class UserInfo implements Comparable<UserInfo> {
	private int rowTotal;
	private String id = new String();
	private String userName = new String();
	private String fullName = new String();

	private int noOfPIedProposal = 0;
	private int noOfCoPIedProposal = 0;
	private int noOfSenioredProposal = 0;
	private Date addedOn = new Date();
	private Date lastAudited = new Date();
	private String lastAuditedBy = new String();
	private String lastAuditAction = new String();

	private boolean isActive;
	private boolean isDeleted;
	private boolean isAdmin;

	public UserInfo() {
	}

	public int getRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(int rowTotal) {
		this.rowTotal = rowTotal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getNoOfPIedProposal() {
		return noOfPIedProposal;
	}

	public void setNoOfPIedProposal(int noOfPIedProposal) {
		this.noOfPIedProposal = noOfPIedProposal;
	}

	public int getNoOfCoPIedProposal() {
		return noOfCoPIedProposal;
	}

	public void setNoOfCoPIedProposal(int noOfCoPIedProposal) {
		this.noOfCoPIedProposal = noOfCoPIedProposal;
	}

	public int getNoOfSenioredProposal() {
		return noOfSenioredProposal;
	}

	public void setNoOfSenioredProposal(int noOfSenioredProposal) {
		this.noOfSenioredProposal = noOfSenioredProposal;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public Date getLastAudited() {
		return lastAudited;
	}

	public void setLastAudited(Date lastAudited) {
		this.lastAudited = lastAudited;
	}

	public String getLastAuditedBy() {
		return lastAuditedBy;
	}

	public void setLastAuditedBy(String lastAuditedBy) {
		this.lastAuditedBy = lastAuditedBy;
	}

	public String getLastAuditAction() {
		return lastAuditAction;
	}

	public void setLastAuditAction(String lastAuditAction) {
		this.lastAuditAction = lastAuditAction;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public UserInfo(int rowTotal, String id, String userName, String fullName,
			int noOfPIedProposal, int noOfCoPIedProposal,
			int noOfSenioredProposal, Date addedOn, Date lastAudited,
			String lastAuditedBy, String lastAuditAction, boolean isActive,
			boolean isDeleted, boolean isAdmin) {
		this.rowTotal = rowTotal;
		this.id = id;
		this.userName = userName;
		this.fullName = fullName;
		this.noOfPIedProposal = noOfPIedProposal;
		this.noOfCoPIedProposal = noOfCoPIedProposal;
		this.noOfSenioredProposal = noOfSenioredProposal;
		this.addedOn = addedOn;
		this.lastAudited = lastAudited;
		this.lastAuditedBy = lastAuditedBy;
		this.lastAuditAction = lastAuditAction;
		this.isActive = isActive;
		this.isDeleted = isDeleted;
		this.isAdmin = isAdmin;
	}

	@Override
	public int compareTo(UserInfo o) {
		if (getLastAudited() == null || o.getLastAudited() == null)
			return 0;
		return o.getLastAudited().compareTo(getLastAudited()); // Descending
	}
}
