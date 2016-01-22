//Edited by: Hector C. Ortiz

package gpms.model;

//import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class PositionDetails implements Cloneable {
	@Expose
	@Property("position title")
	private String positionTitle = new String();

	@Expose
	@Property("position type")
	private String positionType = new String();

	@Expose
	@Property("department")
	private String department = new String();

	@Expose
	@Property("college")
	private String college = new String();

	@Expose
	@Property("is default")
	private boolean isDefault = false;

	public PositionDetails() {
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "PositionDetails [positionTitle=" + positionTitle
				+ ", positionType=" + positionType + ", department="
				+ department + ", college=" + college + ", isDefault="
				+ isDefault + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((college == null) ? 0 : college.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + (isDefault ? 1231 : 1237);
		result = prime * result
				+ ((positionTitle == null) ? 0 : positionTitle.hashCode());
		result = prime * result
				+ ((positionType == null) ? 0 : positionType.hashCode());
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
		PositionDetails other = (PositionDetails) obj;
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
		if (isDefault != other.isDefault)
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
		return true;
	}

	@Override
	public PositionDetails clone() throws CloneNotSupportedException {
		PositionDetails copy = new PositionDetails();
		copy.setCollege(this.college);
		copy.setDepartment(this.department);
		copy.setPositionType(this.positionType);
		copy.setPositionTitle(this.positionTitle);
		return copy;
	}
}
