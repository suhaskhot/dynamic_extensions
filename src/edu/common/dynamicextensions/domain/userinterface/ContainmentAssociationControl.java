/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.domain.userinterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author vishvesh_mulay
 * @hibernate.joined-subclass table="DYEXTN_CONTAINMENT_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ContainmentAssociationControl extends Control
		implements
			ContainmentAssociationControlInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7680442136446012194L;
	/**
	 * 
	 */
	protected Container container;

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
	public Container getContainer()
	{
		return container;
	}

	/**
	 * @param container The container to set.
	 */
	public void setContainer(Container container)
	{
		this.container = container;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#generateHTML()
	 */
	public String generateHTML() throws DynamicExtensionsSystemException
	{
		List<ControlInterface> controlCollection = new ArrayList<ControlInterface>(container.controlCollection);
		Collections.sort(controlCollection);
		
		StringBuffer containerHTML = new StringBuffer();

		for (ControlInterface control : controlCollection)
		{
			String controlHTML = control.generateHTML();
			containerHTML.append(controlHTML);
		}

		return containerHTML.toString();
	}
}
