function formSelectedAction() {
	alert("This is a form to be copied");
}

function showBuildFormJSP() {

 	document.getElementById('operation').value='addControlsToForm';
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.submit();
}

function controlSelectedAction(controlSelectedValue)
{	
	document.getElementById('selectedTool').value=controlSelectedValue;
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/SelectControlAction.do";
	controlsForm.submit();
}


function formCreateAsChanged() {


}
function showCreateFormView()
{
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.action="/dynamicExtensions/LoadFormDefinitionAction.do";
	controlsForm.submit();
}
function showHomePage()
{
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.action = "/dynamicExtensions/ShowHomePageAction.do";
	formDefinitionForm.submit();
}


