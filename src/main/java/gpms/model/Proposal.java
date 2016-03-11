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

import com.google.gson.annotations.Expose;

@Entity(value = ProposalDAO.COLLECTION_NAME, noClassnameStored = true)
public class Proposal extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@Property("proposal no")
	@Indexed(value = IndexDirection.ASC, name = "proposalNoIndex", unique = true)
	private int proposalNo = 0;

	@Expose
	@Property("date created")
	private Date dateCreated = new Date();

	@Expose
	@Property("date submitted")
	private Date dateSubmitted = null;

	@Expose
	@Property("proposal status")
	private List<Status> proposalStatus = new ArrayList<>(
			Arrays.asList(Status.NOTSUBMITTEDBYPI));

	// TODO : add status variables here
	@Expose
	@Property("submitted by PI")
	private SubmitType submittedByPI = SubmitType.NOTSUBMITTED;

	@Expose
	@Property("deleted by PI")
	private DeleteType deletedByPI = DeleteType.NOTDELETED;

	@Expose
	@Property("Chair approval")
	private ApprovalType chairApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("Business Manager approval")
	private ApprovalType businessManagerApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("IRB approval")
	private ApprovalType IRBApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("Dean approval")
	private ApprovalType DeanApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("University Research Administrator approval")
	private ApprovalType UniversityResearchAdministratorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("University Research Administrator withdraw")
	private WithdrawType UniversityResearchAdministratorWithdraw = WithdrawType.NOTWITHDRAWN;

	@Expose
	@Property("University Research Director approval")
	private ApprovalType UniversityResearchDirectorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("University Research Director deletion")
	private DeleteType UniversityResearchDirectorDeletion = DeleteType.NOTDELETED;

	@Expose
	@Property("University Research Administrator submitted")
	private SubmitType UniversityResearchAdministratorSubmission = SubmitType.NOTSUBMITTED;

	@Expose
	@Property("University Research Director archived")
	private ArchiveType UniversityResearchDirectorArchived = ArchiveType.NOTARCHIVED;

	// END

	@Expose
	@Embedded("investigator info")
	private InvestigatorInfo investigatorInfo = new InvestigatorInfo();

	@Expose
	@Embedded("project info")
	private ProjectInfo projectInfo = new ProjectInfo();

	@Expose
	@Embedded("sponsor and budget info")
	private SponsorAndBudgetInfo sponsorAndBudgetInfo = new SponsorAndBudgetInfo();

	// Until this clone is done
	// below need to be done

	@Expose
	@Embedded("cost share info")
	private CostShareInfo costShareInfo = new CostShareInfo();

	@Expose
	@Embedded("university commitments")
	private UniversityCommitments universityCommitments = new UniversityCommitments();

	@Expose
	@Embedded("conflict of interest and commitment info")
	private ConflictOfInterest conflicOfInterest = new ConflictOfInterest();

	@Expose
	@Embedded("compliance info")
	private ComplianceInfo complianceInfo = new ComplianceInfo();

	@Expose
	@Embedded("additional info")
	private AdditionalInfo additionalInfo = new AdditionalInfo();

	@Expose
	@Embedded("collaboration info")
	private CollaborationInfo collaborationInfo = new CollaborationInfo();

	@Expose
	@Embedded("proprietary/confidential info")
	private ConfidentialInfo confidentialInfo = new ConfidentialInfo();

	@Expose
	@Embedded("OSPSection info")
	private OSPSectionInfo oSPSectionInfo = new OSPSectionInfo();

	// @Expose
	@Embedded("signature info")
	private List<SignatureInfo> signatureInfo = new ArrayList<SignatureInfo>();

	@Expose
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

	public OSPSectionInfo getoSPSectionInfo() {
		return oSPSectionInfo;
	}

	public void setoSPSectionInfo(OSPSectionInfo oSPSectionInfo) {
		this.oSPSectionInfo = oSPSectionInfo;
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
				+ submittedByPI + ", deletedByPI=" + deletedByPI
				+ ", chairApproval=" + chairApproval
				+ ", businessManagerApproval=" + businessManagerApproval
				+ ", IRBApproval=" + IRBApproval + ", DeanApproval="
				+ DeanApproval + ", UniversityResearchAdministratorApproval="
				+ UniversityResearchAdministratorApproval
				+ ", UniversityResearchAdministratorWithdraw="
				+ UniversityResearchAdministratorWithdraw
				+ ", UniversityResearchDirectorApproval="
				+ UniversityResearchDirectorApproval
				+ ", UniversityResearchDirectorDeletion="
				+ UniversityResearchDirectorDeletion
				+ ", UniversityResearchAdministratorSubmission="
				+ UniversityResearchAdministratorSubmission
				+ ", UniversityResearchDirectorArchived="
				+ UniversityResearchDirectorArchived + ", investigatorInfo="
				+ investigatorInfo + ", projectInfo=" + projectInfo
				+ ", sponsorAndBudgetInfo=" + sponsorAndBudgetInfo
				+ ", costShareInfo=" + costShareInfo
				+ ", universityCommitments=" + universityCommitments
				+ ", conflicOfInterest=" + conflicOfInterest
				+ ", complianceInfo=" + complianceInfo + ", additionalInfo="
				+ additionalInfo + ", collaborationInfo=" + collaborationInfo
				+ ", confidentialInfo=" + confidentialInfo
				+ ", oSPSectionInfo=" + oSPSectionInfo + ", signatureInfo="
				+ signatureInfo + ", appendices=" + appendices + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((DeanApproval == null) ? 0 : DeanApproval.hashCode());
		result = prime * result
				+ ((IRBApproval == null) ? 0 : IRBApproval.hashCode());
		result = prime
				* result
				+ ((UniversityResearchAdministratorApproval == null) ? 0
						: UniversityResearchAdministratorApproval.hashCode());
		result = prime
				* result
				+ ((UniversityResearchAdministratorSubmission == null) ? 0
						: UniversityResearchAdministratorSubmission.hashCode());
		result = prime
				* result
				+ ((UniversityResearchAdministratorWithdraw == null) ? 0
						: UniversityResearchAdministratorWithdraw.hashCode());
		result = prime
				* result
				+ ((UniversityResearchDirectorApproval == null) ? 0
						: UniversityResearchDirectorApproval.hashCode());
		result = prime
				* result
				+ ((UniversityResearchDirectorArchived == null) ? 0
						: UniversityResearchDirectorArchived.hashCode());
		result = prime
				* result
				+ ((UniversityResearchDirectorDeletion == null) ? 0
						: UniversityResearchDirectorDeletion.hashCode());
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
				+ ((deletedByPI == null) ? 0 : deletedByPI.hashCode());
		result = prime
				* result
				+ ((investigatorInfo == null) ? 0 : investigatorInfo.hashCode());
		result = prime * result
				+ ((oSPSectionInfo == null) ? 0 : oSPSectionInfo.hashCode());
		result = prime * result
				+ ((projectInfo == null) ? 0 : projectInfo.hashCode());
		result = prime * result + proposalNo;
		result = prime * result
				+ ((proposalStatus == null) ? 0 : proposalStatus.hashCode());
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
		if (DeanApproval != other.DeanApproval)
			return false;
		if (IRBApproval != other.IRBApproval)
			return false;
		if (UniversityResearchAdministratorApproval != other.UniversityResearchAdministratorApproval)
			return false;
		if (UniversityResearchAdministratorSubmission != other.UniversityResearchAdministratorSubmission)
			return false;
		if (UniversityResearchAdministratorWithdraw != other.UniversityResearchAdministratorWithdraw)
			return false;
		if (UniversityResearchDirectorApproval != other.UniversityResearchDirectorApproval)
			return false;
		if (UniversityResearchDirectorArchived != other.UniversityResearchDirectorArchived)
			return false;
		if (UniversityResearchDirectorDeletion != other.UniversityResearchDirectorDeletion)
			return false;
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
		if (deletedByPI != other.deletedByPI)
			return false;
		if (investigatorInfo == null) {
			if (other.investigatorInfo != null)
				return false;
		} else if (!investigatorInfo.equals(other.investigatorInfo))
			return false;
		if (oSPSectionInfo == null) {
			if (other.oSPSectionInfo != null)
				return false;
		} else if (!oSPSectionInfo.equals(other.oSPSectionInfo))
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

	public boolean checkIRBReviewRequired() {
		// TODO to check IRB Review Required based on the variables
		boolean IRBReviewRequired = true;
		return IRBReviewRequired;
	}

}
