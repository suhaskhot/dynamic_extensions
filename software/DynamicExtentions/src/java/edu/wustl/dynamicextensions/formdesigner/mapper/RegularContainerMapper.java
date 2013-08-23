
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.wustl.dynamicextensions.formdesigner.usercontext.AppUserContextProvider;
import edu.wustl.dynamicextensions.formdesigner.usercontext.CSDProperties;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

public class RegularContainerMapper extends ContainerMapper {

	public Container propertiesToContainer(Properties properties, boolean editControls, UserContext userContext)
			throws Exception {
		Container container = new Container();
		propertiesToContainer(properties, container, editControls, userContext);
		return container;
	}

	public void propertiesToContainer(Properties formProperties, Container container, boolean editControls,
			UserContext userContext) throws Exception {
		container.setName(formProperties.getString("formName"));
		container.setCaption(formProperties.getString("caption"));
		container.setId(formProperties.getLong("id"));

		if (userContext != null) {
			container.setLastUpdatedBy(userContext.getUserId());
			container.setLastUpdatedTime(new Date());
			if (container.getCreatedBy() == null || container.getCreatedBy() == 0) {
				container.setCreatedBy(userContext.getUserId());
				container.setCreationTime(new Date());
			}
		}
		Collection<Map<String, Object>> controlCollection = formProperties.getListOfMap("controlCollection");

		if (controlCollection != null) {
			for (Map<String, Object> controlPropertiesMap : controlCollection) {
				Properties controlProps = new Properties(controlPropertiesMap);
				String status = controlProps.getString("status");
				if (status != null && status.trim().equalsIgnoreCase("delete")) {
					// delete control
					String controlName = controlProps.getString("controlName");
					if (container.getControl(controlName) != null) {
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

			// process skip rules
			Properties skipRulesPropertiesList = new Properties(formProperties.getMap("skipRules"));
			Set<String> keySet = skipRulesPropertiesList.getAllProperties().keySet();
			SkipRuleMapper skipRuleMapper = new SkipRuleMapper(container);

			List<SkipRule> skipRules = container.getSkipRules();

			if (skipRules != null) {
				for (int skipRuleCntr = 0; skipRuleCntr < skipRules.size(); skipRuleCntr++) {
					container.removeSkipRule(skipRuleCntr);
				}
			}

			for (String key : keySet) {
				Properties skipRuleProperties = new Properties(skipRulesPropertiesList.getMap(key));
				container.addSkipRule(skipRuleMapper.propertiesToSkipRule(skipRuleProperties));
			}
		}
	}

	/**
	 * @param container
	 * @param controlProperties
	 * @throws Exception 
	 */
	private void addEditControl(Container container, Properties controlProperties) throws Exception {
		Control control = controlMapper.propertiesToControl(controlProperties);
		control.setContainer(container);
		if (controlProperties.get("id") != null) {
			container.editControl(control.getName(), control);
		} else {
			if (container.getControl(control.getName()) == null) {
				container.addControl(control);
			} else {
				container.editControl(control.getName(), control);
			}
		}
	}

	/**
	 * @param container
	 * @return
	 */
	@Override
	public Properties containerToProperties(Container container) {
		DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("formName", container.getName());
		propertiesMap.put("caption", container.getCaption());
		// get from api
		AppUserContextProvider userContextProvider = CSDProperties.getInstance().getUserContextProvider();
		Long createdBy = container.getCreatedBy();
		if (createdBy != null && createdBy != 0) {
			propertiesMap.put("createdBy", userContextProvider.getUserNameById(container.getCreatedBy()));
			propertiesMap.put("createdOn", dateFormat.format(container.getCreationTime()));
		}

		Long lastUpdatedBy = container.getLastUpdatedBy();

		if (lastUpdatedBy != null && lastUpdatedBy != 0) {
			propertiesMap.put("lastModifiedBy", userContextProvider.getUserNameById(container.getLastUpdatedBy()));
			propertiesMap.put("lastModifiedOn", dateFormat.format(container.getLastUpdatedTime()));
		}

		propertiesMap.put("id", container.getId());
		propertiesMap.put("status", CSDConstants.STATUS_SAVED);
		List<Map<String, Object>> controlPropertiesCollection = new ArrayList<Map<String, Object>>();
		for (Control control : container.getControls()) {
			controlPropertiesCollection.add(controlMapper.controlToProperties(control).getAllProperties());
		}
		propertiesMap.put("controlCollection", controlPropertiesCollection);

		Map<String, Object> skipRuleMap = new HashMap<String, Object>();
		String skipRuleKey = "skipRule_";
		Integer skipRuleKeyNum = 0;
		SkipRuleMapper skipRuleMApper = new SkipRuleMapper(container);
		for (SkipRule skipRule : container.getSkipRules()) {
			skipRuleMap.put(skipRuleKey + skipRuleKeyNum, skipRuleMApper.skipRuleToProperties(skipRule)
					.getAllProperties());
			skipRuleKeyNum++;
		}
		propertiesMap.put("skipRules", skipRuleMap);
		return new Properties(propertiesMap);
	}

}
