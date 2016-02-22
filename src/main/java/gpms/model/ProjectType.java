package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class ProjectType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@Property("research-basic")
	private boolean isResearchBasic;

	@Expose
	@Property("research-applied")
	private boolean isResearchApplied;

	@Expose
	@Property("research-development")
	private boolean isResearchDevelopment;

	@Expose
	@Property("instruction")
	private boolean isInstruction;

	@Expose
	@Property("other sponsored activity")
	private boolean isOtherSponsoredActivity;

	public ProjectType() {

	}

	public void setIsResearchBasic(boolean is_research_basic) {
		if (!this.isResearchBasic && is_research_basic) {
			this.isResearchBasic = is_research_basic;
			isResearchApplied = false;
			isResearchDevelopment = false;
			isInstruction = false;
			isOtherSponsoredActivity = false;
		}
	}

	public boolean getIsResearchBasic() {
		return isResearchBasic;
	}

	public void setIsResearchApplied(boolean is_research_applied) {
		if (!this.isResearchApplied && is_research_applied) {
			isResearchBasic = false;
			this.isResearchApplied = is_research_applied;
			isResearchDevelopment = false;
			isInstruction = false;
			isOtherSponsoredActivity = false;
		}
	}

	public boolean getIsResearchApplied() {
		return isResearchApplied;
	}

	public void setIsResearchDevelopment(boolean is_research_development) {
		if (!this.isResearchDevelopment && is_research_development) {
			isResearchBasic = false;
			isResearchApplied = false;
			this.isResearchDevelopment = is_research_development;
			isInstruction = false;
			isOtherSponsoredActivity = false;
		}
	}

	public boolean getIsResearchDevelopment() {
		return isResearchDevelopment;
	}

	public void setIsInstruction(boolean is_instruction) {
		if (!this.isInstruction && is_instruction) {
			isResearchBasic = false;
			isResearchApplied = false;
			isResearchDevelopment = false;
			this.isInstruction = is_instruction;
			isOtherSponsoredActivity = false;
		}
	}

	public boolean getIsInstruction() {
		return isInstruction;
	}

	public void setIsOtherSponsoredActivity(boolean is_other_sposored_activity) {
		if (!this.isOtherSponsoredActivity && is_other_sposored_activity) {
			isResearchBasic = false;
			isResearchApplied = false;
			isResearchDevelopment = false;
			isInstruction = false;
			this.isOtherSponsoredActivity = is_other_sposored_activity;
		}
	}

	public boolean getIsOtherSponsoredActivity() {
		return isOtherSponsoredActivity;
	}

	@Override
	public String toString() {
		String outPut = "";
		outPut += "Research-Basic           : " + isResearchBasic + "\n";
		outPut += "Research-Applied          : " + isResearchApplied + "\n";
		outPut += "Research-Development     : " + isResearchDevelopment + "\n";
		outPut += "Instruction              : " + isInstruction + "\n";
		outPut += "Other Sponsored Activity : " + isOtherSponsoredActivity;
		return outPut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isInstruction ? 1231 : 1237);
		result = prime * result + (isOtherSponsoredActivity ? 1231 : 1237);
		result = prime * result + (isResearchApplied ? 1231 : 1237);
		result = prime * result + (isResearchBasic ? 1231 : 1237);
		result = prime * result + (isResearchDevelopment ? 1231 : 1237);
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
		if (isInstruction != other.isInstruction)
			return false;
		if (isOtherSponsoredActivity != other.isOtherSponsoredActivity)
			return false;
		if (isResearchApplied != other.isResearchApplied)
			return false;
		if (isResearchBasic != other.isResearchBasic)
			return false;
		if (isResearchDevelopment != other.isResearchDevelopment)
			return false;
		return true;
	}

}
