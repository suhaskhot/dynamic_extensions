package edu.common.dynamicextensions.ui.util; 

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;


/**
 * This Class represents the a single value for multi select attribute.
 * 
 * @author Rahul Ner 
 */
public class CollectionAttributeRecordValue extends DynamicExtensionBaseDomainObject
{

	/**
	 * 
	 */
	protected String value;
	
	
	
	/**
	 * This method returns the name of the AbstractMetadata.
	 * @return the name of the AbstractMetadata.
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

	/**
	 * This method returns the unique identifier of the AbstractMetadata.
	 * @return the identifier of the AbstractMetadata.
	 */
	public Long getId()
	{
		return id;
	}

	
	
}
