package gpms.model;

import gpms.dao.ProposalDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(value = ProposalDAO.COLLECTION_NAME, noClassnameStored = true)
@JsonIgnoreProperties({ "signatureInfo", "id", "version", "auditLog",
		"userProfile", "action", "activityDate" })
public class Proposal extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("proposal no")
	@Indexed(value = IndexDirection.ASC, name = "proposalNoIndex", unique = true)
	private int proposalNo = 0;

	@Property("date created")
	private Date dateCreated = new Date();

	@Property("date submitted")
	private Date dateSubmitted = null;

	@Property("proposal status")
	private List<Status> proposalStatus = new ArrayList<>(
			Arrays.asList(Status.NOTSUBMITTEDBYPI));

	@Property("submitted by PI")
	private SubmitType submittedByPI = SubmitType.NOTSUBMITTED;

	@Property("ready for submission by PI")
	private ReadyType readyForSubmissionByPI = ReadyType.NOTREADYFORSUBMIT;

	@Property("deleted by PI")
	private DeleteType deletedByPI = DeleteType.NOTDELETED;

	@Property("chair approval")
	private ApprovalType chairApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Property("business manager approval")
	private ApprovalType businessManagerApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Property("irb approval")
	private ApprovalType irbApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Property("dean approval")
	private ApprovalType deanApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Property("research administrator approval")
	private ApprovalType researchAdministratorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Property("research administrator withdraw")
	private WithdrawType researchAdministratorWithdraw = WithdrawType.NOTWITHDRAWN;

	@Property("research director approval")
	private ApprovalType researchDirectorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Property("research director deletion")
	private DeleteType researchDirectorDeletion = DeleteType.NOTDELETED;

	@Property("research administrator submitted")
	private SubmitType researchAdministratorSubmission = SubmitType.NOTSUBMITTED;

	@Property("research director archived")
	private ArchiveType researchDirectorArchived = ArchiveType.NOTARCHIVED;

	@Embedded("investigator info")
	private InvestigatorInfo investigatorInfo = new InvestigatorInfo();

	@Embedded("project info")
	private ProjectInfo projectInfo = new ProjectInfo();

	@Embedded("sponsor and budget info")
	private SponsorAndBudgetInfo sponsorAndBudgetInfo = new SponsorAndBudgetInfo();

	@Embedded("cost share info")
	private CostShareInfo costShareInfo = new CostShareInfo();

	@Embedded("university commitments")
	private UniversityCommitments universityCommitments = new UniversityCommitments();

	@Embedded("conflict of interest and commitment info")
	private ConflictOfInterest conflicOfInterest = new ConflictOfInterest();

	@Embedded("compliance info")
	private ComplianceInfo complianceInfo = new ComplianceInfo();

	@Property("irb approval required")
	private boolean irbApprovalRequired = false;

	@Embedded("additional info")
	private AdditionalInfo additionalInfo = new AdditionalInfo();

	@Embedded("collaboration info")
	private CollaborationInfo collaborationInfo = new CollaborationInfo();

	@Embedded("proprietary/confidential info")
	private ConfidentialInfo confidentialInfo = new ConfidentialInfo();

	@Embedded("osp section info")
	private OSPSectionInfo ospSectionInfo = new OSPSectionInfo();

	@JsonProperty("signatureInfo")
	@Embedded("signature info")
	private List<SignatureInfo> signatureInfo = new ArrayList<SignatureInfo>();

	@JsonProperty("appendices")
	@Embedded("appendices")
	private List<Appendix> appendices = new ArrayList<Appendix>();

	public Proposal() {

	}

	public int getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(int proposalNo) {
		this.proposalNo = proposalNo;
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

	public List<Status> getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(List<Status> proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public SubmitType getSubmittedByPI() {
		return submittedByPI;
	}

	public void setSubmittedByPI(SubmitType submittedByPI) {
		this.submittedByPI = submittedByPI;
	}

	public ReadyType getReadyForSubmissionByPI() {
		return readyForSubmissionByPI;
	}

	public void setReadyForSubmissionByPI(ReadyType readyForSubmissionByPI) {
		this.readyForSubmissionByPI = readyForSubmissionByPI;
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

	public InvestigatorInfo getInvestigatorInfo() {
		return investigatorInfo;
	}

	public void setInvestigatorInfo(InvestigatorInfo investigatorInfo) {
		this.investigatorInfo = investigatorInfo;
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	public SponsorAndBudgetInfo getSponsorAndBudgetInfo() {
		return sponsorAndBudgetInfo;
	}

	public void setSponsorAndBudgetInfo(
			SponsorAndBudgetInfo sponsorAndBudgetInfo) {
		this.sponsorAndBudgetInfo = sponsorAndBudgetInfo;
	}

	public CostShareInfo getCostShareInfo() {
		return costShareInfo;
	}

	public void setCostShareInfo(CostShareInfo costShareInfo) {
		this.costShareInfo = costShareInfo;
	}

	public UniversityCommitments getUniversityCommitments() {
		return universityCommitments;
	}

	public void setUniversityCommitments(
			UniversityCommitments universityCommitments) {
		this.universityCommitments = universityCommitments;
	}

	public ConflictOfInterest getConflicOfInterest() {
		return conflicOfInterest;
	}

	public void setConflicOfInterest(ConflictOfInterest conflicOfInterest) {
		this.conflicOfInterest = conflicOfInterest;
	}

	public ComplianceInfo getComplianceInfo() {
		return complianceInfo;
	}

	public void setComplianceInfo(ComplianceInfo complianceInfo) {
		this.complianceInfo = complianceInfo;
	}

	public boolean isIrbApprovalRequired() {
		return irbApprovalRequired;
	}

	public void setIrbApprovalRequired(boolean irbApprovalRequired) {
		this.irbApprovalRequired = irbApprovalRequired;
	}

	public AdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(AdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public CollaborationInfo getCollaborationInfo() {
		return collaborationInfo;
	}

	public void setCollaborationInfo(CollaborationInfo collaborationInfo) {
		this.collaborationInfo = collaborationInfo;
	}

	public ConfidentialInfo getConfidentialInfo() {
		return confidentialInfo;
	}

	public void setConfidentialInfo(ConfidentialInfo confidentialInfo) {
		this.confidentialInfo = confidentialInfo;
	}

	public OSPSectionInfo getOspSectionInfo() {
		return ospSectionInfo;
	}

	public void setOspSectionInfo(OSPSectionInfo ospSectionInfo) {
		this.ospSectionInfo = ospSectionInfo;
	}

	public List<SignatureInfo> getSignatureInfo() {
		return signatureInfo;
	}

	public void setSignatureInfo(List<SignatureInfo> signatureInfo) {
		this.signatureInfo = signatureInfo;
	}

	public List<Appendix> getAppendices() {
		return appendices;
	}

	public void setAppendices(List<Appendix> appendices) {
		this.appendices = appendices;
	}

	@Override
	public String toString() {
		return "Proposal [proposalNo=" + proposalNo + ", dateCreated="
				+ dateCreated + ", dateSubmitted=" + dateSubmitted
				+ ", proposalStatus=" + proposalStatus + ", submittedByPI="
				+ submittedByPI + ", readyForSubmissionByPI="
				+ readyForSubmissionByPI + ", deletedByPI=" + deletedByPI
				+ ", chairApproval=" + chairApproval
				+ ", businessManagerApproval=" + businessManagerApproval
				+ ", irbApproval=" + irbApproval + ", deanApproval="
				+ deanApproval + ", researchAdministratorApproval="
				+ researchAdministratorApproval
				+ ", researchAdministratorWithdraw="
				+ researchAdministratorWithdraw + ", researchDirectorApproval="
				+ researchDirectorApproval + ", researchDirectorDeletion="
				+ researchDirectorDeletion
				+ ", researchAdministratorSubmission="
				+ researchAdministratorSubmission
				+ ", researchDirectorArchived=" + researchDirectorArchived
				+ ", investigatorInfo=" + investigatorInfo + ", projectInfo="
				+ projectInfo + ", sponsorAndBudgetInfo="
				+ sponsorAndBudgetInfo + ", costShareInfo=" + costShareInfo
				+ ", universityCommitments=" + universityCommitments
				+ ", conflicOfInterest=" + conflicOfInterest
				+ ", complianceInfo=" + complianceInfo
				+ ", irbApprovalRequired=" + irbApprovalRequired
				+ ", additionalInfo=" + additionalInfo + ", collaborationInfo="
				+ collaborationInfo + ", confidentialInfo=" + confidentialInfo
				+ ", ospSectionInfo=" + ospSectionInfo + ", signatureInfo="
				+ signatureInfo + ", appendices=" + appendices + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result
				+ ((appendices == null) ? 0 : appendices.hashCode());
		result = prime
				* result
				+ ((businessManagerApproval == null) ? 0
						: businessManagerApproval.hashCode());
		result = prime * result
				+ ((chairApproval == null) ? 0 : chairApproval.hashCode());
		result = prime
				* result
				+ ((collaborationInfo == null) ? 0 : collaborationInfo
						.hashCode());
		result = prime * result
				+ ((complianceInfo == null) ? 0 : complianceInfo.hashCode());
		result = prime
				* result
				+ ((confidentialInfo == null) ? 0 : confidentialInfo.hashCode());
		result = prime
				* result
				+ ((conflicOfInterest == null) ? 0 : conflicOfInterest
						.hashCode());
		result = prime * result
				+ ((costShareInfo == null) ? 0 : costShareInfo.hashCode());
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result
				+ ((dateSubmitted == null) ? 0 : dateSubmitted.hashCode());
		result = prime * result
				+ ((deanApproval == null) ? 0 : deanApproval.hashCode());
		result = prime * result
				+ ((deletedByPI == null) ? 0 : deletedByPI.hashCode());
		result = prime
				* result
				+ ((investigatorInfo == null) ? 0 : investigatorInfo.hashCode());
		result = prime * result
				+ ((irbApproval == null) ? 0 : irbApproval.hashCode());
		result = prime * result + (irbApprovalRequired ? 1231 : 1237);
		result = prime * result
				+ ((ospSectionInfo == null) ? 0 : ospSectionInfo.hashCode());
		result = prime * result
				+ ((projectInfo == null) ? 0 : projectInfo.hashCode());
		result = prime * result + proposalNo;
		result = prime * result
				+ ((proposalStatus == null) ? 0 : proposalStatus.hashCode());
		result = prime
				* result
				+ ((readyForSubmissionByPI == null) ? 0
						: readyForSubmissionByPI.hashCode());
		result = prime
				* result
				+ ((researchAdministratorApproval == null) ? 0
						: researchAdministratorApproval.hashCode());
		result = prime
				* result
				+ ((researchAdministratorSubmission == null) ? 0
						: researchAdministratorSubmission.hashCode());
		result = prime
				* result
				+ ((researchAdministratorWithdraw == null) ? 0
						: researchAdministratorWithdraw.hashCode());
		result = prime
				* result
				+ ((researchDirectorApproval == null) ? 0
						: researchDirectorApproval.hashCode());
		result = prime
				* result
				+ ((researchDirectorArchived == null) ? 0
						: researchDirectorArchived.hashCode());
		result = prime
				* result
				+ ((researchDirectorDeletion == null) ? 0
						: researchDirectorDeletion.hashCode());
		result = prime * result
				+ ((signatureInfo == null) ? 0 : signatureInfo.hashCode());
		result = prime
				* result
				+ ((sponsorAndBudgetInfo == null) ? 0 : sponsorAndBudgetInfo
						.hashCode());
		result = prime * result
				+ ((submittedByPI == null) ? 0 : submittedByPI.hashCode());
		result = prime
				* result
				+ ((universityCommitments == null) ? 0 : universityCommitments
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposal other = (Proposal) obj;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (appendices == null) {
			if (other.appendices != null)
				return false;
		} else if (!appendices.equals(other.appendices))
			return false;
		if (businessManagerApproval != other.businessManagerApproval)
			return false;
		if (chairApproval != other.chairApproval)
			return false;
		if (collaborationInfo == null) {
			if (other.collaborationInfo != null)
				return false;
		} else if (!collaborationInfo.equals(other.collaborationInfo))
			return false;
		if (complianceInfo == null) {
			if (other.complianceInfo != null)
				return false;
		} else if (!complianceInfo.equals(other.complianceInfo))
			return false;
		if (confidentialInfo == null) {
			if (other.confidentialInfo != null)
				return false;
		} else if (!confidentialInfo.equals(other.confidentialInfo))
			return false;
		if (conflicOfInterest == null) {
			if (other.conflicOfInterest != null)
				return false;
		} else if (!conflicOfInterest.equals(other.conflicOfInterest))
			return false;
		if (costShareInfo == null) {
			if (other.costShareInfo != null)
				return false;
		} else if (!costShareInfo.equals(other.costShareInfo))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateSubmitted == null) {
			if (other.dateSubmitted != null)
				return false;
		} else if (!dateSubmitted.equals(other.dateSubmitted))
			return false;
		if (deanApproval != other.deanApproval)
			return false;
		if (deletedByPI != other.deletedByPI)
			return false;
		if (investigatorInfo == null) {
			if (other.investigatorInfo != null)
				return false;
		} else if (!investigatorInfo.equals(other.investigatorInfo))
			return false;
		if (irbApproval != other.irbApproval)
			return false;
		if (irbApprovalRequired != other.irbApprovalRequired)
			return false;
		if (ospSectionInfo == null) {
			if (other.ospSectionInfo != null)
				return false;
		} else if (!ospSectionInfo.equals(other.ospSectionInfo))
			return false;
		if (projectInfo == null) {
			if (other.projectInfo != null)
				return false;
		} else if (!projectInfo.equals(other.projectInfo))
			return false;
		if (proposalNo != other.proposalNo)
			return false;
		if (proposalStatus == null) {
			if (other.proposalStatus != null)
				return false;
		} else if (!proposalStatus.equals(other.proposalStatus))
			return false;
		if (readyForSubmissionByPI != other.readyForSubmissionByPI)
			return false;
		if (researchAdministratorApproval != other.researchAdministratorApproval)
			return false;
		if (researchAdministratorSubmission != other.researchAdministratorSubmission)
			return false;
		if (researchAdministratorWithdraw != other.researchAdministratorWithdraw)
			return false;
		if (researchDirectorApproval != other.researchDirectorApproval)
			return false;
		if (researchDirectorArchived != other.researchDirectorArchived)
			return false;
		if (researchDirectorDeletion != other.researchDirectorDeletion)
			return false;
		if (signatureInfo == null) {
			if (other.signatureInfo != null)
				return false;
		} else if (!signatureInfo.equals(other.signatureInfo))
			return false;
		if (sponsorAndBudgetInfo == null) {
			if (other.sponsorAndBudgetInfo != null)
				return false;
		} else if (!sponsorAndBudgetInfo.equals(other.sponsorAndBudgetInfo))
			return false;
		if (submittedByPI != other.submittedByPI)
			return false;
		if (universityCommitments == null) {
			if (other.universityCommitments != null)
				return false;
		} else if (!universityCommitments.equals(other.universityCommitments))
			return false;
		return true;
	}

	// public Status getProposalStatus() {
	// // TODO to check the status based on the variables
	// Status proposalStatus = Status.NOTSUBMITTEDBYPI;
	// if (!this.isSubmittedByPI()) {
	// proposalStatus = Status.NOTSUBMITTEDBYPI;
	// } else if (this.isDeletedByPI()) {
	// proposalStatus = Status.DELETEDBYPI;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval()
	// .equals(ApprovalType.READYFORAPPROVAL)) {
	// proposalStatus = Status.WAITINGFORCHAIRAPPROVAL;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.DISAPPROVED)) {
	// proposalStatus = Status.RETURNEDBYCHAIR;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getBusinessManagerReviewal().equals(
	// ApprovalType.READYFORAPPROVAL)) {
	// proposalStatus = Status.READYFORREVIEWBYBUSINESSMANAGER;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getBusinessManagerReviewal().equals(
	// ApprovalType.APPROVED)) {
	// proposalStatus = Status.REVIEWEDBYBUSINESSMANAGER;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getBusinessManagerReviewal().equals(
	// ApprovalType.DISAPPROVED)) {
	// proposalStatus = Status.DISAPPROVEDBYBUSINESSMANAGER;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getIRBReviewal().equals(ApprovalType.READYFORAPPROVAL)) {
	// proposalStatus = Status.READYFORREVIEWBYIRB;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getIRBReviewal().equals(ApprovalType.APPROVED)) {
	// proposalStatus = Status.APPROVEDBYIRB;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getIRBReviewal().equals(ApprovalType.DISAPPROVED)) {
	// proposalStatus = Status.DISAPPROVEDBYIRB;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.READYFORAPPROVAL)) {
	// proposalStatus = Status.READYFORREVIEWBYDEAN;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.READYFORAPPROVAL)) {
	// proposalStatus = Status.APPROVEDBYDEAN;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.DISAPPROVED)) {
	// proposalStatus = Status.RETURNEDBYDEAN;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.getUniversityResearchDirectorApproval().equals(
	// ApprovalType.READYFORAPPROVAL)) {
	// proposalStatus = Status.SUBMITTEDTORESEARCHDIRECTOR;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.DISAPPROVED)) {
	// proposalStatus = Status.DISAPPROVEDBYRESEARCHADMIN;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.isUniversityResearchAdministratorWithdraw()) {
	// proposalStatus = Status.WITHDRAWBYRESEARCHADMIN;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.getUniversityResearchDirectorApproval().equals(
	// ApprovalType.APPROVED)) {
	// proposalStatus = Status.READYFORSUBMISSION;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.isUniversityResearchDirectorDeletion()) {
	// proposalStatus = Status.DELETEDBYRESEARCHDIRECTOR;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.getUniversityResearchDirectorApproval().equals(
	// ApprovalType.DISAPPROVED)) {
	// proposalStatus = Status.DISAPPROVEDBYRESEARCHDIRECTOR;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.isUniversityResearchAdministratorSubmission()
	// && this.getUniversityResearchDirectorApproval().equals(
	// ApprovalType.APPROVED)) {
	// proposalStatus = Status.SUBMITTEDBYRESEARCHADMIN;
	// } else if (this.isSubmittedByPI()
	// && this.getChairApproval().equals(ApprovalType.APPROVED)
	// && this.getDeanApproval().equals(ApprovalType.APPROVED)
	// && this.getUniversityResearchAdministratorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.isUniversityResearchAdministratorSubmission()
	// && this.getUniversityResearchDirectorApproval().equals(
	// ApprovalType.APPROVED)
	// && this.isUniversityResearchDirectorArchived()) {
	// proposalStatus = Status.ARCHIVEDBYRESEARCHDIRECTOR;
	// }
	// return proposalStatus;
	// }

}
