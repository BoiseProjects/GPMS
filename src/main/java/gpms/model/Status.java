package gpms.model;

public enum Status {
	// NEW value, REVIEWED, APPROVED, DISAPPROVED, PENDING, CONFIRMED, DELETED,
	// CLOSED, ARCHIVED, WITHDRAWN, SUBMITTED
	// Not Submitted by PI, Waiting for Chair Approval, Waiting for Dean\'s
	// Approval, Internal Withdraw by PI, Reviewed by Research Office,
	// Submitted by Research Office, Awarded,Withdraw by Research Office,PI has
	// granted the permission,Approved by Department Chair, Approved by Business
	// manager

	NOTSUBMITTEDBYPI("Not Submitted by PI"), WAITINGFORCHAIRAPPROVAL(
			"Waiting for Chair Approval"), WAITINGFORDEANAPPROVAL(
			"Waiting for Dean's Approval"), WAITINGFORUNIVERSITYRESEARCHOFFICEAPPROVAL(
			"Waiting for University Research Office's Approval"), DELETEDBYPI(
			"Deleted by PI"), REVIEWEDBYRESEARCHOFFICE(
			"Reviewed by Research Office"), SUBMITTEDBYRESEARCHOFFICE(
			"Submitted by Research Office"), AWARDED("Awarded"), WITHDRAWBYRESEARCHOFFICE(
			"Withdraw by Research Office"), PIHASGRANTEDTHEPERMISSION(
			"PI has granted the permission"), APPROVEDBYBUSINESSMANAGER(
			"Approved by Business Manager"), APPROVEDBYDEPARTMENTCHAIR(
			"Approved by Department Chair"), APPROVEDBYDEAN("Approved by Dean"), APPROVEDBYUNIVERSITYRESEARCHDIRECTOR(
			"Approved by University Research Director"), ARCHIVEDBYUNIVERSITYRESEARCHDIRECTOR(
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
