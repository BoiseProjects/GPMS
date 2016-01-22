package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class ProjectLocation implements Serializable {
	@Expose
	@Property("off-campus")
	private boolean offCampus;

	@Expose
	@Property("on-campus")
	private boolean onCampus;

	public ProjectLocation() {
	}

	public boolean isOffCampus() {
		return offCampus;
	}

	public void setOffCampus(boolean offCampus) {
		if (!this.offCampus && offCampus) {
			this.offCampus = offCampus;
			onCampus = false;
		}
	}

	public boolean isOnCampus() {
		return onCampus;
	}

	public void setOnCampus(boolean onCampus) {
		if (!this.onCampus && onCampus) {
			offCampus = false;
			this.onCampus = onCampus;
		}
	}

	@Override
	public String toString() {
		String outPut = "";
		outPut += "Off-Campus : " + offCampus + "\n";
		outPut += "On-Campus  : " + onCampus;
		return outPut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (offCampus ? 1231 : 1237);
		result = prime * result + (onCampus ? 1231 : 1237);
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
		ProjectLocation other = (ProjectLocation) obj;
		if (offCampus != other.offCampus)
			return false;
		if (onCampus != other.onCampus)
			return false;
		return true;
	}

}
