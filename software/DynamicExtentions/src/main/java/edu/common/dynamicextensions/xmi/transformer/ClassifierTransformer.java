
package edu.common.dynamicextensions.xmi.transformer;

import java.util.Collection;
import java.util.HashSet;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classifier tag is not generated with the DE exported xmi, so this transformer
 * class is used to add it.
 * 
 * @author Kunal Kamble
 * 
 */
public class ClassifierTransformer extends AbstractXMITransformer
{

	private String baseId;

	public ClassifierTransformer(Document document, String base)
	{
		super(document);
		baseId = base;
	}

	public void transform()
	{
		Element logicalModel = (Element) ((Element) ((Element) document.getElementsByTagName(
				UML_MODEL).item(0)).getElementsByTagName(UML_NAMESPACE_OWNED_ELEMENT).item(0))
				.getElementsByTagName(UML_PACKAGE).item(0);
		addClassifierRole(logicalModel);
	}

	/**
	 * add classifier role
	 * @param parentPackage
	 */
	private void addClassifierRole(Element parentPackage)
	{

		Collection<Node> packageList = getChildPackages(parentPackage);
		for (Node childPackage : packageList)
		{
			addClassifierRole((Element) childPackage);
		}
		if (packageList.size() > 0)
		{

			Element collabElement = document.createElement(UML_COLLABORATION);
			parentPackage.getElementsByTagName(UML_NAMESPACE_OWNED_ELEMENT).item(0).appendChild(
					collabElement);
			addCollaborationAttributes(collabElement);

			Element nameSpaceElement = document.createElement(UML_NAMESPACE_OWNED_ELEMENT);
			collabElement.appendChild(nameSpaceElement);
			for (Node node : packageList)
			{
				nameSpaceElement
						.appendChild(createClassifierElement((Element) node, parentPackage));
			}

		}

	}

	/**
	 * Get list of all child packages
	 * @param parentPackage
	 * @return
	 */
	private Collection<Node> getChildPackages(Element parentPackage)
	{

		NodeList nodeList = parentPackage.getElementsByTagName(UML_PACKAGE);
		Collection<Node> childPackage = new HashSet<Node>();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			if (nodeList.item(i).getParentNode().getParentNode().equals(parentPackage))
			{
				childPackage.add(nodeList.item(i));
			}
		}
		return childPackage;
	}

	/** Add collaboration attribute
	 * @param collabElement
	 */
	private void addCollaborationAttributes(Element collabElement)
	{

		Attr xmiIdAttr = document.createAttribute(XMI_ID);
		xmiIdAttr.setValue(XmiIdGenerator.getNextId());

		Attr nameAttr = document.createAttribute(NAME);
		nameAttr.setValue(COLLABORATIONS);

		collabElement.getAttributes().setNamedItem(nameAttr);
		collabElement.getAttributes().setNamedItem(xmiIdAttr);

	}

	/** 
	 * Create classifier element
	 * @param childPackage
	 * @param parentPackage
	 * @return
	 */
	private Element createClassifierElement(Element childPackage, Element parentPackage)
	{

		Element classifierElement = document.createElement(UML_CLASSIFIER_ROLE);
		addClassifierAttributes(classifierElement, childPackage);

		Element taggedValueElement = document.createElement(UML_MODEL_ELEMENT_TAGGED_VALUE);
		classifierElement.appendChild(taggedValueElement);

		taggedValueElement.appendChild(getTaggedValue(parentPackage.getAttributeNode(NAME)
				.getValue(), PACKAGE_NAME));
		taggedValueElement.appendChild(getTaggedValue(parentPackage.getAttributeNode(XMI_ID)
				.getValue(), PACKAGE));
		taggedValueElement.appendChild(getTaggedValue(PACKAGE, EA_ELE_TYPE));
		taggedValueElement.appendChild(getTaggedValue(PACKAGE, EA_STYPE));

		taggedValueElement.appendChild(getTaggedValue(classifierElement.getAttributeNode(XMI_ID)
				.getValue(), PACKAGE2));

		return classifierElement;
	}

	/**
	 * add attributes to the classifier element
	 * @param classifierElement
	 * @param childPackage
	 */
	private void addClassifierAttributes(Element classifierElement, Element childPackage)
	{

		Attr nameAttr = document.createAttribute(NAME);
		nameAttr.setValue(childPackage.getAttributeNode(NAME).getValue());

		Attr visibilityAttr = document.createAttribute(VISIBILITY);
		visibilityAttr.setValue(PUBLIC);

		Attr xmiIdAttr = document.createAttribute(XMI_ID);
		xmiIdAttr.setValue(XmiIdGenerator.getNextId());

		Attr baseAttr = document.createAttribute(BASE);
		baseAttr.setValue(baseId);

		classifierElement.getAttributes().setNamedItem(nameAttr);
		classifierElement.getAttributes().setNamedItem(visibilityAttr);
		classifierElement.getAttributes().setNamedItem(xmiIdAttr);
		classifierElement.getAttributes().setNamedItem(baseAttr);

	}
}
