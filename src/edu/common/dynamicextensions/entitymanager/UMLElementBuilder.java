/**
 * 
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

/**
 * @author chetan_patil
 */
public class UMLElementBuilder
{

	/**
	 * This method create an UML:Model element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Model element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLModel(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		Element umlModelElement = XMIBuilderUtil.createElementNode(document, "UML:Model",
				tagAttributeMap, null);

		LinkedHashMap<String, String> xmiId = new LinkedHashMap<String, String>();
		xmiId.put("xmi.id", (umlModelElement.getAttribute("xmi.id") + "_fix_0"));
		umlModelElement.appendChild(getUMLNamespace_OwnedElement(document, xmiId));

		return umlModelElement;
	}

	/**
	 * This method create an UML:Namespace.ownedElement element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Namespace.ownedElement element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLNamespace_OwnedElement(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Namespace.ownedElement",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Class element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Class element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLClass(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Class", tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Classifier.feature element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Classifier.feature element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLClassifier_Feature(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Classifier.feature",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Attribute element along with its children elements
	 * UML:Attribute.initialValue and UML:StructuralFeature.type
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLAttribute(Document document,
			LinkedHashMap<String, String> tagAttributeMap, String defaultValue, String deXmiId,
			boolean isCollection) throws DynamicExtensionsApplicationException
	{
		// Create UML:Attribute element
		Element umlAttributeElement = XMIBuilderUtil.createElementNode(document, "UML:Attribute",
				tagAttributeMap, null);
		String umlAttributeXMIId = umlAttributeElement.getAttribute("xmi.id");

		// Create child UML:StructuralFeature.multiplicity element		
		Element umlStructuralFeature_MultiplicityElement = generateUMLStructuralFeature_Multiplicity(
				document, umlAttributeXMIId, isCollection);
		umlAttributeElement.appendChild(umlStructuralFeature_MultiplicityElement);

		// Create child UML:Attribute.initialValue
		Element umlAttribute_InitialValueElement = generateUMLAttribute_InitialValue(document,
				umlAttributeXMIId, defaultValue);
		umlAttributeElement.appendChild(umlAttribute_InitialValueElement);

		// Create child UML:StructuralFeature.type element
		Element umlStructuralFeature_TypeElement = generateUMLStructuralFeature_Type(document,
				umlAttributeXMIId, deXmiId);
		umlAttributeElement.appendChild(umlStructuralFeature_TypeElement);

		return umlAttributeElement;
	}

	/**
	 * 
	 * @param document
	 * @param umlAttributeXMIId
	 * @param isCollection
	 * @param deXmiId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Element generateUMLStructuralFeature_Multiplicity(Document document,
			String umlAttributeXMIId, boolean isCollection)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> subElementsAttributes = null;

		// Create and UML:StructuralFeature.multiplicity element
		subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("xmi.id", (umlAttributeXMIId + "_fix_0"));
		Element umlStructuralFeature_MultiplicityElement = getUMLStructuralFeature_Multiplicity(
				document, subElementsAttributes);
		String structuralFeature_MultiplicityElementid = umlStructuralFeature_MultiplicityElement
				.getAttribute("xmi.id");

		// Create UML:Multiplicity element
		String minCardinality = "1", maxCardinality = "1";
		if (isCollection)
		{
			maxCardinality = "*";
		}
		Element umlMultiplicityElement = generateUMLMultiplicityElement(document,
				structuralFeature_MultiplicityElementid, minCardinality, maxCardinality);
		umlStructuralFeature_MultiplicityElement.appendChild(umlMultiplicityElement);

		return umlStructuralFeature_MultiplicityElement;
	}

	private static Element generateUMLMultiplicityElement(Document document, String parentId,
			String minCardinality, String maxCardinality)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("xmi.id", (parentId + "_fix_0"));
		Element umlMultiplicityElement = getUMLMultiplicity(document, subElementsAttributes);

		// Create UML:Multiplicity.range element
		subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("xmi.id",
				(umlMultiplicityElement.getAttribute("xmi.id") + "_fix_0"));
		Element umlMultiplicity_Range = getUMLMultiplicity_Range(document, subElementsAttributes);
		umlMultiplicityElement.appendChild(umlMultiplicity_Range);

		// Create UML:MultiplicityRange element
		subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("lower", minCardinality);
		subElementsAttributes.put("upper", maxCardinality);
		subElementsAttributes.put("xmi.id",
				(umlMultiplicity_Range.getAttribute("xmi.id") + "_fix_0"));
		Element umlMultiplicityRange = getUMLMultiplicityRange(document, subElementsAttributes);
		umlMultiplicity_Range.appendChild(umlMultiplicityRange);

		return umlMultiplicityElement;
	}

	/**
	 * 
	 * @param document
	 * @param umlAttributeXMIId
	 * @param defaultValue
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Element generateUMLAttribute_InitialValue(Document document,
			String umlAttributeXMIId, String defaultValue)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("xmi.id", (umlAttributeXMIId + "_fix_1"));
		Element umlAttribute_InitialValueElement = getUMLAttribute_InitialValue(document,
				subElementsAttributes);

		// Create UML:Expression element
		subElementsAttributes = new LinkedHashMap<String, String>();
		if (defaultValue != null && !defaultValue.equals(""))
		{
			subElementsAttributes.put("body", defaultValue);
		}
		subElementsAttributes.put("xmi.id", umlAttribute_InitialValueElement.getAttribute("xmi.id")
				+ "_fix_0");
		Element umlExpressionElement = getUMLExpression(document, subElementsAttributes);
		umlAttribute_InitialValueElement.appendChild(umlExpressionElement);

		return umlAttribute_InitialValueElement;
	}

	/**
	 * 
	 * @param document
	 * @param umlAttributeXMIId
	 * @param deXmiId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	private static Element generateUMLStructuralFeature_Type(Document document,
			String umlAttributeXMIId, String deXmiId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("xmi.id", (umlAttributeXMIId + "_fix_2"));
		Element umlStructuralFeature_TypeElement = getUMLStructuralFeature_Type(document,
				subElementsAttributes);

		// Create Foundation.Core.Classifier element
		subElementsAttributes = new LinkedHashMap<String, String>();
		subElementsAttributes.put("xmi.idref", deXmiId);
		subElementsAttributes.put("xmi.id", (umlStructuralFeature_TypeElement
				.getAttribute("xmi.id") + "_fix_0"));
		Element foundation_Core_Classifier = getFoundation_Core_Classifier(document,
				subElementsAttributes);
		umlStructuralFeature_TypeElement.appendChild(foundation_Core_Classifier);

		return umlStructuralFeature_TypeElement;
	}

	/**
	 * This method create an UML:StructuralFeatureType.Multiplicity element.
	 * @param document holds the DOM Tree and also used to create an Element
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute.initialValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLStructuralFeature_Multiplicity(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:StructuralFeature.Multiplicity",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:StructuralFeatureType.Multiplicity element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute.initialValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getFoundation_Core_Classifier(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "Foundation.Core.Classifier",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Multiplicity element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute.initialValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLMultiplicity(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil
				.createElementNode(document, "UML:Multiplicity", tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Multiplicity.range element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute.initialValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLMultiplicity_Range(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Multiplicity.range",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:MultiplicityRange element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute.initialValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLMultiplicityRange(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:MultiplicityRange", tagAttributeMap,
				null);
	}

	/**
	 * This method create an UML:Attribute.initialValue element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Attribute.initialValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLAttribute_InitialValue(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Attribute.initialValue",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Expression element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Expression element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLExpression(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Expression", tagAttributeMap, null);
	}

	/**
	 * This method create an UML:StructuralFeature.type element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:StructuralFeature.type element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLStructuralFeature_Type(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:StructuralFeature.type",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Classifier element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Classifier element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLClassifier(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Classifier", tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Operation element along with its child UML:BehavioralFeature.parameter.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Operation element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLOperation(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		Element umlOperation = XMIBuilderUtil.createElementNode(document, "UML:Operation",
				tagAttributeMap, null);

		// Append child UML:BehavioralFeature.parameter
		LinkedHashMap<String, String> xmiId = new LinkedHashMap<String, String>();
		xmiId.put("xmi.id", umlOperation.getAttribute("xmi.id") + "_fix_1");
		umlOperation.appendChild(getUMLBehavioralFeature_Parameter(document, xmiId));

		return umlOperation;
	}

	/**
	 * This method create an UML:BehavioralFeature.parameter element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:BehavioralFeature.parameter element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLBehavioralFeature_Parameter(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:BehavioralFeature.parameter",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Parameter element along with its children
	 * UML:Parameter.type and UML:Parameter.defaultValue.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Parameter element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLParameter(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		Element umlParameter = XMIBuilderUtil.createElementNode(document, "UML:Parameter",
				tagAttributeMap, null);

		// Append child UML:Parameter.type
		LinkedHashMap<String, String> xmiId = new LinkedHashMap<String, String>();
		xmiId.put("xmi.id", (umlParameter.getAttribute("xmi.id") + "_fix_0"));
		umlParameter.appendChild(getUMLParameterType(document, xmiId));

		// Append child UML:Parameter.defaultValue
		xmiId = new LinkedHashMap<String, String>();
		xmiId.put("xmi.id", (umlParameter.getAttribute("xmi.id") + "_fix_1"));
		umlParameter.appendChild(getUMLParameter_DefaultValue(document, xmiId));

		return umlParameter;
	}

	/**
	 * This method create an UML:Package.type element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Package.type element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLParameterType(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Parameter.type", tagAttributeMap,
				null);
	}

	/**
	 * This method create an UML:Package.defaultValue element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Package.defaultValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLParameter_DefaultValue(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Parameter.defaultValue",
				tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Package element along with its child UML:Namespace.ownedElement.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:Package element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLPackage(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		Element umlPackage = XMIBuilderUtil.createElementNode(document, "UML:Package",
				tagAttributeMap, null);

		// Append child UML:Namespace.ownedElement
		LinkedHashMap<String, String> xmiId = new LinkedHashMap<String, String>();
		xmiId.put("xmi.id", (umlPackage.getAttribute("xmi.id") + "_fix_1"));
		umlPackage.appendChild(getUMLNamespace_OwnedElement(document, xmiId));

		return umlPackage;
	}

	/**
	 * This method create an UML:DataType element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:DataType element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLDataType(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:DataType", tagAttributeMap, null);
	}

	/**
	 * This method create an UML:TaggedValue element.
	 * @param document holds the DOM Tree
	 * @param tagAttributeMap represents the list of attribute-value pair 
	 * @return UML:TaggedValue element
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLTaggedValue(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:TaggedValue", tagAttributeMap, null);
	}

	/**
	 * This method create an UML:Interface element.
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLInterface(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Interface", tagAttributeMap, null);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLGeneralizationElement(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Generalization", tagAttributeMap,
				null);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLGeneralizableElement_GeneralizationElement(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document,
				"UML:GeneralizationElement.generalization", tagAttributeMap, null);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getFoundation_Core_GeneralizationElement(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "Foundation.Core.Generalization",
				tagAttributeMap, null);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLAssociation(Document document,
			LinkedHashMap<String, String> tagAttributeMap, Element targetClassElement,
			String sourceClassName, String sourceClassId, Cardinality sourceMinCardinality,
			Cardinality sourceMaxCardinality, Cardinality targetMinCardinality,
			Cardinality targetMaxCardinality) throws DynamicExtensionsApplicationException
	{
		// Create UML:Association element
		Element umlAssociationElement = XMIBuilderUtil.createElementNode(document,
				"UML:Association", tagAttributeMap, null);
		String associationId = umlAssociationElement.getAttribute("xmi.id");

		// Create UML:Association.connection element
		Element umlAssociation_ConnectionElement = generateUMLAssociation_ConnectionElement(
				document, associationId);
		umlAssociationElement.appendChild(umlAssociation_ConnectionElement);
		String association_ConnectionId = umlAssociation_ConnectionElement.getAttribute("xmi.id");

		String targetClassName = targetClassElement.getAttribute("name");
		String targetClassId = targetClassElement.getAttribute("xmi.id");
		int index = 0;

		// Create UML:AssociationEnd element
		Element umlAssociationEndSourceElement = generateAssociationEndElement(document,
				sourceClassName, sourceClassId, association_ConnectionId, sourceMinCardinality,
				sourceMaxCardinality, index++);
		umlAssociation_ConnectionElement.appendChild(umlAssociationEndSourceElement);

		// Create UML:AssociationEnd element
		Element umlAssociationEndTargetElement = generateAssociationEndElement(document,
				targetClassName, targetClassId, association_ConnectionId, targetMinCardinality,
				targetMaxCardinality, index++);
		umlAssociation_ConnectionElement.appendChild(umlAssociationEndTargetElement);

		return umlAssociationElement;
	}

	private static Element generateAssociationEndElement(Document document, String className,
			String classId, String association_ConnectionId, Cardinality minCardinality,
			Cardinality maxCardinality, int index) throws DynamicExtensionsApplicationException
	{
		Element umlAssociationEndSourceElement = generateUMLAssociationEndElement(document,
				className, classId, association_ConnectionId, index);
		String associationEndId = umlAssociationEndSourceElement.getAttribute("xmi.id");

		// UML:AssociationEnd.multiplicity element
		Element umlAssociationEnd_Multiplicity = generateUMLAssociationEnd_MultiplicityElement(
				document, associationEndId);
		String associationEnd_MultiplicityId = umlAssociationEnd_Multiplicity
				.getAttribute("xmi.id");
		umlAssociationEndSourceElement.appendChild(umlAssociationEnd_Multiplicity);

		// Create UML:Multiplcity element
		String lower = minCardinality.getValue().toString();
		String upper = null;
		if (maxCardinality.equals(Cardinality.MANY))
		{
			upper = "*";
		}
		else
		{
			lower = maxCardinality.getValue().toString();
		}
		Element umlMultiplicityElement = generateUMLMultiplicityElement(document,
				associationEnd_MultiplicityId, lower, upper);
		umlAssociationEnd_Multiplicity.appendChild(umlMultiplicityElement);

		return umlAssociationEndSourceElement;
	}

	private static Element generateUMLAssociation_ConnectionElement(Document document,
			String associationId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("xmi.id", associationId + "_fix_1");
		return UMLElementBuilder.getUMLAssociation_Connection(document, tagAttributeMap);
	}

	private static Element generateUMLAssociationEndElement(Document document, String name,
			String type, String parentId, int index) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("visibility", "private");
		tagAttributeMap.put("name", name);
		tagAttributeMap.put("aggregation", "none");
		tagAttributeMap.put("isOrdered", "false");
		tagAttributeMap.put("isNavigable", "false");
		tagAttributeMap.put("type", type);
		tagAttributeMap.put("xmi.id", parentId + "_fix_" + index);
		return UMLElementBuilder.getUMLAssociationEnd(document, tagAttributeMap);
	}

	private static Element generateUMLAssociationEnd_MultiplicityElement(Document document,
			String parentId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("xmi.id", parentId + "_fix_0");
		return UMLElementBuilder.getUMLAssociationEnd_Multiplicity(document, tagAttributeMap);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLAssociation_Connection(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:Association.connection",
				tagAttributeMap, null);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLAssociationEnd(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:AssociationEnd", tagAttributeMap,
				null);
	}

	/**
	 * 
	 * @param document
	 * @param tagAttributeMap
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static Element getUMLAssociationEnd_Multiplicity(Document document,
			LinkedHashMap<String, String> tagAttributeMap)
			throws DynamicExtensionsApplicationException
	{
		return XMIBuilderUtil.createElementNode(document, "UML:AssociationEnd.multiplicity",
				tagAttributeMap, null);
	}

}
