package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class CostShareInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("institutional committed")
	boolean institutionalCommitted;

	@Property("third party committed")
	boolean thirdPartyCommitted;

	public CostShareInfo() {

	}

	public boolean isInstitutionalCommitted() {
		return institutionalCommitted;
	}

	public void setInstitutionalCommitted(boolean institutionalCommitted) {
		this.institutionalCommitted = institutionalCommitted;
	}

	public boolean isThirdPartyCommitted() {
		return thirdPartyCommitted;
	}

	public void setThirdPartyCommitted(boolean thirdPartyCommitted) {
		this.thirdPartyCommitted = thirdPartyCommitted;
	}

	@Override
	public String toString() {
		return "CostShareInfo [institutionalCommitted="
				+ institutionalCommitted + ", thirdPartyCommitted="
				+ thirdPartyCommitted + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (institutionalCommitted ? 1231 : 1237);
		result = prime * result + (thirdPartyCommitted ? 1231 : 1237);
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
		CostShareInfo other = (CostShareInfo) obj;
		if (institutionalCommitted != other.institutionalCommitted)
			return false;
		if (thirdPartyCommitted != other.thirdPartyCommitted)
			return false;
		return true;
	}

}
