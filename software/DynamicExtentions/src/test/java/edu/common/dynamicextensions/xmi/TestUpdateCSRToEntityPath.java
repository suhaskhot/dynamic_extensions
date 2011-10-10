
package edu.common.dynamicextensions.xmi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestUpdateCSRToEntityPath extends DynamicExtensionsBaseTestCase
{

	public void testAddCuratedPathsFromToAllNewEntities()
	{
		try
		{

			List<AssociationInterface> associationList = new ArrayList<AssociationInterface>();
			EntityManagerInterface entityManager = EntityManager.getInstance();

			long staticEntityId = entityManager
					.getEntityId("edu.wustl.catissuecore.domain.EventVisit");
			long hookEntityId = entityManager
					.getEntityId("edu.wustl.catissuecore.domain.RecordEntry");
			Collection<AssociationInterface> recordEntryAssociationColl = EntityManager
					.getInstance().getAssociations(staticEntityId, hookEntityId);
			if (recordEntryAssociationColl == null || recordEntryAssociationColl.isEmpty())
			{
				fail("testAddCuratedPathsFromToAllEntities --> No association found between EventVisit --> RecordEntry in static model");
			}
			EntityGroupInterface testGroup = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			List<Long> newEntitiesList = new ArrayList<Long>();
			newEntitiesList.addAll(EntityManager.getInstance().getAllEntityIdsForEntityGroup(
					testGroup.getId()));
			associationList.add(recordEntryAssociationColl.iterator().next());
			UpdateCSRToEntityPath
					.addCuratedPathsFromToAllEntities(associationList, newEntitiesList);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testAddCuratedPathsFromToAllEntities-->failed. exception occured.");
		}

	}

	public void testAddCuratedPathsFromToAllEntities()
	{
		try
		{

			List<AssociationInterface> associationList = new ArrayList<AssociationInterface>();
			EntityManagerInterface entityManager = EntityManager.getInstance();

			long staticEntityId = entityManager
					.getEntityId("edu.wustl.catissuecore.domain.EventVisit");
			long hookEntityId = entityManager
					.getEntityId("edu.wustl.catissuecore.domain.RecordEntry");
			Collection<AssociationInterface> recordEntryAssociationColl = EntityManager
					.getInstance().getAssociations(staticEntityId, hookEntityId);
			if (recordEntryAssociationColl == null || recordEntryAssociationColl.isEmpty())
			{
				fail("testAddCuratedPathsFromToAllEntities --> No association found between EventVisit --> RecordEntry in static model");
			}

			associationList.add(recordEntryAssociationColl.iterator().next());
			UpdateCSRToEntityPath.addCuratedPathsFromToAllEntities(associationList, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testAddCuratedPathsFromToAllEntities-->failed. exception occured.");
		}

	}
}
