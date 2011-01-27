
package edu.common.dynamicextensions.categoryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.common.dynamicextensions.client.CategoryMetadataClient;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.validation.ValidatorUtil;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 *
 * @author mandar_shidhore
 *
 */
public class TestCategoryManager extends DynamicExtensionsBaseTestCase
{

	/**
	 * This test case will create the csv file which
	 * contains the Conatiner name & container identifier's used for bulk operations.
	 * Test case will fail if metadata file does not exists after completing.
	 */
	public void testCreateCategoryMetadataFile()
	{
		try
		{
			String[] args = {RESOURCE_DIR_PATH + "categoryNamesMetadata.txt", APPLICATIONURL};
			CategoryMetadataClient.main(args);
			System.out.println("done category metadata Creation");
			File recievedFile = new File("catMetadataFile.csv");
			if (!recievedFile.exists())
			{
				fail("catMetadataFile.csv does not exist.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * step 1 : This method will first insert the data for Test Category_Chemotherapy Category .
	 * step 2: retrieve the inserted record.
	 * step 3: edit some data & then try to edit the data.
	 */
	public void testEditDataForForAllCategories()
	{
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();
		Map<CategoryInterface, Exception> failedCatVsException = new HashMap<CategoryInterface, Exception>();
		List<CategoryInterface> categoryList = getAllCategories();
		for (CategoryInterface category : categoryList)
		{
			try
			{
				System.out.println("Inserting record for " + category.getName());
				CategoryEntityInterface rootCatEntity = category.getRootCategoryElement();
				Map<BaseAbstractAttributeInterface, Object> dataValue = mapGenerator
						.createDataValueMapForCategory(rootCatEntity, 0);

				Long recordId = categoryManager.insertData(category, dataValue);
				System.out.println("Record inserted succesfully for " + category.getName()
						+ " RecordId " + recordId);
				Map<BaseAbstractAttributeInterface, Object> editedDataValue = categoryManager
						.getRecordById(rootCatEntity, recordId);
				mapGenerator.validateRetrievedDataValueMap(editedDataValue, dataValue);
				categoryManager.editData(rootCatEntity, editedDataValue, recordId);
				System.out.println("Record Edited succesfully for " + category.getName()
						+ " RecordId " + recordId);
				editedDataValue = categoryManager.getRecordById(rootCatEntity, recordId);
				mapGenerator.validateRetrievedDataValueMap(editedDataValue, dataValue);
			}
			catch (Exception e)
			{
				System.out.println("Record Insertion failed for Category " + category.getName());
				failedCatVsException.put(category, e);
			}
		}
		printFailedCategoryReport(failedCatVsException, "Record Insertion failed for Category ");

	}

	/**
	 * This test case will try to insert the data for Test Category_Lab Information category.
	 */
	public void testInsertDataForAllCategories()
	{
		Map<CategoryInterface, Exception> failedCatVsException = new HashMap<CategoryInterface, Exception>();
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();
		List<CategoryInterface> categoryList = getAllCategories();
		for (CategoryInterface category : categoryList)
		{
			try
			{
				System.out.println("Inserting record for " + category.getName());
				Map<BaseAbstractAttributeInterface, Object> dataValue;
				CategoryEntityInterface rootCatEntity = category.getRootCategoryElement();
				dataValue = mapGenerator.createDataValueMapForCategory(rootCatEntity, 0);
				long recordId = categoryManager.insertData(category, dataValue);
				System.out.println("Record inserted succesfully for " + category.getName()
						+ " RecordId " + recordId);
			}
			catch (Exception e)
			{
				System.out.println("Record Insertion failed for Category " + category.getName());
				failedCatVsException.put(category, e);
			}
		}
		printFailedCategoryReport(failedCatVsException, "Record Insertion failed for Category ");
	}

	private void printFailedCategoryReport(Map<CategoryInterface, Exception> failedCatVsException,
			String message)
	{
		for (Entry<CategoryInterface, Exception> entryObject : failedCatVsException.entrySet())
		{
			CategoryInterface category = entryObject.getKey();
			Exception exception = entryObject.getValue();
			System.out.println(message + category.getName());
			System.out.println("Exception :");
			exception.printStackTrace();
		}
		if (!failedCatVsException.isEmpty())
		{
			fail();
		}

	}

	/**
	 * This test case will try to generate the html for each of the category in present in the DB in Edit mode.
	 * Test case is failed if the exception is occured in generating the html for any of the Category.
	 *
	 */
	public void testGenerateHtmlForContainerInEditMode()
	{
		Map<CategoryInterface, Exception> failedCatVsException = new HashMap<CategoryInterface, Exception>();

		List<CategoryInterface> categoryList = getAllCategories();
		for (CategoryInterface category : categoryList)
		{
			try
			{
				for (Object container : category.getRootCategoryElement().getContainerCollection())
				{
					((ContainerInterface) container).generateContainerHTML(category.getName(),
							WebUIManagerConstants.EDIT_MODE);
				}
			}
			catch (Exception e)
			{
				System.out.println("Html generation failed for Category " + category.getName());
				failedCatVsException.put(category, e);
			}
		}
		printFailedCategoryReport(failedCatVsException, "Html generation failed for Category ");

	}

	/**
	 * This test case will try to generate the html for each of the category in present in the DB in view mode.
	 * Test case is failed if the exception is occured in generating the html for any of the Category.
	 *
	 */
	public void testGenerateHtmlForContainerInViewMode()
	{
		Map<CategoryInterface, Exception> failedCatVsException = new HashMap<CategoryInterface, Exception>();
		List<CategoryInterface> categoryList = getAllCategories();
		for (CategoryInterface category : categoryList)
		{
			try
			{

				for (Object container : category.getRootCategoryElement().getContainerCollection())
				{
					((ContainerInterface) container).generateContainerHTML(category.getName(),
							WebUIManagerConstants.VIEW_MODE);
				}
			}
			catch (Exception e)
			{
				System.out.println("Html generation failed for Category " + category.getName());
				failedCatVsException.put(category, e);
			}
		}
		printFailedCategoryReport(failedCatVsException, "Html generation failed for Category ");
	}

	/**
	 * This test case will try to validate the data for all categories present in DB.
	 */
	public void testValidateDataForAllCategories()
	{
		Map<CategoryInterface, Exception> failedCatVsException = new HashMap<CategoryInterface, Exception>();
		boolean isValidationFailed = false;
		List<CategoryInterface> categoryList = getAllCategories();
		for (CategoryInterface category : categoryList)
		{
			try
			{

				System.out.println("Validating record for " + category.getName());
				Map<BaseAbstractAttributeInterface, Object> dataValue;
				CategoryEntityInterface rootCatEntity = category.getRootCategoryElement();
				dataValue = mapGenerator.createDataValueMapForCategory(rootCatEntity, 0);
				List<String> errorList = new ArrayList<String>();
				ValidatorUtil.validateEntity(dataValue, errorList,
						(ContainerInterface) rootCatEntity.getContainerCollection().iterator()
								.next(), true);
				if (errorList.isEmpty())
				{
					System.out.println("Record validated succesfully for category "
							+ category.getName());
				}
				else
				{
					System.out.println("Record validation failed for category "
							+ category.getName());
					for (String error : errorList)
					{
						System.out.println("error --> " + error);
					}
					isValidationFailed = true;
				}
			}
			catch (Exception e)
			{
				System.out.println("Record validation failed for category " + category.getName());
				failedCatVsException.put(category, e);
			}
		}
		printFailedCategoryReport(failedCatVsException, "Record validation failed for category ");
		if (isValidationFailed)
		{
			fail("Record validation failed for category");
		}
	}

	/**
	 * This method will retrieve the category with the given name from DB.
	 * @param name name of the category
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	private CategoryInterface retriveCategoryByName(String name)
			throws DynamicExtensionsSystemException, DAOException
	{
		List<CategoryInterface> categoryList = getAllCategories();
		CategoryInterface categoryByName = null;
		for (CategoryInterface category : categoryList)
		{
			if (category.getName().equals(name))
			{
				categoryByName = category;
				break;
			}
		}
		if (categoryByName == null)
		{
			throw new DynamicExtensionsSystemException("Category with name " + name + " not found");
		}
		return categoryByName;
	}

	/**
	 * This method will retrieve all the categories in the DB.
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	private List<CategoryInterface> getAllCategories()
	{
		HibernateDAO hibernateDAO;
		List<CategoryInterface> categoryList = null;
		try
		{
			hibernateDAO = DynamicExtensionsUtility.getHibernateDAO();

			categoryList = hibernateDAO.retrieve(CategoryInterface.class.getName());
			DynamicExtensionsUtility.closeDAO(hibernateDAO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
		return categoryList;
	}

	/*public void testInsertDataForAllCategoriesForBO()
	{
		Map<CategoryInterface, Exception> failedCatVsException = new HashMap<CategoryInterface, Exception>();
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();
		List<CategoryInterface> categoryList = getAllCategories();
		for (CategoryInterface category : categoryList)
		{
			try
			{
				System.out.println("testInsertDataForAllCategoriesForBO:Inserting record for "
						+ category.getName());
				Map<Long, Object> dataValue;
				CategoryEntityInterface rootCatEntity = category.getRootCategoryElement();
				dataValue = mapGenerator.createIdToValueMapForCategory(rootCatEntity, 0);
				long recordId = categoryManager.insertData(category, dataValue);
				System.out
						.println("testInsertDataForAllCategoriesForBO: Record inserted succesfully for "
								+ category.getName() + " RecordId " + recordId);
			}
			catch (Exception e)
			{
				System.out
						.println("testInsertDataForAllCategoriesForBO: Record Insertion failed for Category "
								+ category.getName());
				failedCatVsException.put(category, e);
			}
		}
		printFailedCategoryReport(failedCatVsException,
				"testInsertDataForAllCategoriesForBO: Record Insertion failed for Category ");
	}*/

}