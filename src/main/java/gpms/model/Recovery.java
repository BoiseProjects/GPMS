package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class Recovery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("full recovery")
	private boolean fullRecovery;

	@Property("no recovery normal sponsor policy")
	private boolean noRecoveryNormalSponsorPolicy;

	@Property("no recovery institutional waiver")
	private boolean noRecoveryInstitutionalWaiver;

	@Property("limited recovery normal sponsor policy")
	private boolean limitedRecoveryNormalSponsorPolicy;

	@Property("limited recovery institutional waiver")
	private boolean limitedRecoveryInstitutionalWaiver;

	public Recovery() {

	}

	public boolean isFullRecovery() {
		return fullRecovery;
	}

	public void setFullRecovery(boolean fullRecovery) {
		this.fullRecovery = fullRecovery;
	}

	public boolean isNoRecoveryNormalSponsorPolicy() {
		return noRecoveryNormalSponsorPolicy;
	}

	public void setNoRecoveryNormalSponsorPolicy(
			boolean noRecoveryNormalSponsorPolicy) {
		this.noRecoveryNormalSponsorPolicy = noRecoveryNormalSponsorPolicy;
	}

	public boolean isNoRecoveryInstitutionalWaiver() {
		return noRecoveryInstitutionalWaiver;
	}

	public void setNoRecoveryInstitutionalWaiver(
			boolean noRecoveryInstitutionalWaiver) {
		this.noRecoveryInstitutionalWaiver = noRecoveryInstitutionalWaiver;
	}

	public boolean isLimitedRecoveryNormalSponsorPolicy() {
		return limitedRecoveryNormalSponsorPolicy;
	}

	public void setLimitedRecoveryNormalSponsorPolicy(
			boolean limitedRecoveryNormalSponsorPolicy) {
		this.limitedRecoveryNormalSponsorPolicy = limitedRecoveryNormalSponsorPolicy;
	}

	public boolean isLimitedRecoveryInstitutionalWaiver() {
		return limitedRecoveryInstitutionalWaiver;
	}

	public void setLimitedRecoveryInstitutionalWaiver(
			boolean limitedRecoveryInstitutionalWaiver) {
		this.limitedRecoveryInstitutionalWaiver = limitedRecoveryInstitutionalWaiver;
	}

	@Override
	public String toString() {
		return "Recovery [fullRecovery=" + fullRecovery
				+ ", noRecoveryNormalSponsorPolicy="
				+ noRecoveryNormalSponsorPolicy
				+ ", noRecoveryInstitutionalWaiver="
				+ noRecoveryInstitutionalWaiver
				+ ", limitedRecoveryNormalSponsorPolicy="
				+ limitedRecoveryNormalSponsorPolicy
				+ ", limitedRecoveryInstitutionalWaiver="
				+ limitedRecoveryInstitutionalWaiver + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fullRecovery ? 1231 : 1237);
		result = prime * result
				+ (limitedRecoveryInstitutionalWaiver ? 1231 : 1237);
		result = prime * result
				+ (limitedRecoveryNormalSponsorPolicy ? 1231 : 1237);
		result = prime * result + (noRecoveryInstitutionalWaiver ? 1231 : 1237);
		result = prime * result + (noRecoveryNormalSponsorPolicy ? 1231 : 1237);
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
		Recovery other = (Recovery) obj;
		if (fullRecovery != other.fullRecovery)
			return false;
		if (limitedRecoveryInstitutionalWaiver != other.limitedRecoveryInstitutionalWaiver)
			return false;
		if (limitedRecoveryNormalSponsorPolicy != other.limitedRecoveryNormalSponsorPolicy)
			return false;
		if (noRecoveryInstitutionalWaiver != other.noRecoveryInstitutionalWaiver)
			return false;
		if (noRecoveryNormalSponsorPolicy != other.noRecoveryNormalSponsorPolicy)
			return false;
		return true;
	}

}
