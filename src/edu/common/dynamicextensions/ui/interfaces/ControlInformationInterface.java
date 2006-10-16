package edu.common.dynamicextensions.ui.interfaces;
/**
 * @author deepti_shelar
 */
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public interface ControlInformationInterface {
	/**
	 * 
	 */
	public String getName();
	/**
	 * @param attribute The attribute to set.
	 */
	public void setName(String name);
	/**
	 * If user has chosen it to be kept hidden.
	 * @return Returns the isHidden.
	 */
	Boolean getIsHidden();
	/**
	 * @param isHidden The isHidden to set.
	 */
	void setIsHidden(Boolean isHidden);
	/**
	 * The sequence Number for setting it at the desired place in the tree and so in the UI.
	 * @return Returns the sequenceNumber.
	 */
	Integer getSequenceNumber();
	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	void setSequenceNumber(Integer sequenceNumber);
	/**
	 * @return Returns the attribute.
	 *
	 */
	public AbstractAttributeInterface getAbstractAttribute();
	/**
	 * @param attribute The attribute to set.
	 */
	public void setAbstractAttribute(AbstractAttributeInterface attributeInterface);




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
	 *//*
	public String getChoiceList() ;
	*//**
	 * @param choiceList The choiceList to set.
	 *//*
	public void setChoiceList(String choiceList) ;*/

	/**
	 * @return the attributeCssClass
	 */
	public String getCssClass() ;

	/**
	 * @param attributeCssClass the attributeCssClass to set
	 */
	public void setCssClass(String attributeCssClass) ;


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
	 *//*
	public List getToolsList(); 

	*//**
	 * @param toolsList the toolsList to set
	 *//*
	public void setToolsList(List toolsList); 
	*//**
	 * @return the userSelectedTool
	 *//*
	public String getUserSelectedTool();

	*//**
	 * @param userSelectedTool the userSelectedTool to set
	 *//*
	public void setUserSelectedTool(String userSelectedTool) ;*/
	/**
	 * @return Returns the columns.
	 */
	Integer getColumns();
	/**
	 * @param columns The columns to set.
	 */
	void setColumns(Integer columns);
	/**
	 * @return Returns the rows.
	 */
	Integer getRows();
	/**
	 * @param rows The rows to set.
	 */
	void setRows(Integer rows);
	/**
     * @return Returns the isMultiSelect.
     */
    Boolean getIsMultiSelect();
    /**
     * @param isMultiSelect The isMultiSelect to set.
     */
    void setIsMultiSelect(Boolean isMultiSelect) ;
    /**
	 * @return Returns the displayChoiceList.
	 *//*
	public List getDisplayChoiceList(); 
	*//**
	 * @param displayChoiceList The displayChoiceList to set.
	 *//*
	public void setDisplayChoiceList(List displayChoiceList);*/
    /**
     * 
     * @param userSelectedTool
     */
    void setUserSelectedTool(String userSelectedTool);
    /**
     * 
     * @param htmlFile
     */
    void setHtmlFile(String htmlFile);
   /* *//**
	 * @return the noOfLines
	 *//*
	public String getNoOfLines() ;

	*//**
	 * @param noOfLines the noOfLines to set
	 *//*
	public void setNoOfLines(String noOfLines) ;*/
}
