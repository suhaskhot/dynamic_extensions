
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_ABSTRACT_CONTAINMENT_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public abstract class AbstractContainmentControl extends Control implements AbstractContainmentControlInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	protected ContainerInterface container;

	/**
	 * @return container
	 * @hibernate.many-to-one cascade="save-update" column="CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
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

}