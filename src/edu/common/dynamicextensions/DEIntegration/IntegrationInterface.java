/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.common.dynamicextensions.DEIntegration;

import java.sql.SQLException;
import java.util.Collection;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This interface is acting as an interface for host application to communicate with DE
 * @author shital_lawhale
 *
 */

public interface IntegrationInterface
{

	/**
	 * 
	 * @param hookEntityId
	 * @return the container Id of the DE entities that are associated with given static hook entity
	 */
	public Collection getDynamicEntitiesContainerIdFromHookEntity(Long hookEntityId)
			throws DynamicExtensionsSystemException;

	/**
	 * 
	 * @param hookEntityId
	 * @return  the container Id of the DE entities/categories that are associated with given static hook entity
	 * @throws DynamicExtensionsSystemException
	 */
	public Collection getCategoriesContainerIdFromHookEntity(Long hookEntityId)
			throws DynamicExtensionsSystemException;

	/**
	 * 
	 * @param hookEntityRecId
	 * @param containerId
	 * @param hookEntityId
	 * @return the category or form record id based on the containerId and hookentityRecId  
	 */
	public Collection getDynamicEntityRecordIdFromHookEntityRecordId(String hookEntityRecId,
			Long containerId, Long hookEntityId) throws DynamicExtensionsSystemException,
			SQLException;

	/**
	 * 
	 * @param categoryContainerId
	 * @param staticRecId
	 * @param hookEntityId
	 * @return the record id of the category depending on hook entity record id. 
	 */
	public Collection getCategoryRecIdBasedOnHookEntityRecId(Long categoryContainerId,
			Long staticRecId, Long hookEntityId) throws DynamicExtensionsSystemException,
			SQLException;

	/**
	 * This method associate the DE records with the given Static hook entity record id 
	 * @param containerId
	 * @param staticEntityRecordId
	 * @param dynamicEntityRecordId
	 * @param hookEntityId
	 */
	public void associateRecords(Long containerId, Long staticEntityRecordId,
			Long dynamicEntityRecordId, Long hookEntityId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException,
			BizLogicException, DAOException;

	/**
	 * This method associate the static hook entity with the Dynamic entity  
	 * @param hookEntityId
	 * @param dynamicEntityId
	 * @param isEntityFromXmi
	 * @param isCategory
	 * @return
	 */
	public Long addAssociation(Long hookEntityId, Long dynamicEntityId, boolean isEntityFromXmi,
			boolean isCategory) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, BizLogicException;

	/**
	 * 
	 * @param containerId
	 * @return whether this entity is simple DE form /category. 
	 */
	public boolean isCategory(Long containerId) throws DynamicExtensionsSystemException;

}
