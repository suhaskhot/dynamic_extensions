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

	document.getElementById('operation').value='controlSelected';
	document.getElementById('selectedTool').value=controlSelectedValue;
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.submit();
}


function formCreateAsChanged() {


}

