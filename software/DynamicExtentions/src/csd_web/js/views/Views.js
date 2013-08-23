var Views = {

	/*
	 * Form View
	 */
	FormView : Backbone.View.extend({
		initialize : function() {
			_.bindAll(this, 'render'); // fixes loss of context for
			this.render();// self-rendering
		},
		loadForm : function() {

			Routers.formEventsRouterPointer.navigate("loadCachedForm/"
					+ $('#formCaption').val(), true);

		},
		saveForm : function() {

			// Save Model
			// alert(JSON.stringify(this.model.toJSON()));
			this.populateControlsInForm();
			$("#formWaitingImage").show();
			// set form info from summary
			this.model.setFormInformation(Main.mainTabBarView
					.getFormSummaryView().getModel());
			
			
			this.model.save({
				save : "yes"
			}, {
				wait : true,
				success : function(model, response) {
					if (model.get("status") == "saved") {
						
						Routers.updateCachedFormMethod(model);
						$("#formWaitingImage").hide();
						var message = model.get('caption')
								+ " was saved successfully.";
						$("#popupMessageText").html(message);
						$("#dialog-message").dialog('open');
					} else {
						$("#formWaitingImage").hide();
						$("#popupMessageText").html(
								"Could not save the form successfully.");
						$("#dialog-message").dialog('open');
					}
				},
				error : function(model, response) {
					$("#formWaitingImage").hide();
					$("#popupMessageText").html(
							"Could not save the form successfully.");
					$("#dialog-message").dialog('open');
				}

			});
		},

		loadModelInSessionForPreview : function() {
			$("#formWaitingImage").show();
			this.populateControlsInForm();
			this.model.save({
				save : "no"
			}, {
				wait : true,
				success : function(model, response) {
					FormBizLogic.loadPreview();
				}
			});
		},

		loadModelInSession : function() {
			$("#formWaitingImage").show();
			this.populateControlsInForm();
			this.model.save({
				save : "no"
			}, {
				wait : true,
				success : function(model, response) {
					$("#formWaitingImage").hide();
				}
			});

		},

		populateControlsInForm : function() {
			this.model.set({
				controlCollection : new Array()
			});

			// Set controls
			for ( var key in this.model.get('controlObjectCollection')) {
				var control = this.model.get('controlObjectCollection')[key];

				if (control.get('subForm') != undefined) {
					this.model.get('controlObjectCollection')[key].get(
							'subForm').set({
						controlCollection : new Array()
					});
					for ( var subKey in control.get('subForm').get(
							'controlObjectCollection')) {
						var subControl = control.get('subForm').get(
								'controlObjectCollection')[subKey];
						var subControlTemplate = subControl.get('template');
						subControl.set({template : ""});
						this.model.get('controlObjectCollection')[key].get(
								'subForm').get('controlCollection').push(
								subControl.toJSON());
						subControl.set({template : subControlTemplate});
					}
				}
				var controlTemplate = control.get('template');
				control.set({template : ""});
				this.model.get('controlCollection').push(control.toJSON());
				control.set({template : controlTemplate});
			}

		},

		getFormModel : function() {

			return this.model;
		},
		render : function() {
			/*
			 * this.$el.html(Mustache.to_html(
			 * Templates.templateList['formTemplate'], this.model .toJSON()));
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
					"click #addPv" : "addPv",
					"click #deletePv" : "deletePv",
					"click #deleteControlButtonid" : "deleteControl",
					"keyup #controlCaption" : "setAttributeName"
				},

				setAttributeName : function(event) {
					$('#attributeName').val(
							Utility.toCamelCase($('#controlCaption').val()));
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

				updateModel : function(event) {
					this.populateFields();
					var validationMessages = this.model.validate(this.model
							.toJSON());
					var status = "error";
					if (validationMessages.length == 0) {
						var displayLabel = $('#controlCaption').val() + " ("
								+ $('#controlName').val() + ")";
						status = ControlBizLogic.createControlNode(
								displayLabel, $('#controlName').val(),
								this.model.get('type'), this.model);
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

				populateFields : function() {
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
								noOfDigits : $('#noOfDigits').val(),
								noOfDigitsAfterDecimal : $(
										'#noOfDigitsAfterDecimal').val(),
								maximumValue : $('#maximumValue').val(),
								minimumValue : $('#minimumValue').val(),
								width : $('#width').val(),
								dataType : $('#dataType').val(),
								format : $('#format').val(),
								isPHI : $('#isPHI').is(":checked"),
								isMandatory : $('#isMandatory').is(":checked"),
								isAutoCalculate : $('#autoCalculate').is(
										":checked"),
								toolTip : $('#toolTip').val(),
								subFormName : $('#subFormName').val()
							});
					// set pvs
					this.model.set({
						pvs : Main.currentFieldView.getModel().get('pvs'),
						pvFile : Main.currentFieldView.getModel().get('pvFile')
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
							+ pageBreak.get('controlName') + ")", pageBreak
							.get('controlName'), "pageBreak", pageBreak);
				},

				showMessages : function(validationMessages, status) {

					if (validationMessages.length == 0) {

						if (status == "save" || status == "update") {
							this.setSuccessMessageHeader();
							$("#messagesDiv")
									.append(
											Utility.messageSpace
													+ this.model.get('caption')
													+ " added to the form successfully.");
							$('#createControlButtonid').attr("disabled", true);
							$('#addPv').prop("disabled", true);
							$('#deletePv').prop("disabled", true);
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
					$("#messagesDiv").append(
							Utility.messageSpace + "<b>Successful</b><br>");
				},

				setErrorMessageHeader : function() {
					$("#messagesDiv").html("");
					$("#messagesDiv").removeClass('success');
					$("#messagesDiv").addClass('error');
					$("#messagesDiv").append(
							Utility.messageSpace + "<b>Error(s)</b><br>");
				},

				render : function() {
					this.$el.html(Mustache.to_html(this.model.get('template'),
							this.model.toJSON()));
					// clear messages div
					$("#messagesDiv").html("");
					$("#messagesDiv").removeClass('success');
					$("#messagesDiv").removeClass('error');
					$('#isPHI').prop('checked', this.model.get('isPHI'));
					$('#isMandatory').prop('checked',
							this.model.get('isMandatory'));
					$('#autoCalculate').prop('checked',
							this.model.get('isAutoCalculate'));

					// data type
					$("#dataType").val(this.model.get('dataType')).prop(
							'selected', true);
					// format
					$("#format").val(this.model.get('format')).prop('selected',
							true);
					// make short code readonly
					$('#controlName').prop('readonly', 'readonly');

					switch ((this.model.get('type'))) {
					case "radioButton":

					case "listBox":

					case "multiselectBox":

					case "multiselectCheckBox":
						this.renderPvUI();
						break;
					default:
					}
					// init page break
					$("#addPageBreak").buttonset();

				},

				renderPvUI : function() {
					$('#permissibleValuesForm')
							.ajaxForm(
									{
										beforeSend : function() {
											$("#pvFileWaitingImage").show();
										},
										complete : function(xhr) {
											var receivedData = $
													.parseJSON(xhr.responseText);
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
							.setImagePath("csd_web/dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
					this.pvGrid
							.setHeader("<span id = 'header1'>Value</span>,<span id='header2'>Numeric Code</span>,"
									+ "<span id='header3'>Definition</span>,<span id='header4'>Definition Source</span>,"
									+ "<span id='header5'>Concept Code</span>,status");
					this.pvGrid.setInitWidths("100,100,150,130,*,0");
					// this.pvGrid.setColAlign("right,left");
					this.pvGrid.setColTypes("ed,ed,ed,ed,ed,ro");
					this.pvGrid.enableMultiselect(true);
					// this.pvGrid.setColSorting("str,str");
					this.pvGrid.setSkin("clear");
					this.pvGrid.selMultiRows = true;
					this.pvGrid.enablePaging(true, 10, 3, "recinfoArea");
					this.pvGrid.setPagingSkin("bricks");
					this.pvGrid.enableAlterCss("evenGridRow", "unevenGridRow");
					this.pvGrid
							.attachEvent(
									"onCellChanged",
									function(rId, cInd, nValue) {
										// alert(rId + ' ' + cInd + ' ' +
										// nValue);
										if (Main.currentFieldView.getModel()
												.get('pvs')[rId] == undefined) {
											// not decided
										} else {
											Main.currentFieldView.getModel()
													.get('pvs')[rId] = Main.currentFieldView
													.setpvPropertyBasedOnIndex(
															cInd,
															Main.currentFieldView
																	.getModel()
																	.get('pvs')[rId],
															nValue);
										}

									});
					this.pvGrid.init();

					for ( var i = 1; i < 6; i++) {
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
					this.pvGrid.addRow(this.model.get('controlName')
							+ GlobalMemory.pvCounter, ",,,,,new");
					this.model.get('pvs')[this.model.get('controlName')
							+ GlobalMemory.pvCounter] = this
							.generatePvFromGridData('', '', '', '', '', "add");
					this.pvGrid.selectRowById(this.model.get('controlName')
							+ GlobalMemory.pvCounter);
					GlobalMemory.pvCounter++;

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
					$('#createControlButtonid').val('Update Conrol');
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
					tree.setImagePath("csd_web/dhtmlxSuite_v35/dhtmlxTree/"
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
					Main.carousel.tinycarousel_move(pos > 7 ? (pos % 7) + 1
							: pos);
					$('#' + Main.currentFieldView.getModel().get('type')).css(
							'background-color', '#F0F0F0 ');
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
							.setImagePath("csd_web/dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
					this.tab.addTab("summaryTab", "Summary", "150px");
					this.tab.addTab("controlTab", "Add/Edit Control", "150px");
					this.tab.addTab("advancedControlPropertiesTab",
							"Advanced Options", "150px");
					this.tab.addTab("designMode", "Design", "150px");
					this.tab.addTab("previewTab", "Preview", "150px");
					this.tab.setContent("summaryTab", "summary");
					this.tab.setContent("controlTab", "control");
					this.tab.setContent("designMode", "design");
					this.tab.setContent("previewTab", "preview");
					// this.tab.setContentHref("previewTab",
					// "csd_web/pages/preview.html");
					this.tab.setContent("advancedControlPropertiesTab",
							"advancedControlProperties");

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
							url : "csdApi/form/currentuser",
							async : false
						}).responseText;
						var today = new Date();
						result = $.parseJSON(result);
						formInfo.set({
							createdBy : result.userName,
							createdOn : today.getMonth() + "/" + today.getDay()
									+ "/" + today.getFullYear()
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

					$('#controllingField')
							.change(
									function() {
										// based on control type show hide pvs
										var control = ControlBizLogic
												.getControlFromControlName($(
														'#controllingField')
														.val());
										// clear the messages
										Main.advancedControlsView
												.clearMessage();

										switch (control.get('type')) {
										case "radioButton":

										case "listBox":

										case "multiselectBox":

										case "multiselectCheckBox":
											$('#controllingValuesDiv').hide();
											$('#pvDiv').show();
											AdvancedControlPropertiesBizLogic
													.populatePvSelectBoxWithControlNames(
															'pvs', control);
											$("#pvs").trigger("liszt:updated");
											break;
										case "numericField":
											$('#pvDiv').hide();
											$('#controllingValuesDiv').show();
											break;
										default:
											$('#pvDiv').hide();
											$('#controllingValuesDiv').hide();
										}
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
					$("#skipLogic").click(function() {
						$('#calculatedAttributeTab').hide();
						$('#skipLogicTab').show();
						$("#controlledField").trigger("liszt:updated");
						// populate skip logic

					});

					$("#calculatedAttribute").click(function() {
						$('#skipLogicTab').hide();
						$('#calculatedAttributeTab').show();
						// populate calculated attribute
					});

					// Render the custom formula field
					this.formulaField = new K_AutoComplete({
						inputElementId : "formulaField",
						data : ControlBizLogic
								.getListOfCurrentNumericControls(false)
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
						location.href = "/csdApi/form/permissibleValues/"
								+ control.get('controlName');
					}
				},

				refreshFormulaField : function() {
					this.formulaField = new K_AutoComplete({
						inputElementId : "formulaField",
						data : ControlBizLogic
								.getListOfCurrentNumericControls(false)
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

						case "multiselectCheckBox":

							AdvancedControlPropertiesBizLogic
									.populatePvSelectBoxWithControlNames(
											'subsetPvs', control);
							AdvancedControlPropertiesBizLogic
									.populatePvSelectBoxWithControlNames(
											'defaultPv', control);

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
										complete : function(xhr) {
											var receivedData = $
													.parseJSON(xhr.responseText);
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
					GlobalMemory.formulaCounter = 0;
					$('#' + tableId + ' tr').each(function() {
						$(this).removeClass('formulaTableRowEven');
						$(this).removeClass('formulaTableRowOdd');
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
					if (controlName != null && controlName != undefined) {
						if (this.doesFormulaForControlExist(controlName)) {
							$("#popupMessageText").html(
									"A formula exists for the given control");
							$("#dialog-message").dialog('open');

						} else {
							this.addFormulaToTable(controlName, _formula);
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

					// validate

					var skipRule = this.generateSkipRuleFromData(
							controlledAttributes, controlName, pvs,
							controllingConditon, controllingValues, action,
							defaultPv, pvSubSet, pvSubsetFile, allAny);
					var validationMessages = this.validateSkipRule(skipRule);

					if (validationMessages.length == 0) {
						this.addSkipRuleToTable(skipRule,
								GlobalMemory.skipRulesCounter);
						AdvancedControlPropertiesBizLogic
								.setSkipRuleForAttirbute(controlName, skipRule);
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

				addFormulaToTable : function(controlName, _formula) {
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
							+ _td_element_end + _td_element_start + controlName
							+ _td_element_end + _td_element_start + _formula
							+ _td_element_end + _tr_element_end;

					$("#formulaTableHeader").eq(0).after(_table_row);

					// add formula and set calculated
					AdvancedControlPropertiesBizLogic
							.setFormulaForCalculatedAttirbute(controlName,
									true, _formula);

					$('#formulaField').val("");
					this.formulaRowCounter++;
					this.setTableCss('formulaTable');
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

					$("#skipRulesTableHeader").eq(0).after(_table_row);

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
							Utility.messageSpace + "<b>Successful</b><br>");
				},

				setErrorMessageHeader : function() {
					$("#advancedPropertiesmessagesDiv").html("");
					$("#advancedPropertiesmessagesDiv").removeClass('success');
					$("#advancedPropertiesmessagesDiv").addClass('error');
					$("#advancedPropertiesmessagesDiv").append(
							Utility.messageSpace + "<b>Error(s)</b><br>");
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
			"click a" : "createControl"
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
			Main.carousel.tinycarousel_move(pos > 7 ? (pos % 7) + 1 : pos);
			$('#' + Main.currentFieldView.getModel().get('type')).css(
					'background-color', '#F0F0F0 ');

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

				exportForm : function(event) {
					var formId = Main.formView.getFormModel().get('id');
					if (formId == null || formId == undefined) {
						var message = "In order to export, the form needs to be saved.";
						$("#popupMessageText").html(message);
						$("#dialog-message").dialog('open');
					} else {
						location.href = "ExportFormAction.de?containerId="
								+ formId;
					}

				},

				importForm : function(event) {

					$("#form-import-dialog").dialog({
						buttons : {
							Cancel : function() {
								$(this).dialog("close");
							}
						}
					});
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
					}).css("font-size", "10px");

					$('#importForm')
							.ajaxForm(
									{
										beforeSend : function() {
											$("#importFileWaitingImage").show();
										},
										complete : function(xhr) {
											var receivedData = $
													.parseJSON(xhr.responseText);
											$("#importFileWaitingImage").hide();
											if (receivedData.status == "success") {
												Routers.formEventsRouterPointer
														.navigate(
																"loadCachedForm/"
																		+ receivedData.containerIds[0],
																true);
												$("#form-import-dialog")
														.dialog("close");
											} else {
												alert("Error importing file.");
											}
										}
									});
					$("#importFileWaitingImage").hide();
				},

				saveForm : function(event) {
					this.model.set({
						formName : $('#formName').val(),
						caption : $('#formCaption').val()
					});
					Main.formView.saveForm();
				},

				setCaptionAndName : function(event) {
					Main.treeView.getTree().setItemText(1,
							$('#formCaption').val(), '');
					$('#formName').val(
							Utility.toCamelCase($('#formCaption').val()));

				},

				getModel : function() {
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
	DesignMode : Backbone.View.extend({
		layoutGrid : undefined,
		initialize : function() {
			$(this.el).unbind("click");// will avoid calling the click event
			// multiple time on single click
			_.bindAll(this, 'render');
			this.render();
		},

		render : function() {
			var template = _.template($("#designModeView").html(), {});
			this.$el.html(template);
			this.initLayoutGrid();

		},

		events : {
			"click #addRow" : "addRow",
			"click #addColumn" : "addColumn",
		},

		initLayoutGrid : function() {
			layoutGrid = new dhtmlXGridObject("layoutGrid");
			layoutGrid
					.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
			layoutGrid.setHeader(" ");
			layoutGrid.setInitWidths("300");
			layoutGrid.setColAlign("center");
			// layoutGrid.setColSorting(",connector");
			layoutGrid.setSkin("kspl_csdDesignMode");
			layoutGrid.setColTypes("ro");
			layoutGrid._drag_validate = true;
			layoutGrid.enableDragAndDrop(true);
			layoutGrid.enableAutoWidth(true, 900, 300);
			layoutGrid.init();
		},

		updateControlObjectCollection : function(name, xPosition, seqNumber) {
			Main.formView.getFormModel().get('controlObjectCollection')[name]
					.set({
						xPos : xPosition,
						sequenceNumber : seqNumber
					});
		},

		addRow : function() {
			var newId = (new Date()).valueOf();
			Main.designModeViewPointer.getGridObject().addRow(newId, "");
		},
		addColumn : function() {
			var colNum = Main.designModeViewPointer.getGridObject()
					.getColumnsNum();
			Main.designModeViewPointer.getGridObject().insertColumn(colNum, '',
					'ro', '300', '', 'center');
		},

		getGridObject : function() {
			return layoutGrid;
		}
	})

}