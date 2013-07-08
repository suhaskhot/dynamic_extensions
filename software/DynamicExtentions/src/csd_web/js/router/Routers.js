var Routers = {

	formEventsRouterPointer : null,
	controlEventsRouterPointer : null,
	updateCachedFormMethod : null,

	updateCachedControl : function(model) {
		if (Main.formView.getFormModel().get('controlObjectCollection')[model
				.get('controlName')] != undefined) {
			delete Main.formView.getFormModel().get('controlObjectCollection')[model
					.get('controlName')];
		}
		Main.formView.getFormModel().get('controlObjectCollection')[model
				.get('controlName')] = model;
	},

	createControl : function(controlType) {

		var controlModel = new Models.Field({
			type : controlType,
			defaultValue : 'default',
			sequenceNumber : Main.sequenceNumCntr
		});
		Main.sequenceNumCntr++;
		if (Main.currentFieldView != null) {
			Main.currentFieldView.destroy();
		}
		Main.currentFieldView = Utility.addFieldHandlerMap[controlType](
				controlModel, true, 'controlContainer');
	},

	ControlRouter : Backbone.Router
			.extend({
				routes : {
					"showCachedControl/:name" : "showControl",
					"createCachedControl/:name" : "createControl"
				},

				showControl : function(name) {
					var fieldModel = Main.formView.getFormModel().get(
							'controlObjectCollection')[name];
					Views.showControl('controlContainer', fieldModel);
				},

				createControl : function(name) {
					if (Main.formView.getFormModel().get(
							'controlObjectCollection') == null) {
						Main.formView.getFormModel().set({
							controlObjectCollection : new Array()
						});
					}

					if (Main.formView.getFormModel().get(
							'controlObjectCollection')[name] == undefined) {
						Main.treeView.getTree().insertNewChild(1,
								10 + Main.nodeCounter, name, 0, 0, 0, 0,
								"CHILD,CHECKED");
						Main.nodeCounter++;
					}

				}

			}),

	FormRouter : Backbone.Router
			.extend({
				routes : {
					"loadCachedForm/:id" : "loadForm"
				},
				initialize : function() {
					Routers.updateCachedFormMethod = this.updateFormAndUI;
				},

				loadForm : function(id) {

					if (Main.formView == null) {
						Main.formView = Views.showForm('formTab',
								new Models.Form());

					}
					Main.formView.getFormModel().fetch({
						url : 'csdApi/form/' + id,
						success : this.loadFormSuccessHandler
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
				},

				loadControlsInModelAndTree : function(model) {
					for ( var cntr = 0; cntr < model.get('controlCollection').length; cntr++) {
						var control = new Models.Field(model
								.get('controlCollection')[cntr]);
						this.populateModelWithControls(control);
						this.populateTreeWithControlNodes(control
								.get('controlName'));
					}
				},

				populateModelWithControls : function(control) {
					var updatedControl = Utility.addFieldHandlerMap[control
							.get('type')](control, false, 'controlContainer');
					Main.formView.getFormModel().get('controlObjectCollection')[control
							.get('controlName')] = updatedControl;
				},

				populateTreeWithControlNodes : function(controlName) {
					Main.treeView.getTree().insertNewChild(1, Main.nodeCounter,
							controlName, 0, 0, 0, 0,
							"SELECT,CALL,TOP,CHILD,CHECKED");
					Main.nodeCounter++;
				}
			}),

	initializeRouters : function() {
		this.controlEventsRouterPointer = new this.ControlRouter;
		this.formEventsRouterPointer = new this.FormRouter;
		Backbone.history.start();
	}
}