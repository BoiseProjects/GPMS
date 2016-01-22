package gpms.model;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class SignatureInfo implements Serializable {
	@Expose
	@Property("user profile id")
	private String userProfileId = new String();

	@Expose
	@Property("full name")
	private String fullName = new String();

	@Expose
	@Property("signature")
	private String signature = new String();

	@Expose
	@Property("position title")
	private String positionTitle = new String();

	@Expose
	@Property("signed date")
	private Date signedDate = null;

	@Expose
	@Property("note")
	private String note = new String();

	@Expose
	@Property("is delegated")
	private boolean isDelegated = false;

	@Expose
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
		return isDelegated;
	}

	public void setDelegated(boolean isDelegated) {
		this.isDelegated = isDelegated;
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
				+ fullName + ", signature=" + signature + ", positionTitle="
				+ positionTitle + ", signedDate=" + signedDate + ", note="
				+ note + ", isDelegated=" + isDelegated + ", delegatedAs="
				+ delegatedAs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((delegatedAs == null) ? 0 : delegatedAs.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + (isDelegated ? 1231 : 1237);
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
		if (delegatedAs == null) {
			if (other.delegatedAs != null)
				return false;
		} else if (!delegatedAs.equals(other.delegatedAs))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (isDelegated != other.isDelegated)
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
