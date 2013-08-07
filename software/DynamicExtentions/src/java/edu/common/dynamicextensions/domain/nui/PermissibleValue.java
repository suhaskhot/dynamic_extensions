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
	
	@Override
	public int hashCode()
	{
		int hashCodeValue;
		if (value != null)
		{
			hashCodeValue = value.hashCode();
		}
		else
		{
			hashCodeValue = 0;
		}
		return hashCodeValue;
	}

	 @Override
	    public boolean equals(Object obj) {
	        if (obj == this) {
	            return true;
	        }
	        if (obj == null || obj.getClass() != this.getClass()) {
	            return false;
	        }

	        PermissibleValue guest = (PermissibleValue) obj;
	        return value.equals(guest.value);
	               
	    }


}
