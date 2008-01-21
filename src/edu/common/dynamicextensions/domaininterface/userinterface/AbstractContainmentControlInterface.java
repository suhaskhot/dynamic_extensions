
package edu.common.dynamicextensions.domaininterface.userinterface;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

public interface AbstractContainmentControlInterface extends ControlInterface
{
	/**
	 * @return container
	 */
	ContainerInterface getContainer();

	/**
	 * @param container The container to set.
	 */
	void setContainer(ContainerInterface container);

	/**
	 * @return
	 */
	public boolean isCardinalityOneToMany();

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public String generateEditModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public String generateViewModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface#generateLinkHTML()
	 */
	public String generateLinkHTML() throws DynamicExtensionsSystemException;

}
