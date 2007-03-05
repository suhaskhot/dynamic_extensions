
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

	public static void main(String[] args)
	{
		try
		{
			EntityGroupInterface entityGroup = new MockGroup().initializeEntityGroup();

			XMIBuilder xmiBuilder = new XMIBuilder();
			xmiBuilder.exportMetadataToXMI(entityGroup);
		}
		catch (DynamicExtensionsApplicationException dynamicExtensionsApplicationException)
		{
			System.out.print(dynamicExtensionsApplicationException.getMessage());
			System.exit(1);
		}
	}

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

		NodeList umlPackageList = document.getElementsByTagName("UML:Package");
		for (int nodeIndex = 0; nodeIndex < umlPackageList.getLength(); nodeIndex++)
		{
			Element umlPackage = (Element) umlPackageList.item(nodeIndex);
			String packageName = umlPackage.getAttribute("name");
			if (packageName.equals("Logical Model"))
			{
				Element namespaceOwnedElement = (Element) umlPackage.getFirstChild();
				namespaceOwnedElement.appendChild(groupElement);
				break;
			}
		}

		String xmiFileName = entityGroup.getName() + ".xmi";
		/* Write document to XMI file */
		XMIBuilderUtil.writeDOMToXML(document, xmiFileName);
	}

	/**
	 * This method appends children of UML:Paramenter to it.
	 * @param document DOM Tree holder
	 * @param umlParameter UML:Parameter element
	 * @param eaXmiId the value of eaxmiid attribute of the UML:Classifier
	 * @throws DynamicExtensionsApplicationException
	 */
	private void appendUMLParameterChildren(Document document, Element umlParameter, int eaXmiId)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();

		/* UML:Classifier */
		Element element = (Element) umlParameter.getChildNodes().item(0);
		tagAttributeMap.put("xmi.idref", "eaxmiid" + eaXmiId);
		tagAttributeMap.put("xmi.id", element.getAttribute("xmi.id") + "_fix_0");

		element.appendChild(UMLElementBuilder.getUMLClassifier(document, tagAttributeMap));

		element = (Element) umlParameter.getChildNodes().item(1);
		/* UML:Expression */
		tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("xmi.id", element.getAttribute("xmi.id") + "_fix_0");

		element.appendChild(UMLElementBuilder.getUMLExpression(document, tagAttributeMap));
	}

	/**
	 * This method is used by appendUMLDataType() method for creating attribute map for
	 * different datatypes.
	 * @param attributeName - Name of the datatype
	 * @param eaXmiId - Identifier of the datatype element in the Document
	 * @return
	 */
	private LinkedHashMap<String, String> createtagAttributeMap(String dataType, String eaXmiId)
	{
		LinkedHashMap<String, String> tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", dataType);
		tagAttributeMap.put("xmi.id", eaXmiId);
		tagAttributeMap.put("visibility", "protected");
		tagAttributeMap.put("isRoot", "false");
		tagAttributeMap.put("isLeaf", "false");
		tagAttributeMap.put("isAbstract", "false");

		return tagAttributeMap;
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
			Element namespaceOwnedElement = (Element) groupPackageElement.getFirstChild();

			String entityName = null, entityClassId = null;
			Element entityClassElement, classifierFeatureElement;
			Collection<EntityInterface> entityCollection = entityGroup.getEntityCollection();
			for (EntityInterface entity : entityCollection)
			{
				entityName = entity.getName();

				// Create UML:Class element for the Entity
				entityClassElement = generateClassElement(document, entityName, groupPackageId);
				entityClassId = entityClassElement.getAttribute("xmi.id");
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
					}
				}

				// Append the UML:Class element to the UML:Package 
				namespaceOwnedElement.appendChild(entityClassElement);
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
		Element xmiContent = (Element) document.getElementsByTagName("XMI.content").item(0);

		LinkedHashMap<String, String> tagAttributeMap = null;

		// Create and append UML:Model element to XMI:content
		tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", "EA Model");
		tagAttributeMap.put("xmi.id", MODEL_ID_PREFIX + UniqueIDGenerator.getId());
		Element eaModel = UMLElementBuilder.getUMLModel(document, tagAttributeMap);
		xmiContent.appendChild(eaModel);

		Element namespaceOwnedElement = (Element) eaModel.getFirstChild();

		// Create UML:Class
		tagAttributeMap = new LinkedHashMap<String, String>();
		tagAttributeMap.put("name", "EARootClass");
		tagAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		tagAttributeMap.put("isRoot", "true");
		tagAttributeMap.put("isLeaf", "false");
		tagAttributeMap.put("isAbstract", "false");
		Element eaRootClass = UMLElementBuilder.getUMLClass(document, tagAttributeMap);
		namespaceOwnedElement.appendChild(eaRootClass);

		// Create and append UML:Package element
		Element logicalView = generatePackageElement(document, "Logical View");
		namespaceOwnedElement.appendChild(logicalView);

		// Create and append UML:Package element
		Element logicalModelPackage = generatePackageElement(document, "Logical Model");
		namespaceOwnedElement = (Element) logicalView.getFirstChild();
		namespaceOwnedElement.appendChild(logicalModelPackage);

		namespaceOwnedElement = (Element) logicalModelPackage.getFirstChild();
		Element javaPackage = generateJavaPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(javaPackage);
	}

	private Element generateJavaPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element javaPackage = generatePackageElement(document, "java");
		Element namespaceOwnedElement = (Element) javaPackage.getFirstChild();

		Element langPackage = generateLangPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(langPackage);

		Element utilPackage = generateUtilPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(utilPackage);

		Element sqlPackage = generateSqlPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(sqlPackage);

		return javaPackage;
	}

	private Element generateLangPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element langPackage = generatePackageElement(document, "lang");

		Element namespaceOwnedElement = (Element) langPackage.getFirstChild();
		String parenetId = langPackage.getAttribute("xmi.id");

		Element longClass = generateClassElement(document, "Long", parenetId);
		namespaceOwnedElement.appendChild(longClass);
		name_IdMap.put(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE, longClass
				.getAttribute("xmi.id"));

		Element booleanClass = generateClassElement(document, "Boolean", parenetId);
		namespaceOwnedElement.appendChild(booleanClass);
		name_IdMap.put(EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE, booleanClass
				.getAttribute("xmi.id"));

		Element stringClass = generateClassElement(document, "String", parenetId);
		namespaceOwnedElement.appendChild(stringClass);
		name_IdMap.put(EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE, stringClass
				.getAttribute("xmi.id"));

		Element integerClass = generateClassElement(document, "Integer", parenetId);
		namespaceOwnedElement.appendChild(integerClass);
		name_IdMap.put(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE, integerClass
				.getAttribute("xmi.id"));

		Element doubleClass = generateClassElement(document, "Double", parenetId);
		namespaceOwnedElement.appendChild(doubleClass);
		name_IdMap.put(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE, doubleClass
				.getAttribute("xmi.id"));

		Element floatClass = generateClassElement(document, "Float", parenetId);
		namespaceOwnedElement.appendChild(floatClass);
		name_IdMap.put(EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE, floatClass
				.getAttribute("xmi.id"));

		return langPackage;
	}

	private Element generateSqlPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element sqlPackage = generatePackageElement(document, "sql");

		Element namespaceOwnedElement = (Element) sqlPackage.getFirstChild();
		String parenetId = sqlPackage.getAttribute("xmi.id");

		Element blobInterface = generateInterfaceElement(document, "Blob", parenetId);
		name_IdMap.put(EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE, blobInterface
				.getAttribute("xmi.id"));
		namespaceOwnedElement.appendChild(blobInterface);

		return sqlPackage;
	}

	private Element generateUtilPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element utilPackage = generatePackageElement(document, "util");

		Element namespaceOwnedElement = (Element) utilPackage.getFirstChild();
		String parenetId = utilPackage.getAttribute("xmi.id");

		Element dateClass = generateClassElement(document, "Date", parenetId);
		name_IdMap.put(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE, dateClass
				.getAttribute("xmi.id"));
		name_IdMap.put(EntityManagerConstantsInterface.DATE_TIME_ATTRIBUTE_TYPE, dateClass
				.getAttribute("xmi.id"));
		namespaceOwnedElement.appendChild(dateClass);

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
