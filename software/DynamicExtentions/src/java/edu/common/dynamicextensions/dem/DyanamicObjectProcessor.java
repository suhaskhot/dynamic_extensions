package edu.common.dynamicextensions.dem;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.AbstractBaseMetadataManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.DEConstants.Cardinality;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.metadata.util.DyExtnObjectCloner;

public class DyanamicObjectProcessor extends AbstractBaseMetadataManager{

	/**
	 * Used for cloning object
	 */
	private final DyExtnObjectCloner cloner = new DyExtnObjectCloner();
	private final HibernateDAO hibernateDAO;

	public DyanamicObjectProcessor() throws DAOException
	{
		hibernateDAO = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory("dem")
		.getDAO();
		hibernateDAO.openSession(null);
	}
	/**
	 * This constructs a new object from the data in the map.
	 * @param entity entity
	 * @param dataValue data value map.
	 * @param hibernateDAO hibernate dos
	 * @return object created.
	 * @throws DynamicExtensionsApplicationException exception.
	 */
	public  Object createObject(EntityInterface entity,
			Map<AbstractAttributeInterface, Object> dataValue)
			throws DynamicExtensionsApplicationException
	{
		String packageName = null;
		packageName = getPackageName(entity, packageName);

		Object newObject = null;

		try
		{
			// If empty, insert row with only identifier column value.
			if (dataValue == null)
			{
				dataValue = new HashMap();
			}

			// Create a new instance.
			String className = packageName + "." + entity.getName();
			Class newObjectClass = Class.forName(className);
			newObject = createObjectForClass(className);

			// If empty, insert row with only identifier column value.
			for (Entry<AbstractAttributeInterface, Object> dataValueEntry : dataValue.entrySet())
			{
				AbstractAttribute attribute = (AbstractAttribute) dataValueEntry.getKey();

				if (attribute instanceof AssociationInterface)
				{
					AssociationInterface association = (AssociationInterface) attribute;

					EntityInterface baseEntity = association.getEntity();
					String baseEntClassName = packageName + "." + baseEntity.getName();
					EntityInterface targetEntity = association.getTargetEntity();

					Object value = dataValueEntry.getValue();

					List<Map> listOfMapsForContainedEntity = (List) value;
					for (Map valueMapForContainedEntity : listOfMapsForContainedEntity)
					{
						Object associatedObject = createObject(targetEntity,
								valueMapForContainedEntity);
						addSourceObject(newObject, associatedObject, baseEntClassName, association);
						addTargetObject(newObject, associatedObject, associatedObject.getClass()
								.getName(), association);

					}
				}
				else
				{
					String dataType = ((AttributeMetadataInterface) attribute)
							.getAttributeTypeInformation().getDataType();

					newObject = setObjectProperty(attribute, dataType, newObjectClass, dataValue,
							newObject);
				}
			}
/*			hibernateDAO.insert(newObject);*/
			Long identifier = getObjectId(newObject);
			dataValue.put(new edu.common.dynamicextensions.domain.EntityRecord(), identifier);
		}
		catch (ClassNotFoundException exception)
		{
			throw new DynamicExtensionsApplicationException(
					"Exception encountered during data entry!", exception);
		}
		catch (Exception exception)
		{
			throw new DynamicExtensionsApplicationException(
					"Exception encountered during data entry!", exception);
		}

		// Add the object to the set of auditable DE objects, for auditing.
		//auditableDEObjects.add(newObject);

		return newObject;
	}

	/**
	 * This updates the existing object with data in the map.
	 * @param entity entity
	 * @param dataValue data value map
	 * @param oldObject old object which is to be updated.
	 * @param hibernateDAO hibernate dao.
	 * @return updated object
	 * @throws DynamicExtensionsApplicationException exception.
	 */
	private Object updateObject(EntityInterface entity,
			Map<AbstractAttributeInterface, Object> dataValue, Object oldObject) throws DynamicExtensionsApplicationException
	{
		String packageName = null;
		packageName = getPackageName(entity, packageName);
		Object clonedObject = cloner.clone(oldObject);
		try
		{
			Class oldObjectClass = oldObject.getClass();
			// If empty, insert row with only identifier column value.
			if (dataValue == null)
			{
				dataValue = new HashMap();
			}

			for (Entry<AbstractAttributeInterface, Object> dataValueEntry : dataValue.entrySet())
			{
				AbstractAttribute attribute = (AbstractAttribute) dataValueEntry.getKey();

				if (attribute instanceof AssociationInterface)
				{
					AssociationInterface association = (AssociationInterface) attribute;

					EntityInterface baseEntity = association.getEntity();
					String baseEntityClassName = packageName + "." + baseEntity.getName();

					EntityInterface targetEntity = association.getTargetEntity();
					Cardinality targetMaxCardinality = association.getTargetRole()
							.getMaximumCardinality();

					String targetRole = getTargetRoleNameForMethodInvocation(association);

					Collection<Object> containedObjects = null;

					// Get the associated object(s).
					Object associatedObjects = invokeGetterMethod(oldObjectClass, targetRole,
							oldObject);

					if (targetMaxCardinality != Cardinality.ONE)
					{
						if (associatedObjects == null)
						{
							containedObjects = new HashSet<Object>();
						}
						else
						{
							containedObjects = (Collection) associatedObjects;
						}
					}

					Set<Object> objectsToBeRetained = new HashSet<Object>();

					Object value = dataValueEntry.getValue();
					List<Map> listOfMapsForContainedEntity = (List) value;

					for (Map valueMapForContainedEntity : listOfMapsForContainedEntity)
					{
						boolean isNew = false;
						Object objForUpdate = null;

						if (targetMaxCardinality == Cardinality.ONE)
						{
							objForUpdate = associatedObjects;
						}
						else
						{
							edu.common.dynamicextensions.domain.EntityRecord entityRecord = new edu.common.dynamicextensions.domain.EntityRecord();
							Long recordId = (Long) valueMapForContainedEntity.get(entityRecord);

							if (recordId != null)
							{
								for (Object obj : containedObjects)
								{
									Long identifier = getObjectId(obj);
									if (identifier.intValue() == recordId.intValue())
									{
										objForUpdate = obj;
										objectsToBeRetained.add(objForUpdate);

										break;
									}
								}
							}
						}

						if (objForUpdate == null)
						{
							String targetEntityClassName = packageName + "."
									+ targetEntity.getName();
							objForUpdate = createObjectForClass(targetEntityClassName);
							isNew = true;
						}

						objForUpdate = updateObject(targetEntity, valueMapForContainedEntity,
								objForUpdate);
						Class assoObjectClass = objForUpdate.getClass();
						String source = getSourceRoleNameForMethodInvocation(association);
						invokeSetterMethod(assoObjectClass, source, Class
								.forName(baseEntityClassName), objForUpdate, oldObject);

						if (targetMaxCardinality == Cardinality.ONE)
						{
							invokeSetterMethod(oldObjectClass, targetRole, objForUpdate.getClass(),
									oldObject, objForUpdate);
							break;
						}
						else
						{
							if (isNew)
							{
								containedObjects.add(objForUpdate);
								objectsToBeRetained.add(objForUpdate);
							}
						}
					}

					if (containedObjects != null)
					{
						List objectsToBeRemoved = new ArrayList();
						Iterator iterator = containedObjects.iterator();
						while (iterator.hasNext())
						{
							objectsToBeRemoved.add(iterator.next());
						}

						containedObjects.removeAll(objectsToBeRemoved);
						containedObjects.addAll(objectsToBeRetained);
					}

					if (targetMaxCardinality == Cardinality.MANY)
					{
						invokeSetterMethod(oldObjectClass, targetRole, Class
								.forName("java.util.Collection"), oldObject, containedObjects);
					}
				}
				else if (attribute instanceof AttributeInterface)
				{
					String dataType = ((AttributeMetadataInterface) attribute)
							.getAttributeTypeInformation().getDataType();
					oldObject = setObjectProperty(attribute, dataType, oldObjectClass, dataValue,
							oldObject);
				}
			}
			Long identifier = getObjectId(oldObject);
			if (identifier == null)
			{
				hibernateDAO.insert(oldObject);
			}
			else
			{
				hibernateDAO.update(oldObject, clonedObject);
			}
			hibernateDAO.commit();
			identifier = getObjectId(oldObject);
			dataValue.put(new edu.common.dynamicextensions.domain.EntityRecord(), identifier);
		}
		catch (Exception exception)
		{

			try {
				hibernateDAO.rollback();
			} catch (DAOException e) {
				throw new DynamicExtensionsApplicationException(
						"Exception encountered during editing data!", e);
			}
			throw new DynamicExtensionsApplicationException(
					"Exception encountered during editing data!", exception);
		}finally
		{
			try {
				hibernateDAO.closeSession();
			} catch (DAOException e) {
				throw new DynamicExtensionsApplicationException(
						"Exception encountered during editing data!", e);
			}
		}

		return oldObject;
	}

	@Override
	protected void logFatalError(Exception exception,
			AbstractMetadataInterface abstrMetadata) {
		// TODO Auto-generated method stub

	}
	public void updateStaticEntityObject(Object staticEntity,
			Object dynamicEntity, Association association)
			throws DynamicExtensionsApplicationException {
		Set<Object> containedObjects;
		try {
			containedObjects = (Set<Object>) invokeGetterMethod(staticEntity
					.getClass(), association.getTargetEntity().getName()
					+ "Collection", staticEntity);
			containedObjects.add(dynamicEntity);
			invokeSetterMethod(staticEntity.getClass(), association
					.getTargetEntity().getName()
					+ "Collection", Class.forName("java.util.Collection"),
					staticEntity, containedObjects);

		} catch (NoSuchMethodException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating static entity", e);
		} catch (IllegalAccessException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating static entity", e);
		} catch (InvocationTargetException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating static entity", e);
		} catch (ClassNotFoundException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating static entity", e);
		}

	}

	public void updateDynamicEntityObject(Object staticEntity,
			Object dynamicEntity, Association association)
			throws DynamicExtensionsApplicationException {
		try {
			invokeSetterMethod(dynamicEntity.getClass(),
					association.getEntity().getName()
							.substring(
									association.getEntity().getName()
											.lastIndexOf(".") + 1),
					staticEntity.getClass(), dynamicEntity, staticEntity);
			System.out.println();
		} catch (NoSuchMethodException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating dynamic entity", e);
		} catch (IllegalAccessException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating dynamic entity", e);
		} catch (InvocationTargetException e) {
			throw new DynamicExtensionsApplicationException(
					"Error populating dynamic entity", e);
		}

	}
	public void associateObjects(Map<String, Object> paramaterObjectMap) throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException {
		Long staticEntityId = (Long) paramaterObjectMap.get(WebUIManagerConstants.STATIC_OBJECT_ID);
		Long dynamicEntityId = (Long) paramaterObjectMap.get(WebUIManagerConstants.DYNAMIC_OBJECT_ID);
		Association association = (Association) paramaterObjectMap.get(WebUIManagerConstants.ASSOCIATION);

		String tmpPackageName = (String) paramaterObjectMap.get(WebUIManagerConstants.PACKAGE_NAME);
		HibernateDAO hibernateDAO = getHibernateDAO();
		try {
			Object staticEntity = hibernateDAO.retrieveById(tmpPackageName
					+ "."
					+ association.getEntity().getName().substring(
							association.getEntity().getName().lastIndexOf(".") + 1), staticEntityId);
			Object oldStaticEntity = cloner.clone(staticEntity);

			Object dynamicEntity = hibernateDAO.retrieveById(tmpPackageName + "."
					+ association.getTargetEntity().getName(), dynamicEntityId);
			Object oldDynamicEntity = cloner.clone(dynamicEntity);


			updateStaticEntityObject(staticEntity,dynamicEntity,association);
			updateDynamicEntityObject(staticEntity,dynamicEntity,association);

			hibernateDAO.update(dynamicEntity, oldDynamicEntity);
			hibernateDAO.update(staticEntity, oldStaticEntity);
			hibernateDAO.commit();

		} catch (DAOException e) {
			throw new DynamicExtensionsSystemException(
					"Error in associating objects", e);
		}finally
		{
			try {
				hibernateDAO.closeSession();
			} catch (DAOException e) {
				throw new DynamicExtensionsSystemException(
						"Error in associating objects", e);
			}
		}
	}

	public static HibernateDAO getHibernateDAO() throws DynamicExtensionsSystemException {
		HibernateDAO hibernateDao = null;
		try
		{
			hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory("dem")
					.getDAO();
			hibernateDao.openSession(null);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(
					"Error occured while opening the DAO session", e);
		}
		return hibernateDao;
	}
	public void associateRecord(Map<String, Object> paramaterObjectMap) throws DynamicExtensionsSystemException, DAOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
	{
		HibernateDAO hibernateDAO =null;
		try
		{
			hibernateDAO = getHibernateDAO();
			Long srcEntityId = (Long) paramaterObjectMap.get(WebUIManagerConstants.STATIC_OBJECT_ID);
			Long entityId = (Long) paramaterObjectMap.get(WebUIManagerConstants.DYNAMIC_OBJECT_ID);
			AssociationInterface lastAsso = (AssociationInterface) paramaterObjectMap.get(WebUIManagerConstants.ASSOCIATION);

			String packageName = (String) paramaterObjectMap.get(WebUIManagerConstants.PACKAGE_NAME);
			final String sourceObjectClassName = packageName + "."+ lastAsso.getEntity().getName();
			final String targetObjectClassName = packageName + "."+ lastAsso.getTargetEntity().getName();
			final Object sourceObject = hibernateDAO.retrieveById(sourceObjectClassName, srcEntityId);
			final Object targetObject = hibernateDAO.retrieveById(targetObjectClassName, entityId);
			final DyExtnObjectCloner cloner = new DyExtnObjectCloner();
			Object clonedTarget = cloner.clone(targetObject);
			addSourceObject(sourceObject, targetObject, sourceObjectClassName, lastAsso);
			hibernateDAO.update(targetObject, clonedTarget);
		} catch (DAOException e) {
			throw new DynamicExtensionsSystemException(
					"Error in associating objects", e);
		}finally
		{
			try {
				hibernateDAO.closeSession();
			} catch (DAOException e) {
				throw new DynamicExtensionsSystemException(
						"Error in associating records.", e);
			}
		}
	}
	public Object insertRecordsForCategoryEntityTree(Map<String, Object> paramaterObjectMap) throws DynamicExtensionsSystemException, DAOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SecurityException, IllegalArgumentException, InstantiationException
	{
		HibernateDAO hibernateDAO=null;
		try
		{
			hibernateDAO = getHibernateDAO();
			Long srcEntityId = (Long) paramaterObjectMap.get(WebUIManagerConstants.STATIC_OBJECT_ID);

			AssociationInterface lastAsso = (AssociationInterface) paramaterObjectMap.get(WebUIManagerConstants.ASSOCIATION);

			String packageName = (String) paramaterObjectMap.get(WebUIManagerConstants.PACKAGE_NAME);
			final String sourceObjectClassName = packageName + "."+ lastAsso.getEntity().getName();
			final String targetObjectClassName = packageName + "."+ lastAsso.getTargetEntity().getName();
			final Object sourceObject = hibernateDAO.retrieveById(sourceObjectClassName, srcEntityId);
			Object clonedSourceObject = cloner.clone(sourceObject);
			Object targetObject = createObjectForClass(targetObjectClassName);

			hibernateDAO.insert(targetObject);
			Object clonedTargetObject = cloner.clone(targetObject);
			addTargetObject(sourceObject, targetObject, targetObjectClassName,lastAsso);

			hibernateDAO.update(sourceObject, clonedSourceObject);
			addSourceObject(sourceObject, targetObject, sourceObjectClassName,lastAsso);

			hibernateDAO.update(targetObject, clonedTargetObject);
			return targetObject;
			/*final Object sourceObject = hibernateDAO.retrieveById(sourceObjectClassName, srcEntityId);
			final Object targetObject = hibernateDAO.retrieveById(targetObjectClassName, entityId);
			final DyExtnObjectCloner cloner = new DyExtnObjectCloner();
			Object clonedTarget = cloner.clone(targetObject);
			addSourceObject(sourceObject, targetObject, sourceObjectClassName, lastAsso);
			hibernateDAO.update(targetObject, clonedTarget);*/
		} catch (DAOException e) {
			throw new DynamicExtensionsSystemException(
					"Error in associating objects", e);
		}finally
		{
			try {
				hibernateDAO.closeSession();
			} catch (DAOException e) {
				throw new DynamicExtensionsSystemException(
						"Error in associating records.", e);
			}
		}
	}
	public  Object editObject(EntityInterface entity,
			Map<AbstractAttributeInterface, Object> dataValue,Long recordId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		HibernateDAO hibernateDAO =null;
		Object updatedObject =null;
		try
		{
			hibernateDAO = getHibernateDAO();

			String packageName = null;
			packageName = getPackageName(entity, packageName);
			String className = packageName + "." + entity.getName();
			Object oldObject;
			oldObject = hibernateDAO.retrieveById(className, recordId);

			 updatedObject = updateObject(entity, dataValue, oldObject);
		}
		catch (DAOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (DynamicExtensionsSystemException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				hibernateDAO.closeSession();
			} catch (DAOException e) {
				throw new DynamicExtensionsSystemException(
						"Error in associating records.", e);
			}
		}
		return updatedObject;
	}
}
