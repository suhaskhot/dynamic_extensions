/*
 * Created on Nov 16, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.GroupUIBeanInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadGroupDefinitionProcessor extends BaseDynamicExtensionsProcessor
{

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
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public void loadGroupDetails(EntityGroupInterface entityGroup, GroupUIBeanInterface groupUIBean,Long staticEntityId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (groupUIBean != null)
		{
			if (entityGroup != null && entityGroup.getId() == null)
			{
				groupUIBean.setGroupNameText(entityGroup.getName());
				groupUIBean.setGroupDescription(entityGroup.getDescription());
			}
			else if (entityGroup != null && entityGroup.getId() != null)
			{
				groupUIBean.setCreateGroupAs(ProcessorConstants.GROUP_CREATEFROM_EXISTING);
				groupUIBean.setGroupName(entityGroup.getId().toString());
				groupUIBean.setGroupDescription(entityGroup.getDescription());
			}
			else
			{
				groupUIBean.setCreateGroupAs(ProcessorConstants.DEFAULT_GROUP_CREATEAS);
			}
			groupUIBean.setGroupList(populateGroupList(staticEntityId));
		}
	}

	/**
	 *
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public List populateGroupList(Long staticEntityId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		
		EntityCache entityCache=EntityCache.getInstance();
		EntityInterface hookEntity=entityCache.getEntityById(staticEntityId);
		Collection<NameValueBean> entityGroups=entityCache.getEntityGroupsByEntity(hookEntity);
		List<NameValueBean> groupList = new ArrayList<NameValueBean>(edu.wustl.cab2b.common.util.Utility.convertGroupNameForDisplay(entityGroups));
		DynamicExtensionsUtility.sortNameValueBeanListByName(groupList);
		return groupList;
	}
}