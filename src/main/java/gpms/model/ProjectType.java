package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class ProjectType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("research-basic")
	private boolean researchBasic;

	@Property("research-applied")
	private boolean researchApplied;

	@Property("research-development")
	private boolean researchDevelopment;

	@Property("instruction")
	private boolean instruction;

	@Property("other sponsored activity")
	private boolean otherSponsoredActivity;

	public ProjectType() {

	}

	public boolean isResearchBasic() {
		return researchBasic;
	}

	public void setResearchBasic(boolean researchBasic) {
		this.researchBasic = researchBasic;
	}

	public boolean isResearchApplied() {
		return researchApplied;
	}

	public void setResearchApplied(boolean researchApplied) {
		this.researchApplied = researchApplied;
	}

	public boolean isResearchDevelopment() {
		return researchDevelopment;
	}

	public void setResearchDevelopment(boolean researchDevelopment) {
		this.researchDevelopment = researchDevelopment;
	}

	public boolean isInstruction() {
		return instruction;
	}

	public void setInstruction(boolean instruction) {
		this.instruction = instruction;
	}

	public boolean isOtherSponsoredActivity() {
		return otherSponsoredActivity;
	}

	public void setOtherSponsoredActivity(boolean otherSponsoredActivity) {
		this.otherSponsoredActivity = otherSponsoredActivity;
	}

	@Override
	public String toString() {
		return "ProjectType [researchBasic=" + researchBasic
				+ ", researchApplied=" + researchApplied
				+ ", researchDevelopment=" + researchDevelopment
				+ ", instruction=" + instruction + ", otherSponsoredActivity="
				+ otherSponsoredActivity + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (instruction ? 1231 : 1237);
		result = prime * result + (otherSponsoredActivity ? 1231 : 1237);
		result = prime * result + (researchApplied ? 1231 : 1237);
		result = prime * result + (researchBasic ? 1231 : 1237);
		result = prime * result + (researchDevelopment ? 1231 : 1237);
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
		ProjectType other = (ProjectType) obj;
		if (instruction != other.instruction)
			return false;
		if (otherSponsoredActivity != other.otherSponsoredActivity)
			return false;
		if (researchApplied != other.researchApplied)
			return false;
		if (researchBasic != other.researchBasic)
			return false;
		if (researchDevelopment != other.researchDevelopment)
			return false;
		return true;
	}

}
