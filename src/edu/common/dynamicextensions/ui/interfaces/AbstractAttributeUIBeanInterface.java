package edu.common.dynamicextensions.ui.interfaces;


/**
 * 
 * @author deepti_shelar
 *
 */
public interface AbstractAttributeUIBeanInterface {
	
	/**
	 * Returns the attributesize
	 * @return
	 */
	public String getAttributeSize() ;
	/**
	 * @param attributeSize The attributeSize to set.
	 */
	public void setAttributeSize(String attributeSize);
	/**
	 * @return Returns the attributeFormat.
	 */
	public String getFormat(); 
	/**
	 * @param attributeFormat The attributeFormat to set.
	 */
	public void setFormat(String attributeFormat);
	
	/**
	 * @return Returns the attributeDefaultValue.
	 */
	public String getAttributeDefaultValue() ;
	/**
	 * @param attributeDefaultValue The attributeDefaultValue to set.
	 */
	public void setAttributeDefaultValue(String attributeDefaultValue) ;
	/**
	 * @return Returns the attributeDisplayUnits.
	 */
	public String getAttributeDisplayUnits() ;
	/**
	 * @param attributeDisplayUnits The attributeDisplayUnits to set.
	 */
	public void setAttributeDisplayUnits(String attributeDisplayUnits) ;
	/**
	 * @return Returns the attributeDecimalPlaces.
	 */
	public String getAttributeDecimalPlaces() ;
	/**
	 * @param attributeDecimalPlaces The attributeDecimalPlaces to set.
	 */
	public void setAttributeDecimalPlaces(String attributeDecimalPlaces) ;
	
	/**
	 * @return Returns the attributeIdentifier.
	 */
	public String getAttributeIdentifier() ;
	/**
	 * @param attributeIdentifier The attributeIdentifier to set.
	 */
	public void setAttributeIdentifier(String attributeIdentifier);
	
	/**
	 * @return the attributeMeasurementUnits
	 */
	public String getAttributeMeasurementUnits() ;

	/**
	 * @param attributeMeasurementUnits the attributeMeasurementUnits to set
	 */
	public void setAttributeMeasurementUnits(String attributeMeasurementUnits);
	/**
	 * @return Returns the dataType.
	 */

	public String getDataType();
	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(String dataType);
	
	/**
	 * Number of digits before decimal 
	 * @return
	 */
	public String getAttributeDigits();
	
	/**
	 * @param attributeDigits : Number of digits before decimal
	 */
	public void setAttributeDigits(String attributeDigits);
	
	/**
	 * 
	 * @return Name
	 */
	public String getName();
	
	/**
	 * 
	 * @param name Name
	 */
	public void setName(String name);
	
	/**
	 * 
	 * @return Description
	 */
	public String getDescription();
	
	/**
	 * 
	 * @param name Description
	 */
	public void setDescription(String name);
	/**
	 * @return Returns the choiceList.
	 */
	public String getChoiceList(); 
	/**
	 * @param choiceList The choiceList to set.
	 */
	public void setChoiceList(String choiceList);
	/**
	 * @return Returns the displayChoice.
	 */
	public String getDisplayChoice();
	/**
	 * @param displayChoice The displayChoice to set.
	 */
	public void setDisplayChoice(String displayChoice);
	/**
	 * 
	 * @return Concept code
	 */
	public String getAttributeConceptCode();

	/**
	 * 
	 * @param conceptCode Concept code
	 */
	public void setAttributeConceptCode(String conceptCode);
	/**
	 * 
	 * @return
	 */
	public String getDateValueType();
/**
 * 
 * @param dateValueType
 */
	public void setDateValueType(String dateValueType);
	/**
	 * 
	 * @return
	 */
	public String getAttributeIdentified();
	/**
	 * 
	 * @param attributeIdentified
	 */
	public void setAttributeIdentified(String attributeIdentified);
	
	/**
	 * 
	 * @return
	 */
	String[] getValidationRules();
	/**
	 * 
	 * @param validationRules
	 */
	void setValidationRules(String[] validationRules);
}
}