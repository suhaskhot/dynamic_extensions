
package edu.common.dynamicextensions.xmi.transformer;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InheritanceTranformer extends AbstractXMITransformer
{



	public InheritanceTranformer(Document document)
	{
		super(document);
	}

	public void transform()
	{

		NodeList list = document.getElementsByTagName(UML_GENERALIZATION);
		for (int i = 0; i < list.getLength(); i++)
		{
			// 1: Get existing Inheritance info
			Element generaliztaionElement = (Element) list.item(i);

			if (generaliztaionElement.getElementsByTagName(UML_GENERALIZATION_CHILD).item(0) != null)
			{
				// 2: Add new attributes
				updateAttributes(generaliztaionElement);
				updateTagValues(generaliztaionElement);
			}
		}
		clean();

	}

	/**
	 * @param document
	 */
	private void clean()
	{
		NodeList nodeList = document.getElementsByTagName(UML_GENERALIZABLE_ELEMENT_GENERALIZATION);
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

	private void updateTagValues(Element generalizationElement)
	{
		// create UML:ModelElement.taggedValue element
		Element taggedElement = document.createElement(UML_MODEL_ELEMENT_TAGGED_VALUE2);

		taggedElement.appendChild(getTaggedValue(EA_SOURCE_TYPE, CLASS));
		taggedElement.appendChild(getTaggedValue(EA_TARGET_TYPE, CLASS));
		taggedElement.appendChild(getTaggedValue(EA_TYPE, GENERALIZATION));
		taggedElement.appendChild(getTaggedValue(DIRECTION, SOURCE_GT_DESTINATION2));

		taggedElement.appendChild(getTaggedValue(EA_SOURCE_NAME,
				getClassName(generalizationElement, generalizationElement.getAttributeNode(
						SUPERTYPE).getValue())));
		taggedElement
				.appendChild(getTaggedValue(EA_TARGET_NAME, getClassName(generalizationElement,
						generalizationElement.getAttributeNode(SUBTYPE).getValue())));

		generalizationElement.appendChild(taggedElement);

		// 4: add new tag values
		// 5: clear old tags

	}

	private void updateAttributes(Element generaliztaionElement)
	{
		addChildId(generaliztaionElement);
		removeChildElement(generaliztaionElement);

		addParentId(generaliztaionElement);
		removeParentElement(generaliztaionElement);
	}

	private void removeParentElement(Element generaliztaionElement)
	{

		generaliztaionElement.removeChild(generaliztaionElement.getElementsByTagName(
				UML_GENERALIZATION_PARENT).item(0));

	}

	private void removeChildElement(Element generaliztaionElement)
	{

		generaliztaionElement.removeChild(generaliztaionElement.getElementsByTagName(
				UML_GENERALIZATION_CHILD).item(0));

	}

	private void addParentId(Element generaliztaionElement)
	{
		String parentId = getParentId(generaliztaionElement);
		Attr parentAttr = document.createAttribute(SUPERTYPE);
		parentAttr.setValue(parentId);
		generaliztaionElement.getAttributes().setNamedItem(parentAttr);
	}

	private String getParentId(Element generaliztaionElement)
	{

		Element parenetElemnet = (Element) generaliztaionElement.getElementsByTagName(
				UML_GENERALIZATION_PARENT).item(0);
		Element classEle = (Element) parenetElemnet.getElementsByTagName(UML_CLASS).item(0);
		return classEle.getAttributeNode(XMI_IDREF).getValue();
	}

	private void addChildId(Element generaliztaionElement)
	{
		String childId = getChildId(generaliztaionElement);
		Attr childAttr = document.createAttribute(SUBTYPE);
		childAttr.setValue(childId);
		generaliztaionElement.getAttributes().setNamedItem(childAttr);
	}

	private String getChildId(Element generaliztaionElement)
	{
		Element parentElement = (Element) generaliztaionElement.getElementsByTagName(
				UML_GENERALIZATION_CHILD).item(0);
		Element classEle = (Element) parentElement.getElementsByTagName(UML_CLASS).item(0);
		return classEle.getAttributeNode(XMI_IDREF).getValue();
	}

}
