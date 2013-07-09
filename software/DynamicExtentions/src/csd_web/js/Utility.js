var Utility = {
	constructBaseURL : function() {
		var baseURL = "";
		var currentURL = window.location + "";
		var tempCurrentURL = currentURL.split('/');
		for (cntr = 0; cntr < tempCurrentURL.length - 1; cntr++) {
			baseURL += tempCurrentURL[cntr] + "/"
		}
		return baseURL;
	},

	getShortCode : function(type) {
		var shortCode = Main.nodeCounter;

		switch (type) {

		case "stringTextField":
			shortCode = "ST_" + Main.nodeCounter;
			break;

		case "numericField":
			shortCode = "NT_" + Main.nodeCounter;
			break;

		case "textArea":
			shortCode = "TA_" + Main.nodeCounter;
			break;

		case "radioButton":
			shortCode = "RB_" + Main.nodeCounter;
			break;

		case "checkBox":
			shortCode = "CB_" + Main.nodeCounter;
			break;

		case "listBox":
			shortCode = "LB_" + Main.nodeCounter;
			break;

		case "multiselectBox":
			shortCode = "MLB_" + Main.nodeCounter;
			break;

		case "multiselectCheckBox":
			shortCode = "MCB_" + Main.nodeCounter;
			break;

		case "datePicker":
			shortCode = "DP_" + Main.nodeCounter;
			break;

		case "fileUpload":
			shortCode = "FU_" + Main.nodeCounter;
			break;

		case "note":
			shortCode = "N_" + Main.nodeCounter;
			break;

		case "heading":
			shortCode = "H_" + Main.nodeCounter;
			break;

		case "label":
			shortCode = "L_" + Main.nodeCounter;
			break;

		case "subForm":
			shortCode = "SF_" + Main.nodeCounter;
			break;

		default:

		}
		Main.nodeCounter++;
		return shortCode;
	},

	toCamelCase : function(str) {
		str = $.camelCase(str.replace(/[_ ]/g, '-')).replace(/-/g, '');
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	},

	split : function(val) {
		return val.split(/\s\s*/);
	},

	extractLast : function(term) {
		return this.split(term).pop();
	},

	trim : function(str) {
		if (str.trim)
			return str.trim();
		else
			return str.replace(/(^\s*)|(\s*$)/g, "");
	},

	addFieldHandlerMap : null,

	addStringTextField : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['stringTextFieldTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addNumericField : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['numericFieldTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addTextArea : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['textAreaTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addRadioButton : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['radioButtonTemplate']
					+ Templates.templateList['pvTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addCheckBox : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['checkBoxTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addDropDown : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['singleSelectDropdownTemplate']
					+ Templates.templateList['pvTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addMultiselectDropDown : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['multiSelectDropdownTemplate']
					+ Templates.templateList['pvTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addMultiselectCheckBox : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['multiSelectCheckBoxTemplate']
					+ Templates.templateList['pvTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addDatePicker : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['datePickerTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addFileUpload : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['fileUploadTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addNote : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addHeading : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addLabel : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['submitButtonTemplate']
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	addSubForm : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['subFormTemplate']
					+ Templates.templateList['submitButtonTemplate'],
			subForm : new Models.Form()
		});

		if (show)
			return Views.showControl(container, controlModel);
		else
			return controlModel;
	},

	initializeFieldHandlerMap : function() {
		this.addFieldHandlerMap = new Array();

		this.addFieldHandlerMap['stringTextField'] = this.addStringTextField;
		this.addFieldHandlerMap['numericField'] = this.addNumericField;
		this.addFieldHandlerMap['textArea'] = this.addTextArea;
		this.addFieldHandlerMap['radioButton'] = this.addRadioButton;
		this.addFieldHandlerMap['checkBox'] = this.addCheckBox;
		this.addFieldHandlerMap['listBox'] = this.addDropDown;
		this.addFieldHandlerMap['multiselectBox'] = this.addMultiselectDropDown;
		this.addFieldHandlerMap['multiselectCheckBox'] = this.addMultiselectCheckBox;
		this.addFieldHandlerMap['datePicker'] = this.addDatePicker;
		this.addFieldHandlerMap['fileUpload'] = this.addFileUpload;
		this.addFieldHandlerMap['note'] = this.addNote;
		this.addFieldHandlerMap['heading'] = this.addHeading;
		this.addFieldHandlerMap['label'] = this.addLabel;
		this.addFieldHandlerMap['subForm'] = this.addSubForm;
	},

	getAddFieldHandler : function(controlType) {
		return this.addFieldHandlerMap[controlType];
	}

}