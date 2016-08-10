package gpms.model;

import gpms.dao.DelegationDAO;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

@Entity(value = DelegationDAO.COLLECTION_NAME, noClassnameStored = true)
public class Delegation extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Reference(value = "delegator user profile"/* , lazy = true */)
	private UserProfile userProfile = new UserProfile();

	@Property("delegator user id")
	private String delegatorId = new String();

	@Property("delegatee user id")
	private String delegateeId = new String();

	@Property("delegatee")
	private String delegatee = new String();

	@Property("delegatee username")
	private String delegateeUsername = new String();

	@Property("delegatee email")
	private String delegateeEmail = new String();

	@Property("delegatee college")
	private String delegateeCollege = new String();

	@Property("delegatee department")
	private String delegateeDepartment = new String();

	@Property("delegatee position type")
	private String delegateePositionType = new String();

	@Property("delegatee position title")
	private String delegateePositionTitle = new String();

	@Property("delegated college")
	private String delegatedCollege = new String();

	@Property("delegated department")
	private String delegatedDepartment = new String();

	@Property("delegated position type")
	private String delegatedPositionType = new String();

	@Property("delegated position title")
	private String delegatedPositionTitle = new String();

	@Property("proposal id")
	private String proposalId = new String();

	@Property("from")
	private Date from = new Date();

	@Property("to")
	private Date to = new Date();

	@Property("action")
	private String action = new String();

	@Property("delegation reason")
	private String reason = new String();

	@Property("delegation file name")
	private String delegationFileName = new String();

	@Property("created on")
	private Date createdOn = new Date();

	@Property("revoked")
	private boolean revoked = false;

	public Delegation() {

	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getDelegatorId() {
		return delegatorId;
	}

	public void setDelegatorId(String delegatorId) {
		this.delegatorId = delegatorId;
	}

	public String getDelegateeId() {
		return delegateeId;
	}

	public void setDelegateeId(String delegateeId) {
		this.delegateeId = delegateeId;
	}

	public String getDelegatee() {
		return delegatee;
	}

	public void setDelegatee(String delegatee) {
		this.delegatee = delegatee;
	}

	public String getDelegateeUsername() {
		return delegateeUsername;
	}

	public void setDelegateeUsername(String delegateeUsername) {
		this.delegateeUsername = delegateeUsername;
	}

	public String getDelegateeEmail() {
		return delegateeEmail;
	}

	public void setDelegateeEmail(String delegateeEmail) {
		this.delegateeEmail = delegateeEmail;
	}

	public String getDelegateeCollege() {
		return delegateeCollege;
	}

	public void setDelegateeCollege(String delegateeCollege) {
		this.delegateeCollege = delegateeCollege;
	}

	public String getDelegateeDepartment() {
		return delegateeDepartment;
	}

	public void setDelegateeDepartment(String delegateeDepartment) {
		this.delegateeDepartment = delegateeDepartment;
	}

	public String getDelegateePositionType() {
		return delegateePositionType;
	}

	public void setDelegateePositionType(String delegateePositionType) {
		this.delegateePositionType = delegateePositionType;
	}

	public String getDelegateePositionTitle() {
		return delegateePositionTitle;
	}

	public void setDelegateePositionTitle(String delegateePositionTitle) {
		this.delegateePositionTitle = delegateePositionTitle;
	}

	public String getDelegatedCollege() {
		return delegatedCollege;
	}

	public void setDelegatedCollege(String delegatedCollege) {
		this.delegatedCollege = delegatedCollege;
	}

	public String getDelegatedDepartment() {
		return delegatedDepartment;
	}

	public void setDelegatedDepartment(String delegatedDepartment) {
		this.delegatedDepartment = delegatedDepartment;
	}

	public String getDelegatedPositionType() {
		return delegatedPositionType;
	}

	public void setDelegatedPositionType(String delegatedPositionType) {
		this.delegatedPositionType = delegatedPositionType;
	}

	public String getDelegatedPositionTitle() {
		return delegatedPositionTitle;
	}

	public void setDelegatedPositionTitle(String delegatedPositionTitle) {
		this.delegatedPositionTitle = delegatedPositionTitle;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDelegationFileName() {
		return delegationFileName;
	}

	public void setDelegationFileName(String delegationFileName) {
		this.delegationFileName = delegationFileName;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	@Override
	public String toString() {
		return "Delegation [userProfile=" + userProfile + ", delegatorId="
				+ delegatorId + ", delegateeId=" + delegateeId + ", delegatee="
				+ delegatee + ", delegateeUsername=" + delegateeUsername
				+ ", delegateeEmail=" + delegateeEmail + ", delegateeCollege="
				+ delegateeCollege + ", delegateeDepartment="
				+ delegateeDepartment + ", delegateePositionType="
				+ delegateePositionType + ", delegateePositionTitle="
				+ delegateePositionTitle + ", delegatedCollege="
				+ delegatedCollege + ", delegatedDepartment="
				+ delegatedDepartment + ", delegatedPositionType="
				+ delegatedPositionType + ", delegatedPositionTitle="
				+ delegatedPositionTitle + ", proposalId=" + proposalId
				+ ", from=" + from + ", to=" + to + ", action=" + action
				+ ", reason=" + reason + ", delegationFileName="
				+ delegationFileName + ", createdOn=" + createdOn
				+ ", revoked=" + revoked + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime
				* result
				+ ((delegatedCollege == null) ? 0 : delegatedCollege.hashCode());
		result = prime
				* result
				+ ((delegatedDepartment == null) ? 0 : delegatedDepartment
						.hashCode());
		result = prime
				* result
				+ ((delegatedPositionTitle == null) ? 0
						: delegatedPositionTitle.hashCode());
		result = prime
				* result
				+ ((delegatedPositionType == null) ? 0 : delegatedPositionType
						.hashCode());
		result = prime * result
				+ ((delegatee == null) ? 0 : delegatee.hashCode());
		result = prime
				* result
				+ ((delegateeCollege == null) ? 0 : delegateeCollege.hashCode());
		result = prime
				* result
				+ ((delegateeDepartment == null) ? 0 : delegateeDepartment
						.hashCode());
		result = prime * result
				+ ((delegateeEmail == null) ? 0 : delegateeEmail.hashCode());
		result = prime * result
				+ ((delegateeId == null) ? 0 : delegateeId.hashCode());
		result = prime
				* result
				+ ((delegateePositionTitle == null) ? 0
						: delegateePositionTitle.hashCode());
		result = prime
				* result
				+ ((delegateePositionType == null) ? 0 : delegateePositionType
						.hashCode());
		result = prime
				* result
				+ ((delegateeUsername == null) ? 0 : delegateeUsername
						.hashCode());
		result = prime
				* result
				+ ((delegationFileName == null) ? 0 : delegationFileName
						.hashCode());
		result = prime * result
				+ ((delegatorId == null) ? 0 : delegatorId.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result
				+ ((proposalId == null) ? 0 : proposalId.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + (revoked ? 1231 : 1237);
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result
				+ ((userProfile == null) ? 0 : userProfile.hashCode());
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
		Delegation other = (Delegation) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (delegatedCollege == null) {
			if (other.delegatedCollege != null)
				return false;
		} else if (!delegatedCollege.equals(other.delegatedCollege))
			return false;
		if (delegatedDepartment == null) {
			if (other.delegatedDepartment != null)
				return false;
		} else if (!delegatedDepartment.equals(other.delegatedDepartment))
			return false;
		if (delegatedPositionTitle == null) {
			if (other.delegatedPositionTitle != null)
				return false;
		} else if (!delegatedPositionTitle.equals(other.delegatedPositionTitle))
			return false;
		if (delegatedPositionType == null) {
			if (other.delegatedPositionType != null)
				return false;
		} else if (!delegatedPositionType.equals(other.delegatedPositionType))
			return false;
		if (delegatee == null) {
			if (other.delegatee != null)
				return false;
		} else if (!delegatee.equals(other.delegatee))
			return false;
		if (delegateeCollege == null) {
			if (other.delegateeCollege != null)
				return false;
		} else if (!delegateeCollege.equals(other.delegateeCollege))
			return false;
		if (delegateeDepartment == null) {
			if (other.delegateeDepartment != null)
				return false;
		} else if (!delegateeDepartment.equals(other.delegateeDepartment))
			return false;
		if (delegateeEmail == null) {
			if (other.delegateeEmail != null)
				return false;
		} else if (!delegateeEmail.equals(other.delegateeEmail))
			return false;
		if (delegateeId == null) {
			if (other.delegateeId != null)
				return false;
		} else if (!delegateeId.equals(other.delegateeId))
			return false;
		if (delegateePositionTitle == null) {
			if (other.delegateePositionTitle != null)
				return false;
		} else if (!delegateePositionTitle.equals(other.delegateePositionTitle))
			return false;
		if (delegateePositionType == null) {
			if (other.delegateePositionType != null)
				return false;
		} else if (!delegateePositionType.equals(other.delegateePositionType))
			return false;
		if (delegateeUsername == null) {
			if (other.delegateeUsername != null)
				return false;
		} else if (!delegateeUsername.equals(other.delegateeUsername))
			return false;
		if (delegationFileName == null) {
			if (other.delegationFileName != null)
				return false;
		} else if (!delegationFileName.equals(other.delegationFileName))
			return false;
		if (delegatorId == null) {
			if (other.delegatorId != null)
				return false;
		} else if (!delegatorId.equals(other.delegatorId))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (proposalId == null) {
			if (other.proposalId != null)
				return false;
		} else if (!proposalId.equals(other.proposalId))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (revoked != other.revoked)
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (userProfile == null) {
			if (other.userProfile != null)
				return false;
		} else if (!userProfile.equals(other.userProfile))
			return false;
		return true;
	}

}
