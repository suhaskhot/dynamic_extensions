function formSelectedAction() {
	alert("This is a form to be copied");
}

function nextClicked() {
 	document.getElementById('operation').value='addControlsToForm';
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.submit();
}

function controlSelectedAction(controlSelectedValue)
{
alert(controlSelectedValue);
	document.getElementById('operation').value='controlSelected';
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.submit();
}


function formCreateAsChanged() {
alert("hi");
	/*var formTypeSelected = document.getElementById('createAs').value;
	var createAsTypeChanged = document.getElementById('createAsTypeChanged');
    createAsTypeChanged.value = formTypeSelected;
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.submit();*/
	
}

