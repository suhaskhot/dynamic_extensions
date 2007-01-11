/*
 * Created on Nov 16, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.GroupUIBeanInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

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
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface saveGroupDetails(GroupUIBeanInterface groupUIBean,ContainerInterface containerInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		GroupProcessor groupProcessor = GroupProcessor.getInstance(); 
		String groupOperation = groupUIBean.getGroupOperation();
		EntityGroupInterface entityGroup = null;

		//Use existing group
		if((groupUIBean.getCreateGroupAs()!=null)&&((groupUIBean.getCreateGroupAs().equals(ProcessorConstants.GROUP_CREATEFROM_EXISTING))))
		{
			Long entityGroupId = new Long(groupUIBean.getGroupName());
			//entityGroup = getEntityGroup(containerInterface,entityGroupId);
			//if (entityGroup == null) {
			entityGroup = groupProcessor.getEntityGroupByIdentifier(groupUIBean.getGroupName());
			//}
		}
		else
		{
			//Create new entity group
			//Validate entity group name
			DynamicExtensionsUtility.validateName(groupUIBean.getGroupNameText());
			entityGroup = groupProcessor.createEntityGroup();
			groupProcessor.populateEntityGroupDetails(entityGroup, groupUIBean);
		}
		//if group to be saved
		if((groupOperation!=null)&&(groupOperation.equals(ProcessorConstants.SAVE_GROUP)))
		{
			//Save to DB 
			entityGroup = groupProcessor.saveEntityGroup(entityGroup);
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
	public void updateEntityGroup(ContainerInterface container ,EntityGroupInterface entityGroup,GroupUIBeanInterface groupUIBean) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityInterface entity = null;

		if((entityGroup!=null)&&(container!=null))
		{
			entity = container.getEntity();
			if(entity!=null)
			{
				entity.removeAllEntityGroups();
				entity.addEntityGroupInterface(entityGroup);
			}

			//Save to DB
			String groupOperation = groupUIBean.getGroupOperation();
			if((groupOperation!=null)&&(groupOperation.equals(ProcessorConstants.SAVE_GROUP)))
			{
				//Save to DB
				EntityManager.getInstance().persistEntity(entity);
			}
		}
	}
}
