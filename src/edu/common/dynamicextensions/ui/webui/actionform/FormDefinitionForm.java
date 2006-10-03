
package edu.common.dynamicextensions.ui.webui.actionform;


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
public class FormDefinitionForm  extends AbstractActionForm {
    /**
     * Name
     */
    protected String formName;
    
    /**
     * Description
     */
    protected String description;
    /**
     * Entity Identifier
     */
    protected String entityIdentifier;
    /**
     * CreateAs
     */
    protected String createAs;
    /**
     * createAsTypeChanged
     */
    protected String createAsTypeChanged;
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
     public void reset() {
    	 formName = "";
    	 description = "";
    	 createAs = "";
    	 existingFormsList.clear();        
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
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
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
	public String getCreateAs() {
		return createAs;
	}

	public void setCreateAs(String createAs) {
		this.createAs = createAs;
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
        if ( createAs == null || validator.isEmpty(String.valueOf(createAs))) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.item.required", ApplicationProperties
                            .getValue("eav.form.createAs")));
        }
        
        return errors;
    }

	public List getExistingFormsList() {
		return existingFormsList;
	}

	public void setExistingFormsList(List existingFormsList) {
		this.existingFormsList = existingFormsList;
	}

	public String getSelectForm() {
		return selectForm;
	}

	public void setSelectForm(String selectForm) {
		this.selectForm = selectForm;
	}

	public String getCreateAsTypeChanged() {
		return createAsTypeChanged;
	}

	public void setCreateAsTypeChanged(String createAsTypeChanged) {
		this.createAsTypeChanged = createAsTypeChanged;
	}

	


}
