package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class BaseOptions implements Serializable {
	@Expose
	@Property("yes")
	private boolean yes;

	@Expose
	@Property("no")
	private boolean no;

	@Expose
	@Property("not applicable")
	private boolean notApplicable;

	public BaseOptions() {
	}

	public boolean isYes() {
		return yes;
	}

	public void setYes(boolean yes) {
		if (!this.yes && yes) {
			this.yes = yes;
			no = false;
			notApplicable = false;
		}
	}

	public boolean isNo() {
		return no;
	}

	public void setNo(boolean no) {
		if (!this.no && no) {
			yes = false;
			this.no = no;
			notApplicable = false;
		}
	}

	public boolean isNotApplicable() {
		return notApplicable;
	}

	public void setNotApplicable(boolean notApplicable) {
		if (!this.notApplicable && notApplicable) {
			yes = false;
			no = false;
			this.notApplicable = notApplicable;
		}
	}

	@Override
	public String toString() {
		return "BaseOptions [yes=" + yes + ", no=" + no + ", notApplicable="
				+ notApplicable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (no ? 1231 : 1237);
		result = prime * result + (notApplicable ? 1231 : 1237);
		result = prime * result + (yes ? 1231 : 1237);
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
		BaseOptions other = (BaseOptions) obj;
		if (no != other.no)
			return false;
		if (notApplicable != other.notApplicable)
			return false;
		if (yes != other.yes)
			return false;
		return true;
	}

}
