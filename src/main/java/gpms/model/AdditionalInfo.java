package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class AdditionalInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("anticipates foreign nationals payment")
	private boolean anticipatesForeignNationalsPayment;

	@Property("anticipates course release time")
	private boolean anticipatesCourseReleaseTime;

	@Property("related to center for advanced energy studies")
	private boolean relatedToCenterForAdvancedEnergyStudies;

	public AdditionalInfo() {

	}

	public boolean isAnticipatesForeignNationalsPayment() {
		return anticipatesForeignNationalsPayment;
	}

	public void setAnticipatesForeignNationalsPayment(
			boolean anticipatesForeignNationalsPayment) {
		this.anticipatesForeignNationalsPayment = anticipatesForeignNationalsPayment;
	}

	public boolean isAnticipatesCourseReleaseTime() {
		return anticipatesCourseReleaseTime;
	}

	public void setAnticipatesCourseReleaseTime(
			boolean anticipatesCourseReleaseTime) {
		this.anticipatesCourseReleaseTime = anticipatesCourseReleaseTime;
	}

	public boolean isRelatedToCenterForAdvancedEnergyStudies() {
		return relatedToCenterForAdvancedEnergyStudies;
	}

	public void setRelatedToCenterForAdvancedEnergyStudies(
			boolean relatedToCenterForAdvancedEnergyStudies) {
		this.relatedToCenterForAdvancedEnergyStudies = relatedToCenterForAdvancedEnergyStudies;
	}

	@Override
	public String toString() {
		return "AdditionalInfo [anticipatesForeignNationalsPayment="
				+ anticipatesForeignNationalsPayment
				+ ", anticipatesCourseReleaseTime="
				+ anticipatesCourseReleaseTime
				+ ", relatedToCenterForAdvancedEnergyStudies="
				+ relatedToCenterForAdvancedEnergyStudies + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (anticipatesCourseReleaseTime ? 1231 : 1237);
		result = prime * result
				+ (anticipatesForeignNationalsPayment ? 1231 : 1237);
		result = prime * result
				+ (relatedToCenterForAdvancedEnergyStudies ? 1231 : 1237);
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
		AdditionalInfo other = (AdditionalInfo) obj;
		if (anticipatesCourseReleaseTime != other.anticipatesCourseReleaseTime)
			return false;
		if (anticipatesForeignNationalsPayment != other.anticipatesForeignNationalsPayment)
			return false;
		if (relatedToCenterForAdvancedEnergyStudies != other.relatedToCenterForAdvancedEnergyStudies)
			return false;
		return true;
	}

}
