package gpms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "rowTotal", "id", "proposalNo", "projectTitle",
		"projectType", "typeOfRequest", "projectLocation", "grantingAgencies",
		"directCosts", "faCosts", "totalCosts", "faRate", "dateCreated",
		"dateSubmitted", "dueDate", "projectPeriodFrom", "projectPeriodTo",
		"lastAudited", "lastAuditedBy", "lastAuditAction", "piUser",
		"copiUsers", "seniorUsers", "allUsers", "currentuserProposalRoles",
		"deleted", "proposalStatus", "submittedByPI", "deletedByPI",
		"chairApproval", "businessManagerApproval", "irbapproval",
		"deanApproval", "researchAdministratorApproval",
		"researchAdministratorWithdraw", "researchDirectorApproval",
		"researchDirectorDeletion", "researchAdministratorSubmission",
		"researchDirectorArchived" })
@Row(colsOrder = { "Proposal No", "Project Title", "Project Type",
		"Type Of Request", "Project Location", "Granting Agencies",
		"Direct Costs", "F&A Costs", "Total Costs", "F&A Rate", "Date Created",
		"Date Submitted", "Due Date", "Project Period From",
		"Project Period To", "Proposal Status", "Last Audited",
		"Last Audited By", "Last Audit Action", "Is Deleted?" })
public class ProposalInfo {
	@JsonProperty("rowTotal")
	private int rowTotal;

	@JsonProperty("id")
	private String id = new String();

	// Proposal
	@JsonProperty("proposalNo")
	@Column(name = "Proposal No")
	private int proposalNo = 0;

	// ProjectInfo
	@JsonProperty("projectTitle")
	@Column(name = "Project Title")
	private String projectTitle = new String();

	@JsonProperty("projectType")
	@Column(name = "Project Type")
	private String projectType = new String();

	@JsonProperty("typeOfRequest")
	@Column(name = "Type Of Request")
	private List<String> typeOfRequest = new ArrayList<String>();

	@JsonProperty("projectLocation")
	@Column(name = "Project Location")
	private String projectLocation = new String();

	// SponsorAndBudgetInfo
	@JsonProperty("grantingAgencies")
	@Column(name = "Granting Agencies")
	private List<String> grantingAgencies = new ArrayList<String>();

	@JsonProperty("directCosts")
	@Column(name = "Direct Costs")
	private double directCosts;

	@JsonProperty("faCosts")
	@Column(name = "F&A Costs")
	private double faCosts;

	@JsonProperty("totalCosts")
	@Column(name = "Total Costs")
	private double totalCosts;

	@JsonProperty("faRate")
	@Column(name = "F&A Rate")
	private double faRate;

	// Proposal
	@JsonProperty("dateCreated")
	@Column(name = "Date Created", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dateCreated = new Date();

	@JsonProperty("dateSubmitted")
	@Column(name = "Date Submitted", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dateSubmitted = new Date();

	// ProjectInfo
	@JsonProperty("dueDate")
	@Column(name = "Due Date", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dueDate = new Date();

	@JsonProperty("projectPeriodFrom")
	@Column(name = "Project Period From", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date projectPeriodFrom = new Date();

	@JsonProperty("projectPeriodTo")
	@Column(name = "Project Period To", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date projectPeriodTo = new Date();

	@JsonProperty("lastAudited")
	@Column(name = "Last Audited", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date lastAudited = new Date();

	@JsonProperty("lastAuditedBy")
	@Column(name = "Last Audited By")
	private String lastAuditedBy = new String();

	@JsonProperty("lastAuditAction")
	@Column(name = "Last Audit Action")
	private String lastAuditAction = new String();

	// PI, CO-PI and Senior UserProfiles
	@JsonProperty("piUser")
	private String piUser = new String();

	@JsonProperty("copiUsers")
	private List<String> copiUsers = new ArrayList<String>();

	@JsonProperty("seniorUsers")
	private List<String> seniorUsers = new ArrayList<String>();

	@JsonProperty("allUsers")
	private List<String> allUsers = new ArrayList<String>();

	// Proposal Roles
	@JsonProperty("currentuserProposalRoles")
	private List<String> currentuserProposalRoles = new ArrayList<String>();

	@JsonProperty("deleted")
	@Column(name = "Is Deleted?")
	private boolean deleted = false;

	// Proposal
	@JsonProperty("proposalStatus")
	@Column(name = "Proposal Status")
	private List<String> proposalStatus = new ArrayList<String>();

	// Proposal Status variables
	@JsonProperty("submittedByPI")
	private SubmitType submittedByPI = SubmitType.NOTSUBMITTED;

	@JsonProperty("deletedByPI")
	private DeleteType deletedByPI = DeleteType.NOTDELETED;

	@JsonProperty("chairApproval")
	private ApprovalType chairApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@JsonProperty("businessManagerApproval")
	private ApprovalType businessManagerApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@JsonProperty("irbapproval")
	private ApprovalType irbApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@JsonProperty("deanApproval")
	private ApprovalType deanApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@JsonProperty("researchAdministratorApproval")
	private ApprovalType researchAdministratorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@JsonProperty("researchAdministratorWithdraw")
	private WithdrawType researchAdministratorWithdraw = WithdrawType.NOTWITHDRAWN;

	@JsonProperty("researchDirectorApproval")
	private ApprovalType researchDirectorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@JsonProperty("researchDirectorDeletion")
	private DeleteType researchDirectorDeletion = DeleteType.NOTDELETED;

	@JsonProperty("researchAdministratorSubmission")
	private SubmitType researchAdministratorSubmission = SubmitType.NOTSUBMITTED;

	@JsonProperty("researchDirectorArchived")
	private ArchiveType researchDirectorArchived = ArchiveType.NOTARCHIVED;

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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
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

	public List<String> getCurrentuserProposalRoles() {
		return currentuserProposalRoles;
	}

	public void setCurrentuserProposalRoles(
			List<String> currentuserProposalRoles) {
		this.currentuserProposalRoles = currentuserProposalRoles;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public List<String> getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(List<String> proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public SubmitType getSubmittedByPI() {
		return submittedByPI;
	}

	public void setSubmittedByPI(SubmitType submittedByPI) {
		this.submittedByPI = submittedByPI;
	}

	public DeleteType getDeletedByPI() {
		return deletedByPI;
	}

	public void setDeletedByPI(DeleteType deletedByPI) {
		this.deletedByPI = deletedByPI;
	}

	public ApprovalType getChairApproval() {
		return chairApproval;
	}

	public void setChairApproval(ApprovalType chairApproval) {
		this.chairApproval = chairApproval;
	}

	public ApprovalType getBusinessManagerApproval() {
		return businessManagerApproval;
	}

	public void setBusinessManagerApproval(ApprovalType businessManagerApproval) {
		this.businessManagerApproval = businessManagerApproval;
	}

	public ApprovalType getIrbApproval() {
		return irbApproval;
	}

	public void setIrbApproval(ApprovalType irbApproval) {
		this.irbApproval = irbApproval;
	}

	public ApprovalType getDeanApproval() {
		return deanApproval;
	}

	public void setDeanApproval(ApprovalType deanApproval) {
		this.deanApproval = deanApproval;
	}

	public ApprovalType getResearchAdministratorApproval() {
		return researchAdministratorApproval;
	}

	public void setResearchAdministratorApproval(
			ApprovalType researchAdministratorApproval) {
		this.researchAdministratorApproval = researchAdministratorApproval;
	}

	public WithdrawType getResearchAdministratorWithdraw() {
		return researchAdministratorWithdraw;
	}

	public void setResearchAdministratorWithdraw(
			WithdrawType researchAdministratorWithdraw) {
		this.researchAdministratorWithdraw = researchAdministratorWithdraw;
	}

	public ApprovalType getResearchDirectorApproval() {
		return researchDirectorApproval;
	}

	public void setResearchDirectorApproval(
			ApprovalType researchDirectorApproval) {
		this.researchDirectorApproval = researchDirectorApproval;
	}

	public DeleteType getResearchDirectorDeletion() {
		return researchDirectorDeletion;
	}

	public void setResearchDirectorDeletion(DeleteType researchDirectorDeletion) {
		this.researchDirectorDeletion = researchDirectorDeletion;
	}

	public SubmitType getResearchAdministratorSubmission() {
		return researchAdministratorSubmission;
	}

	public void setResearchAdministratorSubmission(
			SubmitType researchAdministratorSubmission) {
		this.researchAdministratorSubmission = researchAdministratorSubmission;
	}

	public ArchiveType getResearchDirectorArchived() {
		return researchDirectorArchived;
	}

	public void setResearchDirectorArchived(ArchiveType researchDirectorArchived) {
		this.researchDirectorArchived = researchDirectorArchived;
	}

}
