package edu.common.dynamicextensions.ui.interfaces;
/**
 * @author deepti_shelar
 */
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public interface ControlInformationInterface {
	/**
	 * @return Returns the attribute.
	 *
	 */
	public AttributeInterface getAttribute();
	/**
	 * @param attribute The attribute to set.
	 */
	public void setAttribute(AttributeInterface attributeInterface);

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
	 * @return Returns the choiceList.
	 */
	public String getChoiceList() ;
	/**
	 * @param choiceList The choiceList to set.
	 */
	public void setChoiceList(String choiceList) ;

	/**
	 * @return the attributeCssClass
	 */
	public String getCssClass() ;

	/**
	 * @param attributeCssClass the attributeCssClass to set
	 */
	public void setCssClass(String attributeCssClass) ;

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
	public String getTooltip() ;

	/**
	 * @param attributeTooltip the attributeTooltip to set
	 */
	public void setTooltip(String attributeTooltip) ;

	/**
	 * @return the attributeCaption
	 */
	public String getCaption() ;

	/**
	 * @param attributeTooltip the attributeTooltip to set
	 */
	public void setCaption(String attributeTooltip) ;

	/**
	 * 
	 * @return If the control is a password field
	 */
	public Boolean getIsPassword();

	/**
	 * 
	 * @param isPassword If it is a password field set as true
	 */
	public void setIsPassword(Boolean isPassword);


	/**
	 * @return the toolsList
	 */
	public List getToolsList(); 

	/**
	 * @param toolsList the toolsList to set
	 */
	public void setToolsList(List toolsList); 
	/**
	 * @return the userSelectedTool
	 */
	public String getUserSelectedTool();

	/**
	 * @param userSelectedTool the userSelectedTool to set
	 */
	public void setUserSelectedTool(String userSelectedTool) ;
}
