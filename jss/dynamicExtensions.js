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
    var controlsForm=document.getElementById("controlsForm");
	controlsForm.action="/dynamicExtensions/AddControlsAction.do";
	controlsForm.submit();
}
function addControlToForm() {
	var arg = window.dialogArguments;
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
	arg.document.getElementById('operation').value='showCreateFormView';
	controlsForm.submit();
	window.close();
}