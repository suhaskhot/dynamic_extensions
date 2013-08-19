package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.Container;


public abstract class ContainerMapper {
	protected static ControlMapper controlMapper = new ControlMapper();
	
	public abstract Container propertiesToContainer(Properties properties, boolean editControls) throws Exception;

	public abstract void propertiesToContainer(Properties formProperties, Container container, boolean editControls)
			throws Exception;

	public abstract Properties containerToProperties(Container container) throws Exception;
}
