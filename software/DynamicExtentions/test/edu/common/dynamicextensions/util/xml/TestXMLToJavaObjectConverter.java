
package edu.common.dynamicextensions.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.util.xml.PvVersion.XmlCategory;
import edu.wustl.common.util.logger.Logger;

public class TestXMLToJavaObjectConverter extends TestCase
{

	private static final Logger LOGGER = Logger.getCommonLogger(TestXMLToJavaObjectConverter.class);
	public static final String CATEGORY_NAME = "Test Category_Lab Information";

	public void testXMLToObjectConverter() throws SAXException
	{
		try
		{
			XMLToObjectConverter converter = new XMLToObjectConverter(PvVersion.class.getPackage()
					.getName(), null);
			File file = new File("src/resources/xml/TestPvVersion.xml");
			FileInputStream fileInputStream = new FileInputStream(file);
			PvVersion version = (PvVersion) converter.getJavaObject(fileInputStream);
			final XmlCategory xmlCategory = version.getXmlCategory();
			//Category category = categoryList.get(0);
			LOGGER.info("Category name: " + xmlCategory.getName());
			assertEquals(CATEGORY_NAME, xmlCategory.getName());
		}
		catch (JAXBException e)
		{
			LOGGER.error(e.getMessage());
			fail();
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error(e.getMessage());
			fail();
		}
	}
}
