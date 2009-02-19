
package edu.common.dynamicextensions.xmi.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface;
import edu.common.dynamicextensions.validation.DateRangeValidator;
import edu.common.dynamicextensions.validation.RangeValidator;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author falguni_sachde
 *
 */
public class XMIImportValidator
{

	/**
	 * This method verify that default value specified is withing given range for numeric type attribute 
	 * @param attribute
	 * @param controlsForm
	 * @param defaultValue
	 * @throws DynamicExtensionsApplicationException
	 */
	public static void verifyDefaultValueIsInRange(AbstractAttributeInterface attribute,
			AbstractAttributeUIBeanInterface controlsForm, Number defaultValue)
			throws DynamicExtensionsApplicationException
	{

		if (controlsForm.getMin() != null && controlsForm.getMin().length() != 0
				&& controlsForm.getMax() != null && controlsForm.getMax().length() != 0
				&& defaultValue != null)
		{
			String min = controlsForm.getMin();
			String max = controlsForm.getMax();

			try
			{
				verifyDefaultValueIsInRange(attribute, String.valueOf(defaultValue), min, max,
						attribute.getName());
			}
			catch (DynamicExtensionsValidationException e)
			{
				ApplicationProperties.initBundle("ApplicationResources");
				List<String> placeHolders = new ArrayList<String>();
				placeHolders.add(defaultValue.toString());
				placeHolders.add(attribute.getName());
				placeHolders.add(min);
				placeHolders.add(max);
				Logger.out.info(ApplicationProperties.getValue("validationError")
						+ ApplicationProperties.getValue("defValueOORange", placeHolders));

				throw new DynamicExtensionsApplicationException(e.getMessage(),e);
			}
			catch (DataTypeFactoryInitializationException e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(),e);
			}
		}
	}

	/**
	 * This method verify that default value specified is withing given range for date type attribute only using daterangevalidator
	 * @param attribute
	 * @param controlsForm
	 * @param defaultValue
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException 
	 */
	public static void verifyDefaultValueForDateIsInRange(AbstractAttributeInterface attribute,
			AbstractAttributeUIBeanInterface controlsForm, String defaultValue)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{

		if (controlsForm.getMin() != null && controlsForm.getMin().length() != 0
				&& controlsForm.getMax() != null && controlsForm.getMax().length() != 0
				&& defaultValue != null)
		{
			String min = controlsForm.getMin();
			String max = controlsForm.getMax();
			Map<String, String> values = new HashMap<String, String>();
			values.put("min", min);
			values.put("max", max);

			DateRangeValidator dateRangeValidator = new DateRangeValidator();
			try
			{
				dateRangeValidator.validate((AttributeMetadataInterface) attribute, defaultValue,
						values, attribute.getName());

			}
			catch (DynamicExtensionsValidationException e)
			{
				ApplicationProperties.initBundle("ApplicationResources");
				Logger.out.info(ApplicationProperties.getValue("validationError")
						+ ApplicationProperties.getValue("defValueFor")
						+ ApplicationProperties.getValue(e.getErrorCode(), e.getPlaceHolderList()));
				throw new DynamicExtensionsSystemException(e.getMessage(),e);

			}
		}
	}

	/**
	 * @param attribute
	 * @param defaultValue
	 * @param min
	 * @param max
	 * @param attributeName
	 * @throws DynamicExtensionsValidationException
	 * @throws DataTypeFactoryInitializationException
	 */
	private static void verifyDefaultValueIsInRange(AbstractAttributeInterface attribute,
			String defaultValue, String min, String max, String attributeName)
			throws DynamicExtensionsValidationException, DataTypeFactoryInitializationException
	{
		if (defaultValue != null && min != null && max != null)
		{
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("min", min);
			parameterMap.put("max", max);

			RangeValidator rangeValidator = new RangeValidator();
			rangeValidator.validate((AttributeMetadataInterface) attribute, defaultValue,
					parameterMap, attributeName);
		}
	}

	/**
	 * @param attribute
	 * @param booleanVal
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public static String validateDefBooleanValue(AbstractAttributeInterface attribute,
			String booleanVal) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{

		if (!(booleanVal.trim().equalsIgnoreCase(ProcessorConstants.TRUE) || booleanVal.trim()
				.equalsIgnoreCase(ProcessorConstants.FALSE)))
		{
			ApplicationProperties.initBundle("ApplicationResources");
			List<String> placeHolders = new ArrayList<String>();
			placeHolders.add(booleanVal);
			placeHolders.add(attribute.getName());
			Logger.out.info(ApplicationProperties.getValue("validationError")
					+ ApplicationProperties.getValue("defValueBoolInvalid", placeHolders));

			throw new DynamicExtensionsApplicationException("Validation failed");

		}
		return booleanVal;

	}

	/**
	 * It will validate weather the data type of the attribute is valid for the
	 * primary key of the entity
	 * @param primaryAttribute
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static boolean validateDataTypeForPrimaryKey(AttributeInterface primaryAttribute)
			throws DynamicExtensionsApplicationException
	{
		AttributeTypeInformationInterface attrInfo = primaryAttribute.getAttributeTypeInformation();
		String attributeDataType = attrInfo.getDataType();

		if (EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE.equals(attributeDataType)
				|| EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE.equals(attributeDataType)
				|| EntityManagerConstantsInterface.OBJECT_ATTRIBUTE_TYPE.equals(attributeDataType))
		{
			throw new DynamicExtensionsApplicationException(
					"Data Type of the primaryKey Attribute " + primaryAttribute.getName()
							+ " is not acceptable for " + primaryAttribute.getEntity().getName());
		}

		return true;
	}
}
