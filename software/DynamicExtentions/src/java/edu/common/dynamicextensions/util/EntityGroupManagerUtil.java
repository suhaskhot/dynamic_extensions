package edu.common.dynamicextensions.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.QueryBuilderFactory;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationDirection;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationType;
import edu.common.dynamicextensions.util.global.DEConstants.Cardinality;
import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.common.dynamicextensions.xmi.DynamicQueryList;
import edu.common.dynamicextensions.xmi.XMIConstants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class EntityGroupManagerUtil {

	private static final Logger LOGGER = Logger.getCommonLogger(EntityGroupManagerUtil.class);

	public static Set<Long> getAssociatedFormId(EntityGroupInterface entityGroup)
			throws DynamicExtensionsSystemException {
		Set<Long> formIdSet = new HashSet<Long>();
		for (ContainerInterface container : entityGroup
				.getMainContainerCollection()) {
			formIdSet.add(container.getId());
			Collection<Long> categoryContainerId = getCategoryContainerId(container
					.getAbstractEntity().getId());
			if (!categoryContainerId.isEmpty()) {
				formIdSet.addAll(categoryContainerId);
			}
		}
		return formIdSet;
	}

	private static Collection<Long> getCategoryContainerId(Long entityId)
			throws DynamicExtensionsSystemException {
		String query = "select cont.identifier from dyextn_category cat join dyextn_category_entity catEnt" +
		" on cat.root_category_element = catEnt.identifier join dyextn_container cont" +
		" on cont.abstract_entity_id = catEnt.identifier and" +
		" catEnt.ENTITY_ID = ?";
		final LinkedList<ColumnValueBean> queryDataList = new LinkedList<ColumnValueBean>();
		queryDataList.add(new ColumnValueBean("catEnt.ENTITY_ID", entityId));
		ResultSet resultSet = null;
		JDBCDAO jdbcDAO = null;
		Collection<Long> catContainerId = new ArrayList<Long>();
		try
		{
			jdbcDAO = DynamicExtensionsUtility.getJDBCDAO();
			resultSet = jdbcDAO.getResultSet(query, queryDataList, null);
			while (resultSet.next())
			{
				catContainerId.add(resultSet.getLong(1));
			}
		}
		catch (final DAOException e)
		{
			throw new DynamicExtensionsSystemException("Exception in query execution", e);
		}
		catch (final SQLException e)
		{
			throw new DynamicExtensionsSystemException("Exception in execution query :: " + query
					+ " identifier :: " + queryDataList.get(0).getColumnValue(), e);
		}
		finally
		{
			try
			{
				jdbcDAO.closeStatement(resultSet);
				DynamicExtensionsUtility.closeDAO(jdbcDAO);
			}
			catch (final DAOException e)
			{
				throw new DynamicExtensionsSystemException("Exception in query execution", e);
			}
		}

		return catContainerId;
	}
	
	/**
	 * It will hook all the mainContainers with the Provided Hook entity.
	 * @param hibernatedao dao used for retrieving the hook entity.
	 * @param dynamicQueryList query list in which to add the Queries for adding the association column
	 * @param mainContainerList main containers which should be hooked.
	 * @param isEditedXmi is it the edit xmi case.
	 * @throws DAOException exception
	 * @throws DynamicExtensionsSystemException exception
	 * @throws BizLogicException exception
	 * @throws DynamicExtensionsApplicationException exception.
	 */
	public static void integrateWithHookEntity(String hookEntityName, List<ContainerInterface> mainContainerList,
			boolean isEditedXmi,DynamicQueryList dynamicQueryList, final Set<AssociationInterface> intermodelAssociationCollection) throws DAOException, DynamicExtensionsSystemException,
			BizLogicException, DynamicExtensionsApplicationException
	{
		if (!hookEntityName.equalsIgnoreCase("None"))
		{
			// For CLINPORTAL, there is only one hook entity object i.e. RECORD ENTRY
			LOGGER.info("################################################################################ ");
			LOGGER.info("Now associating with hook entity -> " + hookEntityName + "....");
			LOGGER.info("################################################################################ ");
			DynamicQueryList queryList = associateHookEntity(mainContainerList, isEditedXmi,
					hookEntityName,intermodelAssociationCollection);
			if (queryList != null)
			{
				dynamicQueryList.getQueryList().addAll(queryList.getQueryList());
				dynamicQueryList.getRevQueryList().addAll(queryList.getRevQueryList());
			}
		}
	}
	
	private static DynamicQueryList associateHookEntity(List<ContainerInterface> mainContainerList,
			boolean isEditedXmi, String hookEntityName,final Set<AssociationInterface> intermodelAssociationCollection) throws DAOException,
			DynamicExtensionsSystemException, BizLogicException,
			DynamicExtensionsApplicationException
	{
		//hooked with the record Entry
		DynamicQueryList queryList = null;
		
		// hookEntity is retrieved from cache.. 
		EntityInterface hookEntity = getStaticEntity(hookEntityName);

		List<ContainerInterface> newContainers = null;
		if (isEditedXmi) {
			LOGGER.info("AbstractXmiImporter :: associateHookEntity :: newContainers :: "+newContainers);
			newContainers = getNewContainers(mainContainerList, hookEntity);
		} else {
			newContainers = mainContainerList;
		}
		
		try {
			queryList = addNewIntegrationObjects(hookEntity, newContainers, intermodelAssociationCollection);
		} catch (Exception e) {
			throw new RuntimeException("Error adding integration objects", e);
		}
		return queryList;

	}
	
	/**
	 * It will add the new association between static entity & each of the main containers entity.
	 * @param staticEntity source entity of the association to be added.
	 * @param mainContainerList main container list .
	 * @param hibernatedao dao
	 * @return query list
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 * @throws BizLogicException exception.
	 * @throws DynamicExtensionsApplicationException exception.
	 */
	private static DynamicQueryList addNewIntegrationObjects(EntityInterface staticEntity,
			List<ContainerInterface> mainContainerList, final Set<AssociationInterface> intermodelAssociationCollection)
			throws DynamicExtensionsSystemException, DAOException, BizLogicException,
			DynamicExtensionsApplicationException
	{
		try {
				EntityManager.getInstance().persistEntityMetadataForAnnotation(staticEntity, true,false, null);
		}catch(Exception e)
		{
			LOGGER.info("AbstractXmiImporter :: addNewIntegrationObjects :: Exception :: "+e.getMessage());
		}
		List<String> queriesList = new ArrayList<String>();
		List<String> revQueryList = new ArrayList<String>();
		for (ContainerInterface containerInterface : mainContainerList)
		{
			if(containerInterface.getAbstractEntity() != null)
			{
				AssociationInterface association = createAssociation(staticEntity,
						(EntityInterface) containerInterface.getAbstractEntity());
				//Add association to the static entity.
				if(association != null) {
					staticEntity.addAssociation(association);

					//create an association without setting the source entity.
					//Hook entity will be updated as the source on the server
					intermodelAssociationCollection.add(association);
					queriesList.addAll(QueryBuilderFactory.getQueryBuilder().getQueryPartForAssociation(
							association, revQueryList, true));
				}
			}
		}
		DynamicQueryList dynamicQueryList = new DynamicQueryList();
		if(queriesList != null) {
			dynamicQueryList.setQueryList(queriesList);
			dynamicQueryList.setRevQueryList(revQueryList);
		}
		return dynamicQueryList;
	}

	/**
	 *It will create a new association object between the staticEntity & dynamicEntity.
	 * @param staticEntity source entity
	 * @param dynamicEntity destination entity.
	 * @return newly added association
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public static AssociationInterface createAssociation(EntityInterface staticEntity,
			EntityInterface dynamicEntity) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		if(staticEntity != null && dynamicEntity != null && dynamicEntity.getId() != null) {
		//Create source role and target role for the association
			String roleName = staticEntity.getId().toString().concat("_").concat(
					dynamicEntity.getId().toString());
			RoleInterface sourceRole = getRole(AssociationType.CONTAINTMENT, roleName,
					Cardinality.ZERO, Cardinality.ONE);
			RoleInterface targetRole = getRole(AssociationType.CONTAINTMENT, roleName,
					Cardinality.ZERO, Cardinality.MANY);
	
			//Create association with the created source and target roles.
			AssociationInterface association = getAssociation(dynamicEntity,
					AssociationDirection.SRC_DESTINATION, roleName, sourceRole, targetRole);
	
			//Create constraint properties for the created association.
			ConstraintPropertiesInterface constProperts = AnnotationUtil.getConstraintProperties(
					staticEntity, dynamicEntity);
			association.setConstraintProperties(constProperts);
			return association;
		}
		return null;
	}
	
	public static EntityGroupInterface createEntityGroup(String groupName, EntityGroupInterface entityGroup)
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (entityGroup == null)
		{
			DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
			entityGroup = domainObjectFactory.createEntityGroup();
			entityGroup.setShortName(groupName);
			entityGroup.setName(groupName);
			entityGroup.setLongName(groupName);
			entityGroup.setDescription(groupName);
		}
		return entityGroup;
	}
	
	
	public static EntityGroupInterface retrieveEntityGroup(final String entityGroupName)
	throws DynamicExtensionsSystemException
	{	
		EntityGroupInterface entityGroup = EntityCache.getInstance().getEntityGroupByName(entityGroupName);

		LOGGER.info(" ");
		LOGGER.info(" ");
		LOGGER.info("#################################");
		LOGGER.info("##   EntityGroup present = " + ((entityGroup == null) ? "N" : "Y")	+ "   ##");
		LOGGER.info("#################################");
		LOGGER.info(" ");
		LOGGER.info(" ");

		return entityGroup;
	}
	public static void addTaggedValue(final String packageName, EntityGroupInterface entityGroup)
	{
		Collection<TaggedValueInterface> tvColl = entityGroup.getTaggedValueCollection();
		if (tvColl == null)
		{
			tvColl = new HashSet<TaggedValueInterface>();
		}
		// It will search the tag with same key in the tvColl so that it will not add the tag multiple times.
		TaggedValueInterface taggedValue = DynamicExtensionsUtility.updatePackageName(tvColl,XMIConstants.TAGGED_NAME_PACKAGE_NAME);
		// If tag not present it will create new one & add it to the object. else will use same previous object.
		if (taggedValue == null)
		{
			taggedValue = DomainObjectFactory.getInstance().createTaggedValue();
			taggedValue.setKey(XMIConstants.TAGGED_NAME_PACKAGE_NAME);
			taggedValue.setValue(packageName);
			tvColl.add(taggedValue);
		}
		else
		{
			taggedValue.setValue(packageName);
		}
	}

	/**
	 * It will create a new Role object using given parameters
	 * @param associationType associationType
	 * @param name name
	 * @param minCard  minCard
	 * @param maxCard maxCard
	 * @return  RoleInterface
	 */
	private static RoleInterface getRole(AssociationType associationType, String name,
			Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}
	
	/**
	 * It will create a new association & then set its name etc by using the given parameter.
	 * @param targetEntity target entity of the association
	 * @param assonDirectn direction of association
	 * @param assoName name of association
	 * @param sourceRole source role
	 * @param targetRole target role
	 * @return association object
	 * @throws DynamicExtensionsSystemException exception.
	 */
	private static AssociationInterface getAssociation(EntityInterface targetEntity,
			AssociationDirection assonDirectn, String assoName, RoleInterface sourceRole,
			RoleInterface targetRole) throws DynamicExtensionsSystemException
	{
		AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
		association.setTargetEntity(targetEntity);
		association.setAssociationDirection(assonDirectn);
		association.setName(assoName);
		association.setSourceRole(sourceRole);
		association.setTargetRole(targetRole);
		return association;
	}

	/**
	 * It will separate the containers in new containers list & existing main container list
	 * according to weather they are already associated with hook entity or not.
	 * @param mainContainerList containers which are to be separated.
	 * @param staticEntity static entity.
	 * @return 
	 */
	private static List<ContainerInterface> getNewContainers(List<ContainerInterface> mainContainerList,
			EntityInterface staticEntity)
	{
		List<ContainerInterface> newContainers = new ArrayList<ContainerInterface>();
		for (ContainerInterface mainContainer : mainContainerList)
		{
			boolean isAssonPresent = false;
			Collection<AssociationInterface> allAssociations = staticEntity.getAllAssociations();
			for (AssociationInterface association : allAssociations)
			{
				if (mainContainer.getAbstractEntity().getId() != null && association.getTargetEntity().getId().compareTo(
						mainContainer.getAbstractEntity().getId()) == 0)
				{
					isAssonPresent = true;
					break;
				}
			}
			if (!isAssonPresent)
			{
				newContainers.add(mainContainer);
			}
		}
		return newContainers;
	}
	
	/**
	 * It will retrieve the Entity with name given in hook entity using provided DAo
	 * @param hookEntityName name of the hook entity to retrieve.
	 * @param hibernatedao dao used for retrieving the hook entity.
	 * @return the retrieved entity.
	 * @throws DAOException exception.
	 * @throws DynamicExtensionsApplicationException exception.
	 * @throws DynamicExtensionsSystemException exception.
	 */
	public static EntityInterface getStaticEntity(String hookEntityName)
			throws DAOException, DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		EntityInterface entity = null;
			/**
			 *  Get the static entity that needs to be hooked from cache.
			 */
		entity = EntityCache.getInstance().getEntityByName(hookEntityName);
		if(entity != null) {
				Logger.out.info("\n###### DEUtility :: getStaticEntity :: "+hookEntityName+" :: is hooked ######\n ");
		}
		return entity;
	}
}



