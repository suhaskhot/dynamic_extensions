
package edu.common.dynamicextensions.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import edu.common.dynamicextensions.domaininterface.AssociationMetadataInterface;

import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Sujay Narkar
 * @author Rahul Ner
 * @author chetan_patil
 */
public class ValidatorUtil
{

	/**
	 * @param attributeValueMap  key - AttributeInterface
	 *                           value - value that user has entred for this attribute
	 * @return errorList if any
	 * @throws DynamicExtensionsSystemException : Exception 
	 */
	public static List<String> validateEntity(
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, List<String> errorList)
			throws DynamicExtensionsSystemException
	{
		if(errorList == null)
		{
			errorList = new ArrayList<String>();
		}
		Set<Map.Entry<BaseAbstractAttributeInterface, Object>> attributeSet = attributeValueMap
				.entrySet();
		if (attributeSet != null || !attributeSet.isEmpty())
		{
			for (Map.Entry<BaseAbstractAttributeInterface, Object> attributeValueNode : attributeSet)
			{
				BaseAbstractAttributeInterface abstractAttribute = attributeValueNode.getKey();
				if (abstractAttribute instanceof AttributeMetadataInterface)
				{
					errorList.addAll(validateAttributes(attributeValueNode));
				}
				else if (abstractAttribute instanceof AssociationMetadataInterface)
				{
					AssociationMetadataInterface associationInterface = (AssociationMetadataInterface) abstractAttribute;
					if (AssociationType.CONTAINTMENT.equals(associationInterface.getAssociationType()))
					{
						List<Map<BaseAbstractAttributeInterface, Object>> valueObject = (List<Map<BaseAbstractAttributeInterface, Object>>) attributeValueMap
								.get(abstractAttribute);
						for (Map<BaseAbstractAttributeInterface, Object> subAttributeValueMap : valueObject)
						{
							errorList.addAll(validateEntityAttributes(subAttributeValueMap));
						}
					}
				}
			}
		}

		
		return errorList;
	}

	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private static List<String> validateEntityAttributes(
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap)
			throws DynamicExtensionsSystemException
	{
		List<String> errorList = new ArrayList<String>();

		Set<Map.Entry<BaseAbstractAttributeInterface, Object>> attributeSet = attributeValueMap
				.entrySet();
		if (attributeSet == null || attributeSet.isEmpty())
		{
			return errorList;
		}

		for (Map.Entry<BaseAbstractAttributeInterface, Object> attributeValueNode : attributeSet)
		{
			BaseAbstractAttributeInterface abstractAttribute = attributeValueNode.getKey();
			if (abstractAttribute instanceof AttributeMetadataInterface)
			{
				errorList.addAll(validateAttributes(attributeValueNode));
			}
		}

		return errorList;
	}

	private static List<String> validateAttributes(
			Map.Entry<BaseAbstractAttributeInterface, Object> attributeValueNode)
			throws DynamicExtensionsSystemException
	{
		List<String> errorList = new ArrayList<String>();

		BaseAbstractAttributeInterface abstractAttribute = attributeValueNode.getKey();
		AttributeMetadataInterface attribute = (AttributeMetadataInterface) abstractAttribute;
		Collection<RuleInterface> attributeRuleCollection = attribute.getRuleCollection();
		if (attributeRuleCollection != null || !attributeRuleCollection.isEmpty())
		{
			String errorMessage = null;
			for (RuleInterface rule : attributeRuleCollection)
			{
				String ruleName = rule.getName();
				if (!ruleName.equals("unique"))
				{
					Object valueObject = attributeValueNode.getValue();
					Map<String, String> parameterMap = getParamMap(rule);
					try
					{
						checkValidation(attribute, valueObject, rule, parameterMap);
					}
					catch (DynamicExtensionsValidationException e)
					{
						errorMessage = ApplicationProperties.getValue(e.getErrorCode(), e
								.getPlaceHolderList());
						errorList.add(errorMessage);
					}
				}
			}
		}

		return errorList;
	}

	public static void checkUniqueValidationForAttribute(AttributeMetadataInterface attribute,
			Object valueObject, Long recordId) throws DynamicExtensionsValidationException,
			DynamicExtensionsSystemException
	{
		Collection<RuleInterface> attributeRuleCollection = attribute.getRuleCollection();

		if (attributeRuleCollection != null || !attributeRuleCollection.isEmpty())
		{
			for (RuleInterface rule : attributeRuleCollection)
			{
				String ruleName = rule.getName();
				if (ruleName.equals("unique"))
				{
					Map<String, String> parameterMap = new HashMap<String, String>();
					if (recordId != null)
					{
						parameterMap.put("recordId", recordId.toString());
					}
					checkValidation(attribute, valueObject, rule, parameterMap);
					break;
				}
			}
		}
	}

	private static void checkValidation(AttributeMetadataInterface attribute, Object valueObject,
			RuleInterface rule, Map<String, String> parameterMap)
			throws DynamicExtensionsSystemException, DynamicExtensionsValidationException
	{
		String ruleName = rule.getName();
		ValidatorRuleInterface validatorRule = ControlConfigurationsFactory.getInstance()
				.getValidatorRule(ruleName);
		validatorRule.validate(attribute, valueObject, parameterMap);
	}

	/**
	 * This method returns the Map of parameters of the Rule.
	 * @param ruleInterface the Rule instance whose Map of parameter are to be fetched.
	 * @return the Map of parameters of the Rule.
	 * 					key - name of parameter
	 * 					value - value of parameter
	 */
	private static Map<String, String> getParamMap(RuleInterface rule)
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