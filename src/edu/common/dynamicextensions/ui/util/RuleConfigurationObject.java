package edu.common.dynamicextensions.ui.util;

import java.util.Map;

/**
 * 
 * @author deepti_shelar
 *
 */
public class RuleConfigurationObject
{
	private String displayLabel;
	private String ruleName;
	private String ruleClassName;
	private String errorKey;
	private Map ruleParametersMap = null;
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
	 * @return the errorKey
	 */
	public String getErrorKey()
	{
		return errorKey;
	}
	/**
	 * @param errorKey the errorKey to set
	 */
	public void setErrorKey(String errorKey)
	{
		this.errorKey = errorKey;
	}
	
	
	/**
	 * @return the ruleClassName
	 */
	public String getRuleClassName()
	{
		return ruleClassName;
	}
	/**
	 * @param ruleClassName the ruleClassName to set
	 */
	public void setRuleClassName(String ruleClassName)
	{
		this.ruleClassName = ruleClassName;
	}
	/**
	 * @return the ruleName
	 */
	public String getRuleName()
	{
		return ruleName;
	}
	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName)
	{
		this.ruleName = ruleName;
	}
	/**
	 * @return the ruleParametersMap
	 */
	public Map getRuleParametersMap()
	{
		return ruleParametersMap;
	}
	/**
	 * @param ruleParametersMap the ruleParametersMap to set
	 */
	public void setRuleParametersMap(Map ruleParametersMap)
	{
		this.ruleParametersMap = ruleParametersMap;
	}
}
