package gpms.model;

public enum Status {
	// NEW value, REVIEWED, APPROVED, DISAPPROVED, PENDING, CONFIRMED, DELETED,
	// CLOSED, ARCHIVED, WITHDRAWN, SUBMITTED
	// Not Submitted by PI, Waiting for Chair Approval, Waiting for Dean\'s
	// Approval, Internal Withdraw by PI, Reviewed by Research Office,
	// Submitted by Research Office, Awarded,Withdraw by Research Office,PI has
	// granted the permission,Approved by Department Chair, Approved by Business
	// manager

	NOTSUBMITTEDBYPI("Not Submitted by PI"), DELETEDBYPI("Deleted by PI"), WAITINGFORCHAIRAPPROVAL(
			"Waiting for Chair's Approval"), READYFORREVIEW("Ready for Review"), RETURNEDBYCHAIR(
			"Returned by Chair"), REVIEWEDBYBUSINESSMANAGER(
			"Reviewed by Business Manager"), DISAPPROVEDBYBUSINESSMANAGER(
			"Disapproved by Business Manager"), APPROVEDBYIRB("Approved by IRB"), DISAPPROVEDBYIRB(
			"Disapproved by IRB"), APPROVEDBYDEAN("Approved by Dean"), RETURNEDBYDEAN(
			"Returned by Dean"), SUBMITTEDTORESEARCHDIRECTOR(
			"Submitted to University Research Director"), DISAPPROVEDBYRESEARCHADMIN(
			"Disapproved by University Research Administrator"), WITHDRAWBYRESEARCHADMIN(
			"Withdrawn by University Research Administrator"), READYFORSUBMISSION(
			"Ready for submission"), DELETEDBYRESEARCHDIRECTOR(
			"Deleted by University Research Director"), DISAPPROVEDBYRESEARCHDIRECTOR(
			"Disapproved by University Research Director"), SUBMITTEDBYRESEARCHADMIN(
			"Submitted by University Research Administrator"), ARCHIVEDBYRESEARCHDIRECTOR(
			"Archived by University Research Director");

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
