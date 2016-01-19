package gpms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder = { "rowTotal", "id", "userName", "fullName",
//		"noOfPIedProposal", "noOfCoPIedProposal", "noOfSenioredProposal",
//		"addedOn", "lastAudited", "lastAuditedBy", "lastAuditAction",
//		"isDeleted", "isActive", "isAdmin" })
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name", visible = true)
//@JsonPropertyOrder({ "rowTotal", "id", "userName", "fullName",
//		"noOfPIedProposal", "noOfCoPIedProposal", "noOfSenioredProposal",
//		"addedOn", "lastAudited", "lastAuditedBy", "lastAuditAction",
//		"deleted", "activated", "adminUser" })
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

	// @JsonProperty("deleted")
	private boolean deleted;

	// @JsonProperty("activated")
	private boolean activated;

	// @JsonProperty("adminUser")
	private boolean adminUser;

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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	@Override
	public int compareTo(UserInfo o) {
		if (getLastAudited() == null || o.getLastAudited() == null)
			return 0;
		return o.getLastAudited().compareTo(getLastAudited()); // Descending
	}
}
