
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represents a Group of Entities.
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_ENTITY_GROUP"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class EntityGroup extends AbstractMetadata implements java.io.Serializable, EntityGroupInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Short name of the Entity group.
	 */
	protected String shortName;
	
	/**
	 * Long  name of the Entity group.
	 */
	protected String longName;
	
	/**
	 * The Version of the Entity group.
	 */
	protected String version;
	
	/**
	 * Collection of Entity in this Entity group.
	 */
	protected Collection<EntityInterface> entityCollection = new HashSet<EntityInterface>();

	/**
	 * Empty Constructor
	 */
	public EntityGroup()
	{
	}

	/**
	 * This method returns the Collection of the Entities in the group.
	 * @hibernate.set name="entityCollection" table="DYEXTN_ENTITY_GROUP_REL" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ENTITY_GROUP_ID"
	 * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.Entity" column="ENTITY_ID"
	 * @return Returns the Collection of the Entities in the group.
	 */
	public Collection<EntityInterface> getEntityCollection()
	{
		return entityCollection;
	}

	/**
	 * This method sets the entityCollection to the given Collection of the Entities.
	 * @param entityCollection The entityCollection to set.
	 */
	public void setEntityCollection(Collection<EntityInterface> entityCollection)
	{
		this.entityCollection = entityCollection;
	}

	/**
	 * This method returns the long name of the Entity group.
	 * @hibernate.property name="longName" type="string" column="LONG_NAME" 
	 * @return the long name of the Entity group..
	 */
	public String getLongName()
	{
		return longName;
	}

	/**
	 * This method sets the long name of the Entity group to the given name
	 * @param longName the name to be set.
	 */
	public void setLongName(String longName)
	{
		this.longName = longName;
	}

	/**
	 * This method returns the short name of the Entity group.
	 * @hibernate.property name="shortName" type="string" column="SHORT_NAME"  
	 * @return the short name of the Entity group.
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * This method sets the short name of the Entity group to the given name
	 * @param longName the name to be set.
	 */
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	/**
	 * This method returns the version of the Entity group.
	 * @hibernate.property name="version" type="string" column="VERSION" 
	 * @return the version of the Entity group.
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * This method sets the version of the Entity group to the given version
	 * @param version the version to be set.
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

     /**
     * 
     */
    public void addEntity(EntityInterface entityInterface) {
        if (this.entityCollection == null) {
            entityCollection = new HashSet<EntityInterface>();
        }
        entityCollection.add(entityInterface);
        
    }
}