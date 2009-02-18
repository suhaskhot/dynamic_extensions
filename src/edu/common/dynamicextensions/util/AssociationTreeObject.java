
package edu.common.dynamicextensions.util;

import java.util.Collection;
import java.util.HashSet;

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
	 * @param id
	 * @param label
	 */
	public AssociationTreeObject(Long id, String label)
	{
		this.id = id;
		this.label = label;
	}

	/**
	 * 
	 *
	 */
	public AssociationTreeObject()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	Collection associationTreeObjCollection;

	/**
	 * 
	 * @return
	 */
	public Collection getAssociationTreeObjectCollection()
	{
		return associationTreeObjCollection;
	}

	/**
	 * 
	 * @param associationTreeObjCollection
	 */
	public void setAssociationTreeObjectCollection(Collection associationTreeObjCollection)
	{
		this.associationTreeObjCollection = associationTreeObjCollection;
	}

	/**
	 * 
	 * @param associationTreeObject
	 */
	public void addAssociationTreeObject(AssociationTreeObject associationTreeObject)
	{
		if (this.associationTreeObjCollection == null)
		{
			associationTreeObjCollection = new HashSet();
		}
		associationTreeObjCollection.add(associationTreeObject);
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
