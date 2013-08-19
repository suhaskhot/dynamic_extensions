var Models = {

	Form : Backbone.Model
			.extend({
				defaults : {
					caption : "New Form ",
					formName : "newForm",
					status : "new",
					save : "yes",
					id : null,
					skipRules : {},
					controlCollection : new Array(),
					controlObjectCollection : {}
				},

				url : function() {
					return "csdApi/form";
				},

				initialize : function() {
					console.log("Form Model created");
				},

				getControl : function(name) {
					var controlName = name.split(".");
					if (controlName.length == 1) {
						return this.get('controlObjectCollection')[controlName[0]];
					} else {
						return this.get('controlObjectCollection')[controlName[0]]
								.get('subForm').get('controlObjectCollection')[controlName[1]];
					}
				},

				addControl : function(controlName, control) {
					var cntrlNames = controlName.split(".");
					if (cntrlNames.length == 1) {
						this.get('controlObjectCollection')[controlName] = control;
					} else {
						this.get('controlObjectCollection')[cntrlNames[0]].get(
								'subForm').get('controlObjectCollection')[cntrlNames[1]] = control;
					}
				},

				editControl : function(controlName, control) {
					this.deleteControl(controlName);
					this.addControl(controlName, control);
				},

				deleteControl : function(controlName) {
					var cntrlName = controlName.split(".");
					var control = this.getControl(controlName);
					if (cntrlName.length == 1) {
						if (control != undefined) {
							delete this.get('controlObjectCollection')[cntrlName[0]];
						}
					} else {
						if (control != undefined) {
							delete this.get('controlObjectCollection')[cntrlName[0]]
									.get('subForm').get(
											'controlObjectCollection')[cntrlName[1]];
						}
					}

				},

				markControlAsDeleted : function(controlName) {
					var cntrlName = controlName.split(".");
					var control = this.getControl(controlName);
					if (cntrlName.length == 1) {
						if (control != undefined) {
							this.get('controlObjectCollection')[cntrlName[0]]
									.set({
										status : "delete"
									});
						}
					} else {
						if (control != undefined) {
							this.get('controlObjectCollection')[cntrlName[0]]
									.get('subForm').get(
											'controlObjectCollection')[cntrlName[1]]
									.set({
										status : "delete"
									});
						}
					}

				},

				addSkipRule : function(skipRule) {
					this.get('skipRules')[GlobalMemory.skipRulesCounter] = skipRule;
					GlobalMemory.skipRulesCounter++;
				},

				editSkipRule : function(id, skipRule) {
					delete this.get('skipRules')[id];
					this.get('skipRules')[id] = skipRule;
				},

				deleteSkipRule : function(id) {
					delete this.get('skipRules')[id];
				},

				addCalculationFormula : function(controlName, formula) {
					this.get('controlObjectCollection')[controlName]
							.addCalculationFormula(formula);
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
					noOfDigits : 19,// based on migration code
					noOfDigitsAfterDecimal : 5, // based on migration code
					pvs : {}
				},

				addPermissibleValue : function(id, pv) {
					this.get('pvs')[id] = pv;
				},

				deletePermissibleValue : function(id) {
					delete this.get('pvs')[id];
				},

				editPermissibleValue : function(id, pv) {
					delete this.get('pvs')[id];
					this.get('pvs')[id] = pv;
				},

				url : function() {
					return "csdApi/form/control";
				},

				initialize : function() {
					console.log("FieldControl Model created");
				},

				addCalculationFormula : function(cFormula) {
					if (this.get('type') == "numericField") {
						this.set({
							isCalculated : true,
							formula : cFormula
						});
					}
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
						errors.push({
							name : 'maximumValue',
							message : 'Maximum value should be numeric.'
						});
					}

					if (!attrs.minimumValue) {
					} else if (attrs.minimumValue != ""
							&& isNaN(attrs.minimumValue)) {
						errors.push({
							name : 'minimumValue',
							message : 'Minimum value should be numeric.'
						});
					}
					if (!attrs.width) {
					} else if (attrs.width != "" && isNaN(attrs.width)) {
						errors.push({
							name : 'width',
							message : 'Width should be numeric.'
						});
					}

					if (!attrs.noOfRows) {
					} else if (attrs.noOfRows != "" && isNaN(attrs.noOfRows)) {
						errors.push({
							name : 'noOfRows',
							message : 'Number of rows should be numeric.'
						});
					}

					switch (attrs.type) {
					case "radioButton":

					case "listBox":

					case "multiselectBox":

					case "multiselectCheckBox":

						if (Object.keys(attrs.pvs).length == 0 && !attrs.pvFile) {
							errors.push({
								name : 'pvs',
								message : 'Permissible values are required.'
							});
						}
						break;
					case "numericField":
						if (!attrs.noOfDigits || isNaN(attrs.noOfDigits)) {
							errors.push({
								name : 'noOfDigits',
								message : 'Number of Digits should be numeric.'
							});
						}

						if (!attrs.noOfDigitsAfterDecimal
								|| isNaN(attrs.noOfDigitsAfterDecimal)) {
							errors
									.push({
										name : 'noOfDigitsAfterDecimal',
										message : 'Number of Digits After Decimal should be numeric.'
									});
						}
						break;

					default:
					}
					return errors;
				}
			})

}