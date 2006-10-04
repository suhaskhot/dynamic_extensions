
package edu.common.dynamicextensions.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 *
 */
public class Concept extends AbstractDomainObject implements java.io.Serializable {
    
    protected static final long serialVersionUID = 1234567890L;

    /**
     * 
     */
    protected Long id;
    /**
     * 
     */
    protected String description;
    /**
     * 
     */
    protected String publicId;
    /**
     * 
     */
    protected String source;
    
    
	/**
     * @hibernate.property name="description" type="string" column="DESCRIPTION" 
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
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_CONCEPT_SEQ"
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
     * @hibernate.property name="publicId" type="string" column="PUBLIC_ID" 
	 * @return Returns the publicId.
	 */
	public String getPublicId() {
		return publicId;
	}
	/**
	 * @param publicId The publicId to set.
	 */
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	/**
     * @hibernate.property name="source" type="string" column="SOURCE" 
	 * @return Returns the source.
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source The source to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}
    
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
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}
