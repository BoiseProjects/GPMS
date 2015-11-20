//Edited by: Hector C. Ortiz
/**
 * @author Thomas Volz
 */

package gpms.model;

import gpms.dao.UserProfileDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.utils.IndexDirection;

import com.google.gson.annotations.Expose;

@Entity(value = UserProfileDAO.COLLECTION_NAME, noClassnameStored = true)
// @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
// property = "id")
public class UserProfile extends BaseEntity {
	@Expose
	@Property("first name")
	@Indexed(value = IndexDirection.ASC, name = "firstNameIndex")
	private String firstName = new String();

	@Expose
	@Property("middle name")
	// @Indexed(value = IndexDirection.ASC, name = "middleNameIndex")
	private String middleName = new String();

	@Expose
	@Property("last name")
	@Indexed(value = IndexDirection.ASC, name = "lastNameIndex")
	private String lastName = new String();

	@Expose
	@Property("date of birth")
	private Date dateOfBirth = new Date();

	@Expose
	@Property("gender")
	private String gender = new String();

	@Expose
	@Embedded("details")
	private List<PositionDetails> details = new ArrayList<PositionDetails>();

	@Expose
	@Property("office number")
	private List<String> officeNumbers = new ArrayList<String>();

	@Expose
	@Property("mobile number")
	private List<String> mobileNumbers = new ArrayList<String>();

	@Expose
	@Property("home number")
	private List<String> homeNumbers = new ArrayList<String>();

	@Expose
	@Property("other number")
	private List<String> otherNumbers = new ArrayList<String>();

	@Expose
	@Embedded("addresses")
	private List<Address> addresses = new ArrayList<Address>();

	@Expose
	@Property("work email")
	@Indexed(name = "workEmailsIndex", unique = true)
	private List<String> workEmails = new ArrayList<String>();

	@Expose
	@Property("personal email")
	private List<String> personalEmails = new ArrayList<String>();

	@Expose
	@Reference(value = "user id"/* , lazy = true */)
	private UserAccount userAccount = new UserAccount();

	@Expose
	@Property("is deleted")
	private boolean isDeleted;

	/**
	 * Non-Parameterized constructor, needed for @id assignment
	 */
	public UserProfile() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<PositionDetails> getDetails() {
		return details;
	}

	/**
	 * Will return a PositionDetails object from the list, for manipulation
	 * 
	 * @param index
	 *            index to find
	 * @return PositionDetails object to return
	 */
	public PositionDetails getDetails(int index) {
		return details.get(index);
	}

	public void setDetails(List<PositionDetails> details) {
		this.details = details;
	}

	public List<String> getOfficeNumbers() {
		return officeNumbers;
	}

	public void setOfficeNumbers(List<String> officeNumbers) {
		this.officeNumbers = officeNumbers;
	}

	public List<String> getMobileNumbers() {
		return mobileNumbers;
	}

	public void setMobileNumbers(List<String> mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

	public List<String> getHomeNumbers() {
		return homeNumbers;
	}

	public void setHomeNumbers(List<String> homeNumbers) {
		this.homeNumbers = homeNumbers;
	}

	public List<String> getOtherNumbers() {
		return otherNumbers;
	}

	public void setOtherNumbers(List<String> otherNumbers) {
		this.otherNumbers = otherNumbers;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<String> getWorkEmails() {
		return workEmails;
	}

	public void setWorkEmails(List<String> workEmails) {
		this.workEmails = workEmails;
	}

	public List<String> getPersonalEmails() {
		return personalEmails;
	}

	public void setPersonalEmails(List<String> personalEmails) {
		this.personalEmails = personalEmails;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setUserId(UserAccount newUserAccount) {
		userAccount = newUserAccount;
	}

	/**
	 * toString returns full user name
	 * 
	 * @return full name of the user
	 */
	@Override
	public String toString() {
		return new StringBuffer(" First Name : ").append(this.getFirstName())
				.append(" Middle Name : ").append(this.getMiddleName())
				.append(" Last Name : ").append(this.getLastName())
				.append(" Date of Birth : ").append(this.getDateOfBirth())
				.append(" Gender : ").append(this.getGender())
				.append(" Account name: ").append(userAccount.getUserName())
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addresses == null) ? 0 : addresses.hashCode());
		result = prime * result
				+ ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((details == null) ? 0 : details.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((homeNumbers == null) ? 0 : homeNumbers.hashCode());
		result = prime * result + (isDeleted ? 1231 : 1237);
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result
				+ ((mobileNumbers == null) ? 0 : mobileNumbers.hashCode());
		result = prime * result
				+ ((officeNumbers == null) ? 0 : officeNumbers.hashCode());
		result = prime * result
				+ ((otherNumbers == null) ? 0 : otherNumbers.hashCode());
		result = prime * result
				+ ((personalEmails == null) ? 0 : personalEmails.hashCode());
		result = prime * result
				+ ((userAccount == null) ? 0 : userAccount.hashCode());
		result = prime * result
				+ ((workEmails == null) ? 0 : workEmails.hashCode());
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
		UserProfile other = (UserProfile) obj;
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (details == null) {
			if (other.details != null)
				return false;
		} else if (!details.equals(other.details))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (homeNumbers == null) {
			if (other.homeNumbers != null)
				return false;
		} else if (!homeNumbers.equals(other.homeNumbers))
			return false;
		if (isDeleted != other.isDeleted)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (mobileNumbers == null) {
			if (other.mobileNumbers != null)
				return false;
		} else if (!mobileNumbers.equals(other.mobileNumbers))
			return false;
		if (officeNumbers == null) {
			if (other.officeNumbers != null)
				return false;
		} else if (!officeNumbers.equals(other.officeNumbers))
			return false;
		if (otherNumbers == null) {
			if (other.otherNumbers != null)
				return false;
		} else if (!otherNumbers.equals(other.otherNumbers))
			return false;
		if (personalEmails == null) {
			if (other.personalEmails != null)
				return false;
		} else if (!personalEmails.equals(other.personalEmails))
			return false;
		if (userAccount == null) {
			if (other.userAccount != null)
				return false;
		} else if (!userAccount.equals(other.userAccount))
			return false;
		if (workEmails == null) {
			if (other.workEmails != null)
				return false;
		} else if (!workEmails.equals(other.workEmails))
			return false;
		return true;
	}

	@Override
	public UserProfile clone() throws CloneNotSupportedException {
		UserProfile copy = new UserProfile();
		copy.setFirstName(firstName);
		copy.setMiddleName(middleName);
		copy.setLastName(lastName);
		copy.setDateOfBirth(dateOfBirth);
		copy.setGender(gender);

		for (PositionDetails pd : this.details) {
			copy.getDetails().add(pd.clone());
		}
		for (String phone : this.officeNumbers) {
			copy.getOfficeNumbers().add(phone);
		}
		for (String phone : this.mobileNumbers) {
			copy.getMobileNumbers().add(phone);
		}
		for (String phone : this.homeNumbers) {
			copy.getHomeNumbers().add(phone);
		}
		for (String phone : this.otherNumbers) {
			copy.getOtherNumbers().add(phone);
		}
		for (String email : this.workEmails) {
			copy.getWorkEmails().add(email);
		}
		for (String email : this.personalEmails) {
			copy.getPersonalEmails().add(email);
		}

		for (Address address : this.addresses) {
			copy.getAddresses().add(address);
		}

		copy.setUserId(this.userAccount.clone());
		copy.setId(this.getId());
		copy.setVersion(this.getVersion());
		copy.setDeleted(this.isDeleted());
		for (AuditLog entry : this.getAuditLog()) {
			copy.addEntryToAuditLog(entry);
		}

		return copy;
	}

	public String getFullName() {
		if (this.middleName != null && !this.middleName.isEmpty())
			return this.firstName + " " + this.middleName + " " + this.lastName;
		else
			return this.firstName + " " + this.lastName;

	}

	public void addEntryToAuditLog(AuditLog audit) {
		this.getAuditLog().add(audit);
	}
}
