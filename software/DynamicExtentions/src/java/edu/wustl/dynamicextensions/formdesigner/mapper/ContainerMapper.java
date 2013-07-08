
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

public class ContainerMapper
{

	public Container propertiesToContainer(Properties properties) throws Exception
	{
		Container container = new Container();
		propertiesToContainer(properties, container);
		return container;
	}

	public void propertiesToContainer(Properties formProperties, Container container) throws Exception
	{
		container.setName(formProperties.getString("formName"));
		container.setCaption(formProperties.getString("caption"));
		for (Map<String, Object> controlPropertiesMap : formProperties
				.getListOfMap("controlCollection"))
		{
			addEditControl(container, new Properties(controlPropertiesMap));
		}
	}

	/**
	 * @param container
	 * @param controlProperties
	 * @throws Exception 
	 */
	private void addEditControl(Container container, Properties controlProperties) throws Exception
	{
		ControlMapper controlMapper = new ControlMapper();
		if (controlProperties.contains("id"))
		{
			container.editControl(controlProperties.getString(CSDConstants.CONTROL_NAME),
					controlMapper.propertiesToControl(controlProperties));
		}
		else
		{
			container.addControl(controlMapper.propertiesToControl(controlProperties));
		}
	}

	/**
	 * @param container
	 * @return
	 */
	public Properties containerToProperties(Container container)
	{
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("formName", container.getName());
		propertiesMap.put("caption", container.getCaption());
		propertiesMap.put("id", container.getId());
		propertiesMap.put("status", CSDConstants.STATUS_SAVED);
		ControlMapper controlMapper = new ControlMapper();
		List<Map<String, Object>> controlPropertiesCollection = new ArrayList<Map<String, Object>>();
		for (Control control : container.getControls())
		{
			controlPropertiesCollection.add(controlMapper.controlToProperties(control)
					.getAllProperties());
		}
		propertiesMap.put("controlCollection", controlPropertiesCollection);
		return new Properties(propertiesMap);
	}
}
