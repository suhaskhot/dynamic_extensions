/*
 * Created on Nov 16, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.ui.interfaces.GroupUIBeanInterface;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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

	public EntityGroupInterface saveGroupDetails(GroupUIBeanInterface groupUIBean)
	{
		GroupProcessor groupProcessor = GroupProcessor.getInstance(); 
		String groupOperation = groupUIBean.getGroupOperation();
		EntityGroupInterface entityGroup = null;

		//Use existing group
		if((groupUIBean.getCreateGroupAs()!=null)&&((groupUIBean.getCreateGroupAs().equals(ProcessorConstants.GROUP_CREATEFROM_EXISTING))))
		{
			entityGroup = groupProcessor.getEntityGroupByName(groupUIBean.getGroupName());
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
