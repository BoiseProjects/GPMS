package gpms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

//import org.bson.types.ObjectId;

@Embedded
public class SponsorAndBudgetInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("granting agency")
	private List<String> grantingAgency = new ArrayList<String>();

	@Property("direct costs")
	private double directCosts;

	@Property("fa costs")
	private double faCosts;

	@Property("total costs")
	private double totalCosts;

	@Property("fa rate")
	private double faRate;

	public SponsorAndBudgetInfo() {

	}

	public List<String> getGrantingAgency() {
		return grantingAgency;
	}

	public void setGrantingAgency(List<String> grantingAgency) {
		this.grantingAgency = grantingAgency;
	}

	public double getDirectCosts() {
		return directCosts;
	}

	public void setDirectCosts(double directCosts) {
		this.directCosts = directCosts;
	}

	public double getFaCosts() {
		return faCosts;
	}

	public void setFaCosts(double faCosts) {
		this.faCosts = faCosts;
	}

	public double getTotalCosts() {
		return totalCosts;
	}

	public void setTotalCosts(double totalCosts) {
		this.totalCosts = totalCosts;
	}

	public double getFaRate() {
		return faRate;
	}

	public void setFaRate(double faRate) {
		this.faRate = faRate;
	}

	@Override
	public String toString() {
		return "SponsorAndBudgetInfo [grantingAgency=" + grantingAgency
				+ ", directCosts=" + directCosts + ", faCosts=" + faCosts
				+ ", totalCosts=" + totalCosts + ", faRate=" + faRate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(directCosts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(faCosts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(faRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((grantingAgency == null) ? 0 : grantingAgency.hashCode());
		temp = Double.doubleToLongBits(totalCosts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		SponsorAndBudgetInfo other = (SponsorAndBudgetInfo) obj;
		if (Double.doubleToLongBits(directCosts) != Double
				.doubleToLongBits(other.directCosts))
			return false;
		if (Double.doubleToLongBits(faCosts) != Double
				.doubleToLongBits(other.faCosts))
			return false;
		if (Double.doubleToLongBits(faRate) != Double
				.doubleToLongBits(other.faRate))
			return false;
		if (grantingAgency == null) {
			if (other.grantingAgency != null)
				return false;
		} else if (!grantingAgency.equals(other.grantingAgency))
			return false;
		if (Double.doubleToLongBits(totalCosts) != Double
				.doubleToLongBits(other.totalCosts))
			return false;
		return true;
	}

}
