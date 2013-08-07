/**
 * 
 */

var ControlBizLogic = {

	updatePVs : function(model) {
		// set pvs
		var updatedModel = model;
		var currPvs = Main.currentFieldView.getModel().get('pvs');
		var pvFileLocation = Main.currentFieldView.getModel().get('pvFile');
		updatedModel.set({
			pvs : {},
			pvFile : pvFileLocation
		});
		var cntr = 0;
		for ( var key in currPvs) {
			updatedModel.get('pvs')[cntr] = currPvs[key];
			cntr++;
		}
		return updatedModel;
	},

	/*
	 * Used to update a control with pvs and model changes
	 */
	updateCachedControl : function(model) {
		var isUpdate = "save";

		if (Main.formView.getFormModel().get('controlObjectCollection') == undefined) {
			Main.formView.getFormModel().set({
				controlObjectCollection : {}
			});
		}

		// check if a tree node is selected, if it is then update the subform(if
		// the selected node represents a subform) else update the main form
		if (Main.treeView.getTree().getSelectedItemId() != "") {
			var selectedNodeText = Main.treeView.getTree().getUserData(
					Main.treeView.getTree().getSelectedItemId(), "controlName");

			var selectedModel = Main.formView.getFormModel().get(
					'controlObjectCollection')[selectedNodeText];
			if (selectedModel == undefined) {
				ControlBizLogic.updateCachedControlOfMainForm(model);
				isUpdate = "save"
			} else {

				if (selectedModel.get('type') == "subForm") {

					if (model.get('type') == "subForm") {
						// alert('Cannot add a sub form within another sub
						// form');
						isUpdate = "error";
					} else {
						isUpdate = ControlBizLogic.updateCachedControlOfSubForm(model,
								selectedNodeText);
					}
				} else {
					ControlBizLogic.updateCachedControlOfMainForm(model);
					isUpdate = "update"
				}
			}
		} else {
			isUpdate = ControlBizLogic.updateCachedControlOfMainForm(model);
		}

		return isUpdate;

	},

	deleteControl : function(model) {
		var selectedItemId = Main.treeView.getTree().getSelectedItemId();
		var parentItemId = Main.treeView.getTree().getParentId(selectedItemId);

		// get selected item.
		if (parentItemId == 1) {

			// mark the form's attribute as deleted
			if (Main.formView.getFormModel().get('controlObjectCollection')[model
					.get('controlName')] != undefined) {
				Main.formView.getFormModel().get('controlObjectCollection')[model
						.get('controlName')].set({
					status : "delete"
				});
				if (model.get('type') == "subForm") {
					Main.treeView.getTree().deleteChildItems(selectedItemId);
				}
			}
		} else {
			// it is a sub form's control.
			var subFormControlName = Main.treeView.getTree().getUserData(
					parentItemId, "controlName");

			if (Main.formView.getFormModel().get('controlObjectCollection')[subFormControlName]
					.get('subForm').get('controlObjectCollection')[model
					.get('controlName')] != undefined) {
				Main.formView.getFormModel().get('controlObjectCollection')[subFormControlName]
						.get('subForm').get('controlObjectCollection')[model
						.get('controlName')].set({
					status : "delete"
				});
			}
		}

		var selectedItemName = Main.treeView.getTree().getUserData(
				selectedItemId, "controlName");
		Main.treeView.getTree().deleteItem(selectedItemId, true);
	},

	/*
	 * Used to update a control in the main form
	 */
	updateCachedControlOfMainForm : function(model) {

		var isUpdate = "save";

		if (Main.formView.getFormModel().get('controlObjectCollection')[model
				.get('controlName')] != undefined) {
			delete Main.formView.getFormModel().get('controlObjectCollection')[model
					.get('controlName')];
			isUpdate = "update";
		}
		Main.formView.getFormModel().get('controlObjectCollection')[model
				.get('controlName')] = model;
		return isUpdate;
	},

	/*
	 * Used to update a control in the sub form
	 */
	updateCachedControlOfSubForm : function(model, subFormControlName) {

		var isUpdate = "save";

		if (Main.formView.getFormModel().get('controlObjectCollection')[subFormControlName]
				.get('subForm').get('controlObjectCollection')[model
				.get('controlName')] != undefined) {
			delete Main.formView.getFormModel().get('controlObjectCollection')[subFormControlName]
					.get('subForm').get('controlObjectCollection')[model
					.get('controlName')];
			isUpdate = "update";
		}
		Main.formView.getFormModel().get('controlObjectCollection')[subFormControlName]
				.get('subForm').get('controlObjectCollection')[model
				.get('controlName')] = model;

		return isUpdate;

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
						'caption' : control.get('caption')
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
					if (subControl.get('type') == "numericField") {
						if (isNameCaptionPair) {
							controls.push({
								'name' : control.get('controlName') + "."
										+ subControlName,
								'caption' : control.get('caption')
							});
						} else {
							controls.push(control.get('controlName') + "."
									+ subControlName);
						}
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
							+ control.get('controlName') + ")"
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

					if (isNameCaptionPair) {
						controls.push({
							'name' : control.get('controlName') + "."
									+ subControlName,
							'caption' : subControl.get('caption') + " ("
									+ subControlName + ")"
						});
						
					} else {
						controls.push(control.get('controlName') + "."
								+ subControlName);
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

		if(controlName!=undefined && controlName!=null)
			return ControlBizLogic.getControlFromControlName(controlName).get('caption');
		else
			return null;
	},

	getControlTypeFromControlName : function(controlName) {
		
		if(controlName!=undefined && controlName!=null)
			return ControlBizLogic.getControlFromControlName(controlName).get('type');
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

	createControlNode : function(name, controlName) {

		var selectedNodeText = Main.treeView.getTree().getUserData(
				Main.treeView.getTree().getSelectedItemId(), "controlName");

		// Selected node text should come from user data of selected
		// node
		if (Main.formView.getFormModel().get('controlObjectCollection') == undefined) {
			Main.formView.getFormModel().set({
				controlObjectCollection : {}
			});
		}
		var selectedControl = Main.formView.getFormModel().get(
				'controlObjectCollection')[selectedNodeText];
		var existingControl = Main.formView.getFormModel().get(
				'controlObjectCollection')[controlName];

		if (Main.treeView.getTree().getSelectedItemId() != "") {
			if (selectedControl == undefined) {
				ControlBizLogic.createTreeNode(name, true, controlName);
			} else {
				// Something is selected from the tree

				if (selectedControl.get('type') == "subForm") {
					// Selected node is a sub form
					if (selectedControl.get('subForm').get(
							'controlObjectCollection') == null) {
						// Set control to an empty Array if its not
						// set
						Main.formView.getFormModel().get(
								'controlObjectCollection')[selectedNodeText]
								.get('subForm').set({
									controlObjectCollection : {}
								});
					}
					// see if the control exists
					if (selectedControl.get('subForm').get(
							'controlObjectCollection')[name] == undefined) {
						// The control does not exist so add a node
						// to
						// the sub

						var parentId = Main.treeView.getTree().getParentId(
								Main.treeView.getTree().getSelectedItemId());
						if (Main.currentFieldView.getModel().get('type') == "subForm") {
							if (parentId == 1) {
								var trail = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
								Main.currentFieldView.setErrorMessageHeader();
								$("#messagesDiv")
										.append(
												trail
														+ "Cannot add a sub form within another sub form");

							} else {

								ControlBizLogic.createTreeNode(name, true, controlName);
							}
						} else {

							ControlBizLogic.createTreeNode(name, false, controlName);
						}

					}

				} else {

					// Selected node is not a sub form so add or
					// update a
					// node to
					// the main form

					if (existingControl == undefined) {
						// Add a control
						ControlBizLogic.createTreeNode(name, true, controlName);
					}
				}
			}
		} else {
			// Nothing is selected from the tree
			if (existingControl == undefined) {
				// Add a control
				ControlBizLogic.createTreeNode(name, true, controlName);
			}
		}

	},

	createTreeNode : function(name, isMainForm, controlName) {
		var id = 10 + GlobalMemory.nodeCounter;

		if (isMainForm) {
			Main.treeView.getTree().insertNewChild(1, id, name, 0, 0, 0, 0,
					"CHILD,CHECKED");
		} else {
			Main.treeView.getTree().insertNewChild(
					Main.treeView.getTree().getSelectedItemId(), id, name, 0,
					0, 0, 0, "CHILD,CHECKED");
		}
		Main.treeView.getTree().setUserData(id, "controlName", controlName);
		GlobalMemory.nodeCounter++;
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

		return sequenceNumber == null ? 1 : (sequenceNumber + 1);
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
		Utility.resetCarouselControlSelect();

		if (Main.currentFieldView != null) {
			// Erase the existing view
			Main.currentFieldView.destroy();
		}

		var parentId = Main.treeView.getTree().getParentId(id);
		// Get the relevant model
		var fieldModel = null;

		if (parentId == 1) {
			var controlName = Main.treeView.getTree().getUserData(id,
					"controlName");
			/*
			 * fieldModel = Main.formView.getFormModel().get(
			 * 'controlObjectCollection')[Main.treeView.getTree()
			 * .getItemText(id)];
			 */
			fieldModel = Main.formView.getFormModel().get(
					'controlObjectCollection')[controlName];
		} else {
			// It is a subform control, get the subform and display the control.
			var controlName = Main.treeView.getTree().getUserData(parentId,
					"controlName");
			var subControlName = Main.treeView.getTree().getUserData(id,
					"controlName");

			var subForm = Main.formView.getFormModel().get(
					'controlObjectCollection')[controlName];

			fieldModel = subForm.get('subForm').get('controlObjectCollection')[subControlName];

		}

		// Create a view with the relevant model
		Main.currentFieldView = Views.showControl('controlContainer',
				fieldModel);
		// Show the control tab if not selected
		if (Main.mainTabBarView.getTabBar().getActiveTab() != 'controlTab') {
			Main.mainTabBarView.getTabBar().setTabActive('controlTab');
		}
		// Select the carousel's control #FFFFFF
		var pos = Utility.getControlIndexForCarousel(Main.currentFieldView
				.getModel().get('type'));
		Main.carousel.tinycarousel_move(pos > 8 ? (pos % 8) + 1 : pos);
		$('#' + Main.currentFieldView.getModel().get('type')).css(
				'background-color', '#F0F0F0 ');

		// Populate pv grid
		// reset the pv counter

		switch ((Main.currentFieldView.getModel().get('type'))) {
		case "radioButton":

		case "listBox":

		case "multiselectBox":

		case "multiselectCheckBox":
			GlobalMemory.pvCounter = 0;
			// iterate through the pv list
			for ( var cntr in Main.currentFieldView.getModel().get('pvs')) {
				// get the i'th pv
				var pv = Main.currentFieldView.getModel().get('pvs')[cntr];
				// add the i'th pv
				var rowId = Main.currentFieldView.getModel().get('controlName')
						+ GlobalMemory.pvCounter;
				Main.currentFieldView.getPvGrid().addRow(
						rowId,
						pv.value + ',' + pv.numericCode + ',' + pv.definition
								+ ',' + pv.definitionSource + ','
								+ pv.conceptCode + ',' + "saved");
				// increment the pv counter
				GlobalMemory.pvCounter++;
			}
			break;
		default:
		}
		// enable delete button of the selected control.
		Main.currentFieldView.enableDeleteButton();
		return true;
	},

	/*
	 * Create an empty control for editing
	 */
	createControl : function(controlType) {

		if (Main.currentFieldView != null) {
			// Erase the existing view
			Main.currentFieldView.destroy();
		}

		var controlModel = new Models.Field({
			type : controlType,
			pvs : {},
			sequenceNumber : ControlBizLogic.getNextSequenceNumber(),
			controlName : Utility.getShortCode(controlType)
		});
		GlobalMemory.sequenceNumCntr++;

		Main.currentFieldView = Utility.addFieldHandlerMap[controlType](
				controlModel, true, 'controlContainer');
	},

	//

	/*
	 * Handler for "onSelect" event of tab
	 */

	csdControlsTabSelectHandler : function(id) {
		if (id == "designMode") {
			Main.designModeViewPointer = new Views.DesignMode({
				el : $("#design")
			});
			Routers.designModeOnBeforeDragEvent();
			Routers.designModeOnDragEvent();
			Routers.populateDesignModeTab();
		} else if (id == "previewTab") {
			Main.formView.loadModelInSessionForPreview();
		}

		else if (id == "advancedControlPropertiesTab") {

			Main.advancedControlsView.clearMessage();
			Main.formView.loadModelInSession();
			AdvancedControlPropertiesBizLogic.refreshListsWithControls();
			Main.advancedControlsView.refreshFormulaField();
			$('#availableFields1').prop(
					'title',
					ControlBizLogic.getCaptionFromControlName($('#availableFields1')
							.val()));
			

		}
	}

}