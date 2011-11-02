/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright://TODO</p>
 *@author Vishvesh Mulay
 *@author Rahul Ner
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.AssociationTreeObject;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.common.dynamicextensions.xmi.XMIConstants;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author kunal_kamble
 *
 */
public class TestEntityGroupManager extends DynamicExtensionsBaseTestCase
{


	public TestEntityGroupManager()
	{
		entityGroupManager = EntityGroupManager.getInstance();
	}
	private EntityGroupManagerInterface entityGroupManager;

	/**
	 * test getAllEntityGroupBeans of entityGroupManagerInterface
	 */
	public void testGetAllEntityGroupBeans()
	{
		try
		{
			entityGroupManager.getAllEntityGroupBeans();
		}
		catch (DynamicExtensionsSystemException e)
		{
			fail();
		}
	}

	/**
	 * test CheckForDuplicateEntityGroupName of entityGroupManagerInterface
	 */
	public void testCheckForDuplicateEntityGroupName()
	{
		try
		{
			EntityGroupInterface entityGroupInterface = entityGroupManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			entityGroupManager.checkForDuplicateEntityGroupName(entityGroupInterface);
			fail();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * test GetEntityGroupByShortName of entityGroupManagerInterface
	 */
	public void testGetEntityGroupByShortName()
	{
		try
		{
			EntityGroupInterface entityGroupInterface = entityGroupManager.getEntityGroupByShortName(TEST_ENTITYGROUP_NAME);
			assertNotNull(entityGroupInterface);

		}

		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * test GetTaggedValue of entityGroupManagerInterface
	 */
	public void testGetTaggedValue()
	{
		try
		{
			EntityGroupInterface entityGroup = entityGroupManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			String packageName = entityGroupManager.getTaggedValue(entityGroup, XMIConstants.TAGGED_NAME_PACKAGE_NAME);
			assertEquals(TEST_MODLE_PCKAGE_NAME, packageName);

		}

		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * test ValidateEntityGroup of entityGroupManagerInterface
	 */
	public void testValidateEntityGroup()
	{
		try
		{
			EntityGroupInterface entityGroup = entityGroupManager.getEntityGroupByName(TEST_ENTITYGROUP_NAME);
			assertEquals(true,entityGroupManager.validateEntityGroup(entityGroup));

		}

		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * test ValidateEntityGroup of entityGroupManagerInterface
	 */
	public void testGetMainContainer()
	{
		try
		{
			EntityGroupInterface entityGroup = entityGroupManager.getEntityGroupByShortName(TEST_ENTITYGROUP_NAME);
			Collection<NameValueBean> beans =  entityGroupManager.getMainContainer(entityGroup.getId());
			assertNotNull(beans);
		}

		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * test ValidateEntityGroup of entityGroupManagerInterface
	 */
	public void testGetAssociationTree()
	{
		try
		{
			Collection<AssociationTreeObject> beans =  entityGroupManager.getAssociationTree();
			assertNotNull(beans);
		}

		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			fail();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
			fail();
		}
	}
}