
package edu.common.dynamicextensions.xmi.transformer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This is specifically for caCORE 4.2 transformation
 * @author Kunal Kamble
 *
 */
public class XMITransformerCaCORE42
{

	public static final String OMG_ORG_UML1_3 = "omg.org/UML1.3";
	public static final String XMLNS_UML = "xmlns:UML";
	public static final String UML_CLASSIFIER = "UML:Classifier";
	public static final String UML_STRUCTURAL_FEATURE_TYPE = "UML:StructuralFeature.type";
	public static final String UML_NAMESPACE_OWNED_ELEMENT = "UML:Namespace.ownedElement";
	public static final String IS_ABSTRACT = "isAbstract";
	public static final String IS_LEAF = "isLeaf";
	public static final String IS_ROOT = "isRoot";
	public static final String NAME = "name";
	public static final String XMI_ID = "xmi.id";
	public static final String UML_CLASS = "UML:Class";
	public static final String UML_MODEL = "UML:Model";
	public static final String YES = "yes";
	public static final String WINDOWS_1252 = "windows-1252";
	private Document document;

	private String xmiFile;

	public String getXmiFile()
	{
		return xmiFile;
	}

	public void setXmiFile(String xmiFile)
	{
		this.xmiFile = xmiFile;
	}

	public XMITransformerCaCORE42(String filePath) throws DynamicExtensionsSystemException
	{

		xmiFile = filePath;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try
		{
			docBuilder = docFactory.newDocumentBuilder();
			document = docBuilder.parse(filePath);
		}
		catch (ParserConfigurationException e)
		{
			throw new DynamicExtensionsSystemException("Error initializing from file " + filePath,
					e);
		}
		catch (SAXException e)
		{
			throw new DynamicExtensionsSystemException("Error initializing from file " + filePath,
					e);
		}
		catch (IOException e)
		{
			throw new DynamicExtensionsSystemException("Error initializing from file " + filePath,
					e);
		}

	}

	public void transform() throws DynamicExtensionsSystemException
	{

		// 1: Change xmlns:UML to xmlns:UML="omg.org/UML1.3"
		updateXmlns();

		// 2: Rename tag UML:Class tag under UML:StructuralFeature.type to
		// UML:Classifier
		renameClassTagForAttribute();

		// 3: Add tag <UML:Class name="EARootClass"
		// xmi.id="EAID_11111111_5487_4080_A7F4_41526CB0AA00" isRoot="true"
		// isLeaf="false" isAbstract="false"/>
		String baseId = XmiIdGenerator.getNextId();
		addEaRootClass(baseId);

		// 4: Update inheritance tag
		new InheritanceTranformer(document).transform();

		// 5: Update dependency tag
		new DependencyTranformer(document).transform();

		// 6: Add classifier role
		new ClassifierTransformer(document, baseId).transform();

		// Final: Write transformed DOM to the destination file.
		writeFile();
	}

	private void writeFile() throws DynamicExtensionsSystemException
	{

		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try
		{
			transformer = tFactory.newTransformer();
			// 2: Update encoding to encoding="windows-1252"
			transformer.setOutputProperty(OutputKeys.ENCODING, WINDOWS_1252);

			transformer.setOutputProperty(OutputKeys.INDENT, YES);
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new FileOutputStream(xmiFile));
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		}
		catch (TransformerConfigurationException e)
		{
			throw new DynamicExtensionsSystemException("Error writing xmi file" + xmiFile, e);
		}
		catch (TransformerException e)
		{
			throw new DynamicExtensionsSystemException("Error writing xmi file" + xmiFile, e);
		}
		catch (FileNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Error writing xmi file" + xmiFile, e);
		}

	}

	private void addEaRootClass(String baseId)
	{
		Element eaNode = (Element) document.getElementsByTagName(UML_MODEL).item(0);
		Element nsOwnedElement = getOwnedElementNode(eaNode);

		Element eaRootClass = document.createElement(UML_CLASS);
		Attr xmiId = document.createAttribute(XMI_ID);
		xmiId.setValue(baseId);
		Attr atrrName = document.createAttribute(NAME);
		atrrName.setValue("EARootClass");

		Attr attrIsRoot = document.createAttribute(IS_ROOT);
		attrIsRoot.setValue(Boolean.TRUE.toString());
		Attr attrIsLeaf = document.createAttribute(IS_LEAF);
		attrIsLeaf.setValue(Boolean.FALSE.toString());
		Attr attrIsAbstract = document.createAttribute(IS_ABSTRACT);
		attrIsAbstract.setValue(Boolean.FALSE.toString());

		eaRootClass.getAttributes().setNamedItem(xmiId);
		eaRootClass.getAttributes().setNamedItem(atrrName);
		eaRootClass.getAttributes().setNamedItem(attrIsRoot);
		eaRootClass.getAttributes().setNamedItem(attrIsLeaf);
		eaRootClass.getAttributes().setNamedItem(attrIsAbstract);
		nsOwnedElement.appendChild(eaRootClass);

	}

	private Element getOwnedElementNode(Element eaNode)
	{

		NodeList list = eaNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++)
		{
			if (list.item(i).getNodeName().equals(UML_NAMESPACE_OWNED_ELEMENT))
			{
				return (Element) list.item(i);
			}
		}

		return null;
	}

	private void renameClassTagForAttribute()
	{
		NodeList nodeList = document.getElementsByTagName(UML_STRUCTURAL_FEATURE_TYPE);
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Element childNode = (Element) nodeList.item(i);
			NodeList dataTypeNodeList = childNode.getElementsByTagName(UML_CLASS);
			if (dataTypeNodeList.getLength() > 0)
			{
				// Create an element with the new name
				Element element2 = document.createElement(UML_CLASSIFIER);

				Element oldClassNode = (Element) dataTypeNodeList.item(0);
				// Copy the attributes to the new element
				NamedNodeMap attrs = oldClassNode.getAttributes();
				for (int j = 0; j < attrs.getLength(); j++)
				{
					Attr attr2 = (Attr) document.importNode(attrs.item(j), true);
					element2.getAttributes().setNamedItem(attr2);
				}

				// Move all the children
				while (oldClassNode.hasChildNodes())
				{
					element2.appendChild(oldClassNode.getFirstChild());
				}

				// Replace the old node with the new node
				oldClassNode.getParentNode().replaceChild(element2, oldClassNode);
			}
		}
	}

	private void updateXmlns()
	{
		document.getFirstChild().getAttributes().getNamedItem(XMLNS_UML).setNodeValue(
				OMG_ORG_UML1_3);
	}

}
