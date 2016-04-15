package gpms.accesscontrol;

public class AttributeRecord {
	private String attributeName;
	private String fullAttributeName;
	private String category;
	private String dataType;
	private String values;

	public AttributeRecord() {

	}

	public AttributeRecord(String attributeName, String fullAttributeName,
			String category, String dataType, String values) {
		this.attributeName = attributeName;
		this.fullAttributeName = fullAttributeName;
		this.category = category;
		this.dataType = dataType;
		this.values = values;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getFullAttributeName() {
		return fullAttributeName;
	}

	public void setFullAttributeName(String fullAttributeName) {
		this.fullAttributeName = fullAttributeName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "AttributeRecord [attributeName=" + attributeName
				+ ", fullAttributeName=" + fullAttributeName + ", category="
				+ category + ", dataType=" + dataType + ", values=" + values
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime
				* result
				+ ((fullAttributeName == null) ? 0 : fullAttributeName
						.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		AttributeRecord other = (AttributeRecord) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (fullAttributeName == null) {
			if (other.fullAttributeName != null)
				return false;
		} else if (!fullAttributeName.equals(other.fullAttributeName))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

}
