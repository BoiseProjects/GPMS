package gpms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "rowTotal", "id", "delegatee", "delegatee_email",
		"delegatee_position_title", "delegated_position_title",
		"delegated_actions", "delegation_reason", "dateCreated",
		"delegatedFrom", "delegatedTo", "lastAudited", "lastAuditedBy",
		"lastAuditAction", "revoked" })
@Row(colsOrder = { "Delegatee", "Delegatee Email", "Delegatee Position Title",
		"Delegated Position Title", "Delegated Actions", "Delegation Reason",
		"Date Created", "Delegated From", "Delegated To", "Last Audited",
		"Last Audited By", "Last Audit Action", "Is Revoked?" })
public class DelegationInfo {
	@JsonProperty("rowTotal")
	private int rowTotal;

	@JsonProperty("id")
	private String id = new String();

	@JsonProperty("delegatee")
	@Column(name = "Delegatee")
	private String delegatee = new String();

	@JsonProperty("delegatee_email")
	@Column(name = "Delegatee Email")
	private String delegateeEmail = new String();

	@JsonProperty("delegatee_position_title")
	@Column(name = "Delegatee Position Title")
	private String delegateePositionTitle = new String();

	@JsonProperty("delegated_position_title")
	@Column(name = "Delegated Position Title")
	private String positionTitle = new String();

	@JsonProperty("delegated_actions")
	@Column(name = "Delegated Actions")
	private List<String> delegatedActions = new ArrayList<String>();

	@JsonProperty("delegation_reason")
	@Column(name = "Delegation Reason")
	private String delegationReason = new String();

	@JsonProperty("dateCreated")
	@Column(name = "Date Created", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dateCreated = new Date();

	@JsonProperty("delegatedFrom")
	@Column(name = "Delegated From", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date delegatedFrom = new Date();

	@JsonProperty("delegatedTo")
	@Column(name = "Delegated To", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date delegatedTo = new Date();

	@JsonProperty("lastAudited")
	@Column(name = "Last Audited", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date lastAudited = new Date();

	@JsonProperty("lastAuditedBy")
	@Column(name = "Last Audited By")
	private String lastAuditedBy = new String();

	@JsonProperty("lastAuditAction")
	@Column(name = "Last Audit Action")
	private String lastAuditAction = new String();

	@JsonProperty("revoked")
	@Column(name = "Is Revoked?")
	private boolean revoked = false;

	public DelegationInfo() {

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

	public String getDelegatee() {
		return delegatee;
	}

	public void setDelegatee(String delegatee) {
		this.delegatee = delegatee;
	}

	public String getDelegateeEmail() {
		return delegateeEmail;
	}

	public void setDelegateeEmail(String delegateeEmail) {
		this.delegateeEmail = delegateeEmail;
	}

	public String getDelegateePositionTitle() {
		return delegateePositionTitle;
	}

	public void setDelegateePositionTitle(String delegateePositionTitle) {
		this.delegateePositionTitle = delegateePositionTitle;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public List<String> getDelegatedActions() {
		return delegatedActions;
	}

	public void setDelegatedActions(List<String> delegatedActions) {
		this.delegatedActions = delegatedActions;
	}

	public String getDelegationReason() {
		return delegationReason;
	}

	public void setDelegationReason(String delegationReason) {
		this.delegationReason = delegationReason;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDelegatedFrom() {
		return delegatedFrom;
	}

	public void setDelegatedFrom(Date delegatedFrom) {
		this.delegatedFrom = delegatedFrom;
	}

	public Date getDelegatedTo() {
		return delegatedTo;
	}

	public void setDelegatedTo(Date delegatedTo) {
		this.delegatedTo = delegatedTo;
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

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

}
