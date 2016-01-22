//Written by : Hector C. Ortiz
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
public class AuditLog implements Comparable<AuditLog>, Cloneable {
	// @Expose
	@Reference(value = "author info", lazy = true)
	private UserProfile userProfile = new UserProfile();

	// @Expose
	@Property("action")
	private String action = new String();

	// @Expose
	@Property("activity on")
	@Indexed(value = IndexDirection.DESC, name = "activityOnIndex")
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

	public void setUserProfile(UserProfile userProfileId) {
		this.userProfile = userProfileId;
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
	public int compareTo(AuditLog o) {
		if (getActivityDate() == null || o.getActivityDate() == null)
			return 0;
		// return getActivityDate().compareTo(o.getActivityDate()); // Ascending
		return o.getActivityDate().compareTo(getActivityDate()); // Descending
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

	@Override
	protected AuditLog clone() throws CloneNotSupportedException {
		AuditLog copy = new AuditLog();
		copy.setUserProfile(this.userProfile.clone());
		copy.setAction(this.action);
		copy.setActivityDate(this.activityDate);
		return copy;
	}

}
