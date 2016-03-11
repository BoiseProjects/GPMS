package gpms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

@Row(colsOrder = { "Proposal No", "Project Title", "Project Type",
		"Type Of Request", "Project Location", "Granting Agencies",
		"Direct Costs", "F&A Costs", "Total Costs", "F&A Rate", "Date Created",
		"Date Submitted", "Due Date", "Project Period From",
		"Project Period To", "Proposal Status", "Last Audited",
		"Last Audited By", "Last Audit Action", "Is Deleted?" })
public class ProposalInfo {
	private int rowTotal;
	private String id = new String();

	// Proposal
	@Column(name = "Proposal No")
	private int proposalNo = 0;

	// ProjectInfo
	@Column(name = "Project Title")
	private String projectTitle = new String();
	@Column(name = "Project Type")
	private String projectType = new String();
	@Column(name = "Type Of Request")
	private List<String> typeOfRequest = new ArrayList<String>();
	@Column(name = "Project Location")
	private String projectLocation = new String();

	// SponsorAndBudgetInfo
	@Column(name = "Granting Agencies")
	private List<String> grantingAgencies = new ArrayList<String>();
	@Column(name = "Direct Costs")
	private double directCosts;
	@Column(name = "F&A Costs")
	private double faCosts;
	@Column(name = "Total Costs")
	private double totalCosts;
	@Column(name = "F&A Rate")
	private double faRate;

	// Proposal
	@Column(name = "Date Created", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dateCreated = new Date();
	@Column(name = "Date Submitted", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dateSubmitted = new Date();

	// ProjectInfo
	@Column(name = "Due Date", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date dueDate = new Date();
	@Column(name = "Project Period From", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date projectPeriodFrom = new Date();
	@Column(name = "Project Period To", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date projectPeriodTo = new Date();

	// Proposal
	@Column(name = "Proposal Status")
	private List<String> proposalStatus = new ArrayList<String>();

	// Proposal Status variables
	private SubmitType submittedByPI = SubmitType.NOTSUBMITTED;
	private DeleteType deletedByPI = DeleteType.NOTDELETED;
	private ApprovalType chairApproval = ApprovalType.NOTREADYFORAPPROVAL;
	private ApprovalType businessManagerApproval = ApprovalType.NOTREADYFORAPPROVAL;
	private ApprovalType IRBApproval = ApprovalType.NOTREADYFORAPPROVAL;
	private ApprovalType DeanApproval = ApprovalType.NOTREADYFORAPPROVAL;
	private ApprovalType UniversityResearchAdministratorApproval = ApprovalType.NOTREADYFORAPPROVAL;
	private WithdrawType UniversityResearchAdministratorWithdraw = WithdrawType.NOTWITHDRAWN;
	private ApprovalType UniversityResearchDirectorApproval = ApprovalType.NOTREADYFORAPPROVAL;
	private DeleteType UniversityResearchDirectorDeletion = DeleteType.NOTDELETED;
	private SubmitType UniversityResearchAdministratorSubmission = SubmitType.NOTSUBMITTED;
	private ArchiveType UniversityResearchDirectorArchived = ArchiveType.NOTARCHIVED;

	// END

	@Column(name = "Last Audited", dataFormat = "yyyy/MM/dd hh:mm:ss")
	private Date lastAudited = new Date();

	@Column(name = "Last Audited By")
	private String lastAuditedBy = new String();

	@Column(name = "Last Audit Action")
	private String lastAuditAction = new String();

	// PI, CO-PI and Senior UserProfiles
	private String piUser = new String();
	private List<String> copiUsers = new ArrayList<String>();
	private List<String> seniorUsers = new ArrayList<String>();

	private List<String> allUsers = new ArrayList<String>();

	// Proposal Roles
	private List<String> currentuserProposalRoles = new ArrayList<String>();

	@Column(name = "Is Deleted?")
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

	public ApprovalType getIRBApproval() {
		return IRBApproval;
	}

	public void setIRBApproval(ApprovalType iRBApproval) {
		IRBApproval = iRBApproval;
	}

	public ApprovalType getDeanApproval() {
		return DeanApproval;
	}

	public void setDeanApproval(ApprovalType deanApproval) {
		DeanApproval = deanApproval;
	}

	public ApprovalType getUniversityResearchAdministratorApproval() {
		return UniversityResearchAdministratorApproval;
	}

	public void setUniversityResearchAdministratorApproval(
			ApprovalType universityResearchAdministratorApproval) {
		UniversityResearchAdministratorApproval = universityResearchAdministratorApproval;
	}

	public WithdrawType getUniversityResearchAdministratorWithdraw() {
		return UniversityResearchAdministratorWithdraw;
	}

	public void setUniversityResearchAdministratorWithdraw(
			WithdrawType universityResearchAdministratorWithdraw) {
		UniversityResearchAdministratorWithdraw = universityResearchAdministratorWithdraw;
	}

	public ApprovalType getUniversityResearchDirectorApproval() {
		return UniversityResearchDirectorApproval;
	}

	public void setUniversityResearchDirectorApproval(
			ApprovalType universityResearchDirectorApproval) {
		UniversityResearchDirectorApproval = universityResearchDirectorApproval;
	}

	public DeleteType getUniversityResearchDirectorDeletion() {
		return UniversityResearchDirectorDeletion;
	}

	public void setUniversityResearchDirectorDeletion(
			DeleteType universityResearchDirectorDeletion) {
		UniversityResearchDirectorDeletion = universityResearchDirectorDeletion;
	}

	public SubmitType getUniversityResearchAdministratorSubmission() {
		return UniversityResearchAdministratorSubmission;
	}

	public void setUniversityResearchAdministratorSubmission(
			SubmitType universityResearchAdministratorSubmission) {
		UniversityResearchAdministratorSubmission = universityResearchAdministratorSubmission;
	}

	public ArchiveType getUniversityResearchDirectorArchived() {
		return UniversityResearchDirectorArchived;
	}

	public void setUniversityResearchDirectorArchived(
			ArchiveType universityResearchDirectorArchived) {
		UniversityResearchDirectorArchived = universityResearchDirectorArchived;
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
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
