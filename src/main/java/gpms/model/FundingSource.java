package gpms.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class FundingSource {
	@Expose
	@Property("federal")
	private boolean federal;

	@Expose
	@Property("federal flow-through")
	private boolean federalFlowThrough;

	@Expose
	@Property("Sate of Idaho entity")
	private boolean stateOfIdahoEntity;

	@Expose
	@Property("private for profit")
	private boolean privateForProfit;

	@Expose
	@Property("non-profit organization")
	private boolean nonProfitOrganization;

	@Expose
	@Property("non-Idaho State entity")
	private boolean nonIdahoStateEntity;

	@Expose
	@Property("college or university")
	private boolean collegeOrUniversity;

	@Expose
	@Property("local entity")
	private boolean localEntity;

	@Expose
	@Property("non-Idaho local entity")
	private boolean nonIdahoLocalEntity;

	@Expose
	@Property("tirbal government")
	private boolean tirbalGovernment;

	@Expose
	@Property("foreign")
	private boolean foreign;

	public FundingSource() {
	}

	public boolean isFederal() {
		return federal;
	}

	public void setFederal(boolean federal) {
		this.federal = federal;
	}

	public boolean isFederalFlowThrough() {
		return federalFlowThrough;
	}

	public void setFederalFlowThrough(boolean federalFlowThrough) {
		this.federalFlowThrough = federalFlowThrough;
	}

	public boolean isStateOfIdahoEntity() {
		return stateOfIdahoEntity;
	}

	public void setStateOfIdahoEntity(boolean stateOfIdahoEntity) {
		this.stateOfIdahoEntity = stateOfIdahoEntity;
	}

	public boolean isPrivateForProfit() {
		return privateForProfit;
	}

	public void setPrivateForProfit(boolean privateForProfit) {
		this.privateForProfit = privateForProfit;
	}

	public boolean isNonProfitOrganization() {
		return nonProfitOrganization;
	}

	public void setNonProfitOrganization(boolean nonProfitOrganization) {
		this.nonProfitOrganization = nonProfitOrganization;
	}

	public boolean isNonIdahoStateEntity() {
		return nonIdahoStateEntity;
	}

	public void setNonIdahoStateEntity(boolean nonIdahoStateEntity) {
		this.nonIdahoStateEntity = nonIdahoStateEntity;
	}

	public boolean isCollegeOrUniversity() {
		return collegeOrUniversity;
	}

	public void setCollegeOrUniversity(boolean collegeOrUniversity) {
		this.collegeOrUniversity = collegeOrUniversity;
	}

	public boolean isLocalEntity() {
		return localEntity;
	}

	public void setLocalEntity(boolean localEntity) {
		this.localEntity = localEntity;
	}

	public boolean isNonIdahoLocalEntity() {
		return nonIdahoLocalEntity;
	}

	public void setNonIdahoLocalEntity(boolean nonIdahoLocalEntity) {
		this.nonIdahoLocalEntity = nonIdahoLocalEntity;
	}

	public boolean isTirbalGovernment() {
		return tirbalGovernment;
	}

	public void setTirbalGovernment(boolean tirbalGovernment) {
		this.tirbalGovernment = tirbalGovernment;
	}

	public boolean isForeign() {
		return foreign;
	}

	public void setForeign(boolean foreign) {
		this.foreign = foreign;
	}

	@Override
	public String toString() {
		return "FundingSource [federal=" + federal + ", federalFlowThrough="
				+ federalFlowThrough + ", stateOfIdahoEntity="
				+ stateOfIdahoEntity + ", privateForProfit=" + privateForProfit
				+ ", nonProfitOrganization=" + nonProfitOrganization
				+ ", nonIdahoStateEntity=" + nonIdahoStateEntity
				+ ", collegeOrUniversity=" + collegeOrUniversity
				+ ", localEntity=" + localEntity + ", nonIdahoLocalEntity="
				+ nonIdahoLocalEntity + ", tirbalGovernment="
				+ tirbalGovernment + ", foreign=" + foreign + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (collegeOrUniversity ? 1231 : 1237);
		result = prime * result + (federal ? 1231 : 1237);
		result = prime * result + (federalFlowThrough ? 1231 : 1237);
		result = prime * result + (foreign ? 1231 : 1237);
		result = prime * result + (localEntity ? 1231 : 1237);
		result = prime * result + (nonIdahoLocalEntity ? 1231 : 1237);
		result = prime * result + (nonIdahoStateEntity ? 1231 : 1237);
		result = prime * result + (nonProfitOrganization ? 1231 : 1237);
		result = prime * result + (privateForProfit ? 1231 : 1237);
		result = prime * result + (stateOfIdahoEntity ? 1231 : 1237);
		result = prime * result + (tirbalGovernment ? 1231 : 1237);
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
		FundingSource other = (FundingSource) obj;
		if (collegeOrUniversity != other.collegeOrUniversity)
			return false;
		if (federal != other.federal)
			return false;
		if (federalFlowThrough != other.federalFlowThrough)
			return false;
		if (foreign != other.foreign)
			return false;
		if (localEntity != other.localEntity)
			return false;
		if (nonIdahoLocalEntity != other.nonIdahoLocalEntity)
			return false;
		if (nonIdahoStateEntity != other.nonIdahoStateEntity)
			return false;
		if (nonProfitOrganization != other.nonProfitOrganization)
			return false;
		if (privateForProfit != other.privateForProfit)
			return false;
		if (stateOfIdahoEntity != other.stateOfIdahoEntity)
			return false;
		if (tirbalGovernment != other.tirbalGovernment)
			return false;
		return true;
	}

}
