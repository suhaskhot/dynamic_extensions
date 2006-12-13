/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;

/**
 * @author vishvesh_mulay
 * @hibernate.joined-subclass table="DYEXTN_CONTAINMENT_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ContainmentAssociationControl extends Control implements ContainmentAssociationControlInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7680442136446012194L;
	/**
	 * 
	 */
	protected ContainerInterface container;

	/**
	 * 
	 */
	public ContainmentAssociationControl()
	{
		super();
	}

	/**
	 * @return container
	 * @hibernate.many-to-one  cascade="save-update" column="DISPLAY_CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
	 */
	public ContainerInterface getContainer()
	{
		return container;
	}

	/**
	 * @param container The container to set.
	 */
	public void setContainer(ContainerInterface container)
	{
		this.container = container;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#generateHTML()
	 */
	public String generateHTML() throws DynamicExtensionsSystemException
	{
		return UserInterfaceiUtility.generateHTML(container);
	}
}
