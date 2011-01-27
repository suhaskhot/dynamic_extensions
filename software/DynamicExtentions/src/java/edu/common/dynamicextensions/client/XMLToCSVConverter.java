
package edu.common.dynamicextensions.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * The Class XMLToCSVConverter.
 * @author rajesh_vyas
 */
public class XMLToCSVConverter
{

	/** The Constant SHOW2. */
	private static final String SHOW2 = "show";

	/** The Constant DISPLAY_LABEL. */
	private static final String DISPLAY_LABEL = "Display_Label:";

	/** The Constant PERMISSIBLE_VALUE_FILE. */
	private static final String PERMISSIBLE_VALUE_FILE = "permissibleValueFile";

	/** The Constant SKIP_LOGIC_ATTRIBUTE. */
	// private static final String SKIP_LOGIC_ATTRIBUTE = "SkipLogicAttribute";

	/** The Constant DEPENDENT_ATTRIBUTE. */
	private static final String DEPENDENT_ATTRIBUTE = "DependentAttribute";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(XMLToCSVConverter.class.getName());

	/** The Constant CAPTION2. */
	private static final String CAPTION2 = "caption";

	/** The Constant INSTANCE. */
	private static final String INSTANCE = "Instance";

	/** The Constant INSTANCES. */
	private static final String INSTANCES = "Instances";

	/** The Constant VALUE. */
	private static final String VALUE = "value";

	/** The Constant KEY. */
	private static final String KEY = "key";

	/** The Constant UI_PROPERTY. */
	private static final String UI_PROPERTY = "Property";

	/** The Constant UI_CONTROL. */
	private static final String UI_CONTROL = "uiControl";

	/** The Constant ATTRIBUTE_NAME. */
	private static final String ATTRIBUTE_NAME = "attributeName";

	/** The Constant CLASS_NAME. */
	private static final String CLASS_NAME = "className";

	/** The Constant SINGLE_LINE_DISPLAY. */
	private static final String SINGLE_LINE_DISPLAY = "SingleLineDisplay";

	/** The Constant ATTRIBUTE. */
	private static final String ATTRIBUTE = "Attribute";

	/** The Constant FORM_NAME. */
	private static final String FORM_NAME = "name";

	/** The Constant SKIP_LOGIC. */
	private static final String SKIP_LOGIC = "SkipLogic";

	/** The Constant SKIP_LOGIC ATTRIBUTE. */
	private static final String SKIP_LOGIC_ATTRIBUTE = "SkipLogicAttribute";

	/** The Constant RELATED_ATTRIBUTE. */
	private static final String RELATED_ATTRIBUTE = "RelatedAttribute";

	/** The Constant FORM. */
	private static final String FORM = "Form";

	/** The Constant ENTITY_GROUP_NAME. */
	private static final String ENTITY_GROUP_NAME = "entityGroup";

	/** The Constant FORM_DEFINITION_NAME. */
	private static final String FORM_DEFINITION_NAME = FORM_NAME;

	/** The Constant FORM_DEFINITION. */
	private static final String FORM_DEFINITION = "FormDefinition";

	/** The Constant for Header. */
	private static final String HEADING = "Heading";

	/** The Constant for Notes. */
	private static final String NOTES = "Notes";

	/** The document. */
	private transient Document document;

	/** The writer. */
	private final transient Writer writer;

	/** The input source. */
	private final transient InputSource inputSource;

	/** The new line. */
	private final transient String newLine = System.getProperty("line.separator");

	/** The string builder. */
	private final transient StringBuilder stringBuilder;

	/** The rules required string. */
	private transient String rulesString;

	/** The rules required string. */
	private transient String defaultValueString;

	/** The entity group name. */
	private transient String entityGroupName;

	/** The permissible Value Options String */
	private transient String permValueOptionsString;

	/** The First UI Property*/
	private transient boolean isFirstUIProperty;

	/**The Second UI Property*/
	private transient boolean isSecondUIProperty;

	/** The input Directory*/
	private final transient String inputDir;

	/** The output Directory*/
	private transient String outputDir;

	/** This List used to store the form related information.*/
	private final transient List<StringBuffer> strBufferList = new ArrayList<StringBuffer>();

	/** Sub Form constant. */
	private static final String SUB_FORM = "Subform";

	/** This list is used to store the instance information*/
	private final transient List<String> instanceList = new ArrayList<String>();

	/**This list is used to store the skip logic information */
	private final transient List<String> skipLogicList = new ArrayList<String>();

	/** Determines whether skip logic*/
	private transient boolean skipLogicAdded = false;

	/** Constant for arrow operator.*/
	private static final String ARROW_OPERATOR = "->";

	/** Constant for show/hide paste button**/
	private static final String SHOW_PASTE = "showPaste";

	/**
	 * Instantiates a new xML to csv converter.
	 * @param xmlFile  the xml file
	 * @param csvFile  the csv file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XMLToCSVConverter(final File xmlFile, final File csvFile) throws IOException
	{
		LOGGER.info("XML file:" + xmlFile.getAbsolutePath());
		LOGGER.info("CSV file:" + csvFile.getAbsolutePath());
		writer = new BufferedWriter(new FileWriter(csvFile));
		inputSource = new InputSource(new FileReader(xmlFile));
		stringBuilder = new StringBuilder();
		inputDir = xmlFile.getParent();
		outputDir = csvFile.getParent();
	}

	/**
	 * Tx xml.
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void txXML() throws SAXException, IOException
	{
		final DOMParser domParser = new DOMParser();
		try
		{
			domParser.parse(inputSource);
			document = domParser.getDocument();
			txFormDefinition();
			writer.write(stringBuilder.toString());
		}
		finally
		{
			writer.close();
		}
	}

	/**
	 * Append to string builder.
	 * @param stringToBeAppend the string to be append
	 */
	private void appendToStringBuilder(final String stringToBeAppend)
	{
		LOGGER.debug("Appending: " + stringToBeAppend);
		stringBuilder.append(stringToBeAppend);
	}

	/**
	 * Tx form definition.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txFormDefinition() throws IOException
	{
		final NodeList formDefinitionTag = document.getElementsByTagName(FORM_DEFINITION);
		final int length = formDefinitionTag.getLength();

		for (int i = 0; i < length; i++)
		{
			final Node item = formDefinitionTag.item(i);
			appendToStringBuilder("Form_Definition" + newLine + newLine);
			final NamedNodeMap formDefinitionProperties = item.getAttributes();

			final Node formDefinitionName = formDefinitionProperties
					.getNamedItem(FORM_DEFINITION_NAME);
			appendToStringBuilder(formDefinitionName.getNodeValue() + newLine + newLine);

			final Node entityGroupNameNode = formDefinitionProperties
					.getNamedItem(ENTITY_GROUP_NAME);
			entityGroupName = entityGroupNameNode.getNodeValue();
			appendToStringBuilder(entityGroupName + newLine + newLine);

			txFormDefinitionChildren(item);
		}
	}

	/**
	 * Tx form definition children.
	 * @param item the item
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txFormDefinitionChildren(final Node item) throws IOException
	{
		final NodeList childNodes = item.getChildNodes();
		final int length2 = childNodes.getLength();

		for (int j = 0; j < length2; j++)
		{
			final Node item2 = childNodes.item(j);
			final String nodeName = item2.getNodeName();
			if (nodeName.equals(FORM))
			{
				txForm(item2, false);
				appendListToStringBuilder();
			}
			else if (nodeName.equals(RELATED_ATTRIBUTE))
			{
				txRelatedAttributes(item2);
			}
		}
	}

	/**
	 * Tx skiplogic.
	 * @param item  the item
	 * @param isSubform
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String txSkiplogic(final Node item, final boolean isSubform) throws IOException
	{
		final StringBuffer strBuffer = new StringBuffer();
		if (!skipLogicAdded)
		{
			strBuffer.append(SKIP_LOGIC_ATTRIBUTE + ":" + newLine);
			skipLogicAdded = true;
		}

		final NodeList controllingAttributes = item.getChildNodes();
		final int length = controllingAttributes.getLength();
		for (int i = 0; i < length; i++)
		{
			final Node controllingAttributeNode = controllingAttributes.item(i);
			processControllingAttribute(controllingAttributeNode, strBuffer, isSubform);
		}
		return strBuffer.toString();
	}

	/**
	 * Process controlling attribute.
	 * @param controllingAttributeNode the controlling attribute node
	 * @param strBuffer
	 * @param isSubform
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void processControllingAttribute(final Node controllingAttributeNode,
			final StringBuffer strBuffer, final boolean isSubform) throws IOException
	{
		final NodeList childNodes = controllingAttributeNode.getChildNodes();
		String txInstance = null;
		final int length = childNodes.getLength();

		Node instance = null;
		Node dependentNode = null;
		for (int i = 0; i < length; i++)
		{
			final Node item = childNodes.item(i);
			if (item.getNodeName().equals(INSTANCE))
			{
				instance = item;
			}
			else if (item.getNodeName().equals(DEPENDENT_ATTRIBUTE))
			{
				dependentNode = item;
			}
		}

		for (int i = 0; i < length; i++)
		{
			final Node item = childNodes.item(i);
			if (item.getNodeName().equals(ATTRIBUTE))
			{
				strBuffer.append(newLine);
				strBuffer.append("instance:");
				txInstance = txInstance(instance, isSubform, strBuffer);
				strBuffer.append(',');
				strBuffer.append(txInstance);
				strBuffer.append(newLine);
				strBuffer.append(txInstance).append(':');
				txSkipLogicAttribute(item, strBuffer);
				strBuffer.append(",dependentAttribute~");
				txSkipLogicDependent(dependentNode, strBuffer, isSubform);
				strBuffer.append(newLine);
			}
		}
	}

	/**
	 * Tx skip logic dependent.
	 * @param dependentAttributeNode the dependent attribute node
	 * @param stringBuffer
	 * @param isSubform
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txSkipLogicDependent(final Node dependentAttributeNode,
			final StringBuffer stringBuffer, final boolean isSubform) throws IOException
	{
		final NodeList childNodes = dependentAttributeNode.getChildNodes();

		final int length = childNodes.getLength();
		for (int i = 0; i < length; i++)
		{
			final Node item = childNodes.item(i);
			if (item.getNodeName().equals(INSTANCE))
			{
				txInstance(item, isSubform, stringBuffer);
			}
			else if (item.getNodeName().equals(ATTRIBUTE))
			{
				txSkipLogicDependentAttribute(item, stringBuffer);
			}
		}
	}

	/**
	 * Tx skip logic dependent attribute.
	 * @param item the item
	 * @param stringBuffer
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txSkipLogicDependentAttribute(final Node item, final StringBuffer stringBuffer)
			throws IOException
	{
		final String TRUE = "true";
		final NamedNodeMap attributes = item.getAttributes();
		final Node attributeName = attributes.getNamedItem("attributeName");
		stringBuffer.append(':').append(attributeName.getNodeValue());

		final Node attributeName1 = attributes.getNamedItem("isSelectiveReadOnly");
		final Node attributeName2 = attributes.getNamedItem("isShowHide");

		if (attributeName1.getNodeValue().equals(TRUE)
				|| attributeName2.getNodeValue().equals(TRUE))
		{
			stringBuffer.append(",options~");

			if (attributeName1.getNodeValue().equals(TRUE))
			{
				stringBuffer.append("IsSelectiveReadOnly=").append(attributeName1.getNodeValue());
			}
			else if (attributeName2.getNodeValue().equals(TRUE))
			{
				stringBuffer.append("IsShowHide=").append(attributeName2.getNodeValue());
			}
		}

		final Node subset = getSubset(item);

		if (subset != null)
		{
			final NamedNodeMap attributes2 = subset.getAttributes();
			final Node permissibleValueFile = attributes2.getNamedItem(PERMISSIBLE_VALUE_FILE);
			stringBuffer.append(",Permissible_Values_File~");
			outputDir = outputDir == null ? "" : outputDir + File.separator;
			stringBuffer.append(outputDir);
			readPermissibleValues(permissibleValueFile.getNodeValue());
			stringBuffer.append(permissibleValueFile.getNodeValue());
		}
	}

	/**
	 * Read permissible values.
	 * @param nodeValue the node value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readPermissibleValues(final String nodeValue)
			throws IOException
	{
		final BufferedReader bufferedReader = new BufferedReader(new FileReader(inputDir
				+ File.separator + nodeValue));
		final FileWriter pvWriter = new FileWriter(outputDir + File.separator + nodeValue);
		try
		{
			String readLine = bufferedReader.readLine();
			while (readLine != null)
			{
				pvWriter.write(readLine + newLine);
				readLine = bufferedReader.readLine();
			}
		}
		finally
		{
			bufferedReader.close();
			pvWriter.close();
		}
	}

	/**
	 * Gets the subset.
	 * @param item the item
	 * @return the subset
	 */
	private Node getSubset(final Node item)
	{
		Node subset = null;
		final NodeList childNodes = item.getChildNodes();
		final int length = childNodes.getLength();

		for (int i = 0; i < length; i++)
		{
			final Node item2 = childNodes.item(i);
			if (item2.getNodeName().equals("subset"))
			{
				subset = item2;
			}
		}
		return subset;
	}

	/**
	 * Tx skip logic attribute.
	 * @param skipLogicAttribute the skip logic attribute
	 * @param strBuffer
	 * @param strBuffer
	 */
	private void txSkipLogicAttribute(final Node skipLogicAttribute, final StringBuffer strBuffer)
	{
		final NamedNodeMap attributes = skipLogicAttribute.getAttributes();
		final Node attributeName = attributes.getNamedItem(ATTRIBUTE_NAME);
		final Node value = attributes.getNamedItem(VALUE);
		strBuffer.append(attributeName.getNodeValue()).append(':').append(value.getNodeValue());
	}

	/**
	 * Tx form.
	 * @param item  the item
	 * @param isSubform
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txForm(final Node item, final boolean isSubform) throws IOException
	{
		boolean containsSubform = false;
		final StringBuffer strBuffer = new StringBuffer();
		final NamedNodeMap formProperties = item.getAttributes();

		final Node formName = formProperties.getNamedItem(FORM_NAME);
		final Node show = formProperties.getNamedItem(SHOW2);

		strBuffer.append(DISPLAY_LABEL + formName.getNodeValue() + ", show=" + show.getNodeValue()
				+ newLine);

		final NodeList childNodes = item.getChildNodes();

		final int length = childNodes.getLength();

		for (int i = 0; i < length; i++)
		{
			final Node item2 = childNodes.item(i);
			final String nodeName = item2.getNodeName();

			if (nodeName.equals(INSTANCES))
			{
				txInstances(item2, isSubform, strBuffer);
			}
			else if (nodeName.equals(ATTRIBUTE))
			{
				txAttribute(item2, strBuffer);
			}
			else if (nodeName.equals(SINGLE_LINE_DISPLAY))
			{
				txSingleLineDisplay(item2, isSubform, strBuffer);
			}
			else if (SUB_FORM.equals(nodeName))
			{
				containsSubform = true;
				strBuffer.append("subcategory:").append(
						item2.getAttributes().getNamedItem(FORM_NAME).getNodeValue()).append(
						":multiline:").append(SHOW_PASTE).append('=').append(
						item2.getAttributes().getNamedItem(SHOW_PASTE).getNodeValue()).append(
						newLine);
				txForm(item2, true);
			}
			else if (nodeName.equals(SKIP_LOGIC))
			{
				skipLogicList.add(txSkiplogic(item2, isSubform));
			}
			if (!containsSubform && i == length - 1 && instanceList.size() - 1 > 0)
			{
				instanceList.remove(instanceList.size() - 1);
			}
		}
		strBuffer.append(newLine);
		strBufferList.add(strBuffer);
	}

	/**
	 * Tx single line display.
	 * @param item  the item
	 * @param isSubform
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txSingleLineDisplay(final Node item, final boolean isSubform,
			final StringBuffer strBuffer) throws IOException
	{
		final NodeList childNodes = item.getChildNodes();
		final int length = childNodes.getLength();
		if (!isSubform)
		{
			strBuffer.append("SingleLineDisplay:start").append(newLine);
		}
		for (int i = 0; i < length; i++)
		{
			final Node item2 = childNodes.item(i);

			final String nodeName = item2.getNodeName();
			if (nodeName.equals(ATTRIBUTE))
			{
				txAttribute(item2, strBuffer);
			}
		}
		if (!isSubform)
		{
			strBuffer.append("SingleLineDisplay:end").append(newLine);
		}
	}

	/**
	 * Tx instances.
	 * @param item2  the item2
	 * @param isSubform
	 * @param strBuffer
	 * @throws IOException  Signals that an I/O exception has occurred.
	 */
	private void txInstances(final Node item2, final boolean isSubform, final StringBuffer strBuffer)
			throws IOException
	{
		strBuffer.append("instance:");

		final NodeList childNodes = item2.getChildNodes();
		final int length = childNodes.getLength();

		for (int i = 0; i < length; i++)
		{
			final Node item = childNodes.item(i);
			final String nodeName = item.getNodeName();

			if (nodeName.equals(INSTANCE))
			{
				txInstance(item, isSubform, strBuffer);
				strBuffer.append(newLine);
			}
		}
	}

	/**
	 * Tx instance.
	 * @param item2 the item2
	 * @param isSubform
	 * @param strBuffer
	 * @return the string
	 * @throws IOException  Signals that an I/O exception has occurred.
	 */
	private String txInstance(final Node item2, final boolean isSubform,
			final StringBuffer strBuffer) throws IOException
	{
		final StringBuffer instance = new StringBuffer();
		if (isSubform && !instanceList.isEmpty())
		{
			for (int index = instanceList.size() - 1; index >= 0; index--)
			{
				instance.insert(0, instanceList.get(index) + ARROW_OPERATOR);
			}
		}
		if (!instance.toString().contains(item2.getFirstChild().getNodeValue()))
		{
			instance.append(item2.getFirstChild().getNodeValue());
			instanceList.add(item2.getFirstChild().getNodeValue());
		}
		if (instance.toString().endsWith(ARROW_OPERATOR))
		{
			instance.replace(0, instance.length(), instance.substring(0, instance
					.lastIndexOf(ARROW_OPERATOR)));
		}
		strBuffer.append(instance);
		return instance.toString();
	}

	/**
	 * Tx attribute.
	 * @param item the item
	 * @param strBuffer
	 * @throws IOException  Signals that an I/O exception has occurred.
	 */
	private void txAttribute(final Node item, final StringBuffer strBuffer) throws IOException
	{

		// boolean isFirstUIProperty = false;
		final NodeList childNodes = item.getChildNodes();

		final int length = childNodes.getLength();

		for (int i = 0; i < length; i++)
		{
			final Node item1 = childNodes.item(i);
			final String nodeName = item1.getNodeName();

			if (nodeName.equals(HEADING))
			{
				appendHeading(item1, strBuffer);
			}
			if (nodeName.equals(NOTES))
			{
				appendNotes(item1, strBuffer);
			}
		}
		final NamedNodeMap controlProperties = item.getAttributes();

		final Node className = controlProperties.getNamedItem(CLASS_NAME);
		strBuffer.append(className.getNodeValue()).append(':');

		final Node attributeName = controlProperties.getNamedItem(ATTRIBUTE_NAME);
		strBuffer.append(attributeName.getNodeValue()).append(',');

		final Node uiControlName = controlProperties.getNamedItem(UI_CONTROL);
		strBuffer.append(uiControlName.getNodeValue()).append(',');

		final Node controlCaption = controlProperties.getNamedItem(CAPTION2);
		strBuffer.append('\"').append(controlCaption.getNodeValue()).append('\"');

		for (int j = 0; j < length; j++)
		{
			final Node item2 = childNodes.item(j);
			final String nodeName = item2.getNodeName();

			appendUIProperty(item2, nodeName, strBuffer);
		}
		// isFirstUIProperty = false;
		isSecondUIProperty = false;
		appendSeparators(strBuffer);
		strBuffer.append(newLine);
	}

	/**
	 * Append Notes.
	 * @param item Node object.
	 * @param strBuffer
	 * @throws IOException  Signals that an I/O exception has occurred.
	 */
	private void appendNotes(final Node item, final StringBuffer strBuffer) throws IOException
	{

		final NodeList childNodes = item.getChildNodes();

		final int childNodesLength = childNodes.getLength();

		for (int i = 0; i < childNodesLength; i++)
		{

			final Node item1 = childNodes.item(i);
			final String childNodeName = item1.getNodeName();

			if ("Note".equals(childNodeName))
			{
				final String childNodeValue = item1.getFirstChild().getNodeValue();
				strBuffer.append("NOTE~");
				strBuffer.append(childNodeValue);
				strBuffer.append(newLine);
			}

		}

	}

	/**
	 * Append Heading.
	 * @param item Node object.
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void appendHeading(final Node item, final StringBuffer strBuffer) throws IOException
	{
		final String headingValue = item.getFirstChild().getNodeValue();
		strBuffer.append("HEADING~");
		strBuffer.append(headingValue);
		strBuffer.append(newLine);

	}

	/**
	 * Append ui property.
	 * @param item2 the item2
	 * @param nodeName the node name.
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void appendUIProperty(final Node item2, final String nodeName,
			final StringBuffer strBuffer) throws IOException
	{
		if (nodeName.equals(UI_PROPERTY))
		{
			txUIProperties(item2, strBuffer);
		}
	}

	/**
	 * Append separators.
	 * @param strBuffer
	 */
	private void appendSeparators(final StringBuffer strBuffer)
	{
		// remove last ":"
		final char charAt = strBuffer.charAt(strBuffer.length() - 1);
		if (charAt == ':')
		{
			strBuffer.delete(strBuffer.length() - 1, strBuffer.length());
		}
		appendRulesString(strBuffer);
		appendPermValueString(strBuffer);
		appendDefaultValueString(strBuffer);
	}

	/**
	 * Append RequiredString.
	 * @param strBuffer
	 */
	private void appendRulesString(final StringBuffer strBuffer)
	{
		if (rulesString != null)
		{
			final String localRequiredString = rulesString;
			rulesString = "";
			strBuffer.append(localRequiredString);
		}
	}

	/**
	 * Append DefaultValue.
	 * @param strBuffer
	 */
	private void appendDefaultValueString(final StringBuffer strBuffer)
	{
		if (defaultValueString != null)
		{
			final String localDefaultValueString = defaultValueString;
			defaultValueString = "";
			strBuffer.append(localDefaultValueString);
		}
	}

	/**
	 * Append PermValue.
	 * @param strBuffer
	 */
	private void appendPermValueString(final StringBuffer strBuffer)
	{
		if (permValueOptionsString != null)
		{
			final String localPermValueOptionsString = permValueOptionsString;
			permValueOptionsString = "";
			strBuffer.append(localPermValueOptionsString);
		}
	}

	/**
	 * Tx ui properties.
	 * @param item  the item
	 * @param strBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txUIProperties(final Node item, final StringBuffer strBuffer) throws IOException
	{
		if (!isFirstUIProperty)
		{
			isFirstUIProperty = true;
		}
		final NamedNodeMap controlProperties = item.getAttributes();

		final Node keyNode = controlProperties.getNamedItem(KEY);
		final String nodeValue = keyNode.getNodeValue();

		boolean isRequiredRuleDefined = rulesString != null && rulesString.indexOf("required") >= 0;
		final boolean isMaxRuleDefined = rulesString != null && rulesString.indexOf("max") >= 0;
		if ("Min".equals(nodeValue))
		{
			if (rulesString == null)
			{
				rulesString = "";
			}
			rulesString += (isMaxRuleDefined ? "&min=" : isRequiredRuleDefined
					? ":range-min="
					: ",Rules~range-min=")
					+ controlProperties.getNamedItem(VALUE).getNodeValue();
		}
		else if ("Max".equals(nodeValue))
		{
			if (rulesString == null)
			{
				rulesString = "";
			}
			rulesString += (isRequiredRuleDefined ? ":range-max=" : ",Rules~range-max=")
					+ controlProperties.getNamedItem(VALUE).getNodeValue();
		}
		else if ("required".equals(nodeValue))
		{
			isRequiredRuleDefined = true;
			rulesString = ",Rules~required";
		}
		else if ("IsOrdered".equals(nodeValue))
		{
			final Node valueNode = controlProperties.getNamedItem(VALUE);
			permValueOptionsString = ",PermVal_Options~IsOrdered=" + valueNode.getNodeValue();
		}
		else if ("defaultValue".equals(nodeValue))
		{
			final Node valueNode = controlProperties.getNamedItem(VALUE);
			defaultValueString = ",defaultValue=\"" + valueNode.getNodeValue() + "\"";
		}
		else
		{
			if (isFirstUIProperty && !isSecondUIProperty)
			{
				strBuffer.append(",options~");
				isSecondUIProperty = true;
			}
			strBuffer.append(nodeValue).append('=');
			final Node valueNode = controlProperties.getNamedItem(VALUE);
			strBuffer.append(valueNode.getNodeValue());
			strBuffer.append(':');
		}
	}

	/**
	 * Tx related attributes.
	 * @param item  the item
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void txRelatedAttributes(final Node item) throws IOException
	{
		Node instanceNode = null;
		Node uiPropertyNode = null;
		appendToStringBuilder("RelatedAttribute:" + newLine);

		final NamedNodeMap attributes = item.getAttributes();

		final NodeList childNodes = item.getChildNodes();
		final int length2 = childNodes.getLength();

		for (int i = 0; i < length2; i++)
		{
			final Node item3 = childNodes.item(i);
			final String nodeName = item3.getNodeName();

			if (nodeName.equals(INSTANCES))
			{
				instanceNode = item3;
			}
			if (nodeName.equals(UI_PROPERTY))
			{
				uiPropertyNode = item3;
			}
		}
		txRAInRequiredCSVOrder(instanceNode, uiPropertyNode, attributes);
	}

	/**
	 * Tx ra in required csv order.
	 * @param instanceNode the instance node
	 * @param uiPropertyNode the ui property node
	 * @param attributes the attributes
	 * @param strBuffer buffer to store output.
	 * @throws IOException ignals that an I/O exception has occurred.
	 */
	private void txRAInRequiredCSVOrder(final Node instanceNode, final Node uiPropertyNode,
			final NamedNodeMap attributes) throws IOException
	{
		final StringBuffer strBuffer = new StringBuffer();
		if (instanceNode != null)
		{
			txInstances(instanceNode, false, strBuffer);
			appendToStringBuilder(strBuffer.toString());
		}
		final Node classeName = attributes.getNamedItem(CLASS_NAME);
		appendToStringBuilder(classeName.getNodeValue() + ":");
		final Node attributeName = attributes.getNamedItem(ATTRIBUTE_NAME);
		appendToStringBuilder(attributeName.getNodeValue() + "=");
		final Node relatedAttributeValue = attributes.getNamedItem(VALUE);
		appendToStringBuilder(relatedAttributeValue.getNodeValue());
		if (uiPropertyNode != null)
		{
			appendToStringBuilder(",options~");
			isFirstUIProperty = false;
			txUIProperties(uiPropertyNode, strBuffer);
			appendToStringBuilder(strBuffer.toString());
		}
	}

	/**
	 * This method appends form properties to stringBuilder.
	 */
	private void appendListToStringBuilder()
	{
		for (final StringBuffer buffer : strBufferList)
		{
			appendToStringBuilder(buffer.toString() + newLine);
		}
		Collections.reverse(strBufferList);
		final StringBuffer buffer = new StringBuffer();
		for (final StringBuffer stringBuffer : strBufferList)
		{
			final String str = stringBuffer.substring(stringBuffer.indexOf("instance:") + 9,
					stringBuffer.lastIndexOf("]") + 1);
			final String[] instanceArray = str.split(ARROW_OPERATOR);
			final int length = instanceArray.length;
			final String lastInstance = instanceArray[length - 1];
			buffer.append(lastInstance.substring(0, lastInstance.indexOf('['))).append('~');
			appendEntityNamesToStringBuffer(buffer, stringBuffer, instanceArray, length);
		}
		final int lastIndexOfentityGroupName = stringBuilder.lastIndexOf(newLine + entityGroupName
				+ newLine)
				+ entityGroupName.length() + 2;
		stringBuilder.insert(lastIndexOfentityGroupName, newLine + newLine + buffer);
		appendSkipLogicToStringBuilder();
	}

	/**
	 * @param buffer buffer to append entity names.
	 * @param stringBuffer string buffer.
	 * @param instanceArray Instance array.
	 * @param length Length of an instance array.
	 */
	private void appendEntityNamesToStringBuffer(final StringBuffer buffer,
			final StringBuffer stringBuffer, final String[] instanceArray, final int length)
	{
		for (int index = 0; index < length; index++)
		{
			buffer.append(instanceArray[index].substring(0, instanceArray[index].indexOf('[')));
			if (index != length - 1)
			{
				buffer.append(':');
			}
		}
		if (strBufferList.size() - 1 != strBufferList.indexOf(stringBuffer))
		{
			buffer.append(',');
		}
	}

	/**
	 * This method appends skipLogic string to stringBuilder.
	 */
	private void appendSkipLogicToStringBuilder()
	{
		if (!skipLogicList.isEmpty())
		{
			for (final String str : skipLogicList)
			{
				stringBuilder.append(str);
			}
		}
	}
}