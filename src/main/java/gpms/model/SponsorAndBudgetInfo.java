package gpms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

//import org.bson.types.ObjectId;

@Embedded
public class SponsorAndBudgetInfo implements Serializable {
	@Expose
	@Property("granting agency")
	private List<String> grantingAgency = new ArrayList<String>();

	@Expose
	@Property("direct costs")
	private double directCosts;

	@Expose
	@Property("F&A costs")
	private double FACosts;

	@Expose
	@Property("total costs")
	private double totalCosts;

	@Expose
	@Property("F&A rate")
	private double FARate;

	public SponsorAndBudgetInfo() {
	}

	public List<String> getGrantingAgency() {
		return grantingAgency;
	}

	public void setGrantingAgency(List<String> grantingAgency) {
		this.grantingAgency = grantingAgency;
	}

	public void addGrantingAgency(String agencyName) {
		this.grantingAgency.add(agencyName);
	}

	public double getDirectCosts() {
		return directCosts;
	}

	public void setDirectCosts(double directCosts) {
		this.directCosts = directCosts;
	}

	public double getFACosts() {
		return FACosts;
	}

	public void setFACosts(double fACosts) {
		FACosts = fACosts;
	}

	public double getTotalCosts() {
		return totalCosts;
	}

	public void setTotalCosts(double totalCosts) {
		this.totalCosts = totalCosts;
	}

	public double getFARate() {
		return FARate;
	}

	public void setFARate(double fARate) {
		FARate = fARate;
	}

	@Override
	public String toString() {
		String outPut = "";
		outPut += "Granting Agency : " + grantingAgency.toString() + "\n";
		outPut += "Direct Costs    : " + directCosts + "\n";
		outPut += "F&A costs       : " + FACosts + "\n";
		outPut += "Total Costs     : " + totalCosts + "\n";
		outPut += "F&A rate        : " + FARate;
		return outPut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(FACosts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(FARate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(directCosts);
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
		if (Double.doubleToLongBits(FACosts) != Double
				.doubleToLongBits(other.FACosts))
			return false;
		if (Double.doubleToLongBits(FARate) != Double
				.doubleToLongBits(other.FARate))
			return false;
		if (Double.doubleToLongBits(directCosts) != Double
				.doubleToLongBits(other.directCosts))
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
