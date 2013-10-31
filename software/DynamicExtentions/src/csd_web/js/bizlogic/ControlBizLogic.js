/**
 * 
 */

var ControlBizLogic = {

	_controlTypeChangeRules : {
		'stringTextField' : [ 'numericField', 'textArea' ],
		'numericField' : [ 'stringTextField' ],
		'textArea' : [ 'stringTextField' ],
		'radioButton' : [ 'checkBox', 'comboBox', 'listBox', 'multiselectBox',
				'multiselectCheckBox' ],
		'comboBox' : [ 'checkBox', 'listBox', 'multiselectBox',
				'multiselectCheckBox', 'radioButton' ],
		'listBox' : [ 'checkBox', 'comboBox', 'multiselectBox',
				'multiselectCheckBox', 'radioButton' ],
		'multiselectBox' : [ 'checkBox', 'comboBox', 'listBox',
				'multiselectCheckBox', 'radioButton' ],
		'multiselectCheckBox' : [ 'checkBox', 'comboBox', 'listBox',
				'multiselectBox', 'radioButton' ],
		'checkBox' : [ 'radioButton', 'comboBox', 'listBox', 'multiselectBox',
				'multiselectCheckBox' ],
		'note' : [ 'heading', 'label' ],
		'heading' : [ 'note', 'label' ],
		'label' : [ 'note', 'heading' ]
	},

	_controlLabels : {
		'stringTextField' : 'String Text Box',
		'numericField' : 'Numeric',
		'textArea' : 'Text Area',
		'radioButton' : 'Radio Button',
		'comboBox' : 'Drop down',
		'checkBox' : 'Check Box',
		'multiselectBox' : 'Multi Select List Box',
		'listBox' : 'List Box',
		'multiselectCheckBox' : 'Multi Select Check Box',
		'datePicker' : 'Date Picker',
		'fileUpload' : 'File Upload',
		'note' : 'Note',
		'heading' : 'Heading',
		'label' : 'Label',
		'subForm' : 'Sub Form',
		'pageBreak' : 'Page Break'
	},

	getPermissibleControlTypes : function(controlType) {
		return ControlBizLogic._controlTypeChangeRules[controlType];
	},

	getLabelByType : function(controlType) {
		return ControlBizLogic._controlLabels[controlType];
	},

	deleteControl : function(model) {

		/*
		 * var controlName = model.get('controlName'); if
		 * (model.get('parentName') != undefined) { controlName =
		 * model.get('parentName') + "." + controlName; }
		 */
		Main.formView.getFormModel().deleteControl(model.get('editName'));

		Main.treeView.getTree().deleteItem(model.get('formTreeNodeId'), true);
	},

	getListOfControlsForSkipRuleControllingField : function() {
		var controls = new Array();
		for ( var key in Main.formView.getFormModel().get(
				'controlObjectCollection')) {
			var control = Main.formView.getFormModel().get(
					'controlObjectCollection')[key];

			switch (control.get('type')) {
			case "numericField":

			case "radioButton":

			case "listBox":

			case "multiselectBox":

			case "comboBox":

			case "multiselectCheckBox":
				controls.push({
					'name' : control.get('controlName'),
					'caption' : control.get('caption'),
					'userDefinedName' : control.get('userDefinedName')
				});
				break;
			default:
			}

			if (control.get('type') == "subForm") {
				for ( var subKey in control.get('subForm').get(
						'controlObjectCollection')) {

					var subControl = control.get('subForm').get(
							'controlObjectCollection')[subKey];
					var subControlName = subControl.get('controlName');
					var _name = control.get('controlName') + "."
							+ subControlName;

					switch (subControl.get('type')) {
					case "numericField":

					case "radioButton":

					case "listBox":

					case "multiselectBox":

					case "comboBox":

					case "multiselectCheckBox":
						var _userDefinedName = control.get('userDefinedName')
								+ "." + subControl.get('userDefinedName');
						controls.push({
							'name' : _name,
							'caption' : subControl.get('caption'),
							'userDefinedName' : _userDefinedName
						});
						break;
					default:
					}

				}
			}
		}
		return controls;
	},

	getListOfCurrentNumericControls : function(isNameCaptionPair) {
		var controls = new Array();
		for ( var key in Main.formView.getFormModel().get(
				'controlObjectCollection')) {
			var control = Main.formView.getFormModel().get(
					'controlObjectCollection')[key];
			if (control.get('type') == "numericField") {
				if (isNameCaptionPair) {
					controls.push({
						'name' : control.get('controlName'),
						'caption' : control.get('caption'),
						'userDefinedName' : control.get('userDefinedName')
					});
				} else {
					controls.push(control.get('controlName'));
				}
			}

			if (control.get('type') == "subForm") {
				for ( var subKey in control.get('subForm').get(
						'controlObjectCollection')) {

					var subControl = control.get('subForm').get(
							'controlObjectCollection')[subKey];
					var subControlName = subControl.get('controlName');
					var _name = control.get('controlName') + "."
							+ subControlName;

					if (subControl.get('type') == "numericField") {
						if (isNameCaptionPair) {

							var _userDefinedName = control
									.get('userDefinedName')
									+ "." + subControl.get('userDefinedName');
							controls.push({
								'name' : _name,
								'caption' : subControl.get('caption'),
								'userDefinedName' : _userDefinedName
							});
						} else {
							controls.push(_name);
						}
					}
				}
			}
		}
		return controls;
	},

	getListOfUDNForNumericControls : function() {
		var controls = new Array();
		for ( var key in Main.formView.getFormModel().get(
				'controlObjectCollection')) {
			var control = Main.formView.getFormModel().get(
					'controlObjectCollection')[key];

			if (control.get('type') == "numericField") {
				controls.push(control.get('userDefinedName'));
			}

			if (control.get('type') == "subForm") {
				for ( var subKey in control.get('subForm').get(
						'controlObjectCollection')) {

					var subControl = control.get('subForm').get(
							'controlObjectCollection')[subKey];

					if (subControl.get('type') == "numericField") {
						var _userDefinedName = control.get('userDefinedName')
								+ "." + subControl.get('userDefinedName');
						controls.push(_userDefinedName);
					}
				}
			}

		}
		return controls;
	},

	getListOfCurrentControls : function(isNameCaptionPair) {
		var controls = new Array();
		for ( var key in Main.formView.getFormModel().get(
				'controlObjectCollection')) {
			var control = Main.formView.getFormModel().get(
					'controlObjectCollection')[key];

			if (isNameCaptionPair) {
				controls.push({
					'name' : control.get('controlName'),
					'caption' : control.get('caption') + " ("
							+ control.get('userDefinedName') + ")",
					'userDefinedName' : control.get('userDefinedName')
				});
			} else {
				controls.push(control.get('controlName'));
			}

			if (control.get('type') == "subForm") {
				for ( var subKey in control.get('subForm').get(
						'controlObjectCollection')) {

					var subControl = control.get('subForm').get(
							'controlObjectCollection')[subKey];
					var subControlName = subControl.get('controlName');
					var _name = control.get('controlName') + "."
							+ subControlName;

					if (isNameCaptionPair) {
						var _userDefinedName = control.get('userDefinedName')
								+ "." + subControl.get('userDefinedName');

						controls.push({
							'name' : _name,
							'caption' : subControl.get('caption') + " ("
									+ subControl.get('userDefinedName') + ")",
							'userDefinedName' : _userDefinedName
						});

					} else {
						controls.push(_name);
					}
				}
			}
		}
		return controls;
	},

	getListOfPvNameValuePairs : function(control) {
		var pvList = new Array();
		for ( var key in control.get('pvs')) {
			pvList.push({
				'name' : control.get('pvs')[key].value,
				'caption' : control.get('pvs')[key].value
			});
		}
		return pvList;
	},

	getCaptionFromControlName : function(controlName) {

		if (controlName != undefined && controlName != null)
			return ControlBizLogic.getControlFromControlName(controlName).get(
					'caption');
		else
			return null;
	},

	getUDNFromControlName : function(controlName) {

		if (controlName != undefined && controlName != null)
			return ControlBizLogic.getControlFromControlName(controlName).get(
					'userDefinedName');
		else
			return null;
	},

	getControlTypeFromControlName : function(controlName) {

		if (controlName != undefined && controlName != null)
			return ControlBizLogic.getControlFromControlName(controlName).get(
					'type');
		else
			return null;
	},

	getControlFromControlName : function(controlName) {
		if (controlName != undefined && controlName != null
				&& controlName != "") {
			var controlNames = controlName.split(".");
			if (controlNames.length == 1) {
				if (Main.formView.getFormModel().get('controlObjectCollection')[controlName] != undefined) {
					return Main.formView.getFormModel().get(
							'controlObjectCollection')[controlName];
				}
			} else {
				if (Main.formView.getFormModel().get('controlObjectCollection')[controlNames[0]]
						.get('subForm').get('controlObjectCollection')[controlNames[1]] != undefined) {
					return Main.formView.getFormModel().get(
							'controlObjectCollection')[controlNames[0]].get(
							'subForm').get('controlObjectCollection')[controlNames[1]];
				}
			}
		}
	},

	createControlNode : function(name, controlName, type, model, copyControl) {

		// Check for selected control type, if its subform then add to it else
		// add to main form.
		var selectedNodeId = Main.treeView.getTree().getSelectedItemId();
		var selectedNodeControlType = Main.treeView.getTree().getUserData(
				selectedNodeId, "controlType");
		var selectedNodeControlName = Main.treeView.getTree().getUserData(
				selectedNodeId, "controlName");
		var status = "save";

		if (selectedNodeId == undefined || selectedNodeId == "") {
			// no node has been selected, hence add it to main form
			var editModel = Main.formView.getFormModel().getControl(
					model.get('controlName'));
			if (editModel == undefined) {
				var id = ControlBizLogic.createTreeNode(1, name, controlName,
						type);
				model.set({
					formTreeNodeId : id
				});
			}

			Main.formView.getFormModel().editControl(model.get('controlName'),
					model);

		} else {
			if (selectedNodeControlType == "subForm") {
				// add it to the sub form
				if (type == "subForm" || type == "pageBreak") {
					Main.currentFieldView.setErrorMessageHeader();
					$("#messagesDiv")
							.append(
									Utility.messageSpace
											+ "Cannot add a sub form or a page break within another sub form");
					status = "error";
				} else {
					var editModel = Main.formView.getFormModel().getControl(
							selectedNodeControlName + "."
									+ model.get('controlName'));
					if (editModel == undefined) {
						var id = ControlBizLogic.createTreeNode(selectedNodeId,
								name, controlName, type);
						model.set({
							formTreeNodeId : id,
							xPos : GlobalMemory.nodeCounter
						});
					}
					Main.formView.getFormModel().editControl(
							selectedNodeControlName + "."
									+ model.get('controlName'), model);

				}
			} else {
				// add it to the main form
				var editModel = Main.formView.getFormModel().getControl(
						model.get('controlName'));
				if (editModel == undefined) {
					var id = ControlBizLogic.createTreeNode(1, name,
							controlName, type);
					model.set({
						formTreeNodeId : id
					});
				}

				Main.formView.getFormModel().editControl(
						model.get('controlName'), model);

			}

		}
		GlobalMemory.nodeCounter++;
		if (copyControl == true) {
			this.copySubForm(model);
		}

		return status;
	},

	createTreeNode : function(parentId, name, controlName, type) {
		var id = 10 + GlobalMemory.nodeCounter;

		Main.treeView.getTree().insertNewChild(parentId, id, name, 0, 0, 0, 0,
				"CHILD,CHECKED");

		Main.treeView.getTree().setUserData(id, "controlName", controlName);
		Main.treeView.getTree().setUserData(id, "controlType", type);
		if (type == "pageBreak") {
			Main.treeView.getTree().setItemStyle(id,
					"font-weight:bold; font-style:italic; font-color:#505050;");
		}

		return id;
	},

	copySubForm : function(model) {

		/*
		 * var shortCode = Utility.getShortCode(model.get('type')); model.set({
		 * controlName : shortCode }); GlobalMemory.nodeCounter++;
		 */

		if (model.get('type') == "subForm") {

			// update controls and set their attributes
			var subForm = new Models.Form(model.get('subForm').toJSON());
			var subFrm = model.get('subForm');

			for ( var key in subFrm.get('controlObjectCollection')) {

				var subFormControl = new Models.Field(subFrm
						.get('controlObjectCollection')[key].toJSON());
				var _shortCode = Utility.getShortCode(subFormControl
						.get('type'));
				subFormControl.set({
					controlName : _shortCode
				});
				GlobalMemory.nodeCounter++;

				var _editName = model.get('controlName') + "."
						+ subFormControl.get('controlName');
				var _treeDisplayName = subFormControl.get('caption') + "("
						+ subFormControl.get('userDefinedName') + ")";

				subFormControl.set({
					editName : _editName
				});

				var id = ControlBizLogic.createTreeNode(model
						.get('formTreeNodeId'), _treeDisplayName,
						subFormControl.get('controlName'), subFormControl
								.get('type'));

				subFormControl.set({
					formTreeNodeId : id
				});

				delete subForm.get('controlObjectCollection')[key];
				subForm.get('controlObjectCollection')[_shortCode] = subFormControl;
				subForm.get('controlsOrder').push(_shortCode);
				var _subForm = subForm;
				GlobalMemory.nodeCounter++;
			}
			model.set({
				subForm : _subForm
			});
		}
	},

	/*
	 * Generate the next sequence number by retrieving the max sequence number
	 * in the model and incrementing it by 1
	 */
	getNextSequenceNumber : function() {
		var sequenceNumber = null;
		if (Main.formView.getFormModel().get('controlObjectCollection') != null) {
			sequenceNumber = 0;
			for ( var key in Main.formView.getFormModel().get(
					'controlObjectCollection')) {
				var tempSequenceNum = Main.formView.getFormModel().get(
						'controlObjectCollection')[key].get('sequenceNumber');
				if (tempSequenceNum > sequenceNumber) {
					sequenceNumber = tempSequenceNum;
				}
			}
		}

		return sequenceNumber == null ? 0 : (sequenceNumber + 1);
	},

	/*
	 * update pv file name
	 */
	addUploadedPvFileNameToCurrentModel : function(fileLocation) {
		Main.currentFieldView.getModel().set({
			pvFile : fileLocation
		});
	},

	/*
	 * Handler for form tree node click
	 */
	formTreeNodeClickHandler : function(id) {
		// De select selected control

		// selected node is a form
		if (id == 1) {
			Main.mainTabBarView.selectTab("summaryTab");
		}
		Utility.resetCarouselControlSelect();

		// Show the control tab if not selected
		Main.mainTabBarView.selectTab("controlTab");

		if (Main.currentFieldView != null) {
			// Erase the existing view
			Main.currentFieldView.destroy();
		}

		// Get the relevant model
		var fieldModel = ControlBizLogic.getSelectedModelFromTree(id);

		ControlBizLogic.populateViewWithControl(fieldModel);
		// Select the carousel's control #FFFFFF
		Main.mainTabBarView.highlightSelectedControlType();

		// enable delete button of the selected control.
		Main.currentFieldView.enableDisableButton("delete", false);
		Main.currentFieldView.enableDisableButton("copy", false);
		Main.currentFieldView.setSubmitCaptionToUpdate();

		return true;
	},

	createCopyControl : function(fieldModel) {

		if (Main.currentFieldView != null) {
			// Erase the existing view
			Main.currentFieldView.destroy();
		}
		var control = new Models.Field(fieldModel.toJSON());
		var shortCode = Utility.getShortCode(control.get('type'));
		control.set({
			editName : undefined,
			formTreeNodeId : undefined,
			controlName : shortCode,
			caption : undefined,
			copy : true,
			userDefinedName : undefined
		});
		GlobalMemory.nodeCounter++;
		this.populateViewWithControl(control);
	},

	populateViewWithControl : function(fieldModel) {
		// Create a view with the relevant model
		Main.currentFieldView = Views.showControl('controlContainer',
				fieldModel);

		// Populate pv grid
		// reset the pv counter
		ControlBizLogic.populatePvViewForControl(fieldModel);

	},

	populatePvViewForControl : function(fieldModel) {
		switch (fieldModel.get('type')) {
		case "radioButton":

		case "listBox":

		case "multiselectBox":

		case "comboBox":

		case "multiselectCheckBox":
			// show pv data in grid
			ControlBizLogic.showPvDataForCurrentControl(fieldModel);
			break;
		default:
		}
	},

	getSelectedModelFromTree : function(id) {
		var parentId = Main.treeView.getTree().getParentId(id);
		var controlName = Main.treeView.getTree()
				.getUserData(id, "controlName");

		var controlType = Main.treeView.getTree()
				.getUserData(id, "controlType");
		var parentControlType = Main.treeView.getTree().getUserData(parentId,
				"controlType");

		var parentControlName = null;
		var modifiedControlName = controlName;
		if (parentControlType == "subForm") {
			parentControlName = Main.treeView.getTree().getUserData(parentId,
					"controlName");
			modifiedControlName = parentControlName + "." + modifiedControlName;
		}

		var control = Main.formView.getFormModel().getControl(
				modifiedControlName);

		// set parent name if it is a sub form's control
		if (parentControlName != null) {
			control.set({
				parentName : parentControlName
			});
		}
		// set tree node id
		control.set({
			treeNodeId : id
		});
		return control;
	},

	showPvDataForCurrentControl : function(model) {
		GlobalMemory.pvCounter = 0;
		// iterate through the pv list
		var defaultPv = model.get('defaultPv');
		for ( var cntr in model.get('pvs')) {
			// get the i'th pv
			var pv = model.get('pvs')[cntr];
			// add the i'th pv
			var rowId = cntr;
			var defaultPvRadio = "<input type='radio' name='defaultPv' value='"
					+ rowId + "'>";
			Main.currentFieldView.getPvGrid().addRow(
					rowId,
					[ pv.value, pv.numericCode, pv.definition,
							pv.definitionSource, pv.conceptCode,
							defaultPvRadio, "saved" ]);
			if (defaultPv != undefined) {
				if (pv.value == defaultPv.value) {

					$('input:radio[name="defaultPv"][value="' + rowId + '"]')
							.attr('checked', true);
				}
			}
			ControlBizLogic.initDefaultPv();
			// increment the pv counter
			GlobalMemory.pvCounter++;
		}
	},

	/*
	 * Create an empty control for editing
	 */
	createControl : function(controlType) {
		var shortCode = Utility.getShortCode(controlType);
		var controlModel = new Models.Field({
			type : controlType,
			pvs : {},
			sequenceNumber : ControlBizLogic.getNextSequenceNumber(),
			controlName : shortCode
		});
		if (controlType == "pageBreak") {
			controlModel.set({
				userDefinedName : shortCode
			});
		}
		return controlModel;
	},

	//

	/*
	 * Handler for "onSelect" event of tab
	 */

	csdControlsTabSelectHandler : function(id) {
		if (id != "designMode") {
			DesignModeBizLogic.populateControlPositions();
		}
		if (id == "designMode") {
			DesignModeBizLogic.loadMatrixWithControls(Main.formView
					.getFormModel().get('controlObjectCollection'),
					Main.formView.getFormModel().get('controlsOrder'));
			DesignModeBizLogic.populateLayoutGrid();
			/*
			 * var controlData = {}; var controlOrder = new Array();
			 * 
			 * for(var i = 0; i < 5; i++){ var control = new Models.Field(); var
			 * controlName = 'ST' + i; control.set({sequenceNumber : 3 + i, xPos :
			 * 1, controlName : controlName}); controlData[controlName] =
			 * control; controlOrder.push(controlName); }
			 * controlData['ST2'].set({sequenceNumber: 4, xPos : 5});
			 * 
			 * DesignModeBizLogic.loadMatrixWithControls(controlData,
			 * controlOrder); DesignModeBizLogic.populateLayoutGrid();
			 */

			// Routers.populateDesignModeTab();
		} else if (id == "previewTab") {
			Main.formView.loadModelInSessionForPreview();
		}

		else if (id == "advancedControlPropertiesTab") {
			GlobalMemory.skipRuleId = null;
			Main.advancedControlsView.clearMessage();
			Main.formView.loadModelInSession();
			AdvancedControlPropertiesBizLogic.refreshListsWithControls();
			Main.advancedControlsView.refreshFormulaField();
			$('#availableFields1').prop(
					'title',
					ControlBizLogic.getCaptionFromControlName($(
							'#availableFields1').val()));

		}
	},

	isUserDefinedNameUnique : function(controlModel) {
		var userDefinedName = controlModel.get('userDefinedName');
		var controlName = controlModel.get('controlName');
		var isUnique = true;
		var controlCollection = Main.formView.getFormModel().get(
				'controlObjectCollection');
		for ( var key in controlCollection) {
			var control = controlCollection[key];
			if (userDefinedName == control.get('userDefinedName')) {
				if (controlName != control.get('controlName')) {
					isUnique = false;
					break;
				}
			}
			if (control.get('type') == "subForm") {
				var subFormControls = control.get('subForm').get(
						'controlObjectCollection');
				for ( var subFormControlKey in subFormControls) {
					if (userDefinedName == subFormControls[subFormControlKey]
							.get('userDefinedName')) {
						if (controlName != subFormControls[subFormControlKey]
								.get('controlName')) {
							isUnique = false;
							break;
						}
					}
				}
			}
		}
		return isUnique;
	},

	validateUserDefinedName : function(controlModel) {
		var isUnique = ControlBizLogic.isUserDefinedNameUnique(controlModel);
		var isValid = Utility.checkNameForCorrectness(controlModel
				.get('userDefinedName'));
		return (isUnique && isValid);
	},

	isControlNameUniqueInForm : function(control) {

		// get selected control's properties to distinguish between main form
		// controls and sub form controls
		var selectedNodeId = Main.treeView.getTree().getSelectedItemId();
		var selectedNodeControlType = Main.treeView.getTree().getUserData(
				selectedNodeId, "controlType");
		var selectedNodeControlName = Main.treeView.getTree().getUserData(
				selectedNodeId, "controlName");
		var isUnique = false;

		if (control.has('editName')) {
			// edit case
			var editName = control.get('editName').split(".");
			var controlName = control.get('controlName');
			// edit control
			if (editName.length > 1) {
				controlName = editName[0] + "." + controlName;
			}
			if (Main.formView.getFormModel().getControl(controlName) == undefined) {
				isUnique = true;
			}

		} else {
			// add case
			if ((selectedNodeControlType == undefined && selectedNodeControlName == undefined)
					|| (selectedNodeControlType == "" && selectedNodeControlName == "")) {
				if (selectedNodeControlType == "subForm") {
					// subForm control add case

					if (Main.formView.getFormModel().getControl(
							selectedNodeControlName + "."
									+ control.get('controlName')) == undefined) {
						isUnique = true;
					}

				} else {
					if (Main.formView.getFormModel().getControl(
							control.get('controlName')) == undefined) {
						isUnique = true;
					}
				}
			} else {
				if (Main.formView.getFormModel().getControl(
						control.get('controlName')) == undefined) {
					isUnique = true;
				}
			}
		}
		return isUnique;

	},
	initDefaultPv : function() {

		$('input:radio[name="defaultPv"]').change(
				function() {
					var _defaultPv = Main.currentFieldView.getModel()
							.get('pvs')[$(this).val()];
					Main.currentFieldView.getModel().set({
						defaultPv : _defaultPv
					});
				});

	},

	pvGridHandler : function(rId, cInd, nValue) {
		// alert(rId + ' ' + cInd + ' ' +
		// nValue);
		if (Main.currentFieldView.getModel().get('pvs')[rId] == undefined) {
			// not decided
		} else {
			var isValid = true;
			// validation for numeric value
			switch (cInd) {
			case 1:
				/*
				 * if (isNaN(nValue)) { isValid = false;
				 * Main.currentFieldView.setErrorMessageHeader();
				 * 
				 * $("#messagesDiv").append( Utility.messageSpace + "! " +
				 * "Numeric code should be numeric." + "<br>");
				 * Main.currentFieldView.getPvGrid.cellById(rId, cInd)
				 * .setValue(''); }
				 */
				break;
			default:
			}

			if (isValid) {
				Main.currentFieldView.getModel().get('pvs')[rId] = Main.currentFieldView
						.setpvPropertyBasedOnIndex(cInd, Main.currentFieldView
								.getModel().get('pvs')[rId], nValue);
			}
		}

	},

	changeControl : function(control, newType) {

		control.set({
			type : newType,
			defaultValue : undefined
		});

		var cntrolView = Utility.addFieldHandlerMap[newType](control, true,
				'controlContainer');

		return cntrolView;
	},

	changeCurrentControlAndDisplay : function() {
		// change and show the new type
		Main.currentFieldView.populateFields();
		var currentModel = Main.currentFieldView.getModel();

		// alert(JSON.stringify(currentModel.toJSON()));
		Main.currentFieldView.destroy();
		Main.currentFieldView = ControlBizLogic.changeControl(currentModel, $(
				"#newControlType").val());
		// ControlBizLogic.populateViewWithControl(currentModel);
		ControlBizLogic.populatePvViewForControl(Main.currentFieldView
				.getModel());

		// edit the control if it has
		// been already added

		if (currentModel.has('editName')) {
			Main.formView.getFormModel().editControl(
					currentModel.get('editName'), currentModel);
			Main.currentFieldView.setSubmitCaptionToUpdate();
			Main.currentFieldView.enableDisableButton("delete", false);
			Main.currentFieldView.enableDisableButton("copy", false);
			Main.treeView.getTree().setUserData(
					currentModel.get('formTreeNodeId'), "controlType",
					currentModel.get('type'));
		}
		// change the carousel
		Utility.resetCarouselControlSelect();
		Main.mainTabBarView.highlightSelectedControlType();
	},

	populatePermissibleControlTypes : function(controlType, selectDropDownId) {
		var permTypes = ControlBizLogic.getPermissibleControlTypes(controlType);
		if (permTypes == undefined) {
		} else {
			$("#" + selectDropDownId).empty();
			for ( var cntr = 0; cntr < permTypes.length; cntr++) {
				var type = permTypes[cntr];
				var label = ControlBizLogic.getLabelByType(type);
				$("#" + selectDropDownId).append($("<option/>", {
					value : type,
					text : label,
					title : label
				}));
			}
		}

	}

}