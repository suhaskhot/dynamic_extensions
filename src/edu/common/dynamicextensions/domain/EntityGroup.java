package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_ENTITY_GROUP"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class EntityGroup extends AbstractMetadata  implements java.io.Serializable,EntityGroupInterface {

    private static final long serialVersionUID = 1234567890L;
    /**
     * 
     */
    protected String shortName;
    /**
     * 
     */
    protected String longName;
    /**
     * 
     */
    protected String version;
    /**
     * Collection of entity in this entity group.
     */
	protected Collection entityCollection;
     
    /**
     * 
     *
     */
	public EntityGroup(){

	}
	
    /**
     * @hibernate.set name="entityCollection" table="DYEXTN_ENTITY_GROUP_REL" 
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="ENTITY_GROUP_ID"
     * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.Entity" column="ENTITY_ID"
     * @return Returns the entityCollection.
     */
    public Collection getEntityCollection() {
        return entityCollection;
    }
    /**
     * @param entityCollection The entityCollection to set.
     */
    public void setEntityCollection(Collection entityCollection) {
        this.entityCollection = entityCollection;
    }
    
  /**
   * 
   */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
	/**
     * @hibernate.property name="longName" type="string" column="LONG_NAME" 
	 * @return Returns the longName.
	 */
	public String getLongName() {
		return longName;
	}
	/**
	 * @param longName The longName to set.
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}
	/**
     * @hibernate.property name="shortName" type="string" column="SHORT_NAME"  
	 * @return Returns the shortName.
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName The shortName to set.
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
     * @hibernate.property name="version" type="string" column="VERSION" 
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}