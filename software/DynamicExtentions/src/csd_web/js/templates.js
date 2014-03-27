var Templates = {

	templateList : null,

	getTemplate : function(templateName) {

		return this.templateList[templateName];
	},

	loadTemplateList : function() {
		this.templateList = new Array();

		var result = $.ajax({
			url : "../pages/templates/bodyTemplate.html",
			async : false
		}).responseText;
		this.templateList['bodyTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/controlTabTemplate.html",
			async : false
		}).responseText;
		this.templateList['controlTabTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/formTemplate.html",
			async : false
		}).responseText;
		this.templateList['formTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/numericFieldTemplate.html",
			async : false
		}).responseText;
		this.templateList['numericFieldTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/stringTextFieldTemplate.html",
			async : false
		}).responseText;
		this.templateList['stringTextFieldTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/radioButton.html",
			async : false
		}).responseText;
		this.templateList['radioButtonTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/checkBox.html",
			async : false
		}).responseText;
		this.templateList['checkBoxTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/singleSelectDropdown.html",
			async : false
		}).responseText;
		this.templateList['singleSelectDropdownTemplate'] = result;

		result = $.ajax({
			url : "../pages/templates/multiSelect.html",
			async : false
		}).responseText;
		this.templateList['multiSelectDropdownTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/multiSelectCheckBox.html",
			async : false
		}).responseText;
		this.templateList['multiSelectCheckBoxTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/textArea.html",
			async : false
		}).responseText;
		this.templateList['textAreaTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/datePicker.html",
			async : false
		}).responseText;
		this.templateList['datePickerTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/fileUpload.html",
			async : false
		}).responseText;
		this.templateList['fileUploadTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/pvTemplate.html",
			async : false
		}).responseText;
		this.templateList['pvTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/submitButtonTemplate.html",
			async : false
		}).responseText;
		this.templateList['submitButtonTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/subForm.html",
			async : false
		}).responseText;
		this.templateList['subFormTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/advancedPropertiesTemplate.html",
			async : false
		}).responseText;
		this.templateList['advancedPropertiesTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/note.html",
			async : false
		}).responseText;
		this.templateList['noteTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/label.html",
			async : false
		}).responseText;
		this.templateList['labelTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/heading.html",
			async : false
		}).responseText;
		this.templateList['headingTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/pageBreak.html",
			async : false
		}).responseText;
		this.templateList['pageBreakTemplate'] = result;
		
		result = $.ajax({
			url : "../pages/templates/summaryTemplate.html",
			async : false
		}).responseText;
		this.templateList['summaryTemplate'] = result;
	}
}
