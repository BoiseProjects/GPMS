package gpms.model;

public class UserProposalCount {

	private int totalProposalCount = 0, piCount = 0, coPICount = 0,
			seniorCount = 0;

	public UserProposalCount() {
	
	}

	public int getTotalProposalCount() {
		return totalProposalCount;
	}

	public void setTotalProposalCount(int totalProposalCount) {
		this.totalProposalCount = totalProposalCount;
	}

	public int getPiCount() {
		return piCount;
	}

	public void setPiCount(int piCount) {
		this.piCount = piCount;
	}

	public int getCoPICount() {
		return coPICount;
	}

	public void setCoPICount(int coPICount) {
		this.coPICount = coPICount;
	}

	public int getSeniorCount() {
		return seniorCount;
	}

	public void setSeniorCount(int seniorCount) {
		this.seniorCount = seniorCount;
	}

}
