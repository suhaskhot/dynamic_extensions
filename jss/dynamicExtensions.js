function formSelectedAction() {
	alert("This is a form to be copied");
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
	document.getElementById('operation').value='controlSelectedAction';
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.submit();
}


function formCreateAsChanged() {
}
function showCreateFormView()
{

 	document.getElementById('operation').value='showCreateFormView';
	var controlsForm = document.getElementById('controlsForm');
	controlsForm.submit();
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

function addControlToForm() {
	var controlsForm = document.getElementById("controlsForm");
	controlsForm.action="/dynamicExtensions/AddControlsAction.do";
	controlsForm.submit();
}


