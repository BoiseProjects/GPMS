package gpms.model;

import java.util.Date;

import org.mongodb.morphia.annotations.Property;

public class SignatureUserInfo {
	@Property("user profile id")
	private String userProfileId = new String();

	@Property("full name")
	private String fullName = new String();

	@Property("user name")
	private String userName = new String();

	@Property("email")
	private String email = new String();

	@Property("college")
	private String college = new String();

	@Property("department")
	private String department = new String();

	@Property("position type")
	private String positionType = new String();

	@Property("position title")
	private String positionTitle = new String();

	@Property("signature")
	private String signature = new String();

	@Property("signed date")
	private Date signedDate = null;

	@Property("note")
	private String note = new String();

	@Property("delegated")
	private boolean delegated = false;

	@Property("delegated as")
	private String delegatedAs = new String();

	public SignatureUserInfo() {

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Date getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isDelegated() {
		return delegated;
	}

	public void setDelegated(boolean delegated) {
		this.delegated = delegated;
	}

	public String getDelegatedAs() {
		return delegatedAs;
	}

	public void setDelegatedAs(String delegatedAs) {
		this.delegatedAs = delegatedAs;
	}

}
