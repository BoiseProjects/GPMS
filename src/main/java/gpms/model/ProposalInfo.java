package gpms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProposalInfo implements Comparable<ProposalInfo> {
	private int rowTotal;
	private String id = new String();

	// Proposal
	private int proposalNo = 0;

	// ProjectInfo
	private String projectTitle = new String();
	private String projectType = new String();
	private List<String> typeOfRequest = new ArrayList<String>();
	private String projectLocation = new String();

	// SponsorAndBudgetInfo
	private List<String> grantingAgencies = new ArrayList<String>();
	private double directCosts;
	private double faCosts;
	private double totalCosts;
	private double faRate;

	// Proposal
	private Date dateReceived = new Date();
	private Date dateSubmitted = new Date();

	// ProjectInfo
	private Date dueDate = new Date();
	private Date projectPeriodFrom = new Date();
	private Date projectPeriodTo = new Date();

	// Proposal
	private List<String> proposalStatus = new ArrayList<String>();

	private Date lastAudited = new Date();
	private String lastAuditedBy = new String();
	private String lastAuditAction = new String();

	// PI, CO-PI and Senior UserProfiles
	private String piUser = new String();
	private List<String> copiUsers = new ArrayList<String>();
	private List<String> seniorUsers = new ArrayList<String>();

	private List<String> allUsers = new ArrayList<String>();

	// Proposal Roles
	private List<String> currentuserProposalRoles = new ArrayList<String>();

	private boolean isDeleted = false;

	public ProposalInfo() {

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

	public int getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(int proposalNo) {
		this.proposalNo = proposalNo;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public List<String> getProposalStatus() {
		return proposalStatus;
	}

	// TODO
	public void setProposalStatus(List<String> proposalStatus) {
		this.proposalStatus.addAll(proposalStatus);
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public List<String> getTypeOfRequest() {
		return typeOfRequest;
	}

	public void setTypeOfRequest(List<String> typeOfRequest) {
		this.typeOfRequest = typeOfRequest;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getProjectPeriodFrom() {
		return projectPeriodFrom;
	}

	public void setProjectPeriodFrom(Date projectPeriodFrom) {
		this.projectPeriodFrom = projectPeriodFrom;
	}

	public Date getProjectPeriodTo() {
		return projectPeriodTo;
	}

	public void setProjectPeriodTo(Date projectPeriodTo) {
		this.projectPeriodTo = projectPeriodTo;
	}

	public String getProjectLocation() {
		return projectLocation;
	}

	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}

	public List<String> getGrantingAgencies() {
		return grantingAgencies;
	}

	public void setGrantingAgencies(List<String> grantingAgencies) {
		this.grantingAgencies = grantingAgencies;
	}

	public double getDirectCosts() {
		return directCosts;
	}

	public void setDirectCosts(double directCosts) {
		this.directCosts = directCosts;
	}

	public double getFaCosts() {
		return faCosts;
	}

	public void setFaCosts(double faCosts) {
		this.faCosts = faCosts;
	}

	public double getTotalCosts() {
		return totalCosts;
	}

	public void setTotalCosts(double totalCosts) {
		this.totalCosts = totalCosts;
	}

	public double getFaRate() {
		return faRate;
	}

	public void setFaRate(double faRate) {
		this.faRate = faRate;
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

	public String getPiUser() {
		return piUser;
	}

	public void setPiUser(String piUser) {
		this.piUser = piUser;
	}

	public List<String> getCopiUsers() {
		return copiUsers;
	}

	public void setCopiUsers(List<String> copiUsers) {
		this.copiUsers = copiUsers;
	}

	public List<String> getSeniorUsers() {
		return seniorUsers;
	}

	public void setSeniorUsers(List<String> seniorUsers) {
		this.seniorUsers = seniorUsers;
	}

	public List<String> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<String> allUsers) {
		this.allUsers = allUsers;
	}

	// TODO
	public List<String> getCurrentuserProposalRoles() {
		return currentuserProposalRoles;
	}

	public void setCurrentuserProposalRoles(
			List<String> currentuserProposalRoles) {
		this.currentuserProposalRoles = currentuserProposalRoles;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public int compareTo(ProposalInfo o) {
		if (getLastAudited() == null || o.getLastAudited() == null)
			return 0;
		return o.getLastAudited().compareTo(getLastAudited()); // Descending
	}

}
