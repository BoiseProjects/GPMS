package gpms.model;

public enum DeleteType {
	DELETED("Deleted"), NOTDELETED("Not Deleted");

	private final String deleteType;

	private DeleteType(String name) {
		this.deleteType = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : deleteType.equals(otherName);
	}

	@Override
	public String toString() {
		return this.deleteType;
	}
}
