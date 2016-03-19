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

}
