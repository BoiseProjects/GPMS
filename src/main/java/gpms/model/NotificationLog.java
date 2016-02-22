package gpms.model;

import gpms.dao.NotificationDAO;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

import com.google.gson.annotations.Expose;

@Entity(value = NotificationDAO.COLLECTION_NAME, noClassnameStored = true)
public class NotificationLog extends BaseEntity implements
		Comparable<NotificationLog>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@Property("type")
	private String type = new String();

	@Expose
	@Property("action")
	private String action = new String();

	@Expose
	@Property("proposal id")
	private String proposalId = new String();

	@Expose
	@Property("proposal title")
	private String proposalTitle = new String();

	@Expose
	@Property("user profile id")
	private String userProfileId = new String();

	@Expose
	@Property("user name")
	private String username = new String();

	@Expose
	@Property("college")
	private String college = new String();

	@Expose
	@Property("department")
	private String department = new String();

	@Expose
	@Property("position type")
	private String positionType = new String();

	@Expose
	@Property("position title")
	private String positionTitle = new String();

	@Expose
	@Property("is viewed by user")
	private boolean isViewedByUser = false;

	@Expose
	@Property("is viewed by admin")
	private boolean isViewedByAdmin = false;

	@Property("activity on")
	@Indexed(value = IndexDirection.ASC, name = "activityDateIndex")
	private Date activityDate = new Date();

	@Expose
	@Property("for admin")
	private boolean isForAdmin = false;

	@Expose
	@Property("is critical")
	private boolean isCritical = false;

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
		return isViewedByUser;
	}

	public void setViewedByUser(boolean isViewedByUser) {
		this.isViewedByUser = isViewedByUser;
	}

	public boolean isViewedByAdmin() {
		return isViewedByAdmin;
	}

	public void setViewedByAdmin(boolean isViewedByAdmin) {
		this.isViewedByAdmin = isViewedByAdmin;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public boolean isForAdmin() {
		return isForAdmin;
	}

	public void setForAdmin(boolean isForAdmin) {
		this.isForAdmin = isForAdmin;
	}

	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((activityDate == null) ? 0 : activityDate.hashCode());
		result = prime * result + ((college == null) ? 0 : college.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + (isCritical ? 1231 : 1237);
		result = prime * result + (isForAdmin ? 1231 : 1237);
		result = prime * result + (isViewedByAdmin ? 1231 : 1237);
		result = prime * result + (isViewedByUser ? 1231 : 1237);
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
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (isCritical != other.isCritical)
			return false;
		if (isForAdmin != other.isForAdmin)
			return false;
		if (isViewedByAdmin != other.isViewedByAdmin)
			return false;
		if (isViewedByUser != other.isViewedByUser)
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
				+ ", isViewedByUser=" + isViewedByUser + ", isViewedByAdmin="
				+ isViewedByAdmin + ", activityDate=" + activityDate
				+ ", isForAdmin=" + isForAdmin + ", isCritical=" + isCritical
				+ "]";
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
