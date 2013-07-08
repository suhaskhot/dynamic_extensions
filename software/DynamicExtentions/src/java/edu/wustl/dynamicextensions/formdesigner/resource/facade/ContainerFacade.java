
package edu.wustl.dynamicextensions.formdesigner.resource.facade;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.wustl.dynamicextensions.formdesigner.mapper.ContainerMapper;
import edu.wustl.dynamicextensions.formdesigner.mapper.ControlMapper;
import edu.wustl.dynamicextensions.formdesigner.mapper.Properties;

public class ContainerFacade
{

	private Container container = null;
	private static ContainerMapper containerMapper = new ContainerMapper();

	/**
	 * @param container
	 */
	private ContainerFacade(Container container)
	{
		this.container = container;
	}

	/**
	 * @param containerProps
	 * @return
	 * @throws Exception 
	 */
	public static ContainerFacade createContainer(Properties containerProps) throws Exception
	{
		return new ContainerFacade(containerMapper.propertiesToContainer(containerProps));
	}

	public void updateContainer(Properties containerProps) throws Exception
	{
		containerMapper.propertiesToContainer(containerProps, container);
	}

	/**
	 * @param id
	 * @return
	 */
	public static ContainerFacade loadContainer(Long id)
	{
		Container newContainer = Container.getContainer(id);

		return new ContainerFacade(newContainer);
	}

	/**
	 * @param controlProps
	 * @throws Exception 
	 */
	public void createControl(Properties controlProps) throws Exception
	{
		Control control = new ControlMapper().propertiesToControl(controlProps);
		container.addControl(control);
	}

	/**
	 * @param control
	 */
	public void deleteControl(String controlName)
	{
		container.deleteControl(controlName);
	}

	/**
	 * 
	 */
	public void persistContainer()
	{
		container.save(null);
	}

	/**
	 * @return
	 */
	public Properties getProperties()
	{
		return containerMapper.containerToProperties(container);
	}

}
