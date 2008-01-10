package edu.common.dynamicextensions.domaininterface.userinterface;

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

}
