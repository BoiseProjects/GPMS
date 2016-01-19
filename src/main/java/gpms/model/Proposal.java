package gpms.model;

import gpms.dao.ProposalDAO;

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
public class Proposal extends BaseEntity {
	@Expose
	@Property("proposal no")
	@Indexed(value = IndexDirection.ASC, name = "proposalNoIndex", unique = true)
	private int proposalNo = 0;

	@Expose
	@Property("date received")
	private Date dateReceived = new Date();

	@Expose
	@Property("proposal status")
	private Status proposalStatus = Status.NEW;

	@Expose
	@Embedded("investigator info")
	private InvestigatorInfo investigatorInfo = new InvestigatorInfo();

	@Expose
	@Embedded("project info")
	private ProjectInfo projectInfo = new ProjectInfo();

	@Expose
	@Embedded("sponsor and budget info")
	private SponsorAndBudgetInfo sponsorAndBudgetInfo = new SponsorAndBudgetInfo();

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

	@Expose
	@Embedded("signature info")
	private List<SignatureInfo> signatureInfo = new ArrayList<SignatureInfo>();

	public Proposal() {
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
		Date currDate = new Date();
		if (dateReceived.equals(currDate) || dateReceived.after(currDate)) {
			this.dateReceived = dateReceived;
		}
	}

	public Status getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(Status status) {
		this.proposalStatus = status;
	}

	public InvestigatorInfo getInvestigatorInfo() {
		return investigatorInfo;
	}

	/**
	 * When Investigator info is set, we will add a uniquely generated id to
	 * everyone involved at the creation of the proposal this id is the
	 * proposalKey, which is an ObjectId object, but we run the toString to make
	 * it more manageable for our purposes.
	 * 
	 * @param investigatorInfo
	 */
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

	@Override
	public String toString() {
		return "Proposal [proposalNo=" + proposalNo + ", dateReceived="
				+ dateReceived + ", proposalStatus=" + proposalStatus
				+ ", investigatorInfo=" + investigatorInfo + ", projectInfo="
				+ projectInfo + ", sponsorAndBudgetInfo="
				+ sponsorAndBudgetInfo + ", costShareInfo=" + costShareInfo
				+ ", universityCommitments=" + universityCommitments
				+ ", conflicOfInterest=" + conflicOfInterest
				+ ", complianceInfo=" + complianceInfo + ", additionalInfo="
				+ additionalInfo + ", collaborationInfo=" + collaborationInfo
				+ ", confidentialInfo=" + confidentialInfo
				+ ", oSPSectionInfo=" + oSPSectionInfo + ", signatureInfo="
				+ signatureInfo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
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
				+ ((dateReceived == null) ? 0 : dateReceived.hashCode());
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
		if (dateReceived == null) {
			if (other.dateReceived != null)
				return false;
		} else if (!dateReceived.equals(other.dateReceived))
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
		if (proposalStatus != other.proposalStatus)
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
		if (universityCommitments == null) {
			if (other.universityCommitments != null)
				return false;
		} else if (!universityCommitments.equals(other.universityCommitments))
			return false;
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void addEntryToAuditLog(AuditLog audit) {
		this.getAuditLog().add(audit);
	}

}
