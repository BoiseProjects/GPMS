package gpms.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class BaseInfo implements Cloneable {
	@Expose
	@Property("MTDC")
	private boolean MTDC;

	@Expose
	@Property("TDC")
	private boolean TDC;

	@Expose
	@Property("TC")
	private boolean TC;

	@Expose
	@Property("other")
	private boolean other;

	@Expose
	@Property("not applicable")
	private boolean notApplicable;

	public BaseInfo() {
	}

	public boolean isMTDC() {
		return MTDC;
	}

	public void setMTDC(boolean mTDC) {
		MTDC = mTDC;
	}

	public boolean isTDC() {
		return TDC;
	}

	public void setTDC(boolean tDC) {
		TDC = tDC;
	}

	public boolean isTC() {
		return TC;
	}

	public void setTC(boolean tC) {
		TC = tC;
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
		return "Base [MTDC=" + MTDC + ", TDC=" + TDC + ", TC=" + TC
				+ ", other=" + other + ", notApplicable=" + notApplicable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (MTDC ? 1231 : 1237);
		result = prime * result + (TC ? 1231 : 1237);
		result = prime * result + (TDC ? 1231 : 1237);
		result = prime * result + (notApplicable ? 1231 : 1237);
		result = prime * result + (other ? 1231 : 1237);
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
		if (MTDC != other.MTDC)
			return false;
		if (TC != other.TC)
			return false;
		if (TDC != other.TDC)
			return false;
		if (notApplicable != other.notApplicable)
			return false;
		if (this.other != other.other)
			return false;
		return true;
	}

	@Override
	protected BaseInfo clone() throws CloneNotSupportedException {
		BaseInfo copy = new BaseInfo();
		copy.setMTDC(this.MTDC);
		copy.setTDC(this.TDC);
		copy.setTC(this.TC);
		copy.setOther(this.other);
		copy.setNotApplicable(this.notApplicable);
		return copy;
	}

}
