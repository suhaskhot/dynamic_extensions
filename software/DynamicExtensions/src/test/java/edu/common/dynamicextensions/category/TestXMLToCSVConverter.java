
package edu.common.dynamicextensions.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.category.beans.UIProperty;
import edu.common.dynamicextensions.category.enums.ControlEnum;
import edu.common.dynamicextensions.client.XMLToCSV;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.userinterface.ListBox;
import edu.common.dynamicextensions.domain.userinterface.TextArea;
import edu.common.dynamicextensions.domain.userinterface.TextField;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.util.logger.Logger;

/**
 * Test xml to csv conversion
 * @author rajesh_vyas
 *
 */
public class TestXMLToCSVConverter extends DynamicExtensionsBaseTestCase
{

	private static final Logger LOGGER = Logger.getCommonLogger(TestXMLToCSVConverter.class);

	/**
	 *
	 */
	public void testTxXML()
	{
		XMLToCSV xmlToCSV = new XMLToCSV();
		try
		{
			//						String inputFileName = "E:/testcaseFile/Patient.xml";
			//						String testFileName = "E:/testcaseFile/test_Patient.csv";

			String outputDirString = System.getProperty("java.io.tmpdir");
			String inputFileName = "src/resources/xml/Patient.xml";
			String testFileName = "src/resources/csv/test_Patient.csv";
			File outputDir = new File(outputDirString);

			xmlToCSV.main(new String[]{inputFileName});
			xmlToCSV.main(new String[]{inputFileName, ""});
			xmlToCSV.main(new String[]{"", ""});
			xmlToCSV.main(new String[]{inputFileName, outputDirString, outputDirString});
			xmlToCSV.main(new String[]{inputFileName, outputDirString});
			xmlToCSV.main(new String[]{inputFileName, outputDirString});
			xmlToCSV.main(new String[]{"inputFileDirName", outputDirString});

			System.out.println("");
			File inputXMLFile = new File(inputFileName);
			System.out.println("Input file: " + inputXMLFile.getAbsolutePath());
			System.out.println(outputDir);
			xmlToCSV.convertXMLs(inputXMLFile, outputDir, null);
			File cSVFile = new File(testFileName);
			String expected = readXML(cSVFile);
			String actual = readXML(new File(outputDir, "Patient.csv"));
			LOGGER.info("Expected Result:" + expected);
			LOGGER.info("Actual Result:" + actual);
			assertEquals(expected, actual);
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			fail();
		}
	}

	/**
	 * @param expectedFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String readXML(File expectedFile) throws FileNotFoundException, IOException
	{
		String textString = null;
		BufferedReader xmlBufferedReader = null;
		try
		{
			xmlBufferedReader = new BufferedReader(new FileReader(expectedFile));
			StringBuilder stringBuilder = new StringBuilder();
			char[] cbuf = new char[1024];
			int read = xmlBufferedReader.read(cbuf);

			while (read != -1)
			{
				stringBuilder.append(cbuf);
				read = xmlBufferedReader.read(cbuf);
			}
			textString = stringBuilder.toString();
		}
		catch (Exception ex)
		{
			System.out.println("" + ex.getCause().toString());
		}
		finally
		{
			close(xmlBufferedReader);
		}

		return textString;
	}

	private void close(Reader reader) throws IOException
	{
		if (reader != null)
		{
			reader.close();
		}
	}

	/**
	 * Test category enums for radio button.
	 */
	public void testCategoryEnumsForRadioButton()
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		ControlInterface control = factory.createRadioButton();
		AttributeInterface booleanAttribute = factory.createBooleanAttribute();
		booleanAttribute.getAttributeTypeInformation().setDataElement(factory.createUserDefinedDE());
		booleanAttribute.setName("XYZ");
		control.setBaseAbstractAttribute(booleanAttribute);
		Collection<UIProperty> controlUIProperties = control.getControlTypeValues();
		assertNotNull(controlUIProperties);
		control.setControlTypeValues(controlUIProperties);
		assertTrue(true);
	}

	/**
	 * Test category enums for text field.
	 */
	public void testCategoryEnumsForTextField()
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		ControlInterface control = factory.createTextField();
		AttributeInterface stringAttribute = factory.createStringAttribute();
		stringAttribute.setRuleCollection(new HashSet<RuleInterface>());
		CategoryAttributeInterface categoryAttribute = factory.createCategoryAttribute();
		categoryAttribute.setName("XYZ Category Attr");
		categoryAttribute.setAbstractAttribute(stringAttribute);
		stringAttribute.setName("XYZ");
		control.setBaseAbstractAttribute(categoryAttribute);
		((TextField)control).setColumns(Integer.valueOf("5"));
		((TextField)control).setIsPassword(true);
		((TextField)control).setIsUrl(true);
		Collection<UIProperty> controlUIProperties = control.getControlTypeValues();
		assertNotNull(controlUIProperties);
		control.setControlTypeValues(controlUIProperties);
		assertTrue(true);
	}

	/**
	 * Test category enums for text area.
	 */
	public void testCategoryEnumsForTextArea()
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		ControlInterface control = factory.createTextArea();
		AttributeInterface stringAttribute = factory.createStringAttribute();
		stringAttribute.setRuleCollection(new HashSet<RuleInterface>());
		CategoryAttributeInterface categoryAttribute = factory.createCategoryAttribute();
		categoryAttribute.setName("XYZ Category Attr");
		categoryAttribute.setAbstractAttribute(stringAttribute);
		stringAttribute.setName("XYZ");
		control.setBaseAbstractAttribute(categoryAttribute);
		((TextArea)control).setColumns(Integer.valueOf("5"));
		((TextArea)control).setRows(Integer.valueOf("5"));
		Collection<UIProperty> controlUIProperties = control.getControlTypeValues();
		assertNotNull(controlUIProperties);
		control.setControlTypeValues(controlUIProperties);
		assertTrue(true);
	}

	/**
	 * Test category enums for combobox.
	 */
	public void testCategoryEnumsForCombobox()
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		ControlInterface control = factory.createComboBox();
		AttributeInterface stringAttribute = factory.createStringAttribute();
		stringAttribute.getAttributeTypeInformation().setDataElement(factory.createUserDefinedDE());
		stringAttribute.setRuleCollection(new HashSet<RuleInterface>());
		stringAttribute.setName("XYZ");
		control.setBaseAbstractAttribute(stringAttribute);
		Collection<UIProperty> controlUIProperties = control.getControlTypeValues();
		assertNotNull(controlUIProperties);
		control.setControlTypeValues(controlUIProperties);
		assertTrue(true);
	}

	/**
	 * Test category enums for listbox.
	 */
	public void testCategoryEnumsForListbox()
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		ControlInterface control = factory.createListBox();
		AttributeInterface stringAttribute = factory.createStringAttribute();
		stringAttribute.getAttributeTypeInformation().setDataElement(factory.createUserDefinedDE());
		stringAttribute.setRuleCollection(new HashSet<RuleInterface>());
		stringAttribute.setName("XYZ");
		control.setBaseAbstractAttribute(stringAttribute);
		((ListBox)control).setIsMultiSelect(true);
		((ListBox)control).setIsUsingAutoCompleteDropdown(true);
		Collection<UIProperty> controlUIProperties = control.getControlTypeValues();
		assertNotNull(controlUIProperties);
		control.setControlTypeValues(controlUIProperties);
		assertTrue(true);
	}

	/**
	 * Test category enums for date picker.
	 */
	public void testCategoryEnumsForDatePicker()
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		ControlInterface control = factory.createDatePicker();
		AttributeInterface dateAttribute = factory.createDateAttribute();
		dateAttribute.setRuleCollection(new HashSet<RuleInterface>());
		dateAttribute.setName("XYZ");
		control.setBaseAbstractAttribute(dateAttribute);
		Collection<UIProperty> controlUIProperties = control.getControlTypeValues();
		assertNotNull(controlUIProperties);
		control.setControlTypeValues(controlUIProperties);
		assertTrue(true);
	}
}
