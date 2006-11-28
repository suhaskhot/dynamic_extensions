function formSelectedAction() {
	
}
function tagHandlerFunction(selectedTool) {
	document.getElementById('userSelectedTool').value=selectedTool;
	
}

function showBuildFormJSP() {
 	document.getElementById('operation').value='buildForm';
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.submit();
}

function controlSelectedAction()
{	
	clearControlAttributes();
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/SelectControlAction.do";
	controlsForm.submit();
}

function formCreateAsChanged() {
}

function showHomePageFromCreateForm()
{
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.action="/dynamicExtensions/DynamicExtensionHomePage.do";
	formDefinitionForm.submit();
}
function showHomePageFromBuildForm() {
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/DynamicExtensionHomePage.do";
	controlsForm.submit();
}

function showHomePageFromCreateGroup()
{
	var groupForm = document.getElementById('groupForm');
	groupForm.action="/dynamicExtensions/DynamicExtensionHomePage.do";
	groupForm.submit();
}

function addControlToFormTree() {
	
       document.getElementById('operation').value='controlAdded';
   	var controlsForm=document.getElementById("controlsForm");
     controlsForm.action="/dynamicExtensions/AddControlsAction.do";
	controlsForm.submit();
}
function addControlToForm() {
	
	if (window.dialogArguments) 
	{
	    window.opener = window.dialogArguments;
	}
	if(window.opener!=null)
	{
		window.opener.document.getElementById('operation').value='controlAdded';
		var controlsForm=window.opener.document.getElementById("controlsForm");
		if(controlsForm!=null)
		{
			controlsForm.action="/dynamicExtensions/AddControlsAction.do";
			controlsForm.submit();	
		}
	}
	window.close();
}

function cancelControlOpern(addBtnCaption,formTitle)
{
	clearCommonAttributes();
	clearControlAttributes();
	changeOperationMode(addBtnCaption,formTitle);
}

function changeOperationMode(addBtnCaption,formTitle)
{
	if(document.getElementById('controlOperation')!=null)
	{
		document.getElementById('controlOperation').value="Add";
	}
	if(document.getElementById('addControlToFormButton')!=null)
	{
		document.getElementById('addControlToFormButton').value=addBtnCaption;
	}
	if(document.getElementById('formTitle')!=null)
	{
		document.getElementById('formTitle').innerHTML=formTitle;
	}
}

function closeWindow() {
		window.close();
}
function showNextActionConfirmDialog()
{
	var  url="/dynamicExtensions/pages/confirmNextActionDialog.jsp";
	
	 if (window.showModalDialog) 
	 {
	 	var modalDialogProperties = "dialogHeight: 200px; dialogWidth: 350px; dialogTop: 300px; dialogLeft: 350px; edge: Sunken; center: Yes; resizable: Yes; status: No; help:no";
	 	window.showModalDialog(url,window,modalDialogProperties);
	 }
	 else
	 {
	 	var windowProperties = "height=200,width=350,top=300,left=350,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=yes ,modal=yes";
	 	window.open(url, window, windowProperties);
	
	 }
}
function showCreateFormJSP() {
	if (window.dialogArguments) 
	{
		window.opener = window.dialogArguments;
	}
    	var controlsForm=window.opener.document.getElementById("controlsForm");
    	if(controlsForm!=null)
    	{
		controlsForm.action="/dynamicExtensions/LoadFormDefinitionAction.do";
		controlsForm.submit();
	}
	window.close();
}

//Added by Preeti
function dataFldDataTypeChanged(datatypeControl)
{
	if(datatypeControl!=null)
	{
		var selectedDatatype = datatypeControl.value;
		var divForDataTypeId = selectedDatatype + "DataType";
		var divForDataType = document.getElementById(divForDataTypeId);
		
		if(divForDataType!=null)
		{
			var substitutionDiv = document.getElementById('substitutionDiv');
			substitutionDiv.innerHTML = divForDataType.innerHTML;
		}
		 insertRules(datatypeControl);
	}
}
function insertRules(datatypeControl)
{
	var selectedDatatype = datatypeControl.value;
	var divForDataTypeId = selectedDatatype + "Div";

	var divForDataType = document.getElementById(divForDataTypeId);
	var divForCommonRule = document.getElementById("commonsDiv");

	if(divForDataType!=null)
	{
		var tempInnerHTML = "<table width=\"100%\"><tr><td width=\"100%\">" + divForCommonRule.innerHTML + "</td></tr><tr><td width=\"100%\">" + divForDataType.innerHTML;

		while (tempInnerHTML.indexOf("tempValidationRules") != -1)
		{
			tempInnerHTML = tempInnerHTML.replace("tempValidationRules","validationRules");
		}

		var substitutionDivRules = document.getElementById('substitutionDivRules');
		substitutionDivRules.innerHTML = tempInnerHTML;
	}
}

function initBuildForm()
{
	//If single line textbox, dont show row for noOfLines
	if(document.getElementById("linesTypeHidden")!=null)
	{
		textBoxTypeChange(document.getElementById("linesTypeHidden")); 
	}
	var dataTypeElt = document.getElementById("initialDataType");
	if(dataTypeElt!=null)
	{
		//Load datatype details for selected datatype
		dataFldDataTypeChanged(dataTypeElt);
	}
	
	var sourceElt =document.getElementById("displayChoice");
	
	if(sourceElt!=null)
	{
		//Load source details for selected sourcetype
		changeSourceForValues(sourceElt);
	}
	
	//Change visibilty of row displaying options list based on the number of rows.
	changeChoiceListTableDisplay();
	
	
	//Reinitialize counter for number of options
	initializeChoiceListCounter();
	
	
	//Initilialize default value for list of options
	initializeOptionsDefaultValue();
	
	
	//If other option is selected in measurement units, enable the text box next to it
	var cboMeasurementUnits = document.getElementById('attributeMeasurementUnits');
	measurementUnitsChanged(cboMeasurementUnits);
	
	//List box type : Combo-box or List box
	var attributeMultiSelect = document.getElementById('hiddenIsMultiSelect');
	if(attributeMultiSelect!=null)
	{
		listTypeChanged(attributeMultiSelect);
	}
	
	//Date page initializations
	var dateValueType = document.getElementById('initialDateValueType');
	if(dateValueType!=null)
	{
		changeDateType(dateValueType);
	}
}
function changeChoiceListTableDisplay()
{
	var choiceListTable = document.getElementById('choiceListTable');
	if(choiceListTable!=null)
	{
		var noOfRows = choiceListTable.rows.length;
		if(noOfRows>0)
		{
			document.getElementById('optionsListRow').style.display = "";
		}
		else
		{
			document.getElementById('optionsListRow').style.display = "none";
		}
	}
}
function initializeChoiceListCounter()
{
	var choiceListElementCnter = document.getElementById('choiceListCounter');
	var choiceListTable = document.getElementById('choiceListTable');
	if(choiceListElementCnter !=null)
	{
		var noOfChoices = 1;
		if(choiceListTable!=null)
		{
			noOfChoices = choiceListTable.rows.length;	
		}
		choiceListElementCnter.value=noOfChoices+"";
	}
}
function changeSourceForValues(sourceControl)
{
	if(sourceControl!=null)
	{
		var sourceForValues = sourceControl.value;
		
		if(sourceForValues!=null)
		{
			var divForSourceId = sourceForValues + "Values";

			var divForSource = document.getElementById(divForSourceId);
			if(divForSource!=null)
			{
				var valueSpecnDiv = document.getElementById('optionValuesSpecificationDiv');
				if(valueSpecnDiv!=null)
				{
					var divForSourceHTML = divForSource.innerHTML;
					
					while (divForSourceHTML.indexOf("tempOptionNames") != -1)
					{
						divForSourceHTML = divForSourceHTML.replace("tempOptionNames","optionNames");
					}
					while (divForSourceHTML.indexOf("tempOptionDescriptions") != -1)
					{
						divForSourceHTML = divForSourceHTML.replace("tempOptionDescriptions","optionDescriptions");
					}
					while (divForSourceHTML.indexOf("tempOptionConceptCodes") != -1)
					{
						divForSourceHTML = divForSourceHTML.replace("tempOptionConceptCodes","optionConceptCodes");
					}
					valueSpecnDiv.innerHTML = divForSourceHTML ;
				}
			}
		
		}
	}
}

//addToChoiceList : indicates whether the choice shld be added to choice list
//This will be true when called while adding choice at runtime, and false when adding at load time
function addChoiceToList(addToChoiceList)
{
	var optionName = document.getElementById('optionName');
	var optionConceptCode = document.getElementById('optionConceptCode');
	var optionDescription = document.getElementById('optionDescription');
	var choiceListElementCnter = document.getElementById('choiceListCounter');
	
	var elementNo = 1;
	if(choiceListElementCnter!=null)
	{
		elementNo = parseInt(choiceListElementCnter.value) + 1 ;
	}
	//increment number of elements count
	document.getElementById('choiceListCounter').value = elementNo + "";
	
	if((optionName!=null)&&(optionName.value!=""))
	{
		newValue = optionName.value;
		var choiceListTable  = document.getElementById('choiceListTable');
	
		if(choiceListTable!=null)
		{
			var myNewRow = choiceListTable.insertRow(choiceListTable.rows.length);
			var myNewCell =  null;
			
			//Add Option to table
			myNewCell =  myNewRow.insertCell(myNewRow.cells.length);
			myNewCell.setAttribute("id",optionName.value);
			myNewCell.setAttribute("class","formFieldBottom");
			myNewCell.setAttribute("className","formFieldBottom");
			myNewCell.setAttribute("width","10%");
			
			var chkBoxId = "chkBox" + elementNo;
			myNewCell.innerHTML = "<input type='checkbox' id='" + chkBoxId +"' value='"+optionName.value + "' />" + optionName.value;
			
			myNewCell =  myNewRow.insertCell(myNewRow.cells.length);
			myNewCell.setAttribute("style","display:none;");
			
			//Add hidden variables for option names, option description and option concept codes
			myNewCell.innerHTML  = "<input type='hidden' id='optionNames' name='optionNames' value='" + optionName.value + "' />" 
					       +"<input type='hidden' id='optionDescriptions' name='optionDescriptions' value='" + optionDescription.value + "' />" 
					       +"<input type='hidden' id='optionConceptCodes' name='optionConceptCodes' value='" + optionConceptCode.value + "' />" ;
			
			
			optionName.value = "";
			if(optionConceptCode!=null)
			{
				optionConceptCode.value="";
			}
			if(optionDescription!=null)
			{
				optionDescription.value="";
			}
			
			//Display row for option list if no of rows > 0
			var noOfRows = choiceListTable.rows.length;
			if(noOfRows>0)
			{
				document.getElementById('optionsListRow').style.display = "";
			}
			
		}
	}
	
}

function deleteElementsFromChoiceList()
{
	var valuestable = document.getElementById('choiceListTable');
	if(valuestable!=null)
	{
		
		var choiceListElementCnter = document.getElementById('choiceListCounter');
		var noOfElements = 0;
		if(choiceListElementCnter!=null)
		{
			noOfElements = parseInt(choiceListElementCnter.value);
		}
		var chkBoxId = "";
		var chkBox;
		
		for(var i=1;i<=noOfElements;i++)
		{
			
			chkBoxId = "chkBox" + i;
			chkBox = document.getElementById(chkBoxId);
			if(chkBox!=null)
			{
				var rowofCheckBox = chkBox.parentNode.parentNode ;
				
				if(chkBox.checked == true)
				{
					if(rowofCheckBox!=null)
					{
						var rowIndexOfChkBox = rowofCheckBox.rowIndex;
						if(rowIndexOfChkBox!=null)
						{
							valuestable.deleteRow(rowIndexOfChkBox);
						}
					}
				}
			}
		}
		//Hide row for option list if no of rows < 0
		var noOfRows = valuestable.rows.length;
		if(noOfRows<=0)
		{
			document.getElementById('optionsListRow').style.display = "none";
		}
	}
}
function initializeOptionsDefaultValue()
{
	var valuestable = document.getElementById('choiceListTable');
	var defaultValue = document.getElementById('attributeDefaultValue');
	if((defaultValue!=null)&&(valuestable!=null))
	{
		var rowForDefaultValue = document.getElementById(defaultValue.value+"");
		if(rowForDefaultValue!=null)
		{
			rowForDefaultValue.style.fontWeight='bold';
		}
	}
}
function setDefaultValue()
{
	var defaultValue = document.getElementById('attributeDefaultValue');
	defaultValue.value = "";
	var defaultValueSelected = false;
	var valuestable = document.getElementById('choiceListTable');
	if(valuestable!=null)
	{
		var choiceListElementCnter = document.getElementById('choiceListCounter');
		var noOfElements = 0;
		if(choiceListElementCnter!=null)
		{
			noOfElements = parseInt(choiceListElementCnter.value);
		}

		var chkBoxId = "";
		var chkBox;

		for(var i=1;i<=noOfElements;i++)
		{

			chkBoxId = "chkBox" + i;

			chkBox = document.getElementById(chkBoxId);

			if(chkBox!=null)
			{
				var rowofCheckBox = chkBox.parentNode.parentNode;
				if((chkBox.checked == true)&&(defaultValueSelected==false))
				{
					defaultValue.value = chkBox.value; 
					chkBox.checked = false;
					rowofCheckBox.style.fontWeight='bold';
					defaultValueSelected = true;
				}
				else
				{
					chkBox.checked = false;	
					rowofCheckBox.style.fontWeight='normal';
				}
			}
		}
 	}
}

//Added by sujay

function showFormPreview() 
{
	var entitySaved = document.getElementById('entitySaved');

	if(entitySaved!=null)
	{
		entitySaved.value="";
	}	
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/LoadDataEntryFormAction.do?showFormPreview=true";
	controlsForm.submit();
}

function addFormAction()
{
	document.getElementById('operationMode').value = 'AddNewForm';
	//document.getElementById('formsIndexForm').submit;
}

function textBoxTypeChange(obj)
{
	if(obj.value == 'SingleLine')
	{
		document.getElementById('rowForNumberOfLines').style.display="none";
	}
	if(obj.value == 'MultiLine')
	{
		document.getElementById('rowForNumberOfLines').style.display="";
	}
}

// Added by Chetan
function backToControlForm() 
{
	var dataEntryForm = document.getElementById('dataEntryForm');
	if(dataEntryForm != null)
	{
		dataEntryForm.action="/dynamicExtensions/LoadFormControlsAction.do";
		dataEntryForm.submit();
	}
}

function clearCommonAttributes()
{
	if(document.getElementById('caption') != null)
	{
		document.getElementById('caption').value = "";
	}
	if(document.getElementById('attributeConceptCode') != null)
	{
		document.getElementById('attributeConceptCode').value = "";
	}
	if(document.getElementById('description') != null)
	{
		document.getElementById('description').value = "";
	}
}


function clearControlAttributes()
{
var controlsForm = document.getElementById('controlsForm');

	if(document.getElementById('attributeSize') != null)
	{
	document.getElementById('attributeSize').value = "";
	}
	if(document.getElementById('attributeDefaultValue') != null)
	{
	document.getElementById('attributeDefaultValue').value = "";
	}
	if(document.getElementById('attributeDigits') != null)
	{
	document.getElementById('attributeDigits').value = "";
	}
	if(document.getElementById('attributeDecimalPlaces') != null)
	{
	document.getElementById('attributeDecimalPlaces').value = "";
	}
	
	if(document.getElementById('attributeMeasurementUnits') != null)
	{
	document.getElementById('attributeMeasurementUnits').value = "";
	}
	if(document.getElementById('measurementUnitOther') != null)
	{
	document.getElementById('measurementUnitOther').value = "";
	}
	if(document.getElementById('format') != null)
	{
	document.getElementById('format').value = "";
	}
	if(controlsForm.attributeNoOfRows != null)
	{
	controlsForm.attributeNoOfRows.value = "";
	}
	if(controlsForm.attributenoOfCols != null)
	{
	controlsForm.attributenoOfCols.value = "";
	}
	
	if(document.getElementById('attributeIsPassword') != null)
	{
		document.getElementById('attributeIsPassword').value = "";
	}
	
	if(document.getElementById('choiceListTable') != null)
	{
		deleteAllRows(document.getElementById('choiceListTable'));
		//Change visibilty of row displaying options list based on the number of rows.
		changeChoiceListTableDisplay();
			
		//Reinitialize counter for number of options
		initializeChoiceListCounter();
	
	}
	
}
function deleteAllRows(table)
{
	var noOfRows = table.rows.length;
	for(var i=noOfRows-1;i>=0;i--)
	{
		table.deleteRow(i);
	}
}

function saveEntity()
{
	var entitySaved = document.getElementById('entitySaved');
	if(entitySaved!=null)
	{	
		entitySaved.value='true';
	}
	var controlsForm = document.getElementById('controlsForm');
	if(controlsForm!=null)
	{
		controlsForm.action="/dynamicExtensions/SaveEntityAction.do";
		//controlsForm.submit();	
	}
}

function loadPreviewForm()
{
	var entitySaved = document.getElementById('entitySaved');
	if(entitySaved!=null)
	{
		if(entitySaved.value=='true')
		{
			var backBtn = document.getElementById('backToPrevious');
			if(backBtn!=null)
			{
				backBtn.disabled='true';
			}
		}
	}
}

function listTypeChanged(obj)
{
	if(obj!=null)
	{
		var rowForDisplayHeight = document.getElementById('rowForDisplayHeight');
		if(rowForDisplayHeight!=null)
		{
			if(obj.value == 'SingleSelect')
			{
				rowForDisplayHeight.style.display="none";
			}
			if(obj.value == 'MultiSelect')
			{
				rowForDisplayHeight.style.display="";
			}
		}
	}
}


function deleteAllElementsFromChoiceTable()
{
	var valuestable = document.getElementById('choiceListTable');
	
	if(valuestable!=null)
	{
		var choiceListElementCnter = document.getElementById('choiceListCounter');
		var noOfElements = 0;
		if(choiceListElementCnter!=null)
		{
			noOfElements = parseInt(choiceListElementCnter.value);
		}
		
		for(i=0; i<noOfElements; i++)
		{
			var chkBoxId = "chkBox" + i;
			chkBox = document.getElementById(chkBoxId);
			if(chkBox!=null)
			{
				chkBox.checked = true;
			}
		}
		deleteElementsFromChoiceList();
	}
}

//When Date type is changed :  Disable default value txt box for None and Todays date option
function changeDateType(dateType)
{	
	if(dateType!=null)
	{
		dateTypeValue =dateType.value;
	}
//	var defValueTxtBox = document.getElementById('attributeDefaultValue');
	var rowForDefaultValue = document.getElementById('rowForDateDefaultValue');
	if((dateTypeValue == "None")||(dateTypeValue == "Today"))
	{
		rowForDefaultValue.style.display="none";
	}
	else
	{
		rowForDefaultValue.style.display="";
	}
}

function addDynamicData(recordIdentifier)
{
	var dataEntryForm = document.getElementById('dataEntryForm');
	
	if(dataEntryForm!=null)
	{		
		if(recordIdentifier != null || recordIdentifier != "")
		{
			document.getElementById('recordIdentifier').value = recordIdentifier;
			document.getElementById('isEdit').value = "true";
		}
		dataEntryForm.action="/dynamicExtensions/ApplyDataEntryFormAction.do";
	}
}

function showFormDefinitionPage()
{
	var previewForm = document.getElementById('previewForm');
	previewForm.action="/dynamicExtensions/LoadFormDefinitionAction.do";
	previewForm.submit();
}
function showTooltip(text,obj,message) {
	var tooltip = "";
	var w1 = obj.scrollWidth;
	var w2 = obj.offsetWidth;
	var difference = w1-w2;
	if(difference > 0) {
		tooltip = text;
		obj.title = tooltip;
	} else {
		if(message != null)
		{ 
		  tooltip = message;
		  obj.title = tooltip;
		} else {
			if(obj.tagName != "IMG") {
			  obj.title = "";
			}  
		}
	}
}

function hideTooltip() 
{
  el = document.getElementById("akvtooltip");
  if( el != null)
  {
  	 el.style.visibility="hidden";
  }
}

function controlSelected(ths) {
	
	var prevRow = document.getElementById('previousControl').value;
	if (prevRow != null && prevRow != '' && prevRow != undefined) 
	{
		document.getElementById(prevRow).className = "tableItemUnselected";
	}

	document.getElementById('previousControl').value = ths.id;
	ths.className = "tableItemSelected";
	
	//Added by Preeti
	document.getElementById('controlOperation').value='Edit';
	document.getElementById('selectedControlId').value=ths.id;
	
	var controlsForm=document.getElementById('controlsForm');
    controlsForm.action='/dynamicExtensions/LoadFormControlsAction.do';
    controlsForm.submit();
}

function measurementUnitsChanged(cboMeasuremtUnits)
{
	if(cboMeasuremtUnits!=null)
	{
		var txtMeasurementUnitOther = document.getElementById('measurementUnitOther');
		if(txtMeasurementUnitOther!=null)
		{
			if(cboMeasuremtUnits.value =="other")
			{
				txtMeasurementUnitOther.disabled=false;
				txtMeasurementUnitOther.style.display="inline";
				txtMeasurementUnitOther.focus();
				
			}
			else
			{
				txtMeasurementUnitOther.value="";
				txtMeasurementUnitOther.disabled=true;
				txtMeasurementUnitOther.style.display="none";
			}
		}
	}
}
function ruleSelected(ruleObject)
{
	if(ruleObject.value == 'range') 
	{
		if(ruleObject.checked ==false)
		{
			document.getElementById('min').value='';
			document.getElementById('max').value='';
		}

	}
}

function deleteControl()
{

	checkAttribute = document.controlsForm.checkAttribute;
	//alert(checkAttribute.length);
	for(i = 0; i < checkAttribute.length-1; i++)
	{
		if(checkAttribute[i].checked)
		{
			var startPoint = (document.getElementById(checkAttribute[i].value + "rowNum")).value;
			deleteRow('controlList', startPoint)
		}
	}
	resetRowNum(checkAttribute);


}


function resetRowNum(checkAttribute)
{
	for(i = 0; i < checkAttribute.length-1; i++)
	{
		(document.getElementById(checkAttribute[i].value + "rowNum")).value = i + 1 ;
	}
}

function deleteRow(tableId, startPoint)
{
	var tab = document.getElementById(tableId);
	tab.deleteRow(startPoint);
}


function decreaseSequencenumber()
{
	checkAttribute = document.controlsForm.checkAttribute;
	for(i = 0; i < checkAttribute.length-1; i++)
	{
		if(checkAttribute[i].checked)
		{
			var startPoint = (document.getElementById(checkAttribute[i].value + "rowNum")).value;
			moveRowsUp('controlList',startPoint,1);
		}
	}
	resetRowNum(checkAttribute);
}



function moveRowsUp (tableId, startPoint, counter)
{
	var tab = document.getElementById(tableId);
	for (var i = 0 ; i < counter; i++) 
	{
		if (startPoint == 1)
		{
			break;
		}
		tab.moveRow(startPoint,startPoint-1);
		startPoint +=1;
	}
}


function increaseSequencenumber()
{
  	checkAttribute = document.controlsForm.checkAttribute;
	
	for(i = 0; i < checkAttribute.length-1; i++)
	{
		if(checkAttribute[i].checked)
		{
			var startPoint = (document.getElementById(checkAttribute[i].value + "rowNum")).value;
			//alert(startPoint);
			//alert(checkAttribute.length-1);
			if(startPoint != checkAttribute.length-1)
			{
				moveRowsDown('controlList',startPoint,1);
			}
		}
	}
	resetRowNum(checkAttribute);
}

function moveRowsDown(tableId, startPoint, counter)
{
	var tab = document.getElementById(tableId);
	for (var i = 0 ; i < counter; i++) 
	{
		tab.moveRow(startPoint,parseInt(startPoint)+1);
		startPoint -=1;
	}
}
function showDefineGroupPage(formName)
{
	var form = document.getElementById(formName+'');
	if(form!=null)
	{
		form.action="/dynamicExtensions/LoadGroupDefinitionAction.do";
		form.submit();		
	}
}

function changeGroupSource(groupSrc)
{
	
	if(groupSrc!=null)
	{
		
		var divForGrpDetails = document.getElementById('groupDetailsDiv');
		var divForGrpSrc = document.getElementById(groupSrc.value+"Div");
		if((divForGrpSrc!=null)&&(divForGrpDetails!=null))
		{
			divForGrpDetails.innerHTML = divForGrpSrc.innerHTML;
		}
	}
}

function initDefineGroupForm()
{
	
	changeGroupSource(document.getElementById('createGroupAs'));
}

function showDefineFormJSP()
{
	
	var groupForm = document.getElementById('groupForm');
	var groupOperation = document.getElementById('groupOperation');
	if(groupOperation!=null)
	{
		groupOperation.value = "showNextPage";
	}
	if(groupForm!=null)
	{
		groupForm.action = "/dynamicExtensions/ApplyGroupDefinitionAction.do";
		groupForm.submit();
	}
}

function setEditOperationMode(target)
{
	document.getElementById('operationMode').value = 'EditForm';
	var formsIndexForm = document.getElementById('formsIndexForm');
	formsIndexForm.action = target;
	formsIndexForm.submit();
}

function saveGroup()
{
	var groupForm = document.getElementById('groupForm');
	var groupOperation = document.getElementById('groupOperation');
	if(groupOperation!=null)
	{
		groupOperation.value = "savegroup";
	}
	if(groupForm!=null)
	{
		groupForm.action = "/dynamicExtensions/ApplyGroupDefinitionAction.do";
		groupForm.submit();
	}
}
function toggle(id,p) 
{
	selId =document.getElementById('selectedAttrib').value; 
	if(selId!='')
		document.getElementById(selId).style.fontWeight='normal';
	document.getElementById('selectedAttrib').value='';
	var myChild = document.getElementById(id);
	if((myChild!=null)&&(myChild!=undefined))
	{
		if(myChild.style.display!='block')
		{
			myChild.style.display='block';
			document.getElementById(p).className='folderOpen';
		}
		else
		{
			myChild.style.display='none';
			document.getElementById(p).className='folder';
		}
	}
}
function changeSelection(str1,seqno)
{	
	selId =document.getElementById('selectedAttrib').value;
	document.getElementById('selectedAttrib').value=str1;
	document.getElementById(str1).style.fontWeight='bold';
	if(selId!='')
	{
		document.getElementById(selId).style.fontWeight='normal';
	}
	var controlsForm=document.getElementById('controlsForm');
}
