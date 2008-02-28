
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.AssociationTreeObject;
import edu.wustl.common.beans.NameValueBean;

/**
*
* @author rajesh_patil
*
*/
public interface EntityGroupManagerInterface
{
	/**
	 * This method persists an entity group and the associated entities and also creates the data table
	 * for the entities.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface persistEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method persists an entity group and the associated entities without creating the data table
	 * for the entities.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityGroupInterface persistEntityGroupMetadata(EntityGroupInterface entityGroupInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method returns the EntityGroupInterface given the short name for the
	 * entity.
	 * @param entityGroupShortName short name for entity group
	 * @return entityGroupInterface entity group interface
	 * @throws DynamicExtensionsSystemException
	 */
	EntityGroupInterface getEntityGroupByShortName(String entityGroupShortName) throws DynamicExtensionsSystemException;

	/**
	 * @param entityGroupName
	 * @return EntityGroupInterface EntityGroupInterface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityGroupInterface getEntityGroupByName(String entityGroupName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * @param entityGroupIdentifier
	 * @return
	 */
	Collection<NameValueBean> getMainContainer(Long entityGroupIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * getAllEntityGroupBeans.
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	Collection<NameValueBean> getAllEntityGroupBeans() throws DynamicExtensionsSystemException;

	/**
	*
	* @param entityGroupInterface
	* @return
	* @throws DynamicExtensionsApplicationException
	*/
	Collection<AssociationTreeObject> getAssociationTree() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * validateEntityGroup.
	 * @param entityGroup
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	boolean validateEntityGroup(EntityGroupInterface entityGroup) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * This method checks if the entity group can be created with the given name or not.
	 * @param entityGroup
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public void checkForDuplicateEntityGroupName(EntityGroupInterface entityGroup) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException;

}
