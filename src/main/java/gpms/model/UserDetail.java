package gpms.model;

public class UserDetail {

	private String userProfileId = new String();

	private String fullName = new String();

	private String email = new String();

	private String college = new String();

	private String department = new String();

	private String positionType = new String();

	private String positionTitle = new String();

	public UserDetail() {

	}

	public String getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(String userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	@Override
	public String toString() {
		return "UserDetail [userProfileId=" + userProfileId + ", fullName="
				+ fullName + ", email=" + email + ", college=" + college
				+ ", department=" + department + ", positionType="
				+ positionType + ", positionTitle=" + positionTitle + "]";
	}

}
