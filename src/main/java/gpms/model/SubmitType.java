package gpms.model;

public enum SubmitType {

	SUBMITTED("Submitted"), NOTSUBMITTED("Not Submitted");

	private final String submitType;

	private SubmitType(String name) {
		this.submitType = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : submitType.equals(otherName);
	}

	@Override
	public String toString() {
		return this.submitType;
	}
}
