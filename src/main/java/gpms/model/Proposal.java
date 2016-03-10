package gpms.model;

import gpms.dao.ProposalDAO;

import java.io.Serializable;
import java.util.ArrayList;
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

	// TODO : add status variables here
	@Expose
	@Property("submitted by PI")
	private boolean submittedByPI = false;

	@Expose
	@Property("deleted by PI")
	private boolean deletedByPI = false;

	@Expose
	@Property("Chair approval")
	private ApprovalType chairApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("Business Manager reviewal")
	private ApprovalType businessManagerReviewal = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("IRB reviewal")
	private ApprovalType IRBReviewal = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("Dean approval")
	private ApprovalType DeanApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("University Research Administrator approval")
	private ApprovalType UniversityResearchAdministratorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("University Research Administrator withdraw")
	private boolean UniversityResearchAdministratorWithdraw = false;

	@Expose
	@Property("University Research Director approval")
	private ApprovalType UniversityResearchDirectorApproval = ApprovalType.NOTREADYFORAPPROVAL;

	@Expose
	@Property("University Research Director deletion")
	private boolean UniversityResearchDirectorDeletion = false;

	@Expose
	@Property("University Research Administrator submitted")
	private boolean UniversityResearchAdministratorSubmission = false;

	@Expose
	@Property("University Research Director archived")
	private boolean UniversityResearchDirectorArchived = false;

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
		Date currDate = new Date();
		if (dateCreated.equals(currDate) || dateCreated.after(currDate)) {
			this.dateCreated = dateCreated;
		}
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public InvestigatorInfo getInvestigatorInfo() {
		return investigatorInfo;
	}

	public boolean isSubmittedByPI() {
		return submittedByPI;
	}

	public void setSubmittedByPI(boolean submittedByPI) {
		this.submittedByPI = submittedByPI;
	}

	public boolean isDeletedByPI() {
		return deletedByPI;
	}

	public void setDeletedByPI(boolean deletedByPI) {
		this.deletedByPI = deletedByPI;
	}

	public ApprovalType getChairApproval() {
		return chairApproval;
	}

	public void setChairApproval(ApprovalType chairApproval) {
		this.chairApproval = chairApproval;
	}

	public ApprovalType getBusinessManagerReviewal() {
		return businessManagerReviewal;
	}

	public void setBusinessManagerReviewal(ApprovalType businessManagerReviewal) {
		this.businessManagerReviewal = businessManagerReviewal;
	}

	public ApprovalType getIRBReviewal() {
		return IRBReviewal;
	}

	public void setIRBReviewal(ApprovalType iRBReviewal) {
		IRBReviewal = iRBReviewal;
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

	public boolean isUniversityResearchAdministratorWithdraw() {
		return UniversityResearchAdministratorWithdraw;
	}

	public void setUniversityResearchAdministratorWithdraw(
			boolean universityResearchAdministratorWithdraw) {
		UniversityResearchAdministratorWithdraw = universityResearchAdministratorWithdraw;
	}

	public ApprovalType getUniversityResearchDirectorApproval() {
		return UniversityResearchDirectorApproval;
	}

	public void setUniversityResearchDirectorApproval(
			ApprovalType universityResearchDirectorApproval) {
		UniversityResearchDirectorApproval = universityResearchDirectorApproval;
	}

	public boolean isUniversityResearchDirectorDeletion() {
		return UniversityResearchDirectorDeletion;
	}

	public void setUniversityResearchDirectorDeletion(
			boolean universityResearchDirectorDeletion) {
		UniversityResearchDirectorDeletion = universityResearchDirectorDeletion;
	}

	public boolean isUniversityResearchAdministratorSubmission() {
		return UniversityResearchAdministratorSubmission;
	}

	public void setUniversityResearchAdministratorSubmission(
			boolean universityResearchAdministratorSubmission) {
		UniversityResearchAdministratorSubmission = universityResearchAdministratorSubmission;
	}

	public boolean isUniversityResearchDirectorArchived() {
		return UniversityResearchDirectorArchived;
	}

	public void setUniversityResearchDirectorArchived(
			boolean universityResearchDirectorArchived) {
		UniversityResearchDirectorArchived = universityResearchDirectorArchived;
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
				+ ", submittedByPI=" + submittedByPI + ", deletedByPI="
				+ deletedByPI + ", chairApproval=" + chairApproval
				+ ", businessManagerReviewal=" + businessManagerReviewal
				+ ", IRBReviewal=" + IRBReviewal + ", DeanApproval="
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
				+ ((IRBReviewal == null) ? 0 : IRBReviewal.hashCode());
		result = prime
				* result
				+ ((UniversityResearchAdministratorApproval == null) ? 0
						: UniversityResearchAdministratorApproval.hashCode());
		result = prime * result
				+ (UniversityResearchAdministratorSubmission ? 1231 : 1237);
		result = prime * result
				+ (UniversityResearchAdministratorWithdraw ? 1231 : 1237);
		result = prime
				* result
				+ ((UniversityResearchDirectorApproval == null) ? 0
						: UniversityResearchDirectorApproval.hashCode());
		result = prime * result
				+ (UniversityResearchDirectorArchived ? 1231 : 1237);
		result = prime * result
				+ (UniversityResearchDirectorDeletion ? 1231 : 1237);
		result = prime * result
				+ ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result
				+ ((appendices == null) ? 0 : appendices.hashCode());
		result = prime
				* result
				+ ((businessManagerReviewal == null) ? 0
						: businessManagerReviewal.hashCode());
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
		result = prime * result + (deletedByPI ? 1231 : 1237);
		result = prime
				* result
				+ ((investigatorInfo == null) ? 0 : investigatorInfo.hashCode());
		result = prime * result
				+ ((oSPSectionInfo == null) ? 0 : oSPSectionInfo.hashCode());
		result = prime * result
				+ ((projectInfo == null) ? 0 : projectInfo.hashCode());
		result = prime * result + proposalNo;
		result = prime * result
				+ ((signatureInfo == null) ? 0 : signatureInfo.hashCode());
		result = prime
				* result
				+ ((sponsorAndBudgetInfo == null) ? 0 : sponsorAndBudgetInfo
						.hashCode());
		result = prime * result + (submittedByPI ? 1231 : 1237);
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
		if (IRBReviewal != other.IRBReviewal)
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
		if (businessManagerReviewal != other.businessManagerReviewal)
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
		boolean IRBReviewRequired = false;
		return IRBReviewRequired;
	}

}
