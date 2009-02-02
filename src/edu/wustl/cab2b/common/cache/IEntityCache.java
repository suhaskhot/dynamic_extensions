
package edu.wustl.cab2b.common.cache;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;

/**
 * @author gautam_shetty
 */
public interface IEntityCache
{

	/**
	 * Refreshes the entity cache.
	 */
	void refreshCache();

	/**
	 * Returns the Entity objects whose fields match with the respective not null 
	 * fields in the passed entity object.
	 * @param entity The entity object.
	 * @return the Entity objects whose fields match with the respective not null 
	 * fields in the passed entity object.
	 */
	MatchedClass getEntityOnEntityParameters(Collection<EntityInterface> entityCollection);

	/**
	 * Returns the Entity objects whose Attribute fields match with the respective not null 
	 * fields in the passed Attribute object.
	 * @param entity The entity object.
	 * @return the Entity objects whose Attribute fields match with the respective not null 
	 * fields in the passed Attribute object.
	 */
	MatchedClass getEntityOnAttributeParameters(
			Collection<AttributeInterface> attributeCollection);

	/**
	 * Returns the Entity objects whose Permissible Value fields match with the respective not null 
	 * fields in the passed Attribute object.
	 * @param entity The entity object.
	 * @return the Entity objects whose Permissible Value fields match with the respective not null 
	 * fields in the passed Permissible Value object.
	 */
	MatchedClass getEntityOnPermissibleValueParameters(
			Collection<PermissibleValueInterface> PVCollection);

	/**
	* Returns the Entity objects whose source classes fields match with the respective not null 
	* fields in the passed entity object.
	* @param entity The entity object.
	* @return the Entity objects whose source classes fields match with the respective not null 
	* fields in the passed entity object.
	*/
	MatchedClass getCategories(Collection<EntityInterface> entityCollection);

	/**
	 * Returns the Entity objects whose attributes's source classes fields match with the respective not null 
	 * fields in the passed entity object.
	 * @param entity The entity object.
	 * @return the Entity objects whose attributes's source classes fields match with the respective not null 
	 * fields in the passed entity object.
	 */
	MatchedClass getCategoriesAttributes(Collection<AttributeInterface> attributeCollection);

	/**
	 * This method adds entity and its other details like associaion and permissible values into the cache.
	 * 
	 * @param entity entity to add
	 */
	void addEntityToCache(EntityInterface entity);
}