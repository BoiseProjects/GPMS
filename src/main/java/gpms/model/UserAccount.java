package gpms.model;

import gpms.dao.UserAccountDAO;
import gpms.utils.PasswordHash;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

@Entity(value = UserAccountDAO.COLLECTION_NAME, noClassnameStored = true)
// @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
// property = "_id")
public class UserAccount extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("username")
	@Indexed(value = IndexDirection.ASC, name = "userNameIndex", unique = true)
	private String userName = new String();

	@Property("password")
	private String password = new String();

	@Property("deleted")
	private boolean deleted = false;

	@Property("active")
	private boolean active = false;

	@Property("admin")
	private boolean admin = false;

	@Property("added on")
	private Date addedOn = new Date();

	public UserAccount() {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		try {
			this.password = PasswordHash.createHash(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	@Override
	public String toString() {
		return "UserAccount [userName=" + userName + ", password=" + password
				+ ", deleted=" + deleted + ", active=" + active + ", admin="
				+ admin + ", addedOn=" + addedOn + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((addedOn == null) ? 0 : addedOn.hashCode());
		result = prime * result + (admin ? 1231 : 1237);
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		UserAccount other = (UserAccount) obj;
		if (active != other.active)
			return false;
		if (addedOn == null) {
			if (other.addedOn != null)
				return false;
		} else if (!addedOn.equals(other.addedOn))
			return false;
		if (admin != other.admin)
			return false;
		if (deleted != other.deleted)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
