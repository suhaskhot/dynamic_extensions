/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright://TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domain.FloatAttribute;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.util.logger.Logger;

public class TestEntityManager extends DynamicExtensionsBaseTestCase
{

	/**
	 * 
	 */
	public TestEntityManager()
	{
		super();
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0 name
	 */
	public TestEntityManager(String arg0)
	{
		super(arg0);
		//TODO Auto-generated constructor stub
	}

	/**
	 * @see edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase#setUp()
	 */
	protected void setUp()
	{
		super.setUp();
	}

	/**
	 * @see edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase#tearDown()
	 */
	protected void tearDown()
	{
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testCreateEntity()
	{
		try
		{
			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			entity = (Entity) EntityManager.getInstance().createEntity(entity);
			Entity newEntity = (Entity) EntityManager.getInstance().getEntityByIdentifier(
					entity.getId().toString());
			// Checking whether metadata information is saved properly or not.
			assertEquals(entity.getName(), newEntity.getName());
			String tableName = entity.getTableProperties().getName();
			String query = "Select * from " + tableName;
			executeQuery(query);
		}
		catch (Exception e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * 
	 */
	public void testCreateEntityWithEntityGroup()
	{
		try
		{
			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			EntityGroup entityGroup = new EntityGroup();
			entityGroup.setName("testEntityGroup");
			entity.addEntityGroupInterface(entityGroup);
			entityGroup.addEntity(entity);
			entity = (Entity) EntityManager.getInstance().createEntity(entity);
			Entity newEntity = (Entity) EntityManager.getInstance().getEntityByIdentifier(
					entity.getId().toString());
			//  Checking whether metadata information is saved properly or not.
			assertEquals(entity.getName(), newEntity.getName());
			Collection collection = newEntity.getEntityGroupCollection();
			Iterator iter = collection.iterator();
			EntityGroup eg = (EntityGroup) iter.next();
			assertEquals(eg.getId(), entityGroup.getId());

		}
		catch (Exception e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * 
	 *
	 */
	public void testCreateEntityForQueryException()
	{
		try
		{
			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			TableProperties tableProperties = new TableProperties();
			tableProperties.setName("!##$$%");
			entity.setTableProperties(tableProperties);
			entity = (Entity) EntityManager.getInstance().createEntity(entity);
			fail("Exception should have occured but did not");
		}
		catch (DynamicExtensionsSystemException e)
		{
			Logger.out.info("Exception because of wrong table name.");
			Logger.out.info(e.getMessage());
		}
		catch (Exception e)
		{
			//TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 *
	 */
	public void testCreateEntityForRollbackQuery()
	{
		Entity entity = null;
		try
		{
			entity = (Entity) new MockEntityManager().initializeEntity();
			TableProperties tableProperties = new TableProperties();
			tableProperties.setName("Created_table");
			entity.setTableProperties(tableProperties);
			String query = "create table Created_table (id integer)";
			executeQuery(query);
			EntityManager.getInstance().createEntity(entity);
			fail("Exception should have occured but did not");
		}
		catch (DynamicExtensionsSystemException e)
		{
			Logger.out.info("Exception because of wrong table name.");
			Logger.out.info(e.getMessage());
		}
		catch (Exception e)
		{
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			executeQuery("drop table Created_table");
			try
			{
				Entity newEntity = (Entity) EntityManager.getInstance().getEntityByIdentifier(
						entity.getId().toString());
				fail();

			}
			catch (DynamicExtensionsApplicationException e)
			{
				Logger.out.info("Entity object not found in the database ....");
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}

		}

	}

	/**
	 * This method tests GetRecordById method for the condition where record and entity does exists
	 */
	public void testGetRecordById()
	{

		try
		{
			EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			entity = (Entity) entityManagerInterface.createEntity(entity);

			Entity newEntity = (Entity) entityManagerInterface.getEntityByIdentifier(entity.getId()
					.toString());
			Map dataValue = new HashMap();

			Iterator attrIterator = newEntity.getAttributeCollection().iterator();
			int i = 0;
			while (attrIterator.hasNext())
			{
				AttributeInterface attribute = (AttributeInterface) attrIterator.next();

				if (attribute instanceof StringAttributeInterface)
				{
					dataValue.put(attribute, "temp" + i);
				}
				else if (attribute instanceof DateAttributeInterface)
				{
					dataValue.put(attribute, "11-01-2006");
				}

				i++;
			}

			entityManagerInterface.insertData(newEntity, dataValue);

			assertEquals("Employee", entity.getName());
			Map map = EntityManager.getInstance().getRecordById(entity, new Long(1));

			System.out.println(map);
		}
		catch (DynamicExtensionsSystemException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}
		catch (DynamicExtensionsApplicationException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * This method tests GetRecordById method for the condition wheer record does not exists for given id.
	 */
	public void testGetRecordByIdNoRecord()
	{

		try
		{
			EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			entity = (Entity) entityManagerInterface.createEntity(entity);

			Map map = EntityManager.getInstance().getRecordById(entity, new Long(1));
			assertEquals(0, map.size());
		}
		catch (DynamicExtensionsSystemException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}
		catch (DynamicExtensionsApplicationException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * This method tests GetRecordById method for the condition where entity is not saved , entity  is null or record id id null
	 */
	public void testGetRecordByIdEntityNotSaved()
	{

		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		Entity entity = null;

		try
		{
			entity = (Entity) new MockEntityManager().initializeEntity();

			Map map = EntityManager.getInstance().getRecordById(entity, new Long(1));
			fail("Exception should have be thrown since entity is not saved");

		}
		catch (DynamicExtensionsSystemException e)
		{
			assertTrue(true);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Unexpected Exception occured");
		}

		try
		{
			Map map = EntityManager.getInstance().getRecordById(null, new Long(1));
			fail("Exception should have be thrown since entity is not saved");
		}
		catch (DynamicExtensionsSystemException e)
		{
			assertTrue(true);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
		}

		try
		{
			entity = (Entity) entityManagerInterface.createEntity(entity);

			Map map = EntityManager.getInstance().getRecordById(entity, null);
			fail("Exception should have be thrown since entity is not saved");
		}
		catch (DynamicExtensionsSystemException e)
		{
			assertTrue(true);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
		}

	}

	/**
	 * 
	 */
	public void testCreateContainer()
	{
		try
		{
			Container container = (Container) new MockEntityManager().getContainer("abc");
			EntityManager.getInstance().createContainer(container);
			Collection list = EntityManager.getInstance().getAllContainers();
			assertNotNull(list);
			Iterator iter = list.iterator();
			boolean flag = false;
			while (iter.hasNext())
			{
				Container cont = (Container) iter.next();
				if (cont.getId().equals(container.getId()))
				{
					flag = true;
					break;
				}
			}
			assertTrue(flag);
		}
		catch (Exception e)
		{
			//TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * 
	 */
	public void testCreateContainerForContainerWithoutEntity()
	{
		try
		{
			Container container = new Container();
			EntityManager.getInstance().createContainer(container);
			Collection list = EntityManager.getInstance().getAllContainers();
			assertNotNull(list);
			Iterator iter = list.iterator();
			boolean flag = false;
			while (iter.hasNext())
			{
				Container cont = (Container) iter.next();
				if (cont.getId().equals(container.getId()))
				{
					flag = true;
					break;
				}
			}
			assertTrue(flag);
		}
		catch (Exception e)
		{
			// //TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}
	}

	/**
	 * 
	 */
	public void testEditEntityForNewAddedAttribute()
	{
		try
		{
			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			entity = (Entity) EntityManager.getInstance().createEntity(entity);
			AttributeInterface attr = new MockEntityManager().initializeStringAttribute("na", "ab");
			attr.setEntity(entity);
			entity.addAbstractAttribute(attr);
			entity = (Entity) EntityManager.getInstance().editEntity(entity);
			//   Checking whether metadata information is saved properly or not.
			String tableName = entity.getTableProperties().getName();

		}
		catch (Exception e)
		{
			//     //TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * 
	 */
	public void testEditEntityForModifiedIsNullableAttribute()
	{
		try
		{
			Entity entity = (Entity) new MockEntityManager().initializeEntity();
			AttributeInterface attr = new MockEntityManager().initializeStringAttribute("na", "ab");
			attr.setEntity(entity);
			entity.addAbstractAttribute(attr);
			entity = (Entity) EntityManager.getInstance().createEntity(entity);
			attr = (AttributeInterface) entity.getAttributeByIdentifier(attr.getId());
			attr.setIsNullable(new Boolean(false));
			entity = (Entity) EntityManager.getInstance().editEntity(entity);
			Attribute savedAttribute = (Attribute) entity.getAttributeByIdentifier(attr.getId());
			assertFalse(savedAttribute.getIsNullable());
			//   Checking whether metadata information is saved properly or not.
			String tableName = entity.getTableProperties().getName();
			//   //TODO Check table query
		}
		catch (Exception e)
		{
			// //TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}

	/**
	 * 
	 */
	public void testEditEntity()
	{
		Entity entity = new Entity();
		entity.setName("Stock Quote");
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

		try
		{
			EntityInterface savedEntity = entityManagerInterface.createEntity(entity);

			//Edit entity
			AttributeInterface floatAtribute = new FloatAttribute();
			floatAtribute.setName("Price");

			savedEntity.addAbstractAttribute(floatAtribute);
			EntityInterface editedEntity = entityManagerInterface.editEntity(savedEntity);

			Map dataValue = new HashMap();
			dataValue.put(floatAtribute, "15.90");
			entityManagerInterface.insertData(editedEntity, dataValue);

			//Edit entity
			AttributeInterface floatAtribute1 = new FloatAttribute();
			floatAtribute.setName("NewPrice");
			editedEntity.addAbstractAttribute(floatAtribute1);

			java.sql.ResultSetMetaData metadata = executeQueryForMetadata("select * from "
					+ editedEntity.getTableProperties().getName());
			assertEquals(metadata.getColumnCount(), 2);

			EntityInterface newEditedEntity = entityManagerInterface.editEntity(editedEntity);
			dataValue.put(floatAtribute1, "16.90");
			entityManagerInterface.insertData(newEditedEntity, dataValue);

			metadata = executeQueryForMetadata("select * from "
					+ editedEntity.getTableProperties().getName());
			assertEquals(metadata.getColumnCount(), 3);

		}
		catch (DynamicExtensionsSystemException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}
		catch (DynamicExtensionsApplicationException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}
		catch (Exception e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}

	}

	/**
	 * 
	 */
	public void testEditEntityRemoveAttribute()
	{
		Entity entity = new Entity();
		entity.setName("Stock Quote");
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

		try
		{

			//Edit entity
			AttributeInterface floatAtribute = new FloatAttribute();
			floatAtribute.setName("Price");

			entity.addAbstractAttribute(floatAtribute);
			EntityInterface savedEntity = entityManagerInterface.createEntity(entity);

			Map dataValue = new HashMap();
			dataValue.put(floatAtribute, "15.90");
			entityManagerInterface.insertData(savedEntity, dataValue);

			java.sql.ResultSetMetaData metadata = executeQueryForMetadata("select * from "
					+ savedEntity.getTableProperties().getName());
			assertEquals(metadata.getColumnCount(), 2);

			//remove attribute
			savedEntity.removeAbstractAttribute(floatAtribute);
			EntityInterface editedEntity = entityManagerInterface.editEntity(entity);

			metadata = executeQueryForMetadata("select * from "
					+ editedEntity.getTableProperties().getName());
			assertEquals(metadata.getColumnCount(), 1);
		}
		catch (DynamicExtensionsSystemException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}
		catch (DynamicExtensionsApplicationException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}
		catch (Exception e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}

	}

	/**
	 * 
	 */
	public void testCreateEnityWithNullAttribute()
	{
		try
		{
			Entity entity = new Entity();
			entity.addAbstractAttribute(null);
			EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
			EntityInterface savedEntity = entityManagerInterface.createEntity(entity);
			assertEquals(savedEntity.getAbstractAttributeCollection(), null);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}
		catch (DynamicExtensionsSystemException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}

	}

	/**
	 * This method test for the created date null for an entity.
	 */
	public void testCreateEntityNull()
	{
		EntityInterface entity = new Entity();
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		entity.setCreatedDate(null);

		try
		{
			EntityInterface savedEntity = entityManagerInterface.createEntity(entity);
			assertEquals(savedEntity.getName(), entity.getName());

			String tableName = entity.getTableProperties().getName();
			String query = "Select * from " + tableName;
			executeQuery(query);

			assertNotNull(savedEntity.getCreatedDate());
		}
		catch (DynamicExtensionsSystemException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}
		catch (DynamicExtensionsApplicationException e)
		{
			fail();
			Logger.out.debug(e.getStackTrace());
		}

	}

}
