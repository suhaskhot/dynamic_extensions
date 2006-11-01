function formSelectedAction() {
	//alert("This is a form to be copied");
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
	var controlOperation = document.getElementById('controlOperation');
	controlOperation.value = 'Add';
	
	var toolBoxClicked = document.getElementById('toolBoxClicked');
	toolBoxClicked.value = 'True';
	
	clearForm();
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/LoadFormControlsAction.do";
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
function addControlToFormTree() {
	document.getElementById('operation').value='controlAdded';
    var controlsForm=document.getElementById("controlsForm");
    controlsForm.action="/dynamicExtensions/AddControlsAction.do";
//    document.getElementById('controlOperation').value='add';
	controlsForm.submit();
}
function addControlToForm() {
	
	var arg = window.dialogArguments;
	arg.document.getElementById('operation').value='controlAdded';
    	var controlsForm=arg.document.getElementById("controlsForm");
	controlsForm.action="/dynamicExtensions/AddControlsAction.do";
	controlsForm.submit();
	window.close();
}

function closeWindow() {
		window.close();
}
function showNextActionConfirmDialog()
{
	var  url="/dynamicExtensions/pages/confirmNextActionDialog.jsp";
	 var properties = "dialogHeight: 200px; dialogWidth: 350px; dialogTop: 300px; dialogLeft: 350px; edge: Sunken; center: Yes; resizable: Yes; status: No; help:no"
     window.showModalDialog(url, window, properties);
}
function showCreateFormJSP() {
	var arg = window.dialogArguments;
    	var controlsForm=arg.document.getElementById("controlsForm");
	controlsForm.action="/dynamicExtensions/LoadFormDefinitionAction.do";
	controlsForm.submit();
	window.close();
}

//Added by Preeti
function changeDataType(datatypeControl)
{
	var selectedIdx = datatypeControl.selectedIndex;
	if(datatypeControl!=null)
	{
		if(datatypeControl.options!=null)
		{
			var divForDataTypeId = datatypeControl.options[selectedIdx].text + "DataType";
			var divForDataType = document.getElementById(divForDataTypeId);
			if(divForDataType!=null)
			{
				var substitutionDiv = document.getElementById('substitutionDiv');
				substitutionDiv.innerHTML = divForDataType.innerHTML;

				//Disable the value specification code for boolean values

				var valueSpecnDiv = document.getElementById('valueSpecificationDiv');
				if(valueSpecnDiv!=null)
				{
					if(divForDataTypeId == "BooleanDataType")
					{
						valueSpecnDiv.style.display = "none";
					}
					else
					{
						valueSpecnDiv.style.display = "block";
					}
				}

			}
		}
	}
}



function initBuildForm()
{
	var dataTypeElt = document.getElementById("dataType");
	if(dataTypeElt!=null)
	{
	//Load datatype details for selected datatype
		changeDataType(dataTypeElt);
	}
	
	/*var sourceElt =document.getElementById("displayChoice");
	if(sourceElt!=null)
	{
		//Load source details for selected sourcetype
		changeSourceForValues(sourceElt);
	}*/
	
	//Reinitialize counter
	var choiceListElementCnter = document.getElementById('choiceListCounter');
	if(choiceListElementCnter !=null)
	{
		choiceListElementCnter.value="1";
	}
	addChoicesFromListToTable();
}
function addChoicesFromListToTable()
{
	var choiceList = document.getElementById("choiceList");
	if(choiceList!=null)
	{
		var choiceListValue = choiceList.value;
		if((choiceListValue!=null)&&(choiceListValue!=""))
		{
			var choice_array=choiceListValue.split(",");
			if(choice_array!=null)
			{
				for(var i=0;i<choice_array.length;i++)
				{
					if((choice_array[i]!=null)&&(choice_array[i]!=""))
					{
						//Add choice to list
						var textBox = document.getElementById('choiceValue');
						if(textBox !=null)
						{
							textBox.value = choice_array[i];
							addChoiceToList(false);
						}	
					}
					
				}
			}
		}
	}
}
/*function changeSourceForValues(sourceControl)
{
	if(sourceControl!=null)
	{
		var selectedIdx = sourceControl.selectedIndex;
		if(sourceControl.options!=null)
		{
			for(var i=0; i<sourceControl.length; i++)
			{
				var divForSourceId = sourceControl.options[i].text + "Values";

				var divForSource = document.getElementById(divForSourceId);
				if(divForSource!=null)
				{
					var valueSpecnDiv = document.getElementById('valueSpecificationDiv');
					if(valueSpecnDiv!=null)
					{
						valueSpecnDiv.innerHTML = divForSource.innerHTML;
					}

				}
			}
		}
		else
		{
			var divForSource = document.getElementById("UserDefinedValues");
			if(divForSource!=null)
			{
				var valueSpecnDiv = document.getElementById('valueSpecificationDiv');
				if(valueSpecnDiv!=null)
				{
					valueSpecnDiv.innerHTML = divForSource.innerHTML;
				}

			}
		}
	}
}*/

//addToChoiceList : indicates whether the choice shld be added to choice list
//This will be true when called while adding choice at runtime, and false when adding at load time
function addChoiceToList(addToChoiceList)
{
	var textBox = document.getElementById('choiceValue');
	var choiceListElementCnter = document.getElementById('choiceListCounter');
	
	var elementNo = 0;
	if(choiceListElementCnter!=null)
	{
		elementNo = choiceListElementCnter.value;
	}

	if((textBox!=null)&&(textBox.value!=""))
	{
		newValue = textBox.value;
		var choiceListTable  = document.getElementById('choiceListTable');
	
		if(choiceListTable!=null)
		{
			var myNewRow = choiceListTable.insertRow();
			var myNewCell =  null;
			//Radio button for default value selection
			myNewCell =  myNewRow.insertCell();
			myNewCell.setAttribute("className","formMessage");
			myNewCell.innerHTML="<input type='radio' name='attributeDefaultValue' value='"+textBox.value+"'>"  ;
			
			//Text 
			myNewCell =  myNewRow.insertCell();
			myNewCell.setAttribute("className","formMessage");
			myNewCell.innerHTML=textBox.value;
			
			var choicelist = document.getElementById('choiceList');
			if(choicelist !=null)
			{
				//add to choicelist
				if(addToChoiceList == true)
				{
					choicelist.value = choicelist.value + "," + textBox.value;
				}
			}
			textBox.value = "";
			document.getElementById('choiceListCounter').value = (parseInt(elementNo) + 1) + "";
			
			//Checkbox for delete
			myNewCell =  myNewRow.insertCell();
			myNewCell.setAttribute("className","formMessage");
			var chkBoxId = "chkBox" + elementNo;
			myNewCell.innerHTML="<input type='checkbox' id='" + chkBoxId +"' >";
		}
	}
}

function deleteElementsFromChoiceList()
{
	var valuestable = document.getElementById('choiceListTable');
	if(valuestable!=null)
	{
		//Clear choice list
		var choicelist = document.getElementById('choiceList');
		if(choicelist !=null)
		{
			choicelist.value = "";
		}
		var choiceListElementCnter = document.getElementById('choiceListCounter');
		var noOfElements = 0;
		if(choiceListElementCnter!=null)
		{
			noOfElements = parseInt(choiceListElementCnter.value);
		}
		
		var chkBoxId = "";
		var chkBox;
		for(var i=0;i<noOfElements;i++)
		{
			
			chkBoxId = "chkBox" + i;
	
			chkBox = document.getElementById(chkBoxId);
			if(chkBox!=null)
			{
				var rowofCheckBox = chkBox.parentElement.parentElement;
				
				if(chkBox.checked == true)
				{
					if(rowofCheckBox!=null)
					{
						var rowIndexOfChkBox = rowofCheckBox.rowIndex;
						if(rowIndexOfChkBox!=null)
						{
							valuestable.deleteRow(rowIndexOfChkBox);//since first row is header
						}
					}
				}
				else
				{
					//Add to choice list if not selected
					choicelist.value = choicelist.value + ","  + rowofCheckBox.cells[1].innerHTML  ;
				}
				
			}
		}
	}
	
}


//Added by sujay

function showFormPreview() 
{
	var showPreview = document.getElementById('showPreview');
	showPreview.value = 'True';
	
	var entitySaved = document.getElementById('entitySaved');

	if(entitySaved!=null)
	{
		entitySaved.value="";
	}	
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/LoadFormPreviewAction.do";
	controlsForm.submit();
}

function addFormAction()
{
	document.getElementById('mode').value = 'AddNewForm';
	//document.getElementById('formsIndexForm').submit;
}

function radioButtonClicked(obj)
{
if(obj.value == 'SingleLine')
	{
		document.getElementById('attributeNoOfRows').value="";
		document.getElementById('attributeNoOfRows').disabled=true;
		document.getElementById('noOfLines').disabled=true;
	}
	if(obj.value == 'MultiLine')
	{

		document.getElementById('attributeNoOfRows').disabled=false;
		document.getElementById('noOfLines').disabled=false;
	}
}

// Added by Chetan
function backToControlForm() 
{
	var previewForm = document.getElementById('previewForm');
	if(previewForm!=null)
	{
		previewForm.action="/dynamicExtensions/LoadFormControlsAction.do";
		previewForm.submit();
	}
}

function clearForm()
{
var controlsForm = document.getElementById('controlsForm');

if(controlsForm.name != null)
	{
	controlsForm.name.value = "";
	}
	if(controlsForm.caption != null)
	{
	controlsForm.caption.value = "";
	}
	if(controlsForm.description != null)
	{
	controlsForm.description.value = "";
	}
	if(controlsForm.cssClass != null)
	{
	controlsForm.cssClass.value = "";
	}
	if(controlsForm.tooltip != null)
	{
	controlsForm.tooltip.value = "";
	}
	
	
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
	if(controlsForm.attributeConceptCode != null)
	{
	controlsForm.attributeConceptCode.value = "";
	}
	if(controlsForm.choiceList != null)
	{
	controlsForm.choiceList.value = "";
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
	if(obj.value == 'SingleSelect')
	{
		document.getElementById('attributeNoOfRows').value="";
		document.getElementById('attributeNoOfRows').disabled=true;
	}
	if(obj.value == 'MultiSelect')
	{

		document.getElementById('attributeNoOfRows').disabled=false;
	}
}
function listDataTypeChanged(listControl)
{
	if(listControl!=null)
	{
		var selectedDataType = listControl.value;
		if(selectedDataType=="Yes/No")
		{
			deleteAllElementsFromChoiceTable();
			//Clear choice list
			var choiceList = document.getElementById('choiceList');
			if(choiceList!=null)
			{
				choiceList.value = "true,false";	//True and false values for yes/no flds
				addChoicesFromListToTable();
			}
			
			//Disable add btn
			var addBtn = document.getElementById('addChoiceValue');
			if(addBtn!=null)
			{
				addBtn.disabled=true;
			}
			
			//Disable delete button
			var deleteBtn = document.getElementById('deleteChoiceValue');
			if(deleteBtn!=null)
			{
				deleteBtn.disabled=true;
			}
		}
		else
		{
		
			//Enable add btn
			var addBtn = document.getElementById('addChoiceValue');
			if(addBtn!=null)
			{
				addBtn.disabled=false;
			}

			//Enable delete button
			var deleteBtn = document.getElementById('deleteChoiceValue');
			if(deleteBtn!=null)
			{
				deleteBtn.disabled=false;
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
	var defValueTxtBox = document.getElementById('attributeDefaultValue');
	if((dateTypeValue == "None")||(dateTypeValue == "Today"))
	{
		defValueTxtBox.disabled=true;
	}
	else
	{
		defValueTxtBox.disabled=false;
	}
}

function addDynamicData()
{
	var previewForm = document.getElementById('previewForm');
	if(previewForm!=null)
	{
		previewForm.action="/dynamicExtensions/ApplyDataEntryFormAction.do";
	}
}
function showFormDefinitionPage()
{
	var previewForm = document.getElementById('previewForm');
	previewForm.action="/dynamicExtensions/LoadFormDefinitionAction.do";
	previewForm.submit();
}