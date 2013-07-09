var K_AutoComplete = function(configData) {

	this._inputElement = null;

	this._initialize = function(configData) {

		this._inputElement = configData.inputElementId;
		this._data = configData.data;
		var _this = this;

		$("#" + this._inputElement)
		// don't navigate away from the field on tab when selecting an item
		.bind(
				"keydown",
				function(event) {
					if (event.keyCode === $.ui.keyCode.TAB
							&& $(this).data("ui-autocomplete").menu.active) {
						event.preventDefault();
					}
				}).autocomplete(
				{
					minLength : 0,
					source : function(request, response) {
						// delegate back to autocomplete, but extract the last
						// term
						response($.ui.autocomplete.filter(_this._data, Utility
								.extractLast(request.term)));
					},
					focus : function() {
						// prevent value inserted on focus
						return false;
					},
					select : function(event, ui) {
						var terms = Utility.split(this.value);
						// remove the current input
						terms.pop();
						// add the selected item
						terms.push(ui.item.value);
						// add placeholder to get the double space at the end
						terms.push("");
						this.value = terms.join(" ");
						return false;
					}
				});
	}

	this._initialize(configData);

	this.getValue = function() {
		return $("#" + this._inputElement).val();
	}

}