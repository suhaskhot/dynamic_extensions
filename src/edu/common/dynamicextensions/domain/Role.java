package edu.common.dynamicextensions.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class Role extends AbstractDomainObject implements Serializable{

	protected String associationType;
	protected Integer maxCardinality;
	protected Integer minCardinality;
	protected String name;

	public Role(){

	}

	public void finalize() throws Throwable {

	}

	
    /**
     * @return Returns the associationType.
     */
    public String getAssociationType() {
        return associationType;
    }
    /**
     * @param associationType The associationType to set.
     */
    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }
    /**
     * @return Returns the maxCardinality.
     */
    public Integer getMaxCardinality() {
        return maxCardinality;
    }
    /**
     * @param maxCardinality The maxCardinality to set.
     */
    public void setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
    }
    /**
     * @return Returns the minCardinality.
     */
    public Integer getMinCardinality() {
        return minCardinality;
    }
    /**
     * @param minCardinality The minCardinality to set.
     */
    public void setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
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