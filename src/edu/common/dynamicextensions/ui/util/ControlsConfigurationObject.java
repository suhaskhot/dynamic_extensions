package edu.common.dynamicextensions.ui.util;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author deepti_shelar
 *
 */
public class ControlsConfigurationObject
{
	private String controlName;
	private String displayLabel;
	private String jspName;
	private List commonValidationRules ;
	private Map dataTypeValidationRules ;
	private List dataTypesList ;
	
	/**
	 * @return the controlName
	 */
	public String getControlName()
	{
		return controlName;
	}
	/**
	 * @param controlName the controlName to set
	 */
	public void setControlName(String controlName)
	{
		this.controlName = controlName;
	}
	/**
	 * @return the displayLabel
	 */
	public String getDisplayLabel()
	{
		return displayLabel;
	}
	/**
	 * @param displayLabel the displayLabel to set
	 */
	public void setDisplayLabel(String displayLabel)
	{
		this.displayLabel = displayLabel;
	}
	/**
	 * @return the jspName
	 */
	public String getJspName()
	{
		return jspName;
	}
	/**
	 * @param jspName the jspName to set
	 */
	public void setJspName(String jspName)
	{
		this.jspName = jspName;
	}
	/**
	 * @return the dataTypes
	 */
	public List getDataTypesList()
	{
		return dataTypesList;
	}
	/**
	 * @param dataTypes the dataTypes to set
	 */
	public void setDataTypesList(List dataTypesList)
	{
		this.dataTypesList = dataTypesList;
	}
}
