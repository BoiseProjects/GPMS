package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class BaseInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("mtdc")
	private boolean mtdc;

	@Property("tdc")
	private boolean tdc;

	@Property("tc")
	private boolean tc;

	@Property("other")
	private boolean other;

	@Property("not applicable")
	private boolean notApplicable;

	public BaseInfo() {

	}

	public boolean isMtdc() {
		return mtdc;
	}

	public void setMtdc(boolean mtdc) {
		this.mtdc = mtdc;
	}

	public boolean isTdc() {
		return tdc;
	}

	public void setTdc(boolean tdc) {
		this.tdc = tdc;
	}

	public boolean isTc() {
		return tc;
	}

	public void setTc(boolean tc) {
		this.tc = tc;
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}

	public boolean isNotApplicable() {
		return notApplicable;
	}

	public void setNotApplicable(boolean notApplicable) {
		this.notApplicable = notApplicable;
	}

	@Override
	public String toString() {
		return "BaseInfo [mtdc=" + mtdc + ", tdc=" + tdc + ", tc=" + tc
				+ ", other=" + other + ", notApplicable=" + notApplicable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (mtdc ? 1231 : 1237);
		result = prime * result + (notApplicable ? 1231 : 1237);
		result = prime * result + (other ? 1231 : 1237);
		result = prime * result + (tc ? 1231 : 1237);
		result = prime * result + (tdc ? 1231 : 1237);
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
		BaseInfo other = (BaseInfo) obj;
		if (mtdc != other.mtdc)
			return false;
		if (notApplicable != other.notApplicable)
			return false;
		if (this.other != other.other)
			return false;
		if (tc != other.tc)
			return false;
		if (tdc != other.tdc)
			return false;
		return true;
	}

}
