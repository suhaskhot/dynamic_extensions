
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_PERMISSIBLE_VALUE" 
 *
 */
public abstract class PermissibleValue extends AbstractDomainObject implements java.io.Serializable,PermissibleValueInterface {
    
    /**
     * Unique identifier for the object
     */
    protected Long id;
   

    /**
     * 
     */
	public Long getSystemIdentifier() {
		return id;
	}

    /**
     * 
     */
	public void setSystemIdentifier(Long id) {
		this.id = id;
		
	}
	
	public abstract Object getValueAsObject();
	
	/**
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_PERMISSIBLEVAL_SEQ" 
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
}
