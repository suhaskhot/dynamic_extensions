/*
 * Created on Nov 15, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadGroupDefinitionProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.GroupForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadGroupDefinitionAction extends BaseDynamicExtensionsAction
{
	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		GroupForm groupForm = (GroupForm)form;
		loadGroup(request, groupForm);
		
		groupForm.setGroupList(getListOfGroups());
		return mapping.findForward("success"); 
	}

	/**
	 * @return List of group names
	 */
	private List getListOfGroups()
	{
		ArrayList<String> groupList = new ArrayList<String>();
		groupList.add("group1");
		groupList.add("group2");
		groupList.add("group2");
		groupList.add("group2");
		return groupList;
	}
	private void loadGroup(HttpServletRequest request ,GroupForm groupForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String operationMode = groupForm.getOperationMode();
		String containerIdentifier = request.getParameter("containerIdentifier");
		ContainerInterface container = null;
		if (operationMode != null && operationMode.equalsIgnoreCase(Constants.EDIT_FORM))
		{
			container = DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
			CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, container);
		}
		else
		{
			container = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		}
		EntityGroupInterface entityGroup = null;
		if(container!=null)
		{
			EntityInterface entity = container.getEntity();
			if(entity!=null)
			{
				Collection<EntityGroupInterface> entityGroupCollection = entity.getEntityGroupCollection();
				if(entityGroupCollection!=null)
				{
					Iterator<EntityGroupInterface> entityGroupIter = entityGroupCollection.iterator();
					if(entityGroupIter.hasNext())
					{
						entityGroup = entityGroupIter.next();
					}
				}
			}
		}
		else
		{
			entityGroup= (EntityGroupInterface) CacheManager.getObjectFromCache(request, Constants.ENTITYGROUP_INTERFACE);
		}
		LoadGroupDefinitionProcessor loadGroupDefinitionProcessor = LoadGroupDefinitionProcessor.getInstance();
		loadGroupDefinitionProcessor.loadGroupDetails(entityGroup,groupForm);
	}
}