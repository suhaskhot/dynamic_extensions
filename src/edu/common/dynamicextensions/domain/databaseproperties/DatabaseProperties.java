package edu.common.dynamicextensions.domain.databaseproperties;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.databaseproperties.DatabasePropertiesInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_DATABASE_PROPERTIES"
 */
public  abstract class DatabaseProperties extends AbstractDomainObject implements  Serializable,DatabasePropertiesInterface{
    /**
     * Unique identifier for the object
     */
	protected Long id;
	/**
	 * Name of the database property.
	 */
	protected String name;
    
    /**
     * 
     *
     */
	public DatabaseProperties(){

	}
	 /**
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_DATABASE_PROPERTIES_SEQ"
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
}