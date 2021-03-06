var edu = edu || {};
edu.common = edu.common || {};
edu.common.de = edu.common.de || {};

edu.common.de.RequiredValidator = function(field, dataEl) {
  this.validate = function() {
    var valid = false;
    var el;

    if (dataEl instanceof Array) { // GroupField
      for (var i = 0; i < dataEl.length; ++i) {
        if (dataEl[i].prop('checked')) {
          valid = true;
          break;
        }
      }
      el = field.clusterEl;
    } else {
      if (dataEl.prop('type') == 'checkbox') {
        valid = dataEl.prop('checked');
      } else {
        valid = dataEl.val() && dataEl.val().length != 0;
      }
      el = field.inputEl;
    }

    if (!valid) {
      edu.common.de.Utility.highlightError(el, field.getCaption() + ' is required field');
    } else {
      edu.common.de.Utility.unHighlightError(el, field.getTooltip());
    }

    return valid;
  };
};

edu.common.de.RangeValidator = function(field, dataEl, params) {
  this.validate = function() {
    var val = dataEl.val();
    if (!$.isNumeric(val)) {
      return true; // range validator not applicable for non-numeric fields
    }

    var number = Number(val);
    if ($.isNumeric(params.min) && number < Number(params.min)) {
      edu.common.de.Utility.highlightError(field.inputEl, field.getCaption() + " cannot be less than " + params.min);
      return false;
    } else if ($.isNumeric(params.max) && number > Number(params.max)) {
      edu.common.de.Utility.highlightError(field.inputEl, field.getCaption() + " cannot be more than " + params.max);
      return false;
    } else {
      edu.common.de.Utility.unHighlightError(field.inputEl, field.getTooltip());
      return true;
    }
  };
};

edu.common.de.NumericValidator = function(field, dataEl, params) {
  this.validate = function() {
    var val = dataEl.val();
    if (!val || val.trim().length == 0) { // empty field
      return true; 
    }

    if (!$.isNumeric(val)) {
      edu.common.de.Utility.highlightError(field.inputEl, field.getCaption() + " must be a numeric");
      return false;
    }

    var numberParts = val.split(".");
    var noOfDigits = params.noOfDigitsAfterDecimal;
    if (numberParts.length > 1 && noOfDigits != null && noOfDigits != undefined) {
      var realPart = numberParts[1].trim();
      if (realPart.length > noOfDigits) {
        edu.common.de.Utility.highlightError(
          field.inputEl, 
          field.getCaption() + " cannot have more than " + noOfDigits + " digits after decimal point");
        return false;
      }
    } 

    edu.common.de.Utility.unHighlightError(field.inputEl, field.getTooltip());
    return true;
  };
};

edu.common.de.FieldValidator = function(rules, field, dataEl) {
  if (!dataEl) {
    dataEl = field.inputEl;
  }

  var validators = [];
  if (!rules) {
    rules = [];
  }

  for (var i = 0; i < rules.length; ++i) {
    var validator;
    if (rules[i].name == 'required') {
      validator = new edu.common.de.RequiredValidator(field, dataEl, rules[i].params);
    } else if (rules[i].name == 'range') {
      validator = new edu.common.de.RangeValidator(field, dataEl, rules[i].params);
    } else if (rules[i].name == 'numeric') {
      validator = new edu.common.de.NumericValidator(field, dataEl, rules[i].params);
    }

    if (validator) {
      validators.push(validator);
    }
  }

  this.validate = function() {
    var valid = true;
    for (var i = 0; i < validators.length; ++i) {
      if (!validators[i].validate()) {
        valid = false;
        break;
      }
    }

    return valid;
  } 

  if (dataEl instanceof Array) {
    for (var i = 0; i < dataEl.length; ++i) {
      dataEl[i].change(this.validate);
    }
    field.clusterEl.focusout(this.validate);
  } else {
    dataEl.change(this.validate);
    dataEl.focusout(this.validate);
  }
};

edu.common.de.Form = function(args) {
  if (typeof args.formDiv == "string") {
    this.formDiv = $("#" + args.formDiv);
  } else {
    this.formDiv = args.formDiv;
  }

  this.fieldObjs = [];
 
  this.formId = args.id;
  this.formDef = args.formDef;
  this.formDefUrl = args.formDefUrl;
  this.formDefXhr = null;

  this.recordId = args.recordId;
  this.formData = args.formData;
  this.formDataUrl = args.formDataUrl;
  this.formSaveUrl = args.formSaveUrl;
  this.formDataXhr = null;

  this.fileUploadUrl = args.fileUploadUrl;
  this.fileDownloadUrl = args.fileDownloadUrl;
  this.appData = args.appData;
  this.dateFormat = args.dateFormat;
  
  if (!this.formDef && this.formDefUrl) {
    var url = this.formDefUrl.replace(":formId", this.formId);
    this.formDefXhr = $.ajax({type: 'GET', url: url});
  }

  if (this.recordId && !this.formData && this.formDataUrl) {
    var url = this.formDataUrl.replace(":formId", args.id)
                              .replace(":recordId", args.recordId);
    this.formDataXhr = $.ajax({type: 'GET', url: url});
  }

  this.render = function() {
    var that = this;
    if (this.formDefXhr && this.formDataXhr) {
      $.when(this.formDefXhr, this.formDataXhr).then(function(formDef, formData) {
        that.formDef = formDef[0];
        that.formData = formData[0];
        that.render0();
      });
    } else if (this.formDefXhr) {
      this.formDefXhr.then(function(formDef) {
        that.formDef = formDef;
        that.render0();
      });
    } else if (this.formDataXhr) {
      this.formDataXhr.then(function(formData) {
        that.formData = formData;
        that.render0();
      });
    } else {
      this.render0();
    }
  };

  this.processFormDef = function(prefix, rows) {
    for (var i = 0; i < rows.length; ++i) {
      var row = rows[i];
      for (var j = 0; j < row.length; ++j) {
        if (row[j].type == 'subForm') {
          this.processFormDef(prefix + row[j].name + ".", row[j].rows);
        } else {
          row[j].fqn = prefix + row[j].name;
        }
      }
    }
  };
        
  this.destroy = function() {
    this.formDiv.children().remove();
  };

  this.render0 = function() {
    if (this.formDef.rows == undefined) {
      this.formDef = JSON.parse(this.formDef);
    }

    var rows = this.formDef.rows;
    this.processFormDef("", rows);

    var formCtrls = $("<div/>");
    for (var i = 0; i < rows.length; ++i) {
      formCtrls.append(this.rowCtrls(rows[i]));
    }

    if (args.showActionBtns === undefined || 
        args.showActionBtns === null || 
        args.showActionBtns === true) {
      formCtrls.append(this.getActionButtons());
    }

    var caption = args.showTitle == false ? undefined : this.formDef.caption;
    var panel = edu.common.de.Utility.panel(caption, formCtrls, 'default');
    this.formDiv.append(panel);

    if (this.formData != undefined) {
      this.formData = JSON.parse(this.formData);
    }

    this.setValue(this.formData);
  };

  this.setValue = function(formData) {
    var recId = undefined;
    if (formData) {
      recId = formData.id;
    }

    for (var i = 0; i < this.fieldObjs.length; ++i) {
      var fieldObj = this.fieldObjs[i];
      if (recId) {
        fieldObj.setValue(recId, formData[fieldObj.getName()]);
      }
      fieldObj.postRender();
    }
  };

  this.getValue = function() {
    var formData = {};

    for (var i = 0; i < this.fieldObjs.length; ++i) {
      var field = this.fieldObjs[i];
      var value = field.getValue();

      if (!value) { // note doesn't have value;
        continue;
      }

      formData[value.name] = value.value;
    }

    return formData;
  };

  this.rowCtrls = function(row) {
    var fields = row;
    var noFields = fields.length;
    var width = noFields == 1 ? 8 : Math.floor(12 / noFields);
    var colWidthCls = "col-xs-" + width;

    var rowDiv = edu.common.de.Utility.row();
    for (var i = 0; i < fields.length; ++i) {
      var field = fields[i];
      var widthCls = (field.type == 'subForm') ? "col-xs-12" : colWidthCls;
      rowDiv.append($("<div/>").addClass(widthCls).append(this.getFieldEl(field)));
    }

    return rowDiv;
  };

  this.getFieldEl = function(field) {
    var id      = 'de-' + field.name
    var labelEl = field.type != 'label' ? this.fieldLabel(id, field.caption) : undefined;

    var fieldObj = edu.common.de.FieldFactory.getField(field, undefined, args);
    var inputEl = fieldObj.render();
    this.fieldObjs.push(fieldObj);

    var fieldEl = $("<div/>").addClass("form-group");
    if (labelEl) {
      fieldEl.append(labelEl);
    }
  
    return fieldEl.append(inputEl);
  };

  this.fieldLabel = function(name, label) {
    return $("<label/>").addClass("control-label").prop('for', name).append(label);
  };

  this.getActionButtons = function() {
    var save       = $("<button/>").attr({"type": "button", "id": "saveForm"}).addClass("btn btn-primary").append("Save");
    var cancel     = $("<button/>").attr({"type": "button", "id": "cancelForm"}).addClass("btn btn-default").append("Cancel");
    var deleteForm = $("<button/>").attr({"type": "button", "id": "deleteForm"}).addClass("btn btn-warning").append("Delete");
    var print      = $("<button/>").attr({"type": "button", "id": "print"}).addClass("btn btn-info").append("Print");

    if (!this.formData) {
      deleteForm.attr('disabled', true);
      print.attr('disabled', true);
    }

    var that = this;
    save.on("click", function() { that.save(); });
    cancel.on("click", function() { that.cancel(); });
    deleteForm.on("click", function() { that.deleteForm(); });
    print.on("click", function() { that.print(); });
    
    var btns =   $("<div/>").addClass("modal-footer")
      .append(cancel).append(deleteForm)
      .append(print).append(save);
    return edu.common.de.Utility.row().append(btns);
  };

  this.save = function() {
    if (!this.validate()) {
      if (args.onValidationError) {
        args.onValidationError();
      }
      return;
    }

    var formData = {appData: this.appData};
    var url = this.formSaveUrl.replace(":formId", this.formId);
    var method;
    if (this.recordId) {
      url = url.replace(":recordId", this.recordId);
      formData.recordId = this.recordId;
      method = 'PUT';
    } else {
      url = url.replace(":recordId", "");
      method = 'POST';
    }

    $.extend(formData, this.getValue());
    var that = this;
    $.ajax({
      type: method,
      url: url,
      data: JSON.stringify(formData)
    }).done(function(data) { 
      data = JSON.parse(data);
      that.recordId = data.id;
      if (args.onSaveSuccess) {
        args.onSaveSuccess(data);     
      }
    }).fail(function(data) { 
      if (args.onSaveError) {
        args.onSaveError(data);
      }
    });
  };

  this.cancel = function() {
    if (args.onCancel) {
      args.onCancel();
    }
  };

  this.print = function() {
    if (args.onPrint) {
      args.onPrint(this.getFormPrintHtml());
    }
  };

  this.getFormPrintHtml = function() {
    return $("<div/>").append(
      $("<table/>")
        .addClass("table table-condensed table-bordered")
        .css("width", "100%")
        .append(this.getPrintEl()));
  };

  this.deleteForm = function() {
    if (args.onDelete) {
      args.onDelete();
    }
  };
  
  this.validate = function() {
    var valid = true;
    for (var i = 0; i < this.fieldObjs.length; ++i) {
      if (!this.fieldObjs[i].validate()) { // validate all fields
        valid = false;
      }
    }

    return valid;
  };

  this.getPrintEl = function() {
    var els = [];
    for (var i = 0; i < this.fieldObjs.length; ++i) {
      if (this.fieldObjs[i] instanceof edu.common.de.Note) {
        continue;
      }

      var tr = $("<tr/>");

      var captionTd = $("<td/>")
        .addClass("de-print-label")
        .append($("<label/>").append(this.fieldObjs[i].getCaption()));   
      tr.append(captionTd);

      var valueTd = $("<td/>")
        .addClass("de-print-value")
        .append(this.fieldObjs[i].getPrintEl ? this.fieldObjs[i].getPrintEl() : "Undefined Print Fn");
      tr.append(valueTd);

      els.push(tr);
    }

    return $("<tbody/>").append(els);
   };
};

edu.common.de.FieldFactory = {

  getField: function(field, idx, args) {
    var fieldObj;
    idx = idx || "";

    var id = 'de-' + field.name + "-" + idx;

    if (field.type == 'stringTextField') {
      fieldObj = new edu.common.de.TextField(id, field, args);
    } else if (field.type == 'numberField') {
      fieldObj = new edu.common.de.NumberField(id, field, args);
    } else if (field.type == 'textArea') {
      fieldObj = new edu.common.de.TextArea(id, field, args);
    } else if (field.type == 'datePicker') {
      fieldObj = new edu.common.de.DatePicker(id, field, args);
    } else if (field.type == 'booleanCheckbox') {
      fieldObj = new edu.common.de.BooleanCheckbox(id, field, args);
    } else if (field.type == 'combobox' || field.type == 'listbox' || field.type == 'multiSelectListbox') {
      fieldObj = new edu.common.de.SelectField(id, field, args);
    } else if (field.type == 'radiobutton' || field.type == 'checkbox') {
      fieldObj = new edu.common.de.GroupField(id, field, args);
    } else if (field.type == 'subForm' && !field.singleEntry) {
      fieldObj = new edu.common.de.SubFormField(id, field, args);
    } else if (field.type == 'subForm' && field.singleEntry) {
      fieldObj = new edu.common.de.ComponentForm(id, field, args);
    } else if (field.type == 'fileUpload') {
      fieldObj = new edu.common.de.FileUploadField(id, field, args);
    } else if (field.type == 'label') {
      fieldObj = new edu.common.de.Note(id, field, args);
    } else {
      var params = {id: id, field: field, args: args};
      fieldObj = edu.common.de.FieldManager.getInstance().getField(field.type, params);
      if (!fieldObj) {
        fieldObj = new edu.common.de.UnknownField(id, field, args);
      }
    }

    return fieldObj;
  }
};

edu.common.de.TextField = function(id, field) {
  this.inputEl = null;
  this.validator;

  this.render = function() {
    this.inputEl = $("<input/>")
      .prop({id: id, type: 'text', title: field.toolTip, value: field.defaultValue})
      .addClass("form-control");

    this.validator = new edu.common.de.FieldValidator(field.validationRules, this);
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(recId, value) {
    this.recId = recId;
    this.inputEl.val(value);
  };

  this.getDisplayValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.NumberField = function(id, field) {
  this.inputEl = null;
  this.validator;

  this.render = function() {
    this.inputEl = $("<input/>") 
      .prop({id: id, type: 'text', title: field.toolTip, value: field.defaultValue})
      .addClass("form-control");

    var rules = field.validationRules.concat({name: 'numeric', params: {noOfDigitsAfterDecimal: field.noOfDigitsAfterDecimal}});
    this.validator = new edu.common.de.FieldValidator(rules, this);
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(recId, value) {
    this.recId = recId;
    this.inputEl.val(value);
  };

  this.getDisplayValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.TextArea = function(id, field) {
  this.inputEl = null;
  this.validator;

  this.render = function() {
    var noOfRows = 2;
    if (field.noOfRows && field.noOfRows > 0) {
      noOfRows = field.noOfRows;
    }

    this.inputEl = $("<textarea/>")
      .prop({id: id, rows: noOfRows, title: field.toolTip, value: field.defaultValue})
      .addClass("form-control");
    this.validator = new edu.common.de.FieldValidator(field.validationRules, this);
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };
  
  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.getDisplayValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  }

  this.setValue = function(recId, value) {
    this.recId = recId;
    this.inputEl.val(value);
  };
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.DatePicker = function(id, field) {
  this.inputEl = null;
  this.dateEl = null;
  this.timeEl = null;
  this.validator;

  this.render = function() {
    this.dateEl = $("<input/>")
      .prop({id: id, type: 'text'})
      .addClass("form-control");

    var dateField = $("<div/>").addClass("plus-addon plus-addon-input-right de-date-picker")
      .append(this.dateEl)
      .append($("<span/>").addClass("glyphicon glyphicon-calendar"));

    this.inputEl = $("<div/>"); 
    this.inputEl.append(dateField);

    var format = field.format;
    if (format && format.indexOf('HH:mm') != -1) {
      dateFmt = dateFormat.concat(" HH:mm");
      this.timeEl = $("<input/>")
        .prop({id: 'time', type: 'text'})
        .addClass("form-control");

      dateField.css("width","59%");

      var timeField = $("<div/>").addClass("plus-addon plus-addon-input-right de-time-picker")
        .append(this.timeEl)
        .append($("<span/>").addClass("glyphicon glyphicon-time"));

      this.inputEl.append(timeField);
    }

    this.inputEl.prop({title: field.toolTip});
    this.validator = new edu.common.de.FieldValidator(field.validationRules, this, this.dateEl);

    if (format && format.length != 0) {
      format = format.toUpperCase();
      if (format.indexOf("D") != -1 && format.indexOf("M") != -1 && format.indexOf("Y") != -1) {
        format = 0;
      } else if (format.indexOf("M") != -1 && format.indexOf("Y") != -1) {
        format = 1;
      } else if (format.indexOf("Y") != -1) {
        format = 2;
      } else {
        format = 0;
      }
    } else {
      format = 0; // minViewMode = 0 days
    }

    this.dateEl.datepicker({
      format: typeof dateFormat == "undefined" ? format : dateFormat,
      autoclose: true,
      minViewMode: format});

    if (this.timeEl) {
      this.timeEl.timepicker({
        defaultTime: false,
        showMeridian: false
      });
    }

    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };
	  
  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    var val = this.dateEl.val();
    if (this.timeEl) {
      val = val + ' ' + this.timeEl.val();
    }
    return {name: field.name, value: val};
  };

  this.setValue = function(recId, value) {
    this.recId = recId;
    if (!value || value.trim().length == 0) {
      return;
    }

    var format = field.format;
    if (format.indexOf('HH:mm') != -1) { // TODO: Fix this
      var dateTime = value.split(" ");
      this.dateEl.val(dateTime[0]);
      this.timeEl.val(dateTime[1]);
    } else {
      this.dateEl.val(value);
    }

    this.dateEl.datepicker('update');
  };

  this.getDisplayValue = function() {
    return this.getValue();
  }
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.BooleanCheckbox = function(id, field) {
  this.inputEl = null;
  this.dataEl = null;
  this.validator;

  this.render = function() {
    this.dataEl = $("<input/>")
      .prop({type: 'checkbox', id: id, value: 'true', title: field.toolTip});
    this.inputEl = $("<div/>")
      .append(this.dataEl).css("padding", "6px 0px");

    this.validator = new edu.common.de.FieldValidator(field.validationRules, this, this.dataEl);
    this.setValue(undefined, field.defaultChecked)
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };
	  
  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    var value = this.dataEl.prop('checked') ? "1" : "0";
    return {name: field.name, value: value};
  };

  this.setValue = function(recId, value) {
    this.recId = recId; 
    if (value == "1" || value == true) {
      this.dataEl.val("true");
      this.dataEl.prop('checked', true);
    } else {
      this.dataEl.val("false");
      this.dataEl.prop('checked', false);
    }
  };

  this.getDisplayValue = function() {
    var value = this.dataEl.prop('checked') ? "true" : "false";
    return {name: field.name, value: value};
  };
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.SelectField = function(id, field) {
  this.inputEl = null;
  this.validator;

  this.render = function() {
    var isMultiSelect = (field.type == 'listbox' || field.type == 'multiSelectListbox');
    this.inputEl = $("<select/>")
      .prop({id: id, multiple: isMultiSelect, title: field.toolTip})
      .addClass("de-select")
      .append($("<option/>"));

    this.validator = new edu.common.de.FieldValidator(field.validationRules, this);
    for (var i = 0; i < field.pvs.length; ++i) {
      var pv = field.pvs[i];
      this.inputEl.append($("<option/>").prop("value", pv.value).append(pv.value));
    }

    return this.inputEl;
  };

  this.postRender = function() {
    this.inputEl.select2({allowClear: true});
    this.inputEl.siblings('div.select2-container').prop({title: field.toolTip});
  };

  this.getName = function() {
    return field.name;
  };
  
  this.getCaption = function() {
    return field.caption;
  };
  
  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(recId, value) {
    value = edu.common.de.Utility.getValueByDataType(field, value);
    this.recId = recId;
    this.inputEl.val(value);
    this.inputEl.trigger('change');
  };

  this.getDisplayValue = function() {
    var value = this.inputEl.val();
    value = (value instanceof Array) ? value.join() : value;
    return {name: field.name, value: value};
  };
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.GroupField = function(id, field) {
  this.clusterEl = [];
  this.inputEls = [];

  this.render = function() {
    var currentDiv;
    var count = 0;
    var optionsPerRow = field.optionsPerRow || 3;
    var width = "" + Math.floor(95 / optionsPerRow) - 1 + "%";
    var type = field.type == 'radiobutton' ? 'radio' : 'checkbox';
    var typeclass = field.type == 'radiobutton' ? 'radio-inline' : 'checkbox-inline';

    for (var i = 0; i < field.pvs.length; ++i) {
      var pv = field.pvs[i];
      var defaultVal = false;
      if (count % optionsPerRow == 0) {
        if (currentDiv) {
          this.clusterEl.push(currentDiv);
        }

        currentDiv = $("<div/>");
      }
      
      if (field.defaultValue != undefined &&  field.defaultValue.value == pv.value) {
        defaultVal = true;
      }

      var btn = $("<input/>").prop({type: type, name: field.name + id, value: pv.value, title: field.toolTip, checked: defaultVal});
      currentDiv.append($("<label/>").addClass(typeclass).append(btn).append(pv.value).css("width", width));
      this.inputEls.push(btn);
      ++count;
    }

    this.clusterEl.push(currentDiv);
    this.clusterEl = $("<div/>").addClass("form-inline").append(this.clusterEl);
    this.validator = new edu.common.de.FieldValidator(field.validationRules, this, this.inputEls);
    return this.clusterEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };
	  
  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    var checked = [];
    for (var i = 0; i < this.inputEls.length; ++i) {
      if (this.inputEls[i].prop('checked')) {
        checked.push(this.inputEls[i].val());
      }
    }

    var value = checked;
    if (field.type == 'radiobutton') {
      value = checked.length > 0 ? checked[0] : null;
    }

    return {name: field.name, value: value};
  };

  this.setValue = function(recId, value) {
    this.recId = recId;
    value = edu.common.de.Utility.getValueByDataType(field, value);
    var checked;
    if (field.type == 'radiobutton') {
      checked = [value];
    } else {
      checked = value;
    }

    for (var i = 0; i < this.inputEls.length; ++i) {
      if ($.inArray(this.inputEls[i].val(), checked) != -1) {
        this.inputEls[i].prop('checked', true);
      } else {
        this.inputEls[i].prop('checked', false);
      }
    }
  };

  this.getDisplayValue = function() {
    var value = this.getValue().value;
    value = (value instanceof Array) ? value.join().substring(1) : value;
    return {name: field.name, value: value};
  };
  
  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.ComponentForm = function(id, field, args) {
  this.render = function() {
    this.el = $("<div/>");
    this.form = new edu.common.de.Form({
      formDef: field, 
      formDiv: this.el, 
      showActionBtns: false,
      showTitle: false
    });
    this.form.render();
    return this.el;
  }

  this.postRender = function() {
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: [this.form.getValue()]};
  };

  this.setValue = function(recId, value) {
    var formData = {}
    if (value && value instanceof Array) {
      formData = value[0];
    }
    this.form.setValue(formData);
  };

  this.validate = function() {
    return this.form.validate();
  };

  this.getPrintEl = function() {
    return $("<table/>").addClass("table table-condensed").append(this.form.getPrintEl());
  };
};

edu.common.de.SubFormField = function(id, sfField, args) {
  this.subFormEl = null;
  this.sfFieldsEl = null;
  this.rowIdx = 0;
  this.fieldObjsRows = [];
  this.recIds = [];
  this.getFields = function() {
    var fields = [];
    for (var i = 0; i < sfField.rows.length; ++i) {
      for (var j = 0; j < sfField.rows[i].length; ++j) {
        fields.push(sfField.rows[i][j]);
      }
    }

    return fields;
  };

  this.getWidthClass = function(numFields) {
    var width = Math.floor(95 / numFields);
    if (width < 20) {
      width = 20;
    }

    return width + "%";
  };

  this.fields = this.getFields();

  this.widthCls = this.getWidthClass(this.getFields().length);

  this.render = function() {
    this.sfFieldsEl = $("<div/>");
  
    var subFormContent = $("<div/>").addClass("de-sf-content");
    subFormContent
      .append(this.getHeading())
      .append(this.sfFieldsEl);


    this.subFormEl = $("<div/>").prop({id: id, title: sfField.toolTip})
      .append(subFormContent)
      .append(this.getAddButton());

    return this.subFormEl;
  };

  this.postRender = function() {
  };  

  this.getName = function() {
    return sfField.name;
  };

  this.getCaption = function() {
    return sfField.caption;
  };

  this.getValue = function() {
    var values = [];
    for (var i in this.fieldObjsRows) {
      var fieldObjs = this.fieldObjsRows[i];
      var sfInstance = {id: this.recIds[i]};
      for (var j = 0; j < fieldObjs.length; ++j) {
        var value = fieldObjs[j].getValue();
        if (!value) { // note doesn't have value
          continue;
        }
        sfInstance[value.name] = value.value;
      }

      values.push(sfInstance);
    }
    return {name: sfField.name, value: values};
  };

  this.setValue = function(recId, value) {
    this.recId = recId;
    this.sfFieldsEl.children().remove();
    this.fieldObjsRows = [];

    for (var i = 0; i < value.length; ++i) {
      var sfInst = value[i];
      this.addSubFormFieldsRow(false, sfInst.id);
      
      var fieldObjs = this.fieldObjsRows[i];
      for (var j = 0; j < fieldObjs.length; ++j) {
        var fieldValue = sfInst[fieldObjs[j].getName()];
        fieldObjs[j].setValue(sfInst.id, fieldValue);
        fieldObjs[j].postRender();
      }
    }
  };

  this.getDisplayValue = function() {
    return sfField.name;
  };

  var getSfFieldWidth = function(field) { 
    if (field.type != 'datePicker') {
      return 20 + "%";
    }

    if (field.format && field.format.indexOf("HH:mm") != -1) {
      return 25 + "%";
    }

    return 20 + "%";
  };
    
  this.getHeading = function() {
    var heading = $("<div/>").addClass("form-group clearfix").css("white-space", "nowrap");
    for (var i = 0; i < this.fields.length; ++i) {
      var field = this.fields[i];
      var column = $("<div/>").css("width", getSfFieldWidth(field))
        .addClass("de-sf-cell")
        .append(field.caption);
      heading.append(column);
    }

    return heading;
  };

  this.getAddButton = function() {
    var addBtn = edu.common.de.Utility.iconButton({btnClass: 'btn btn-default btn-xs form-inline', icon: 'plus'});
    var that = this;
    addBtn.on("click", function() { that.addSubFormFieldsRow(); });
    return $("<div/>").addClass("form-group clearfix").append(addBtn);
  };

  this.getRemoveButton = function(rowDiv, rowIdx) {
    var removeBtn = edu.common.de.Utility.iconButton({btnClass: 'btn btn-default', icon: 'trash'});
    var that = this;
    removeBtn.on("click", function() {
      rowDiv.remove();
      that.fieldObjsRows.splice(rowIdx, 1);
      that.recIds.splice(rowIdx, 1);
      that.rowIdx--;
    });

    return removeBtn;
  };

  this.addSubFormFieldsRow = function(postRender, recId) {
    var fieldObjs = [];

    var rowDiv = $("<div/>").addClass("form-group clearfix").css("white-space", "nowrap");
    for (var i = 0; i < this.fields.length; ++i) {
      var field = this.fields[i];
      if (field.type == 'subForm') {
        continue;
      }

      var fieldObj = edu.common.de.FieldFactory.getField(field, this.rowIdx, args);
      var fieldEl = fieldObj.render();
      rowDiv.append(this.cell(fieldEl, getSfFieldWidth(field)));
      fieldObjs.push(fieldObj);
    }

    var removeButton = this.getRemoveButton(rowDiv, this.rowIdx);
    rowDiv.append(this.cell(removeButton, "3%"));

    this.sfFieldsEl.append(rowDiv);

    postRender = typeof postRender == 'undefined' || postRender;
    if (postRender) {
      for (var i = 0; i < fieldObjs.length; ++i) {
        fieldObjs[i].postRender();
      }
    }

    this.fieldObjsRows[this.rowIdx] = fieldObjs;
    this.recIds[this.rowIdx] = recId;
    this.rowIdx++;
  };

  this.cell = function(el, width) {
    if (!width) {
      width = this.widthCls;
    }

    return $("<div/>").css("width", width).addClass("de-sf-cell").append(el);
  };
  
  this.validate = function() {
    var valid = true;
    for (var i = 0; i < this.fieldObjsRows.length; ++i) {
      var fieldObjs = this.fieldObjsRows[i];
      for (var j = 0; j < fieldObjs.length; ++j) {
        if (!fieldObjs[j].validate()) {
          valid = false;
        }
      }
    }

    return valid;
  };

  this.getPrintEl = function() {
    if (!this.fieldObjsRows || this.fieldObjsRows.length == 0) {
      return $("<span/>").append("N/A");
    }

    var theadRow = $("<tr/>");
    for (var i = 0; i < this.fieldObjsRows[0].length; ++i) {
      var field = this.fieldObjsRows[0][i];
      if (field instanceof edu.common.de.Note) {
        continue;
      }

      theadRow.append($("<th/>").append(field.getCaption()));
    }

    var thead = $("<thead/>").append(theadRow);

    var tbody = $("<tbody/>");
    for (var i = 0; i < this.fieldObjsRows.length; ++ i) {
      var tr = $("<tr/>");
      for (var j = 0; j < this.fieldObjsRows[i].length; ++j) {
        var field = this.fieldObjsRows[i][j];
        if (field instanceof edu.common.de.Note) {
          continue;
        }
        tr.append($("<td/>").append(edu.common.de.Utility.getPrintEl(field)));
      }
      tbody.append(tr);
    }

    return $("<table/>").addClass("table table-condensed").append(thead).append(tbody);
  };
};

edu.common.de.FileUploadField = function(id, field, args) {
  this.value = {filename: undefined, contentType: undefined, fileId: undefined};

  this.validator;

  this.inputEl = null;       /** The outer div of file input element **/

  this.uploadBtn = null;     /** The file upload button i.e. input[type=file] **/

  this.removeBtn = null;     /** File remove button **/

  this.fileNameSpan = null;  /** Span to contain name of uploaded file **/

  this.progressBar = null;   /** Progress bar div **/

  this.fileNameInput = null;

  this.dirty = false;

  this.render = function() {
    var that = this;
    this.inputEl  = $("<div/>") 
      .prop({title: field.toolTip})
      .addClass("de-fileupload form-control clearfix");
    this.fileNameSpan = $("<span/>");
    this.inputEl.append(this.fileNameSpan);

    this.fileNameInput = $("<input/>").prop({type: 'text'}).addClass("hidden");
    this.inputEl.append(this.fileNameInput);

    this.removeBtn = $("<span/>").addClass("glyphicon glyphicon-remove de-input-addon hidden");
    this.removeBtn.on("click", function() {
      that.setValue(this.recId, {filename: undefined, contentType: undefined, fileId: undefined});
    });

    var uploadIcon = $("<span/>").addClass("glyphicon glyphicon-paperclip de-input-addon");
    var uploadUrl
    if (typeof args.fileUploadUrl == "function") {
      uploadUrl = args.fileUploadUrl(id, field, args);
    } else if (typeof args.fileUploadUrl == "string") {
      uploadUrl = args.fileUploadUrl;
    }

    this.uploadBtn = $("<input/>").attr({name: "file", type: "file", 'data-url': uploadUrl});
    uploadIcon.append(this.uploadBtn);
    this.uploadBtn.fileupload({
      dataType: 'json',

      done: function(e, data) {
        var value = {
          filename: data.result.filename, 
          contentType: data.result.contentType,
          fileId: data.result.fileId
        };
        that.dirty = true;
        that.setValue(that.recId, value);
        that.progressBar.addClass("hidden");
        that.fileNameSpan.removeClass("hidden");
      },

      progress: function(e, data) {
        var pct = Math.floor(data.loaded / data.total * 100);
        if (pct != 100) {
          that.fileNameSpan.addClass("hidden");
          that.progressBar.removeClass("hidden");
          that.progressBar.children().css('width', pct + '%');
        }
      }
    });

    this.progressBar  = $("<div/>").addClass("progress pull-left hidden").css('width', '90%');
    this.progressBar.append($("<div/>").addClass("progress-bar progress-bar-default"));

    var btns = $("<div/>").addClass("pull-right").append(uploadIcon).append(this.removeBtn);
    this.validator = new edu.common.de.FieldValidator(field.validationRules, this, this.fileNameInput);
    return this.inputEl.append(this.progressBar).append(btns);
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name
  };

  this.getCaption = function() {
    return field.caption;
  };
	  
  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function() {
    return {name: field.name, value: this.value}; 
  };
  
  this.setValue = function(recId, value) {
    this.recId = recId;
    this.value = value;

    if (this.value && this.value.filename) {
      if (this.dirty) {
        this.fileNameSpan.text(this.value.filename);
      } else {
        var url = "#";
        if (typeof args.fileDownloadUrl == "function") {
          url = args.fileDownloadUrl(args.id, this.recId, field.fqn);
        } else {
          url = args.fileDownloadUrl;
        }
        var link = $("<a/>").attr("href", url).text(this.value.filename);
        this.fileNameSpan.append(link);
      }

      this.removeBtn.removeClass("hidden");
      this.fileNameInput.val(this.value.filename).change();
    } else {
      this.fileNameSpan.text("");
      this.fileNameSpan.children().remove();
      this.removeBtn.addClass("hidden");
      this.fileNameInput.val("").change();
    }

    return this;
  };

  this.getDisplayValue = function() {
    var filename = undefined;
    if (this.value) {
      filename = this.value.filename;
    }
    return {name: field.name, value: filename};
  };

  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.Note = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<div/>").html(field.caption);
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getTooltip = function() {
    return "";
  };
	  
  this.getValue = function() {
    return undefined;
  };

  this.setValue = function(value) {
  };
  
  this.validate = function() {
    return true;
  };

  this.getDisplayValue = function() {
    return {name: field.name, value: field.caption};
  }
};

edu.common.de.UnknownField = function(id, field) {
  this.inputEl = $("<div/>").append("Field type " + field.type + " unknown");

  this.render = function() {
    return this.inputEl;
  }

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function(value) {
    return {name: field.name, value: "Unknown"};
  };

  this.setValue = function(recId, value) {
  };
  
  this.validate = function() {
    return false;
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};


edu.common.de.Utility = {
  /**
   * Valid values for context are primary, success, info, warning, danger
   */
  panel: function(title, content, context) {
    if (!context) {
      context = "default";
    }

    var panelDiv = $("<div/>").addClass("panel").addClass("panel-" + context);
    if (title) {
      var panelHeading = $("<div/>").addClass("panel-heading");
      var titleDiv = $("<div/>").addClass("panel-title").append(title);
      panelHeading.append(titleDiv);
      panelDiv.append(panelHeading);
    }

    var panelBody = $("<div/>").addClass("panel-body").append(content);
    panelDiv.append(panelBody);

    return panelDiv;
  },

  row: function() {
    return $("<div/>").addClass("row");
  },

  label: function(caption, attrs) {
    return $("<label/>").append(caption).prop(attrs);
  },

  iconButton: function(options) {
    return $("<button/>").addClass(options.btnClass)
             .append($("<span/>").addClass("glyphicon glyphicon-" + options.icon));
  },

  highlightError: function(el, tooltip) {
    if (el.is('select')) {
      el.siblings('div.select2-container').attr('title', tooltip);
    }
    el.addClass('de-input-error').attr('title', tooltip);
  },

  unHighlightError: function(el, tooltip) {
    if (el.is('select')) {
      el.siblings('div.select2-container').attr('title', tooltip);
    }
    el.removeClass('de-input-error').attr('title', tooltip);
  },
  
  getValueByDataType: function(field, value) {
    if (value == undefined || field.dataType == "STRING" || field.dataType == "BOOLEAN") {
      return value;
    }
	
    var parsedVal;
    if (value instanceof Array) {
      parsedVal = [];
      for (var i = 0 ; i < value.length ; i++) {
        if (field.dataType == "INTEGER") {
          parsedVal[i] = parseInt(value[i]).toString();
        } else if (field.dataType == "FLOAT") {
          parsedVal[i] = parseFloat(value[i]).toString();
        }
      }
    } else {
      if (field.dataType == "INTEGER") {
        parsedVal = parseInt(value);
      } else if (field.dataType == "FLOAT") {
        parsedVal = parseFloat(value);
      }
      parsedVal = parsedVal.toString();
    }
    return parsedVal;
  },

  getPrintEl: function(fieldObj) {
    var val = fieldObj.getDisplayValue().value;
    if (val == undefined || val == null) {
      val = "N/A";
    }

    if (val instanceof Array) {
      val = val.join(", ");
    }

    return $("<span/>").append(val);
  }
};

edu.common.de.Extend = function(props) {
  var parent = this;
  var child = function() { return parent.apply(this, arguments); };

  $.extend(child, parent);
  if (props) {
    $.extend(child.prototype, props);
  }

  child.__super__ = parent.prototype;
  return child;
};

edu.common.de.LookupField = function(params) {
  this.inputEl = null;

  this.control = null;

  this.value = '';

  this.validator;

  var timeout = undefined;

  var field = params.field;
 
  var id = params.id;

  var that = this;

  var qFunc = function(qTerm, qCallback) {
    var timeInterval = 500;
    if (qTerm.length == 0) {
      timeInterval = 0;
    }

    if (timeout != undefined) {
      clearTimeout(timeout);
    }

    timeout = setTimeout(
      function() { 
        var promise = that.search(qTerm);
        $.when(promise).done(
          function(results) {
            qCallback(results);
          }
        );
      }, 
      timeInterval);
  };

  var onChange = function(selected) { 
    if (selected) {
      that.value = selected.id;
    } else {
      that.value = '';
    }
  };

  var initSelection = function(elem, callback) {
    if (!that.value) {
      $.when(that.getDefaultValue()).done(
        function(result) {
          that.value = result.id;
          that.control.setValue(result.id);
          callback(result);
        }
      );
    } else {
      $.when(that.lookup(that.value)).done(
        function(result) {
          callback(result);
        }
      );
    }
  };

  this.render = function() {
    this.inputEl = $("<input/>")
      .prop({id: id, title: field.toolTip, value: field.defaultValue})
      .css("border", "0px").css("padding", "0px")
      .val("")
      .addClass("form-control");
    this.validator = new edu.common.de.FieldValidator(field.validationRules, this);
    return this.inputEl;
  };

  this.postRender = function() {
    this.control = new Select2Search(this.inputEl);
    this.control.onQuery(qFunc).onChange(onChange);
    this.control.setValue(this.value);
    this.control.onInitSelection(initSelection).render();
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };

  this.getValue = function() {
    var val = this.control.getValue();
    if (val) {
      val = val.id;
    }

    return {name: field.name, value: val ? val : ''};
  };

  this.getDisplayValue = function() {
    if(!this.control) {
      this.postRender();
    }
    var val = this.control.getValue();
    if (val) {
      var displayValue = val.text;
    }
    return {name: field.name, value: displayValue ? displayValue : '' };
  }

  this.setValue = function(recId, value) {
    this.recId = recId;
    this.value = value ? value : '';
    if (this.control) {
      this.control.setValue(value);
    }
  };

  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };

  this.getDefaultValue = function() {
    return this.svc.getDefaultEntity();
  };

  this.lookup = function(id) {
    return this.svc.getEntity(id);
  };

  this.search = function(qTerm) {
    return this.svc.getEntities(qTerm);
  };
};

edu.common.de.LookupField.extend = edu.common.de.Extend;

edu.common.de.LookupSvc = function(params) {
  var entitiesMap = {};
 
  var defaultList = [];

  var defaultValue;

  this.getEntities = function(queryTerm) {
    var deferred = $.Deferred();
    if (!queryTerm && defaultList.length > 0) {
      deferred.resolve(defaultList);
      return deferred.promise();
    }

    var baseUrl = this.getApiUrl();
    var xhr;
    if (queryTerm) {      
      xhr = $.ajax({type: 'GET', url: baseUrl, data: this.searchRequest(queryTerm)});
    } else if (this.getAllXhr) {
      xhr = this.getAllXhr;
    } else {
      xhr = this.getAllXhr = $.ajax({type: 'GET', url: baseUrl, data: this.searchRequest(queryTerm)});
    }
   
   
    var that = this;
    xhr.done(
      function(data) {
        var result = that.formatResults(data);
        if (!queryTerm) {
          defaultList = result;
        }

        deferred.resolve(result);
      }
    ).fail(
      function(data) {
        alert("Failed to load entities list");
        deferred.resolve([]);
      }
    );

    return deferred.promise();
  };

  this.getEntity = function(id) {
    var deferred = $.Deferred(); 
    var entity = entitiesMap[id];
    if (entity) {
      deferred.resolve(entity);
      return deferred.promise();
    }

    for (var i = 0; i < defaultList.length; ++i) {
      if (defaultList[i].id == id) { 
        deferred.resolve(defaultList[i]);
        return deferred.promise();
      }
    }

    var that = this;
    var baseUrl = this.getApiUrl();
    $.ajax({type: 'GET', url: baseUrl + id})
      .done(function(data) {
        var result = that.formatResult(data);
        entitiesMap[id] = result;
        deferred.resolve(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve entity")
        deferred.resolve(undefined);
      });

    return deferred.promise();
  };

  this.getDefaultEntity = function() {
    var deferred = $.Deferred(); 
    if (defaultValue) {
      deferred.resolve(defaultValue);
      return deferred.promise();
    }

    var that = this;
    this.getDefaultValue().done(
      function(data) {
        var result = that.formatResult(data);
        entitiesMap[result.id] = result;
        defaultValue = result;
        deferred.resolve(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve default value");
        deferred.resolve(undefined);
      });

    return deferred.promise();
  };
};

edu.common.de.LookupSvc.extend = edu.common.de.Extend;
