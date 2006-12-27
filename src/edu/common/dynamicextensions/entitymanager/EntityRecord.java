package edu.common.dynamicextensions.entitymanager;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;

/**
 * This class represents a single record for an entity
 * 
 * @author Rahul Ner
 *
 */
public class EntityRecord
{

	/**
	 * map containing values of the attribute and association
	 */
	protected Map<AbstractAttributeInterface, Object> valueMap = new HashMap<AbstractAttributeInterface, Object>();

	/**
	 * 
	 */
	Long recordId;
	

	public EntityRecord(Long id)
	{
		recordId = id;
	}

	/**
	 * returns the value for the given attribute
	 */
	public Object getValue(AbstractAttributeInterface attribute)
	{
		return valueMap.get(attribute);
	}

	/**
	 * sets the value for the given attribbute
	 */
	public void setValue(AbstractAttributeInterface attribute, Object value)
	{
		valueMap.put(attribute, value);
	}

	/**
	 * @return Map
	 */
	public Map<AbstractAttributeInterface, Object> getValueMap()
	{
		return valueMap;
	}

	/**
	 * @return the recordId
	 */
	public Long getRecordId()
	{
		return recordId;
	}
	
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(Long recordId)
	{
		this.recordId = recordId;
	}
	
}
