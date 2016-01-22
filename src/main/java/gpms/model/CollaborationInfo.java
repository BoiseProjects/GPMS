package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class CollaborationInfo implements Serializable {
	@Expose
	@Property("involve non-funded collaborations")
	private boolean involveNonFundedCollab;

	@Expose
	@Property("involve collaborators")
	private String involvedCollaborators = new String();

	public CollaborationInfo() {
	}

	public boolean isInvolveNonFundedCollab() {
		return involveNonFundedCollab;
	}

	public void setInvolveNonFundedCollab(boolean involveNonFundedCollab) {
		this.involveNonFundedCollab = involveNonFundedCollab;
	}

	public String getInvolvedCollaborators() {
		return involvedCollaborators;
	}

	public void setInvolvedCollaborators(String involvedCollaborators) {
		this.involvedCollaborators = involvedCollaborators;
	}

	@Override
	public String toString() {
		return "CollaborationInfo [involveNonFundedCollab="
				+ involveNonFundedCollab + ", involvedCollaborators="
				+ involvedCollaborators + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (involveNonFundedCollab ? 1231 : 1237);
		result = prime
				* result
				+ ((involvedCollaborators == null) ? 0 : involvedCollaborators
						.hashCode());
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
		CollaborationInfo other = (CollaborationInfo) obj;
		if (involveNonFundedCollab != other.involveNonFundedCollab)
			return false;
		if (involvedCollaborators == null) {
			if (other.involvedCollaborators != null)
				return false;
		} else if (!involvedCollaborators.equals(other.involvedCollaborators))
			return false;
		return true;
	}

}
