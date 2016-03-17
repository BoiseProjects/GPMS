package gpms.model;

public enum WithdrawType {
	WITHDRAWN("Withdrawn"), NOTWITHDRAWN("Not Withdrawn");

	private final String withdrawType;

	private WithdrawType(String name) {
		this.withdrawType = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : withdrawType.equals(otherName);
	}

	@Override
	public String toString() {
		return this.withdrawType;
	}
}
