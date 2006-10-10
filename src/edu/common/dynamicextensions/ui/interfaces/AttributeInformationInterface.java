package edu.common.dynamicextensions.ui.interfaces;

import java.util.List;

/**
 * 
 * @author deepti_shelar
 *
 */
public interface AttributeInformationInterface {
	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName();
	/**
	 * @param attributeName The attributeName to set.
	 */
	public void setAttributeName(String attributeName);
	/**
	 * @return Returns the attributeDescription.
	 */
	public String getAttributeDescription();
	/**
	 * @param attributeDescription The attributeDescription to set.
	 */
	public void setAttributeDescription(String attributeDescription);
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
	public String getAttributeFormat(); 
	/**
	 * @param attributeFormat The attributeFormat to set.
	 */
	public void setAttributeFormat(String attributeFormat);
	/**
	 * @return the selectedControlAttributesList
	 */
	public List getSelectedControlAttributesList();

	/**
	 * @param selectedControlAttributesList the selectedControlAttributesList to set
	 */
	public void setSelectedControlAttributesList(List selectedControlAttributesList);
	/**
	 * @return Returns the dataType.
	 */
	public String getDataType();
	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(String dataType);
	/**
	 * @return Returns the dataTypeList.
	 */
	public List getDataTypeList();
	/**
	 * @param dataTypeList The dataTypeList to set.
	 */
	public void setDataTypeList(List dataTypeList);
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
	 * @return Returns the choiceList.
	 */
	public String getChoiceList() ;
	/**
	 * @param choiceList The choiceList to set.
	 */
	public void setChoiceList(String choiceList) ;
	/**
	 * @return Returns the attributeIdentifier.
	 */
	public String getAttributeIdentifier() ;
	/**
	 * @param attributeIdentifier The attributeIdentifier to set.
	 */
	public void setAttributeIdentifier(String attributeIdentifier);
	/**
	 * @return the attributeCssClass
	 */
	public String getAttributeCssClass() ;

	/**
	 * @param attributeCssClass the attributeCssClass to set
	 */
	public void setAttributeCssClass(String attributeCssClass) ;

	/**
	 * @return the attributeMeasurementUnits
	 */
	public String getAttributeMeasurementUnits() ;

	/**
	 * @param attributeMeasurementUnits the attributeMeasurementUnits to set
	 */
	public void setAttributeMeasurementUnits(String attributeMeasurementUnits);

	/**
	 * @return the attributeMultiSelect
	 */
	public Boolean getAttributeMultiSelect() ;

	/**
	 * @param attributeMultiSelect the attributeMultiSelect to set
	 */
	public void setAttributeMultiSelect(Boolean attributeMultiSelect) ;

	/**
	 * @return the attributenoOfCols
	 */
	public String getAttributenoOfCols() ;

	/**
	 * @param attributenoOfCols the attributenoOfCols to set
	 */
	public void setAttributenoOfCols(String attributenoOfCols) ;

	/**
	 * @return the attributeNoOfRows
	 */
	public String getAttributeNoOfRows() ;

	/**
	 * @param attributeNoOfRows the attributeNoOfRows to set
	 */
	public void setAttributeNoOfRows(String attributeNoOfRows) ;

	/**
	 * @return the attributeScale
	 */
	public String getAttributeScale() ;

	/**
	 * @param attributeScale the attributeScale to set
	 */
	public void setAttributeScale(String attributeScale) ;

	/**
	 * @return the attributeTooltip
	 */
	public String getAttributeTooltip() ;

	/**
	 * @param attributeTooltip the attributeTooltip to set
	 */
	public void setAttributeTooltip(String attributeTooltip) ;

}
