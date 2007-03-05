/**
 * 
 */

package edu.common.dynamicextensions.entitymanager;

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

}
