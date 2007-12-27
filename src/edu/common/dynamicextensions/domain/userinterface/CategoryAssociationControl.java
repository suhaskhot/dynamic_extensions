package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This Class represents the category.
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ASSOCIATION"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */

public class CategoryAssociationControl extends Control implements CategoryAssociationControlInterface
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Maximum cardinality of this role.
	 */
	protected Integer numberOfEntries;

	/**
	 *
	 */
	protected ContainerInterface container;

	/**
	 *
	 */
	public CategoryAssociationControl()
	{
		super();
	}

	/**
	 * @return container
	 * @hibernate.many-to-one  cascade="save-update" column="CATEGORY_CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
	 */
	public ContainerInterface getContainer()
	{
		return container;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface#setContainer(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setContainer(ContainerInterface container)
	{
		this.container = container;
	}

	@Override
	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractAttributeInterface getAbstractAttribute()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * This method returns the number of entries.
	 * @hibernate.property name="numberOfEntries" type="integer" column="NUMBER_OF_ENTRIES"
	 * @return the maximum cardinality.
	 */

	public Integer getNumberOfEntries()
	{
		return numberOfEntries;
	}


	public void setNumberOfEntries(Integer numberOfEntries)
	{
		this.numberOfEntries = numberOfEntries;
	}

}
