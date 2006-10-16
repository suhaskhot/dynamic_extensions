package edu.common.dynamicextensions.ui.webui.actionform;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeInformationInterface;
import edu.common.dynamicextensions.ui.interfaces.ControlInformationInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This represents a ActionForm for BuildForm.jsp
 * @author deepti_shelar
 *
 */
public class ControlsForm extends AbstractActionForm implements 
ControlInformationInterface,AbstractAttributeInformationInterface{
	/**
	 * Attribute Name
	 */
	AbstractAttributeInterface  abstractAttribute;
	/**
	 * 
	 */
	String description;
	
	/**
	 * 
	 */
	String dataType;

	/**
	 * 
	 */
	List dataTypeList;

	/**
	 * 
	 */
	String attributeSize;

	/**
	 * 
	 */   
	String attributeDefaultValue;

	/**
	 * 
	 */    
	String format;
	/**
	 * 
	 */
	String attributeValidationRules;

	/**
	 * 
	 */
	String attributeDisplayUnits;

	/**
	 * 
	 */
	String referenceValues;
	/**
	 * 
	 */
	List displayChoiceList;
	/**
	 * 
	 */
	String displayChoice;
	/**
	 *  number of decimal places
	 */
	String attributeDecimalPlaces;
	/**
	 * Number of digits in number
	 */
	String attributeDigits;
	/**
	 * 
	 */
	String choiceList;
	/**
	 * 
	 */
	String htmlFile;
	/**
	 * Data type changed
	 */
	String dataTypeChanged;
	/**
	 * Attribute identifier
	 */
	String attributeIdentifier;
	/**
	 * 
	 */
	String caption;
    /**
     * 
     */
	String attributeIsPassword;
    /**
     * 
     */
	Boolean isPassword;
    /**
     * 
     */
	List toolsList = new ArrayList();
    /**
     * 
     */
	Boolean IsHidden;
    /**
     * 
     */
	Integer sequenceNumber;
    /**
     * 
     */
	protected String cssClass;
    /**
     * 
     */
	protected String name;
    /**
     * 
     */
	protected String tooltip;
    /**
     * 
     */
	protected String attributeNoOfRows ;
    /**
     * 
     */
	protected String attributenoOfCols;
    /**
     * 
     */
	protected String attributeMultiSelect;
    /**
     * 
     */
	protected String attributeSequenceNumber;
    /**
     * 
     */
	protected String attributeMeasurementUnits;
    /**
     * 
     */
	protected String attributeScale;
    /**
     * 
     */
	protected String userSelectedTool;
    /**
     * 
     */
	protected Integer columns;
    /**
     * 
     */
	protected Integer rows;
    /**
     * 
     */
	protected Boolean isMultiSelect;
    
    /**
     * 
     */
    protected String controlOperation;
    /**
     * 
     */
    protected String selectedControlId;
    /**
     * 
     */
    
    protected String rootName;
    
    /**
     * 
     */
    protected List childList;
    /**
     * 
     */
    protected String toolBoxClicked;
    /**
     * 
     */
    protected String showPreview;
   

	/**
     * 
	 */
	public void reset() {

	}

	/**
	 * Returns the id assigned to form bean.
	 * @return the id assigned to form bean.
	 */
	public int getFormId() {
		return Constants.ATTRIBUTE_FORM_ID;
	}
	/**
	 * 
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)   {

	}


	/**
	 * @return Returns the attributeDescription.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param attributeDescription The attributeDescription to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return Returns the dataTypeList.
	 */
	public List getDataTypeList() {
		return dataTypeList;
	}
	/**
	 * @param dataTypeList The dataTypeList to set.
	 */
	public void setDataTypeList(List dataTypeList) {
		this.dataTypeList = dataTypeList;
	}
	/**
	 * @return Returns the dataType.
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return Returns the dataTypeChanged.
	 */
	public String getDataTypeChanged() {
		return dataTypeChanged;
	}
	/**
	 * @param dataTypeChanged The dataTypeChanged to set.
	 */
	public void setDataTypeChanged(String dataTypeChanged) {
		this.dataTypeChanged = dataTypeChanged;
	}
	/**
	 * @return Returns the htmlFile.
	 */
	public String getHtmlFile() {
		return htmlFile;
	}
	/**
	 * @param htmlFile The htmlFile to set.
	 */
	public void setHtmlFile(String htmlFile) {
		this.htmlFile = htmlFile;
	}
	/**
	 * @return Returns the attributeSize.
	 */
	public String getAttributeSize() {
		return attributeSize;
	}
	/**
	 * @param attributeSize The attributeSize to set.
	 */
	public void setAttributeSize(String attributeSize) {
		this.attributeSize = attributeSize;
	}
	/**
	 * @return Returns the choiceList.
	 */
	public String getChoiceList() {
		return choiceList;
	}
	/**
	 * @param choiceList The choiceList to set.
	 */
	public void setChoiceList(String choiceList) {
		this.choiceList = choiceList;
	}
	/**
	 * @return Returns the attributeDefaultValue.
	 */
	public String getAttributeDefaultValue() {
		return attributeDefaultValue;
	}
	/**
	 * @param attributeDefaultValue The attributeDefaultValue to set.
	 */
	public void setAttributeDefaultValue(String attributeDefaultValue) {
		this.attributeDefaultValue = attributeDefaultValue;
	}
	/**
	 * @return Returns the attributeDisplayUnits.
	 */
	public String getAttributeDisplayUnits() {
		return attributeDisplayUnits;
	}
	/**
	 * @param attributeDisplayUnits The attributeDisplayUnits to set.
	 */
	public void setAttributeDisplayUnits(String attributeDisplayUnits) {
		this.attributeDisplayUnits = attributeDisplayUnits;
	}
	/**
	 * @return Returns the attributeFormat.
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param attributeFormat The attributeFormat to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return Returns the attributeValidationRules.
	 */
	public String getAttributeValidationRules() {
		return attributeValidationRules;
	}
	/**
	 * @param attributeValidationRules The attributeValidationRules to set.
	 */
	public void setAttributeValidationRules(String attributeValidationRules) {
		this.attributeValidationRules = attributeValidationRules;
	}
	/**
	 * @return Returns the attributeIdentifier.
	 */
	public String getAttributeIdentifier() {
		return attributeIdentifier;
	}
	/**
	 * @param attributeIdentifier The attributeIdentifier to set.
	 */
	public void setAttributeIdentifier(String attributeIdentifier) {
		this.attributeIdentifier = attributeIdentifier;
	}
	/**
	 * @return Returns the displayChoiceList.
	 */
	public List getDisplayChoiceList() {
		return displayChoiceList;
	}
	/**
	 * @param displayChoiceList The displayChoiceList to set.
	 */
	public void setDisplayChoiceList(List displayChoiceList) {
		this.displayChoiceList = displayChoiceList;
	}
	/**
	 * @return Returns the attributeDecimalPlaces.
	 */
	public String getAttributeDecimalPlaces() {
		return attributeDecimalPlaces;
	}
	/**
	 * @param attributeDecimalPlaces The attributeDecimalPlaces to set.
	 */
	public void setAttributeDecimalPlaces(String attributeDecimalPlaces) {
		this.attributeDecimalPlaces = attributeDecimalPlaces;
	}
	/**
	 * @return Returns the referenceValues.
	 */
	public String getReferenceValues() {
		return referenceValues;
	}
	/**
	 * @param referenceValues The referenceValues to set.
	 */
	public void setReferenceValues(String referenceValues) {
		this.referenceValues = referenceValues;
	}
	/**
	 * @return Returns the displayChoice.
	 */
	public String getDisplayChoice() {
		return displayChoice;
	}
	/**
	 * @param displayChoice The displayChoice to set.
	 */
	public void setDisplayChoice(String displayChoice) {
		this.displayChoice = displayChoice;
	}



	/**
	 * @return the attributeCssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the attributeMeasurementUnits
	 */
	public String getAttributeMeasurementUnits() {
		return attributeMeasurementUnits;
	}

	/**
	 * @param attributeMeasurementUnits the attributeMeasurementUnits to set
	 */
	public void setAttributeMeasurementUnits(String attributeMeasurementUnits) {
		this.attributeMeasurementUnits = attributeMeasurementUnits;
	}

	/**
	 * @return the attributeMultiSelect
	 */
	public String getAttributeMultiSelect() {
		return attributeMultiSelect;
	}

	/**
	 * @param attributeMultiSelect the attributeMultiSelect to set
	 */
	public void setAttributeMultiSelect(String attributeMultiSelect) {
		this.attributeMultiSelect = attributeMultiSelect;
		isMultiSelect = new Boolean(attributeMultiSelect);
	}

	/**
	 * @return the attributenoOfCols
	 */
	public String getAttributenoOfCols() {
		return attributenoOfCols;
	}

	/**
	 * @param attributenoOfCols the attributenoOfCols to set
	 */
	public void setAttributenoOfCols(String attributenoOfCols) {
		this.attributenoOfCols = attributenoOfCols;
		try
		{
			columns = new Integer(attributenoOfCols);
		}
		catch (NumberFormatException e)
		{
			columns = new Integer(0);
		}
	}

	/**
	 * @return the attributeNoOfRows
	 */
	public String getAttributeNoOfRows() {
		return attributeNoOfRows;
	}

	/**
	 * @param attributeNoOfRows the attributeNoOfRows to set
	 */
	public void setAttributeNoOfRows(String attributeNoOfRows) {
		this.attributeNoOfRows = attributeNoOfRows;
		try
		{
			rows  = new Integer(attributeNoOfRows);
		}
		catch (NumberFormatException e)
		{
			rows = new Integer(0);
		}
		
	}

	/**
	 * @return the attributeScale
	 */
	public String getAttributeScale() {
		return attributeScale;
	}

	/**
	 * @param attributeScale the attributeScale to set
	 */
	public void setAttributeScale(String attributeScale) {
		this.attributeScale = attributeScale;
	}



	



	/**
	 * @return the toolsList
	 */
	public List getToolsList() {
		return toolsList;
	}

	/**
	 * @param toolsList the toolsList to set
	 */
	public void setToolsList(List toolsList) {
		this.toolsList = toolsList;
	}
	/**
	 * @return the userSelectedTool
	 */
	public String getUserSelectedTool() {
		return userSelectedTool;
	}

	/**
	 * @param userSelectedTool the userSelectedTool to set
	 */
	public void setUserSelectedTool(String userSelectedTool) {
		this.userSelectedTool = userSelectedTool;
	}

	public void update(ControlsForm cacheForm) {
		/*this.attributeCssClass = cacheForm.getActivityStatus();
		this.attributeDefaultValue = cacheForm.getAttributeDefaultValue();
		this.attributeDescription = cacheForm.getAttributeDescription();
		this.attributeDisplayUnits = cacheForm.getAttributeDisplayUnits();
		this.attributeFormat = cacheForm.getAttributeFormat();
		this.attributeIdentifier = cacheForm.getAttributeIdentifier();
		this.attributeMultiSelect = cacheForm.getAttributeMultiSelect();
		this.attributeMeasurementUnits = cacheForm.getAttributeMeasurementUnits();
		this.attributeName = cacheForm.getAttributeName();
		this.attributenoOfCols = cacheForm.getAttributenoOfCols();
		this.attributeNoOfRows = cacheForm.getAttributeNoOfRows();
		this.attributeScale = cacheForm.getAttributeScale();
		this.attributeSize = cacheForm.getAttributeSize();
		this.attributeTooltip = cacheForm.getAttributeTooltip();
		this.attributeValidationRules = cacheForm.getAttributeValidationRules();*/
		this.userSelectedTool = cacheForm.getUserSelectedTool();
		this.dataType = cacheForm.getDataType();
		this.displayChoice = cacheForm.getDisplayChoice();
	}
	/**
	 * Overrides the validate method of ActionForm.
	 * */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		/*	Validator validator = new Validator();
		if (attributeSize == null) {

		} else if ( !validator.isNumeric(attributeSize)) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"eav.validation.numeric", ApplicationProperties
					.getValue("eav.form.name")));
		} else {
			    Integer sizeInteger = new Integer(attributeSize);
                if(sizeInteger.intValue() > 38){
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "eav.validation.numericlarge", ApplicationProperties
                            .getValue("eav.form.name")));

                }
        }

		if (attributeName == null) {

		} else if ( validator.isEmpty(attributeName)) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.item.required", ApplicationProperties
					.getValue("eav.control.name")));
		}


		if(referenceValues == null) {

        }else if ( validator.isEmpty(referenceValues)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.item.required", ApplicationProperties
                    .getValue("eav.attribute.EnterChoice")));
        }


        if(attributeDecimalPlaces == null) {

        } else if ( validator.isEmpty(attributeDecimalPlaces)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.item.required", ApplicationProperties
                    .getValue("eav.attribute.DecimalPlaces")));
        }else if ( !validator.isNumeric(attributeDecimalPlaces)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "eav.validation.decimalnumeric", ApplicationProperties
                    .getValue("eav.entity.name")));
        }*/

		return errors;
	}



	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Boolean getIsPassword()
	{
		return this.isPassword;
	}

	public void setIsPassword(Boolean isPassword)
	{
		this.isPassword = isPassword;
	}



	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public AbstractAttributeInterface getAbstractAttribute() {
		return abstractAttribute;
	}

	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface) {
		abstractAttribute = abstractAttributeInterface;
	}

	public Integer getColumns() {
		return columns;
	}

	public Boolean getIsHidden() {
		return IsHidden;
	}

	public String getName() {
		return name;
	}

	public Integer getRows() {
		return rows;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setColumns(Integer columns) {
		this.columns = columns; 
		if(columns!=null)
		{
			this.attributenoOfCols = columns.toString();
		}

	}

	public void setIsHidden(Boolean isHidden) {
		this.IsHidden = isHidden;
	}

	public void setName(String name) {
		this.name = name;		
	}

	public void setRows(Integer rows) {
		this.rows = rows;	
		if(rows!=null)
		{
			this.attributeNoOfRows = rows.toString();
		}
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;		
	}
	/**
     * @return Returns the isMultiSelect.
     */
	public Boolean getIsMultiSelect() {
    	return isMultiSelect;
    }
    /**
     * @param isMultiSelect The isMultiSelect to set.
     */
    public void setIsMultiSelect(Boolean isMultiSelect) {
    	this.isMultiSelect = isMultiSelect;
    	if(isMultiSelect!=null)
    	{
    		this.attributeMultiSelect = isMultiSelect.toString();
    	}
    }

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.ui.interfaces.AbstractAttributeInformationInterface#getAttributeDigits()
	 */
	public String getAttributeDigits()
	{
		return attributeDigits;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.ui.interfaces.AbstractAttributeInformationInterface#setAttributeDigits(java.lang.String)
	 */
	public void setAttributeDigits(String attribDigits)
	{
		attributeDigits = attribDigits;
	}

	public String getAttributeSequenceNumber()
	{
		return this.attributeSequenceNumber;
	}

	public void setAttributeSequenceNumber(String attributeSequenceNumber)
	{
		this.attributeSequenceNumber = attributeSequenceNumber;
		try
		{
			sequenceNumber = new Integer(attributeSequenceNumber);
		}
		catch (NumberFormatException e)
		{
			sequenceNumber	=	new Integer(0);
		}
	}

	public String getAttributeIsPassword()
	{
		return this.attributeIsPassword;
	}

	public void setAttributeIsPassword(String attributeIsPassword)
	{
		this.attributeIsPassword = attributeIsPassword;
		isPassword = new Boolean(attributeIsPassword);
	}
	/**
	 * @return Returns the controlOperation.
	 */
	public String getControlOperation() {
		return controlOperation;
	}
	/**
	 * @param controlOperation The controlOperation to set.
	 */
	public void setControlOperation(String controlOperation) {
		this.controlOperation = controlOperation;
	}
	/**
	 * @return Returns the selectedControlId.
	 */
	public String getSelectedControlId() {
		return selectedControlId;
	}
	/**
	 * @param selectedControlId The selectedControlId to set.
	 */
	public void setSelectedControlId(String selectedControlId) {
		this.selectedControlId = selectedControlId;
	}
	/**
	 * @return Returns the childList.
	 */
	public List getChildList() {
		return childList;
	}
	/**
	 * @param childList The childList to set.
	 */
	public void setChildList(List childList) {
		this.childList = childList;
	}
	/**
	 * @return Returns the rootName.
	 */
	public String getRootName() {
		return rootName;
	}
	/**
	 * @param rootName The rootName to set.
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	/**
	 * @return Returns the toolBoxClicked.
	 */
	public String getToolBoxClicked() {
		return toolBoxClicked;
	}
	/**
	 * @param toolBoxClicked The toolBoxClicked to set.
	 */
	public void setToolBoxClicked(String toolBoxClicked) {
		this.toolBoxClicked = toolBoxClicked;
	}
	/**
	 * @return Returns the showPreview.
	 */
	public String getShowPreview() {
		return showPreview;
	}
	/**
	 * @param showPreview The showPreview to set.
	 */
	public void setShowPreview(String showPreview) {
		this.showPreview = showPreview;
	}
}

