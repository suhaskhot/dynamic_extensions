
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DisableAction;
import edu.common.dynamicextensions.domain.nui.EnableAction;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.ShowAction;
import edu.common.dynamicextensions.domain.nui.ShowPvAction;
import edu.common.dynamicextensions.domain.nui.SkipAction;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class SkipRuleMapper {

	private Container container;

	public SkipRuleMapper(Container container) {
		this.container = container;
	}

	public SkipRule propertiesToSkipRule(Properties skipRuleProperties) throws NumberFormatException,
			FileNotFoundException, IOException {

		String controllingCondition = skipRuleProperties.getString("controllingCondition");
		String controllingValues = skipRuleProperties.getString("controllingValues");
		String controllingField = skipRuleProperties.getString("controllingAttributes");
		String action = skipRuleProperties.getString("action");
		String controlledFields = skipRuleProperties.getString("controlledAttributes");
		String controllingPvs = skipRuleProperties.getString("controllingPvs");
		String pvSubSet = skipRuleProperties.getString("pvSubSet");
		String pvSubSetFile = skipRuleProperties.getString("pvSubSetFile");
		String defaultPv = skipRuleProperties.getString("defaultPv");
		String allOrAny = skipRuleProperties.getString("allAny");

		SkipRuleBuilder skipRuleBuilder = new SkipRuleBuilder(container);

		if (controlledFields != null) {
			if (controlledFields.contains("[") || controlledFields.contains("]"))
				controlledFields = controlledFields.substring(1, controlledFields.length() - 1);
		}

		List<String> cntrldFields = getExtractedControlledValues(controlledFields);

		if (controllingPvs != null) {
			if (controllingPvs.contains("[") || controllingPvs.contains("]"))
				controllingPvs = controllingPvs.substring(1, controllingPvs.length() - 1);
		}

		if (pvSubSet != null) {
			if (pvSubSet.contains("[") || pvSubSet.contains("]")) {
				pvSubSet = pvSubSet.substring(1, pvSubSet.length() - 1);
			}
		}

		if (defaultPv != null) {
			if (defaultPv.trim().equalsIgnoreCase("")) {
				defaultPv = null;
			}
		}
		// and / or get it from properties
		if (allOrAny.equalsIgnoreCase("all")) {
			skipRuleBuilder.allConditions();
		} else if (allOrAny.equalsIgnoreCase("any")) {
			skipRuleBuilder.anyCondition();
		}

		if (controllingValues != null) {
			if (controllingValues.trim().equalsIgnoreCase("")) {
				controllingValues = null;
			}
		}

		populateSkipRuleCondition(controllingCondition, controllingValues, controllingField, controllingPvs,
				skipRuleBuilder);

		populateSkipRuleAction(action, controlledFields, pvSubSet, pvSubSetFile, defaultPv, skipRuleBuilder,
				cntrldFields);

		return skipRuleBuilder.getSkipRule();

	}

	/**
	 * @param action
	 * @param controlledFields
	 * @param pvSubSet
	 * @param pvSubSetFile
	 * @param defaultPv
	 * @param skipRuleBuilder
	 * @param cntrldFields
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	private void populateSkipRuleAction(String action, String controlledFields, String pvSubSet, String pvSubSetFile,
			String defaultPv, SkipRuleBuilder skipRuleBuilder, List<String> cntrldFields) throws FileNotFoundException,
			IOException, NumberFormatException {
		if (action.equalsIgnoreCase("pvSubSet")) {

			if (pvSubSet != null) {
				String[] subsetPvs = pvSubSet.split(",");
				if (subsetPvs.length == 0) {
					skipRuleBuilder.createPvSubSet(controlledFields, defaultPv, pvSubSet);
				} else {
					skipRuleBuilder.createPvSubSet(controlledFields, defaultPv, subsetPvs);
				}

			} else if (pvSubSetFile != null) {

				skipRuleBuilder.createPvSubSet(controlledFields, defaultPv, PvMapper.getPvsFromFile(pvSubSetFile));
			}

		} else {

			skipRuleBuilder.perform(action, cntrldFields.toArray(new String[cntrldFields.size()]));
		}
	}

	/**
	 * @param controllingCondition
	 * @param controllingValues
	 * @param controllingField
	 * @param controllingPvs
	 * @param skipRuleBuilder
	 */
	private void populateSkipRuleCondition(String controllingCondition, String controllingValues,
			String controllingField, String controllingPvs, SkipRuleBuilder skipRuleBuilder) {
		if (controllingCondition != null && controllingValues != null) {

			if (controllingCondition.equalsIgnoreCase("greaterThan")) {

				skipRuleBuilder.greaterThanCondition(controllingField, controllingValues);

			} else if (controllingCondition.equalsIgnoreCase("lessThan")) {

				skipRuleBuilder.lessThanCondition(controllingField, controllingValues);

			} else if (controllingCondition.equalsIgnoreCase("equalTo")) {

				skipRuleBuilder.equalsCondition(controllingField, controllingValues);

			} else if (controllingCondition.equalsIgnoreCase("lessThanEqualTo")) {

				skipRuleBuilder.lessThanEqualsCondition(controllingField, controllingValues);

			} else if (controllingCondition.equalsIgnoreCase("greaterThanEqualTo")) {

				skipRuleBuilder.greaterThanEqualsCondition(controllingField, controllingValues);
			}

		} else if (controllingPvs != null) {

			String[] pvs = controllingPvs.split(",");
			if (pvs.length == 0) {
				skipRuleBuilder.equalsCondition(controllingField, controllingPvs.trim());
			} else {
				for (String pv : pvs) {
					skipRuleBuilder.equalsCondition(controllingField, pv.trim());
				}
			}
		}
	}

	/**
	 * @param controlledFields
	 * @return
	 */
	private List<String> getExtractedControlledValues(String controlledFields) {
		List<String> cntrldFields = new ArrayList<String>();
		String[] tmpCntrlFields = controlledFields.split(",");
		if (tmpCntrlFields.length == 0) {
			cntrldFields.add(controlledFields.trim());

		} else {
			for (String tmpCntrlField : tmpCntrlFields) {
				cntrldFields.add(tmpCntrlField.trim());
			}
		}
		return cntrldFields;
	}

	public Properties skipRuleToProperties(SkipRule skipRule) {

		Map<String, Object> skipRuleProperties = new HashMap<String, Object>();
		List<Map<String, Object>> skipConditions = new ArrayList<Map<String, Object>>();
		Set<String> skipConditionValues = new HashSet<String>();

		getSkipConditions(skipRule, skipConditions, skipConditionValues);
		if (!skipConditions.isEmpty()) {
			Map<String, Object> skipCondition = skipConditions.get(0);
			List<String> controllingValueStr = new ArrayList<String>();

			for (String controllingValue : skipConditionValues) {
				controllingValueStr.add(controllingValue);
			}

			LogicalOp anyOrAllOp = skipRule.getLogicalOp();
			String allOrAny = "all";
			if (anyOrAllOp == LogicalOp.OR) {
				allOrAny = "any";
			}
			skipRuleProperties.put("allAny", allOrAny);

			Control controllingField = (Control) skipCondition.get("controllingField");
			String controllingFieldName = controllingField.getName();
			if (container != controllingField.getContainer()) {
				String controlContainerName = controllingField.getContainer().getName();
				if (container.getName().equalsIgnoreCase(controlContainerName)) {

				} else {
					String controllingFieldPreFix = getSubFormControlNameFromSubFormName(controlContainerName,
							controllingFieldName);
					if (controllingFieldPreFix != null) {
						controllingFieldName = controllingFieldPreFix + "." + controllingFieldName;
					}

					//container.gets

				}

			}

			skipRuleProperties.put("controllingCondition", skipCondition.get("controllingCondition"));
			skipRuleProperties.put("controllingAttributes", controllingFieldName);

			if (controllingField instanceof SelectControl) {
				skipRuleProperties.put("controllingPvs", controllingValueStr);
			} else {
				skipRuleProperties.put("controllingValues", controllingValueStr);
			}
			String action = "";
			List<SkipAction> skipActions = skipRule.getActions();
			SkipAction skipAction = skipRule.getActions().get(0);
			List<String> pvSubSet = new ArrayList<String>();
			String defaultPv = null;

			if (skipAction instanceof ShowAction) {
				action = "show";
			} else if (skipAction instanceof ShowAction) {
				action = "hide";
			} else if (skipAction instanceof EnableAction) {
				action = "enable";
			} else if (skipAction instanceof DisableAction) {
				action = "disable";
			} else if (skipAction instanceof ShowPvAction) {
				action = "pvSubSet";
				List<PermissibleValue> subsetPvs = ((ShowPvAction) skipAction).getListOfPvs();
				for (PermissibleValue subSetPv : subsetPvs) {
					pvSubSet.add(subSetPv.getValue());
				}
				PermissibleValue defPv = ((ShowPvAction) skipAction).getDefaultPv();
				if (defPv != null) {
					defaultPv = defPv.getValue();
				}
			}

			List<String> controlledFields = getControlledFields(skipActions);

			if (!pvSubSet.isEmpty()) {
				skipRuleProperties.put("pvSubSet", pvSubSet);
			}

			if (defaultPv != null) {
				skipRuleProperties.put("defaultPv", defaultPv);
			}

			skipRuleProperties.put("action", action);
			skipRuleProperties.put("controlledAttributes", controlledFields);
		}

		return new Properties(skipRuleProperties);
	}

	/**
	 * @param skipActions
	 * @return
	 */
	private List<String> getControlledFields(List<SkipAction> skipActions) {
		List<String> controlledFields = new ArrayList<String>();
		for (SkipAction skpAction : skipActions) {
			Control targetControl = skpAction.getTargetCtrl();
			String targetControlName = targetControl.getName();

			String controlContainerName = targetControl.getContainer().getName();
			if (container.getName().equalsIgnoreCase(controlContainerName)) {

			} else {
				String tagetControlPreFix = getSubFormControlNameFromSubFormName(controlContainerName,
						targetControlName);

				if (tagetControlPreFix != null) {
					targetControlName = tagetControlPreFix + "." + targetControlName;
				}
				//container.gets

			}

			controlledFields.add(targetControlName);
		}
		return controlledFields;
	}

	/**
	 * @param skipRule
	 * @param skipConditions
	 * @param skipConditionValues
	 */
	private void getSkipConditions(SkipRule skipRule, List<Map<String, Object>> skipConditions,
			Set<String> skipConditionValues) {
		for (SkipCondition skipCondition : skipRule.getConditions()) {
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("controllingCondition", getStringRepOfRelationalOp(skipCondition.getRelationalOp()));
			conditionMap.put("controllingField", skipCondition.getSourceControl());
			conditionMap.put("controllingValue", skipCondition.getValue());
			skipConditionValues.add(skipCondition.getValue());
			skipConditions.add(conditionMap);
		}
	}

	private String getStringRepOfRelationalOp(RelationalOp relOp) {

		String stringRep = "";
		if (relOp == RelationalOp.EQ) {

			stringRep = "equalTo";

		} else if (relOp == RelationalOp.GT) {

			stringRep = "greaterThan";

		} else if (relOp == RelationalOp.LT) {

			stringRep = "lessThan";

		} else if (relOp == RelationalOp.GE) {

			stringRep = "greaterThanEqualTo";

		} else if (relOp == RelationalOp.LE) {

			stringRep = "lessThanEqualTo";
		}

		return stringRep;
	}

	public String getSubFormControlNameFromSubFormName(String subFormName, String subControlName) {
		String subFormControlName = null;
		for (Control control : container.getControls()) {
			if (control instanceof SubFormControl) {
				SubFormControl subFormControl = (SubFormControl) control;
				if (subFormControl.getSubContainer().getName().equalsIgnoreCase(subFormName)) {
					if (subFormControl.getSubContainer().getControl(subControlName) != null) {
						subFormControlName = subFormControl.getName();
						break;
					}
				}
			}
		}

		return subFormControlName;
	}
}
