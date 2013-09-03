
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;

public abstract class ContainerMapper {

	protected static ControlMapper controlMapper = new ControlMapper();

	public abstract Container propertiesToContainer(Properties properties, UserContext userContext) throws Exception;

	public abstract void propertiesToContainer(Properties formProperties, Container container, UserContext userContext)
			throws Exception;

	public abstract Properties containerToProperties(Container container) throws Exception;
}
