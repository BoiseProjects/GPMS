package gpms.model;

public enum ArchiveType {
	ARCHIVED("Archived"), NOTARCHIVED("Not Archived");

	private final String archiveType;

	private ArchiveType(String name) {
		this.archiveType = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : archiveType.equals(otherName);
	}

	@Override
	public String toString() {
		return this.archiveType;
	}
}
