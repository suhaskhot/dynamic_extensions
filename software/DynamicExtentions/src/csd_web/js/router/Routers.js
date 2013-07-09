var Routers = {

	formEventsRouterPointer : null,
	controlEventsRouterPointer : null,
	updateCachedFormMethod : null,

	updatePVs : function(model) {
		// set pvs
		var updatedModel = model;
		var currPvs = Main.currentFieldView.getModel().get('pvs');
		var pvFileLocation = Main.currentFieldView.getModel().get('pvFile');
		updatedModel.set({
			pvs : new Array(),
			pvFile : pvFileLocation
		});

		for ( var key in currPvs) {
			updatedModel.get('pvs').push(currPvs[key]);
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
				controlObjectCollection : new Array()
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
				this.updateCachedControlOfMainForm(model);
				isUpdate = "save"
			} else {

				if (selectedModel.get('type') == "subForm") {

					if (model.get('type') == "subForm") {
						// alert('Cannot add a sub form within another sub
						// form');
						isUpdate = "error";
					} else {
						isUpdate = this.updateCachedControlOfSubForm(model,
								selectedNodeText);
					}
				} else {
					this.updateCachedControlOfMainForm(model);
					isUpdate = "update"
				}
			}
		} else {
			isUpdate = this.updateCachedControlOfMainForm(model);
		}

		return isUpdate;

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

	loadPreview : function() {
		$.ajax({
			url : "csdApi/form/preview"
		}).done(
				function(data) {
					// alert(data);
					var div = $('#previewFrame').contents().find(
							'#csd_preview_content');
					div.html(data);
					$("#formWaitingImage").hide();

				});
	},

	getListOfCurrentControls : function() {
		var controls = new Array();
		for ( var key in Main.formView.getFormModel().get(
				'controlObjectCollection')) {
			var controlName = Main.formView.getFormModel().get(
					'controlObjectCollection')[key].get('controlName');
			controls.push(controlName);
		}
		return controls;
	},

	// Populate the given select box with the latest control names
	populateSelectBoxWithControlNames : function(selectTagId) {
		$("#" + selectTagId).empty();
		var controls = this.getListOfCurrentControls();
		for ( var key in controls) {
			var controlName = controls[key];
			$("#" + selectTagId).append($("<option/>", {
				value : controlName,
				text : controlName
			}));
		}
	},

	// Refresh lists with current controls.

	refreshListsWithControls : function() {
		this.populateSelectBoxWithControlNames("availableFields1");
	},

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
			var advancedProperties = new Views.AdvancedPropertiesTabView({
				el : $('#advancedControlProperties'),
				model : null
			});
			Routers.refreshListsWithControls();

		}
	},

	/*
	 * Handler for form tree node click
	 */
	formTreeNodeClickHandler : function(id) {
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
		// Populate pv grid
		// reset the pv counter

		switch ((Main.currentFieldView.getModel().get('type'))) {
		case "radioButton":

		case "listBox":

		case "multiselectBox":

		case "multiselectCheckBox":
			Main.pvCounter = 0;
			// iterate through the pv list
			for ( var cntr = 0; cntr < Main.currentFieldView.getModel().get(
					'pvs').length; cntr++) {
				// get the i'th pv
				var pv = Main.currentFieldView.getModel().get('pvs')[cntr];
				// add the i'th pv
				var rowId = Main.currentFieldView.getModel().get('controlName')
						+ Main.pvCounter;
				Main.currentFieldView.getPvGrid().addRow(
						rowId,
						pv.value + ',' + pv.numericCode + ',' + pv.definition
								+ ',' + pv.definitionSource + ','
								+ pv.conceptCode + ',' + "saved");
				// increment the pv counter
				Main.pvCounter++;
			}
			break;
		default:
		}

		return true;
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
	 * Create an empty control for editing
	 */
	createControl : function(controlType) {

		if (Main.currentFieldView != null) {
			// Erase the existing view
			Main.currentFieldView.destroy();
		}

		var controlModel = new Models.Field({
			type : controlType,
			pvs : new Array(),
			sequenceNumber : this.getNextSequenceNumber(),
			controlName : Utility.getShortCode(controlType)
		});
		Main.sequenceNumCntr++;

		Main.currentFieldView = Utility.addFieldHandlerMap[controlType](
				controlModel, true, 'controlContainer');
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
	 * Control Router. Used to perform actions initiated a Control's User
	 * Interface
	 */
	ControlRouter : Backbone.Router.extend({
		routes : {
			"createCachedControl/:name/control:controlName" : "create"
		},
		create : function() {
			alert("Domo");
		}

	}),

	/*
	 * Used to create control Node in the tree
	 */
	createControlNode : function(name, controlName) {

		var selectedNodeText = Main.treeView.getTree().getUserData(
				Main.treeView.getTree().getSelectedItemId(), "controlName");

		// Selected node text should come from user data of selected
		// node
		if (Main.formView.getFormModel().get('controlObjectCollection') == undefined) {
			Main.formView.getFormModel().set({
				controlObjectCollection : new Array()
			});
		}
		var selectedControl = Main.formView.getFormModel().get(
				'controlObjectCollection')[selectedNodeText];
		var existingControl = Main.formView.getFormModel().get(
				'controlObjectCollection')[controlName];

		if (Main.treeView.getTree().getSelectedItemId() != "") {
			if (selectedControl == undefined) {
				this.createTreeNode(name, true, controlName);
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
									controlObjectCollection : new Array()
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
								var message = trail
										+ "Cannot add a sub form within another sub form";
								$("#messagesDiv").append(message);
							} else {

								this.createTreeNode(name, true, controlName);
							}
						} else {

							this.createTreeNode(name, false, controlName);
						}

					}

				} else {

					// Selected node is not a sub form so add or
					// update a
					// node to
					// the main form

					if (existingControl == undefined) {
						// Add a control
						this.createTreeNode(name, true, controlName);
					}
				}
			}
		} else {
			// Nothing is selected from the tree
			if (existingControl == undefined) {
				// Add a control
				this.createTreeNode(name, true, controlName);
			}
		}

	},

	createTreeNode : function(name, isMainForm, controlName) {
		var id = 10 + Main.nodeCounter;

		if (isMainForm) {
			Main.treeView.getTree().insertNewChild(1, id, name, 0, 0, 0, 0,
					"TOP,CHILD,CHECKED");
		} else {
			Main.treeView.getTree().insertNewChild(
					Main.treeView.getTree().getSelectedItemId(), id, name, 0,
					0, 0, 0, "TOP,CHILD,CHECKED");
		}
		Main.treeView.getTree().setUserData(id, "controlName", controlName);
		Main.nodeCounter++;
	},

	/*
	 * Form Router. Used to perform actions initiated a Container's User
	 * Interface
	 */

	FormRouter : Backbone.Router
			.extend({
				routes : {
					"loadCachedForm/:id" : "loadForm"

				},
				initialize : function() {
					// Routers.updateCachedFormMethod = this.updateFormAndUI;
					Routers.updateCachedFormMethod = this.updateFormAndUI;
				},

				dummy : function() {
				},

				loadForm : function(id) {
					$("#formWaitingImage").show();
					if (Main.formView == null) {
						Main.formView = Views.showForm('formTab',
								new Models.Form());

					}
					Main.formView.getFormModel().fetch({
						url : 'csdApi/form/' + id,
						success : this.loadFormSuccessHandler
					});
				},

				updateId : function(nId) {
					Main.formView.getFormModel().set({
						id : nId,
						status : saved
					});
				},

				updateFormUI : function(model) {
					Main.formView.render();
					Main.formView.getFormModel().set({
						controlObjectCollection : new Array()
					});
				},

				updateTreeWithFormName : function(model) {
					Main.treeView.getTree().setItemText(1,
							model.get('caption'), 'Form\'s caption');
					Main.treeView.getTree().deleteChildItems(1);
				},

				updateFormAndUI : function(model) {
					Routers.formEventsRouterPointer.updateFormUI(model);
					Routers.formEventsRouterPointer
							.updateTreeWithFormName(model);
					Routers.formEventsRouterPointer
							.loadControlsInModelAndTree(model);
				},

				loadFormSuccessHandler : function(model, response) {
					Routers.formEventsRouterPointer.updateFormAndUI(model);
					$("#formWaitingImage").hide();
				},

				loadControlsInModelAndTree : function(model) {
					// Need to add code to populate sub forms
					for ( var cntr = 0; cntr < model.get('controlCollection').length; cntr++) {

						var control = new Models.Field(model
								.get('controlCollection')[cntr]);
						var displayLbl = control.get('caption') + " ("
								+ control.get('controlName') + ")";
						this.populateTreeWithControlNodes(control
								.get('controlName'), displayLbl);
						var parentId = Main.nodeCounter - 1;
						if (control.get('type') == "subForm") {
							var subFrm = new Models.Form(control.get('subForm'));
							subFrm.set({
								controlObjectCollection : new Array()
							});
							for ( var subCntr = 0; subCntr < subFrm
									.get('controlCollection').length; subCntr++) {

								var subControl = new Models.Field(subFrm
										.get('controlCollection')[subCntr]);

								var updatedControl = Utility.addFieldHandlerMap[subControl
										.get('type')](subControl, false,
										'controlContainer');

								subFrm.get('controlObjectCollection')[subControl
										.get('controlName')] = updatedControl;
								var displayLabel = subControl.get('caption')
										+ " (" + subControl.get('controlName')
										+ ")";

								Main.treeView.getTree().insertNewChild(
										parentId, Main.nodeCounter,
										displayLabel, 0, 0, 0, 0,
										"SELECT,CALL,TOP,CHILD,CHECKED");
								Main.treeView.getTree().setUserData(
										Main.nodeCounter, "controlName",
										subControl.get('controlName'));
								Main.nodeCounter++;

							}
							// control.set({
							// subForm : subFrm
							// });
						}

						this.populateModelWithControls(control, subFrm);

					}
				},

				populateModelWithControls : function(control, subFrm) {
					var updatedControl = Utility.addFieldHandlerMap[control
							.get('type')](control, false, 'controlContainer');
					if (control.get('type') == "subForm") {
						updatedControl.set({
							subForm : subFrm
						});

					}
					Main.formView.getFormModel().get('controlObjectCollection')[control
							.get('controlName')] = updatedControl;
				},

				populateTreeWithControlNodes : function(controlName,
						displayLabel) {
					Main.treeView.getTree().insertNewChild(1, Main.nodeCounter,
							displayLabel, 0, 0, 0, 0,
							"SELECT,CALL,TOP,CHILD,CHECKED");
					Main.treeView.getTree().setUserData(Main.nodeCounter,
							"controlName", controlName);

					Main.nodeCounter++;
				}
			}),

	/*
	 * Router initialization
	 */
	initializeRouters : function() {
		this.controlEventsRouterPointer = new this.ControlRouter;
		this.formEventsRouterPointer = new this.FormRouter;
		Backbone.history.start();
	},

	designModeOnBeforeDragEvent : function() {

		Main.designModeViewPointer
				.getGridObject()
				.attachEvent(
						"onBeforeDrag",
						function(sId, sInd) {
							Main.designModeViewPointer.getGridObject().rowToDragElement = function(
									id) {
								var text = Main.designModeViewPointer
										.getGridObject().cells(sId, sInd)
										.getValue();
								if (text == "") {
									text = "Empty";
								}
								return text;
							}
							return true;
						});
	},

	designModeOnDragEvent : function() {
		Main.designModeViewPointer
				.getGridObject()
				.attachEvent(
						"onDrag",
						function(sId, tId, sObj, tObj, sInd, tInd) {
							try {
								var sourceCell = Main.designModeViewPointer
										.getGridObject().cells(sId, sInd);
								var targetCell = Main.designModeViewPointer
										.getGridObject().cells(tId, tInd);
								// alert(sourceCell.getValue());
								if (sourceCell.getValue() != "") {

									var targetValue = targetCell.getValue();
									targetCell.setValue(sourceCell.getValue());
									var rowId = Main.designModeViewPointer
											.getGridObject().getRowIndex(tId);
									Main.designModeViewPointer
											.updateControlObjectCollection(
													sourceCell.getValue(),
													tInd, rowId + 1);
									sourceCell.setValue('');

									var sourceValue = '';
									var seqNo = '';
									while (targetValue != '') {
										seqNo = rowId + 1;
										if (Main.designModeViewPointer
												.getGridObject()
												.doesRowExist(
														Main.designModeViewPointer
																.getGridObject()
																.getRowId(seqNo)) == false) {
											var newId = (new Date()).valueOf();
											Main.designModeViewPointer
													.getGridObject().addRow(
															newId, "");
										}
										if (Main.designModeViewPointer
												.getGridObject().cellByIndex(
														seqNo, tInd).getValue() != '') {
											var sourceValue = Main.designModeViewPointer
													.getGridObject()
													.cellByIndex(seqNo, tInd)
													.getValue();
											Main.designModeViewPointer
													.getGridObject()
													.cellByIndex(seqNo, tInd)
													.setValue(targetValue);
											Main.designModeViewPointer
													.updateControlObjectCollection(
															targetValue, tInd,
															seqNo + 1);
											targetValue = sourceValue;
										} else {
											Main.designModeViewPointer
													.getGridObject()
													.cellByIndex(seqNo, tInd)
													.setValue(targetValue);
											Main.designModeViewPointer
													.updateControlObjectCollection(
															targetValue, tInd,
															seqNo + 1);
											break;
										}
										rowId++;
									}

									Main.designModeViewPointer.getGridObject().rowToDragElement = function(
											id) {
										var text = Main.designModeViewPointer
												.getGridObject().cells(sId,
														sInd).getValue();
										return text;
									}
								}
							} catch (e) {
							}
						});
	},

	populateDesignModeTab : function() {
		if (Main.formView.getFormModel()
				&& Main.formView.getFormModel().get('controlObjectCollection') != null) {
			for ( var index in Main.formView.getFormModel().get(
					'controlObjectCollection')) {
				var xPos = Main.formView.getFormModel().get(
						'controlObjectCollection')[index].get("xPos");
				// -1 because db doesn't accept zero and grid zero based
				var seqNumber = Main.formView.getFormModel().get(
						'controlObjectCollection')[index].get("sequenceNumber") - 1;
				var name = Main.formView.getFormModel().get(
						'controlObjectCollection')[index].get("controlName");
				// alert("xpos= " + xPos + " seqNumber = " + seqNumber + " name=
				// " + name + " gridRows = " + layoutGrid.getRowsNum() + "
				// gridColNum = " +layoutGrid.getColumnsNum());

				// this will add columns if not present
				while (Main.designModeViewPointer.getGridObject()

				.getColumnsNum() - 1 < xPos) {
					var colNum = Main.designModeViewPointer.getGridObject()
							.getColumnsNum();
					Main.designModeViewPointer.getGridObject().insertColumn(
							colNum, '', 'ro', '300', '', 'center');
				}
				// this will add row if not present
				while (Main.designModeViewPointer.getGridObject().getRowsNum() - 1 < seqNumber) {
					var newId = (new Date()).valueOf();
					Main.designModeViewPointer.getGridObject()
							.addRow(newId, "");
				}

				Main.designModeViewPointer.getGridObject().cellByIndex(
						seqNumber, xPos).setValue(name);
			}
		}
	}
}