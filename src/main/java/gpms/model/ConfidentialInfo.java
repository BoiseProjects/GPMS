package gpms.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.google.gson.annotations.Expose;

@Embedded
public class ConfidentialInfo implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@Property("contain confidential information")
	private boolean containConfidentialInformation;

	@Expose
	@Property("on pages")
	private String onPages = new String();

	@Expose
	@Property("patentable")
	private boolean patentable;

	@Expose
	@Property("copyrightable")
	private boolean copyrightable;

	@Expose
	@Property("involve intellectual property")
	private boolean involveIntellectualProperty;

	public ConfidentialInfo() {
	}

	public boolean isContainConfidentialInformation() {
		return containConfidentialInformation;
	}

	public void setContainConfidentialInformation(
			boolean containConfidentialInformation) {
		this.containConfidentialInformation = containConfidentialInformation;
	}

	public String getOnPages() {
		return onPages;
	}

	public void setOnPages(String onPages) {
		this.onPages = onPages;
	}

	public boolean isPatentable() {
		return patentable;
	}

	public void setPatentable(boolean patentable) {
		this.patentable = patentable;
	}

	public boolean isCopyrightable() {
		return copyrightable;
	}

	public void setCopyrightable(boolean copyrightable) {
		this.copyrightable = copyrightable;
	}

	public boolean isInvolveIntellectualProperty() {
		return involveIntellectualProperty;
	}

	public void setInvolveIntellectualProperty(
			boolean involveIntellectualProperty) {
		this.involveIntellectualProperty = involveIntellectualProperty;
	}

	@Override
	public String toString() {
		return "ConfidentialInfo [containConfidentialInformation="
				+ containConfidentialInformation + ", onPages=" + onPages
				+ ", patentable=" + patentable + ", copyrightable="
				+ copyrightable + ", involveIntellectualProperty="
				+ involveIntellectualProperty + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (containConfidentialInformation ? 1231 : 1237);
		result = prime * result + (copyrightable ? 1231 : 1237);
		result = prime * result + (involveIntellectualProperty ? 1231 : 1237);
		result = prime * result + ((onPages == null) ? 0 : onPages.hashCode());
		result = prime * result + (patentable ? 1231 : 1237);
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
		ConfidentialInfo other = (ConfidentialInfo) obj;
		if (containConfidentialInformation != other.containConfidentialInformation)
			return false;
		if (copyrightable != other.copyrightable)
			return false;
		if (involveIntellectualProperty != other.involveIntellectualProperty)
			return false;
		if (onPages == null) {
			if (other.onPages != null)
				return false;
		} else if (!onPages.equals(other.onPages))
			return false;
		if (patentable != other.patentable)
			return false;
		return true;
	}

}
