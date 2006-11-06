/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0 name
	 */
	public TestEntityManager(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
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
			//Checking whether metadata information is saved properly or not.
			assertEquals(entity.getName(), newEntity.getName());
			String tableName = entity.getTableProperties().getName();
			String query = "Select * from " + tableName;
			executeQuery(query);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
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

			//System.out.println(map);;
		}
		catch (DynamicExtensionsSystemException e)
		{
			// TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}
		catch (DynamicExtensionsApplicationException e)
		{
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}
		catch (DynamicExtensionsApplicationException e)
		{
			// TODO Auto-generated catch block
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

			//

			Map map = EntityManager.getInstance().getRecordById(entity, new Long(1));
			fail("Exception should have be thrown since entity is not saved");

		}
		catch (DynamicExtensionsSystemException e)
		{
			assertTrue(true);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			Logger.out.debug(e.getMessage());
		}

	}

}
