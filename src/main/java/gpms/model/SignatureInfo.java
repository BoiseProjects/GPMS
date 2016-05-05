package gpms.model;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class SignatureInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("user profile id")
	private String userProfileId = new String();

	@Property("full name")
	private String fullName = new String();

	@Property("email")
	private String email = new String();

	@Property("signature")
	private String signature = new String();

	@Property("position title")
	private String positionTitle = new String();

	@Property("signed date")
	private Date signedDate = null;

	@Property("note")
	private String note = new String();

	@Property("delegated")
	private boolean delegated = false;

	@Property("delegated as")
	private String delegatedAs = new String();

	public SignatureInfo() {

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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
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

	@Override
	public String toString() {
		return "SignatureInfo [userProfileId=" + userProfileId + ", fullName="
				+ fullName + ", email=" + email + ", signature=" + signature
				+ ", positionTitle=" + positionTitle + ", signedDate="
				+ signedDate + ", note=" + note + ", delegated=" + delegated
				+ ", delegatedAs=" + delegatedAs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (delegated ? 1231 : 1237);
		result = prime * result
				+ ((delegatedAs == null) ? 0 : delegatedAs.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result
				+ ((positionTitle == null) ? 0 : positionTitle.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		result = prime * result
				+ ((signedDate == null) ? 0 : signedDate.hashCode());
		result = prime * result
				+ ((userProfileId == null) ? 0 : userProfileId.hashCode());
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
		SignatureInfo other = (SignatureInfo) obj;
		if (delegated != other.delegated)
			return false;
		if (delegatedAs == null) {
			if (other.delegatedAs != null)
				return false;
		} else if (!delegatedAs.equals(other.delegatedAs))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (positionTitle == null) {
			if (other.positionTitle != null)
				return false;
		} else if (!positionTitle.equals(other.positionTitle))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		if (signedDate == null) {
			if (other.signedDate != null)
				return false;
		} else if (!signedDate.equals(other.signedDate))
			return false;
		if (userProfileId == null) {
			if (other.userProfileId != null)
				return false;
		} else if (!userProfileId.equals(other.userProfileId))
			return false;
		return true;
	}

}
