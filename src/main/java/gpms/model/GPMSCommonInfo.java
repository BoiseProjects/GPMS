package gpms.model;

public class GPMSCommonInfo {
	private String userName;
	private String userProfileID;
	private String cultureName;
	private String sessionCode;

	public GPMSCommonInfo() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserProfileID() {
		return userProfileID;
	}

	public void setUserProfileID(String userProfileID) {
		this.userProfileID = userProfileID;
	}

	public String getCultureName() {
		return cultureName;
	}

	public void setCultureName(String cultureName) {
		this.cultureName = cultureName;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	@Override
	public String toString() {
		return "GPMSCommonInfo [userName=" + userName + ", userProfileID="
				+ userProfileID + ", cultureName=" + cultureName
				+ ", sessionCode=" + sessionCode + "]";
	}	

}
