
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;

public abstract class ContainerMapper {

	protected ControlMapper controlMapper = ControlMapper.getInstance();
	protected Container rootContainer = null;

	public Container getRootContainer() {
		return rootContainer;
	}

	public void setRootContainer(Container rootContainer) {
		this.rootContainer = rootContainer;
		controlMapper.setRootContainer(rootContainer);
	}

	public abstract Container propertiesToContainer(Properties properties, UserContext userContext) throws Exception;

	public abstract void propertiesToContainer(Properties formProperties, Container container, UserContext userContext)
			throws Exception;

	public abstract Properties containerToProperties(Container container) throws Exception;
}
