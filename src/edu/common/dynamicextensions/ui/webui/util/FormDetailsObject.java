package edu.common.dynamicextensions.ui.webui.util;

import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;

/**
 * 
 * @author deepti_shelar
 */
public class FormDetailsObject {
	private FormDefinitionForm formDefinitionForm = null;
	public FormDetailsObject() {

	}

	public void updateFormDefinitionForm(FormDefinitionForm formDefinitionForm) {
		this.formDefinitionForm = formDefinitionForm;
	}

	public FormDefinitionForm getFormDefinitionForm() {
		return formDefinitionForm;
	}

	public void setFormDefinitionForm(FormDefinitionForm formDefinitionForm) {
		this.formDefinitionForm = formDefinitionForm;
	}
	
	
	
	
}
