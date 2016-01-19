package gpms.model;

import gpms.dao.NotificationDAO;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.utils.IndexDirection;

import com.google.gson.annotations.Expose;

@Entity(value = NotificationDAO.COLLECTION_NAME, noClassnameStored = true)
public class NotificationLog extends BaseEntity implements
		Comparable<NotificationLog> {

	@Expose
	@Property("type")
	private String type = new String();

	@Expose
	@Property("action")
	private String action = new String();

	@Expose
	@Reference(value = "proposal info"/* , lazy = true */)
	private Proposal proposal = new Proposal();

	@Expose
	@Reference(value = "user info"/* , lazy = true */)
	private UserProfile userProfile = new UserProfile();

	@Expose
	@Property("notification for")
	private String userProfileId = new String();

	@Expose
	@Property("is viewed by user")
	private boolean isViewedByUser = false;

	@Expose
	@Property("is viewed by admin")
	private boolean isViewedByAdmin = false;

	@Property("activity on")
	@Indexed(value = IndexDirection.DESC, name = "activityDateIndex")
	private Date activityDate = new Date();

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

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(String userProfileId) {
		this.userProfileId = userProfileId;
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

	public NotificationLog(String type, String action, Proposal proposal,
			UserProfile userProfile, String userProfileId,
			boolean isViewedByUser, boolean isViewedByAdmin, Date activityDate) {
		this.type = type;
		this.action = action;
		this.proposal = proposal;
		this.userProfile = userProfile;
		this.userProfileId = userProfileId;
		this.isViewedByUser = isViewedByUser;
		this.isViewedByAdmin = isViewedByAdmin;
		this.activityDate = activityDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((activityDate == null) ? 0 : activityDate.hashCode());
		result = prime * result + (isViewedByAdmin ? 1231 : 1237);
		result = prime * result + (isViewedByUser ? 1231 : 1237);
		result = prime * result
				+ ((proposal == null) ? 0 : proposal.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((userProfile == null) ? 0 : userProfile.hashCode());
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
		if (isViewedByAdmin != other.isViewedByAdmin)
			return false;
		if (isViewedByUser != other.isViewedByUser)
			return false;
		if (proposal == null) {
			if (other.proposal != null)
				return false;
		} else if (!proposal.equals(other.proposal))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userProfile == null) {
			if (other.userProfile != null)
				return false;
		} else if (!userProfile.equals(other.userProfile))
			return false;
		if (userProfileId == null) {
			if (other.userProfileId != null)
				return false;
		} else if (!userProfileId.equals(other.userProfileId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotificationLog [type=" + type + ", action=" + action
				+ ", proposal=" + proposal + ", userProfile=" + userProfile
				+ ", userProfileId=" + userProfileId + ", isViewedByUser="
				+ isViewedByUser + ", isViewedByAdmin=" + isViewedByAdmin
				+ ", activityDate=" + activityDate + "]";
	}

	@Override
	public int compareTo(NotificationLog o) {
		if (getActivityDate() == null || o.getActivityDate() == null)
			return 0;
		// return getActivityDate().compareTo(o.getActivityDate()); // Ascending
		return o.getActivityDate().compareTo(getActivityDate()); // Descending
	}

}
