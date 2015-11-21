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
}
