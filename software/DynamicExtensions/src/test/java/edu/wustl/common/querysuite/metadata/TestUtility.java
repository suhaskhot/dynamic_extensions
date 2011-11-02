
package edu.wustl.common.querysuite.metadata;

import java.util.Collection;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringValue;
import edu.common.dynamicextensions.domain.TaggedValue;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestUtility extends DynamicExtensionsBaseTestCase
{

	public void testConcatStrings()
	{
		String string1 = "a";
		String string2 = "b";
		String string3 = "c";
		String string4 = "d";
		assertEquals("a_b_c_d", Utility.concatStrings(string1, string2, string3, string4));
	}

	public void testParseClassName()
	{
		String fullyQualifiedName = "edu.wustl.common.querysuite.metadata.Utility";
		assertEquals("Utility", Utility.parseClassName(fullyQualifiedName));
	}

	public void testIsCategory()
	{
		try
		{
			Entity entity = new Entity();
			entity.setName("Stock Quote");
			TaggedValueInterface taggedValue = new TaggedValue();
			taggedValue.setKey("Category");
			taggedValue.setValue("abc");
			Collection<TaggedValueInterface> taggedValues = new java.util.ArrayList<TaggedValueInterface>();
			taggedValues.add(taggedValue);
			entity.addTaggedValue(taggedValue);

			assertEquals(true, Utility.isCategory(entity));
		}
		catch (Exception ex)
		{
			System.out.println("" + ex.getStackTrace());
		}

	}

	public void testgetTaggedValue()
	{
		try
		{
			TaggedValueInterface taggedValue = DomainObjectFactory.getInstance()
					.createTaggedValue();
			taggedValue.setKey("a");
			taggedValue.setValue("b");
			Collection<TaggedValueInterface> taggedValues = new java.util.ArrayList<TaggedValueInterface>();
			taggedValues.add(taggedValue);
			assertNotNull(Utility.getTaggedValue(taggedValues, "a"));
		}
		catch (Exception ex)
		{
			System.out.println("" + ex.getStackTrace());
		}

	}

	public void testGetPermissibleValues()
	{
		try
		{
			DomainObjectFactory factory = DomainObjectFactory.getInstance();
			AttributeInterface bmi = new Attribute();
			bmi.setAttributeTypeInformation(new StringAttributeTypeInformation());
			bmi.setName("BMI");
			((StringAttributeTypeInformation) bmi.getAttributeTypeInformation()).setSize(40);

			// Add permissible values.
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

			StringAttributeTypeInformation bmiTypeInfo = (StringAttributeTypeInformation) bmi
					.getAttributeTypeInformation();
			bmiTypeInfo.setDataElement(userDefinedDE);
			bmiTypeInfo.setDefaultValue(permissibleValue2);
			assertNotNull(Utility.getPermissibleValues(bmi));
			Utility.isEnumerated(bmi);
		}
		catch (Exception ex)
		{
			System.out.println("" + ex.getStackTrace());
		}
	}

	public void testIsEnumerated()
	{
		try
		{
			DomainObjectFactory factory = DomainObjectFactory.getInstance();
			AttributeInterface bmi = new Attribute();
			bmi.setAttributeTypeInformation(new StringAttributeTypeInformation());
			bmi.setName("BMI");
			((StringAttributeTypeInformation) bmi.getAttributeTypeInformation()).setSize(40);

			// Add permissible values.
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

			StringAttributeTypeInformation bmiTypeInfo = (StringAttributeTypeInformation) bmi
					.getAttributeTypeInformation();
			bmiTypeInfo.setDataElement(userDefinedDE);
			bmiTypeInfo.setDefaultValue(permissibleValue2);
			assertEquals(true, (Utility.isEnumerated(bmi)));
		}
		catch (Exception ex)
		{
			System.out.println("" + ex.getStackTrace());
		}
	}

}
