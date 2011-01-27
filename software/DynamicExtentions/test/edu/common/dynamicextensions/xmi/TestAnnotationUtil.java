
package edu.common.dynamicextensions.xmi;

import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestAnnotationUtil extends DynamicExtensionsBaseTestCase
{

	public void testAddAssociation()
	{
		try
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			Long staticEntityId = entityManager
					.getEntityId("edu.wustl.catissuecore.domain.RecordEntry");
			Long dynamicEntityId = entityManager.getContainerIdForEntity(entityManager
					.getEntityId("HealthAnnotations"));

			AnnotationUtil.addAssociation(staticEntityId, dynamicEntityId, true);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("testAddAssociation --> failed, exception occured.");
		}

	}
}
