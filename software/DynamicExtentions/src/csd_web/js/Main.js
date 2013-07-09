/**
 * @author SANJAY
 */

var Main = {

	nodeCounter : 2,
	sequenceNumCntr : 0,
	treeView : null,
	formView : null,
	mainTabBarView : null,
	currentFieldView : null,
	designModeViewPointer : null,
	pvCounter : 0,

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

		this.treeView.getTree().attachEvent("onDblClick",
				Routers.formTreeNodeClickHandler);

		this.mainTabBarView = new Views.TabBarView({
			el : $('#rightTab'),
			model : null
		});
		Views.showControlTab('control');
		$('#slider1').tinycarousel();

		

	}
}

Main.renderUI();