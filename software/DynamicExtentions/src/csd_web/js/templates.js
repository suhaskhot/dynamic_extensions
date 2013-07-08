var Templates = {

	templateList : null,

	getTemplate : function(templateName) {

		return this.templateList[templateName];
	},

	loadTemplateList : function() {
		this.templateList = new Array();

		var result = $.ajax({
			url : "csd_web/pages/templates/bodyTemplate.html",
			async : false
		}).responseText;
		this.templateList['bodyTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/commonControlPropsTemplate.html",
			async : false
		}).responseText;
		this.templateList['commonControlPropsTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/controlTabTemplate.html",
			async : false
		}).responseText;
		this.templateList['controlTabTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/formTemplate.html",
			async : false
		}).responseText;
		this.templateList['formTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/numericFieldTemplate.html",
			async : false
		}).responseText;
		this.templateList['numericFieldTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/stringTextFieldTemplate.html",
			async : false
		}).responseText;
		this.templateList['stringTextFieldTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/radioButton.html",
			async : false
		}).responseText;
		this.templateList['radioButtonTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/checkBox.html",
			async : false
		}).responseText;
		this.templateList['checkBoxTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/singleSelectDropdown.html",
			async : false
		}).responseText;
		this.templateList['singleSelectDropdownTemplate'] = result;

		result = $.ajax({
			url : "csd_web/pages/templates/multiSelect.html",
			async : false
		}).responseText;
		this.templateList['multiSelectDropdownTemplate'] = result;
		//textAreaTemplate
		result = $.ajax({
			url : "csd_web/pages/templates/multiSelectCheckBox.html",
			async : false
		}).responseText;
		this.templateList['multiSelectCheckBoxTemplate'] = result;
		result = $.ajax({
			url : "csd_web/pages/templates/textArea.html",
			async : false
		}).responseText;
		this.templateList['textAreaTemplate'] = result;
	}
}