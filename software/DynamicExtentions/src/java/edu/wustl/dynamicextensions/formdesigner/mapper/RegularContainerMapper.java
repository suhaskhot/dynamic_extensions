
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
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.wustl.dynamicextensions.formdesigner.usercontext.AppUserContextProvider;
import edu.wustl.dynamicextensions.formdesigner.usercontext.CSDProperties;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

public class RegularContainerMapper extends ContainerMapper {
	
	public Container propertiesToContainer(Properties properties, UserContext userContext) throws Exception {
		Container container = new Container();
		propertiesToContainer(properties, container, userContext);
		return container;
	}

	public void propertiesToContainer(Properties formProperties, Container container, UserContext userContext)
			throws Exception {
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

				Control control = controlMapper.propertiesToControl(controlProps, null);
				control.setContainer(container);
				container.addControl(control);
			}
		}
		// process skip rules
		Properties skipRulesPropertiesList = new Properties(formProperties.getMap("skipRules"));
		Set<String> skipRuleKeySet = skipRulesPropertiesList.getAllProperties().keySet();
		SkipRuleMapper skipRuleMapper = new SkipRuleMapper(container);

		for (String key : skipRuleKeySet) {
			Properties skipRuleProperties = new Properties(skipRulesPropertiesList.getMap(key));
			container.addSkipRule(skipRuleMapper.propertiesToSkipRule(skipRuleProperties));
		}

		// process calculated attributes
		Map<String, Object> formulaeProperties = formProperties.getMap("formulae");
		if (formulaeProperties != null) {
			Properties formulaeList = new Properties(formulaeProperties);
			Set<String> formulaeKeySet = formulaeList.getAllProperties().keySet();

			for (String key : formulaeKeySet) {

				String controlName = key;
				String formula = container.getShortCodeFormula(formulaeList.getString(key));
				NumberField numericControl = (NumberField) container.getControl(controlName, "\\.");
				numericControl.setFormula(formula);
			}
		}

	}

	/**
	 * @param container
	 * @return
	 */
	@Override
	public Properties containerToProperties(Container container) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
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
			controlPropertiesCollection.add(controlMapper.controlToProperties(control, container).getAllProperties());
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
