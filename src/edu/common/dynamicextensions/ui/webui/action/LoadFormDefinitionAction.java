
package edu.common.dynamicextensions.ui.webui.action;

/**
 * This Action class Loads the Primary Information needed for CreateForm.jsp.
 * This will first check if the form object is already present in cache , If yes, it will update
 * the actionForm and If No, It will populate the actionForm with fresh data.  
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadFormDefinitionProcessor;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.TreeData;
import edu.common.dynamicextensions.ui.webui.util.TreeGenerator;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;

public class LoadFormDefinitionAction extends BaseDynamicExtensionsAction
{
	/**
	 * This method will call LoadFormDefinitionProcessor to load all the information needed for the form.
	 * It will then forward the action to CreateForm.jsp. 
	 * 
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm) form;
		
		try
		{
			populateContainerInformation(request,formDefinitionForm);
		}
		catch (DynamicExtensionsSystemException dynamicExtensionsSystemException)
		{
			String actionForwardString = catchException(dynamicExtensionsSystemException, request);
			return(mapping.findForward(actionForwardString));
		}
		catch (DynamicExtensionsApplicationException dynamicExtensionsApplicationException)
		{
			String actionForwardString = catchException(dynamicExtensionsApplicationException, request);
			return(mapping.findForward(actionForwardString));
		}
		String groupName = getGroupName(request);
		formDefinitionForm.setGroupName(groupName);
		formDefinitionForm.setTreeData(getEntityTree(groupName));
		formDefinitionForm.setCreateAs(ProcessorConstants.DEFAULT_FORM_CREATEAS);
		return (mapping.findForward(Constants.SUCCESS));
	}

	/**
	 * @param request
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void populateContainerInformation(HttpServletRequest request, FormDefinitionForm formDefinitionForm)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		LoadFormDefinitionProcessor loadFormDefinitionProcessor = LoadFormDefinitionProcessor.getInstance();
		ContainerInterface container = null;
		
		String operationMode = formDefinitionForm.getOperationMode();
		
		String containerIdentifier = request.getParameter("containerIdentifier");
		
		if (operationMode != null && operationMode.equalsIgnoreCase(Constants.ADD_NEW_FORM))
		{
			loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
		}
		else if (operationMode != null && operationMode.equalsIgnoreCase(Constants.EDIT_FORM))
		{
			container = loadFormDefinitionProcessor.getContainerForEditing(containerIdentifier);
			loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
			CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, container);
		}
		else
		{
			container = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
			if (container != null)
			{
				loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
			}
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private String getGroupName(HttpServletRequest request)
	{
		String groupName = null;
		//Get group object from cache and return it
		EntityGroupInterface entityGroup = (EntityGroupInterface) CacheManager.getObjectFromCache(request, Constants.ENTITYGROUP_INTERFACE);
		if(entityGroup!=null)
		{
			groupName = entityGroup.getName();
		}
		else
		{
			groupName = "";
		}
		return groupName;
	}
	/**
	 * @param request
	 */
	private TreeData getEntityTree(String groupName)
	{
		if(groupName!=null)
		{
			TreeGenerator treeGenerator = new TreeGenerator();
			List childList = getChildList();
			return treeGenerator.getTreeData(groupName, childList);
		}
		return null;
	}

	/**
	 * @return List of children for the tree
	 */
	private List getChildList()
	{
		ArrayList<NameValueBean> treeChildList = new ArrayList<NameValueBean>(); 
		NameValueBean treeChildNode = new NameValueBean("New Form","1");
		treeChildList.add(treeChildNode);
		return treeChildList;
	}

	
}
