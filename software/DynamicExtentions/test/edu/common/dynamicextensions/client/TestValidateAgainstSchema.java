
package edu.common.dynamicextensions.client;

import java.io.File;

import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.util.logger.Logger;

public class TestValidateAgainstSchema extends DynamicExtensionsBaseTestCase
{

	private static final Logger LOGGER = Logger.getCommonLogger(TestValidateAgainstSchema.class);

	public void testValidateSchema()
	{
		boolean actual = true;
		boolean expected = false;
		try
		{
			SchemaValidator schemaValidator = new SchemaValidator();
			//			String schemaName = "E:/testcaseFile/category_schema.xsd";
			//			String xmlName = "E:/testcaseFile/Patient.xml";

			String schemaName = "src/conf/category_schema.xsd";
			String xmlName = "src/resources/xml/Patient.xml";

			File schemaFile = new File(schemaName);
			File xmlFile = new File(xmlName);
			schemaValidator.validateAgainstSchema(schemaFile, xmlFile);
			expected = true;
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			fail();
		}
		assertEquals(expected, actual);
	}

}
