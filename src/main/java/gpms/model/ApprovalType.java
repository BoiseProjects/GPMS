package gpms.model;

public enum ApprovalType {

	APPROVED("Approved"), DISAPPROVED("Disapproved"), READYFORAPPROVAL(
			"Ready for Approval"), NOTREADYFORAPPROVAL("Not Ready for Approval");

	private final String approvalType;

	private ApprovalType(String name) {
		this.approvalType = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : approvalType.equals(otherName);
	}

	@Override
	public String toString() {
		return this.approvalType;
	}
}
