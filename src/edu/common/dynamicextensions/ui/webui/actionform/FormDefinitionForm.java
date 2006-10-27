
package edu.common.dynamicextensions.ui.webui.actionform;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.interfaces.ContainerUIBeanInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityUIBeanInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * This actionFotm stores information about the Container and the Entity.
 * @author deepti_shelar
 *
 */
public class FormDefinitionForm  extends AbstractActionForm implements EntityUIBeanInterface,ContainerUIBeanInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name : 
	 */
	protected String formName;

	/**
	 * Description
	 */
	protected String formDescription;
	/**
	 * Entity Identifier
	 */
	protected String entityIdentifier;
	/**
	 * CreateAs
	 */
	protected String createAs;
	
	/**
	 * existingFormsList
	 */
	protected List existingFormsList;
	/**
	 * selectForm;
	 */
	protected String selectForm;
	/**
	 * 
	 */
	protected String buttonCss;
	/**
	 * 
	 */
	protected String formCaption;
	/**
	 * 
	 */
	protected String mainTableCss;
	/**
	 * 
	 */
	protected String requiredFieldIndicatior;
	/**
	 * 
	 */
	protected String requiredFieldWarningMessage;
	/**
	 * 
	 */
	protected String titleCss;
	/**
	 * 
	 */
	protected String conceptCode;
	/**
	 * mode
	 */
	protected String mode;
	
	public FormDefinitionForm()
	{
		createAs = "NewForm";
	}

	/**
	 * @return the mode
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}
    
		/**
	 * Returns the id assigned to form bean.
	 * @return the id assigned to form bean.
	 */
	public int getFormId() {
		return Constants.ENTITY_FORM_ID;
	}
	/**
	 * 
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)   {

	}

	/**
	 * @return Returns the description.
	 */
	public String getFormDescription() {
		return formDescription;
	}
	/**
	 * @param description The description to set.
	 */
	public void setFormDescription(String description) {
		this.formDescription = description;
	}
	/**
	 * @return Returns the name.
	 */
	public String getFormName() {
		return formName;
	}
	/**
	 * @param name The name to set.
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}
	/**
	 * @return Returns the entityIdentifier.
	 */
	public String getEntityIdentifier() {
		return entityIdentifier;
	}
	/**
	 * @param entityIdentifier The entityIdentifier to set.
	 */
	public void setEntityIdentifier(String entityIdentifier) {
		this.entityIdentifier = entityIdentifier;
	}
	/**
	 * gets CreateAs
	 */
	public String getCreateAs() {
		return createAs;
	}
	/**
	 * sets CreateAs
	 */
	public void setCreateAs(String createAs) {
		this.createAs = createAs;
	}
	/**
	 * 
	 */
	public List getExistingFormsList() {
		return existingFormsList;
	}
	/**
	 * 
	 */
	public void setExistingFormsList(List existingFormsList) {
		this.existingFormsList = existingFormsList;
	}
	/**
	 * 
	 */
	public String getSelectForm() {
		return selectForm;
	}
	/**
	 * 
	 */
	public void setSelectForm(String selectForm) {
		this.selectForm = selectForm;
	}
	/**
	 * 
	 */
	public String getButtonCss() {
		return buttonCss;
	}
	/**
	 * 
	 */
	public String getFormCaption() {
		return formCaption;
	}
	/**
	 * 
	 */
	public String getMainTableCss() {
		return mainTableCss;
	}
	/**
	 * 
	 */
	public String getRequiredFieldIndicatior() {
		return requiredFieldIndicatior;
	}
	/**
	 * 
	 */
	public String getRequiredFieldWarningMessage() {
		return requiredFieldWarningMessage;
	}
	/**
	 * 
	 */
	public String getTitleCss() {
		return titleCss;
	}
	/**
	 * 
	 */
	public void setButtonCss(String buttonCss) {
		this.buttonCss = buttonCss;
	}
	/**
	 * 
	 */
	public void setFormCaption(String caption) {
		this.formCaption = caption;
	}
	/**
	 * 
	 */
	public void setMainTableCss(String mainTableCss) {
		this.mainTableCss = mainTableCss;
	}
	/**
	 * 
	 */
	public void setRequiredFieldIndicatior(String requiredFieldIndicatior) {
	this.requiredFieldIndicatior = requiredFieldIndicatior;	
	}
	/**
	 * 
	 */
	public void setRequiredFieldWarningMessage(String requiredFieldWarningMessage) {
		this.requiredFieldWarningMessage = requiredFieldWarningMessage;
	}
	/**
	 * 
	 */
	public void setTitleCss(String titleCss) {
		this.titleCss = titleCss;
	}
	/**
	 * 
	 */
	public void update(FormDefinitionForm cacheForm) {
		this.formName = cacheForm.getFormName();
		this.formDescription = cacheForm.getFormDescription();
		this.formCaption = cacheForm.getFormCaption();
		this.createAs = cacheForm.getCreateAs();
	}
	/**
	 * 
	 */
	public void reset() {
		formName = "";
		formDescription = "";
		createAs = "";
	}  
	/**
	 * Overrides the validate method of ActionForm.
	 * */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();

		if ( formName == null || validator.isEmpty(String.valueOf(formName))) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.item.required", ApplicationProperties
					.getValue("eav.form.name")));
		}
		
		if (conceptCode == null || validator.isEmpty(String.valueOf(conceptCode))) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.item.required", ApplicationProperties
					.getValue("eav.form.conceptCode")));
		}
		
		if (createAs == null || validator.isEmpty(String.valueOf(createAs))) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.item.required", ApplicationProperties
					.getValue("eav.form.createAs")));
		}
	return errors;
	}
	/**
	 * @return the conceptCode
	 */
	public String getConceptCode() {
		return conceptCode;
	}
	/**
	 * @param conceptCode the conceptCode to set
	 */
	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}
	
}
