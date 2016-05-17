package gpms.model;

public class SignatureByAllUsers {

	private boolean signedByPI = false;
	private boolean signedByAllCoPIs = false;
	private boolean signedByAllChairs = false;
	private boolean signedByAllBusinessManagers = false;
	private boolean signedByAllDeans = false;
	private boolean signedByAllIRBs = false;
	private boolean signedByAllResearchAdmins = false;
	private boolean signedByAllResearchDirectors = false;

	public SignatureByAllUsers() {

	}

	public boolean isSignedByPI() {
		return signedByPI;
	}

	public void setSignedByPI(boolean signedByPI) {
		this.signedByPI = signedByPI;
	}

	public boolean isSignedByAllCoPIs() {
		return signedByAllCoPIs;
	}

	public void setSignedByAllCoPIs(boolean signedByAllCoPIs) {
		this.signedByAllCoPIs = signedByAllCoPIs;
	}

	public boolean isSignedByAllChairs() {
		return signedByAllChairs;
	}

	public void setSignedByAllChairs(boolean signedByAllChairs) {
		this.signedByAllChairs = signedByAllChairs;
	}

	public boolean isSignedByAllBusinessManagers() {
		return signedByAllBusinessManagers;
	}

	public void setSignedByAllBusinessManagers(
			boolean signedByAllBusinessManagers) {
		this.signedByAllBusinessManagers = signedByAllBusinessManagers;
	}

	public boolean isSignedByAllDeans() {
		return signedByAllDeans;
	}

	public void setSignedByAllDeans(boolean signedByAllDeans) {
		this.signedByAllDeans = signedByAllDeans;
	}

	public boolean isSignedByAllIRBs() {
		return signedByAllIRBs;
	}

	public void setSignedByAllIRBs(boolean signedByAllIRBs) {
		this.signedByAllIRBs = signedByAllIRBs;
	}

	public boolean isSignedByAllResearchAdmins() {
		return signedByAllResearchAdmins;
	}

	public void setSignedByAllResearchAdmins(boolean signedByAllResearchAdmins) {
		this.signedByAllResearchAdmins = signedByAllResearchAdmins;
	}

	public boolean isSignedByAllResearchDirectors() {
		return signedByAllResearchDirectors;
	}

	public void setSignedByAllResearchDirectors(
			boolean signedByAllResearchDirectors) {
		this.signedByAllResearchDirectors = signedByAllResearchDirectors;
	}

	@Override
	public String toString() {
		return "SignatureByAllUsers [signedByPI=" + signedByPI
				+ ", signedByAllCoPIs=" + signedByAllCoPIs
				+ ", signedByAllChairs=" + signedByAllChairs
				+ ", signedByAllBusinessManagers="
				+ signedByAllBusinessManagers + ", signedByAllDeans="
				+ signedByAllDeans + ", signedByAllIRBs=" + signedByAllIRBs
				+ ", signedByAllResearchAdmins=" + signedByAllResearchAdmins
				+ ", signedByAllResearchDirectors="
				+ signedByAllResearchDirectors + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (signedByAllBusinessManagers ? 1231 : 1237);
		result = prime * result + (signedByAllChairs ? 1231 : 1237);
		result = prime * result + (signedByAllCoPIs ? 1231 : 1237);
		result = prime * result + (signedByAllDeans ? 1231 : 1237);
		result = prime * result + (signedByAllIRBs ? 1231 : 1237);
		result = prime * result + (signedByAllResearchAdmins ? 1231 : 1237);
		result = prime * result + (signedByAllResearchDirectors ? 1231 : 1237);
		result = prime * result + (signedByPI ? 1231 : 1237);
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
		SignatureByAllUsers other = (SignatureByAllUsers) obj;
		if (signedByAllBusinessManagers != other.signedByAllBusinessManagers)
			return false;
		if (signedByAllChairs != other.signedByAllChairs)
			return false;
		if (signedByAllCoPIs != other.signedByAllCoPIs)
			return false;
		if (signedByAllDeans != other.signedByAllDeans)
			return false;
		if (signedByAllIRBs != other.signedByAllIRBs)
			return false;
		if (signedByAllResearchAdmins != other.signedByAllResearchAdmins)
			return false;
		if (signedByAllResearchDirectors != other.signedByAllResearchDirectors)
			return false;
		if (signedByPI != other.signedByPI)
			return false;
		return true;
	}

}
