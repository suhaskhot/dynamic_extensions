package edu.common.dynamicextensions.util;

import java.util.Collection;


public class AssociationTreeObject
{
	/**
	 * 
	 */
	Long id;
	
	/**
	 * 
	 */
	
	String label;
	/**
	 * 
	 */
	Collection associationTreeObjectCollection;
	/**
	 * 
	 * @return
	 */
	public Collection getAssociationTreeObjectCollection()
	{
		return associationTreeObjectCollection;
	}
	
	/**
	 * 
	 * @param associationTreeObjectCollection
	 */
	public void setAssociationTreeObjectCollection(Collection associationTreeObjectCollection)
	{
		this.associationTreeObjectCollection = associationTreeObjectCollection;
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel()
	{
		return label;
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	} 

}
