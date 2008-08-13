
package edu.common.dynamicextensions.validation.category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.parser.CategoryCSVConstants;
import edu.common.dynamicextensions.util.parser.CategoryCSVFileParser;

/**
 * @author kunal_kamble
 *
 */
public class CategoryValidator
{
	EntityGroupInterface entityGroup;

	CategoryCSVFileParser categoryFileParser;

	StringBuffer errorMessage = new StringBuffer();

	/**
	 * @param entityGroup
	 */
	public CategoryValidator(CategoryCSVFileParser categoryFileParser)
	{
		this.categoryFileParser = categoryFileParser;
	}

	/**
	 * @return
	 */
	public EntityGroupInterface getEntityGroup()
	{
		return entityGroup;
	}

	/**
	 * @param entityGroup
	 */
	public void setEntityGroup(EntityGroupInterface entityGroup)
	{
		this.entityGroup = entityGroup;
	}

	/**
	 * @throws DynamicExtensionsSystemException
	 */
	public void validateMultiplicity() throws DynamicExtensionsSystemException
	{
		if (categoryFileParser.readLine()[0].split(":").length < 2)
		{
			throw new DynamicExtensionsSystemException(getErrorMessageStart() + " miltiplicity not defined.");
		}
	}

	/**
	 * @param entityName
	 * @throws DynamicExtensionsSystemException
	 */
	public void validateEntityName(String entityName) throws DynamicExtensionsSystemException
	{
		String errorMessage = getErrorMessageStart() + "Entity with name " + entityName + " does not exist";
		checkForNullRefernce(entityGroup.getEntityByName(entityName), errorMessage);
	}

	/**
	 * 
	 */
	public void validateSubcategoryTag()
	{
		categoryFileParser.getTargetContainerCaption();
	}

	/**
	 * @param object
	 * @param message
	 * @throws DynamicExtensionsSystemException
	 */
	public static void checkForNullRefernce(Object object, String message) throws DynamicExtensionsSystemException
	{
		if (object == null)
		{
			throw new DynamicExtensionsSystemException(message);
		}
	}
	
	public static void checkRangeAgainstAttributeValueRange(AttributeInterface attribute, Map<String, Object> rulesMap) throws DynamicExtensionsSystemException, ParseException
	{
		if (attribute == null)
		{
			throw new DynamicExtensionsSystemException("Attribute is NULL");
		}
		
		Map<String, Object> categoryMinMaxValuesMap = (Map<String, Object>) rulesMap.get(CategoryCSVConstants.RANGE.toLowerCase());
		
		if (categoryMinMaxValuesMap != null && !categoryMinMaxValuesMap.isEmpty())
		{
			String minValue = (String) categoryMinMaxValuesMap.get(CategoryCSVConstants.MIN.toLowerCase());
			String maxValue = (String) categoryMinMaxValuesMap.get(CategoryCSVConstants.MAX.toLowerCase());
			
			Set<RuleInterface> rules = new HashSet<RuleInterface>(attribute.getRuleCollection());
			String min = "";
			String max = "";
			
			for (RuleInterface rule : rules)
			{
				if (rule.getName().equalsIgnoreCase(CategoryCSVConstants.RANGE) || rule.getName().equalsIgnoreCase("dateRange"))
				{
					Set<RuleParameterInterface> ruleParameters = new HashSet<RuleParameterInterface>(rule.getRuleParameterCollection());
					
					for (RuleParameterInterface ruleParameter : ruleParameters)
					{
						if (ruleParameter.getName().equalsIgnoreCase(CategoryCSVConstants.MIN))
						{
							min = ruleParameter.getValue();
						}
						if (ruleParameter.getName().equalsIgnoreCase(CategoryCSVConstants.MAX))
						{
							max = ruleParameter.getValue();
						}
					}
				}
			}
			
			if (min.length() != 0 && max.length() != 0)
			{
				validateRange(attribute.getAttributeTypeInformation(), minValue, maxValue, min, max);
			}
		}
	}

	/**
	 * @param attributeTypeInformation
	 * @param minValue
	 * @param maxValue
	 * @param min
	 * @param max
	 * @throws DynamicExtensionsSystemException
	 * @throws ParseException
	 */
	private static void validateRange(AttributeTypeInformationInterface attributeTypeInformation, String minValue, String maxValue, String min, String max) throws DynamicExtensionsSystemException, ParseException
	{
		if (attributeTypeInformation instanceof IntegerAttributeTypeInformation || attributeTypeInformation instanceof StringAttributeTypeInformation)
		{
			if (Integer.parseInt(minValue) < Integer.parseInt(min) || Integer.parseInt(maxValue) > Integer.parseInt(max))
			{
				throw new DynamicExtensionsSystemException("Range values for category attributes must be a subset of range values for its attribute."); 
			}
		}
		else if (attributeTypeInformation instanceof DateAttributeTypeInformation)
		{		
			SimpleDateFormat sf = new SimpleDateFormat(ProcessorConstants.SQL_DATE_ONLY_FORMAT);
			sf.setLenient(false);
			if (sf.parse(minValue).before(sf.parse(min)) || sf.parse(maxValue).after(sf.parse(max)))
			{
				throw new DynamicExtensionsSystemException("Date range values for category attributes must be a subset of date range values for its attribute."); 
			}
		}
	}

	/**
	 * @return
	 */
	private String getErrorMessageStart()
	{
		return "Error at line:" + categoryFileParser.getLineNumber() + " ";
	}

}
