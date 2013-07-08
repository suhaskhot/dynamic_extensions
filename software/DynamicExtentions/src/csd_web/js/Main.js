/**
 * @author SANJAY
 */

var Main = {

	nodeCounter : 1,
	sequenceNumCntr : 0,
	treeView : null,
	formView : null,
	mainTabBarView : null,
	currentFieldView : null,

	renderUI : function() {
		Templates.loadTemplateList();
		Utility.initializeFieldHandlerMap();
		Views.showBody();
		Routers.initializeRouters();
		var form = new Models.Form();
		this.formView = Views.showForm('formTab', form);
		this.treeView = new Views.TreeView({
			el : $('#leftTab'),
			model : null
		});

		this.treeView.getTree().attachEvent(
				"onDblClick",
				function(id) {
					if (Main.currentFieldView != null) {
						Main.currentFieldView.destroy();
					}
					var fieldModel = Main.formView.getFormModel().get(
							'controlObjectCollection')[Main.treeView.getTree()
							.getItemText(id)];
					Main.currentFieldView = Views.showControl(
							'controlContainer', fieldModel);

					return true;
				});

		this.mainTabBarView = new Views.TabBarView({
			el : $('#rightTab'),
			model : null
		});
		Views.showControlTab('control');
	}
}

Main.renderUI();