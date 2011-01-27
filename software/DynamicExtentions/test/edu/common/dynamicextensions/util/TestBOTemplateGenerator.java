/**
 *
 */

package edu.common.dynamicextensions.util;

import java.io.File;

import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author shrishail_kalshetty
 * This class tests the BO template files get successfully generated.
 *
 */
public class TestBOTemplateGenerator extends DynamicExtensionsBaseTestCase
{

	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(TestBOTemplateGenerator.class);

	/**
	 * Generate XML and CSV template for category object.
	 */
	public void testGenerateXMLAndCSVTemplate()
	{
		try
		{
			String filePath = System.getProperty("user.dir") + File.separator + "src"
					+ File.separator + "resources" + File.separator + "xml";
			CategoryInterface category = CategoryManager.getInstance().getAllCategories()
					.iterator().next();
			if (category == null)
			{
				LOGGER.info("testGenerateXMLAndCSVTemplate() failed.");
				fail();
			}
			else
			{
				BOTemplateGenerator boTemplateGenerator = new BOTemplateGenerator(category);
				boTemplateGenerator.generateXMLAndCSVTemplate(System.getProperty("user.dir"),
						filePath + File.separator + "Participant.xml", filePath + File.separator
								+ "mapping.xml");
				LOGGER.info("testGenerateXMLAndCSVTemplate() executed successfully.");
			}
			deleteFiles(filePath);
		}
		catch (BulkOperationException boException)
		{
			LOGGER.error("BulkOperation Exception: ", boException);
			boException.printStackTrace();
			fail();
		}
		catch (DynamicExtensionsSystemException deException)
		{
			LOGGER.error("DE System Exception: ", deException);
			deException.printStackTrace();
			fail();
		}
		catch (DynamicExtensionsApplicationException deAppException)
		{
			LOGGER.error("DE System Exception: ", deAppException);
			deAppException.printStackTrace();
			fail();
		}
	}

	/**
	 * @param filePath Delete Files created.
	 */
	private void deleteFiles(String filePath)
	{
		File file = new File(filePath);
		if (file.isDirectory() && file.exists())
		{
			for (String fileName : file.list())
			{
				LOGGER.info("File " + fileName + " Deleted: " + new File(fileName).delete());
			}
		}
		LOGGER.info("Directory " + file.getName() + " Deleted: " + file.delete());
	}
}
