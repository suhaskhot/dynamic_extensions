
package edu.common.dynamicextensions.domain;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represents the a single record for multi select attribute.
 * 
 * @author Rahul Ner 
 * @hibernate.class  table="DYEXTN_COLLECTION_ATTR_RECORD"
 */
public class CollectionAttributeRecord extends AbstractDomainObject implements Serializable
{

	/**
	 * Empty Constructor.
	 */
	public CollectionAttributeRecord()
	{
	}
	
	/**
	 * Serial Version Unique Identifier
	 */
	protected static final long serialVersionUID = 1234567890L;

	/**
	 * Internally generated identifier.
	 */
	protected Long id;

	/**
	 * Entity to which this collectionRecord belongs
	 */
	protected EntityInterface entity;

	/**
	 * Attribute to which this collectionRecord belongs
	 */
	protected AttributeInterface attribute;

	/**
	 * a record Id to which this collectionRecord belongs
	 */
	protected Long recordId;

	/**
	 * value of this collectionRecord.
	 */
	protected String value;

	/**
	 * This method returns the unique identifier of the AbstractMetadata.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_COLLECTION_ATTR_REC_SEQ"
	 * @return the identifier of the AbstractMetadata.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method sets the unique identifier of the AbstractMetadata.
	 * @param id The identifier to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * This method returns the Entity associated with this collectionRecord.
	 * @hibernate.many-to-one column="ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity" constrained="true"
	 * @return EntityInterface the Entity associated with the collectionRecord.
	 */
	public EntityInterface getEntity()
	{
		return entity;
	}

	/**
	 * This method sets the Entity associated with this collectionRecord.
	 * @param entityInterface The entity to be set.
	 */
	public void setEntity(EntityInterface entityInterface)
	{
		if (entityInterface != null)
		{
			this.entity = (Entity) entityInterface;
		}
	}

	/**
	 * This method returns the Attribute associated with this collectionRecord.
	 * @hibernate.many-to-one column="ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.Attribute" constrained="true"
	 * @return AttributeInterface the Attribut associated with the collectionRecord.
	 */
	public AttributeInterface getAttribute()
	{
		return attribute;
	}

	/**
	 * @param attribute The attribute to set.
	 */
	public void setAttribute(AttributeInterface attribute)
	{
		this.attribute = attribute;
	}

	/**
	 * This method returns the record associated with this collectionRecord.
	 * @hibernate.property column="RECORD_ID" class="long" constrained="true"
	 * @return AttributeInterface the Attribut associated with the collectionRecord.
	 */
	public Long getRecordId()
	{
		return recordId;
	}

	/**
	 * @param recordId The recordId to set.
	 */
	public void setRecordId(Long recordId)
	{
		this.recordId = recordId;
	}

	/**
	 * This method returns the value of the collectionRecord.
	 * @hibernate.property name="value" type="string" column="RECORD_VALUE" length="4000"
	 * @return the value of the collectionRecord.
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}
}
