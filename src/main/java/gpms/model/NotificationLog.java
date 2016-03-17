package gpms.model;

import gpms.dao.NotificationDAO;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

@Entity(value = NotificationDAO.COLLECTION_NAME, noClassnameStored = true)
public class NotificationLog extends BaseEntity implements
		Comparable<NotificationLog>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("type")
	private String type = new String();

	@Property("action")
	private String action = new String();

	@Property("proposal id")
	private String proposalId = new String();

	@Property("proposal title")
	private String proposalTitle = new String();

	@Property("user profile id")
	private String userProfileId = new String();

	@Property("user name")
	private String username = new String();

	@Property("college")
	private String college = new String();

	@Property("department")
	private String department = new String();

	@Property("position type")
	private String positionType = new String();

	@Property("position title")
	private String positionTitle = new String();

	@Property("viewed by user")
	private boolean viewedByUser = false;

	@Property("viewed by admin")
	private boolean viewedByAdmin = false;

	@Property("activity on")
	@Indexed(value = IndexDirection.ASC, name = "activityDateIndex")
	private Date activityDate = new Date();

	@Property("for admin")
	private boolean forAdmin = false;

	@Property("critical")
	private boolean critical = false;

	public NotificationLog() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public String getProposalTitle() {
		return proposalTitle;
	}

	public void setProposalTitle(String proposalTitle) {
		this.proposalTitle = proposalTitle;
	}

	public String getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(String userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public boolean isViewedByUser() {
		return viewedByUser;
	}

	public void setViewedByUser(boolean viewedByUser) {
		this.viewedByUser = viewedByUser;
	}

	public boolean isViewedByAdmin() {
		return viewedByAdmin;
	}

	public void setViewedByAdmin(boolean viewedByAdmin) {
		this.viewedByAdmin = viewedByAdmin;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public boolean isForAdmin() {
		return forAdmin;
	}

	public void setForAdmin(boolean forAdmin) {
		this.forAdmin = forAdmin;
	}

	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((activityDate == null) ? 0 : activityDate.hashCode());
		result = prime * result + ((college == null) ? 0 : college.hashCode());
		result = prime * result + (critical ? 1231 : 1237);
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + (forAdmin ? 1231 : 1237);
		result = prime * result
				+ ((positionTitle == null) ? 0 : positionTitle.hashCode());
		result = prime * result
				+ ((positionType == null) ? 0 : positionType.hashCode());
		result = prime * result
				+ ((proposalId == null) ? 0 : proposalId.hashCode());
		result = prime * result
				+ ((proposalTitle == null) ? 0 : proposalTitle.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((userProfileId == null) ? 0 : userProfileId.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		result = prime * result + (viewedByAdmin ? 1231 : 1237);
		result = prime * result + (viewedByUser ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationLog other = (NotificationLog) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (activityDate == null) {
			if (other.activityDate != null)
				return false;
		} else if (!activityDate.equals(other.activityDate))
			return false;
		if (college == null) {
			if (other.college != null)
				return false;
		} else if (!college.equals(other.college))
			return false;
		if (critical != other.critical)
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (forAdmin != other.forAdmin)
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
		if (proposalTitle == null) {
			if (other.proposalTitle != null)
				return false;
		} else if (!proposalTitle.equals(other.proposalTitle))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userProfileId == null) {
			if (other.userProfileId != null)
				return false;
		} else if (!userProfileId.equals(other.userProfileId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (viewedByAdmin != other.viewedByAdmin)
			return false;
		if (viewedByUser != other.viewedByUser)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotificationLog [type=" + type + ", action=" + action
				+ ", proposalId=" + proposalId + ", proposalTitle="
				+ proposalTitle + ", userProfileId=" + userProfileId
				+ ", username=" + username + ", college=" + college
				+ ", department=" + department + ", positionType="
				+ positionType + ", positionTitle=" + positionTitle
				+ ", viewedByUser=" + viewedByUser + ", viewedByAdmin="
				+ viewedByAdmin + ", activityDate=" + activityDate
				+ ", forAdmin=" + forAdmin + ", critical=" + critical + "]";
	}

	@Override
	public int compareTo(NotificationLog o) {
		if (getActivityDate() == null || o.getActivityDate() == null)
			return 0;
		// return getActivityDate().compareTo(o.getActivityDate()); //Ascending
		return o.getActivityDate().compareTo(getActivityDate()); // Descending
	}

	@PrePersist
	public void prePersist() {
		this.activityDate = (activityDate == null) ? new Date() : activityDate;
	}
}
