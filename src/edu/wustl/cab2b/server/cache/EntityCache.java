
package edu.wustl.cab2b.server.cache;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.beans.MatchedClassEntry;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.cache.CompareUtil;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * This class is used to cache the Entity and its Attribute objects.
 * 
 * @author Chandrakant Talele
 * @author gautam_shetty
 * @author Rahul Ner
 */
public class EntityCache extends AbstractEntityCache
{

	private static final long serialVersionUID = 1234567890L;

	private EntityCache()
	{
		super();
	}

	/**
	 * @return the singleton instance of the EntityCache class.
	 */
	public static synchronized EntityCache getInstance()
	{
		if (entityCache == null)
		{
			entityCache = new EntityCache();
		}
		return (EntityCache) entityCache;
	}

	/**
	 * @throws RemoteException 
	 * @see edu.wustl.cab2b.common.cache.AbstractEntityCache#getCab2bEntityGroups()
	 */
	protected Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException
	{
		return DynamicExtensionUtility.getCab2bEntityGroups();
	}

	/**
	 * Returns the Entity objects whose source classes fields match with the
	 * respective not null fields in the passed entity object.
	 * 
	 * @param patternEntityCollection The entity object.
	 * @return the Entity objects whose source classes fields match with the
	 *         respective not null fields in the passed entity object.
	 */
	public MatchedClass getCategories(Collection<EntityInterface> patternEntityCollection)
	{
		MatchedClass matchedClass = new MatchedClass();
		CategoryOperations categoryOperations = new CategoryOperations();
		for (Category category : categories)
		{
			Set<EntityInterface> classesInCategory = categoryOperations
					.getAllSourceClasses(category);

			for (EntityInterface classInCategory : classesInCategory)
			{
				for (EntityInterface patternEntity : patternEntityCollection)
				{
					MatchedClassEntry matchedClassEntry = CompareUtil.compare(classInCategory,
							patternEntity);
					if (matchedClassEntry != null)
					{
						long deEntityID = category.getDeEntityId();
						EntityInterface entityInterface = getEntityById(deEntityID);
						matchedClass.getEntityCollection().add(entityInterface);
						matchedClass.addMatchedClassEntry(matchedClassEntry);
					}
				}
			}
		}
		return matchedClass;
	}

	/**
	 * 
	 * Returns the Entity objects whose attributes's source classes fields match
	 * with the respective not null fields in the passed entity object.
	 * 
	 * @param patternAttributeCollection The entity object.
	 * @return the Entity objects whose attributes's source classes fields match
	 *         with the respective not null fields in the passed entity object.
	 */
	public MatchedClass getCategoriesAttributes(
			Collection<AttributeInterface> patternAttributeCollection)
	{
		MatchedClass matchedClass = new MatchedClass();

		CategoryOperations categoryOperations = new CategoryOperations();
		for (Category category : categories)
		{
			Set<AttributeInterface> attributesInCategory = categoryOperations
					.getAllSourceAttributes(category);
			for (AttributeInterface attributeInCategory : attributesInCategory)
			{
				for (AttributeInterface patternAttribute : patternAttributeCollection)
				{
					MatchedClassEntry matchedClassEntry = CompareUtil.compare(attributeInCategory,
							patternAttribute);
					if (matchedClassEntry != null)
					{

						long deEntityID = category.getDeEntityId();
						EntityInterface entityInterface = getEntityById(deEntityID);
						matchedClass.getEntityCollection().add(entityInterface);
						matchedClass.addMatchedClassEntry(matchedClassEntry);
					}
				}
			}
		}
		return matchedClass;
	}

	/**
	 * It will return all the categories present in the Database .
	 * @return Collection of the CategoryInterface in the database.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection<CategoryInterface> getAllCategories()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();
		return categoryManager.getAllCategories();
	}

	/**
	 * It will return the Category with the id as given identifier in the parameter.
	 * @param identifier.
	 * @return category with given identifier.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryInterface getCategoryById(Long identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (CategoryInterface) DynamicExtensionsUtility.getObjectByIdentifier(
				CategoryInterface.class.getName(), identifier.toString());
	}

	/**
	 * It will return the CategoryAttribute with the id as given identifier in the parameter.
	 * @param identifier
	 * @return categoryAttribute with given identifier
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryAttributeInterface getCategoryAttributeById(Long identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (CategoryAttributeInterface) DynamicExtensionsUtility.getObjectByIdentifier(
				CategoryAttributeInterface.class.getName(), identifier.toString());
	}

	/**
	 * It will return the CategoryAssociation with the id as given identifier in the parameter.
	 * @param identifier
	 * @return CategoryAssociation with given identifier
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryAssociationInterface getCategoryAssociationById(Long identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (CategoryAssociationInterface) DynamicExtensionsUtility.getObjectByIdentifier(
				CategoryAssociationInterface.class.getName(), identifier.toString());
	}

	/**
	 * It will return the CategoryEntity with the id as given identifier in the parameter.
	 * @param identifier
	 * @return categoryEntity with given identifier
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryEntityInterface getCategoryEntityById(Long identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (CategoryEntityInterface) DynamicExtensionsUtility.getObjectByIdentifier(
				CategoryEntityInterface.class.getName(), identifier.toString());
	}

	/**
	 * It will return the Container with the id as given identifier in the parameter.
	 * @param identifier
	 * @return Container with given identifier
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public ContainerInterface getContainerById(Long identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (ContainerInterface) DynamicExtensionsUtility.getObjectByIdentifier(
				ContainerInterface.class.getName(), identifier.toString());
	}

	/**
	 * It will return the Control with the id as given identifier in the parameter.
	 * @param identifier
	 * @return Control with given identifier
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public ControlInterface getControlById(Long identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (ControlInterface) DynamicExtensionsUtility.getObjectByIdentifier(
				ControlInterface.class.getName(), identifier.toString());
	}

}