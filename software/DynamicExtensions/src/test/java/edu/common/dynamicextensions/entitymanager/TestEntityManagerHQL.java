package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;


public class TestEntityManagerHQL extends DynamicExtensionsBaseTestCase
{
	public TestEntityManagerHQL()
	{
		entityManager = EntityManager.getInstance();
	}
	private EntityManagerInterface  entityManager;

	public void testGetAllEntityIdsForEntityGroup() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroup = entityManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
		assertNotNull(entityManager.getAllEntityIdsForEntityGroup(entityGroup.getId()));
	}

	public void testGetContainerIdFromEntityId() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroup = entityManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
		assertNotNull(entityManager.getContainerIdFromEntityId(entityGroup.getEntityByName("PatientInformation").getId()));
	}
	public void testGetAllSystemGenEntityGroupBeans() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		assertNotNull(entityManager.getAllSystemGenEntityGroupBeans());
	}

	public void testGetContainerCaptionFromEntityId() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroup = entityManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
		assertNotNull(entityManager.getContainerIdFromEntityId(entityGroup.getEntityByName("PatientInformation").getId()));

	}

	public void testGetMainContainer() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroup = entityManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
		assertNotNull(entityManager.getMainContainer(entityGroup.getId()));
	}

	public void testGetContainerCaption() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroup = entityManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
		String containerCaption = entityManager.getContainerCaption(entityGroup.getEntityByName("PatientInformation").getId());
		assertNotNull(containerCaption);
		Long containerId = entityManager.getContainerIdByCaption(containerCaption);
		assertNotNull(containerId);
		assertNotNull(entityManager.getEntityIdByContainerId(containerId));
	}

	public void testGetAllEntities() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		assertNotNull(entityManager.getAllEntities());
	}

	public void testGetRootCategoryEntityIdByCategoryName() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		assertNotNull(entityManager.getRootCategoryEntityIdByCategoryName("Test Category_Chemotherapy"));
	}

	public void testGetAllContainerBeans() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		assertNotNull(entityManager.getAllContainerBeans());
	}

	public void testGetNextIdentifierForEntity() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		assertNotNull(entityManager.getNextIdentifierForEntity("PatientInformation"));
	}
}
