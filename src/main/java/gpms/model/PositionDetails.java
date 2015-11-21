//Edited by: Hector C. Ortiz

package gpms.model;

//import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class PositionDetails {
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

	public PositionDetails() {
	}

	public PositionDetails(String positionTitle, String positionType,
			String department, String college) {
		this.positionTitle = positionTitle;
		this.positionType = positionType;
		this.department = department;
		this.college = college;
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

	@Override
	public String toString() {
		String posDet = "Position Title: " + positionTitle + "\n";
		posDet += "Position Type: " + positionType + "\n";
		posDet += "College: " + college + "\n";
		posDet += "Department: " + department + "\n";
		return posDet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((college == null) ? 0 : college.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
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
	public PositionDetails clone() throws CloneNotSupportedException{
		return new PositionDetails(this.positionTitle, this.positionType,
				this.department, this.college);
	}

}
