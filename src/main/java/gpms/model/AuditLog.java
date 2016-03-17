package gpms.model;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.utils.IndexDirection;

@Embedded
// @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
// property = "id")
public class AuditLog {
	@Reference(value = "author info", lazy = true)
	private UserProfile userProfile = new UserProfile();

	@Property("action")
	private String action = new String();

	@Property("activity on")
	@Indexed(value = IndexDirection.ASC, name = "activityOnIndex")
	private Date activityDate = new Date();

	public AuditLog() {
		
	}

	public AuditLog(UserProfile authorProfile, String action, Date activityDate) {
		this.userProfile = authorProfile;
		this.action = action;
		this.activityDate = activityDate;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	@Override
	public String toString() {
		return "AuditLog [userProfile=" + userProfile + ", action=" + action
				+ ", activityDate=" + activityDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((activityDate == null) ? 0 : activityDate.hashCode());
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
		AuditLog other = (AuditLog) obj;
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
		if (userProfile == null) {
			if (other.userProfile != null)
				return false;
		} else if (!userProfile.equals(other.userProfile))
			return false;
		return true;
	}

}
