
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
	 * @param categoryInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	CategoryInterface persistCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * 
	 * @param categoryInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	CategoryInterface persistCategoryMetadata(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * 
	 * @param category
	 * @param dataValue
	 * @param userId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public Long insertData(CategoryInterface category, Map<BaseAbstractAttributeInterface, Object> dataValue, Long... userId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * 
	 * @param category
	 * @param dataValueMapList
	 * @param userId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public List<Long> insertData(CategoryInterface category, List<Map<BaseAbstractAttributeInterface, Object>> dataValueMapList, Long...userId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * @param rootCategoryEntity
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws SQLException
	 */
	public Map<BaseAbstractAttributeInterface, Object> getRecordById(CategoryEntityInterface rootCategoryEntity,
			Long recordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException;
	
	/**
	 * @param categoryEntity
	 * @param attributeValueMap
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 */
	public boolean editData(CategoryEntityInterface categoryEntity, Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordId, Long... userId)
	throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException, SQLException;
	
	/**
	 * Check if the subset of permissible values passed is valid.
	 * @param userDefinedDE
	 * @param desiredPermissibleValues
	 * @return true or false depending on valid permissible values subset
	 */
	public boolean isPermissibleValuesSubsetValid(UserDefinedDEInterface userDefinedDE, Map<String,Collection<SemanticPropertyInterface>> desiredPermissibleValues);
	
	

}
