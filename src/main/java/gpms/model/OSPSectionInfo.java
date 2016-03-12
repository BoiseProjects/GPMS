package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class OSPSectionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("list agency")
	private String listAgency = new String();

	@Embedded("funding source")
	private FundingSource fundingSource = new FundingSource();

	@Property("cfda no")
	private String cfdaNo = new String();

	@Property("program no")
	private String programNo = new String();

	@Property("program title")
	private String programTitle = new String();

	@Embedded("recovery")
	private Recovery recovery = new Recovery();

	@Embedded("base")
	private BaseInfo baseInfo = new BaseInfo();

	@Property("pi salary included")
	private boolean piSalaryIncluded;

	@Property("pi salary")
	private double piSalary;

	@Property("pi fringe")
	private double piFringe;

	@Property("department id")
	private String departmentId = new String();

	@Embedded("institutional cost share documented")
	private BaseOptions institutionalCostDocumented = new BaseOptions();

	@Embedded("third party cost share documented")
	private BaseOptions thirdPartyCostDocumented = new BaseOptions();

	@Property("anticipated subrecipients")
	private boolean anticipatedSubRecipients;

	@Property("anticipated subrecipients names")
	private String anticipatedSubRecipientsNames;

	@Embedded("pi eligibility waiver on file")
	private BasePIEligibilityOptions piEligibilityWaiver = new BasePIEligibilityOptions();

	@Embedded("conflict of interest forms on file")
	private BaseOptions conflictOfInterestForms = new BaseOptions();

	@Embedded("excluded party list checked")
	private BaseOptions excludedPartyListChecked = new BaseOptions();

	public OSPSectionInfo() {

	}

	public String getListAgency() {
		return listAgency;
	}

	public void setListAgency(String listAgency) {
		this.listAgency = listAgency;
	}

	public FundingSource getFundingSource() {
		return fundingSource;
	}

	public void setFundingSource(FundingSource fundingSource) {
		this.fundingSource = fundingSource;
	}

	public String getCfdaNo() {
		return cfdaNo;
	}

	public void setCfdaNo(String cfdaNo) {
		this.cfdaNo = cfdaNo;
	}

	public String getProgramNo() {
		return programNo;
	}

	public void setProgramNo(String programNo) {
		this.programNo = programNo;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public Recovery getRecovery() {
		return recovery;
	}

	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}

	public boolean isPiSalaryIncluded() {
		return piSalaryIncluded;
	}

	public void setPiSalaryIncluded(boolean piSalaryIncluded) {
		this.piSalaryIncluded = piSalaryIncluded;
	}

	public double getPiSalary() {
		return piSalary;
	}

	public void setPiSalary(double piSalary) {
		this.piSalary = piSalary;
	}

	public double getPiFringe() {
		return piFringe;
	}

	public void setPiFringe(double piFringe) {
		this.piFringe = piFringe;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public BaseOptions getInstitutionalCostDocumented() {
		return institutionalCostDocumented;
	}

	public void setInstitutionalCostDocumented(
			BaseOptions institutionalCostDocumented) {
		this.institutionalCostDocumented = institutionalCostDocumented;
	}

	public BaseOptions getThirdPartyCostDocumented() {
		return thirdPartyCostDocumented;
	}

	public void setThirdPartyCostDocumented(BaseOptions thirdPartyCostDocumented) {
		this.thirdPartyCostDocumented = thirdPartyCostDocumented;
	}

	public boolean isAnticipatedSubRecipients() {
		return anticipatedSubRecipients;
	}

	public void setAnticipatedSubRecipients(boolean anticipatedSubRecipients) {
		this.anticipatedSubRecipients = anticipatedSubRecipients;
	}

	public String getAnticipatedSubRecipientsNames() {
		return anticipatedSubRecipientsNames;
	}

	public void setAnticipatedSubRecipientsNames(
			String anticipatedSubRecipientsNames) {
		this.anticipatedSubRecipientsNames = anticipatedSubRecipientsNames;
	}

	public BasePIEligibilityOptions getPiEligibilityWaiver() {
		return piEligibilityWaiver;
	}

	public void setPiEligibilityWaiver(
			BasePIEligibilityOptions piEligibilityWaiver) {
		this.piEligibilityWaiver = piEligibilityWaiver;
	}

	public BaseOptions getConflictOfInterestForms() {
		return conflictOfInterestForms;
	}

	public void setConflictOfInterestForms(BaseOptions conflictOfInterestForms) {
		this.conflictOfInterestForms = conflictOfInterestForms;
	}

	public BaseOptions getExcludedPartyListChecked() {
		return excludedPartyListChecked;
	}

	public void setExcludedPartyListChecked(BaseOptions excludedPartyListChecked) {
		this.excludedPartyListChecked = excludedPartyListChecked;
	}

	@Override
	public String toString() {
		return "OSPSectionInfo [listAgency=" + listAgency + ", fundingSource="
				+ fundingSource + ", cfdaNo=" + cfdaNo + ", programNo="
				+ programNo + ", programTitle=" + programTitle + ", recovery="
				+ recovery + ", baseInfo=" + baseInfo + ", piSalaryIncluded="
				+ piSalaryIncluded + ", piSalary=" + piSalary + ", piFringe="
				+ piFringe + ", departmentId=" + departmentId
				+ ", institutionalCostDocumented="
				+ institutionalCostDocumented + ", thirdPartyCostDocumented="
				+ thirdPartyCostDocumented + ", anticipatedSubRecipients="
				+ anticipatedSubRecipients + ", anticipatedSubRecipientsNames="
				+ anticipatedSubRecipientsNames + ", piEligibilityWaiver="
				+ piEligibilityWaiver + ", conflictOfInterestForms="
				+ conflictOfInterestForms + ", excludedPartyListChecked="
				+ excludedPartyListChecked + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (anticipatedSubRecipients ? 1231 : 1237);
		result = prime
				* result
				+ ((anticipatedSubRecipientsNames == null) ? 0
						: anticipatedSubRecipientsNames.hashCode());
		result = prime * result
				+ ((baseInfo == null) ? 0 : baseInfo.hashCode());
		result = prime * result + ((cfdaNo == null) ? 0 : cfdaNo.hashCode());
		result = prime
				* result
				+ ((conflictOfInterestForms == null) ? 0
						: conflictOfInterestForms.hashCode());
		result = prime * result
				+ ((departmentId == null) ? 0 : departmentId.hashCode());
		result = prime
				* result
				+ ((excludedPartyListChecked == null) ? 0
						: excludedPartyListChecked.hashCode());
		result = prime * result
				+ ((fundingSource == null) ? 0 : fundingSource.hashCode());
		result = prime
				* result
				+ ((institutionalCostDocumented == null) ? 0
						: institutionalCostDocumented.hashCode());
		result = prime * result
				+ ((listAgency == null) ? 0 : listAgency.hashCode());
		result = prime
				* result
				+ ((piEligibilityWaiver == null) ? 0 : piEligibilityWaiver
						.hashCode());
		long temp;
		temp = Double.doubleToLongBits(piFringe);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(piSalary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (piSalaryIncluded ? 1231 : 1237);
		result = prime * result
				+ ((programNo == null) ? 0 : programNo.hashCode());
		result = prime * result
				+ ((programTitle == null) ? 0 : programTitle.hashCode());
		result = prime * result
				+ ((recovery == null) ? 0 : recovery.hashCode());
		result = prime
				* result
				+ ((thirdPartyCostDocumented == null) ? 0
						: thirdPartyCostDocumented.hashCode());
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
		OSPSectionInfo other = (OSPSectionInfo) obj;
		if (anticipatedSubRecipients != other.anticipatedSubRecipients)
			return false;
		if (anticipatedSubRecipientsNames == null) {
			if (other.anticipatedSubRecipientsNames != null)
				return false;
		} else if (!anticipatedSubRecipientsNames
				.equals(other.anticipatedSubRecipientsNames))
			return false;
		if (baseInfo == null) {
			if (other.baseInfo != null)
				return false;
		} else if (!baseInfo.equals(other.baseInfo))
			return false;
		if (cfdaNo == null) {
			if (other.cfdaNo != null)
				return false;
		} else if (!cfdaNo.equals(other.cfdaNo))
			return false;
		if (conflictOfInterestForms == null) {
			if (other.conflictOfInterestForms != null)
				return false;
		} else if (!conflictOfInterestForms
				.equals(other.conflictOfInterestForms))
			return false;
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (excludedPartyListChecked == null) {
			if (other.excludedPartyListChecked != null)
				return false;
		} else if (!excludedPartyListChecked
				.equals(other.excludedPartyListChecked))
			return false;
		if (fundingSource == null) {
			if (other.fundingSource != null)
				return false;
		} else if (!fundingSource.equals(other.fundingSource))
			return false;
		if (institutionalCostDocumented == null) {
			if (other.institutionalCostDocumented != null)
				return false;
		} else if (!institutionalCostDocumented
				.equals(other.institutionalCostDocumented))
			return false;
		if (listAgency == null) {
			if (other.listAgency != null)
				return false;
		} else if (!listAgency.equals(other.listAgency))
			return false;
		if (piEligibilityWaiver == null) {
			if (other.piEligibilityWaiver != null)
				return false;
		} else if (!piEligibilityWaiver.equals(other.piEligibilityWaiver))
			return false;
		if (Double.doubleToLongBits(piFringe) != Double
				.doubleToLongBits(other.piFringe))
			return false;
		if (Double.doubleToLongBits(piSalary) != Double
				.doubleToLongBits(other.piSalary))
			return false;
		if (piSalaryIncluded != other.piSalaryIncluded)
			return false;
		if (programNo == null) {
			if (other.programNo != null)
				return false;
		} else if (!programNo.equals(other.programNo))
			return false;
		if (programTitle == null) {
			if (other.programTitle != null)
				return false;
		} else if (!programTitle.equals(other.programTitle))
			return false;
		if (recovery == null) {
			if (other.recovery != null)
				return false;
		} else if (!recovery.equals(other.recovery))
			return false;
		if (thirdPartyCostDocumented == null) {
			if (other.thirdPartyCostDocumented != null)
				return false;
		} else if (!thirdPartyCostDocumented
				.equals(other.thirdPartyCostDocumented))
			return false;
		return true;
	}

}
