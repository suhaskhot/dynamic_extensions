var Views = {

	/*
	 * Form View
	 */
	FormView : Backbone.View
			.extend({
				initialize : function() {
					_.bindAll(this, 'render'); // fixes loss of context for
					this.render();// self-rendering
				},
				loadForm : function() {

					Routers.formEventsRouterPointer.navigate("loadCachedForm/"
							+ $('#formCaption').val(), true);

				},
				saveForm : function(showMessage) {
					if (!Utility.checkNameForCorrectness($('#formName').val())) {
					  Utility.notify($("#notifications"), "Form Name should not contain special characters and white spaces", "error");
					  return;
					}
					// Save Model
					// alert(JSON.stringify(this.model.toJSON()));
					this.populateControlsInForm();
					$("#formWaitingImage").show();
					// set form info from summary
					this.model.setFormInformation(Main.mainTabBarView
							.getFormSummaryView().getModel());
					this.model.set({
						formulae : GlobalMemory.formulae
					});

                                        var ctrlColl = this.model.get('controlObjectCollection');
                                        this.model.attributes = _.omit(this.model.attributes, ['controlObjectCollection']);
                                        var sfCtrlColl = {};
                                        for (var i = 0; i < this.model.get('controlCollection').length; ++i) {
                                          var ctrl = this.model.get('controlCollection')[i];
                                          if (ctrl.type == 'subForm') {
                                            var sf = ctrl.subForm;
                                            sfCtrlColl[ctrl.controlName] = sf.get('controlObjectCollection');
                                            sf.attributes = _.omit(sf.attributes, ['controlObjectCollection']);
                                          }
                                        }
                                       
					this.model
							.save(
									{
								    		save : "yes"
									},
									{
										wait : true,
										success : function(model, response) {
											if (model.get("status") == "saved") {

												Routers
														.updateCachedFormMethod(model);
												$("#formWaitingImage").hide();
												var message = model
														.get('caption')
														+ " was saved successfully.";
											/*	$("#popupMessageText").html(
														message);
												$("#dialog-message").dialog(
														'open');
												Main.mainTabBarView
														.getFormSummaryView()
														.displayFormInfo(
																model
																		.getFormInformation());*/
			if (showMessage) {
				Utility.notify($("#notifications"), message, "success");
			}

												// change from Save as to save
												$('#saveForm').prop("value",
														" Save  ")

											} else {
												$("#formWaitingImage").hide();
										/*		$("#popupMessageText")
														.html(
																"Could not save the form successfully.");
												$("#dialog-message").dialog(
														'open'); */
            Utility.notify($("#notifications"), "Could not save the form successfully", "error");

											}
										},
										error : function(model, response) {
											$("#formWaitingImage").hide();
										/*	$("#popupMessageText")
													.html(
															"Could not save the form successfully.");
											$("#dialog-message").dialog('open'); */
            Utility.notify($("#notifications"), message, "success");
										}

									});
                                        this.model.set('controlObjectCollection', ctrlColl);
                                        for (var i = 0; i < this.model.get('controlCollection').length; ++i) {
                                          var ctrl = this.model.get('controlCollection')[i];
                                          if (ctrl.type == 'subForm') {
                                            var sf = ctrl.subForm;
                                            sf.set('controlObjectCollection', sfCtrlColl[ctrl.controlName]);
                                          }
                                        }
				},

				loadModelInSessionForPreview : function() {
					$("#formWaitingImage").show();
					this.populateControlsInForm();
					this.model.setFormInformation(Main.mainTabBarView
							.getFormSummaryView().getModel());
					  $('#previewFrame').hide();
					  $('#preview').empty();
					  var deJson = getDEJson({json: this.model});
					  this.form = new edu.common.de.Form({
						formDef       : deJson,
						formDiv       : 'preview',
						showActionBtns: false
					  });
					  this.form.render();
					  $('#formWaitingImage').hide();
				},

				loadModelInSession : function() {
				  $("#formWaitingImage").show();
				  this.populateControlsInForm();
				  this.model.setFormInformation(Main.mainTabBarView.getFormSummaryView().getModel());
				  this.model.save(
				    {
					  save : "no"
					},

					{
					  wait : true,
					  success : function(model, response) {
					    $("#formWaitingImage").hide();
					    $("#skipRulesTable tr:gt(0)").remove();
					    AdvancedControlPropertiesBizLogic.loadSkipRules(Main.formView.getFormModel());
					  },

					  error : function(model, response) {
					    $("#formWaitingImage").hide();
						$("#popupMessageText").html("Could not process the form successfully.");
						$("#dialog-message").dialog('open');
					  }
					}
				  );
				},

				populateControlsInForm : function() {

//					DesignModeBizLogic.populateControlPositions();

					this.model.set({
						controlCollection : new Array()
					});

					var controlsOrder = this.model.get('controlsOrder');
					var controlObjectCollection = this.model
							.get('controlObjectCollection');
					for (cntr = 0; cntr < controlsOrder.length; cntr++) {

						var controlName = controlsOrder[cntr];
						var control = controlObjectCollection[controlName];

						if (control == undefined) {

						} else {
							if (control.get('type') == "subForm") {

								var _subForm = control.get('subForm');
								var subFormControlsOrder = _subForm
										.get('controlsOrder');
								var subFormControlObjectCollection = _subForm
										.get('controlObjectCollection');

								_subForm.set({
									controlCollection : new Array()
								});

								for ( var subCntr = 0; subCntr < subFormControlsOrder.length; subCntr++) {

									var subFormControlName = subFormControlsOrder[subCntr];
									var subFormControl = subFormControlObjectCollection[subFormControlName];

									if (subFormControl == undefined) {
									} else {
										// set control json
										_subForm.get('controlCollection').push(
												_.omit(subFormControl.attributes, ['template']));
									}
								}
								
								// set sub form
								control.set({
									subForm : _subForm
								});
							}
							// set control json
							this.model.get('controlCollection').push(_.omit(control.attributes, ['template']));
						}
					}

				},

				getFormModel : function() {

					return this.model;
				},
				render : function() {
					/*
					 * this.$el.html(Mustache.to_html(
					 * Templates.templateList['formTemplate'], this.model
					 * .toJSON()));
					 */

					$("#formWaitingImage").hide();
					// init dialog box
					$("#dialog-message").dialog({
						modal : true,
						autoOpen : false,
						buttons : {
							Ok : function() {
								$(this).dialog("close");
							}
						}
					}).css("font-size", "10px");

					$("#general-dialog").dialog({
						modal : true,
						autoOpen : false
					}).css("font-size", "10px");

				}
			}),

	/*
	 * Body View
	 */
	BodyView : Backbone.View.extend({
		el : '#csd_body',
		initialize : function() {
			_.bindAll(this, 'render');
			this.render();
		},
		render : function() {
			this.$el.html(Templates.templateList['bodyTemplate']);
		}
	}),

	/*
	 * Field View
	 */
	FieldView : Backbone.View
			.extend({

				pvGrid : null,

				initialize : function() {
					_.bindAll(this, 'render');
					this.render();// self-rendering
				},
				events : {
					"click #createControlButtonid" : "updateModel",
					"click #deleteControlButtonid" : "deleteControl",
					"click #copyControlButtonid" : "copyControl",
					"click #addPv" : "addPv",
					"click #deletePv" : "deletePv",
					"click #changeControlButtonid" : "changeControl",
					"keyup #controlCaption" : "setAttributeName"
				},

				setAttributeName : function(event) {
					var userDefinedName = Utility.toCamelCase($(
							'#controlCaption').val());
					if (userDefinedName.length > 28) {
						userDefinedName = userDefinedName.substring(0, 27) + ""
								+ GlobalMemory.nodeCounter;
					}
					// $('#controlName').val(userDefinedName);
					$('#userDefinedName').val(userDefinedName);
				},

				changeControl : function(event) {
					var currentType = Main.currentFieldView.getModel().get(
							'type');
					ControlBizLogic.populatePermissibleControlTypes(
							currentType, 'newControlType');
					$("#control-change-dialog")
							.dialog(
									{
										buttons : {
											Cancel : function() {
												$(this).dialog("close");
											},
											Ok : function() {
												ControlBizLogic
														.changeCurrentControlAndDisplay()
												// close the dialog
												$(this).dialog("close");
											}
										}
									});
					$("#control-change-dialog").dialog("open");

				},

				copyControl : function(event) {
					ControlBizLogic.createCopyControl(this.model);

				},

				destroy : function() {
					this.undelegateEvents();
					this.$el.empty();
					this.stopListening();

				},

				deleteControl : function(event) {
					GlobalMemory.currentBufferControlModel = this.model;
					$('#dialogMessageText').html(
							'Do you wish to delete this Control?');
					$("#general-dialog")
							.dialog(
									{
										buttons : {
											Yes : function() {
												$('#deleteControlButtonid')
														.prop("disabled", true);
												$('#createControlButtonid')
														.prop("disabled", true);
												$('#addPv').prop("disabled",
														true);
												$('#deletePv').prop("disabled",
														true);
												ControlBizLogic
														.deleteControl(GlobalMemory.currentBufferControlModel);
												$(this).dialog("close");
												Main.currentFieldView.destroy();
											},
											No : function() {
												$(this).dialog("close");
											}
										}
									});
					$("#general-dialog").dialog("open");

				},

				enableDeleteButton : function() {
					$('#deleteControlButtonid').prop("disabled", false);
				},

				enableDisableButton : function(buttonName, disable) {
					var buttonId = "";
					switch (buttonName) {
					case "save":
						buttonId = "createControlButtonid";
						break;
					case "delete":
						buttonId = "deleteControlButtonid";
						break;
					case "copy":
						buttonId = "copyControlButtonid";
						break;
					default:
						buttonId = "";
					}
					$('#' + buttonId).prop("disabled", disable);

				},

				updateModel : function(event) {

					var newModel = new Models.Field();
					newModel.set({
						type : this.model.get('type')
					});

					this.populateFieldsInModel(newModel);
					var validationMessages = newModel.validate(newModel
							.toJSON());

					/*
					 * if (this.model.get('editName') == undefined &&
					 * !ControlBizLogic .isControlNameUniqueInForm(this.model)) {
					 * validationMessages.push({ name : 'controlName', message :
					 * 'Attribute name should be unique.' }); }
					 */

					if (!ControlBizLogic.validateUserDefinedName(newModel)) {
						validationMessages.push({
							name : 'userDefinedName',
							message : 'Attribute name is invalid.'
						});
					}

					var status = "error";
		
					if (newModel.attributes.dataType == 'INTEGER') {
					  validationMessages = this.validatePvDataType(newModel, "INTEGER")
					  
					} else if (newModel.attributes.dataType == 'FLOAT') {
                      validationMessages = this.validatePvDataType(newModel, "FLOAT")
					} else if (newModel.attributes.dataType == 'BOOLEAN') {
					  validationMessages = this.validatePvDataType(newModel, "BOOLEAN")
				    }
					
					if (validationMessages.length == 0) {
						this.populateFields();
						var displayLabel = $('#controlCaption').val() + " ("
								+ $('#userDefinedName').val() + ")";

						if (this.model.get('editName') == undefined) {
						    var controlType = this.model.get('type');
						    if(controlType == 'fancyControl') {
						      controlType =  newModel.attributes.fancyControlType;
						    }
							status = ControlBizLogic.createControlNode(
									displayLabel, $('#controlName').val(),
									controlType, this.model,
									this.model.get('copy'));

						} else {
							status = "update";
							var editNames = this.model.get('editName').split(
									".");
							var editName = "";
							var newEditName = "";

							if (editNames.length > 1) {
								editName = editNames[1];
							} else {
								editName = editNames[0]
							}

							if (editName != this.model.get('controlName')) {
								if (editNames.length > 1) {
									newEditName = editNames[0] + "."
											+ this.model.get('controlName');
								} else {
									newEditName = this.model.get('controlName');
								}
								Main.formView.getFormModel().deleteControl(
										this.model.get('editName'));
							} else {
								newEditName = this.model.get('editName');
							}

							Main.formView.getFormModel().editControl(
									newEditName, this.model);
							var displayText = this.model.get('caption') + "("
									+ this.model.get('userDefinedName') + ")";

							if (this.model.has('formTreeNodeId')) {
								Main.treeView.getTree().setItemText(
										this.model.get('formTreeNodeId'),
										displayText, '');

								Main.treeView.getTree().setUserData(
										this.model.get('formTreeNodeId'),
										"controlName",
										this.model.get('controlName'));
							}
							Main.formView.saveForm(false);
						}

						// add page break
						var addPageBreak = $(
								'input[name=addPageBreak]:radio:checked').prop(
								'id');
						if (addPageBreak == "pageBreakYes") {
							if (status == "save" || status == "update") {
								this.addPageBreak();
							}
						}
					}

					this.showMessages(validationMessages, status);
				},

				validatePvDataType : function (newModel, dataType) {
				  var validationMessages = newModel.validate(newModel.toJSON());
				  for (var i = GlobalMemory.pvCounter -1 ; i > -1 ; i--) {
				    var pv = eval("newModel.attributes.pvs.pv_" + i);
					
					if (pv == undefined) {
					  break;
					}
					
					var isValid = true;
					
					switch (dataType) {
					  case "INTEGER" :
						if (isNaN(pv.value)) {
					      isValid = false;
						} else if (parseInt(pv.value) != parseFloat(pv.value)) {
					      isValid = false;
						}
						break;
					  case "FLOAT" :
						if (isNaN(pv.value)) {
					      isValid = false;
						}
						break;
					  case "BOOLEAN" :
						if (!(pv.value == "true" || pv.value == "false")) {
     					  isValid = false;
						}
						break;
					}
                    
					if (! isValid) {
					  validationMessages.push({
						name : 'datatype',
						message : 'Permissible value does not match with given data type ' + dataType
					  });
					  break;
					}
				  }
				  return validationMessages;
				 },
				
				populateFields : function() {
					var labelPos = $('input[name=labelAlignment]:radio:checked')
							.prop('id');
					if (labelPos == "align_left") {
						labelPos = "LEFT"
					} else if (labelPos == "align_top") {
						labelPos = "TOP"
					}

					this.model
							.set({
								caption : $('#controlCaption').val(),
								defaultValue : $('#defaultValue').val(),
								controlName : $('#controlName').val(),
								conceptDefinitionSource : $(
										'#conceptDefinitionSource').val(),
								conceptCode : $('#conceptCode').val(),
								conceptDefinition : $('#conceptDefinition')
										.val(),
								conceptPreferredName : $(
										'#conceptPreferredName').val(),
								noOfDigits :  $('#noOfDigits').val() == undefined ? 6 : $('#noOfDigits').val(),
								noOfDigitsAfterDecimal : $(
										'#noOfDigitsAfterDecimal').val(),
								maximumValue : $('#maximumValue').val(),
								minimumValue : $('#minimumValue').val(),
								width : $('#width').val(),
								userDefinedName : $('#userDefinedName').val(),// 

								dataType : $('#dataType').val(),
								format : $('#format').val(),
								isPHI : $('#isPHI').is(":checked"),
								isHTMLLabel : $('#isHTMLLabel').is(":checked"),
								isMandatory : $('#isMandatory').is(":checked"),
								isAutoCalculate : $('#autoCalculate').is(
										":checked"),
								isChecked : $('#isChecked').is(":checked"),
								optionsPerRow : $('#optionsPerRow').val(),
								pvOrder  :$("input:radio[name=sortOrder]:checked" ).val(),
								toolTip : $('#toolTip').val(),
								labelPosition : labelPos,
								subFormName : $('#subFormName').val(),
								fancyControlType : $('#fancyControlType').val()
							});

					// set pvs
					this.model
							.set({
								pvs : Main.currentFieldView.getModel().get(
										'pvs'),
								pvFile : Main.currentFieldView.getModel().get(
										'pvFile'),
								defaultPv : Main.currentFieldView.getModel()
										.get('defaultPv')
							});

				},

				populateFieldsInModel : function(model) {
					var labelPos = $('input[name=labelAlignment]:radio:checked')
							.prop('id');
					if (labelPos == "align_left") {
						labelPos = "LEFT"
					} else if (labelPos == "align_top") {
						labelPos = "TOP"
					}

					model.set({
								caption : $('#controlCaption').val(),
								defaultValue : $('#defaultValue').val(),
								controlName : $('#controlName').val(),
								conceptDefinitionSource : $(
										'#conceptDefinitionSource').val(),
								conceptCode : $('#conceptCode').val(),
								conceptDefinition : $('#conceptDefinition')
										.val(),
								conceptPreferredName : $(
										'#conceptPreferredName').val(),
								noOfDigits : $('#noOfDigits').val() == undefined ? 6 : $('#noOfDigits').val(),
								noOfDigitsAfterDecimal : $(
										'#noOfDigitsAfterDecimal').val(),
								maximumValue : $('#maximumValue').val(),
								minimumValue : $('#minimumValue').val(),
								width : $('#width').val(),
								userDefinedName : $('#userDefinedName').val(),// 

								dataType : $('#dataType').val(),
								format : $('#format').val(),
								isPHI : $('#isPHI').is(":checked"),
								isMandatory : $('#isMandatory').is(":checked"),
								isHTMLLabel : $('#isHTMLLabel').is(":checked"),
								isAutoCalculate : $('#autoCalculate').is(
										":checked"),
								isChecked : $('#isChecked').is(":checked"),
								optionsPerRow : $('#optionsPerRow').val(),
								pvOrder : $("input:radio[name=sortOrder]:checked" ).val(),
								toolTip : $('#toolTip').val(),
								labelPosition : labelPos,
								subFormName : $('#subFormName').val(),
								fancyControlType : $('#fancyControlType').val()
							});

					// set pvs
					model
							.set({
								pvs : Main.currentFieldView.getModel().get(
										'pvs'),
								pvFile : Main.currentFieldView.getModel().get(
										'pvFile'),
								defaultPv : Main.currentFieldView.getModel()
										.get('defaultPv')
							});

				},

				addPageBreak : function() {
					var pageBreak = ControlBizLogic.createControl("pageBreak");
					pageBreak = Utility.addFieldHandlerMap["pageBreak"](
							pageBreak, false, '');
					pageBreak.set({
						caption : "Page Break"
					});
					ControlBizLogic.createControlNode("Page Break ("
							+ pageBreak.get('userDefinedName') + ")", pageBreak
							.get('controlName'), "pageBreak", pageBreak, false);
				},

				showMessages : function(validationMessages, status) {
					$('html, body').animate({scrollTop: 0}, 300);

					if (validationMessages.length == 0) {

						if (status == "update") {
							this.setSuccessMessageHeader();
							$("#messagesDiv").append(Utility.messageSpace
							+ this.model.get('caption')
							+ " updated successfully in the form");
						} else if (status == "save") {
							this.setSuccessMessageHeader();
							$("#messagesDiv")
									.append(
											Utility.messageSpace
													+ this.model.get('caption')
													+ " added to the form successfully.");
							Main.currentFieldView.enableDisableButton("delete",
									false);
							Main.currentFieldView.enableDisableButton("copy",
									false);
							// $('#createControlButtonid').attr("disabled",
							// true);
							// $('#addPv').prop("disabled", true);
							// $('#deletePv').prop("disabled", true);
							this.setSubmitCaptionToUpdate();
						}

					} else {
						this.setErrorMessageHeader();
						for ( var key = 0; key < validationMessages.length; key++) {
							$("#messagesDiv").append(
									Utility.messageSpace + "! "
											+ validationMessages[key].message
											+ "<br>");
						}
					}
				},

				setSuccessMessageHeader : function() {
					$("#messagesDiv").html("");
					$("#messagesDiv").removeClass('error');
					$("#messagesDiv").addClass('success');
				},

				setErrorMessageHeader : function() {
					$("#messagesDiv").html("");
					$("#messagesDiv").removeClass('success');
					$("#messagesDiv").addClass('error');
				},

				render : function() {
					this.$el.html(Mustache.to_html(this.model.get('template'),
							this.model.toJSON()));
					// clear messages div
					$("#messagesDiv").html("");
					$("#messagesDiv").removeClass('success');
					$("#messagesDiv").removeClass('error');
					$('#isHTMLLabel').prop('checked',
							this.model.get('isHTMLLabel'));
					$('#isPHI').prop('checked', this.model.get('isPHI'));
					$('#isMandatory').prop('checked',
							this.model.get('isMandatory'));
					$('#autoCalculate').prop('checked',
							this.model.get('isAutoCalculate'));
					

					// data type
					$("#dataType").val(this.model.get('dataType')).prop(
							'selected', true);
					// pv order
					var pvVal = this.model.get('pvOrder');
					if (pvVal == "ASC") {
						$('input:radio[name=sortOrder][value="ASC"]').prop('checked', true);
					} else if (pvVal == "DESC") {
						$('input:radio[name=sortOrder][value="DESC"]').prop('checked', true);
					} else {
						$('input:radio[name=sortOrder][value="NONE"]').prop('checked', true);
					}

					// format
					$("#format").val(this.model.get('format')).prop('selected',
							true);

					// make short code readonly
					// $('#controlName').prop('readonly', 'readonly');

					switch ((this.model.get('type'))) {

					case "checkBox":
						$('#isChecked').prop('checked',
								this.model.get('isChecked'));
						break;

					case "radioButton":

					case "listBox":

					case "multiselectBox":

					case "comboBox":

					case "multiselectCheckBox":
						this.renderPvUI();
						break;

					case "fancyControl":
					    this.populateFancyControls();
						$("#fancyControlType").val(this.model.get('fancyControlType'))
						  .prop('selected', true);
						break;
					default:
					}
					// init page break
					$("#addPageBreak").buttonset();
					// init label position
					$("#labelAlignment").buttonset();

					// show the set position
					var labelPos = GlobalMemory.globalLabelPosition;
					if (this.model.has('labelPosition')) {
						labelPos = this.model.get('labelPosition');
					}

					if (labelPos == "TOP") {
						labelPos = "align_top";
					} else if (labelPos == "LEFT") {
						labelPos = "align_left";
					}
					$('#' + labelPos).prop('checked', true).button('refresh');

					// page break exists after control
					if (this.model.get('pageBreak') == true) {
						$('#pageBreakYes').prop('checked', true).button(
								'refresh');
					}
					// set page break on click of yes
					$('#pageBreakYes').click(function() {
						Main.currentFieldView.getModel().set({
							pageBreak : true
						});
					});

				},

				populateFancyControls : function() {
				   var fieldList = edu.common.de.FieldManager.getInstance().getFieldList();
				   for(var i=0; i<fieldList.length; i++) {
				     $('#fancyControlType').append($("<option/>").prop("value", fieldList[i].name).append(fieldList[i].displayName));
				   }
				   //$('#fancyControlType').chosen({width: "100%", allow_single_deselect: true});
				},

				renderPvUI : function() {
					$('#permissibleValuesForm')
							.ajaxForm(
									{
										beforeSend : function() {
											$("#pvFileWaitingImage").show();
										},
										success : function(data) {
											var receivedData = JSON.parse(data);

											$("#pvFileWaitingImage").hide();
											if (receivedData.status == "saved") {
												ControlBizLogic
														.addUploadedPvFileNameToCurrentModel(receivedData.file);
											} else {
												var trail = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
												Main.currentFieldView
														.setErrorMessageHeader();
												$("#messagesDiv")
														.append(
																trail
																		+ "Could not upload file");
											}
										}
									});
					$("#pvFileWaitingImage").hide();

					this.pvGrid = new dhtmlXGridObject('pvGrid');
					this.pvGrid
							.setImagePath("../dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
					this.pvGrid
							.setHeader("<span id = 'header1'>Value</span>,<span id='header2'>Numeric Code</span>,"
									+ "<span id='header3'>Definition</span>,<span id='header4'>Definition Source</span>,"
									+ "<span id='header5'>Concept Code</span>,<span id='header6'>Default</span>,status");
					this.pvGrid.setInitWidths("300,0,0,0,0,117,0");
					// this.pvGrid.setColAlign("right,left");
					this.pvGrid.setColTypes("ed,ed,ed,ed,ed,ro,ro");
					this.pvGrid.enableMultiselect(true);
					// this.pvGrid.setColSorting("str,str");
					this.pvGrid.setSkin("clear");
					this.pvGrid.selMultiRows = true;
					this.pvGrid.enablePaging(true, 10, 3, "recinfoArea");
					this.pvGrid.setPagingSkin("bricks");
					this.pvGrid.enableAlterCss("evenGridRow", "unevenGridRow");
					// default pv selection
					/*
					 * grid.forEachRow(function(id){ var
					 * cell=grid.cells(id,INDEX); if (cell.isCheckbox())
					 * cell.setValue(1); });
					 */
					// set default pv
					this.pvGrid.attachEvent("onCellChanged",
							ControlBizLogic.pvGridHandler);
					this.pvGrid.init();

					for ( var i = 1; i < 7; i++) {
						$('#header' + i).addClass("text_grid_header");
					}

				},

				setpvPropertyBasedOnIndex : function(index, pv, propertyValue) {
					var updatedPv = pv;
					switch (index) {
					case 0:
						updatedPv.value = propertyValue;
						break;
					case 1:
						updatedPv.numericCode = propertyValue;
						break;
					case 2:
						updatedPv.definition = propertyValue;
						break;
					case 3:
						updatedPv.definitionSource = propertyValue;
						break;
					case 4:
						updatedPv.conceptCode = propertyValue;
						break;
					default:
						// do not do anything
					}
					return updatedPv;
				},

				getPvGrid : function() {
					return this.pvGrid;
				},

				addPv : function() {
					var rowId = "pv_" + GlobalMemory.pvCounter;
					var defaultPvRadio = "<input type='radio' name='defaultPv' value='"
							+ rowId + "'>";
					this.pvGrid.addRow(rowId, [ "", "", "", "", "",
							defaultPvRadio, "new" ]);
					this.model.get('pvs')[rowId] = this.generatePvFromGridData(
							'', '', '', '', '', "add");

					this.pvGrid.selectRowById(rowId);
					GlobalMemory.pvCounter++;
					ControlBizLogic.initDefaultPv();

				},

				deletePVs : function() {
					var pvIds = this.pvGrid.getSelectedRowId().split(",");

					if (pvIds.length == 1) {

						delete this.model.get('pvs')[pvIds[0]];
						this.pvGrid.deleteRow(pvIds[0]);
					} else if (pvIds.length > 1) {
						for ( var key in pvIds) {
							delete this.model.get('pvs')[pvIds[key]];
							this.pvGrid.deleteRow(pvIds[key]);
						}
					}

				},

				deletePv : function() {

					$('#dialogMessageText').html(
							'Do you wish to delete the Permissible Value(s)?');
					$("#general-dialog").dialog({
						buttons : {
							Yes : function() {
								Main.currentFieldView.deletePVs();
								$(this).dialog("close");
							},
							No : function() {
								$(this).dialog("close");
							}
						}
					});
					$("#general-dialog").dialog("open");

				},

				generatePvFromGridData : function(pvValue, pvNumericCode,
						pvDefinition, pvDefinitionSource, pvConceptCode,
						pvStatus) {
					return {
						value : pvValue,
						numericCode : pvNumericCode,
						definition : pvDefinition,
						definitionSource : pvDefinitionSource,
						conceptCode : pvConceptCode,
						status : pvStatus
					};
				},

				setSubmitCaptionToUpdate : function() {
					$('#createControlButtonid').val('Update Control');
				},

				getModel : function() {
					return this.model;
				}
			}),

	/*
	 * Tree View
	 */

	TreeView : Backbone.View
			.extend({
				formTree : null,
				initialize : function() {
					_.bindAll(this, 'render'); // fixes loss of context for
					// 'this' within methods
					this.render();// self-rendering
				},

				render : function() {
					var tree = new dhtmlXTreeObject(this.el, "100%", "100%", 0);
					tree.setSkin('dhx_terrace');
					tree.setImagePath("../dhtmlxSuite_v35/dhtmlxTree/"
							+ "codebase/imgs/csh_dhx_terrace/");
					tree.enableDragAndDrop(false);
					tree.enableTreeImages(false);
					tree.deleteChildItems(0);
					var xml = "<?xml version='1.0' encoding='utf-8'?>"
							+ "<tree id='0'>"
							+ "<item text=' ' id='1' child='1' >"
							+ "<userdata name='type'>form</userdata><userdata name='system'>true</userdata>"
							+ "</item> " + "</tree>";
					tree.loadXMLString(xml);
					tree.openAllItems(0);
					tree.setItemStyle(1, "font-weight:bold;");
					tree.enableAutoTooltips(true);
					this.formTree = tree;
				},

				getTree : function() {
					return this.formTree;
				}
			}),
	/*
	 * TabBar View
	 */
	TabBarView : Backbone.View
			.extend({

				tab : null,
				summaryView : null,
				initialize : function() {
					_.bindAll(this, 'render'); // fixes loss of context for
					// 'this' within methods
					this.render();// self-rendering
				},

				highlightSelectedControlType : function() {
					var pos = Utility
							.getControlIndexForCarousel(Main.currentFieldView
									.getModel().get('type'));
					var displacement = (pos > 7) ? ((pos < 14) ? (pos % 7) + 1
							: (pos % 7) + 8) : pos;

					Main.carousel.tinycarousel_move(displacement);
					$('#' + Main.currentFieldView.getModel().get('type'))
						.css('background-color', '#F0F0F0 ')
						.css('box-shadow','2px 2px 2px 0px rgba(171, 171, 210, 0.51)');
				},

				selectTab : function(tab) {
					if (Main.mainTabBarView.getTabBar().getActiveTab() != tab) {
						Main.mainTabBarView.getTabBar().setTabActive(tab);
					}
				},

				render : function() {

					/*
					 * var tabbar = new K_Tabbar( { container : this.el,
					 * allignment : 'top', skin : 'dhx_terrace', imagesPath :
					 * 'csd_web/dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/',
					 * tabsConfiguration : [ { id : 'a11', label : 'Summary',
					 * labelWidth : '150px', data : 'summary' } ] }, 'dhtmlx');
					 */

					this.tab = new dhtmlXTabBar(this.el, "top");
					this.tab.setSkin('dhx_terrace');
					// this.tab.setHrefMode("iframes-on-demand");
					this.tab
							.setImagePath("../dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
					this.tab.addTab("summaryTab", "Summary", "200px");
					this.tab.addTab("controlTab", "Add/Edit Control", "200px");
					//this.tab.addTab("advancedControlPropertiesTab",
					//		"Advanced Options", "150px");
					//this.tab.addTab("designMode", "Design", "200px");
					this.tab.addTab("previewTab", "Preview", "200px");
					this.tab.setContent("summaryTab", "summary");
					this.tab.setContent("controlTab", "control");
					//this.tab.setContent("designMode", "design");
					this.tab.setContent("previewTab", "preview");
					// this.tab.setContentHref("previewTab",
					// "csd_web/pages/preview.html");
					//this.tab.setContent("advancedControlPropertiesTab",
					//		"advancedControlProperties");

					this.tab.setTabActive("summaryTab");
					this.tab.attachEvent("onSelect", function(id, last_id) {
						ControlBizLogic.csdControlsTabSelectHandler(id);
						return true;
					});

					// render summary page
					this.loadFormSummary();

				},
				loadFormSummary : function() {
					var formInfo = Main.formView.getFormModel()
							.getFormInformation();

					if (formInfo.get('createdBy') == undefined
							|| formInfo.get('createdBy') == "") {
						var result = $.ajax({
							url : "../../rest/ng/de-forms/currentuser",
							async : false
						}).responseText;

						result = $.parseJSON(result);
						formInfo.set({
							createdBy : result.userName
						});
					}

					this.summaryView = new Views.SummaryView({
						el : $("#formSummaryContainer"),
						model : formInfo
					});
				},

				getFormSummaryView : function() {
					return this.summaryView;
				},

				getTabBar : function() {
					return this.tab;
				}
			}),

	/*
	 * Advanced Properties Tab View
	 */
	AdvancedPropertiesTabView : Backbone.View
			.extend({
				initialize : function() {
					_.bindAll(this, 'render');
					this.render();
				},

				formulaField : null,
				formulaRowCounter : 1,

				render : function() {
					this.$el
							.html(Templates.templateList['advancedPropertiesTemplate']);

					$('#availableFields1').change(
							function() {
								$('#availableFields1').prop(
										'title',
										ControlBizLogic
												.getCaptionFromControlName($(
														'#availableFields1')
														.val()));
							});

					$('#pvSubSetDiv').hide();

					$('#controllingField').change(
							function() {
								AdvancedControlPropertiesBizLogic
										.setSkipRuleControllingFieldsUI();
							});

					$('#controlledField').change(
							function() {
								if ($('#controlledField').val() != null) {
									Main.advancedControlsView
											.populatePvSubSetDropDown();
								}
							});
					// init multi selects

					$("#controlledField").chosen();
					$("#pvs").chosen();

					// Render JQuery Toggle Buttons
					$("#advancedControlsRadio").buttonset();

					$("#skipLogicOperations").buttonset();
					$("#anyOrAll").buttonset();

					// Hide Skip logic tab
					$('#pvSubSetFileWaitingImage').hide();
					$('#skipLogicTab').hide();
					$('#pvDiv').hide();
					$('#controllingValuesDiv').hide();
					$('#calculatedAttributeTab').show();
					// toggle between tabs.
					$("#skipLogic").click(
							function() {
								$('#calculatedAttributeTab').hide();
								$('#skipLogicTab').show();
								$("#controlledField").trigger("liszt:updated");
								AdvancedControlPropertiesBizLogic
										.setSkipRuleControllingFieldsUI();

								// populate skip logic
								AdvancedControlPropertiesBizLogic
										.setSkipRuleControllingFieldsUI();

							});
					// pv subset ui
					$("#pvSubSet").click(function() {
						Main.advancedControlsView.populatePvSubSetDropDown();
					});

					$("#calculatedAttribute").click(function() {
						$('#skipLogicTab').hide();
						$('#calculatedAttributeTab').show();
						// populate calculated attribute
					});

					// Render the custom formula field
					this.formulaField = new K_AutoComplete({
						inputElementId : "formulaField",
						data : ControlBizLogic.getListOfUDNForNumericControls()
					});

				},

				events : {
					"click #addFormulaBtn" : "addFormula",
					"click #addSkipLogicBtn" : "addSkipRule",
					"click #pvSubSet" : "showPvSubSetDiv",
					"click #enable" : "hidePvSubSetDiv",
					"click #show" : "hidePvSubSetDiv",
					"click #downloadPVs" : "downloadPvFile"
				},

				downloadPvFile : function(event) {
					if ($('#controlledField').val().length == 1) {
						var control = ControlBizLogic
								.getControlFromControlName($('#controlledField')
										.val()
										+ "");
						location.href = "/rest/ng/de-form/permissibleValues/"
								+ control.get('controlName');
					}
				},

				refreshFormulaField : function() {
					this.formulaField = new K_AutoComplete({
						inputElementId : "formulaField",
						data : ControlBizLogic.getListOfUDNForNumericControls()
					});
				},

				populatePvSubSetDropDown : function() {

					var control = ControlBizLogic.getControlFromControlName($(
							'#controlledField').val()
							+ "");

					// clear the messages
					Main.advancedControlsView.clearMessage();
					if ($('#controlledField').val().length == 1) {
						switch (control.get('type')) {
						case "radioButton":

						case "listBox":

						case "multiselectBox":

						case "comboBox":

						case "multiselectCheckBox":

							AdvancedControlPropertiesBizLogic
									.populatePvSelectBoxWithControlNames(
											'subsetPvs', control);
							AdvancedControlPropertiesBizLogic
									.populatePvSelectBoxWithControlNames(
											'defaultPv', control);
							// populate "none" as default
							$("#defaultPv").append($("<option/>", {
								value : "SELECT_VAL",
								text : "",
								title : ""
							}));
							$('#defaultPv').val("SELECT_VAL");

							$("#subsetPvs").trigger("liszt:updated");

							break;

						default:

						}
					}

				},

				showPvSubSetDiv : function(event) {
					$('#pvSubSetDiv').show();
					$('#subsetPvs').chosen();
					$('#subsetPvs').trigger("liszt:updated");
					$('#subsetPermissibleValuesForm')
							.ajaxForm(
									{
										beforeSend : function() {
											$("#pvSubSetFileWaitingImage")
													.show();
										},
										success : function(data) {
											var receivedData = JSON.parse(data);

											$("#pvSubSetFileWaitingImage")
													.hide();
											if (receivedData.status == "saved") {
												$('#pvSubsetFile').val(
														receivedData.file);
											} else {

												Main.advancedControlsView
														.setErrorMessageHeader();
												$(
														"#advancedPropertiesmessagesDiv")
														.append(
																Utility.messageSpace
																		+ "Could not upload file");
											}
										}
									});
					Main.advancedControlsView.populatePvSubSetDropDown();

				},

				hidePvSubSetDiv : function(event) {
					$('#pvSubSetDiv').hide();
				},

				setTableCss : function(tableId) {
					GlobalMemory.formulaCounter = 1;
					$('#' + tableId + ' tr').each(function() {

						$(this).addClass('formulaTableRowOdd');

						if (GlobalMemory.formulaCounter % 2 == 0) {

							$(this).addClass('formulaTableRowEven');

						} else {

							$(this).addClass('formulaTableRowOdd');

						}
						GlobalMemory.formulaCounter++;
					});
				},

				doesFormulaForControlExist : function(controlName) {
					GlobalMemory.generalFlag = false;
					GlobalMemory.controlName = controlName;
					$('#formulaTable tr').each(function() {
						var presentName = $(this).find('td').eq(0).text();
						if (GlobalMemory.controlName == presentName) {
							GlobalMemory.generalFlag = true;
							// break;
						}
					});
					return GlobalMemory.generalFlag;
				},

				addFormula : function(event) {
					var controlName = $('#availableFields1').val();
					var _formula = $('#formulaField').val();
					var udn = $("#availableFields1 :selected").text();
					if (controlName != null && controlName != undefined) {
						if (this.doesFormulaForControlExist(controlName)) {
							$("#popupMessageText").html(
									"A formula exists for the given control");
							$("#dialog-message").dialog('open');

						} else {
							this.addFormulaToTable(controlName, _formula, udn);
						}
					}

				},

				addSkipRule : function(event) {
					// Use display label in the drop down. Display label (Short
					// Code)

					var controlName = $('#controllingField').val();
					var action = $(
							'input[name=skipLogicOperations]:radio:checked')
							.prop('id');
					var allAny = $('input[name=anyOrAll]:radio:checked').prop(
							'id');
					var controlledAttributes = $('#controlledField').val();
					var pvs = $('#pvs').val();
					var controllingConditon = $('#controllingValuesCondition')
							.val();
					var controllingValues = $('#controllingValues').val();
					var pvSubsetFile = $('#pvSubsetFile').val();
					var pvSubSet = $('#subsetPvs').val();
					var defaultPv = $('#defaultPv').val();

					if (defaultPv == "SELECT_VAL") {
						defaultPv = "";
					}

					// validate

					var skipRule = this.generateSkipRuleFromData(
							controlledAttributes, controlName, pvs,
							controllingConditon, controllingValues, action,
							defaultPv, pvSubSet, pvSubsetFile, allAny);
					var validationMessages = this.validateSkipRule(skipRule);
					var currentSkipRuleId = GlobalMemory.skipRulesCounter;
					if (validationMessages.length == 0) {
						if (GlobalMemory.skipRuleId == null) {
							// add
							this.addSkipRuleToTable(skipRule,
									GlobalMemory.skipRulesCounter);
						} else {
							// edit
							this.addSkipRuleToTable(skipRule,
									GlobalMemory.skipRuleId);
							currentSkipRuleId = GlobalMemory.skipRuleId;
						}

						AdvancedControlPropertiesBizLogic
								.setSkipRuleForAttirbute(controlName, skipRule,
										currentSkipRuleId);

						// increment counter if skip rule was added.
						if (GlobalMemory.skipRuleId == null) {
							GlobalMemory.skipRulesCounter++;
						}
						//
						// clear components
						$("select option").prop("selected", false);
						$("#pvs").trigger("liszt:updated");
						$("#controlledField").trigger("liszt:updated");
						$("#subsetPvs").trigger("liszt:updated");
						$("#controllingValues").val("");

						this.setSuccessMessageHeader();
						$("#advancedPropertiesmessagesDiv").append(
								Utility.messageSpace
										+ "Skip Rule added successfully"
										+ "<br>");
						// set success message

					} else {

						this.setErrorMessageHeader();
						for ( var key = 0; key < validationMessages.length; key++) {
							$("#advancedPropertiesmessagesDiv").append(
									Utility.messageSpace + "! "
											+ validationMessages[key].message
											+ "<br>");
						}
					}

					GlobalMemory.skipRuleId = null;

				},

				validateSkipRule : function(skipRule) {
					var errors = new Array();
					var controllingAttribute = null;
					var controlledAttributes = null;

					if ((skipRule.controllingAttributes == null)
							|| (skipRule.controllingAttributes == undefined)
							|| (skipRule.controllingAttributes == "")) {
						errors.push({
							name : '',
							message : "Select controlling field."
						});
					} else {

						controllingAttribute = skipRule.controllingAttributes;
					}

					if ((skipRule.controlledAttributes == null)
							|| (skipRule.controlledAttributes == undefined)
							|| (skipRule.controlledAttributes == "")) {
						errors.push({
							name : '',
							message : "Select controlled field(s)."
						});
					} else {

						controlledAttributes = skipRule.controlledAttributes;
					}

					if (controlledAttributes != null
							&& controllingAttribute != null) {
						var isSubFormControl = false;
						if (controllingAttribute.split(".").length > 1) {
							isSubFormControl = true;
						}
						for ( var key = 0; key < controlledAttributes.length; key++) {
							if (isSubFormControl
									&& controlledAttributes[key].split(".").length == 1) {
								errors
										.push({
											name : '',
											message : "Controlling attribute and controlled attribute(s) should belong to the same form or same sub form."
										});
							} else if (!isSubFormControl
									&& controlledAttributes[key].split(".").length > 1) {
								errors
										.push({
											name : '',
											message : "Controlling attribute and controlled attribute(s) should belong to the same form or same sub form."
										});
							}
						}
					}

					if ((skipRule.controllingPvs == null)
							|| (skipRule.controllingPvs == undefined)
							|| (skipRule.controllingPvs == "")) {

						if ((skipRule.controllingValues == null)
								|| (skipRule.controllingValues == undefined)
								|| (skipRule.controllingValues == "")) {
							errors
									.push({
										name : '',
										message : "Select controlling permissible vlaue(s)."
									});
						}
					}

					if ((skipRule.controllingValues == null)
							|| (skipRule.controllingValues == undefined)
							|| (skipRule.controllingValues == "")) {

						if ((skipRule.controllingPvs == null)
								|| (skipRule.controllingPvs == undefined)
								|| (skipRule.controllingPvs == "")) {
							errors.push({
								name : '',
								message : "Select controlling vlaue(s)."
							});
						}
					}

					if (skipRule.controlledAttributes == skipRule.controllingAttributes
							|| skipRule.controlledAttributes
									.indexOf(skipRule.controllingAttributes) > 0) {
						errors
								.push({
									name : '',
									message : "A controlling field cannot be part of the controlled fields in the same skip rule."
								});
					}

					if (skipRule.action == "subSetPv") {
						if (skipRule.controlledAttributes.length > 1) {
							errors.push({
								name : '',
								message : "Select only one controlled field."
							});
						}
					}

					return errors;

				},

				generateSkipRuleFromData : function(slControlledAttributes,
						slControllingAttributes, slControllingPvs,
						slControllingCondition, slControllingValues, slAction,
						slPvDefaultValue, slPvSubset, slPvFileName, slAllAny) {
					return {
						controlledAttributes : slControlledAttributes,
						controllingAttributes : slControllingAttributes,
						controllingPvs : slControllingPvs,
						controllingCondition : slControllingCondition,
						controllingValues : slControllingValues,
						action : slAction,
						defaultPv : slPvDefaultValue,
						pvSubSet : slPvSubset,
						pvSubSetFile : slPvFileName,
						allAny : slAllAny

					};
				},

				addFormulaToTable : function(controlName, _formula, udn) {
					var _tr_element_start = "<tr id = '" + controlName + "'>";
					var _tr_element_end = "</tr>";
					var _td_element_start = "<td class = 'text_normal ";
					var _td_element_end = "</td>";

					if (this.formulaRowCounter % 2 == 0) {
						_td_element_start += "formulaTableRowEven'>";
					} else {
						_td_element_start += "formulaTableRowOdd'>";
					}

					var _table_row = _tr_element_start
							+ _td_element_start
							+ "<a title = 'edit' href = '#calculatedAttribute/"
							+ controlName
							+ "/edit'\"><span class = 'ui-icon ui-icon-pencil' style= 'float : left;'/></a>"
							+ "<span style= 'float : left;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"
							+ "<a title = 'delete' href = '#calculatedAttribute/"
							+ controlName
							+ "/delete'\"><span class = 'ui-icon ui-icon-trash' style= 'float : left;'/></a>"
							+ _td_element_end + _td_element_start + udn
							+ _td_element_end + _td_element_start + _formula
							+ _td_element_end + _tr_element_end;

					if (this.doesRowExist(controlName, "formulaTable")) {
						// edit
						var formulaRow = $('#formulaTable')[0].rows[controlName];
						formulaRow.cells[2].innerHTML = _formula;
					} else {
						// add
						$("#formulaTableHeader").eq(0).after(_table_row);
						this.formulaRowCounter++;
						this.setTableCss('formulaTable');
					}

					// add formula and set calculated
					AdvancedControlPropertiesBizLogic
							.setFormulaForCalculatedAttirbute(controlName,
									true, _formula);

					$('#formulaField').val("");

				},

				doesRowExist : function(rowId, tableId) {

					if ($('#' + tableId)[0].rows[rowId]) {
						return true;

					} else {
						return false;
					}
				},

				addSkipRuleToTable : function(skipRule, id) {

					var _tr_element_start = "<tr id = '" + id + "'>";
					var _tr_element_end = "</tr>";
					var _td_element_start = "<td class = 'text_normal ";
					var _td_element_end = "</td>";

					if (this.formulaRowCounter % 2 == 0) {
						_td_element_start += "formulaTableRowEven'>";
					} else {
						_td_element_start += "formulaTableRowOdd'>";
					}
					var controlledAttribsString = "";
					var skipRuleControlledAttribs = skipRule.controlledAttributes
							+ "";
					var controlAttribs = skipRuleControlledAttribs.split(",");
					for ( var cntr = 0; cntr < controlAttribs.length; cntr++) {
						controlledAttribsString += (ControlBizLogic
								.getCaptionFromControlName(controlAttribs[cntr]) + ",");
					}
					controlledAttribsString = controlledAttribsString.substr(0,
							controlledAttribsString.length - 1);
					var _table_row = _tr_element_start
							+ _td_element_start
							+ "<a title = 'edit' href = '#skipRules/"
							+ id
							+ "/edit'\"><span class = 'ui-icon ui-icon-pencil' style= 'float : left;'/></a>"
							+ "<span style= 'float : left;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"
							+ "<a title = 'delete' href = '#skipRules/"
							+ id
							+ "/delete'\"><span class = 'ui-icon ui-icon-trash' style= 'float : left;'/></a>"
							+ _td_element_end
							+ _td_element_start
							+ ControlBizLogic
									.getCaptionFromControlName(skipRule.controllingAttributes)
							+ _td_element_end + _td_element_start
							+ controlledAttribsString + _td_element_end
							+ _td_element_start + skipRule.action
							+ "<input type = 'hidden' value =" + skipRule
							+ " id =skipRule_" + id + ">" + _td_element_end
							+ _tr_element_end;

					if (this.doesRowExist(id, "skipRulesTable")) {
						// edit

						var skipRuleRowRow = $('#skipRulesTable')[0].rows[id];
						skipRuleRowRow.cells[1].innerHTML = ControlBizLogic
								.getCaptionFromControlName(skipRule.controllingAttributes);
						skipRuleRowRow.cells[2].innerHTML = controlledAttribsString;
						skipRuleRowRow.cells[3].innerHTML = skipRule.action;

					} else {
						// add
						$("#skipRulesTableHeader").eq(0).after(_table_row);
						this.formulaRowCounter++;
					}

				},

				removeFormula : function(id) {
					AdvancedControlPropertiesBizLogic
							.setFormulaForCalculatedAttirbute(id, false, null);
					$('#' + id).remove();
					this.formulaRowCounter--;
				},

				setSuccessMessageHeader : function() {
					$("#advancedPropertiesmessagesDiv").html("");
					$("#advancedPropertiesmessagesDiv").removeClass('error');
					$("#advancedPropertiesmessagesDiv").addClass('success');
					$("#advancedPropertiesmessagesDiv").append(
							Utility.messageSpace);
				},

				setErrorMessageHeader : function() {
					$("#advancedPropertiesmessagesDiv").html("");
					$("#advancedPropertiesmessagesDiv").removeClass('success');
					$("#advancedPropertiesmessagesDiv").addClass('error');
					$("#advancedPropertiesmessagesDiv").append(
							Utility.messageSpace);
				},

				clearMessage : function() {
					$("#advancedPropertiesmessagesDiv").html("");
					$("#advancedPropertiesmessagesDiv").removeClass('success');
					$("#advancedPropertiesmessagesDiv").removeClass('error');
				}

			}),

	/*
	 * ControlTab View
	 */
	ControlTabView : Backbone.View.extend({
		initialize : function() {
			_.bindAll(this, 'render');
			this.render();
		},

		render : function() {
			this.$el.html(Templates.templateList['controlTabTemplate']);
		},

		events : {
			"click a.link" : "createControl"
		},

		createControl : function(event) {

			var controlModel = ControlBizLogic.createControl(event.target.id);
			if (Main.currentFieldView != null) {
				// Erase the existing view
				Main.currentFieldView.destroy();
			}
			Main.currentFieldView = Utility.addFieldHandlerMap[event.target.id]
					(controlModel, true, 'controlContainer');
			Utility.resetCarouselControlSelect();
			var pos = Utility.getControlIndexForCarousel(event.target.id);
			var displacement = (pos > 7) ? ((pos < 14) ? (pos % 7) + 1
					: (pos % 7) + 8) : pos;
			Main.carousel.tinycarousel_move(displacement);
			$('#' + Main.currentFieldView.getModel().get('type'))
				.css('background-color', '#F0F0F0 ')
				.css('box-shadow','2px 2px 2px 0px rgba(171, 171, 210, 0.51)');

		}

	}),

	SummaryView : Backbone.View
			.extend({
				initialize : function() {
					_.bindAll(this, 'render');
					this.render();
				},

				events : {
					"keyup #formCaption" : "setCaptionAndName",
					"click #saveForm" : "saveForm",
					"click #deleteForm" : "loadForm",
					"click #exportForm" : "exportForm",
					"click #importFormButton" : "importForm"
				},

				displayFormInfo : function(formInfo) {
					$("#formCaption").val(formInfo.get('caption'));
					$("#formName").val(formInfo.get('formName')).attr('readOnly', 'readOnly');
					$("#createdBy").text(formInfo.get('createdBy'));
					$("#createdOn").text(formInfo.get('createdOn'));
					$("#lastModifiedBy").text(formInfo.get('lastModifiedBy'));
					$("#lastModifiedOn").text(formInfo.get('lastModifiedOn'));
				},

				exportForm : function(event) {
					var formId = Main.formView.getFormModel().get('id');
					if (formId == null || formId == undefined) {
						var message = "In order to export, the form needs to be saved.";
						$("#popupMessageText").html(message);
						$("#dialog-message").dialog('open');
					} else {
						location.href = "../../ExportFormAction.de?containerId="
								+ formId;
					}

				},

				importForm : function(event) {

//					$("#form-import-dialog").dialog({
//						buttons : {
//							Cancel : function() {
//								$(this).dialog("close");
//							}
//						}
//					});
					$("#form-import-dialog").dialog("open");
				},

				loadForm : function(event) {
					Main.formView.loadForm();
				},

				render : function() {
					// this.$el.html(Templates.templateList['summaryTemplate']);
					this.$el.html(Mustache.to_html(
							Templates.templateList['summaryTemplate'],
							this.model.toJSON()));
					$("#form-import-dialog").dialog({
						modal : true,
						autoOpen : false
					}).css("font-size", "15px");

					$("#control-change-dialog").dialog({
						modal : true,
						autoOpen : false
					}).css("font-size", "10px");

					$('#importForm')
							.ajaxForm(
									{
										beforeSend : function() {
											$("#importFileWaitingImage").show();
										},
										success : function(data) {
											var receivedData = JSON.parse(data);
											$("#importFileWaitingImage").hide();
											if (receivedData.status == "success") {
												Routers.formEventsRouterPointer
														.navigate(
																"loadCachedForm/"
																		+ receivedData.containerIds[0]
																		+ "/true",
																true);
												$("#form-import-dialog")
														.dialog("close");
											} else {
												alert("Error importing file.");
											}
										}
									});
					$("#importFileWaitingImage").hide();
					// init global label position
					$("#globalLabelAlignment").buttonset();

					// setting global label position
					$('#global_align_left').click(function() {
						GlobalMemory.globalLabelPosition = "LEFT";
					});

					$('#global_align_top').click(function() {
						GlobalMemory.globalLabelPosition = "TOP";
					});

				},

				saveForm : function(event) {

					var formCaption = $('#formCaption').val();
					if (formCaption == undefined || formCaption == ""
							|| formCaption == null) {
						var message = "Please specify the form's name.";
					/*	$("#popupMessageText").html(message);
						$("#dialog-message").dialog('open'); */
						Utility.notify($("#notifications"), message, "error");
					} else {
						this.model.set({
							formName : $('#formName').val(),
							caption : formCaption
						});
						Main.formView.saveForm(true);
					}
				},

				setCaptionAndName : function(event) {
					Main.treeView.getTree().setItemText(1,
							$('#formCaption').val(), '');
					var formName = Main.formView.getFormModel().getFormInformation().get('formName');
					if (formName == undefined) {
					  $('#formName').val(Utility.toCamelCase($('#formCaption').val()));
					}

				},

				getModel : function() {
					this.model.set({
						formName : $('#formName').val(),
						caption : $('#formCaption').val()
					});
					return this.model;
				}

			}),

	showForm : function(container, formModel) {
		var formView = new Views.FormView({
			el : $("#" + container),
			model : formModel
		});
		return formView;
	},

	showControl : function(container, controlModel) {
		var controlView = new Views.FieldView({
			el : $("#" + container),
			model : controlModel
		});
		return controlView;
	},

	showBody : function() {
		var bodyView = new Views.BodyView();
		return bodyView;
	},

	showControlTab : function(container) {
		var controlTabView = new Views.ControlTabView({
			el : $("#" + container)
		});
		return controlTabView;
	},

	/*
	 * Design Mode View
	 */
//	DesignMode : Backbone.View.extend({
//		layoutGrid : undefined,
//		initialize : function() {
//			$(this.el).unbind("click");// will avoid calling the click event
//			// multiple time on single click
//			_.bindAll(this, 'render');
//			this.render();
//		},
//
//		render : function() {
//			var template = _.template($("#designModeView").html(), {});
//			this.$el.html(template);
//			this.initLayoutGrid();
//
//		},
//
//		events : {
//			"click #addRow" : "addRow",
//			"click #addColumn" : "addColumn"
//		},
//
//		initLayoutGrid : function() {
//			layoutGrid = new dhtmlXGridObject("layoutGrid");
//			layoutGrid
//					.setImagePath("../dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
//			layoutGrid.setHeader(" ");
//			layoutGrid.setInitWidths("300");
//			layoutGrid.setColAlign("center");
//			// layoutGrid.setColSorting(",connector");
//			layoutGrid.setSkin("kspl_csdDesignMode");
//			layoutGrid.setColTypes("ro");
//			layoutGrid._drag_validate = true;
//			layoutGrid.enableDragAndDrop(true);
//			layoutGrid.enableAutoWidth(true, 900, 300);
//			layoutGrid.init();
//		},
//
//		updateControlObjectCollection : function(name, xPosition, seqNumber) {
//			Main.formView.getFormModel().get('controlObjectCollection')[name]
//					.set({
//						xPos : xPosition,
//						sequenceNumber : seqNumber
//					});
//		},
//
//		addRow : function() {
//			var newId = (new Date()).valueOf();
//			Main.designModeViewPointer.getGridObject().addRow(newId, "");
//		},
//		addColumn : function() {
//			var colNum = Main.designModeViewPointer.getGridObject()
//					.getColumnsNum();
//			Main.designModeViewPointer.getGridObject().insertColumn(colNum, '',
//					'ro', '300', '', 'center');
//		},
//
//		getGridObject : function() {
//			return layoutGrid;
//		}
//	})

};

var getDEJson = function (args) {
  var rows = new Array();
  var columns = null;
  var prevRow = null;
  var rowNo = 0;
  var colNo = 0;

  _.each(args.json.attributes.controlObjectCollection, function(field) {
	    // Create NewField with appropriate new DE attributes
    field = field.attributes;
	var newField = getNewField({field : field});
  
    // Adding control specific properties 
    if (newField.type == 'numberField' || newField.type == 'stringTextField' || newField.type == 'textArea') {
      newField['width'] = field.width;
      newField['defaultValue'] = field.defaultValue;
      if (newField.type == 'stringTextField') {
        newField['url'] = field.url;
        newField['password'] = field.password;
      } else if (newField.type == 'numberField') {
        newField['noOfDigits'] = field.noOfDigits;
        newField['noOfDigitsAfterDecimal'] = field.noOfDigitsAfterDecimal;
        newField['minValue'] = field.minValue;
        newField['maxValue'] = field.maxValue;
        newField['calculated'] = field.calculated;
        newField['formula'] = field.formula;
      } else {
        newField['noOfRows'] = field.noOfRows;
        newField['minLength'] = field.minLength;
        newField['maxLength'] = field.maxLength;
      }
    } else if (newField.type == 'combobox' || newField.type == 'radiobutton' || newField.type == 'checkbox' || 
      newField.type == 'listbox' || newField.type == 'multiSelectListbox') {
      newField['dataType'] = field.dataType;
      newField['dateFormat'] = field.dateFormat;
      var pvs = new Array();
      if (field.pvs) {
        for (var pv in field.pvs) {
          pvs.push(field.pvs[pv]);
        }
      }

      newField['pvs'] = pvs;
    } else if (newField.type == 'datePicker') {
      newField['format'] = field.format;
      newField['showCalendar'] = field.showCalendar;
      newField['defaultType'] = field.defaultType;
      newField['defaultValue'] = field.defaultValue;
    } else if (newField.type == 'booleanCheckbox') {
      newField['defaultChecked'] = field.defaultChecked;
    } else if (newField.type == 'label') {
      newField['heading'] = field.heading;
      newField['note'] = field.note;
    } else if (newField.type == 'fancyControl') {
      newField['type'] = newField.fancyControlType;
    }

    if (prevRow == null || prevRow != field.sequenceNumber) {
      columns = new Array();
      colNo = 0;
      rows[rowNo++] = columns;
      prevRow = field.sequenceNumber;
    }

    columns[colNo++] = newField;
  });
  
  var newJson = {};
  newJson['name'] = args.json.attributes.formName;
  newJson['caption'] = args.json.attributes.caption;
  newJson['rows'] = rows;

  return newJson;
};

var getNewField = function(args) { 
  var field = args.field;
  var newField = {};

  newField['name'] = field.controlName;
  newField['udn'] = field.userDefinedName;
  newField['caption'] = field.caption;
  newField['customLabel'] = field.customLabel;
  newField['labelPosition'] = field.labelPosition;
  newField['toolTip'] = field.toolTip;
  newField['skipLogicSourceControl'] = field.skipLogicSourceControl;
  newField['skipLogicTargetControl'] = field.skipLogicTargetControl;
  newField['calculatedSourceControl'] = field.calculatedSourceControl;
  newField['conceptCode'] = field.conceptCode;
  newField['conceptPreferredName'] = field.conceptPreferredName;
  newField['conceptDefinitionSource'] = field.conceptDefinitionSource;
  newField['conceptDefinition'] = field.conceptDefinition;
  newField['validationRules'] = field.validationRules || [];
  newField['fancyControlType'] = field.fancyControlType;

  // Correcting the type properties 
  if (field.type == 'numericField') {
    newField['type'] = 'numberField';
  } else if (field.type == 'radioButton') {
    newField['type'] = 'radiobutton';	
  } else if (field.type == 'checkBox') {
    newField['type'] = 'booleanCheckbox';	
  } else if (field.type == 'multiselectBox' || field.type == 'listBox') {
    newField['type']  = 'listbox';	
  } else if (field.type == 'comboBox') {
    newField['type'] = 'combobox'
  } else if  (field.type == 'multiselectCheckBox') { 
    newField['type'] = 'checkbox'
  } else if (field.type == 'subForm') {
    newField = new getDEJson({json :field.subForm});
    newField['type'] = field.type;
    newField['singleEntry'] = field.singleEntry;
  } else if (field.type == 'note') {
    newField['type']  = 'label';
    newField['note'] = true;
  } else {
    newField['type'] = field.type;
  }

  return newField;
};
