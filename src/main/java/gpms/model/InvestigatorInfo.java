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
		int count = 0;
		String outPut = "";
		outPut += "PI               : " + "\n";
		outPut += pi.toString() + "\n";
		outPut += "CO-PI            : " + "\n";
		for (InvestigatorRefAndPosition coPi : co_pi) {
			outPut += "Co-Pi number : " + count + "\n";
			outPut += coPi.toString() + "\n";
			count++;
		}
		count = 0;
		outPut += "senior personnel : " + "\n";
		for (InvestigatorRefAndPosition sp : seniorPersonnel) {
			outPut += "Senior Personel number : " + count + "\n";
			outPut += sp.toString() + "\n";
			count++;
		}
		return outPut;
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

	public boolean equals(InvestigatorInfo invInf) {

		boolean coPiEqual = false;
		if (this.co_pi.size() == invInf.co_pi.size()) {
			coPiEqual = true;
			for (int i = 0; i < this.co_pi.size(); i++) {
				if (!this.co_pi.get(i).equals(invInf.co_pi.get(i))) {
					coPiEqual = false;
					break;
				}
			}
		}

		boolean seniorPersonnelEqual = false;
		if (this.seniorPersonnel.size() == invInf.seniorPersonnel.size()) {
			seniorPersonnelEqual = true;
			for (int i = 0; i < this.seniorPersonnel.size(); i++) {
				if (!this.seniorPersonnel.get(i).equals(
						invInf.seniorPersonnel.get(i))) {
					seniorPersonnelEqual = false;
					break;
				}
			}
		}

		return this.pi.equals(invInf.getPi()) && coPiEqual
				&& seniorPersonnelEqual;
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
