/**
 * 
 */

package edu.common.dynamicextensions.entitymanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;

/**
 * This class provides the common XMI services other classes.
 * @author chetan_patil
 */
public class XMIBuilderUtil
{

	/**
	 * This method creates a DOM Document
	 * @return DOM Document
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Document createDocument() throws DynamicExtensionsApplicationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException parserConfigurationException)
		{
			throw new DynamicExtensionsApplicationException("Document creation failure.",
					parserConfigurationException);
		}
		return builder.newDocument();
	}

	/**
	 * This method creates an Element node.
	 * @param document - Holds the DOM Tree
	 * @param elementName - Name of the Element that is to be created.
	 * @param attributes - Attribute list having attribute name and value pair.
	 * @param value - Value of the Element
	 * @return - Element
	 */
	public static Element createElementNode(Document document, String elementName,
			LinkedHashMap<String, String> attributeMap, String value)
			throws DynamicExtensionsApplicationException
	{
		Element element = null;
		if (elementName != null || !elementName.equals(""))
		{
			element = document.createElement(elementName);
		}

		// If attributes are presents, they are added to the El ement node 
		if (attributeMap != null)
		{
			Set<Map.Entry<String, String>> entrySet = attributeMap.entrySet();
			for (Map.Entry<String, String> entryNode : entrySet)
			{
				element.setAttribute(entryNode.getKey(), entryNode.getValue());
			}
		}

		// If value is present it is attached to the Element node.
		if (value != null)
		{
			element.appendChild(document.createTextNode(value));
		}

		return element;
	}

	/**
	 * This method writes the DOMTree referenced by this.document into xmlFileName in XML format.
	 * @param document holds the DOM Tree
	 * @param xmiFileName name of the XML file in which DOM Tree has to be written.
	 * @throws DynamicExtensionsApplicationException
	 */
	public static void writeDOMToXML(Document document, String xmiFileName)
			throws DynamicExtensionsApplicationException
	{
		try
		{
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new java.io.FileWriter(xmiFileName));
			Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
			transformer.transform(domSource, streamResult);
		}
		catch (Exception exception)
		{
			throw new DynamicExtensionsApplicationException("XMI file creation failed.", exception);
		}
	}

	public static String getCurrentTimestamp()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static Element getElementByTagAndName(Document document, String tagName,
			String elementName)
	{
		NodeList nodeList = document.getElementsByTagName(tagName);
		Element element = null;
		if (elementName != null)
		{
			Element _element = null;
			for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++)
			{
				_element = (Element) nodeList.item(nodeIndex);
				String name = _element.getAttribute("name");
				if (name != null && name.equals(elementName))
				{
					element = _element;
					break;
				}
			}
		}
		else
		{
			element = (Element) document.getElementsByTagName(tagName).item(0);
		}
		return element;
	}

	public static String getAttributeType(String dataType)
	{
		String attributeType = null;
		if (dataType.equals(EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE))
		{
			attributeType = "String";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE))
		{
			attributeType = "Float";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.SHORT_ATTRIBUTE_TYPE))
		{
			attributeType = "Short";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE))
		{
			attributeType = "Boolean";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE))
		{
			attributeType = "Blob";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE))
		{
			attributeType = "Date";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.DATE_TIME_ATTRIBUTE_TYPE))
		{
			attributeType = "Date";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE))
		{
			attributeType = "Double";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE))
		{
			attributeType = "Integer";
		}
		else if (dataType.equals(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE))
		{
			attributeType = "Long";
		}
		return attributeType;
	}

	public static int getNextIdForChild(Element element)
	{
		Element childElement = null;
		String childId = null, idNumber = null;
		NodeList childrenNodeList = element.getChildNodes();
		int iElementIdNum = 0, maxId = 0;

		childElement = (Element) childrenNodeList.item(0);
		childId = childElement.getAttribute("xmi.id");
		idNumber = childId.substring(childId.lastIndexOf("_")+1);
		maxId = Integer.parseInt(idNumber);

		for (int i = 1; i < childrenNodeList.getLength(); i++)
		{
			childElement = (Element) childrenNodeList.item(i);
			childId = childElement.getAttribute("xmi.id");
			idNumber = childId.substring(childId.lastIndexOf("_")+1);
			iElementIdNum = Integer.parseInt(idNumber);

			if (iElementIdNum > maxId)
				maxId = iElementIdNum;
		}
		
		return maxId+1;
	}

}
