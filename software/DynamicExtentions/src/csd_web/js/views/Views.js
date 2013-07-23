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
				events : {
					"click #saveFormID" : "saveModel",
					"click #newForm" : "loadForm",
					"keyup #formCaption" : "setTreeCaption"
				},
				loadForm : function() {

					Routers.formEventsRouterPointer.navigate("loadCachedForm/"
							+ $('#formCaption').val(), true);

				},
				saveModel : function(event) {

					// Save Model
					// alert(JSON.stringify(this.model.toJSON()));
					this.populateControlsInForm();
					$("#formWaitingImage").show();
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
												$("#popupMessageText").html(
														message);
												$("#dialog-message").dialog(
														'open');
											} else {
												$("#formWaitingImage").hide();
												$("#popupMessageText")
														.html(
																"Could not save theform successfully.");
												$("#dialog-message").dialog(
														'open');
											}
										},
										error : function(model, response) {
											$("#formWaitingImage").hide();
											$("#popupMessageText")
													.html(
															"Could not save theform successfully.");
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
							Routers.loadPreview();
						}
					});
				},

				populateControlsInForm : function() {
					this.model.set({
						caption : $('#formCaption').val(),
						controlCollection : new Array()
					});
					if (this.model.get('controlObjectCollection') == null) {
						this.model.set({
							controlObjectCollection : {}
						});
					}

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
								this.model.get('controlObjectCollection')[key]
										.get('subForm')
										.get('controlCollection').push(
												subControl.toJSON());
							}
						}
						this.model.get('controlCollection').push(
								control.toJSON());
					}

				},
				setTreeCaption : function(event) {
					Main.treeView.getTree().setItemText(1,
							$('#formCaption').val(), '');
				},
				getFormModel : function() {

					return this.model;
				},
				render : function() {
					this.$el.html(Mustache.to_html(
							Templates.templateList['formTemplate'], this.model
									.toJSON()));
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
				messageSpace : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",

				initialize : function() {
					_.bindAll(this, 'render');
					this.render();// self-rendering
				},
				events : {
					"click #createControlButtonid" : "updateModel",
					"click #addPv" : "addPv",
					"click #deletePv" : "deletePv",
					"click #deleteControlButtonid" : "deleteControl"
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
												Routers
														.deleteControl(GlobalMemory.currentBufferControlModel);
												$(this).dialog("close");
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
					this.model = Routers.updatePVs(this.model);

					this.showMessages(this.model.validate(this.model.toJSON()));

				},

				showMessages : function(validationMessages) {

					if (validationMessages.length == 0) {

						var displayLabel = $('#controlCaption').val() + " ("
								+ $('#controlName').val() + ")";
						var url = "createCachedControl/" + displayLabel
								+ "/control" + $('#controlName').val();

						Routers.createControlNode(displayLabel, $(
								'#controlName').val());

						var status = Routers.updateCachedControl(this.model);
						if (status == "save" || status == "update") {
							this.setSuccessMessageHeader();
							$("#messagesDiv").append(
									this.messageSpace
											+ this.model.get('caption')
											+ " was saved successfully.");
							$('#createControlButtonid').attr("disabled", true);
							$('#addPv').prop("disabled", true);
							$('#deletePv').prop("disabled", true);
						}

					} else {
						this.setErrorMessageHeader();
						for ( var key in validationMessages) {
							$("#messagesDiv").append(
									this.messageSpace + "! "
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
							this.messageSpace + "<b>Successful</b><br>");
				},

				setErrorMessageHeader : function() {
					$("#messagesDiv").html("");
					$("#messagesDiv").removeClass('success');
					$("#messagesDiv").addClass('error');
					$("#messagesDiv").append(
							this.messageSpace + "<b>Error(s)</b><br>");
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

				},

				renderPvUI : function() {
					$('#permissibleValuesForm')
							.ajaxForm(
									{
										/*
										 * beforeSend: function() {
										 * status.empty(); var percentVal =
										 * '0%'; bar.width(percentVal)
										 * percent.html(percentVal); },
										 * uploadProgress: function(event,
										 * position, total, percentComplete) {
										 * var percentVal = percentComplete +
										 * '%'; bar.width(percentVal)
										 * percent.html(percentVal); }, success:
										 * function() { var percentVal = '100%';
										 * bar.width(percentVal)
										 * percent.html(percentVal); },
										 */
										beforeSend : function() {
											$("#pvFileWaitingImage").show();
										},
										complete : function(xhr) {
											var receivedData = $
													.parseJSON(xhr.responseText);
											$("#pvFileWaitingImage").hide();
											if (receivedData.status == "saved") {
												Routers
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

				setControlName : function() {
					if (this.model.get('status') == "new") {
						$('#controlName')
								.val(
										Utility
												.toCamelCase($(
														'#controlCaption')
														.val()));
					}
				},

				getModel : function() {
					return this.model;
				}
			}),

	/*
	 * Tree View
	 */

	TreeView : Backbone.View.extend({
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
					+ "<item text='New Form' id='1' child='1' >"
					+ "<userdata name='system'>true</userdata>" + "</item> "
					+ "</tree>";
			tree.loadXMLString(xml);
			tree.openAllItems(0);
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
				initialize : function() {
					_.bindAll(this, 'render'); // fixes loss of context for
					// 'this' within methods
					this.render();// self-rendering
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
						Routers.csdControlsTabSelectHandler(id);
						return true;
					});
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
					// Render JQuery Toggle Buttons
					$("#advancedControlsRadio").buttonset();
					// Hide Skip logic tab
					$('#skipLogicTab').hide();
					$('#calculatedAttributeTab').show();
					// toggle between tabs.
					$("input:radio").click(function() {
						$('#skipLogicTab').toggle();
						$('#calculatedAttributeTab').toggle();
					});
					// Render the custom formula field
					this.formulaField = new K_AutoComplete({
						inputElementId : "formulaField",
						data : Routers.getListOfCurrentControls()
					});
				},

				events : {
					"click #addFormulaBtn" : "addFormula"
				},

				refreshFormulaField : function() {
					this.formulaField = new K_AutoComplete({
						inputElementId : "formulaField",
						data : Routers.getListOfCurrentControls()
					});
				},

				correctFormulaTableCSS : function() {
					GlobalMemory.formulaCounter = 0;
					$('#formulaTable tr').each(function() {
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
					if (controlName != null && controlName != undefined) {
						if (this.doesFormulaForControlExist(controlName)) {
							$("#popupMessageText").html(
									"A formula exists for the given control");
							$("#dialog-message").dialog('open');

						} else {
							var _tr_element_start = "<tr id = '" + controlName
									+ "'>";
							var _tr_element_end = "</tr>";
							var _td_element_start = "<td class = 'text_normal ";
							var _td_element_end = "</td>";

							if (this.formulaRowCounter % 2 == 0) {
								_td_element_start += "formulaTableRowEven'>";
							} else {
								_td_element_start += "formulaTableRowOdd'>";
							}

							var _formula = $('#formulaField').val();
							var _table_row = _tr_element_start
									+ _td_element_start
									+ controlName
									+ _td_element_end
									+ _td_element_start
									+ _formula
									+ _td_element_end
									+ _td_element_start
									+ "<a title = 'edit' href = '#calculatedAttribute/"
									+ controlName
									+ "/edit'\"><span class = 'ui-icon ui-icon-pencil' style= 'float : left;'/></a>"
									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+ "<a title = 'delete' href = '#calculatedAttribute/"
									+ controlName
									+ "/delete'\"><span class = 'ui-icon ui-icon-trash' style= 'float : left;'/></a>"
									+ _td_element_end + _tr_element_end;

							$("#formulaTableHeader").eq(0).after(_table_row);

							// add formula and set calculated
							Routers.setFormulaForCalculatedAttirbute(
									controlName, true, _formula);
							// remove from drop down
							/*
							 * $( "#availableFields1 option[value='" +
							 * controlName + "']").remove();
							 */
							$('#formulaField').val("");
							this.formulaRowCounter++;
							this.correctFormulaTableCSS();

						}
					}

				},

				removeFormula : function(id) {
					Routers.setFormulaForCalculatedAttirbute(id, false, null);
					$('#' + id).remove();
					this.formulaRowCounter--;
				},

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

			Routers.createControl(event.target.id);
			Utility.resetCarouselControlSelect();
			var pos = Utility.getControlIndexForCarousel(event.target.id);
			Main.carousel.tinycarousel_move(pos > 8 ? (pos % 8) + 1 : pos);
			$('#' + Main.currentFieldView.getModel().get('type')).css(
					'background-color', '#F0F0F0 ');
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