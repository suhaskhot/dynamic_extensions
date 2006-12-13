/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.domaininterface.userinterface;




public interface ContainmentAssociationControlInterface extends ControlInterface
{
	/**
	 * @return container
	 */
	ContainerInterface getContainer();
	

	/**
	 * @param container The container to set.
	 */
	void setContainer(ContainerInterface container);

}
