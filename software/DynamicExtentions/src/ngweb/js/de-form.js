var edu = edu || {};
edu.common = edu.common || {};
edu.common.de = edu.common.de || {};

edu.common.de.Form = function(args) {
  this.formDiv = $("#" + args.formDiv);
  this.fieldObjs = [];
 
  this.formId = args.id;
  this.formDef = args.formDef;
  this.formDefUrl = args.formDefUrl;
  this.formDefXhr = null;

  this.recordId = args.recordId;
  this.formData = args.formData;
  this.formDataUrl = args.formDataUrl;
  this.formDataXhr = null;

  this.appData = args.appData;
  
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

  this.destroy = function() {
    this.formDiv.children().remove();
  };

  this.render0 = function() {
    var formCtrls = $("<div/>");

    var rows = this.formDef.rows;
    for (var i = 0; i < rows.length; ++i) {
      formCtrls.append(this.rowCtrls(rows[i]));
    }

    formCtrls.append(this.getActionButtons());

    var panel = edu.common.de.Utility.panel(this.formDef.caption, formCtrls, 'default');
    this.formDiv.append(panel);

    
    for (var i = 0; i < this.fieldObjs.length; ++i) {
      if (this.formData) {
        this.fieldObjs[i].setValue(this.formData[this.fieldObjs[i].getName()]);
      }
      this.fieldObjs[i].postRender();
    }
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
    var labelEl = this.fieldLabel(id, field.caption);

    var fieldObj = edu.common.de.FieldFactory.getField(field);
    this.fieldObjs.push(fieldObj);

    var fieldEl = fieldObj.render();
    return $("<div/>").addClass("form-group").append(labelEl).append(fieldEl);
  };

  this.fieldLabel = function(name, label) {
    return $("<label/>").addClass("control-label").prop('for', name).append(label);
  };

  this.getActionButtons = function() {
    var btns =   $("<div/>").addClass("modal-footer");
    var save =   $("<button/>").attr({"type": "button", "id": "saveForm"}).addClass("btn btn-primary").append("Save");
    var cancel = $("<button/>").attr({"type": "button", "id": "cancelForm"}).addClass("btn btn-default").append("Cancel");

    var that = this;
    save.on("click", function() { that.save(); });
    return edu.common.de.Utility.row().append(btns.append(cancel).append(save));
  };

  this.save = function() {
    var formData = {appData: this.appData};

    var url = this.formDataUrl.replace(":formId", this.formId);
    var method;
    if (this.recordId) {
      url = url.replace(":recordId", this.recordId);
      formData.recordId = this.recordId;
      method = 'PUT';
    } else {
      url = url.replace(":recordId", "");
      method = 'POST';
    }

    for (var i = 0; i < this.fieldObjs.length; ++i) {
      var value = this.fieldObjs[i].getValue();
      formData[value.name] = value.value;
    }
 
    $.ajax({
      type: method,
      url: url,
      data: JSON.stringify(formData)
    }).done(function(data) { 
      if (args.onSaveSuccess) {
        args.onSaveSuccess(data);     
      }
    }).fail(function(data) { 
      if (args.onSaveError) {
        args.onSaveError(data);
      }
    });
  }
};

edu.common.de.FieldFactory = {
  getField: function(field, idx) {
    var fieldObj;
    idx = idx || "";
    var id = 'de-' + field.name + "-" + idx;

    if (field.type == 'stringTextField') {
      fieldObj = new edu.common.de.TextField(id, field);
    } else if (field.type == 'numberField') {
      fieldObj = new edu.common.de.NumberField(id, field);
    } else if (field.type == 'textArea') {
      fieldObj = new edu.common.de.TextArea(id, field);
    } else if (field.type == 'datePicker') {
      fieldObj = new edu.common.de.DatePicker(id, field);
    } else if (field.type == 'booleanCheckbox') {
      fieldObj = new edu.common.de.BooleanCheckbox(id, field);
    } else if (field.type == 'combobox' || field.type == 'listbox') {
      fieldObj = new edu.common.de.SelectField(id, field);
    } else if (field.type == 'radiobutton' || field.type == 'checkbox') {
      fieldObj = new edu.common.de.GroupField(id, field);
    } else if (field.type == 'subForm') {
      fieldObj = new edu.common.de.SubFormField(id, field);
    } else {
      fieldObj = new edu.common.de.UnknownField(id, field);
    }

    return fieldObj;
  }
};

edu.common.de.TextField = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<input/>").prop({id: id, type: 'text'}).addClass("form-control");
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(value) {
    this.inputEl.val(value);
  };
};

edu.common.de.NumberField = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<input/>").prop({id: id, type: 'number'}).addClass("form-control");
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(value) {
    this.inputEl.val(value);
  };
};

edu.common.de.TextArea = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<textarea/>").prop({id: id, rows: field.noOfRows}).addClass("form-control");
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(value) {
    this.inputEl.val(value);
  };
};

edu.common.de.DatePicker = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<input/>").prop({id: id, type: 'text'});
    this.inputEl.addClass("form-control de-date-picker");

    var format = field.format;
    if (format && format.length != 0) {
      format = format.toLowerCase();
    } else {
      format = "mm-dd-yyyy";
    }

    this.inputEl.datepicker({format: format, autoclose: true});
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(value) {
    this.inputEl.val(value);
  };
};

edu.common.de.BooleanCheckbox = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<input/>").prop({type: 'checkbox', id: id, value: 'true'}).addClass("form-control");
    return this.inputEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(value) {
    this.inputEl.val(value);
  };
};

edu.common.de.SelectField = function(id, field) {
  this.inputEl = null;

  this.render = function() {
    this.inputEl = $("<select/>").prop({id: id, multiple: field.type == 'listbox'})
                                 .addClass("form-control");
    for (var i = 0; i < field.pvs.length; ++i) {
      var pv = field.pvs[i];
      this.inputEl.append($("<option/>").prop("value", pv.value).append(pv.value));
    }
    return this.inputEl;
  };

  this.postRender = function() {
    this.inputEl.chosen({width: "100%"});
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function() {
    return {name: field.name, value: this.inputEl.val()};
  };

  this.setValue = function(value) {
    this.inputEl.val(value);
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
      if (count % optionsPerRow == 0) {
        if (currentDiv) {
          this.clusterEl.push(currentDiv);
        }

        currentDiv = $("<div/>");
      }

      var btn = $("<input/>").prop({type: type, name: field.name, value: pv.value});
      currentDiv.append($("<label/>").addClass(typeclass).append(btn).append(pv.value).css("width", width));
      this.inputEls.push(btn);
      ++count;
    }

    this.clusterEl.push(currentDiv);
    this.clusterEl = $("<div/>").addClass("form-inline").append(this.clusterEl);
    return this.clusterEl;
  };

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
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

  this.setValue = function(value) {
    var checked;
    if (field.type == 'radiobutton') {
      checked = [value];
    } else {
      checked = value;
    }

    for (var i = 0; i < this.inputEls.length; ++i) {
      if ($.inArray(this.inputEls[i].val(), checked) != -1) {
        this.inputEls[i].prop('checked', true);
      }
    }
  };
};

edu.common.de.SubFormField = function(id, sfField) {
  this.subFormEl = null;
  this.sfFieldsEl = null;
  this.rowIdx = 0;
  this.fieldObjsRows = [];

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
    var width = "9%";
    if (numFields < 10) {
      width = Math.floor(95 / numFields) + "%";
    }

    return width;
  };

  this.fields = this.getFields();

  this.widthCls = this.getWidthClass(this.getFields().length);

  this.render = function() {
    this.sfFieldsEl = $("<div/>");
    this.subFormEl = $("<div/>").prop({id: id})
                       .append(this.getHeading())
                       .append(this.sfFieldsEl)
                       .append(this.getAddButton());

    return this.subFormEl;
  };

  this.postRender = function() {
  };  

  this.getName = function() {
    return sfField.name;
  };

  this.getValue = function() {
    var values = [];
    for (var i in this.fieldObjsRows) {
      var fieldObjs = this.fieldObjsRows[i];
      var sfInstance = {};
      for (var j = 0; j < fieldObjs.length; ++j) {
        var value = fieldObjs[j].getValue();
        sfInstance[value.name] = value.value;
      }

      values.push(sfInstance);
    }
    return {name: sfField.name, value: values};
  };

  this.setValue = function(value) {
    this.sfFieldsEl.children().remove();
    this.fieldObjsRows = [];

    for (var i = 0; i < value.length; ++i) {
      var sfInst = value[i];
      this.addSubFormFieldsRow(false);
      
      var fieldObjs = this.fieldObjsRows[i];
      for (var j = 0; j < fieldObjs.length; ++j) {
        var fieldValue = sfInst[fieldObjs[j].getName()];
        fieldObjs[j].setValue(fieldValue);
        fieldObjs[j].postRender();
      }
    }
  };

  this.getHeading = function() {
    var heading = $("<div/>").addClass("form-group clearfix");
    for (var i = 0; i < this.fields.length; ++i) {
      var field = this.fields[i];
      var column = $("<div/>").css("width", this.widthCls).addClass("de-sf-field form-inline").append(field.caption);
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
    var removeBtn = edu.common.de.Utility.iconButton({btnClass: 'btn btn-default form-control', icon: 'trash'});
    var that = this;
    removeBtn.on("click", function() {
      rowDiv.remove();
      delete that.fieldObjsMap["'" + rowIdx + "'"];
    });

    return removeBtn;
  };

  this.addSubFormFieldsRow = function(postRender) {
    var fieldObjs = [];

    var rowDiv = $("<div/>").addClass("form-group clearfix");
    for (var i = 0; i < this.fields.length; ++i) {
      var field = this.fields[i];
      var fieldObj = edu.common.de.FieldFactory.getField(field, this.rowIdx);
      rowDiv.append(this.cell(fieldObj.render()));
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
    this.rowIdx++;
  };

  this.cell = function(el, width) {
    if (!width) {
      width = this.widthCls;
    }

    return $("<div/>").css("width", width).addClass("form-inline de-sf-field").append(el);
  };
}

edu.common.de.UnknownField = function(id, field) {
  this.inputEl = $("<div/>").append("Field type " + field.get('type') + " under known");

  this.render = function() {
    return this.inputEl;
  }

  this.postRender = function() {
  };

  this.getName = function() {
    return field.name;
  };

  this.getValue = function(value) {
  };

  this.setValue = function(value) {
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
  }
};
