package edu.common.dynamicextensions.entitymanager;

import java.util.List;

/**
 * This class represents a collection of all the records present for the given entity.
 * @author Rahul Ner
 * @author Vishvesh Mulay
 *
 */
public class EntityRecordResult implements EntityRecordResultInterface
{

	/**
	 * List of entity records
	 */
	List<EntityRecordInterface> entityRecordList;

	/**
	 * Metadata for the result
	 */
	EntityRecordMetadata entityRecordMetadata;

	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface#getEntityRecordList()
	 */
	public List<EntityRecordInterface> getEntityRecordList()
	{
		return entityRecordList;
	}

	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface#setEntityRecordList(java.util.List)
	 */
	public void setEntityRecordList(List<EntityRecordInterface> entityRecordList)
	{
		this.entityRecordList = entityRecordList;
	}

	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface#getEntityRecordMetadata()
	 */
	public EntityRecordMetadata getEntityRecordMetadata()
	{
		return entityRecordMetadata;
	}

	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface#setEntityRecordMetadata(edu.common.dynamicextensions.entitymanager.EntityRecordMetadata)
	 */
	public void setEntityRecordMetadata(EntityRecordMetadata entityRecordMetadata)
	{
		this.entityRecordMetadata = entityRecordMetadata;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append(entityRecordMetadata.getAttributeList());
		str.append(entityRecordList);
		return str.toString();
	}
}
