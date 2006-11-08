
package edu.common.dynamicextensions.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Sujay Narkar
 * @author Rahul Ner
 */

public class ValidatorUtil
{

	/**
	 * @param attributeValueMap  key - AttributeInterface
	 *                           value - value that user has entred for this attribute
	 * @return errorList if any
	 * @throws DynamicExtensionsSystemException : Exception 
	 */
	public static List<String> validateEntity(Map attributeValueMap) throws DynamicExtensionsSystemException
	{

		List<String> errorList = new ArrayList<String>();

		Set attributeSet = attributeValueMap.keySet();
		if (attributeSet == null || attributeSet.isEmpty())
		{
			return errorList;
		}

		Iterator attributeIterator = attributeSet.iterator();
		while (attributeIterator.hasNext())
		{
			AttributeInterface attribute = (AttributeInterface) attributeIterator.next();
			Collection attributeRuleCollection = attribute.getRuleCollection();
			if (attributeRuleCollection == null || attributeRuleCollection.isEmpty())
			{
				return errorList;
			}
			
			Iterator attributeRuleIterator = attributeRuleCollection.iterator();
			while (attributeRuleIterator.hasNext())
			{
				RuleInterface ruleInterface = (RuleInterface) attributeRuleIterator.next();
				ValidatorRuleInterface validatorRule = ControlConfigurationsFactory.getInstance().getValidatorRule(ruleInterface.getName());
				Object valueObject = attributeValueMap.get(attribute);
				Map paramMap = getParamMap(ruleInterface);
				try
				{
					validatorRule.validate(attribute, valueObject, paramMap);
				}
				catch (DynamicExtensionsValidationException e)
				{
					String errorMessage = ApplicationProperties.getValue(e.getErrorCode(), e.getPlaceHolderList());
					errorList.add(errorMessage);
				}
			}
		}
		
		return errorList;
	}

	/**
	 * 
	 * @param ruleInterface  :Rule interface
	 * @return Map of parameters
	 */
	public static Map<String, String> getParamMap(RuleInterface ruleInterface)
	{
		Map<String, String> paramMap = new HashMap<String, String>();
		Collection<RuleParameterInterface> ruleParamCollection = ruleInterface.getRuleParameterCollection();
		if (ruleParamCollection != null && !ruleParamCollection.isEmpty())
		{
			for(RuleParameterInterface ruleParameter: ruleParamCollection)
			{
				paramMap.put(ruleParameter.getName(), ruleParameter.getValue());
			}
		}
		return paramMap;
	}
	
}