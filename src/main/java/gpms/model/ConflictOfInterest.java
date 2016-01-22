package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class ConflictOfInterest implements Serializable {
	// c_o_i == conflict_of_interest
	@Expose
	@Property("financial COI")
	private boolean financialCOI;

	// if financial_c_o_i == true
	@Expose
	@Property("conflict disclosed")
	private boolean conflictDisclosed;

	// if disclosure_form_change == true, disclosure must be updated
	@Expose
	@Property("disclosure form change")
	private boolean disclosureFormChange;

	public ConflictOfInterest() {
	}

	public boolean isFinancialCOI() {
		return financialCOI;
	}

	public void setFinancialCOI(boolean financialCOI) {
		this.financialCOI = financialCOI;
	}

	public boolean isConflictDisclosed() {
		return conflictDisclosed;
	}

	public void setConflictDisclosed(boolean conflictDisclosed) {
		this.conflictDisclosed = conflictDisclosed;
	}

	public boolean isDisclosureFormChange() {
		return disclosureFormChange;
	}

	public void setDisclosureFormChange(boolean disclosureFormChange) {
		this.disclosureFormChange = disclosureFormChange;
	}

	@Override
	public String toString() {
		return "ConflictOfInterest [financialCOI=" + financialCOI
				+ ", conflictDisclosed=" + conflictDisclosed
				+ ", disclosureFormChange=" + disclosureFormChange + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (conflictDisclosed ? 1231 : 1237);
		result = prime * result + (disclosureFormChange ? 1231 : 1237);
		result = prime * result + (financialCOI ? 1231 : 1237);
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
		ConflictOfInterest other = (ConflictOfInterest) obj;
		if (conflictDisclosed != other.conflictDisclosed)
			return false;
		if (disclosureFormChange != other.disclosureFormChange)
			return false;
		if (financialCOI != other.financialCOI)
			return false;
		return true;
	}

}