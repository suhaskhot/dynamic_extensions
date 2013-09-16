/**
 * 
 */
var AdvancedControlPropertiesBizLogic = {
	/*
	 * Set Formula for calculated attributes
	 */
	setFormulaForCalculatedAttirbute : function(selectionName, isCalc,
			calcFormula) {
		var controlNames = selectionName.split(".");
		if (controlNames.length == 1) {
			if (Main.formView.getFormModel().get('controlObjectCollection')[selectionName] != undefined) {
				Main.formView.getFormModel().get('controlObjectCollection')[selectionName]
						.set({
							isCalculated : isCalc,
							formula : calcFormula
						});
			}
		} else {
			if (Main.formView.getFormModel().get('controlObjectCollection')[controlNames[0]]
					.get('subForm').get('controlObjectCollection')[controlNames[1]] != undefined) {
				Main.formView.getFormModel().get('controlObjectCollection')[controlNames[0]]
						.get('subForm').get('controlObjectCollection')[controlNames[1]]
						.set({
							isCalculated : isCalc,
							formula : calcFormula
						});
			}
		}
	},

	setSkipRuleForAttirbute : function(selectionName, skipLogic) {

		Main.formView.getFormModel().get('skipRules')[GlobalMemory.skipRulesCounter] = skipLogic;

		GlobalMemory.skipRulesCounter++;
	},

	deleteSkipRule : function(id) {

		delete Main.formView.getFormModel().get('skipRules')[id];
		$('#' + id).remove();
	},

	deleteFormula : function(id) {
		AdvancedControlPropertiesBizLogic.setFormulaForCalculatedAttirbute(id,
				false, null);
		$('#' + id).remove();

	},

	// Refresh lists with current controls.

	refreshListsWithControls : function() {
		AdvancedControlPropertiesBizLogic
				.populateCalcAttribSelectBoxWithControlNames("availableFields1");
		AdvancedControlPropertiesBizLogic
				.populateSkipLogicSelectBoxWithControlNames("controlledField");
		AdvancedControlPropertiesBizLogic
				.populateSkipLogicSelectBoxWithControlNames("controllingField");
		$("#controlledField").trigger("liszt:updated");

	},

	// Populate the given select box with the latest control names
	populateCalcAttribSelectBoxWithControlNames : function(selectTagId) {
		$("#" + selectTagId).empty();
		var controls = ControlBizLogic.getListOfCurrentNumericControls(true);
		for ( var key = 0; key < controls.length; key++) {
			var control = controls[key];
			$("#" + selectTagId).append($("<option/>", {
				value : control.name,
				text : control.name,
				title : control.caption
			}));
		}
	},

	populateSkipLogicSelectBoxWithControlNames : function(selectTagId) {
		$("#" + selectTagId).empty();
		var controls = ControlBizLogic.getListOfCurrentControls(true);
		for ( var key = 0; key < controls.length; key++) {
			var control = controls[key];
			$("#" + selectTagId).append($("<option/>", {
				value : control.name,
				text : control.caption,
				title : control.caption
			}));
		}
	},

	populatePvSelectBoxWithControlNames : function(selectTagId, control) {
		$("#" + selectTagId).empty();
		var controls = ControlBizLogic.getListOfPvNameValuePairs(control);
		for ( var key = 0; key < controls.length; key++) {
			var control = controls[key];
			$("#" + selectTagId).append($("<option/>", {
				value : control.name,
				text : control.name,
				title : control.caption
			}));
		}
	},

	loadSkipRules : function(model) {
		//clear the table;
		$("#skipRulesTable tr:gt(0)").remove();
		var skipRules = model.get('skipRules');
		for ( var key in skipRules) {
			// var skipRule = skipRules[key];
			Main.advancedControlsView.addSkipRuleToTable(skipRules[key], key);
			GlobalMemory.skipRulesCounter++;

		}
		$("#pvs").trigger("liszt:updated");
		$("#controlledField").trigger("liszt:updated");
		Main.advancedControlsView.setTableCss('skipRulesTable');
	},
	setSkipRuleControllingFieldsUI : function() {
		// based on control type show hide pvs
		var control = ControlBizLogic.getControlFromControlName($(
				'#controllingField').val());
		// clear the messages
		Main.advancedControlsView.clearMessage();

		switch (control.get('type')) {
		case "radioButton":

		case "listBox":

		case "multiselectBox":
		
		case "comboBox":

		case "multiselectCheckBox":
			$('#controllingValuesDiv').hide();
			$('#pvDiv').show();
			AdvancedControlPropertiesBizLogic
					.populatePvSelectBoxWithControlNames('pvs', control);
			$("#pvs").trigger("liszt:updated");
			break;
		case "numericField":
			$('#pvDiv').hide();
			$('#controllingValuesDiv').show();
			break;
		default:
			$('#pvDiv').hide();
			$('#controllingValuesDiv').hide();
		}
	}

}