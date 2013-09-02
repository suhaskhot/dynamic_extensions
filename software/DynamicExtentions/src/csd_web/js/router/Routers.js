var Routers = {

	formEventsRouterPointer : null,
	updateCachedFormMethod : null,

	/*
	 * AdvancedPropertiesRouter. Used to perform actions initiated a Control's
	 * User Interface
	 */
	AdvancedPropertiesRouter : Backbone.Router
			.extend({
				routes : {
					"calculatedAttribute/:id/:operation" : "setCalculatedAttrib",
					"skipRules/:id/:operation" : "setSkipRule"
				},
				setCalculatedAttrib : function(id, operation) {
					// just remove for now
					if (operation == "delete") {
						$('#dialogMessageText').html(
								'Do you wish to delete this formula?');
						$('#tempData').val(id);
						GlobalMemory.formulaCounter = 0;
						$("#general-dialog")
								.dialog(
										{
											buttons : {
												Yes : function() {
													AdvancedControlPropertiesBizLogic
															.deleteFormula($(
																	'#tempData')
																	.val());
													Main.advancedControlsView
															.setTableCss('formulaTable');
													$(this).dialog("close");
												},
												No : function() {
													$(this).dialog("close");
												}
											}
										});
						$("#general-dialog").dialog("open");
						Routers.formEventsRouterPointer.navigate('clear', {
							trigger : true
						});
					} else if (operation == "edit") {
						var _formula = $('#' + id).find('td').eq(2).text();
						AdvancedControlPropertiesBizLogic.deleteFormula(id);
						$("#availableFields1").val(id);
						$('#formulaField').val(_formula);
					}
				},
				setSkipRule : function(id, operation) {
					// just remove for now
					Main.advancedControlsView.clearMessage();
					if (operation == "delete") {
						$('#dialogMessageText').html(
								'Do you wish to delete this skip rule?');
						$('#tempData').val(id);
						GlobalMemory.formulaCounter = 0;
						$("#general-dialog")
								.dialog(
										{
											buttons : {
												Yes : function() {
													AdvancedControlPropertiesBizLogic
															.deleteSkipRule($(
																	'#tempData')
																	.val());
													Main.advancedControlsView
															.setTableCss('skipRulesTable');
													$(this).dialog("close");
												},
												No : function() {
													$(this).dialog("close");
												}
											}
										});
						$("#general-dialog").dialog("open");
						Routers.formEventsRouterPointer.navigate('clear', {
							trigger : true
						});
					} else if (operation == "edit") {
						var skipRule = Main.formView.getFormModel().get(
								'skipRules')[id];

						AdvancedControlPropertiesBizLogic.deleteSkipRule(id);
						Main.advancedControlsView.setTableCss('skipRulesTable');
						// set controlling fields
						$("#controllingField").val(
								skipRule.controllingAttributes);
						// set controlled fields
						$('#controlledField')
								.val(skipRule.controlledAttributes);
						$("#controlledField").trigger("liszt:updated");

						// set action
						$('#' + skipRule.action).prop('checked', true).button(
								'refresh');

						// set allOrAny
						$('#' + skipRule.allAny).prop('checked', true).button(
								'refresh');
						;
						// enable pvSubSet menu
						if (skipRule.action == "pvSubSet") {
							Main.advancedControlsView
									.populatePvSubSetDropDown();
							$('#pvSubSetDiv').show();
							$("#subsetPvs").chosen();
							$('#subsetPvs').val(skipRule.pvSubSet);
							$('#defaultPv').val(skipRule.defaultPv);
							$("#subsetPvs").trigger("liszt:updated");
						} else {
							$('#pvSubSetDiv').hide();
						}

						AdvancedControlPropertiesBizLogic
								.setSkipRuleControllingFieldsUI();

						if (skipRule.controllingPvs != null
								&& skipRule.controllingPvs != undefined) {

							$("#controllingValuesDiv").hide();

							$('#pvs').val(skipRule.controllingPvs);
							$("#pvs").trigger("liszt:updated");
							$("#pvDiv").show();

						} else if (skipRule.controllingValues != null
								&& skipRule.controllingValues != undefined
								&& skipRule.controllingCondition != null
								&& skipRule.controllingCondition != undefined) {

							$("#pvDiv").hide();
							$('#controllingValuesCondition').val(
									skipRule.controllingCondition);
							$('#controllingValues').val(
									skipRule.controllingValues);
							$("#controllingValuesDiv").show();
						}

					}
				}
			}),

	/*
	 * Used to create control Node in the tree
	 */

	/*
	 * Form Router. Used to perform actions initiated a Container's User
	 * Interface
	 */

	FormRouter : Backbone.Router
			.extend({
				routes : {
					"loadCachedForm/:id/:edit" : "loadForm",
					"clear" : "dummy"

				},
				initialize : function() {
					// Routers.updateCachedFormMethod = this.updateFormAndUI;
					Routers.updateCachedFormMethod = this.updateFormAndUI;
				},

				dummy : function() {
				},

				loadForm : function(id, edit) {
					$("#formWaitingImage").show();
					if (Main.formView == null) {
						Main.formView = Views.showForm('formTab',
								new Models.Form());

					}
					GlobalMemory.editForm = (edit == "true");
					Main.formView.getFormModel().fetch({
						url : 'csdApi/form/' + id + "/" + edit,
						success : this.loadFormSuccessHandler
					});
					// save as
				},

				updateId : function(nId) {
					Main.formView.getFormModel().set({
						id : nId,
						status : saved
					});
				},

				updateFormUI : function(model) {
					Main.formView.render();
				},

				updateTreeWithFormName : function(model) {
					Main.treeView.getTree().setItemText(1,
							model.get('caption'), 'Form\'s caption');
					Main.treeView.getTree().deleteChildItems(1);
				},

				updateFormAndUI : function(model) {
					/*
					 * Routers.formEventsRouterPointer.updateFormUI(model);
					 * Routers.formEventsRouterPointer
					 * .updateTreeWithFormName(model);
					 * Routers.formEventsRouterPointer
					 * .loadControlsInModelAndTree(model); // re populate skip
					 * rules table; $("#skipRulesTable tr:gt(0)").remove();
					 * AdvancedControlPropertiesBizLogic.loadSkipRules(model);
					 */
					Routers.formEventsRouterPointer.updateModelWithIds(model);

				},

				updateUI : function(model) {
					Routers.formEventsRouterPointer.updateFormUI(model);
					Routers.formEventsRouterPointer
							.updateTreeWithFormName(model);
					Routers.formEventsRouterPointer
							.loadControlsInModelAndTree(model);
					// re populate skip rules table;
					$("#skipRulesTable tr:gt(0)").remove();
					AdvancedControlPropertiesBizLogic.loadSkipRules(model);
					Routers.formEventsRouterPointer.updateModelWithIds(model);
				},

				loadFormulae : function(model, subFormName) {
					for ( var key in model.get('controlObjectCollection')) {
						var control = model.get('controlObjectCollection')[key];
						if (control.get('type') == "numericField") {
							if (control.get('isCalculated') != undefined
									&& control.get('isCalculated')) {
								var controlName = control.get('controlName');
								if (subFormName != "") {
									controlName = subFormName + "."
											+ controlName;
								}
								Main.advancedControlsView.addFormulaToTable(
										controlName, control.get('formula'));
							}
						}
						if (control.get('type') == "subForm") {
							this.loadFormulae(control.get('subForm'), control
									.get('controlName'));
						}
					}
				},

				loadFormSuccessHandler : function(model, response) {
					Routers.formEventsRouterPointer.updateUI(model);
					Routers.formEventsRouterPointer.loadFormulae(Main.formView
							.getFormModel(), "");

					AdvancedControlPropertiesBizLogic
							.loadSkipRules(Main.formView.getFormModel());
					Main.formView.getFormModel().set({
						skipRules : model.get('skipRules')
					});
					Main.advancedControlsView.setTableCss('formulaTable');
					Main.mainTabBarView.loadFormSummary();
					Main.mainTabBarView.getFormSummaryView().displayFormInfo(
							model.getFormInformation());

					$("#formWaitingImage").hide();
					// save form
					
					if(!GlobalMemory.editForm){

						$('#saveForm').prop("value", " Save As ")
					}
				},

				updateModelWithIds : function(model) {
					// set form's id
					Main.formView.getFormModel().set({
						id : model.get('id')
					});
					// set controls' ids
					for ( var cntr = 0; cntr < model.get('controlCollection').length; cntr++) {
						var control = new Models.Field(model
								.get('controlCollection')[cntr]);
						Main.formView.getFormModel().setControlId(
								control.get('controlName'), control.get('id'));
						// set sub form controls' ids
						if (control.get('type' == "subForm")) {
							// set sub form id
							Main.formView.getFormModel().setSubFormId(
									control.get('controlName').control.get(
											'subForm').get('id'));
							// set sub controls' ids
							var subFrom = new Models.Form(control
									.get('subForm'));
							for ( var subCntr = 0; cntr < subFrom
									.get('controlCollection').length; subCntr++) {

								var subControl = new Models.Field(subFrom
										.get('controlCollection')[subCntr]);
								var subControlName = control.get('controlName')
										+ "." + subControl.get('controlName');
								Main.formView.getFormModel().setControlId(
										subControlName, subControl.get('id'));

							}
						}

					}
				},

				loadControlsInModelAndTree : function(model) {
					// Need to add code to populate sub forms
					for ( var cntr = 0; cntr < model.get('controlCollection').length; cntr++) {

						var control = new Models.Field(model
								.get('controlCollection')[cntr]);
						var displayLbl = control.get('caption') + " ("
								+ control.get('controlName') + ")";
						this.populateTreeWithControlNodes(control
								.get('controlName'), displayLbl, control
								.get('type'));
						var parentId = GlobalMemory.nodeCounter - 1;
						if (control.get('type') == "subForm") {
							var subFrm = new Models.Form(control.get('subForm'));
							subFrm.set({
								controlObjectCollection : {}
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
										parentId, GlobalMemory.nodeCounter,
										displayLabel, 0, 0, 0, 0,
										"SELECT,CALL,CHILD,CHECKED");
								Main.treeView.getTree().setUserData(
										GlobalMemory.nodeCounter,
										"controlName",
										subControl.get('controlName'));
								Main.treeView.getTree().setUserData(
										GlobalMemory.nodeCounter,
										"controlType", subControl.get('type'));
								GlobalMemory.nodeCounter++;

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
						displayLabel, type) {
					Main.treeView.getTree().insertNewChild(1,
							GlobalMemory.nodeCounter, displayLabel, 0, 0, 0, 0,
							"SELECT,CALL,CHILD,CHECKED");
					Main.treeView.getTree().setUserData(
							GlobalMemory.nodeCounter, "controlName",
							controlName);
					Main.treeView.getTree().setUserData(
							GlobalMemory.nodeCounter, "controlType", type);

					GlobalMemory.nodeCounter++;
				}
			}),

	/*
	 * Router initialization
	 */
	initializeRouters : function() {
		this.formEventsRouterPointer = new this.FormRouter;
		var advancedControlPropsRouter = new this.AdvancedPropertiesRouter;
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
			// add rows
			for ( var i = 0; i < ControlBizLogic.getNextSequenceNumber() - 1; i++) {
				var newId = (new Date()).valueOf();
				Main.designModeViewPointer.getGridObject()
						.addRow(newId + i, "");
			}

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
				Main.designModeViewPointer.getGridObject().cellByIndex(
						seqNumber, xPos).setValue(name);
			}
		}
	}
}