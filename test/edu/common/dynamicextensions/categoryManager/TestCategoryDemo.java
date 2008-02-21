/**
 * This is the test class for creating the categories from various models.
 */

package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.categoryManager.CategoryHelperInterface.ControlEnum;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringValue;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
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
	 * Add bmi attribute to vitals entity and add permissible values to this attribute. 
	 *
	 */
	public void testAddPermissibleValuesToVitalsEntityModel()
	{
		try
		{
			DomainObjectFactory factory = DomainObjectFactory.getInstance();

			EntityGroupInterface entityGroup = retrieveEntityGroup("gcrc");

			// Get the VitalSigns entity from entity group.
			EntityInterface vitals = entityGroup.getEntityByName("VitalSigns");

			AttributeInterface bmi = factory.createStringAttribute();
			bmi.setName("BMI");
			((StringAttributeTypeInformation) bmi.getAttributeTypeInformation()).setSize(40);

			vitals.addAbstractAttribute(bmi);

			// Create permissible values for tumourTissueSite.
			UserDefinedDEInterface userDefinedDE = factory.createUserDefinedDE();

			PermissibleValueInterface permissibleValue1 = factory.createStringValue();
			((StringValue) permissibleValue1).setValue("Underweight: 18.5 or below");

			PermissibleValueInterface permissibleValue2 = factory.createStringValue();
			((StringValue) permissibleValue2).setValue("Healthy Weight: 18.5 - 24.9");

			PermissibleValueInterface permissibleValue3 = factory.createStringValue();
			((StringValue) permissibleValue3).setValue("Overweight: 25.0 - 29.9");

			PermissibleValueInterface permissibleValue4 = factory.createStringValue();
			((StringValue) permissibleValue4).setValue("Obese: 30.0 and above");

			userDefinedDE.addPermissibleValue(permissibleValue1);
			userDefinedDE.addPermissibleValue(permissibleValue2);
			userDefinedDE.addPermissibleValue(permissibleValue3);
			userDefinedDE.addPermissibleValue(permissibleValue4);

			StringAttributeTypeInformation bmiTypeInfo = (StringAttributeTypeInformation) bmi.getAttributeTypeInformation();
			bmiTypeInfo.setDataElement(userDefinedDE);

			EntityGroupManager.getInstance().persistEntityGroup(entityGroup);
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
			ContainerInterface vitalsContainer = categoryHelper.createCategoryEntityAndContainer(vitals, "Vitals");

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

	/**
	 * Create a category from vitals entity and its attributes.
	 * Select a subset of permissible values for one of its category attributes.
	 */
	public void testCreateVitalsCategoryWithAttributeHavingPermissibleValues()
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
			ContainerInterface vitalsContainer = categoryHelper.createCategoryEntityAndContainer(vitals, "Vitals");

			// Set the root category entity.
			categoryHelper.setRootCategoryEntity(vitalsContainer, category);

			// Create User defined DE.
			UserDefinedDEInterface userDefinedDE = (UserDefinedDE) vitals.getAttributeByName("BMI").getAttributeTypeInformation().getDataElement();
			List<String> permissibleValues = new ArrayList<String>();

			for (PermissibleValueInterface pv : userDefinedDE.getPermissibleValueCollection())
			{
				if (!(pv.getValueAsObject().toString().equals("Obese: 30.0 and above")))
				{
					permissibleValues.add(pv.getValueAsObject().toString());
				}
			}

			// Create category attribute(s) for VitalSigns category entity.
			categoryHelper.addControl(vitals, "heartRate", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Pulse");
			categoryHelper.addControl(vitals, "diastolicBloodPressure", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Diastolic BP");
			categoryHelper.addControl(vitals, "systolicBloodPressure", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Systolic BP");
			categoryHelper.addControl(vitals, "weight", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Weight (kg)");
			categoryHelper.addControl(vitals, "height", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Height (cm)");
			categoryHelper.addControl(vitals, "respiratoryRate", vitalsContainer, ControlEnum.TEXT_FIELD_CONTROL, "Respiration");
			categoryHelper.addControl(vitals, "BMI", vitalsContainer, ControlEnum.LIST_BOX_CONTROL, "BMI", permissibleValues);

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
