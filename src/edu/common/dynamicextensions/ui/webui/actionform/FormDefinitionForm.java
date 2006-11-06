
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
public class FormDefinitionForm extends AbstractActionForm implements EntityUIBeanInterface, ContainerUIBeanInterface
{
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
	/**
	 * default constructor
	 *
	 */
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
	public int getFormId()
	{
		return Constants.ENTITY_FORM_ID;
	}

	/**
	 * @param abstractDomain abstractDomain
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * @return Returns the description.
	 */
	public String getFormDescription()
	{
		return formDescription;
	}

	/**
	 * @param description The description to set.
	 */
	public void setFormDescription(String description)
	{
		this.formDescription = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getFormName()
	{
		return formName;
	}

	/**
	 * @param formName The name to set.
	 */
	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	/**
	 * @return Returns the entityIdentifier.
	 */
	public String getEntityIdentifier()
	{
		return entityIdentifier;
	}

	/**
	 * @param entityIdentifier The entityIdentifier to set.
	 */
	public void setEntityIdentifier(String entityIdentifier)
	{
		this.entityIdentifier = entityIdentifier;
	}

	/**
	 * gets CreateAs
	 * @return String createAs
	 */
	public String getCreateAs()
	{
		return createAs;
	}

	/**
	 * @param createAs createAs
	 */
	public void setCreateAs(String createAs)
	{
		this.createAs = createAs;
	}

	/**
	 * @return existingFormsList
	 */
	public List getExistingFormsList()
	{
		return existingFormsList;
	}

	/**
	 * @param existingFormsList existingFormsList
	 */
	public void setExistingFormsList(List existingFormsList)
	{
		this.existingFormsList = existingFormsList;
	}

	/**
	 * @return String selectForm
	 */
	public String getSelectForm()
	{
		return selectForm;
	}

	/**
	 * @param selectForm selectForm
	 */
	public void setSelectForm(String selectForm)
	{
		this.selectForm = selectForm;
	}

	/**
	 * @return buttonCss
	 */
	public String getButtonCss()
	{
		return buttonCss;
	}

	/**
	 * @return formCaption
	 */
	public String getFormCaption()
	{
		return formCaption;
	}

	/**
	 * @return mainTableCss
	 */
	public String getMainTableCss()
	{
		return mainTableCss;
	}

	/**
	 * @return  requiredFieldIndicatior
	 */
	public String getRequiredFieldIndicatior()
	{
		return requiredFieldIndicatior;
	}

	/**
	 * @return requiredFieldWarningMessage
	 */
	public String getRequiredFieldWarningMessage()
	{
		return requiredFieldWarningMessage;
	}

	/**
	 * @return titleCss
	 */
	public String getTitleCss()
	{
		return titleCss;
	}

	/**
	 * @param buttonCss buttonCss
	 */
	public void setButtonCss(String buttonCss)
	{
		this.buttonCss = buttonCss;
	}

	/**
	 * @param caption FormCaption
	 */
	public void setFormCaption(String caption)
	{
		this.formCaption = caption;
	}

	/**
	 * @param mainTableCss MainTableCss
	 */
	public void setMainTableCss(String mainTableCss)
	{
		this.mainTableCss = mainTableCss;
	}

	/**
	 * @param requiredFieldIndicatior requiredFieldIndicatior
	 */
	public void setRequiredFieldIndicatior(String requiredFieldIndicatior)
	{
		this.requiredFieldIndicatior = requiredFieldIndicatior;
	}

	/**
	 * @param requiredFieldWarningMessage requiredFieldWarningMessage
	 */
	public void setRequiredFieldWarningMessage(String requiredFieldWarningMessage)
	{
		this.requiredFieldWarningMessage = requiredFieldWarningMessage;
	}

	/** 
	 * @param titleCss titleCss
	 */
	public void setTitleCss(String titleCss)
	{
		this.titleCss = titleCss;
	}



	/**
	 * 
	 */
	public void reset()
	{
		formName = "";
		formDescription = "";
		createAs = "";
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @param mapping ActionMapping mapping
	 * @param request HttpServletRequest request
	 * @return ActionErrors
	 * */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();

		if (formName == null || validator.isEmpty(String.valueOf(formName)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties.getValue("eav.form.name")));
		}

		if (conceptCode == null || validator.isEmpty(String.valueOf(conceptCode)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties.getValue("eav.form.conceptCode")));
		}

		if (createAs == null || validator.isEmpty(String.valueOf(createAs)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties.getValue("eav.form.createAs")));
		}
		return errors;
	}

	/**
	 * @return the conceptCode
	 */
	public String getConceptCode()
	{
		return conceptCode;
	}

	/**
	 * @param conceptCode the conceptCode to set
	 */
	public void setConceptCode(String conceptCode)
	{
		this.conceptCode = conceptCode;
	}

}
