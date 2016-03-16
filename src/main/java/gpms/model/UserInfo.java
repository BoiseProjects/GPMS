package gpms.model;

import java.util.Date;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder = { "rowTotal", "id", "userName", "fullName",
//		"noOfPIedProposal", "noOfCoPIedProposal", "noOfSenioredProposal",
//		"addedOn", "lastAudited", "lastAuditedBy", "lastAuditAction",
//		"isDeleted", "isActive", "isAdmin" })
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name", visible = true)
@JsonPropertyOrder({ "rowTotal", "id", "userName", "fullName",
		"noOfPIedProposal", "noOfCoPIedProposal", "noOfSenioredProposal",
		"addedOn", "lastAudited", "lastAuditedBy", "lastAuditAction",
		"deleted", "activated", "adminUser" })
@Row(colsOrder = { "User Name", "Full Name", "Number Of PIed Proposal",
		"Number Of CoPIed Proposal", "Number Of Seniored Proposal", "Added On",
		"Last Audited", "Last Audited By", "Last Audit Action", "Is Deleted?",
		"Is Activated?" })
public class UserInfo {

	@JsonProperty("rowTotal")
	private int rowTotal;

	@JsonProperty("id")
	private String id = new String();

	@JsonProperty("userName")
	@Column(name = "User Name")
	private String userName = new String();

	@JsonProperty("fullName")
	@Column(name = "Full Name")
	private String fullName = new String();

	@JsonProperty("noOfPIedProposal")
	@Column(name = "Number Of PIed Proposal")
	private int noOfPIedProposal = 0;

	@JsonProperty("noOfCoPIedProposal")
	@Column(name = "Number Of CoPIed Proposal")
	private int noOfCoPIedProposal = 0;

	@JsonProperty("noOfSenioredProposal")
	@Column(name = "Number Of Seniored Proposal")
	private int noOfSenioredProposal = 0;

	@JsonProperty("addedOn")
	@Column(name = "Added On", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date addedOn = new Date();

	@JsonProperty("lastAudited")
	@Column(name = "Last Audited", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date lastAudited = new Date();

	@JsonProperty("lastAuditedBy")
	@Column(name = "Last Audited By")
	private String lastAuditedBy = new String();

	@JsonProperty("lastAuditAction")
	@Column(name = "Last Audit Action")
	private String lastAuditAction = new String();

	@JsonProperty("deleted")
	@Column(name = "Is Deleted?")
	private boolean deleted;

	@JsonProperty("activated")
	@Column(name = "Is Activated?")
	private boolean activated;

	@JsonProperty("adminUser")
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

}
