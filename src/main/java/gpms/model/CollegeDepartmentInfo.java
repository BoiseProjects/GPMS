package gpms.model;

import org.mongodb.morphia.annotations.Property;

public class CollegeDepartmentInfo {
	private String userProfileId = new String();

	private String college = new String();

	private String department = new String();

	public CollegeDepartmentInfo() {

	}

	public String getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(String userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
}
