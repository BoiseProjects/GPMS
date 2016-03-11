package gpms.model;

import gpms.dao.ProposalDAO;
import gpms.dao.UserProfileDAO;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

import com.google.gson.annotations.Expose;
import com.mongodb.MongoClient;

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

//	@Expose
	@Embedded("signature info")
	private List<SignatureInfo> signatureInfo = new ArrayList<SignatureInfo>();

	@Expose
	@Embedded("appendices")
	private List<Appendix> appendices = new ArrayList<Appendix>();
	
	private static final String DBNAME = "db_gpms";

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

	public List<Status> getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(List<Status> proposalStatus) {
		this.proposalStatus = proposalStatus;
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
				+ ", proposalStatus=" + proposalStatus + ", investigatorInfo="
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
				+ ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result
				+ ((appendices == null) ? 0 : appendices.hashCode());
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
		if (appendices == null) {
			if (other.appendices != null)
				return false;
		} else if (!appendices.equals(other.appendices))
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
		if (universityCommitments == null) {
			if (other.universityCommitments != null)
				return false;
		} else if (!universityCommitments.equals(other.universityCommitments))
			return false;
		return true;
	}
	
	/**
	 * This method will check the signatures for the proposal.
	 * It will first find all the supervisory personnel that SHOULD be signing
	 * the proposal (based on PI, COPI, Senior Personnel -their supervisory personnel-)
	 * Then it will find out if the appropriate number has signed
	 * ie: if between the Pi, CoPi, and SP, there are 4 department chairs,
	 * we need to know that 4 department chairs have signed.
	 * @param1 the ID of the proposal we need to query for
	 * @param2 the position title we want to check
	 * @return true if all required signatures exist
	 * @throws UnknownHostException 
	 */
	public boolean getSignedStatus(String posTitle) throws UnknownHostException
	{
		Morphia morphia = new Morphia();
		//1st Get the Proposal, then get the Pi, CoPi and SP attached to it

		List<InvestigatorRefAndPosition> invList =  this.getInvestigatorInfo().getAllInvList();
		ArrayList<UserProfile> supervisorsList = new ArrayList<UserProfile>();
		//For each person on this list, get their supervisory personnel, and add them to a list, 
		//but avoid duplicate entries.
		UserProfileDAO getSupersDAO = new UserProfileDAO(new MongoClient(), morphia, DBNAME);
		
		String departmentQuery="";
		
		//For each investigator (pi, copi, sp) in the list of them...
		//get their department, then from that department, get the desired position title (chair, dean, etc...)
		//and add those supervisors to the list
		//This may result in duplicate entries being added to the list but we will handle this with a nest for loop
		//Hopefully this does not result in a giant run time
		ArrayList<ObjectId> idList = new ArrayList<ObjectId>();
		for(InvestigatorRefAndPosition query : invList)
		{
			departmentQuery = query.getDepartment();
			List<UserProfile> tempList = getSupersDAO.getSupervisoryPersonnelByTitle(posTitle, departmentQuery);
			for(UserProfile profs : tempList)
			{
				if(!idList.contains(profs.getId()))
				{
					supervisorsList.add(profs);
				}
			}
		}
		
		int sigCount = 0;
		boolean isSigned = true;
		//For all the supervisors on the list, we need to get their status as signed or not signed.
		String department, pType, pTitle, college;
		ObjectId supID;
		List<SignatureInfo> checkSigs = this.getSignatureInfo();

		//2nd Find out all of their supervisory personnel
		for(UserProfile supervisor : supervisorsList)
		{
			for(SignatureInfo thisSig : checkSigs)
			{
				if(!thisSig.getUserProfileId().toString().equals(supervisor.getId().toString()))
				{
					isSigned = false;
				}
				if(!thisSig.getPositionTitle().equals(posTitle))
				{
					isSigned = false;
				}

				if(isSigned)
				{
					sigCount++;
				}
			}

		}
		
		//3rd Evaluate if these personnel have "signed" the proposal
		
		if(sigCount == checkSigs.size())
		{
			return true;
		}else
		{
			return false;	
		}
		
		
	}

}
