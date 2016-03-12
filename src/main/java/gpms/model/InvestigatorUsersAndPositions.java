package gpms.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(Include.NON_NULL)
//@JsonSerialize(as = InvestigatorUsersAndPositions.class)
//@JsonDeserialize(as = InvestigatorUsersAndPositions.class)
public class InvestigatorUsersAndPositions {
	// @JsonProperty
	private String id;
	// @JsonProperty
	private String fullName;
	// @JsonProperty
	private String mobileNumber = new String();
	// @JsonProperty
	private Multimap<String, Object> positions = ArrayListMultimap.create();

	public InvestigatorUsersAndPositions() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Multimap<String, Object> getPositions() {
		return positions;
	}

	public void setPositions(Multimap<String, Object> positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "InvestigatorUsersAndPositions [id=" + id + ", fullName="
				+ fullName + ", mobileNumber=" + mobileNumber + ", positions="
				+ positions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
		result = prime * result
				+ ((positions == null) ? 0 : positions.hashCode());
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
		InvestigatorUsersAndPositions other = (InvestigatorUsersAndPositions) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobileNumber == null) {
			if (other.mobileNumber != null)
				return false;
		} else if (!mobileNumber.equals(other.mobileNumber))
			return false;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		return true;
	}

}
