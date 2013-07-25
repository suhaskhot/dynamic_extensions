var Models = {

	Form : Backbone.Model.extend({
		controlCollection : null,
		controlObjectCollection : null,
		defaults : {
			caption : "New Form ",
			formName : "newForm",
			status : "new",
			save : "yes",
			id : null
		},
		url : function() {
			return "csdApi/form";
		},
		initialize : function() {
			console.log("Form Model created");
			this.controlCollection = new Array();
			this.controlObjectCollection = {};
		}

	}),
	// Field
	Field : Backbone.Model
			.extend({
				defaults : {
					conceptDefinitionSource : "BJC-MED",
					status : "new",
					xPos : 0,
					sequenceNumber : 0,
					id : null,
					width : 15,
					noOfRows : 3,
					pvs : {}
				},
				url : function() {
					return "csdApi/form/control";
				},
				initialize : function() {
					console.log("FieldControl Model created");
				},
				validate : function(attrs) {
					var errors = [];
					if (!attrs.controlName) {
						errors.push({
							name : 'controlName',
							message : 'Field\'s name is required.'
						});
					}
					if (!attrs.maximumValue) {
					} else if (attrs.maximumValue != ""
							&& isNaN(attrs.maximumValue)) {
						// $("#inputField").css('color', 'red');
						errors.push({
							name : 'maximumValue',
							message : 'Maximum value should be numeric.'
						});
					}

					if (!attrs.minimumValue) {
					} else if (attrs.minimumValue != ""
							&& isNaN(attrs.minimumValue)) {
						// $("#inputField").css('color', 'red');
						errors.push({
							name : 'minimumValue',
							message : 'Minimum value should be numeric.'
						});
					}
					if (!attrs.width) {
					} else if (attrs.width != "" && isNaN(attrs.width)) {
						// $("#inputField").css('color', 'red');
						errors.push({
							name : 'width',
							message : 'Width should be numeric.'
						});
					}
					if (!attrs.noOfRows) {
					} else if (attrs.noOfRows != "" && isNaN(attrs.noOfRows)) {
						// $("#inputField").css('color', 'red');
						errors.push({
							name : 'noOfRows',
							message : 'Number of rows should be numeric.'
						});
					}

					if (!attrs.noOfDigits) {
					} else if (attrs.noOfDigits != ""
							&& isNaN(attrs.noOfDigits)) {
						// $("#inputField").css('color', 'red');
						errors.push({
							name : 'noOfDigits',
							message : 'Number of digits should be numeric.'
						});
					}

					if (!attrs.noOfDigitsAfterDecimal) {
					} else if (attrs.noOfDigitsAfterDecimal != ""
							&& isNaN(attrs.noOfDigitsAfterDecimal)) {
						// $("#inputField").css('color', 'red');
						errors
								.push({
									name : 'noOfDigitsAfterDecimal',
									message : 'Number of digits after decimal should be numeric.'
								});
					}

					switch (attrs.type) {
					case "radioButton":

					case "listBox":

					case "multiselectBox":

					case "multiselectCheckBox":

						if (attrs.pvs.length == 0 && !attrs.pvFile) {
							errors.push({
								name : 'pvs',
								message : 'Permissible values are required.'
							});
						}
						break;

					default:
					}
					return errors;
				}
			})

}