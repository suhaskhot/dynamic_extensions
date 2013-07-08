var dhtmlx_tabbar_impl = function(configData) {

	// dhtmlx tabbar object
	this._tabbarObject = null;

	// method to add tab dynamically
	this.addTab = function(tabConfigData) {
		this._tabbarObject.addTab(tabConfigData.id, tabConfigData.label,
				tabConfigData.labelWidth);
		this._tabbarObject.setContent(tabConfigData.id, tabConfigData.data);
	}

	// select a tab
	this.selectTab = function(tabId) {
		this._tabbarObject.setTabActive(tabId);
	}

	// "initialize" method to initialize the tabbar with config data.
	this._initialize = function(configData) {
		// instantiate dhtmlx tabbar object
		this._tabbarObject = new dhtmlXTabBar(configData.container,
				configData.allignment);
		// set skin
		this._tabbarObject.setSkin(configData.skin);
		// set images folder path
		this._tabbarObject.setImagePath(configData.imagesPath);
		// add tabs if specified
		if (configData.tabsConfiguration != undefined
				&& configData.tabsConfiguration != null) {
			for ( var key in configData.tabsConfiguration) {
				this.addTab(configData.tabsConfiguration[key]);
			}
			// set active tab if specified
			if (configData.activeTabId != undefined) {
				this.selectTab(configData.activeTabId);
			}
		}
	}

	// remove a tab
	this.removeTab = function(tabId) {
		this._tabbarObject.removeTab(tabId, true);
	}

	// attach an event
	this.attachEvent = function(eventConfigData) {
		this._tabbarObject.attachEvent(eventConfigData.event,
				eventConfigData.handler);
	}
	
	// "initialize" method call
	this._initialize(configData);

}
