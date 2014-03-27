package edu.common.dynamicextensions.domain.nui;

import org.apache.commons.lang.StringUtils;

public class PermissibleValue  implements Comparable<PermissibleValue>{
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((optionName == null) ? 0 : optionName.hashCode());
		result = prime * result	+ ((numericCode == null) ? 0 : numericCode.hashCode());
		result = prime * result	+ ((definitionSource == null) ? 0 : definitionSource.hashCode());
		result = prime * result	+ ((conceptCode == null) ? 0 : conceptCode.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		PermissibleValue other = (PermissibleValue) obj;
		if (!StringUtils.equals(optionName, other.optionName) ||
			(numericCode == null && other.numericCode != null) ||
			(numericCode != null && !numericCode.equals(other.numericCode)) ||
			!StringUtils.equals(definitionSource, other.definitionSource) ||
			!StringUtils.equals(conceptCode, other.conceptCode) ||
			!StringUtils.equals(value, other.value)) {
			return false;
		}

		return true;
	}

	@Override
	public int compareTo(PermissibleValue obj) {
		return this.value.compareToIgnoreCase(obj.value);
	}
}
