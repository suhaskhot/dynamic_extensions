
package edu.common.dynamicextensions.ndao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.nui.Action;
import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvVersion;
import edu.common.dynamicextensions.domain.nui.RadioButton;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.ShowAction;
import edu.common.dynamicextensions.domain.nui.ShowPvAction;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestContainerDao extends DynamicExtensionsBaseTestCase {

	private static final Logger logger = Logger.getLogger(ContainerDao.class);

	public void testSaveContainer() {
		Container container = createContainer("Patient");
		List<Control> controls = getControls();
		addControlToContainer(container, controls);
		logger.info(container.save(null));
	}

	public void testSkipLogic() {
		Container container = createContainer("Patient");
		List<Control> controls = getControls();
		addControlToContainer(container, controls);

		Control sourceControl = container.getControl(RadioButton.class.getCanonicalName());
		Control targetControl = container.getControl(ComboBox.class.getCanonicalName());
		ShowAction action = new ShowAction();
		String conditionValue = "pv1";

		addSkipLogic(sourceControl, targetControl, action, conditionValue);
		logger.info(container.save(null));

	}

	public void testSkipLogicAcrossContainer() {
		Container container = createContainer("Patient");
		List<Control> controls = getControls();
		addControlToContainer(container, controls);

		Container subForm = createContainer("Patient-subform");
		controls = getControls();
		addControlToContainer(subForm, controls);

		Control sourceControl = container.getControl(RadioButton.class.getCanonicalName());
		Control targetControl = subForm.getControl(ComboBox.class.getCanonicalName());
		ShowAction action = new ShowAction();
		String conditionValue = "pv1";
		addSkipLogic(sourceControl, targetControl, action, conditionValue);

		sourceControl = container.getControl(ListBox.class.getCanonicalName());
		targetControl = container.getControl(ComboBox.class.getCanonicalName());
		ShowPvAction pvAction = new ShowPvAction();
		conditionValue = "pv2";
		pvAction.setListOfPvs(createPVList(3));
		addSkipLogic(sourceControl, targetControl, pvAction, conditionValue);
		addSubform(container, 1, subForm, "Patient-Subform");
		logger.info(container.save(null));

	}

	public void testSkipLogicComboBox() {
		Container mainForm = createContainer("Patient");
		List<Control> controls = getControls();
		addControlToContainer(mainForm, controls);

		Container container = createContainer("Patient-subForm");
		controls = getControls();
		addControlToContainer(container, controls);

		Control sourceControl = container.getControl(ListBox.class.getCanonicalName());
		Control targetControl = container.getControl(ComboBox.class.getCanonicalName());
		ShowPvAction pvAction = new ShowPvAction();
		String conditionValue = "pv2";
		pvAction.setListOfPvs(createPVList(3));
		addSkipLogic(sourceControl, targetControl, pvAction, conditionValue);

		addSubform(mainForm, -1, container, "Patient-subform");

		logger.info(mainForm.save(null));

	}

	public void testSkipLogicMultiselect() {
		Container mainForm = createContainer("Patient");
		List<Control> controls = getControls();
		addControlToContainer(mainForm, controls);

		Container container = createContainer("Patient-subForm");
		controls = getControls();
		addControlToContainer(container, controls);

		Control sourceControl = container.getControl(MultiSelectCheckBox.class.getCanonicalName());
		Control targetControl = container.getControl(ComboBox.class.getCanonicalName());
		ShowPvAction pvAction = new ShowPvAction();
		String conditionValue = "pv2";
		pvAction.setListOfPvs(createPVList(3));
		addSkipLogic(sourceControl, targetControl, pvAction, conditionValue);

		addSubform(mainForm, -1, container, "Patient-subform");

		logger.info(mainForm.save(null));

	}

	private void addSkipLogic(Control sourceControl, Control targetControl, Action action, String conditionValue) {
		SkipRule rule1 = new SkipRule();
		Set<SkipCondition> conditions = new HashSet<SkipCondition>();

		SkipCondition condition = new SkipCondition();
		condition.setRelationalOp(RelationalOp.EQ);
		condition.setValue(conditionValue);
		condition.setSourceControl(sourceControl);
		conditions.add(condition);
		rule1.setConditions(conditions);

		rule1.setAction(action);
		rule1.setLogicalOp(LogicalOp.OR);
		Set<SkipRule> rules = new HashSet<SkipRule>();
		rules.add(rule1);
		targetControl.setSkipRules(rules);
		sourceControl.setSkipLogicSourceControl(true);
	}

	private List<Control> getControls() {
		List<Control> controls = new ArrayList<Control>();
		controls.add(createTextField("Name"));
		controls.add(createRadioButton("Radio"));
		controls.add(createComboBox("Combo"));
		controls.add(createListBox("List Box"));
		controls.add(createCheckBox("Checkbox"));
		controls.add(createDateField("DatePicker"));
		controls.add(createMultiselectCheckbox("Multiselect Box"));
		return controls;
	}

	private Control createMultiselectCheckbox(String checkbox) {
		MultiSelectCheckBox field = new MultiSelectCheckBox();
		return updateSelectControl(checkbox, field);
	}

	public void testOneToOneSubform() {
		Container container = createStuffedContainer("Patient");

		int noOfEntries = 1;
		Container subContainer = createStuffedContainer("Patient-subform");

		String caption = "Patient-subform";

		addSubform(container, noOfEntries, subContainer, caption);

		Container subSubContainer = createStuffedContainer("Patient-subform-subform");

		addSubform(subContainer, noOfEntries, subSubContainer, "Patient-subform-subform");

		logger.info(container.save(null));

	}

	private void addSubform(Container container, int noOfEntries, Container subContainer, String caption) {
		SubFormControl subform = new SubFormControl();
		subform.setName("subformControl");
		subform.setCaption(caption);
		subform.setContainer(container);
		subform.setSubContainer(subContainer);
		subform.setNoOfEntries(noOfEntries);
		container.addControl(subform);
	}

	private Container createStuffedContainer(String caption) {
		Container container = createContainer(caption);
		List<Control> controls = getControls();
		addControlToContainer(container, controls);
		return container;
	}

	public void testOneToManySubform() {
		Container container = createStuffedContainer("Patient");

		int noOfEntries = -1;
		Container subContainer = createStuffedContainer("Patient-subform");

		String caption = "Patient-subform";

		addSubform(container, noOfEntries, subContainer, caption);
		logger.info(container.save(null));

	}

	private void addControlToContainer(Container container, List<Control> controls) {

		for (Control control : controls) {
			control.setName(control.getClass().getCanonicalName());
			container.addControl(control);
		}

	}

	private Container createContainer(String caption) {

		Container container = new Container();
		container.setCaption(caption);
		return container;
	}

	private Control createDateField(String caption) {
		DatePicker field = new DatePicker();
		field.setCaption(caption);
		return field;
	}

	private Control createTextField(String caption) {
		StringTextField field = new StringTextField();
		field.setCaption(caption);

		return field;
	}

	private Control createRadioButton(String caption) {
		RadioButton radioButton = new RadioButton();
		return updateSelectControl(caption, radioButton);
	}

	private Control createCheckBox(String caption) {
		CheckBox field = new CheckBox();
		field.setCaption(caption);
		return field;
	}

	private Control createListBox(String caption) {
		ListBox field = new ListBox();
		return updateSelectControl(caption, field);
	}

	private Control createComboBox(String caption) {
		ComboBox field = new ComboBox();
		return updateSelectControl(caption, field);
	}

	private Control updateSelectControl(String caption, SelectControl selectControl) {
		selectControl.setCaption(caption);
		selectControl.setPvDataSource(getDataSource());
		return selectControl;
	}

	private PvDataSource getDataSource() {
		PvDataSource dataSource = new PvDataSource();
		dataSource.setDataType(DataType.STRING);
		PvVersion pvVersion = new PvVersion();
		pvVersion.setPermissibleValues(createPVList(5));
		List<PvVersion> list = new ArrayList<PvVersion>();
		list.add(pvVersion);
		dataSource.setPvVersions(list);
		return dataSource;
	}

	private List<PermissibleValue> createPVList(int count) {
		List<PermissibleValue> permissibleValues = new ArrayList<PermissibleValue>();
		for (int i = 0; i < count; i++) {
			PermissibleValue permissibleValue = new PermissibleValue();
			permissibleValue.setOptionName("pv" + i);
			permissibleValue.setValue("pv" + i);
			permissibleValues.add(permissibleValue);
		}
		return permissibleValues;
	}

}
