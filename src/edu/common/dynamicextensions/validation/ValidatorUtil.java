
package edu.common.dynamicextensions.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
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
	public static List<String> validateEntity(Map<AbstractAttributeInterface, Object> attributeValueMap) throws DynamicExtensionsSystemException
	{
		List<String> errorList = new ArrayList<String>();
		HashSet<String> errorSet = new HashSet<String>();

		Set<Map.Entry<AbstractAttributeInterface, Object>> attributeSet = attributeValueMap.entrySet();
		if (attributeSet == null || attributeSet.isEmpty())
		{
			return errorList;
		}

		for (Map.Entry<AbstractAttributeInterface, Object> attributeValueNode : attributeSet)
		{
			AbstractAttributeInterface abstractAttribute = attributeValueNode.getKey();
			if (abstractAttribute instanceof AttributeInterface)
			{
				errorSet.addAll(validateAttributes(attributeValueNode));
			}
			else if (abstractAttribute instanceof AssociationInterface)
			{
				List<Map<AbstractAttributeInterface, Object>> valueObject = (List<Map<AbstractAttributeInterface, Object>>) attributeValueMap
						.get(abstractAttribute);
				for (Map<AbstractAttributeInterface, Object> subAttributeValueMap : valueObject)
				{
					errorSet.addAll(validateEntityAttributes(subAttributeValueMap));
				}
			}
		}

		for (String error : errorSet)
		{
			errorList.add(error);
		}
		return errorList;
	}

	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private static List<String> validateEntityAttributes(Map<AbstractAttributeInterface, Object> attributeValueMap)
			throws DynamicExtensionsSystemException
	{
		List<String> errorList = new ArrayList<String>();

		Set<Map.Entry<AbstractAttributeInterface, Object>> attributeSet = attributeValueMap.entrySet();
		if (attributeSet == null || attributeSet.isEmpty())
		{
			return errorList;
		}

		for (Map.Entry<AbstractAttributeInterface, Object> attributeValueNode : attributeSet)
		{
			AbstractAttributeInterface abstractAttribute = attributeValueNode.getKey();
			if (abstractAttribute instanceof AttributeInterface)
			{
				errorList.addAll(validateAttributes(attributeValueNode));
			}
		}

		return errorList;
	}

	private static List<String> validateAttributes(Map.Entry<AbstractAttributeInterface, Object> attributeValueNode)
			throws DynamicExtensionsSystemException
	{
		List<String> errorList = new ArrayList<String>();

		AbstractAttributeInterface abstractAttribute = attributeValueNode.getKey();
		AttributeInterface attribute = (AttributeInterface) abstractAttribute;
		Collection<RuleInterface> attributeRuleCollection = attribute.getRuleCollection();
		if (attributeRuleCollection != null || !attributeRuleCollection.isEmpty())
		{
			for (RuleInterface rule : attributeRuleCollection)
			{
				ValidatorRuleInterface validatorRule = ControlConfigurationsFactory.getInstance().getValidatorRule(rule.getName());
				Object valueObject = attributeValueNode.getValue();
				Map<String, String> parameterMap = getParamMap(rule);
				try
				{
					validatorRule.validate(attribute, valueObject, parameterMap);
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
	 * This method returns the Map of parameters of the Rule.
	 * @param ruleInterface the Rule instance whose Map of parameter are to be fetched.
	 * @return the Map of parameters of the Rule.
	 * 					key - name of parameter
	 * 					value - value of parameter
	 */
	public static Map<String, String> getParamMap(RuleInterface rule)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		Collection<RuleParameterInterface> ruleParamCollection = rule.getRuleParameterCollection();
		if (ruleParamCollection != null && !ruleParamCollection.isEmpty())
		{
			for (RuleParameterInterface ruleParameter : ruleParamCollection)
			{
				parameterMap.put(ruleParameter.getName(), ruleParameter.getValue());
			}
		}
		return parameterMap;
	}

}