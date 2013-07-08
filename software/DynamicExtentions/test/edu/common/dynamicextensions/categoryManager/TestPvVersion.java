
package edu.common.dynamicextensions.categoryManager;

import java.util.Collection;

import edu.common.dynamicextensions.client.PermissibleValuesClient;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestPvVersion extends DynamicExtensionsBaseTestCase
{

	/**
	 * This test case will process the permissible value for the XML Folder & specified in the patientContactInfo.xml.
	 * If one of the category creation is failed then this test case is also failed.
	 */
	public void testAddPVVersionForCategoryAttribute()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/TestPvVersion.xml","true"};
			PermissibleValuesClient.main(parameters);
			/*		CategoryPermissibleValuesProcessor pvVersion = new CategoryPermissibleValuesProcessor();
					pvVersion.importPvVersionValues(parameters[0], parameters[1]);*/

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Category category = (Category) categoryManager
					.getCategoryByName("Test Category_Lab Information");
			Collection<CategoryEntityInterface> categories = category.getRootCategoryElement()
					.getChildCategories();
			String pValue = "";
			for (CategoryEntityInterface categoryEntityInterface : categories)
			{
				if ("LabTest".equals(categoryEntityInterface.getEntity().getName()))
				{
					int sizeOfCollection = categoryEntityInterface.getPath()
							.getSortedPathAssociationRelationCollection().size();
					PathAssociationRelationInterface pathAssociationRelationInterface = categoryEntityInterface
							.getPath().getSortedPathAssociationRelationCollection().get(
									sizeOfCollection - 1);
					if ("1".toString().equals(
							pathAssociationRelationInterface.getTargetInstanceId()))
					{
						CategoryAttributeInterface attributeInterface = categoryEntityInterface
								.getAttributeByName("testName Category Attribute");
						DataElementInterface dataElementInterface = ((CategoryAttribute) attributeInterface)
								.getDataElement(ControlsUtility.getFormattedDate("2010-07-01"));
						UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) dataElementInterface;
						for (PermissibleValueInterface permissibleValue : userDefinedDEInterface
								.getPermissibleValueCollection())
						{
							if ("Allergan Test".equals(permissibleValue.getValueAsObject()
									.toString()))
							{
								pValue = permissibleValue.getValueAsObject().toString();
							}
						}
						assertEquals("3", String.valueOf(userDefinedDEInterface
								.getPermissibleValueCollection().size()));
						assertEquals("Allergan Test", pValue);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testAddPVVersionWithDefaultValueSet()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Validate_Default_value.xml","true"};
			PermissibleValuesClient.main(parameters);

			/*CategoryPermissibleValuesProcessor pvVersion = new CategoryPermissibleValuesProcessor();
			pvVersion.importPvVersionValues(parameters[0], parameters[1]);*/

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Category category = (Category) categoryManager
					.getCategoryByName("Test Category_Chemotherapy");
			Collection<CategoryEntityInterface> categories = category.getRootCategoryElement()
					.getChildCategories();

			String defaultValue = "";
			for (CategoryEntityInterface categoryEntityInterface : categories)
			{
				if ("HealthAnnotations".equals(categoryEntityInterface.getEntity().getName()))
				{
					int sizeOfCollection = categoryEntityInterface.getPath()
							.getSortedPathAssociationRelationCollection().size();
					PathAssociationRelationInterface pathAssociationRelationInterface = categoryEntityInterface
							.getPath().getSortedPathAssociationRelationCollection().get(
									sizeOfCollection - 1);
					if ("1".toString().equals(
							pathAssociationRelationInterface.getTargetInstanceId()))
					{
						CategoryAttributeInterface attributeInterface = categoryEntityInterface
								.getAttributeByName("agent Category Attribute");
						DataElementInterface dataElementInterface = ((CategoryAttribute) attributeInterface)
								.getDataElement(ControlsUtility.getFormattedDate("2010-01-01"));
						UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) dataElementInterface;
						Collection<PermissibleValueInterface> defValues = userDefinedDEInterface
								.getDefaultPermissibleValues();
						if (defValues != null && !defValues.isEmpty())
						{
							PermissibleValueInterface defPValue = defValues.iterator().next();
							defaultValue = defPValue.getValueAsObject().toString();
						}

						assertEquals("Cocaine", defaultValue);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testAddPVVersionWithEmptyDefaultValue()
	{
		try
		{
			// Version already added for Validate_Default_value.xml
			/*String[] parameters = {
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Validate_Default_value.xml",
					"CPUML", APPLICATIONURL, "true"};
			PermissibleValuesClient.main(parameters);*/

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Category category = (Category) categoryManager
					.getCategoryByName("Test Category_Chemotherapy");
			Collection<CategoryEntityInterface> categories = category.getRootCategoryElement()
					.getChildCategories();
			boolean isEmtyDefvalue = false;
			for (CategoryEntityInterface categoryEntityInterface : categories)
			{
				if ("HealthAnnotations".equals(categoryEntityInterface.getEntity().getName()))
				{
					int sizeOfCollection = categoryEntityInterface.getPath()
							.getSortedPathAssociationRelationCollection().size();
					PathAssociationRelationInterface pathAssociationRelationInterface = categoryEntityInterface
							.getPath().getSortedPathAssociationRelationCollection().get(
									sizeOfCollection - 1);
					if ("1".toString().equals(
							pathAssociationRelationInterface.getTargetInstanceId()))
					{
						CategoryAttributeInterface attributeInterface = categoryEntityInterface
								.getAttributeByName("agent Category Attribute");
						DataElementInterface dataElementInterface = ((CategoryAttribute) attributeInterface)
								.getDataElement(ControlsUtility.getFormattedDate("2010-03-01"));
						UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) dataElementInterface;
						Collection<PermissibleValueInterface> defValues = userDefinedDEInterface
								.getDefaultPermissibleValues();
						if (defValues == null || defValues.isEmpty())
						{
							isEmtyDefvalue = true;
						}

						assertEquals("true", String.valueOf(isEmtyDefvalue));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testGetDefaultPVVersion()
	{
		try
		{
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Category category = (Category) categoryManager
					.getCategoryByName("Test Category_Chemotherapy");
			Collection<CategoryEntityInterface> categories = category.getRootCategoryElement()
					.getChildCategories();

			for (CategoryEntityInterface categoryEntityInterface : categories)
			{
				if ("HealthAnnotations".equals(categoryEntityInterface.getEntity().getName()))
				{
					int sizeOfCollection = categoryEntityInterface.getPath()
							.getSortedPathAssociationRelationCollection().size();
					PathAssociationRelationInterface pathAssociationRelationInterface = categoryEntityInterface
							.getPath().getSortedPathAssociationRelationCollection().get(
									sizeOfCollection - 1);
					if ("1".toString().equals(
							pathAssociationRelationInterface.getTargetInstanceId()))
					{
						CategoryAttributeInterface attributeInterface = categoryEntityInterface
								.getAttributeByName("agent Category Attribute");
						DataElementInterface dataElementInterface = ((CategoryAttribute) attributeInterface)
								.getDataElement(ControlsUtility.getFormattedDate(null));
						UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) dataElementInterface;
						assertNull(userDefinedDEInterface.getActivationDate());
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testGetCorrectPVversion()
	{
		try
		{
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Category category = (Category) categoryManager
					.getCategoryByName("Test Category_Chemotherapy");
			Collection<CategoryEntityInterface> categories = category.getRootCategoryElement()
					.getChildCategories();

			for (CategoryEntityInterface categoryEntityInterface : categories)
			{
				if ("HealthAnnotations".equals(categoryEntityInterface.getEntity().getName()))
				{
					int sizeOfCollection = categoryEntityInterface.getPath()
							.getSortedPathAssociationRelationCollection().size();
					PathAssociationRelationInterface pathAssociationRelationInterface = categoryEntityInterface
							.getPath().getSortedPathAssociationRelationCollection().get(
									sizeOfCollection - 1);
					if ("1".toString().equals(
							pathAssociationRelationInterface.getTargetInstanceId()))
					{
						CategoryAttributeInterface attributeInterface = categoryEntityInterface
								.getAttributeByName("agent Category Attribute");
						DataElementInterface dataElementInterface = ((CategoryAttribute) attributeInterface)
								.getDataElement(ControlsUtility.getFormattedDate("2010-02-10"));
						UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) dataElementInterface;
						assertEquals(userDefinedDEInterface.getActivationDate(), ControlsUtility
								.getFormattedDate("2010-02-01"));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testValidateDatePvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/validate_date.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}

	}

	public void testSpecialCharactersPvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Special_Characters.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testNewPvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/NewPVs.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testEditPvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Edit_Existing.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testIncorrectXmlSyntaxPvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Inappropriate_syntax.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testIncorrectCategoryAttributeNamePvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Incorrect_category_Name.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testIncorrectFileNamePvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Inappropri.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testEditCasePvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/TestPvVersion.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	public void testIncorrectOptionPvVersion()
	{
		try
		{
			String[] parameters = {"CPUML", APPLICATIONURL,
					"CPUML/TestModels/TestModel_withTags/edited/PV_Version/Incorrect_Option.xml","true"};
			PermissibleValuesClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

}
