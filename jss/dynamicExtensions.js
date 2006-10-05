function formSelectedAction() {
	alert("This is a form to be copied");
}


function showBuildFormJSP() {

 	document.getElementById('operation').value='buildForm';
	var formDefinitionForm = document.getElementById('formDefinitionForm');
	formDefinitionForm.submit();
}

function controlSelectedAction(controlSelectedValue)
{	
	document.getElementById('selectedTool').value=controlSelectedValue;
	document.getElementById('operation').value='controlSelectedAction';
	var controlsForm = document.getElementById('controlsForm');
//	controlsForm.action="/dynamicExtensions/SelectControlAction.do";
	controlsForm.submit();
}


function formCreateAsChanged() {


}
function showCreateFormView()
{

 	document.getElementById('operation').value='showCreateFormView';
	var controlsForm = document.getElementById('controlsForm');
	//controlsForm.action="/dynamicExtensions/LoadFormDefinitionAction.do";
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


