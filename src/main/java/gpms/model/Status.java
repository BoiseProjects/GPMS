package gpms.model;

public enum Status {
	// NEW value, REVIEWED, APPROVED, DISAPPROVED, PENDING, CONFIRMED, DELETED,
	// CLOSED, ARCHIVED, WITHDRAWN, SUBMITTED
	// Not Submitted by PI, Waiting for Chair Approval, Waiting for Dean\'s
	// Approval, Internal Withdraw by PI, Reviewed by Research Office,
	// Submitted by Research Office, Awarded,Withdraw by Research Office,PI has
	// granted the permission,Approved by Department Chair, Approved by Business
	// manager

	NOTSUBMITTEDBYPI("Not Submitted by PI"), READYFORSUBMITBYPI(
			"Ready for Submit by PI"), DELETEDBYPI("Deleted by PI"), WAITINGFORCHAIRAPPROVAL(
			"Waiting for Chair's Approval"), RETURNEDBYCHAIR(
			"Returned by Chair"), READYFORREVIEWBYBUSINESSMANAGER(
			"Ready for Review by Business Manager"), WAITINGFORDEANAPPROVAL(
			"Waiting for Dean's Approval"), DISAPPROVEDBYBUSINESSMANAGER(
			"Disapproved by Business Manager"), READYFORREVIEWBYIRB(
			"Ready for Review by IRB"), REVIEWEDBYIRB("Reviewed by IRB"), DISAPPROVEDBYIRB(
			"Disapproved by IRB"), APPROVEDBYDEAN("Approved by Dean"), RETURNEDBYDEAN(
			"Returned by Dean"), WAITINGFORRESEARCHADMINAPPROVAL(
			"Waiting for Research Administrator's Approval"), WAITINGFORRESEARCHDIRECTORAPPROVAL(
			"Waiting for Research Director's Approval"), DISAPPROVEDBYRESEARCHADMIN(
			"Disapproved by Research Administrator"), WITHDRAWBYRESEARCHADMIN(
			"Withdrawn by Research Administrator"), READYFORSUBMISSION(
			"Ready for Submission"), DISAPPROVEDBYRESEARCHDIRECTOR(
			"Disapproved by Research Director"), DELETEDBYRESEARCHDIRECTOR(
			"Deleted by Research Director"), SUBMITTEDBYRESEARCHADMIN(
			"Submitted by Research Administrator"), ARCHIVEDBYRESEARCHDIRECTOR(
			"Archived by Research Director");

	private final String name;

	private Status(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
