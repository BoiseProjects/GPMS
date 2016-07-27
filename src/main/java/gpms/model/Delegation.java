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

	@Reference(value = "delegater user profile"/* , lazy = true */)
	private UserProfile userProfile = new UserProfile();

	@Property("delegatee user id")
	private String delegateeId = new String();

	@Property("delegatee full name")
	private String delegateeFullName = new String();

	@Property("delegated college")
	private String college = new String();

	@Property("delegated department")
	private String department = new String();

	@Property("delegated position type")
	private String positionType = new String();

	@Property("delegated position title")
	private String positionTitle = new String();

	@Property("proposal id")
	private String proposalId = new String();

	@Property("from")
	private Date from = new Date();

	@Property("to")
	private Date to = new Date();

	@Property("action")
	private String action = new String();

	public Delegation() {

	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getDelegateeId() {
		return delegateeId;
	}

	public void setDelegateeId(String delegateeId) {
		this.delegateeId = delegateeId;
	}

	public String getDelegateeFullName() {
		return delegateeFullName;
	}

	public void setDelegateeFullName(String delegateeFullName) {
		this.delegateeFullName = delegateeFullName;
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

	@Override
	public String toString() {
		return "Delegation [userProfile=" + userProfile + ", delegateeId="
				+ delegateeId + ", delegateeFullName=" + delegateeFullName
				+ ", college=" + college + ", department=" + department
				+ ", positionType=" + positionType + ", positionTitle="
				+ positionTitle + ", proposalId=" + proposalId + ", from="
				+ from + ", to=" + to + ", action=" + action + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((college == null) ? 0 : college.hashCode());
		result = prime
				* result
				+ ((delegateeFullName == null) ? 0 : delegateeFullName
						.hashCode());
		result = prime * result
				+ ((delegateeId == null) ? 0 : delegateeId.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result
				+ ((positionTitle == null) ? 0 : positionTitle.hashCode());
		result = prime * result
				+ ((positionType == null) ? 0 : positionType.hashCode());
		result = prime * result
				+ ((proposalId == null) ? 0 : proposalId.hashCode());
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
		if (college == null) {
			if (other.college != null)
				return false;
		} else if (!college.equals(other.college))
			return false;
		if (delegateeFullName == null) {
			if (other.delegateeFullName != null)
				return false;
		} else if (!delegateeFullName.equals(other.delegateeFullName))
			return false;
		if (delegateeId == null) {
			if (other.delegateeId != null)
				return false;
		} else if (!delegateeId.equals(other.delegateeId))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (positionTitle == null) {
			if (other.positionTitle != null)
				return false;
		} else if (!positionTitle.equals(other.positionTitle))
			return false;
		if (positionType == null) {
			if (other.positionType != null)
				return false;
		} else if (!positionType.equals(other.positionType))
			return false;
		if (proposalId == null) {
			if (other.proposalId != null)
				return false;
		} else if (!proposalId.equals(other.proposalId))
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
