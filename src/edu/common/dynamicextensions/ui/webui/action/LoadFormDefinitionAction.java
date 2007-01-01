
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
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadFormDefinitionProcessor;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.AssociationTreeObject;
import edu.common.dynamicextensions.util.global.Constants;

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
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm) form;
		try
		{
			populateContainerInformation(request, formDefinitionForm);
		}
		catch (Exception e)
		{
			String actionForwardString = catchException(e, request);
			if ((actionForwardString == null) || (actionForwardString.equals("")))
			{
				return mapping.getInputForward();
			}
			return (mapping.findForward(actionForwardString));
		}

		initializeFormAttributes(request, formDefinitionForm);
		return (mapping.findForward(Constants.SUCCESS));
	}

	/**
	 * @param formDefinitionForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void initializeFormAttributes(HttpServletRequest request, FormDefinitionForm formDefinitionForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String groupName = getGroupName(request);
		formDefinitionForm.setGroupName(groupName);
		boolean addNewNode = true;
		if ((formDefinitionForm.getOperationMode() != null) && (formDefinitionForm.getOperationMode().equals(Constants.EDIT_FORM)))
		{
			addNewNode = false;
		}
		formDefinitionForm.setCurrentEntityTreeXML(getXMLForCurrentEntity(request,addNewNode));
		//formDefinitionForm.setTreeData(getEntityTree(request, addNewNode));
		formDefinitionForm.setCreateAs(ProcessorConstants.DEFAULT_FORM_CREATEAS);
		formDefinitionForm.setViewAs(ProcessorConstants.DEFAULT_FORM_VIEWAS);
		if(formDefinitionForm.getDefinedEntitiesTreeXML()==null)
		{
			formDefinitionForm.setDefinedEntitiesTreeXML(getXMLForDefinedEntities());
		}
	}

	/**
	 * @param request
	 * @param addNewNode
	 * @return
	 */
	private String getXMLForCurrentEntity(HttpServletRequest request, boolean addNewNode)
	{
		StringBuffer currentEntityXML = new StringBuffer();
		ContainerInterface container = (ContainerInterface)CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		String currentContainerName = (String) CacheManager.getObjectFromCache(request, Constants.CURRENT_CONTAINER_NAME);
		currentEntityXML.append("<?xml version='1.0' encoding='iso-8859-1'?> ");
		currentEntityXML.append("<tree id='0'>");
		currentEntityXML.append(getNodeForContainer(container,currentContainerName,addNewNode,true));
		currentEntityXML.append("</tree>");

		return currentEntityXML.toString();
	}

	/**
	 * @param container
	 * @param currentContainerName 
	 * @param addNewNode 
	 * @return
	 */
	private String getNodeForContainer(ContainerInterface container, String currentContainerName, boolean addNewNode,boolean showExpanded)
	{
		StringBuffer xmlNodeForContainer = new StringBuffer();
		if(container!=null)
		{
			//Entity tree will always be accessed with the container name
			String containerName = container.getCaption();
			/*Long containerId = container.getId();
			if(containerId!=null)
			{
				xmlNodeForContainer.append(getXMLNode(containerId.toString(),containerName,false,showExpanded));
			}
			else
			{*/
			xmlNodeForContainer.append(getXMLNode(null,containerName,false,showExpanded));
			//}
			Collection<ControlInterface> controlsCollection = container.getControlCollection();
			if(controlsCollection!=null)
			{
				Iterator<ControlInterface> controlsIterator = controlsCollection.iterator();
				while(controlsIterator.hasNext())
				{
					ControlInterface control = controlsIterator.next();
					if((control!=null)&&(control instanceof ContainmentAssociationControl))
					{
						xmlNodeForContainer.append(getNodeForContainer(((ContainmentAssociationControl)control).getContainer(),currentContainerName,addNewNode,showExpanded));
					}
				}
			}
			if((containerName!=null)&&(containerName.equals(currentContainerName)))
			{
				if(addNewNode)
				{
					getNewEntityNode();
				}
			}
			xmlNodeForContainer.append("</item>");
		}
		else
		{
			//Add new form node to main container node
			xmlNodeForContainer.append(getNewEntityNode());
		}
		return xmlNodeForContainer.toString();
	}

	/**
	 * 
	 */
	private String getNewEntityNode()
	{
		String xmlNodeForNewEntity = getXMLNode(null,"New Form",true,true);
		xmlNodeForNewEntity = xmlNodeForNewEntity  +"</item>";
		return xmlNodeForNewEntity;
	}

	/**
	 * @param text
	 */
	private String getXMLNode(String id, String text,boolean showSelected,boolean showExpanded)
	{
		StringBuffer xmlNode = new StringBuffer();
		if(text!=null)
		{
			xmlNode.append("<item");
			xmlNode.append(" text='" + text + "' ");
			if(id==null)	//if id is null put name as id.
			{
				xmlNode.append(" id='" + text + "' ");
			}
			else
			{
				xmlNode.append(" id='" + id + "' ");
			}
			if(showExpanded)
			{
				xmlNode.append(" open='1' ");
			}
			if(showSelected)
			{
				xmlNode.append(" select='1'");
			}
			xmlNode.append(">");
		}
		return xmlNode.toString();
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
			if (containerIdentifier != null)
			{
				container = loadFormDefinitionProcessor.getContainerForEditing(containerIdentifier);
			}
			else
			{
				container = WebUIManager.getCurrentContainer(request);
			}
			loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
		}
		else if (operationMode != null && operationMode.equalsIgnoreCase(Constants.ADD_SUB_FORM_OPR))
		{
			initializeSubFormAttributes(formDefinitionForm);
		}
		else
		{
			formDefinitionForm.setOperationMode("");
			//container = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
			container = WebUIManager.getCurrentContainer(request);
			if (container != null)
			{
				loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
			}
		}
	}

	/**
	 * @param container
	 * @param formDefinitionForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void initializeSubFormAttributes(FormDefinitionForm formDefinitionForm) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
	{
		formDefinitionForm.setDefinedEntitiesTreeXML(getXMLForDefinedEntities());
	}

	/**
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private String getXMLForDefinedEntities() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer definedEntitiesXML = new StringBuffer();
		EntityManagerInterface entityManager  = EntityManager.getInstance();
		Collection<AssociationTreeObject> associationsCollection =  entityManager.getAssociationTree();
		definedEntitiesXML.append("<?xml version='1.0' encoding='iso-8859-1'?> ");
		definedEntitiesXML.append("<tree id='0'>");
		//Special handling for grp : assign id as "Group_ number
		
		if(associationsCollection!=null)
		{
			Iterator<AssociationTreeObject> assocnIter = associationsCollection.iterator();
			while(assocnIter.hasNext())
			{
				AssociationTreeObject associationObj = assocnIter.next();
				if(associationObj!=null)
				{
					String label =  associationObj.getLabel();
					String id  = Constants.GROUP_PREFIX + associationObj.getId();  
					definedEntitiesXML.append(getXMLNode(id, label,false, false));
					definedEntitiesXML.append(getAssociationTreeXML(associationObj.getAssociationTreeObjectCollection()));
					definedEntitiesXML.append("</item>");
				}
			}
			definedEntitiesXML.append("</tree>");
		}
		
		
		return definedEntitiesXML.toString();
	}

	/**
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private String getAssociationTreeXML(Collection<AssociationTreeObject> associationsCollection) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer associationTreeXML = new StringBuffer();
		if(associationsCollection!=null)
		{
			AssociationTreeObject associationObj = null;
			Long id = null;
			String label = null;
			Iterator<AssociationTreeObject>  iterator = associationsCollection.iterator();
			while(iterator.hasNext())
			{
				associationObj = iterator.next();
				if(associationObj!=null)
				{
					id = associationObj.getId() ;
					label = associationObj.getLabel();
					if((id!=null)&&(label!=null))
					{
						associationTreeXML.append(getXMLNode(id+"", label , false, false));
						associationTreeXML.append(getAssociationTreeXML(associationObj.getAssociationTreeObjectCollection()));
						associationTreeXML.append("</item>");
					}
				}
			}
		}
		return associationTreeXML.toString();
	}

	/**
	 * @param container
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 *//*
	private TreeData getAssociationTree() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		TreeData associationTreeData = new TreeData();
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<AssociationTreeObject> associationTree = entityManager.getAssociationTree();
		if (associationTree != null)
		{
			AssociationTreeObject associationObj = null;
			Long id = null;
			String label = null;
			Iterator<AssociationTreeObject> iterator = associationTree.iterator();
			TreeNode tnode = null;
			while (iterator.hasNext())
			{
				associationObj = iterator.next();
				if (associationObj != null)
				{
					id = associationObj.getId();
					label = associationObj.getLabel();
					if ((id != null) && (label != null))
					{
						tnode = new TreeNode(label, id.intValue());
						tnode.setShowRadioBtn(false);//No radio btns for first level nodes
						addSubTree(tnode, associationObj.getAssociationTreeObjectCollection());
						associationTreeData.add(tnode);
					}
				}
			}
		}
		return associationTreeData;
	}

	  *//**
	  * @param tnode
	  * @param associationTreeObjectCollection
	  *//*
	private void addSubTree(TreeNode tnode, Collection<AssociationTreeObject> associationTreeObjectCollection)
	{
		if (associationTreeObjectCollection != null)
		{
			AssociationTreeObject associationObj = null;
			Iterator<AssociationTreeObject> iterator = associationTreeObjectCollection.iterator();
			TreeNode subnode = null;
			String label = null;
			Long id = null;
			while (iterator.hasNext())
			{
				associationObj = iterator.next();
				if (associationObj != null)
				{
					id = associationObj.getId();
					label = associationObj.getLabel();
					if ((id != null) && (label != null))
					{
						subnode = new TreeNode(label, id.intValue());
						subnode.setShowRadioBtn(true);//show radio btn for sub-nodes
						addSubTree(subnode, associationObj.getAssociationTreeObjectCollection());
						tnode.add(subnode);
					}
				}
			}
		}
	}
	   */
	/**
	 * @param request
	 * @return
	 */
	private String getGroupName(HttpServletRequest request)
	{
		String groupName = null;
		//Get group object from cache and return it
		EntityGroupInterface entityGroup = (EntityGroupInterface) CacheManager.getObjectFromCache(request, Constants.ENTITYGROUP_INTERFACE);
		if (entityGroup != null)
		{
			groupName = entityGroup.getName();
		}
		else
		{
			groupName = "";
		}
		return groupName;
	}

	/*	*//**
	 * @param container 
	 * @param request
	 *//*
	private TreeData getEntityTree(HttpServletRequest request, boolean addNewNode)
	{
		EntityGroupInterface entityGroup = (EntityGroupInterface) CacheManager.getObjectFromCache(request, Constants.ENTITYGROUP_INTERFACE);
		TreeData treedata = new TreeData();
		if (entityGroup != null)
		{
			ContainerInterface mainContainer = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);

			String currentContainerName = (String) CacheManager.getObjectFromCache(request, Constants.CURRENT_CONTAINER_NAME);
			if (mainContainer != null)
			{
				TreeNode mainContainerTreeNode = getTreeNode(mainContainer, currentContainerName, addNewNode);
				if ((currentContainerName == null) && (addNewNode == true))
				{
					//Add new form node to main container node
					mainContainerTreeNode.add(getNewFormNode());
				}
				treedata.add(mainContainerTreeNode);
			}
			else
			{
				//Add new node to group
				if (addNewNode == true)
				{
					treedata.add(getNewFormNode());
				}
			}
		}
		return treedata;
	}
	  */
	/**
	 * @param container
	 * @return
	 *//*
	private TreeNode getTreeNode(ContainerInterface container, String currentContainerName, boolean addNewNode)
	{
		TreeNode containerNode = null;
		if (container != null)
		{
			containerNode = new TreeNode(container.getCaption());

			if (container.getCaption().equals(currentContainerName))
			{
				if (addNewNode)
				{
					containerNode.add(getNewFormNode());
				}
			}
			Collection<ControlInterface> controlsCollection = container.getControlCollection();
			if (controlsCollection != null)
			{
				Iterator<ControlInterface> controlsIterator = controlsCollection.iterator();
				while (controlsIterator.hasNext())
				{
					ControlInterface control = controlsIterator.next();
					if ((control != null) && (control instanceof ContainmentAssociationControl))
					{
						containerNode.add(getTreeNode(((ContainmentAssociationControl) control).getContainer(), currentContainerName, addNewNode));
					}
				}
			}
		}
		return containerNode;
	}
	  */

}
