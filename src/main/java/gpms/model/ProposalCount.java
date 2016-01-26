package gpms.model;

/**
 * This class will allow for a retrieval of the proposal counts for a user based
 * on the selected role ie: User Role = Research Associate Return: Count of
 * Proposals Where User is RA
 * 
 * @author Thomas Volz
 *
 */
public class ProposalCount {

	private int total = 0, pi = 0, copi = 0, senior = 0;

	public ProposalCount() {
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPi() {
		return pi;
	}

	public void setPi(int pi) {
		this.pi = pi;
	}

	public int getCopi() {
		return copi;
	}

	public void setCopi(int copi) {
		this.copi = copi;
	}

	public int getSenior() {
		return senior;
	}

	public void setSenior(int senior) {
		this.senior = senior;
	}

}
