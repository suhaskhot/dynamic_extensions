/*
 * Created on Nov 16, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.ui.interfaces.GroupUIBeanInterface;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GroupProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * 
	 *
	 */
	private GroupProcessor()
	{
		
	}
	/**
	 * 
	 * @return new instance of GroupProcessor
	 */
	public static GroupProcessor getInstance()
	{
		return new GroupProcessor();
	}
	/**
	 * 
	 * @return new object of entity group 
	 */
	public EntityGroupInterface createEntityGroup()
	{
		return DomainObjectFactory.getInstance().createEntityGroup();
	}
	/**
	 * 
	 * @param entityGroup : Entity Group object to be populated
	 * @param groupUIBean : Group UI Bean containing information entered by the user
	 */
	public void populateEntityGroupDetails(EntityGroupInterface entityGroup,GroupUIBeanInterface groupUIBean)
	{
		if((entityGroup!=null)&&(groupUIBean!=null))
		{
			entityGroup.setName(groupUIBean.getGroupName());
			entityGroup.setDescription(groupUIBean.getGroupDescription());
		}
	}
	/**
	 * 
	 * @param groupUIBean : Group UI Bean object to be populated
	 * @param entityGroup : Entity Group object containing group details
	 */
	public void populategroupUIBeanDetails(GroupUIBeanInterface groupUIBean,EntityGroupInterface entityGroup)
	{
		if((entityGroup!=null)&&(groupUIBean!=null))
		{
			groupUIBean.setGroupName(entityGroup.getName());
			groupUIBean.setGroupDescription(entityGroup.getDescription());
		}
	}
	/**
	 * Create entity group object, populate its details and save to DB
	 * @param groupUIBean  : Bean containing group information added by user on UI
	 * @return
	 */
	public EntityGroupInterface createAndSaveGroup(GroupUIBeanInterface groupUIBean)
	{
		EntityGroupInterface entityGroup = createEntityGroup();
		populateEntityGroupDetails(entityGroup, groupUIBean);
		entityGroup = saveEntityGroup(entityGroup);
		return entityGroup;
	}
	/**
	 * 
	 * @param entityGroup Entity group to be saved to the DB
	 * @return
	 */
	public EntityGroupInterface saveEntityGroup(EntityGroupInterface entityGroup)
	{
		//Entity manager method will come here
		//EntityManager.getInstance().
		return entityGroup;
	}
	/**
	 * 
	 * @param groupName Name of the group
	 * @return EntityGroup with given name
	 */
	public EntityGroupInterface getEntityGroupByName(String groupName)
	{
		//Method will return entity group object for given group name
		return  null;
	}
}
