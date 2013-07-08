var Models = {

	Form : Backbone.Model.extend({
		controlCollection : null,
		controlObjectCollection : null,
		defaults : {
			caption : "New Form ",
			formName : "newForm",
			status : "new",
			id : null
		},
		url : function() {
			return "csdApi/form";
		},
		initialize : function() {
			console.log("Form Model created");
			this.controlCollection = new Array();
			this.controlObjectCollection = new Array();
		}
	}),
	// Field
	Field : Backbone.Model.extend({
		defaults : {
			caption : "New Field",
			controlName : "newField",
			conceptDefinitionSource : "BJC-MED",
			status : "new",
			xPos : 0,
			sequenceNumber : 0,
			id : null,
			width : 15
		},
		url : function() {
			return "csdApi/form/control";
		},
		initialize : function() {
			console.log("FieldControl Model created");
		}
	})

}