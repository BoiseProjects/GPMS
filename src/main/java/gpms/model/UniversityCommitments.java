//Written by: Hector C. Ortiz

package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class UniversityCommitments implements Cloneable, Serializable {
	@Expose
	@Property("new renovated facilities required")
	private boolean newRenovatedFacilitiesRequired;

	@Expose
	@Property("rental space required")
	private boolean rentalSpaceRequired;

	@Expose
	@Property("institutional commitment required")
	private boolean institutionalCommitmentRequired;

	public UniversityCommitments() {
	}

	public boolean isNewRenovatedFacilitiesRequired() {
		return newRenovatedFacilitiesRequired;
	}

	public void setNewRenovatedFacilitiesRequired(
			boolean newRenovatedFacilitiesRequired) {
		this.newRenovatedFacilitiesRequired = newRenovatedFacilitiesRequired;
	}

	public boolean isRentalSpaceRequired() {
		return rentalSpaceRequired;
	}

	public void setRentalSpaceRequired(boolean rentalSpaceRequired) {
		this.rentalSpaceRequired = rentalSpaceRequired;
	}

	public boolean isInstitutionalCommitmentRequired() {
		return institutionalCommitmentRequired;
	}

	public void setInstitutionalCommitmentRequired(
			boolean institutionalCommitmentRequired) {
		this.institutionalCommitmentRequired = institutionalCommitmentRequired;
	}

	@Override
	public String toString() {
		return "UniversityCommitments [newRenovatedFacilitiesRequired="
				+ newRenovatedFacilitiesRequired + ", rentalSpaceRequired="
				+ rentalSpaceRequired + ", institutionalCommitmentRequired="
				+ institutionalCommitmentRequired + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (institutionalCommitmentRequired ? 1231 : 1237);
		result = prime * result
				+ (newRenovatedFacilitiesRequired ? 1231 : 1237);
		result = prime * result + (rentalSpaceRequired ? 1231 : 1237);
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
		UniversityCommitments other = (UniversityCommitments) obj;
		if (institutionalCommitmentRequired != other.institutionalCommitmentRequired)
			return false;
		if (newRenovatedFacilitiesRequired != other.newRenovatedFacilitiesRequired)
			return false;
		if (rentalSpaceRequired != other.rentalSpaceRequired)
			return false;
		return true;
	}

	@Override
	protected UniversityCommitments clone() throws CloneNotSupportedException {
		UniversityCommitments copy = new UniversityCommitments();
		copy.setNewRenovatedFacilitiesRequired(this.newRenovatedFacilitiesRequired);
		copy.setRentalSpaceRequired(this.rentalSpaceRequired);
		copy.setInstitutionalCommitmentRequired(this.institutionalCommitmentRequired);
		return copy;
	}

}
