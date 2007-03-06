
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.UniqueIDGenerator;

/**
 * This class generates XMI file from the Entity Object.
 */

/**
 * <p>Title: XMIBuilder Class</p>
 * <p>Description: This Class generates the DOM Tree from the scratch and writes its into a XML file.</p>
 * @author Chetan Patil
 * @version 1.0
 */
public class XMIBuilder implements XMIBuilderConstantsInterface
{

	/**
	 * This method generates the XMI file of the Entity object.
	 * @param entity - Entity Object whos XMI file is to be generated.
	 * @param xmiFileName - Name of the XMI file
	 * @return - DOM Tree holder of the Entity i.e. Document
	 */
	public void exportMetadataToXMI(EntityGroupInterface entityGroup)
			throws DynamicExtensionsApplicationException
	{
		HashMap<String, String> name_IdMap = new HashMap<String, String>();

		// Create document
		Document document = XMIBuilderUtil.createDocument();

		// Create and append XMI element to the document
		Element xmiRoot = XMIElementsBuilder.createXMISkeleton(document);
		document.appendChild(xmiRoot);

		// Generate fixed part of the XMI file
		generateStaticXMIPart(document, name_IdMap);

		// Generate xmi for the EntityGroup 
		Element groupElement = gernerateXMIFromEntityGroup(document, entityGroup, name_IdMap);

		// Append Group package element at the appropriate position in the DOM 
		Element logicalModelPackage = XMIBuilderUtil.getElementByTagAndName(document,
				"UML:Package", "Logical Model");
		Element namespaceOwnedElement = (Element) logicalModelPackage.getFirstChild();
		namespaceOwnedElement.appendChild(groupElement);

		String xmiFileName = entityGroup.getName() + ".xmi";
		/* Write document to XMI file */
		XMIBuilderUtil.writeDOMToXML(document, xmiFileName);
	}

	private Element gernerateXMIFromEntityGroup(Document document,
			EntityGroupInterface entityGroup, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element groupPackageElement = null;
		if (entityGroup != null)
		{
			String groupName = entityGroup.getName();

			// Create UML:Package element for the Group
			groupPackageElement = generatePackageElement(document, groupName);
			String groupPackageId = groupPackageElement.getAttribute("xmi.id");
			LinkedHashMap<String, String> propertyMap = getPropertiesOfUMLPackageElement();
			String modelElement = groupPackageId;
			appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
			Element namespaceOwnedElement = (Element) groupPackageElement.getFirstChild();

			String entityName = null, entityClassId = null;
			Element entityClassElement = null, classifierFeatureElement = null;
			Collection<EntityInterface> entityCollection = entityGroup.getEntityCollection();
			for (EntityInterface entity : entityCollection)
			{
				entityName = entity.getName();

				// Create UML:Class element for the Entity
				entityClassElement = generateClassElement(document, entityName, groupPackageId);
				entityClassId = entityClassElement.getAttribute("xmi.id");
				namespaceOwnedElement.appendChild(entityClassElement);

				propertyMap = getPropertiesOfUMLClassElement(groupPackageId, groupName);
				modelElement = entityClassElement.getAttribute("xmi.id");
				appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

				// Create UML:ClassifierFeature element and append it to the UML:Class element
				classifierFeatureElement = generateClassifierFeatureElement(document, entityClassId);
				entityClassElement.appendChild(classifierFeatureElement);

				String classifierFeatureId = classifierFeatureElement.getAttribute("xmi.id");

				// Iterate through all attributes of the Entity and create UML:Attribute element for each. Then append every 
				// UML:Attribute element to UML:ClassifierFeature element.
				Collection<AbstractAttributeInterface> abstractAttributeCollection = entity
						.getAbstractAttributeCollection();
				int attributeCount = 0;
				for (AbstractAttributeInterface abstractAttribute : abstractAttributeCollection)
				{
					if (abstractAttribute instanceof AttributeInterface)
					{
						AttributeInterface attribute = (AttributeInterface) abstractAttribute;
						AttributeTypeInformationInterface attributeTypeInformation = attribute
								.getAttributeTypeInformation();
						String dataType = attributeTypeInformation.getDataType();
						String deXmiId = name_IdMap.get(dataType);
						String attributeName = attribute.getName();
						String defaultValue = ControlsUtility.getDefaultValue(abstractAttribute);
						boolean isCollection = attribute.getIsCollection();

						Element attributeElement = generateAttributeElement(document,
								attributeName, classifierFeatureId, attributeCount++, defaultValue,
								isCollection, deXmiId);

						classifierFeatureElement.appendChild(attributeElement);

						// Append UML:TaggedValue for UML:Attribute
						String description = abstractAttribute.getDescription();
						String type = XMIBuilderUtil.getAttributeType(dataType);
						propertyMap = getPropertiesOfUMLAttributeElement(type, isCollection,
								description);
						modelElement = attributeElement.getAttribute("xmi.id");
						appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
					}
				}
			}
		}
		return groupPackageElement;
	}

	/**
	 * This method generated static part i.e., fixed part of the XMI structure.
	 * @param document
	 * @param name_IdMap 
	 * @throws DynamicExtensionsApplicationException
	 */
	private void generateStaticXMIPart(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		// Retreive the XMI.content element
		Element xmiContentElement = XMIBuilderUtil.getElementByTagAndName(document, "XMI.content",
				null);

		// Create and append UML:Model element to XMI:content
		Element eaModelElement = generateEAModelElement(document);
		xmiContentElement.appendChild(eaModelElement);

		Element namespaceOwnedElement = (Element) eaModelElement.getFirstChild();
		LinkedHashMap<String, String> propertyMap = null;
		String modelElement = null;

		// Create UML:Class
		Element eaRootClassElement = generateEAClassElement(document);
		namespaceOwnedElement.appendChild(eaRootClassElement);

		// Create UML:Package element for "Logical View"
		Element logicalViewPackage = generatePackageElement(document, "Logical View");
		namespaceOwnedElement.appendChild(logicalViewPackage);

		// Append UML:TaggedValues for "Logical View" UML:Pcakage
		propertyMap = generatePropertyMapForLogicalView(document);
		modelElement = logicalViewPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		// Create UML:Package element for "Logical Model"
		Element logicalModelPackage = generatePackageElement(document, "Logical Model");
		namespaceOwnedElement = (Element) logicalViewPackage.getFirstChild();
		namespaceOwnedElement.appendChild(logicalModelPackage);

		// Append UML:TaggedValues for "Logical Model" UML:Pcakage
		propertyMap = generatePropertyMapForLogicalModel(document);
		modelElement = logicalModelPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		// Create UML:Package element for "java"
		Element javaPackage = generateJavaPackage(document, name_IdMap);
		namespaceOwnedElement = (Element) logicalModelPackage.getFirstChild();
		namespaceOwnedElement.appendChild(javaPackage);

		// Append UML:TaggedValue for 'java' UML:Package
		propertyMap = getPropertiesOfUMLPackageElement();
		modelElement = javaPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
	}

	private Element generateEAClassElement(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", "EARootClass");
		tagAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		tagAttributeMap.put("isRoot", "true");
		tagAttributeMap.put("isLeaf", "false");
		tagAttributeMap.put("isAbstract", "false");
		return UMLElementBuilder.getUMLClass(document, tagAttributeMap);
	}

	private Element generateEAModelElement(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", "EA Model");
		tagAttributeMap.put("xmi.id", MODEL_ID_PREFIX + UniqueIDGenerator.getId());
		return UMLElementBuilder.getUMLModel(document, tagAttributeMap);
	}

	private LinkedHashMap<String, String> generatePropertyMapForLogicalView(Document document)
			throws DynamicExtensionsApplicationException
	{
		Element logicalViewPackage = XMIBuilderUtil.getElementByTagAndName(document, "UML:Package",
				"Logical View");
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		if (logicalViewPackage != null)
		{
			propertyMap.put("tpos", "5");
			propertyMap.put("packageFlags", "CRC=0;isModel=1;VICON=3;");

			propertyMap.putAll(getPropertiesOfUMLPackageElement());
		}
		return propertyMap;
	}

	private LinkedHashMap<String, String> generatePropertyMapForLogicalModel(Document document)
			throws DynamicExtensionsApplicationException
	{
		Element logicalModelPackage = XMIBuilderUtil.getElementByTagAndName(document,
				"UML:Package", "Logical Model");
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		if (logicalModelPackage != null)
		{
			propertyMap.put("tpos", "0");
			propertyMap.putAll(getPropertiesOfUMLPackageElement());
		}
		return propertyMap;
	}

	private void appendUMLTaggedValuesT0XMIContent(Document document,
			LinkedHashMap<String, String> propertyMap, String modelElement)
			throws DynamicExtensionsApplicationException
	{
		Element xmiContentElement = XMIBuilderUtil.getElementByTagAndName(document, "XMI.content",
				null);
		Set<Map.Entry<String, String>> entrySet = propertyMap.entrySet();
		int propertyNumber = 0;
		for (Map.Entry<String, String> entry : entrySet)
		{
			String tagName = entry.getKey();
			String value = entry.getValue();
			Element umlTaggedValueElement = generateUMLTagggedElement(document, modelElement,
					propertyNumber++, tagName, value);
			xmiContentElement.appendChild(umlTaggedValueElement);
		}
	}

	private Element generateUMLTagggedElement(Document document, String modelElement,
			int propertyNumber, String tagName, String value)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();

		tagAttributeMap.put("xmlns:UML", "href://org.omg/UML");
		tagAttributeMap.put("xmi.id", modelElement + "_fix_" + propertyNumber);
		tagAttributeMap.put("tag", tagName);
		tagAttributeMap.put("modelElement", modelElement);
		tagAttributeMap.put("value", value);

		return UMLElementBuilder.getUMLTaggedValue(document, tagAttributeMap);
	}

	private LinkedHashMap<String, String> getPropertiesOfUMLPackageElement()
	{
		String timeStamp = XMIBuilderUtil.getCurrentTimestamp();
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();

		propertyMap.put("created", timeStamp);
		propertyMap.put("modified", timeStamp);
		propertyMap.put("iscontrolled", "FALSE");
		propertyMap.put("lastloaddate", timeStamp);
		propertyMap.put("lastsavedate", timeStamp);
		propertyMap.put("isprotected", "FALSE");
		propertyMap.put("usedtd", "FALSE");
		propertyMap.put("logxml", "FALSE");
		propertyMap.put("batchsave", "0");
		propertyMap.put("batchload", "0");
		propertyMap.put("phase", "1.0");
		propertyMap.put("status", "Proposed");
		propertyMap.put("complexity", "1");
		propertyMap.put("ea_stype", "Public");

		return propertyMap;
	}

	private LinkedHashMap<String, String> getPropertiesOfUMLClassElement(String packageId,
			String packageName)
	{
		String timeStamp = XMIBuilderUtil.getCurrentTimestamp();
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();

		propertyMap.put("isSpecification", "false");
		propertyMap.put("ea_stype", "Class");
		propertyMap.put("ea_ntype", "0");
		propertyMap.put("version", "1.0");
		propertyMap.put("package", packageId);
		propertyMap.put("date_created", timeStamp);
		propertyMap.put("date_modified", timeStamp);
		propertyMap.put("gentype", "Java");
		propertyMap.put("tagged", "0");
		propertyMap.put("package_name", packageName);
		propertyMap.put("phase", "1.0");
		propertyMap.put("complexity", "1");
		propertyMap.put("status", "Proposed");
		//propertyMap.put("ea_localid", ""); //787
		propertyMap.put("ea_eleType", "element");
		//propertyMap.put("eventflag", ""); //ATT=5b55;LNK=1d05;
		propertyMap
				.put("style",
						"BackColor=-1;BorderColor=-1;BorderWidth=-1;FontColor=-1;VSwimLanes=0;HSwimLanes=0;BorderStyle=0;");

		return propertyMap;
	}

	private LinkedHashMap<String, String> getPropertiesOfUMLAttributeElement(String type,
			boolean isCollection, String description)
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();

		propertyMap.put("type", type);
		propertyMap.put("derived", "0");
		propertyMap.put("containment", "Not Specified");
		propertyMap.put("ordered", "0");
		if (isCollection)
		{
			propertyMap.put("collection", "true");
			propertyMap.put("upperBound", "*");
		}
		else
		{
			propertyMap.put("collection", "false");
			propertyMap.put("upperBound", "1");
		}
		propertyMap.put("position", "0");
		propertyMap.put("lowerBound", "1");
		propertyMap.put("duplicates", "0");
		//propertyMap.put("ea_guid", "");
		//propertyMap.put("ea_localid", ""); //787
		propertyMap.put("styleex", "volatile=0;");
		propertyMap.put("description", description);

		return propertyMap;
	}

	private Element generateJavaPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		// Create UML:Package for 'java'
		Element javaPackage = generatePackageElement(document, "java");

		LinkedHashMap<String, String> propertyMap = getPropertiesOfUMLPackageElement();
		String modelElement = null;
		Element namespaceOwnedElement = (Element) javaPackage.getFirstChild();

		// Create UML:Package for 'lang'
		Element langPackage = generateLangPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(langPackage);

		// Append UML:TaggedValue for 'lang' UML:Package
		modelElement = langPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		// Create UML:Pacakage for 'util'
		Element utilPackage = generateUtilPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(utilPackage);

		// Append UML:TaggedValue for 'util' UML:Package
		modelElement = utilPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		// Create UML:Pacakage for 'sql'
		Element sqlPackage = generateSqlPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(sqlPackage);

		// Append UML:TaggedValue for 'sql' UML:Package
		modelElement = sqlPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		return javaPackage;
	}

	private Element generateLangPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element langPackage = generatePackageElement(document, "lang");

		Element namespaceOwnedElement = (Element) langPackage.getFirstChild();
		String packageId = langPackage.getAttribute("xmi.id");
		String packageName = langPackage.getAttribute("name");
		String modelElement = null;
		LinkedHashMap<String, String> propertyMap = getPropertiesOfUMLClassElement(packageId,
				packageName);

		Element longClass = generateClassElement(document, "Long", packageId);
		namespaceOwnedElement.appendChild(longClass);
		modelElement = longClass.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE, longClass
				.getAttribute("xmi.id"));

		Element booleanClass = generateClassElement(document, "Boolean", packageId);
		namespaceOwnedElement.appendChild(booleanClass);
		modelElement = booleanClass.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE, booleanClass
				.getAttribute("xmi.id"));

		Element stringClass = generateClassElement(document, "String", packageId);
		namespaceOwnedElement.appendChild(stringClass);
		modelElement = stringClass.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE, stringClass
				.getAttribute("xmi.id"));

		Element integerClass = generateClassElement(document, "Integer", packageId);
		namespaceOwnedElement.appendChild(integerClass);
		modelElement = integerClass.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE, integerClass
				.getAttribute("xmi.id"));

		Element doubleClass = generateClassElement(document, "Double", packageId);
		namespaceOwnedElement.appendChild(doubleClass);
		modelElement = doubleClass.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE, doubleClass
				.getAttribute("xmi.id"));

		Element floatClass = generateClassElement(document, "Float", packageId);
		namespaceOwnedElement.appendChild(floatClass);
		modelElement = floatClass.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE, floatClass
				.getAttribute("xmi.id"));

		return langPackage;
	}

	private Element generateSqlPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element sqlPackage = generatePackageElement(document, "sql");

		Element namespaceOwnedElement = (Element) sqlPackage.getFirstChild();
		String packageId = sqlPackage.getAttribute("xmi.id");
		String packageName = sqlPackage.getAttribute("name");
		LinkedHashMap<String, String> propertyMap = getPropertiesOfUMLClassElement(packageId,
				packageName);

		Element blobInterface = generateInterfaceElement(document, "Blob", packageId);
		namespaceOwnedElement.appendChild(blobInterface);
		String modelElement = blobInterface.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE, blobInterface
				.getAttribute("xmi.id"));

		return sqlPackage;
	}

	private Element generateUtilPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element utilPackage = generatePackageElement(document, "util");

		Element namespaceOwnedElement = (Element) utilPackage.getFirstChild();
		String packageId = utilPackage.getAttribute("xmi.id");
		String packageName = utilPackage.getAttribute("name");
		LinkedHashMap<String, String> propertyMap = getPropertiesOfUMLClassElement(packageId,
				packageName);

		Element dateClass = generateClassElement(document, "Date", packageId);
		namespaceOwnedElement.appendChild(dateClass);
		String modelElement = utilPackage.getAttribute("xmi.id");
		appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);
		name_IdMap.put(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE, dateClass
				.getAttribute("xmi.id"));
		name_IdMap.put(EntityManagerConstantsInterface.DATE_TIME_ATTRIBUTE_TYPE, dateClass
				.getAttribute("xmi.id"));

		return utilPackage;
	}

	private Element generateClassElement(Document document, String className, String packageId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();

		tagAttributeMap.put("name", className);
		tagAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		tagAttributeMap.put("namespace", packageId);
		tagAttributeMap.put("isRoot", "false");
		tagAttributeMap.put("isLeaf", "false");
		tagAttributeMap.put("isAbstract", "false");
		tagAttributeMap.put("isActive", "false");
		tagAttributeMap.put("visibility", "public");

		return UMLElementBuilder.getUMLClass(document, tagAttributeMap);
	}

	private Element generateInterfaceElement(Document document, String interfaceName,
			String packageId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();

		tagAttributeMap.put("name", interfaceName);
		tagAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		tagAttributeMap.put("namespace", packageId);
		tagAttributeMap.put("isRoot", "false");
		tagAttributeMap.put("isLeaf", "false");
		tagAttributeMap.put("isAbstract", "false");
		tagAttributeMap.put("isActive", "false");
		tagAttributeMap.put("visibility", "public");

		return UMLElementBuilder.getUMLInterface(document, tagAttributeMap);
	}

	private Element generatePackageElement(Document document, String packageName)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = null;

		tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", packageName);
		tagAttributeMap.put("xmi.id", PACKAGE_ID_PREFIX + UniqueIDGenerator.getId());
		tagAttributeMap.put("isRoot", "false");
		tagAttributeMap.put("isLeaf", "false");
		tagAttributeMap.put("isAbstract", "false");
		tagAttributeMap.put("visibility", "public");
		return UMLElementBuilder.getUMLPackage(document, tagAttributeMap);
	}

	private Element generateClassifierFeatureElement(Document document, String classId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("xmi.id", classId + "_fix_1");
		return UMLElementBuilder.getUMLClassifier_Feature(document, tagAttributeMap);
	}

	private Element generateAttributeElement(Document document, String attributeName,
			String classifierFeatureId, int attributeCount, String defaultValue,
			boolean isCollection, String deXmiId) throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		/* Create attribute value list for UML:Attribute element */
		tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", attributeName);
		tagAttributeMap.put("changeable", "none");
		tagAttributeMap.put("visibility", "private");
		tagAttributeMap.put("ownerScope", "instance");
		tagAttributeMap.put("targetScope", "instance");
		tagAttributeMap.put("xmi.id", classifierFeatureId + "_fix_" + attributeCount);
		return UMLElementBuilder.getUMLAttribute(document, tagAttributeMap, defaultValue, deXmiId,
				isCollection);
	}

}
