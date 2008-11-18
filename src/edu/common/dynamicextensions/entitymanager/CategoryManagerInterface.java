
package edu.common.dynamicextensions.entitymanager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * 
 * @author rajesh_patil
 * 
 */
public interface CategoryManagerInterface
{

	/**
	 * 
	 * @param category
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	CategoryInterface persistCategory(CategoryInterface category)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * 
	 * @param category
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	CategoryInterface persistCategoryMetadata(CategoryInterface category)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * 
	 * @param category
	 * @param dataValue
	 * @param userId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public Long insertData(CategoryInterface category,
			Map<BaseAbstractAttributeInterface, Object> dataValue, Long... userId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * 
	 * @param category
	 * @param dataValueMaps
	 * @param userId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public List<Long> insertData(CategoryInterface category,
			List<Map<BaseAbstractAttributeInterface, Object>> dataValueMaps, Long... userId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * @param rootCatEntity
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws SQLException
	 */
	public Map<BaseAbstractAttributeInterface, Object> getRecordById(
			CategoryEntityInterface rootCatEntity, Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			SQLException;

	/**
	 * @param catEntity
	 * @param attributeValues
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 */
	public boolean editData(CategoryEntityInterface catEntity,
			Map<BaseAbstractAttributeInterface, Object> attributeValues, Long recordId,
			Long... userId) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, SQLException;

	/**
	 * Check if the subset of permissible values passed is valid.
	 * @param userDefinedDE
	 * @param desiredPVs
	 * @return true or false depending on valid permissible values subset
	 */
	public boolean isPermissibleValuesSubsetValid(UserDefinedDEInterface userDefinedDE,
			Map<String, Collection<SemanticPropertyInterface>> desiredPVs);

}
