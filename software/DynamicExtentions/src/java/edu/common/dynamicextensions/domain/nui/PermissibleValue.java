package edu.common.dynamicextensions.domain.nui;

public class PermissibleValue {
	private String optionName;
	
	private Long numericCode;
	
	private String definitionSource;
	
	private String conceptCode;
	
	private String value;
	
	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public Long getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(Long numericCode) {
		this.numericCode = numericCode;
	}

	public String getDefinitionSource() {
		return definitionSource;
	}

	public void setDefinitionSource(String definitionSource) {
		this.definitionSource = definitionSource;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
