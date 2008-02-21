/**
 * This is the test class for creating the categories from various models.
 */

package edu.common.dynamicextensions.categoryManager;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.categoryManager.CategoryHelperInterface.ControlEnum;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * 
 * @author mandar_shidhore
 * @author kunal_kamble
 *
 */
public class TestCategoryDemo extends DynamicExtensionsBaseTestCase
{
	/**
	 *
	 */
	public TestCategoryDemo()
	{
		super();
	}

	/**
	 * @param arg0 name
	 */
	public TestCategoryDemo(String arg0)
	{
		super(arg0);
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
	 * Retrieve entity group by its name from database.
	 * @param name name of category
	 * @return entity group
	 */
	private EntityGroupInterface retrieveEntityGroup(String name)
	{
		DefaultBizLogic bizlogic = new DefaultBizLogic();
		Collection<EntityGroupInterface> entityGroupCollection = new HashSet<EntityGroupInterface>();
		EntityGroupInterface entityGroup = null;

		try
		{
			// Fetch the entity group from the database.
			entityGroupCollection = bizlogic.retrieve(EntityGroup.class.getName(), "shortName", name);

			if (entityGroupCollection != null && entityGroupCollection.size() > 0)
			{
				entityGroup = (EntityGroupInterface) entityGroupCollection.iterator().next();
			}
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			fail();
		}

		return entityGroup;
	}

	/**
	 * Create a category from vitals entity and its attributes.
	 *
	 */
	public void testCreateVitalsCategory()
	{
		try
		{
			EntityGroupInterface entityGroup = retrieveEntityGroup("gcrc");

			// Get the VitalSigns entity from entity group.
			EntityInterface vitals = entityGroup.getEntityByName("VitalSigns");

			// Create category.
			CategoryHelperInterface categoryHelper = new CategoryHelper();

			CategoryInterface category = categoryHelper.createCategory("Vitals Category");

			// Create category entity from VitalSigns entity.
			ContainerInterface vitalsContainer = categoryHelper.createCategoryEntityAndContainer(vitals);

			// Set the root category entity.
			categoryHelper.setRootCategoryEntity(vitalsContainer, category);

			// Create category attribute(s) for VitalSigns category entity.
			categoryHelper.addControl(vitals, "heartRate", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Pulse");
			categoryHelper.addControl(vitals, "diastolicBloodPressure", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Diastolic BP");
			categoryHelper.addControl(vitals, "systolicBloodPressure", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Systolic BP");
			categoryHelper.addControl(vitals, "weight", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Weight (kg)");
			categoryHelper.addControl(vitals, "height", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Height (cm)");
			categoryHelper.addControl(vitals, "respiratoryRate", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Respiration");

			// Save the category.
			categoryHelper.saveCategory(category);
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
