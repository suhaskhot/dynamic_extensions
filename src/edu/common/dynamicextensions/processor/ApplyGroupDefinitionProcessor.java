/*
 * Created on Nov 16, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.processor;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.GroupUIBeanInterface;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 */
public class ApplyGroupDefinitionProcessor extends BaseDynamicExtensionsProcessor
{

	/**
	 *
	 */
	private ApplyGroupDefinitionProcessor()
	{
	}

	/**
	 * 
	 * @return new instance of ApplyGroupDefinitionProcessor
	 */
	public static ApplyGroupDefinitionProcessor getInstance()
	{
		return new ApplyGroupDefinitionProcessor();
	}

	/**
	 * 
	 * @param groupUIBean
	 * @param operationMode 
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface saveGroupDetails(GroupUIBeanInterface groupUIBean, ContainerInterface container, String operationMode,
			HttpServletRequest request) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		GroupProcessor groupProcessor = GroupProcessor.getInstance();
		String groupOperation = groupUIBean.getGroupOperation();
		EntityGroupInterface entityGroup = null;

		//Use existing group
		String createGroupAs = groupUIBean.getCreateGroupAs();
		if ((createGroupAs != null) && (createGroupAs.equals(ProcessorConstants.GROUP_CREATEFROM_EXISTING)))
		{
			//entityGroup = groupProcessor.getEntityGroupByIdentifier(groupUIBean.getGroupName());
			if (container != null)
			{
				EntityInterface entity = (EntityInterface) container.getAbstractEntity();
				entityGroup = DynamicExtensionsUtility.getEntityGroup(entity);
				if (operationMode.equals(Constants.EDIT_FORM))
				{
					entityGroup.setDescription(groupUIBean.getGroupDescription());
				}
			}
			else
			{
				entityGroup = groupProcessor.getEntityGroupByIdentifier(groupUIBean.getGroupName());
				container = DomainObjectFactory.getInstance().createContainer();
				container.setCaption("New Form");
				CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, container);
				CacheManager.addObjectToCache(request, Constants.ENTITYGROUP_INTERFACE, entityGroup);
			}
		}
		else
		{
			//Create new entity group and validate entity group name
			DynamicExtensionsUtility.validateName(groupUIBean.getGroupNameText());
			entityGroup = groupProcessor.createEntityGroup();
			groupProcessor.populateEntityGroupDetails(entityGroup, groupUIBean);
		}

		if (createGroupAs.equals(ProcessorConstants.GROUP_CREATEAS_NEW))
		{
			//if group to be saved
			if ((groupOperation != null) && (groupOperation.equals(ProcessorConstants.SAVE_GROUP)))
			{
				//Save to DB 
				entityGroup = groupProcessor.saveEntityGroup(entityGroup);
			}
		}
		return entityGroup;
	}

	private EntityGroupInterface getEntityGroup(ContainerInterface containerInterface, Long entityGroupId)
	{
		//		Collection<EntityGroupInterface> entityGroupCollection = getAllEntityGroups(containerInterface);
		//		
		//		for(EntityGroupInterface entityGroupInterface : entityGroupCollection)
		//		{
		//			if (entityGroupInterface.getId().equals(entityGroupId))
		//			{
		//				return entityGroupInterface;
		//			}
		//		}
		return null;
	}

	/**
	 * @param groupForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public void updateEntityGroup(ContainerInterface container, EntityGroupInterface entityGroup, GroupUIBeanInterface groupUIBean)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityInterface entity = null;

		if ((entityGroup != null) && (container != null))
		{
			entity = (Entity) container.getAbstractEntity();
			if (entity != null)
			{
				//entity.removeAllEntityGroups();
				entity.setEntityGroup(entityGroup);
			}

			//Save to DB
			String groupOperation = groupUIBean.getGroupOperation();
			if ((groupOperation != null) && (groupOperation.equals(ProcessorConstants.SAVE_GROUP)))
			{
				//Save to DB
				EntityManager.getInstance().persistEntity(entity);
			}
		}
	}
}
