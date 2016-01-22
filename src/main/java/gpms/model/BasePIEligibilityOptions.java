package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

public class BasePIEligibilityOptions implements Serializable {
	@Expose
	@Property("yes")
	private boolean yes;

	@Expose
	@Property("no")
	private boolean no;

	@Expose
	@Property("not applicable")
	private boolean notApplicable;

	@Expose
	@Property("this proposal only")
	private boolean thisProposalOnly;

	@Expose
	@Property("blanket")
	private boolean blanket;

	public BasePIEligibilityOptions() {
	}

	public boolean isYes() {
		return yes;
	}

	public void setYes(boolean yes) {
		if (!this.yes && yes) {
			this.yes = yes;
			no = false;
			notApplicable = false;
			thisProposalOnly = false;
			blanket = false;
		}
	}

	public boolean isNo() {
		return no;
	}

	public void setNo(boolean no) {
		if (!this.no && no) {
			yes = false;
			this.no = no;
			notApplicable = false;
			thisProposalOnly = false;
			blanket = false;
		}
	}

	public boolean isNotApplicable() {
		return notApplicable;
	}

	public void setNotApplicable(boolean notApplicable) {
		if (!this.notApplicable && notApplicable) {
			yes = false;
			no = false;
			this.notApplicable = notApplicable;
			thisProposalOnly = false;
			blanket = false;
		}
	}

	public boolean isThisProposalOnly() {
		return thisProposalOnly;
	}

	public void setThisProposalOnly(boolean thisProposalOnly) {
		if (!this.thisProposalOnly && thisProposalOnly) {
			yes = false;
			no = false;
			notApplicable = false;
			this.thisProposalOnly = thisProposalOnly;
			blanket = false;
		}
	}

	public boolean isBlanket() {
		return blanket;
	}

	public void setBlanket(boolean blanket) {
		if (!this.blanket && blanket) {
			yes = false;
			no = false;
			notApplicable = false;
			thisProposalOnly = false;
			this.blanket = blanket;
		}
	}

	@Override
	public String toString() {
		return "BasePIEligibilityOptions [yes=" + yes + ", no=" + no
				+ ", notApplicable=" + notApplicable + ", thisProposalOnly="
				+ thisProposalOnly + ", blanket=" + blanket + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (blanket ? 1231 : 1237);
		result = prime * result + (no ? 1231 : 1237);
		result = prime * result + (notApplicable ? 1231 : 1237);
		result = prime * result + (thisProposalOnly ? 1231 : 1237);
		result = prime * result + (yes ? 1231 : 1237);
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
		BasePIEligibilityOptions other = (BasePIEligibilityOptions) obj;
		if (blanket != other.blanket)
			return false;
		if (no != other.no)
			return false;
		if (notApplicable != other.notApplicable)
			return false;
		if (thisProposalOnly != other.thisProposalOnly)
			return false;
		if (yes != other.yes)
			return false;
		return true;
	}

}
