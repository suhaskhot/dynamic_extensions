/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.domaininterface.userinterface;

import edu.common.dynamicextensions.domain.userinterface.Container;



public interface ContainmentAssociationControlInterface extends ControlInterface
{
	/**
	 * @return container
	 */
	Container getContainer();
	

	/**
	 * @param container The container to set.
	 */
	void setContainer(Container container);

}
