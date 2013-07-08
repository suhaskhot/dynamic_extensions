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

	toCamelCase : function(str) {
		str = $.camelCase(str.replace(/[_ ]/g, '-')).replace(/-/g, '');
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	},

	addFieldHandlerMap : null,

	addStringTextField : function(controlModel, show, container) {
		controlModel.set({
			template : Templates.templateList['commonControlPropsTemplate']
					+ Templates.templateList['stringTextFieldTemplate']
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
	},

	getAddFieldHandler : function(controlType) {
		return this.addFieldHandlerMap[controlType];
	}

}