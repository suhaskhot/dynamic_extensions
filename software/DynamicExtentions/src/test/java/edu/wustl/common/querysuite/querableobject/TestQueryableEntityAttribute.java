
package edu.wustl.common.querysuite.querableobject;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestQueryableEntityAttribute extends DynamicExtensionsBaseTestCase
{

	public void testInvalidConstructor()
	{
		try
		{
			new QueryableEntityAttribute(null, null);
			fail("testInvalidConstructor--> failed, exception should have to be thrown but not.");
		}
		catch (Exception e)
		{
			System.out.println("testInvalidConstructor--> success.");
		}
	}

	public void testQueryableEntityAttOtherProperties()
	{
		try
		{
			EntityGroupInterface entityGroup = EntityGroupManager.getInstance()
					.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			EntityInterface entity = getEntityHavingAttributes(entityGroup);
			AttributeInterface attribute = getAttribute(entity);
			QueryableEntity queryObject = new QueryableEntity(entity);
			QueryableEntityAttribute queryAttribute = new QueryableEntityAttribute(attribute,
					queryObject);
			if (!queryAttribute.getColumnProperties().equals(attribute.getColumnProperties()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, Column properties does not match for attribute "
						+ attribute.getName());
			}
			if (!queryAttribute.getDataType().equals(attribute.getDataType()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, data type does not match for attribute "
						+ attribute.getName());
			}
			if (!queryAttribute.getIsIdentified().equals(attribute.getIsIdentified()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getIsIdentified does not match for attribute "
						+ attribute.getName());
			}
			if (!queryAttribute.getIsNullable().equals(attribute.getIsNullable()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getIsNullable does not match for attribute "
						+ attribute.getName());
			}
			if (!queryAttribute.getIsPrimaryKey().equals(attribute.getIsPrimaryKey()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getIsPrimaryKey does not match for attribute "
						+ attribute.getName());
			}
			if (attribute.getDescription() != null
					&& !queryAttribute.getDescription().equals(attribute.getDescription()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getIsPrimaryKey does not match for attribute "
						+ attribute.getName());
			}
			if (queryAttribute.getTaggedValueCollection().size() != attribute
					.getTaggedValueCollection().size())
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getTaggedValueCollection does not match for attribute "
						+ attribute.getName());
			}
			if (!queryAttribute.getAttributeTypeInformation().equals(
					attribute.getAttributeTypeInformation()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getAttributeTypeInformation does not match for attribute "
						+ attribute.getName());
			}
			if (!queryAttribute.getRootQueryableAttribte().getId().equals(attribute.getId()))
			{
				fail("testQueryableEntityAttOtherProperties-->failed, getRootQueryableAttribte does not match for attribute "
						+ attribute.getName());
			}

		}
		catch (Exception e)
		{
			System.out
					.println("testQueryableEntityAttOtherProperties --> failed. exception thrown.");
		}
	}

	private AttributeInterface getAttribute(EntityInterface entity)
	{
		for (AttributeInterface attribute : entity.getAttributeCollection())
		{
			if (!attribute.getName().equals("id"))
			{
				return attribute;
			}
		}
		return null;
	}

	private EntityInterface getEntityHavingAttributes(EntityGroupInterface entityGroup)
	{
		for (EntityInterface entity : entityGroup.getEntityCollection())
		{
			if (entity.getAttributeCollection().size() > 2)
			{
				return entity;
			}
		}
		return null;
	}
	public void testGetDisplayName()
	{
		try
		{
			EntityGroupInterface entityGroup = EntityGroupManager.getInstance()
			.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			EntityInterface entity = getEntityHavingAttributes(entityGroup);
			AttributeInterface attribute = getAttribute(entity);
			QueryableEntity queryObject = new QueryableEntity(entity);
			QueryableEntityAttribute queryAttribute = new QueryableEntityAttribute(attribute,
					queryObject);
			assertNotNull(queryAttribute.getDisplayName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetDescription-->failed, exception occured.");
		}
	}
	public void testIsTagPresent()
	{
		try
		{
			EntityGroupInterface entityGroup = EntityGroupManager.getInstance()
			.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			EntityInterface entity = getEntityHavingAttributes(entityGroup);
			AttributeInterface attribute = getAttribute(entity);
			QueryableEntity queryObject = new QueryableEntity(entity);
			QueryableEntityAttribute queryAttribute = new QueryableEntityAttribute(attribute,
					queryObject);
			Collection<TaggedValueInterface> taggedValueColl=queryAttribute.getTaggedValueCollection();
			for (TaggedValueInterface taggedValueInterface : taggedValueColl)
			{
				assertTrue(queryAttribute.isTagPresent(taggedValueInterface.getKey()));
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testIsTagPresent-->failed, exception occured.");
		}
	}
	public void testGetTaggedValue()
	{
		try
		{
			EntityGroupInterface entityGroup = EntityGroupManager.getInstance()
			.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			EntityInterface entity = getEntityHavingAttributes(entityGroup);
			AttributeInterface attribute = getAttribute(entity);
			QueryableEntity queryObject = new QueryableEntity(entity);
			QueryableEntityAttribute queryAttribute = new QueryableEntityAttribute(attribute,
					queryObject);
			Collection<TaggedValueInterface> taggedValueColl=queryAttribute.getTaggedValueCollection();
			for (TaggedValueInterface taggedValueInterface : taggedValueColl)
			{
				assertEquals(queryAttribute.getTaggedValue(taggedValueInterface.getKey()),taggedValueInterface.getValue());
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetTaggedValue-->failed, exception occured.");
		}
	}
}
