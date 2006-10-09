package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_ROLE"
 */
public class Role extends AbstractDomainObject implements RoleInterface{
    
    /**
     * Unique identifier for the object
     */
    protected Long id;
	/**
     * The association type : containment or linking
	 */
	protected String associationType;
    /**
     *  Maximum cardinality of this role.
     */
	protected Integer maxCardinality;
    /**
     * Minimum cardinality of this role.
     */
	protected Integer minCardinality;
    /**
     * Name of the role.
     */
	protected String name;
    /**
     * Empty constructor.
     */
	public Role(){

	}
	
	/**
     * @return
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_ROLE_SEQ"
     */
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @hibernate.property name="associationType" type="string" column="ASSOCIATION_TYPE"
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
     * @hibernate.property name="maxCardinality" type="integer" column="MAX_CARDINALITY" 
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
     * @hibernate.property name="minCardinality" type="integer" column="MIN_CARDINALITY" 
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
     * @hibernate.property name="name" type="string" column="NAME" 
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