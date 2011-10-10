
package edu.wustl.common.querysuite.querableobject;


import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;


public class TestQueryableAttributeInterfaceComparator extends DynamicExtensionsBaseTestCase
{

	public void testcompare()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				AbstractQueryableObject queryObject = new QueryableEntity(entity);
				List queryAttributeColl =(List)queryObject.getAllAttributes();
				Collections.sort(queryAttributeColl);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetAllAttributes --> failed.");
		}
	}

}
