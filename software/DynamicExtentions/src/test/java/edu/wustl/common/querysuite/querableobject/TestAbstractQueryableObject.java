
package edu.wustl.common.querysuite.querableobject;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.querysuite.querableobjectinterface.QueryableAttributeInterface;

public class TestAbstractQueryableObject extends DynamicExtensionsBaseTestCase
{

	public void testGetAllAttributes()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				AbstractQueryableObject queryObject = new QueryableEntity(entity);
				Collection<QueryableAttributeInterface> queryAttributeColl = queryObject
						.getAllAttributes();
				for (QueryableAttributeInterface queryAttribute : queryAttributeColl)
				{
					if (queryObject.getAttributeByName(queryAttribute.getName()) == null)
					{
						System.out
								.println("testGetAllAttributes --> failed. Queryable Attribute with name "
										+ queryAttribute.getName()
										+ " not found in entity "
										+ queryObject.getName());
						fail();

					}
					if (queryAttribute.getActualEntity().equals(entity)
							&& !queryObject.isAttributePresent(queryAttribute.getName()))
					{
						System.out
								.println("testGetAllAttributes --> failed. Queryable Attribute with name "
										+ queryAttribute.getName()
										+ " not found in entity "
										+ queryObject.getName());
						fail();

					}
					if (!queryAttribute.getQueryEntity().equals(queryObject))
					{
						System.out
								.println("testGetAllAttributes --> failed. QueryableObject equal method returns false for entity "
										+ queryObject.getName());
						fail();
					}
					if (queryObject.isCategory)
					{
						fail("testGetAllAttributes --> failed. Queryable Object is category while actually it is entity "
								+ queryObject.getName());
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetAllAttributes --> failed.");
		}
	}

}
