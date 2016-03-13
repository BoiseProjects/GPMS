package gpms.model;

public enum ReadyType {

	READYFORSSUBMIT("Ready for Submit"), NOTREADYFORSUBMIT(
			"Not Ready for Submit");

	private final String readyType;

	private ReadyType(String name) {
		this.readyType = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : readyType.equals(otherName);
	}

	@Override
	public String toString() {
		return this.readyType;
	}
}
