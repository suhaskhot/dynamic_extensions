
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

public class ContainerMapper {

	private static ControlMapper controlMapper = new ControlMapper();

	public Container propertiesToContainer(Properties properties, boolean editControls) throws Exception {
		Container container = new Container();
		propertiesToContainer(properties, container, editControls);
		return container;
	}

	public void propertiesToContainer(Properties formProperties, Container container, boolean editControls)
			throws Exception {
		container.setName(formProperties.getString("formName"));
		container.setCaption(formProperties.getString("caption"));
		container.setId(formProperties.getLong("id"));
		Collection<Map<String, Object>> controlCollection = formProperties.getListOfMap("controlCollection");

		if (controlCollection != null) {
			for (Map<String, Object> controlPropertiesMap : controlCollection) {
				Properties controlProps = new Properties(controlPropertiesMap);
				String status = controlProps.getString("status");
				if (status != null && status.trim().equalsIgnoreCase("delete")) {
					// delete control
					String controlName = controlProps.getString("controlName");
					if(container.getControl(controlName)!=null){
						container.deleteControl(controlName);
					}
				} else {
					// add edit controls
					if (editControls) {
						addEditControl(container, controlProps);
					} else {
						container.addControl(controlMapper.propertiesToControl(controlProps));
					}
				}
			}
		}
	}

	/**
	 * @param container
	 * @param controlProperties
	 * @throws Exception 
	 */
	private void addEditControl(Container container, Properties controlProperties) throws Exception {
		if (controlProperties.get("id") != null) {
			container.editControl(controlProperties.getString(CSDConstants.CONTROL_NAME),
					controlMapper.propertiesToControl(controlProperties));
		} else {
			if (container.getControl(controlProperties.getString(CSDConstants.CONTROL_NAME)) == null) {
				container.addControl(controlMapper.propertiesToControl(controlProperties));
			} else {
				container.editControl(controlProperties.getString(CSDConstants.CONTROL_NAME),
						controlMapper.propertiesToControl(controlProperties));
			}
		}
	}

	/**
	 * @param container
	 * @return
	 */
	public Properties containerToProperties(Container container) {
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("formName", container.getName());
		propertiesMap.put("caption", container.getCaption());
		propertiesMap.put("id", container.getId());
		propertiesMap.put("status", CSDConstants.STATUS_SAVED);
		ControlMapper controlMapper = new ControlMapper();
		List<Map<String, Object>> controlPropertiesCollection = new ArrayList<Map<String, Object>>();
		for (Control control : container.getControls()) {
			controlPropertiesCollection.add(controlMapper.controlToProperties(control).getAllProperties());
		}
		propertiesMap.put("controlCollection", controlPropertiesCollection);
		return new Properties(propertiesMap);
	}
}
