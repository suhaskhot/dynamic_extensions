/*
 * Tabbar
 */

var K_Tabbar = function(configData, type) {
	// tabbar impl object
	this._tabbarImplObject = null;

	// initialize tabbar based on type and config data
	this.initialize = function(configData, type) {
		if (type == "dhtmlx") {
			this._tabbarImplObject = new dhtmlx_tabbar_impl(configData);
		}
	}

	// call initialize method
	this.initialize(configData, type);

	// add tab
	this.addTab = function(tabConfigData) {
		this._tabbarImplObject.addTab(tabConfigData);
	}

	// select tab
	this.selectTab = function(tabId) {
		this._tabbarImplObject.selectTab(tabId);
	}

	// add tab
	this.addTab = function(tabConfigData) {
		this._tabbarImplObject.addTab(tabConfigData);
	}

	// remove tab
	this.removeTab = function(tabId) {
		this._tabbarImplObject.removeTab(tabId);
	}

	// attach event
	this.attachEvent = function(eventConfigData) {
		this._tabbarImplObject.attachEvent(eventConfigData);
	}

}