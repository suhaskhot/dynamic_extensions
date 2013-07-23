/**
 * @author SANJAY
 */

var Main = {

	treeView : null,
	formView : null,
	mainTabBarView : null,
	currentFieldView : null,
	designModeViewPointer : null,
	carousel: null,
	advancedControlsView : null,

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
		this.carousel = $('#slider1');
		this.carousel.tinycarousel();
		this.advancedControlsView = new Views.AdvancedPropertiesTabView({
			el : $('#advancedControlProperties'),
			model : null
		});

		

	}
}

Main.renderUI();