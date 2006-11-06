
package edu.common.dynamicextensions.ui.interfaces;

/**
 * 
 * @author deepti_shelar
 *
 */
public interface AbstractAttributeUIBeanInterface
{

	/**
	 * Returns the attributesize
	 * @return String AttributeSize
	 */
	String getAttributeSize();

	/**
	 * @param attributeSize The attributeSize to set.
	 */
	void setAttributeSize(String attributeSize);

	/**
	 * @return Returns the attributeFormat.
	 */
	 String getFormat();

	/**
	 * @param attributeFormat The attributeFormat to set.
	 */
	 void setFormat(String attributeFormat);

	/**
	 * @return Returns the attributeDefaultValue.
	 */
	 String getAttributeDefaultValue();

	/**
	 * @param attributeDefaultValue The attributeDefaultValue to set.
	 */
	 void setAttributeDefaultValue(String attributeDefaultValue);

	/**
	 * @return Returns the attributeDisplayUnits.
	 */
	 String getAttributeDisplayUnits();

	/**
	 * @param attributeDisplayUnits The attributeDisplayUnits to set.
	 */
	 void setAttributeDisplayUnits(String attributeDisplayUnits);

	/**
	 * @return Returns the attributeDecimalPlaces.
	 */
	 String getAttributeDecimalPlaces();

	/**
	 * @param attributeDecimalPlaces The attributeDecimalPlaces to set.
	 */
	 void setAttributeDecimalPlaces(String attributeDecimalPlaces);

	/**
	 * @return Returns the attributeIdentifier.
	 */
	 String getAttributeIdentifier();

	/**
	 * @param attributeIdentifier The attributeIdentifier to set.
	 */
	 void setAttributeIdentifier(String attributeIdentifier);

	/**
	 * @return the attributeMeasurementUnits
	 */
	 String getAttributeMeasurementUnits();

	/**
	 * @param attributeMeasurementUnits the attributeMeasurementUnits to set
	 */
	 void setAttributeMeasurementUnits(String attributeMeasurementUnits);

	/**
	 * @return Returns the dataType.
	 */

	 String getDataType();

	/**
	 * @param dataType The dataType to set.
	 */
	 void setDataType(String dataType);

	/**
	 * Number of digits before decimal 
	 * @return String AttributeDigits
	 */
	 String getAttributeDigits();

	/**
	 * @param attributeDigits : Number of digits before decimal
	 */
	 void setAttributeDigits(String attributeDigits);

	/**
	 * 
	 * @return Name
	 */
	 String getName();

	/**
	 * 
	 * @param name Name
	 */
	 void setName(String name);

	/**
	 * 
	 * @return Description
	 */
	 String getDescription();

	/**
	 * 
	 * @param name Description
	 */
	 void setDescription(String name);

	/**
	 * @return Returns the choiceList.
	 */
	 String getChoiceList();

	/**
	 * @param choiceList The choiceList to set.
	 */
	 void setChoiceList(String choiceList);

	/**
	 * @return Returns the displayChoice.
	 */
	 String getDisplayChoice();

	/**
	 * @param displayChoice The displayChoice to set.
	 */
	 void setDisplayChoice(String displayChoice);

	/**
	 * 
	 * @return Concept code
	 */
	 String getAttributeConceptCode();

	/**
	 * 
	 * @param conceptCode Concept code
	 */
	 void setAttributeConceptCode(String conceptCode);

	/**
	 * 
	 * @return String DateValueType
	 */
	 String getDateValueType();

	/**
	 * 
	 * @param dateValueType dateValueType
	 */
	 void setDateValueType(String dateValueType);

	/**
	 * 
	 * @return String AttributeIdentified
	 */
	 String getAttributeIdentified();

	/**
	 * 
	 * @param attributeIdentified attributeIdentified
	 */
	 void setAttributeIdentified(String attributeIdentified);

	/**
	 * Returns validationRules : value of checkbox fields selected by user
	 * @return String[] ValidationRules
	 */
	String[] getValidationRules();

	/**
	 * 
	 * @param validationRules value of checkbox fields
	 */
	void setValidationRules(String[] validationRules);

	/**
	 * @return the max
	 */
	String getMax();

	/**
	 * @param max the max to set
	 */
	void setMax(String max);

	/**
	 * @return the min
	 */
	String getMin();

	/**
	 * @param min the min to set
	 */
	void setMin(String min);
}
