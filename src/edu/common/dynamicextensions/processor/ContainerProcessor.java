package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;

public class ContainerProcessor extends BaseDynamicExtensionsProcessor {
	/**
	 * Protected constructor for ControlProcessor
	 *
	 */
	protected  ContainerProcessor () {

	}
	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ContainerProcessor getInstance () {
		return new ContainerProcessor();
	}
	public ContainerInterface createContainer() {
		return DomainObjectFactory.getInstance().createContainer();
	}

}
