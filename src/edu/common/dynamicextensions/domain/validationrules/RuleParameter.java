package edu.common.dynamicextensions.domain.validationrules;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class RuleParameter extends AbstractDomainObject implements Serializable {

	protected Long id;
	protected String name;
	protected String value;

	public RuleParameter(){

	}

	public void finalize() throws Throwable {

	}
	
	

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#getSystemIdentifier()
     */
    public Long getSystemIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
     */
    public void setSystemIdentifier(Long arg0) {
        // TODO Auto-generated method stub
        
    }
}