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
					this.model.set({
						caption : $('#formCaption').val(),
						controlCollection : new Array()
					});
					if (this.model.get('controlObjectCollection') == null) {
						this.model.set({
							controlObjectCollection : new Array()
						});
					}
					// Set controls
					for ( var key in this.model.get('controlObjectCollection')) {
						this.model.get('controlCollection').push(
								this.model.get('controlObjectCollection')[key]
										.toJSON());
					}
					// Save Model
					alert(JSON.stringify(this.model.toJSON()));
					this.model.save({}, {
						wait : true,
						success : function(model, response) {
							Routers.updateCachedFormMethod(model);
							alert(model.get('status'));
						}
					});
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
				}
			}),

	/*
	 * Body View
	 */
	BodyView : Backbone.View.extend({
		el : 'body',
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
	FieldView : Backbone.View.extend({

		initialize : function() {
			_.bindAll(this, 'render');
			this.render();// self-rendering
		},
		events : {
			"click #createControlButtonid" : "updateModel",
			"keyup #controlCaption" : "setControlName"
		},

		destroy : function() {
			this.undelegateEvents();
			this.$el.empty();
			this.stopListening();

		},
		updateModel : function(event) {
			this.model.set({
				caption : $('#controlCaption').val(),
				defaultValue : $('#defaultValue').val(),
				controlName : $('#controlName').val(),
				conceptDefinitionSource : $('#conceptDefinitionSource').val(),
				conceptCode : $('#conceptCode').val(),
				conceptDefinition : $('#conceptDefinition').val(),
				conceptPreferredName : $('#conceptPreferredName').val(),
				noOfDigits : $('#noOfDigits').val(),
				noOfDigitsAfterDecimal : $('#noOfDigitsAfterDecimal').val(),
				maximumValue : $('#maximumValue').val(),
				minimumValue : $('#minimumValue').val(),
				width : $('#width').val(),
				numberOfCharacters : $('#numberOfCharacters').val(),
				noOfDigits : $('#noOfDigits').val(),
				pvs : $('#pvs').val()
			});

			Routers.formEventsRouterPointer.navigate("createCachedControl/"
					+ $('#controlName').val(), true);

			Routers.updateCachedControl(this.model);

		},

		render : function() {
			this.$el.html(Mustache.to_html(this.model.get('template'),
					this.model.toJSON()));
			$("#dataType").val(this.model.get('dataType')).prop('selected',
					true);
			if (this.model.get('status') == "saved") {
				$('#controlName').prop('readonly', 'readonly');
			}

		},

		setControlName : function() {
			if (this.model.get('status') == "new") {
				$('#controlName').val(
						Utility.toCamelCase($('#controlCaption').val()));
			}
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
			tree.enableDragAndDrop(true);
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

				initialize : function() {
					_.bindAll(this, 'render'); // fixes loss of context for
					// 'this' within methods
					this.render();// self-rendering
				},

				render : function() {

					/*var tabbar = new K_Tabbar(
							{
								container : this.el,
								allignment : 'top',
								skin : 'dhx_terrace',
								imagesPath : 'csd_web/dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/',
								tabsConfiguration : [ {
									id : 'a11',
									label : 'Summary',
									labelWidth : '150px',
									data : 'summary'
								} ]
							}, 'dhtmlx');*/

					var tabbar1 = new dhtmlXTabBar(this.el, "top");
					tabbar1.setSkin('dhx_terrace');
					tabbar1
							.setImagePath("csd_web/dhtmlxSuite_v35/dhtmlxTabbar/codebase/imgs/");
					tabbar1.addTab("a11", "Summary", "150px");
					tabbar1.addTab("a21", "Add/Edit Control", "150px");
					tabbar1.addTab("a31", "Design", "150px");
					tabbar1.addTab("a41", "Preview", "150px");
					tabbar1.setContent("a11", "summary");
					tabbar1.setContent("a21", "control");
					tabbar1.setContent("a31", "design");
					tabbar1.setContent("a41", "preview");
					tabbar1.setTabActive("a11");
					tabbar1.attachEvent("onSelect", function(id, last_id) {
						if (id == "a31") {
							var designMode = new Views.DesignMode({
								el : $("#design")
							});
						}
						return true;
					});

				},

				getTabBar : function() {
					return this.tab;
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
			Routers.createControl(event.target.id);
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
		columnsInDataView : 3,
		rowInDataView : 1,
		dataViewMatrix : new Array(),

		initialize : function() {
			_.bindAll(this, 'render');
			this.render();
		},

		render : function() {
			var template = _.template($("#designModeView").html(), {});
			this.$el.html(template);
			this.initLayoutGrid();
			this.populateLayoutGrid();
		},
		events : {
			"click #addRow" : "addRow",
			"click #addColumn" : "addColumn",
		},

		initLayoutGrid : function() {
			layoutGrid = new dhtmlXGridObject("layoutGrid");
			layoutGrid
					.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
			layoutGrid.setHeader(" , , ");
			layoutGrid.setInitWidths("300,300,300");
			layoutGrid.setColAlign("center,center,center");
			layoutGrid.setColSorting(",connector");
			layoutGrid.setSkin("kspl_csdDesignMode");
			layoutGrid.setColTypes("ro,ro,ro");
			layoutGrid._drag_validate = true;
			layoutGrid.enableDragAndDrop(true);
			layoutGrid.enableAutoWidth(true);

			layoutGrid.init();

			layoutGrid.attachEvent("onBeforeDrag", function(sId, sInd) {
				layoutGrid.rowToDragElement = function(id) {
					var text = layoutGrid.cells(sId, sInd).getValue();// prepare
					// a
					// text
					// string
					if (text == "") {
						text = "Empty";
					}
					return text;
				}
				return true;
			});

			layoutGrid.attachEvent("onDrag",
					function(sId, tId, sObj, tObj, sInd, tInd) {
						try {
							var sourceCell = layoutGrid.cells(sId, sInd);
							var targetCell = layoutGrid.cells(tId, tInd);

							var targetValue = targetCell.getValue();
							targetCell.setValue(sourceCell.getValue());
							sourceCell.setValue(targetValue);

							var seqNumber = layoutGrid.getRowIndex(tId) + 1;
							var xPosition = tInd + 1;

							layoutGrid.rowToDragElement = function(id) {// sets
								// the
								// cell
								// text
								// of
								// the
								// drag
								// element
								var text = layoutGrid.cells(sId, sInd)
										.getValue();
								return text;
							}

							// alert("value = "+targetCell.getValue() + "
							// xPosition =" + xPosition +" seqNumber = " +
							// seqNumber);
							Main.formView.getFormModel().get(
									'controlObjectCollection')[targetCell
									.getValue()].set({
								xPos : xPosition,
								sequenceNumber : seqNumber
							});
							/*
							 * Main.formView.getFormModel().get(
							 * 'controlObjectCollection')[targetCell
							 * .getValue()].save({}, { url :
							 * 'csdApi/form/control/' + targetCell.getValue(),
							 * wait : true, success : function(model, response) { //
							 * alert(model.get('status')) } });
							 */
						} catch (e) {
						}
					});
		},

		populateLayoutGrid : function() {
			if (Main.formView.getFormModel()
					&& Main.formView.getFormModel().get(
							'controlObjectCollection') != null) {
				for ( var index in Main.formView.getFormModel().get(
						'controlObjectCollection')) {
					var xPos = Main.formView.getFormModel().get(
							'controlObjectCollection')[index].get("xPos");
					var seqNumber = Main.formView.getFormModel().get(
							'controlObjectCollection')[index]
							.get("sequenceNumber");
					var name = Main.formView.getFormModel().get(
							'controlObjectCollection')[index]
							.get("controlName");
					// alert("xpos= " + xPos + " seqNumber = " + seqNumber + "
					// name= " + name + " gridRows = " +
					// layoutGrid.getRowsNum());

					if (layoutGrid.getRowsNum() < seqNumber) {
						while (layoutGrid.getRowsNum() <= seqNumber - 1) {
							var newId = (new Date()).valueOf();
							layoutGrid.addRow(newId, "");
						}
						layoutGrid.cellByIndex(seqNumber - 1, xPos - 1)
								.setValue(name);
					}
					layoutGrid.cellByIndex(seqNumber - 1, xPos - 1).setValue(
							name);
				}
			}
		},

		addRow : function() {
			var newId = (new Date()).valueOf();
			layoutGrid.addRow(newId, "");
		},
		addColumn : function() {
			layoutGrid.insertColumn(0, '', 'ro', '300');
		}
	})

}