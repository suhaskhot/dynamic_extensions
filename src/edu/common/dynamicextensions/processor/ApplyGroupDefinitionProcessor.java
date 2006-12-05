/*
 * Created on Nov 16, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.GroupUIBeanInterface;

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
	public EntityGroupInterface saveGroupDetails(GroupUIBeanInterface groupUIBean) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		GroupProcessor groupProcessor = GroupProcessor.getInstance(); 
		String groupOperation = groupUIBean.getGroupOperation();
		EntityGroupInterface entityGroup = null;

		//Use existing group
		if((groupUIBean.getCreateGroupAs()!=null)&&((groupUIBean.getCreateGroupAs().equals(ProcessorConstants.GROUP_CREATEFROM_EXISTING))))
		{
			entityGroup = groupProcessor.getEntityGroupByIdentifier(groupUIBean.getGroupName());
		}
		else
		{
			//Create new entity group 
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
}
