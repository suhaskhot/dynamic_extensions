
package edu.common.dynamicextensions.util;

import java.util.Collection;
import java.util.HashSet;

public class AssociationTreeObject
{

	/** The identifier. */
	private Long identifier;

	/** The label. */
	private String label;

	/** The association tree obj collection. */
	private Collection<AssociationTreeObject> associationTreeObjCollection;

	/**
	 * The Constructor.
	 * @param identifier the identifier
	 * @param label the label
	 */
	public AssociationTreeObject(Long identifier, String label)
	{
		setId(identifier);
		setLabel(label);
	}

	/**
	 * Gets the association tree object collection.
	 * @return the association tree object collection
	 */
	public Collection<AssociationTreeObject> getAssociationTreeObjectCollection()
	{
		return associationTreeObjCollection;
	}

	/**
	 * Sets the association tree object collection.
	 * @param associationTreeObjCollection the association tree obj collection
	 */
	public void setAssociationTreeObjectCollection(Collection<AssociationTreeObject> associationTreeObjCollection)
	{
		this.associationTreeObjCollection = associationTreeObjCollection;
	}

	/**
	 * Adds the association tree object.
	 * @param associationTreeObject the association tree object
	 */
	public void addAssociationTreeObject(AssociationTreeObject associationTreeObject)
	{
		if (associationTreeObjCollection == null)
		{
			associationTreeObjCollection = new HashSet<AssociationTreeObject>();
		}
		associationTreeObjCollection.add(associationTreeObject);
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public Long getId()
	{
		return identifier;
	}

	/**
	 * Sets the id.
	 * @param identifier the identifier
	 */
	public void setId(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Gets the label.
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * Sets the label.
	 * @param label the label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

}
