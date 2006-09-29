package edu.common.dynamicextensions.ui.webui.actionform;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * @author sujay_narkar
 *
 */
public class ControlsForm extends AbstractActionForm {
	/**
	 * Attribute Name
	 */
	String attributeName;
	/**
	 * 
	 */
	String attributeDescription;
	
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
	String attributeFormat;
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
	 * 
	 */
	String attributeDecimalPlaces;
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
	List toolsList = new ArrayList();
	/**
	 * selectedTool
	 */
	String selectedTool;
	
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
	public String getAttributeDescription() {
		return attributeDescription;
	}
	/**
	 * @param attributeDescription The attributeDescription to set.
	 */
	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}
	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * @param attributeName The attributeName to set.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
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
	public String getAttributeFormat() {
		return attributeFormat;
	}
	/**
	 * @param attributeFormat The attributeFormat to set.
	 */
	public void setAttributeFormat(String attributeFormat) {
		this.attributeFormat = attributeFormat;
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
	 * Overrides the validate method of ActionForm.
	 * */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		if (attributeSize == null) {
			
		} else if ( !validator.isNumeric(attributeSize)) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"eav.validation.numeric", ApplicationProperties
					.getValue("eav.entity.name")));
		} else {
			    Integer sizeInteger = new Integer(attributeSize);
                if(sizeInteger.intValue() > 38){
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "eav.validation.numericlarge", ApplicationProperties
                            .getValue("eav.entity.name")));
                    
                }
        }
		
		if (attributeName == null) {
			
		} else if ( validator.isEmpty(attributeName)) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.item.required", ApplicationProperties
					.getValue("eav.attribute.name")));
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
        }
		
		return errors;
	}

	public List getToolsList() {
		return toolsList;
	}

	public void setToolsList(List toolsList) {
		this.toolsList = toolsList;
	}
	public String getSelectedTool() {
		return selectedTool;
	}

	public void setSelectedTool(String selectedTool) {
		this.selectedTool = selectedTool;
	}

}

