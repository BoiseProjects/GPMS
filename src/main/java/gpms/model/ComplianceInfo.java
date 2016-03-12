package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class ComplianceInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("involve use of human subjects")
	private boolean involveUseOfHumanSubjects;

	@Property("irb")
	private String irb = new String();

	@Property("irb pending status")
	private boolean irbPending;

	@Property("involve use of vertebrate animals")
	private boolean involveUseOfVertebrateAnimals;

	@Property("iacuc")
	private String iacuc = new String();

	@Property("iacuc pending status")
	private boolean iacucPending;

	@Property("involve biosafety concerns")
	private boolean involveBiosafetyConcerns;

	@Property("ibc")
	private String ibc = new String();

	@Property("ibc pending status")
	private boolean ibcPending;

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

	public String getIrb() {
		return irb;
	}

	public void setIrb(String irb) {
		this.irb = irb;
	}

	public boolean isIrbPending() {
		return irbPending;
	}

	public void setIrbPending(boolean irbPending) {
		this.irbPending = irbPending;
	}

	public boolean isInvolveUseOfVertebrateAnimals() {
		return involveUseOfVertebrateAnimals;
	}

	public void setInvolveUseOfVertebrateAnimals(
			boolean involveUseOfVertebrateAnimals) {
		this.involveUseOfVertebrateAnimals = involveUseOfVertebrateAnimals;
	}

	public String getIacuc() {
		return iacuc;
	}

	public void setIacuc(String iacuc) {
		this.iacuc = iacuc;
	}

	public boolean isIacucPending() {
		return iacucPending;
	}

	public void setIacucPending(boolean iacucPending) {
		this.iacucPending = iacucPending;
	}

	public boolean isInvolveBiosafetyConcerns() {
		return involveBiosafetyConcerns;
	}

	public void setInvolveBiosafetyConcerns(boolean involveBiosafetyConcerns) {
		this.involveBiosafetyConcerns = involveBiosafetyConcerns;
	}

	public String getIbc() {
		return ibc;
	}

	public void setIbc(String ibc) {
		this.ibc = ibc;
	}

	public boolean isIbcPending() {
		return ibcPending;
	}

	public void setIbcPending(boolean ibcPending) {
		this.ibcPending = ibcPending;
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
				+ involveUseOfHumanSubjects + ", irb=" + irb + ", irbPending="
				+ irbPending + ", involveUseOfVertebrateAnimals="
				+ involveUseOfVertebrateAnimals + ", iacuc=" + iacuc
				+ ", iacucPending=" + iacucPending
				+ ", involveBiosafetyConcerns=" + involveBiosafetyConcerns
				+ ", ibc=" + ibc + ", ibcPending=" + ibcPending
				+ ", involveEnvironmentalHealthAndSafetyConcerns="
				+ involveEnvironmentalHealthAndSafetyConcerns + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iacuc == null) ? 0 : iacuc.hashCode());
		result = prime * result + (iacucPending ? 1231 : 1237);
		result = prime * result + ((ibc == null) ? 0 : ibc.hashCode());
		result = prime * result + (ibcPending ? 1231 : 1237);
		result = prime * result + (involveBiosafetyConcerns ? 1231 : 1237);
		result = prime * result
				+ (involveEnvironmentalHealthAndSafetyConcerns ? 1231 : 1237);
		result = prime * result + (involveUseOfHumanSubjects ? 1231 : 1237);
		result = prime * result + (involveUseOfVertebrateAnimals ? 1231 : 1237);
		result = prime * result + ((irb == null) ? 0 : irb.hashCode());
		result = prime * result + (irbPending ? 1231 : 1237);
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
		if (iacuc == null) {
			if (other.iacuc != null)
				return false;
		} else if (!iacuc.equals(other.iacuc))
			return false;
		if (iacucPending != other.iacucPending)
			return false;
		if (ibc == null) {
			if (other.ibc != null)
				return false;
		} else if (!ibc.equals(other.ibc))
			return false;
		if (ibcPending != other.ibcPending)
			return false;
		if (involveBiosafetyConcerns != other.involveBiosafetyConcerns)
			return false;
		if (involveEnvironmentalHealthAndSafetyConcerns != other.involveEnvironmentalHealthAndSafetyConcerns)
			return false;
		if (involveUseOfHumanSubjects != other.involveUseOfHumanSubjects)
			return false;
		if (involveUseOfVertebrateAnimals != other.involveUseOfVertebrateAnimals)
			return false;
		if (irb == null) {
			if (other.irb != null)
				return false;
		} else if (!irb.equals(other.irb))
			return false;
		if (irbPending != other.irbPending)
			return false;
		return true;
	}

}
