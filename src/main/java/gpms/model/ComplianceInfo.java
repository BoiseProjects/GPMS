package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class ComplianceInfo implements Cloneable, Serializable {
	@Expose
	@Property("involve use of human subjects")
	private boolean involveUseOfHumanSubjects;

	@Expose
	@Property("IRB")
	private String IRB = new String();

	@Expose
	@Property("IRB pending status")
	private boolean IRBPending;

	@Expose
	@Property("involve use of vertebrate animals")
	private boolean involveUseOfVertebrateAnimals;

	@Expose
	@Property("IACUC")
	private String IACUC = new String();

	@Expose
	@Property("IACUC pending status")
	private boolean IACUCPending;

	@Expose
	@Property("involve biosafety concerns")
	private boolean involveBiosafetyConcerns;

	@Expose
	@Property("IBC")
	private String IBC = new String();

	@Expose
	@Property("IBC pending status")
	private boolean IBCPending;

	@Expose
	@Property("involve environmental health and safety concerns")
	private boolean involveEnvironmentalHealthAndSafetyConcerns;

	public ComplianceInfo() {

	}

	public boolean isInvolveUseOfHumanSubjects() {
		return involveUseOfHumanSubjects;
	}

	public void setInvolveUseOfHumanSubjects(boolean involveUseOfHumanSubjects) {
		this.involveUseOfHumanSubjects = involveUseOfHumanSubjects;
	}

	public String getIRB() {
		return IRB;
	}

	public void setIRB(String iRB) {
		IRB = iRB;
	}

	public boolean isIRBPending() {
		return IRBPending;
	}

	public void setIRBPending(boolean iRBPending) {
		IRBPending = iRBPending;
	}

	public boolean isInvolveUseOfVertebrateAnimals() {
		return involveUseOfVertebrateAnimals;
	}

	public void setInvolveUseOfVertebrateAnimals(
			boolean involveUseOfVertebrateAnimals) {
		this.involveUseOfVertebrateAnimals = involveUseOfVertebrateAnimals;
	}

	public String getIACUC() {
		return IACUC;
	}

	public void setIACUC(String iACUC) {
		IACUC = iACUC;
	}

	public boolean isIACUCPending() {
		return IACUCPending;
	}

	public void setIACUCPending(boolean iACUCPending) {
		IACUCPending = iACUCPending;
	}

	public boolean isInvolveBiosafetyConcerns() {
		return involveBiosafetyConcerns;
	}

	public void setInvolveBiosafetyConcerns(boolean involveBiosafetyConcerns) {
		this.involveBiosafetyConcerns = involveBiosafetyConcerns;
	}

	public String getIBC() {
		return IBC;
	}

	public void setIBC(String iBC) {
		IBC = iBC;
	}

	public boolean isIBCPending() {
		return IBCPending;
	}

	public void setIBCPending(boolean iBCPending) {
		IBCPending = iBCPending;
	}

	public boolean isInvolveEnvironmentalHealthAndSafetyConcerns() {
		return involveEnvironmentalHealthAndSafetyConcerns;
	}

	public void setInvolveEnvironmentalHealthAndSafetyConcerns(
			boolean involveEnvironmentalHealthAndSafetyConcerns) {
		this.involveEnvironmentalHealthAndSafetyConcerns = involveEnvironmentalHealthAndSafetyConcerns;
	}

	@Override
	public String toString() {
		return "ComplianceInfo [involveUseOfHumanSubjects="
				+ involveUseOfHumanSubjects + ", IRB=" + IRB + ", IRBPending="
				+ IRBPending + ", involveUseOfVertebrateAnimals="
				+ involveUseOfVertebrateAnimals + ", IACUC=" + IACUC
				+ ", IACUCPending=" + IACUCPending
				+ ", involveBiosafetyConcerns=" + involveBiosafetyConcerns
				+ ", IBC=" + IBC + ", IBCPending=" + IBCPending
				+ ", involveEnvironmentalHealthAndSafetyConcerns="
				+ involveEnvironmentalHealthAndSafetyConcerns + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IACUC == null) ? 0 : IACUC.hashCode());
		result = prime * result + (IACUCPending ? 1231 : 1237);
		result = prime * result + ((IBC == null) ? 0 : IBC.hashCode());
		result = prime * result + (IBCPending ? 1231 : 1237);
		result = prime * result + ((IRB == null) ? 0 : IRB.hashCode());
		result = prime * result + (IRBPending ? 1231 : 1237);
		result = prime * result + (involveBiosafetyConcerns ? 1231 : 1237);
		result = prime * result
				+ (involveEnvironmentalHealthAndSafetyConcerns ? 1231 : 1237);
		result = prime * result + (involveUseOfHumanSubjects ? 1231 : 1237);
		result = prime * result + (involveUseOfVertebrateAnimals ? 1231 : 1237);
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
		ComplianceInfo other = (ComplianceInfo) obj;
		if (IACUC == null) {
			if (other.IACUC != null)
				return false;
		} else if (!IACUC.equals(other.IACUC))
			return false;
		if (IACUCPending != other.IACUCPending)
			return false;
		if (IBC == null) {
			if (other.IBC != null)
				return false;
		} else if (!IBC.equals(other.IBC))
			return false;
		if (IBCPending != other.IBCPending)
			return false;
		if (IRB == null) {
			if (other.IRB != null)
				return false;
		} else if (!IRB.equals(other.IRB))
			return false;
		if (IRBPending != other.IRBPending)
			return false;
		if (involveBiosafetyConcerns != other.involveBiosafetyConcerns)
			return false;
		if (involveEnvironmentalHealthAndSafetyConcerns != other.involveEnvironmentalHealthAndSafetyConcerns)
			return false;
		if (involveUseOfHumanSubjects != other.involveUseOfHumanSubjects)
			return false;
		if (involveUseOfVertebrateAnimals != other.involveUseOfVertebrateAnimals)
			return false;
		return true;
	}

	@Override
	protected ComplianceInfo clone() throws CloneNotSupportedException {
		ComplianceInfo copy = new ComplianceInfo();
		copy.setInvolveUseOfHumanSubjects(this.involveUseOfHumanSubjects);
		copy.setIRB(this.IRB);
		copy.setIRBPending(this.IRBPending);
		copy.setInvolveUseOfVertebrateAnimals(this.involveUseOfVertebrateAnimals);
		copy.setIACUC(this.IACUC);
		copy.setIACUCPending(this.IACUCPending);
		copy.setInvolveBiosafetyConcerns(this.involveBiosafetyConcerns);
		copy.setIBC(this.IBC);
		copy.setIBCPending(this.IBCPending);
		copy.setInvolveEnvironmentalHealthAndSafetyConcerns(this.involveEnvironmentalHealthAndSafetyConcerns);
		return copy;
	}

}
