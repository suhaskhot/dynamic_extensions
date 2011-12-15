
package edu.common.dynamicextensions.xmi.transformer;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Kunal Kamble
 *
 */
public class DependencyTranformer extends AbstractXMITransformer
{

	public DependencyTranformer(Document document)
	{
		super(document);
	}

	public void transform()
	{
		// : Find dependency tag
		NodeList list = document.getElementsByTagName(UML_DEPENDENCY);
		for (int i = 0; i < list.getLength(); i++)
		{
			// 1: Get existing dependency info
			Element dependencyElement = (Element) list.item(i);

			if (dependencyElement.getElementsByTagName(UML_MODEL_ELEMENT_STEREOTYPE).item(0) != null)
			{
				// 2: Add new attributes
				updateAttributes(dependencyElement);
				updateTagValues(dependencyElement);
				addStereotype(dependencyElement);
			}
		}
		clean();
	}

	/**
	 * add stereo type element
	 * @param dependencyElement
	 */
	private void addStereotype(Element dependencyElement)
	{

		Element modelElement = document.createElement(UML_MODEL_ELEMENT_STEREOTYPE);
		dependencyElement.appendChild(modelElement);

		Element stereotype = document.createElement(UML_STEREOTYPE);
		modelElement.appendChild(stereotype);

		Attr nameAttr = document.createAttribute(NAME);
		nameAttr.setValue(DATA_SOURCE);
		stereotype.getAttributes().setNamedItem(nameAttr);

	}

	/**
	 * remove tag UML:ModelElement.clientDependency
	 * @param document
	 */
	private void clean()
	{
		NodeList nodeList = document.getElementsByTagName(UML_MODEL_ELEMENT_CLIENT_DEPENDENCY);
		Collection<Node> nodes = new ArrayList<Node>();

		int length = nodeList.getLength();
		for (int i = 0; i < length; i++)
		{

			nodes.add(nodeList.item(i));
		}
		for (Node node : nodes)
		{
			Element parent = (Element) ((Element) node).getParentNode();
			parent.removeChild(node);
		}

	}

	/**
	 * update values for the tag UML:ModelElement.taggedValue
	 * @param dependencyElement
	 */
	private void updateTagValues(Element dependencyElement)
	{
		Element taggedElement = (Element) dependencyElement.getElementsByTagName(
				UML_MODEL_ELEMENT_TAGGED_VALUE).item(0);

		taggedElement.appendChild(getTaggedValue(EA_SOURCE_TYPE, CLASS));
		taggedElement.appendChild(getTaggedValue(EA_TARGET_TYPE, CLASS));
		taggedElement.appendChild(getTaggedValue(EA_TYPE, DEPENDENCY));
		taggedElement.appendChild(getTaggedValue(DIRECTION, SOURCE_GT_DESTINATION));
		taggedElement.appendChild(getTaggedValue(STEREOTYPE, DATA_SOURCE));

		taggedElement.appendChild(getTaggedValue(EA_SOURCE_NAME, getClassName(dependencyElement,
				dependencyElement.getAttributeNode(CLIENT).getValue())));
		taggedElement.appendChild(getTaggedValue(EA_TARGET_NAME, getClassName(dependencyElement,
				dependencyElement.getAttributeNode(SUPPLIER).getValue())));

		dependencyElement.appendChild(taggedElement);
	}

	/**
	 * update attributes for the UML:Dependency.client
	 * @param dependencyElement
	 */
	private void updateAttributes(Element dependencyElement)
	{

		addClientAttribute(dependencyElement);
		addSupplierAttribute(dependencyElement);

		dependencyElement.removeChild(dependencyElement.getElementsByTagName(UML_DEPENDENCY_CLIENT)
				.item(0));
		dependencyElement.removeChild(dependencyElement.getElementsByTagName(
				UML_DEPENDENCY_SUPPLIER).item(0));
		dependencyElement.removeChild(dependencyElement.getElementsByTagName(
				UML_MODEL_ELEMENT_STEREOTYPE).item(0));

	}

	/**
	 * add supplier attribute
	 * @param dependencyElement
	 */
	private void addSupplierAttribute(Element dependencyElement)
	{
		String supplierId = getXmiID(dependencyElement, UML_DEPENDENCY_SUPPLIER);
		Attr supplierAttr = document.createAttribute(SUPPLIER);
		supplierAttr.setValue(supplierId);
		dependencyElement.getAttributes().setNamedItem(supplierAttr);
	}

	/**
	 * add client attribute
	 * @param dependencyElement
	 */
	private void addClientAttribute(Element dependencyElement)
	{
		String clientId = getXmiID(dependencyElement, UML_DEPENDENCY_CLIENT);
		Attr clientAttr = document.createAttribute(CLIENT);
		clientAttr.setValue(clientId);
		dependencyElement.getAttributes().setNamedItem(clientAttr);
	}

	/**
	 * get xmi_idref
	 * @param dependencyElement
	 * @param tagName
	 * @return
	 */
	private String getXmiID(Element dependencyElement, String tagName)
	{
		Element childElement = (Element) dependencyElement.getElementsByTagName(tagName).item(0);
		Element classEle = (Element) childElement.getElementsByTagName(UML_CLASS).item(0);
		return classEle.getAttributeNode(XMI_IDREF).getValue();
	}
}
