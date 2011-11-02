
package edu.wustl.common.querysuite.querableobject;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.querysuite.querableobjectinterface.QueryableAttributeInterface;

public class TestQueryableEntity extends DynamicExtensionsBaseTestCase
{

	public void testGetEntityAttributes()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (queryEntity.getEntityAttributes().size() != entity.getAttributeCollection()
						.size())
				{
					System.out
							.println("testgetEntityAttributes --> Failed. Collection size are different for entity :"
									+ entity.getName());
					fail();
				}
				System.out.println("testgetEntityAttributes --> successfull.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testgetEntityAttributes --> failed.");
		}

	}

	public void testGetEntityCollection()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (queryEntity.getEntityCollection().size() != 1)
				{
					System.out
							.println("testGetEntityCollection --> Failed. Entity collection contains more than one queryable entity"
									+ entity.getName());
					fail();
				}
				System.out.println("testGetEntityCollection --> successfull.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetEntityCollection --> failed.");
		}
	}

	public void testGetParentEntity()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (!queryEntity.getId().equals(entity.getId()))
				{
					System.out
							.println("testGetParentEntity --> Failed. Id not match with original entity. Original entity name :"
									+ entity.getName());
					fail();
				}
				if (!queryEntity.getName().equals(entity.getName()))
				{
					System.out
							.println("testGetParentEntity --> Failed. Name not match with original entity. Original entity name :"
									+ entity.getName());
					fail();
				}
				if (entity.getDescription() != null
						&& !queryEntity.getDescription().equals(entity.getDescription()))
				{
					System.out
							.println("testGetParentEntity --> Failed. Description not match with original entity. Original entity name :"
									+ entity.getName());
					fail();
				}
				if (!queryEntity.getTableProperties().equals(entity.getTableProperties()))
				{
					System.out
							.println("testGetParentEntity --> Failed. TableProperties not match with original entity. Original entity name :"
									+ entity.getName());
					fail();
				}
				if (!queryEntity.getRootQueryableObject().equals(queryEntity))
				{
					System.out
							.println("testGetParentEntity --> Failed. root queryableObject not match with original entity. Original entity name :"
									+ entity.getName());
					fail();
				}
				if (entity.getParentEntity() != null
						&& !queryEntity.getParentEntity().equals(
								new QueryableEntity(entity.getParentEntity())))
				{
					System.out
							.println("testGetParentEntity --> Failed. parent queryableEntity not match with original entity. Original entity name :"
									+ entity.getName());
					fail();
				}
				System.out.println("testGetParentEntity --> SuccessFull.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetParentEntity --> failed.");
		}
	}

	public void testQueryableEntityInvalidConstructor()
	{
		try
		{
			new QueryableEntity(null);
			fail("testQueryableEntityInvalidConstructor --> failed, exception should have been thrown.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out
					.println("testQueryableEntityInvalidConstructor --> exception thrown , thus successfull.");
		}
	}

	public void testGetEntityAttributeByName()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (!entity.getAttributeCollection().isEmpty())
				{
					String attName = entity.getAttributeCollection().iterator().next().getName();
					QueryableAttributeInterface queryableAttr = queryEntity
							.getEntityAttributeByName(attName);
					if (queryableAttr == null)
					{
						fail("testGetEntityAttributeByName --> failed , Attribute with name "
								+ attName + " not found in " + queryEntity.getName());
					}
				}
			}
			System.out.println("testGetEntityAttributeByName --> success");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetEntityAttributeByName --> failed.");
		}
	}

	public void testGetTaggedValueColl()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (queryEntity.getTaggedValueCollection().isEmpty())
				{
					String value = queryEntity
							.getTaggedValue(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME);
					if (value != ""
							&& queryEntity
									.isTagPresent(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME))
					{
						fail("testGetTaggedValueColl--> failed, tagged value present on queryEntity while not present on entity "
								+ entity.getName());
					}
				}
				else
				{
					TaggedValueInterface entityTaggedValue = entity.getTaggedValueCollection()
							.iterator().next();
					String value = queryEntity.getTaggedValue(entityTaggedValue.getKey());

					if (!value.equals(entityTaggedValue.getValue()))
					{
						fail("testGetTaggedValueColl--> value of the tagg does not match with that of the tagg of entity "
								+ entity.getName());
					}
				}
			}
			System.out.println("testGetTaggedValueColl --> success");

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetTaggedValueColl --> failed.");
		}
	}

	public void testGetEntityAttributeForQuery()
	{
		try
		{
			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (!entity.getAttributeCollection().isEmpty())
				{
					if (queryEntity.getEntityAttributesForQuery().size() < entity
							.getEntityAttributesForQuery().size())
					{
						fail("testGetEntityAttributeForQuery --> failed , collection of Attributes for query"
								+ " not simialr to that of entity " + queryEntity.getName());
					}
				}
			}
			System.out.println("testGetEntityAttributeForQuery --> success");

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetEntityAttributeForQuery --> failed.");
		}
	}
	public void testGetEntity()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				assertNotNull(queryEntity.getEntity());
				assertEquals(queryEntity.getEntity(),entity);
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetEntity --> failed.");
		}
	}
	public void testGetDiscriminatorValue()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				assertEquals(queryEntity.getDiscriminatorValue(),entity.getDiscriminatorValue());
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetDiscriminatorValue --> failed.");
		}
	}
	public void testGetgetInheritanceStrategy()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				assertEquals(queryEntity.getInheritanceStrategy(),entity.getInheritanceStrategy());
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetgetInheritanceStrategy --> failed.");
		}
	}
	public void testIsAbstract()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				assertEquals(queryEntity.isAbstract(),entity.isAbstract());
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testIsAbstract --> failed.");
		}
	}
	public void testIsTagPresent()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				Collection<TaggedValueInterface> collection=queryEntity.getTaggedValueCollection();
				for (TaggedValueInterface taggedValueInterface : collection)
				{
					assertTrue(queryEntity.isTagPresent(taggedValueInterface.getKey()));
					break;
				}

				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testIsTagPresent --> failed.");
		}
	}
	public void testIsAttributePresent()
	{
		try
		{

			EntityGroupInterface testModel = EntityGroupManager.getInstance().getEntityGroupByName(
					TEST_ENTITYGROUP_NAME);
			for (EntityInterface entity : testModel.getEntityCollection())
			{
				QueryableEntity queryEntity = new QueryableEntity(entity);
				if (!entity.getAttributeCollection().isEmpty())
				{
					String attName = entity.getAttributeCollection().iterator().next().getName();
					assertTrue(queryEntity.isAttributePresent(attName));
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testIsAttributePresent --> failed.");
		}
	}
}
