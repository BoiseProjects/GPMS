package gpms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;

import com.google.gson.annotations.Expose;

@Embedded
public class InvestigatorInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	@Embedded("PI")
	private InvestigatorRefAndPosition pi = new InvestigatorRefAndPosition();
	@Expose
	@Embedded("CO-PI")
	private List<InvestigatorRefAndPosition> co_pi = new ArrayList<InvestigatorRefAndPosition>();
	@Expose
	@Embedded("senior personnel")
	private List<InvestigatorRefAndPosition> seniorPersonnel = new ArrayList<InvestigatorRefAndPosition>();
	private List<InvestigatorRefAndPosition> masterList = new ArrayList<InvestigatorRefAndPosition>();
	
	public InvestigatorInfo() {
	}

	public InvestigatorRefAndPosition getPi() {
		return pi;
	}

	public void setPi(InvestigatorRefAndPosition pi) {
		this.pi = pi;
		//Adding to the master list, to help with signature verification
		masterList.add(pi);
	}

	public List<InvestigatorRefAndPosition> getCo_pi() {
		return co_pi;
	}

	public void setCo_pi(List<InvestigatorRefAndPosition> co_pi) {
		this.co_pi = co_pi;
		//Adding all of these to a master list, to help with signature verification
		for(InvestigatorRefAndPosition coPi : co_pi)
		{
			masterList.add(coPi);
		}
	}

	public List<InvestigatorRefAndPosition> getSeniorPersonnel() {
		return seniorPersonnel;
	}

	public void setSeniorPersonnel(
			List<InvestigatorRefAndPosition> seniorPersonnel) {
		this.seniorPersonnel = seniorPersonnel;
		//Adding all of these to a master list, to help with signature verification
		for(InvestigatorRefAndPosition seniorP : seniorPersonnel)
		{
			masterList.add(seniorP);
		}
	}

	/**
	 * This is the master list of all Pi, CoPi, and SP
	 * This was made to help with signature verification
	 * @return the masterList
	 */
	public List<InvestigatorRefAndPosition> getAllInvList()
	{
		return masterList;
	}
	
	@Override
	public String toString() {
		return "InvestigatorInfo [pi=" + pi + ", co_pi=" + co_pi
				+ ", seniorPersonnel=" + seniorPersonnel + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((co_pi == null) ? 0 : co_pi.hashCode());
		result = prime * result + ((pi == null) ? 0 : pi.hashCode());
		result = prime * result
				+ ((seniorPersonnel == null) ? 0 : seniorPersonnel.hashCode());
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
		InvestigatorInfo other = (InvestigatorInfo) obj;
		if (co_pi == null) {
			if (other.co_pi != null)
				return false;
		} else if (!co_pi.equals(other.co_pi))
			return false;
		if (pi == null) {
			if (other.pi != null)
				return false;
		} else if (!pi.equals(other.pi))
			return false;
		if (seniorPersonnel == null) {
			if (other.seniorPersonnel != null)
				return false;
		} else if (!seniorPersonnel.equals(other.seniorPersonnel))
			return false;
		return true;
	}

}
