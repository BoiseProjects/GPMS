package gpms.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class ResearchAdministrator {
	@Expose
	@Property("DF")
	private boolean DF;

	@Expose
	@Property("LG")
	private boolean LG;

	@Expose
	@Property("LN")
	private boolean LN;

	public ResearchAdministrator() {
	}

	public boolean isDF() {
		return DF;
	}

	public void setDF(boolean dF) {
		DF = dF;
	}

	public boolean isLG() {
		return LG;
	}

	public void setLG(boolean lG) {
		LG = lG;
	}

	public boolean isLN() {
		return LN;
	}

	public void setLN(boolean lN) {
		LN = lN;
	}

	@Override
	public String toString() {
		return "ResearchAdministrator [DF=" + DF + ", LG=" + LG + ", LN=" + LN
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (DF ? 1231 : 1237);
		result = prime * result + (LG ? 1231 : 1237);
		result = prime * result + (LN ? 1231 : 1237);
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
		ResearchAdministrator other = (ResearchAdministrator) obj;
		if (DF != other.DF)
			return false;
		if (LG != other.LG)
			return false;
		if (LN != other.LN)
			return false;
		return true;
	}
	
}
