package edu.common.dynamicextensions.domaininterface.userinterface;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;


public interface CategoryAssociationControlInterface extends ControlInterface
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
	 * This method generates the HMTL for this containement as a Link.
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	String generateLinkHTML() throws DynamicExtensionsSystemException;
}
