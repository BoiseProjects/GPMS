//Edited by: Hector C. Ortiz

package gpms.model;

import java.util.ArrayList;
import java.util.List;

//import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

import com.google.gson.annotations.Expose;

@Embedded
public class InvestigatorInfo {
	@Transient
	public final int MAX_NUM_CO_PI = 4;
	@Transient
	public final int MAX_NUM_SENIOR_PERSONNEL = 10;

	@Expose
	@Embedded("PI")
	private InvestigatorRefAndPosition pi = new InvestigatorRefAndPosition();
	@Expose
	@Embedded("CO-PI")
	private List<InvestigatorRefAndPosition> co_pi = new ArrayList<InvestigatorRefAndPosition>();
	@Expose
	@Embedded("senior personnel")
	private List<InvestigatorRefAndPosition> seniorPersonnel = new ArrayList<InvestigatorRefAndPosition>();

	public InvestigatorInfo() {
	}

	public InvestigatorRefAndPosition getPi() {
		return pi;
	}

	public void setPi(InvestigatorRefAndPosition pi) {
		this.pi = pi;
	}

	public List<InvestigatorRefAndPosition> getCo_pi() {
		return co_pi;
	}

	public void setCo_pi(ArrayList<InvestigatorRefAndPosition> co_pi) {
		if (co_pi.size() <= MAX_NUM_CO_PI) {
			this.co_pi = co_pi;
		}
	}

	public void addCo_pi(InvestigatorRefAndPosition co_pi) {
		if (!this.co_pi.contains(co_pi)) {
			if (this.co_pi.size() < MAX_NUM_CO_PI) {
				this.co_pi.add(co_pi);
			}
		}
	}

	public List<InvestigatorRefAndPosition> getSeniorPersonnel() {
		return seniorPersonnel;
	}

	public void addSeniorPersonnel(InvestigatorRefAndPosition seniorPersonnel) {
		if (this.seniorPersonnel.size() < MAX_NUM_SENIOR_PERSONNEL) {
			this.seniorPersonnel.add(seniorPersonnel);
		}
	}

	public void setSeniorPersonnel(
			ArrayList<InvestigatorRefAndPosition> seniorPersonnel) {
		if (seniorPersonnel.size() <= MAX_NUM_SENIOR_PERSONNEL) {
			this.seniorPersonnel = seniorPersonnel;
		}
	}

	@Override
	public String toString() {
		return "InvestigatorInfo [MAX_NUM_CO_PI=" + MAX_NUM_CO_PI
				+ ", MAX_NUM_SENIOR_PERSONNEL=" + MAX_NUM_SENIOR_PERSONNEL
				+ ", pi=" + pi + ", co_pi=" + co_pi + ", seniorPersonnel="
				+ seniorPersonnel + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + MAX_NUM_CO_PI;
		result = prime * result + MAX_NUM_SENIOR_PERSONNEL;
		result = prime * result + ((co_pi == null) ? 0 : co_pi.hashCode());
		result = prime * result + ((pi == null) ? 0 : pi.hashCode());
		result = prime * result
				+ ((seniorPersonnel == null) ? 0 : seniorPersonnel.hashCode());
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
		InvestigatorInfo other = (InvestigatorInfo) obj;
		if (MAX_NUM_CO_PI != other.MAX_NUM_CO_PI)
			return false;
		if (MAX_NUM_SENIOR_PERSONNEL != other.MAX_NUM_SENIOR_PERSONNEL)
			return false;
		if (co_pi == null) {
			if (other.co_pi != null)
				return false;
		} else if (!co_pi.equals(other.co_pi))
			return false;
		if (pi == null) {
			if (other.pi != null)
				return false;
		} else if (!pi.equals(other.pi))
			return false;
		if (seniorPersonnel == null) {
			if (other.seniorPersonnel != null)
				return false;
		} else if (!seniorPersonnel.equals(other.seniorPersonnel))
			return false;
		return true;
	}

	@Override
	public InvestigatorInfo clone() throws CloneNotSupportedException {
		InvestigatorInfo copy = new InvestigatorInfo();
		copy.setPi(this.pi.clone());

		for (InvestigatorRefAndPosition coPi : this.co_pi) {
			copy.addCo_pi(coPi.clone());
		}
		for (InvestigatorRefAndPosition seniorPersonnel : this.seniorPersonnel) {
			copy.addSeniorPersonnel(seniorPersonnel.clone());
		}
		return copy;
	}

}
