package gpms.model;

public class GPMSCommonInfo implements Cloneable {
	private String userProfileID;
	private String userName;
	private boolean userIsAdmin;
	private String userCollege;
	private String userDepartment;
	private String userPositionType;
	private String userPositionTitle;

	public GPMSCommonInfo() {
	}

	public String getUserProfileID() {
		return userProfileID;
	}

	public void setUserProfileID(String userProfileID) {
		this.userProfileID = userProfileID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean getUserIsAdmin() {
		return userIsAdmin;
	}

	public void setUserIsAdmin(boolean userIsAdmin) {
		this.userIsAdmin = userIsAdmin;
	}

	public String getUserCollege() {
		return userCollege;
	}

	public void setUserCollege(String userCollege) {
		this.userCollege = userCollege;
	}

	public String getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(String userDepartment) {
		this.userDepartment = userDepartment;
	}

	public String getUserPositionType() {
		return userPositionType;
	}

	public void setUserPositionType(String userPositionType) {
		this.userPositionType = userPositionType;
	}

	public String getUserPositionTitle() {
		return userPositionTitle;
	}

	public void setUserPositionTitle(String userPositionTitle) {
		this.userPositionTitle = userPositionTitle;
	}

	@Override
	public String toString() {
		return "GPMSCommonInfo [userProfileID=" + userProfileID + ", userName="
				+ userName + ", userIsAdmin=" + userIsAdmin + ", userCollege="
				+ userCollege + ", userDepartment=" + userDepartment
				+ ", userPositionType=" + userPositionType
				+ ", userPositionTitle=" + userPositionTitle + "]";
	}
}
