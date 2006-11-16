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
public class LoadGroupDefinitionProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 *
	 */
	public LoadGroupDefinitionProcessor()
	{
	}

	/**
	 * This method returns the instance of LoadGroupDefinitionProcessor.
	 * @return LoadGroupDefinitionProcessor Instance of LoadGroupDefinitionProcessor
	 */
	public static LoadGroupDefinitionProcessor getInstance()
	{
		return new LoadGroupDefinitionProcessor();
	}
	/**
	 * 
	 * @param entityGroup Object that contains information of  group
	 * @param groupForm : Form object
	 */
	public void loadGroupDetails(EntityGroupInterface entityGroup,GroupUIBeanInterface  groupUIBean)
	{
		if(groupUIBean!=null)
		{
			if(entityGroup!=null)
			{
				groupUIBean.setGroupName(entityGroup.getName());
				groupUIBean.setGroupDescription(entityGroup.getDescription());
			}
			else
			{
				groupUIBean.setCreateGroupAs(ProcessorConstants.DEFAULT_GROUP_CREATEAS);
			}
		}
	}
}