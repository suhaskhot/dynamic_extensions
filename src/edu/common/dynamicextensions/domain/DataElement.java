
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_DATA_ELEMENT" 
 *
 */
public abstract class DataElement extends AbstractDomainObject implements java.io.Serializable,DataElementInterface {
    
    protected static final long serialVersionUID = 1234567890L;
    /**
     * 
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
    public void setSystemIdentifier(Long systemIdentifier) {
        this.id = systemIdentifier; 
        
    }

	/**
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_DATA_ELEMENT_SEQ" 
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
