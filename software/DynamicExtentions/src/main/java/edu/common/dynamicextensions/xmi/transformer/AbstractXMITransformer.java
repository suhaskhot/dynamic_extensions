
package edu.common.dynamicextensions.xmi.transformer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Used for transforming xmi.
 * @author Kunal Kamble
 *
 */
public abstract class AbstractXMITransformer
{

	public static final String PACKAGE2 = "package2";
	public static final String PUBLIC = "public";
	public static final String BASE = "base";
	public static final String VISIBILITY = "visibility";
	public static final String EA_STYPE = "ea_stype";
	public static final String EA_ELE_TYPE = "ea_eleType";
	public static final String PACKAGE = "package";
	public static final String PACKAGE_NAME = "package_name";
	public static final String UML_MODEL_ELEMENT_TAGGED_VALUE = "UML:ModelElement.taggedValue";
	public static final String UML_CLASSIFIER_ROLE = "UML:ClassifierRole";
	public static final String COLLABORATIONS = "Collaborations";
	public static final String NAME = "name";
	public static final String XMI_ID = "xmi.id";
	public static final String UML_COLLABORATION = "UML:Collaboration";
	public static final String UML_NAMESPACE_OWNED_ELEMENT = "UML:Namespace.ownedElement";
	public static final String UML_PACKAGE = "UML:Package";
	public static final String UML_MODEL = "UML:Model";

	public static final String XMI_IDREF = "xmi.idref";
	public static final String UML_CLASS = "UML:Class";
	public static final String UML_DEPENDENCY_SUPPLIER = "UML:Dependency.supplier";
	public static final String UML_DEPENDENCY_CLIENT = "UML:Dependency.client";
	public static final String SUPPLIER = "supplier";
	public static final String CLIENT = "client";
	public static final String EA_TARGET_NAME = "ea_targetName";
	public static final String SOURCE_GT_DESTINATION = "Source -&gt; Destination";
	public static final String DEPENDENCY = "Dependency";
	public static final String CLASS = "Class";
	public static final String EA_SOURCE_NAME = "ea_sourceName";
	public static final String STEREOTYPE = "stereotype";
	public static final String DIRECTION = "direction";
	public static final String EA_TYPE = "ea_type";
	public static final String EA_TARGET_TYPE = "ea_targetType";
	public static final String EA_SOURCE_TYPE = "ea_sourceType";
	public static final String UML_MODEL_ELEMENT_CLIENT_DEPENDENCY = "UML:ModelElement.clientDependency";
	public static final String DATA_SOURCE = "DataSource";
	public static final String UML_STEREOTYPE = "UML:Stereotype";
	public static final String UML_MODEL_ELEMENT_STEREOTYPE = "UML:ModelElement.stereotype";
	public static final String UML_DEPENDENCY = "UML:Dependency";
	
	public static final String UML_GENERALIZATION_PARENT = "UML:Generalization.parent";
	public static final String TAG = "tag";
	public static final String VALUE = "value";
	public static final String UML_TAGGED_VALUE = "UML:TaggedValue";
	public static final String SUBTYPE = "subtype";
	public static final String SUPERTYPE = "supertype";
	public static final String SOURCE_GT_DESTINATION2 = "Source -&gt; Destination";
	public static final String GENERALIZATION = "Generalization";
	public static final String UML_MODEL_ELEMENT_TAGGED_VALUE2 = "UML:ModelElement.taggedValue";
	public static final String UML_GENERALIZABLE_ELEMENT_GENERALIZATION = "UML:GeneralizableElement.generalization";
	public static final String UML_GENERALIZATION_CHILD = "UML:Generalization.child";
	public static final String UML_GENERALIZATION = "UML:Generalization";
	
	protected Document document;
	
	public AbstractXMITransformer(Document document)
	{
		this.document = document;		
	}

	public abstract void transform();
	
	public Document getDocument()
	{
		return document;
	}

	public void setDocument(Document document)
	{
		this.document = document;
	}


	public Node getTaggedValue(String tagValue, String valueAttribute)
	{
		Element taggedElement = document.createElement(UML_TAGGED_VALUE);
		Attr valueAttr = document.createAttribute(VALUE);
		valueAttr.setValue(valueAttribute);

		Attr tagAttr = document.createAttribute(TAG);
		tagAttr.setValue(tagValue);

		taggedElement.getAttributes().setNamedItem(tagAttr);
		taggedElement.getAttributes().setNamedItem(valueAttr);

		return taggedElement;
	}


	public String getClassName(Element generaliztaionElement, String xmiId)
	{
		NodeList nodeList = document.getElementsByTagName(UML_CLASS);
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Element element = (Element) nodeList.item(i);
			if (element.getAttributeNode(XMI_ID) != null)
			{
				if (xmiId.equals(element.getAttributeNode(XMI_ID).getValue()))
				{
					return element.getAttributeNode(NAME).getValue();
				}
			}
		}
		return null;
	}

}
