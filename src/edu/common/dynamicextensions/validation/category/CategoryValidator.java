
package edu.common.dynamicextensions.validation.category;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.NumericAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.CategoryGenerationUtil;
import edu.common.dynamicextensions.util.global.CategoryConstants;
import edu.common.dynamicextensions.util.parser.CategoryCSVConstants;
import edu.common.dynamicextensions.util.parser.CategoryCSVFileParser;
import edu.common.dynamicextensions.validation.DateRangeValidator;
import edu.common.dynamicextensions.validation.RangeValidator;
import edu.wustl.common.util.global.ApplicationProperties;

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
		ApplicationProperties.initBundle(CategoryCSVConstants.DYNAMIC_EXTENSIONS_ERROR_MESSAGES_FILE);
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
			throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS) + getErrorMessageStart()
					+ ApplicationProperties.getValue(CategoryConstants.MULT_UNDEFINED));
		}
	}

	/**
	 * @param entityName
	 * @throws DynamicExtensionsSystemException
	 */
	public void validateEntityName(String entityName) throws DynamicExtensionsSystemException
	{
		String errorMessage = getErrorMessageStart() + ApplicationProperties.getValue(CategoryConstants.NO_ENTITY) + entityName;
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
			throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS) + message);
		}
	}

	/**
	 * This method checks whether the range specified for category attribute is
	 * a valid subset of range specified for its attribute.
	 * @param attribute
	 * @param rules
	 * @throws DynamicExtensionsSystemException
	 * @throws ParseException
	 * @throws DynamicExtensionsValidationException 
	 */
	public static void checkRangeAgainstAttributeValueRange(AttributeInterface attribute, Map<String, Object> rules)
			throws DynamicExtensionsSystemException, ParseException
	{
		if (attribute == null)
		{
			throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
					+ ApplicationProperties.getValue(CategoryConstants.NULL_ATTR));
		}

		if (!rules.isEmpty())
		{
			Map<String, Object> catMinMaxValues = null;

			if (rules.containsKey(CategoryCSVConstants.DATE_RANGE))
			{
				catMinMaxValues = (Map<String, Object>) rules.get(CategoryCSVConstants.DATE_RANGE);
			}
			else if (rules.containsKey(CategoryCSVConstants.RANGE.toLowerCase()))
			{
				catMinMaxValues = (Map<String, Object>) rules.get(CategoryCSVConstants.RANGE.toLowerCase());
			}

			if (catMinMaxValues != null && !catMinMaxValues.isEmpty())
			{
				validateAttributeTypeInformationMetadata(attribute);
				validateRangeValues(attribute, catMinMaxValues);
			}
		}
	}

	/**
	 * This method checks whether the range specified for category attribute is
	 * a valid subset of range specified for its attribute.
	 * @param attribute
	 * @param catMinMaxValues
	 * @throws DynamicExtensionsSystemException
	 */
	private static void validateRangeValues(AttributeInterface attribute, Map<String, Object> catMinMaxValues)
			throws DynamicExtensionsSystemException
	{
		String minValue = (String) catMinMaxValues.get(CategoryCSVConstants.MIN.toLowerCase());
		String maxValue = (String) catMinMaxValues.get(CategoryCSVConstants.MAX.toLowerCase());

		Map<String, String> values = new HashMap<String, String>();

		Set<RuleInterface> attributeRules = new HashSet<RuleInterface>(attribute.getRuleCollection());

		for (RuleInterface rule : attributeRules)
		{
			if (rule.getName().equalsIgnoreCase(CategoryCSVConstants.RANGE) || rule.getName().equalsIgnoreCase(ProcessorConstants.DATE_RANGE))
			{
				Set<RuleParameterInterface> ruleParameters = new HashSet<RuleParameterInterface>(rule.getRuleParameterCollection());

				for (RuleParameterInterface ruleParameter : ruleParameters)
				{
					if (ruleParameter.getName().equalsIgnoreCase(CategoryCSVConstants.MIN))
					{
						String min = ruleParameter.getValue();
						values.put(CategoryCSVConstants.MIN.toLowerCase(), min);
						validateRange(minValue, attribute, values);
						values.clear();
					}
					if (ruleParameter.getName().equalsIgnoreCase(CategoryCSVConstants.MAX))
					{
						String max = ruleParameter.getValue();
						values.put(CategoryCSVConstants.MAX.toLowerCase(), max);
						validateRange(maxValue, attribute, values);
						values.clear();
					}
				}
			}
		}
	}

	/**
	 * This method performs some basic validations like checking if the attribute or
	 * the attribute type information is null. It also checks whether range is specified 
	 * for valid attribute type information.
	 * @param attribute
	 * @throws DynamicExtensionsSystemException
	 */
	private static void validateAttributeTypeInformationMetadata(AttributeInterface attribute) throws DynamicExtensionsSystemException
	{
		AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();

		if (attrTypeInfo == null)
		{
			throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
					+ ApplicationProperties.getValue(CategoryConstants.NULL_ATTR_TYPE_INFO) + attribute.getName());
		}

		if (!(attrTypeInfo instanceof NumericAttributeTypeInformation || attrTypeInfo instanceof DateAttributeTypeInformation))
		{
			throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
					+ ApplicationProperties.getValue(CategoryConstants.NON_NUM_RANGE) + attribute.getName());
		}
	}

	/**
	 * This method checks whether the range specified for category attribute 
	 * is a valid subset of range specified for its attribute.
	 * @param catAttrValue
	 * @param attribute
	 * @param values
	 * @throws DynamicExtensionsSystemException
	 */
	private static void validateRange(String catAttrValue, AttributeInterface attribute, Map<String, String> values)
			throws DynamicExtensionsSystemException
	{
		AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();

		if (attrTypeInfo instanceof NumericAttributeTypeInformation)
		{
			RangeValidator rangeValidator = new RangeValidator();

			try
			{
				rangeValidator.validate((AttributeMetadataInterface) attribute, catAttrValue, values, attribute.getName());
			}
			catch (DynamicExtensionsValidationException e)
			{
				throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
						+ ApplicationProperties.getValue(e.getErrorCode()) + attribute.getName());
			}
		}
		else
		{
			DateRangeValidator dateRangeValidator = new DateRangeValidator();

			try
			{
				dateRangeValidator.validate((AttributeMetadataInterface) attribute, catAttrValue, values, attribute.getName());
			}
			catch (DynamicExtensionsValidationException e)
			{
				throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
						+ ApplicationProperties.getValue(e.getErrorCode()) + attribute.getName());
			}
		}
	}

	/**
	 * @param attribute
	 * @param rules
	 * @throws DynamicExtensionsSystemException
	 * @throws ParseException
	 */
	public static void checkRequiredRule(AttributeInterface attribute, Map<String, Object> rules) throws DynamicExtensionsSystemException,
			ParseException
	{
		if (attribute == null)
		{
			throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
					+ ApplicationProperties.getValue(CategoryConstants.NULL_ATTR));
		}

		for (RuleInterface rule : attribute.getRuleCollection())
		{
			if (rule.getName().equalsIgnoreCase(CategoryCSVConstants.REQUIRED) && (rules == null || rules.isEmpty()))
			{
				throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
						+ ApplicationProperties.getValue(CategoryConstants.NO_OVERRIDE_REQ_RULE) + attribute.getName());
			}
		}
	}

	/**
	 * @return
	 */
	private String getErrorMessageStart()
	{
		return ApplicationProperties.getValue(CategoryConstants.LINE_NUMBER) + categoryFileParser.getLineNumber() + " ";
	}

	/**
	 * User should not be allowed to use root entity twice in the category creation.
	 * @param entityName
	 * @param mainForms
	 * @param catEntNames
	 * @throws DynamicExtensionsSystemException
	 */
	public void isRootEntityUsedTwice(String entityName, List<String> mainForms, Collection<String> catEntNames)
			throws DynamicExtensionsSystemException
	{
		if (!mainForms.contains(entityName))
		{
			return;
		}

		String entName = null;
		for (String categoryEntityName : catEntNames)
		{
			entName = CategoryGenerationUtil.getEntityName(categoryEntityName);
			if (entName.equals(entityName))
			{
				String errorMessage = getErrorMessageStart() + ApplicationProperties.getValue(CategoryConstants.ROOT_ENT_TWICE) + entityName;
				throw new DynamicExtensionsSystemException(errorMessage);
			}
		}
	}

	/**
	 * This method checks whether 'textArea' is the control type specified for numeric type field.  
	 * @param controlType
	 * @param attribute
	 * @throws DynamicExtensionsSystemException
	 */
	public void isTextAreaForNumeric(String controlType, AttributeInterface attribute) throws DynamicExtensionsSystemException
	{
		if (CategoryCSVConstants.TEXT_AREA.equals(controlType))
		{
			AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();
			if (attrTypeInfo instanceof NumericAttributeTypeInformation)
			{
				throw new DynamicExtensionsSystemException(ApplicationProperties.getValue(CategoryConstants.CREATE_CAT_FAILS)
						+ ApplicationProperties.getValue(CategoryConstants.LINE_NUMBER) + categoryFileParser.getLineNumber()
						+ ApplicationProperties.getValue(CategoryConstants.NO_TEXTAREA) + attribute.getName());
			}
		}
	}
}
