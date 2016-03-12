package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class TypeOfRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("pre-proposal")
	private boolean preProposal;

	@Property("new proposal")
	private boolean newProposal;

	@Property("continuation")
	private boolean continuation;

	@Property("supplement")
	private boolean supplement;

	public TypeOfRequest() {

	}

	public boolean isPreProposal() {
		return preProposal;
	}

	public void setPreProposal(boolean preProposal) {
		this.preProposal = preProposal;
	}

	public boolean isNewProposal() {
		return newProposal;
	}

	public void setNewProposal(boolean newProposal) {
		this.newProposal = newProposal;
	}

	public boolean isContinuation() {
		return continuation;
	}

	public void setContinuation(boolean continuation) {
		this.continuation = continuation;
	}

	public boolean isSupplement() {
		return supplement;
	}

	public void setSupplement(boolean supplement) {
		this.supplement = supplement;
	}

	@Override
	public String toString() {
		return "TypeOfRequest [preProposal=" + preProposal + ", newProposal="
				+ newProposal + ", continuation=" + continuation
				+ ", supplement=" + supplement + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (continuation ? 1231 : 1237);
		result = prime * result + (newProposal ? 1231 : 1237);
		result = prime * result + (preProposal ? 1231 : 1237);
		result = prime * result + (supplement ? 1231 : 1237);
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
		TypeOfRequest other = (TypeOfRequest) obj;
		if (continuation != other.continuation)
			return false;
		if (newProposal != other.newProposal)
			return false;
		if (preProposal != other.preProposal)
			return false;
		if (supplement != other.supplement)
			return false;
		return true;
	}

}
