
package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.UniqueIDGenerator;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

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
		Element xmiRoot = XMISkeletonBuilder.getXMISkeleton(document);
		document.appendChild(xmiRoot);

		// Generate static part of the XMI file
		int deXmiIdIndex = generateStaticXMISection(document, name_IdMap);

		// Generate dynamic section of XMI file
		generateDynamicXMISection(document, entityGroup, name_IdMap, deXmiIdIndex);

		String xmiFileName = entityGroup.getName() + ".xmi";
		/* Write document to XMI file */
		XMIBuilderUtil.writeDOMToXML(document, xmiFileName);
	}

	/**
	 * This method generated static part i.e., fixed part of the XMI structure.
	 * @param document
	 * @param name_IdMap 
	 * @throws DynamicExtensionsApplicationException
	 */
	private int generateStaticXMISection(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		// Retreive the XMI.content element
		Element xmiContentElement = XMIBuilderUtil.getElementByTagAndName(document, "XMI.content",
				null);

		Element namespaceOwnedElement = null;

		// Create and append UML:Model element to XMI:content
		Element eaModelElement = UMLElementGenerator.generateEAModelElement(document);
		xmiContentElement.appendChild(eaModelElement);
		namespaceOwnedElement = appendNamespace_OwnedElement(document, eaModelElement, false);

		// Create and append EARootClass element to UML:Model element 
		Element eaRootClassElement = UMLElementGenerator.generateEARootClassElement(document);
		namespaceOwnedElement.appendChild(eaRootClassElement);

		// Create and append "Logical View" UML:Package element to UML:Model element
		Element logicalViewPackage = generateLogicalViewTree(document, name_IdMap);
		namespaceOwnedElement.appendChild(logicalViewPackage);

		// Create and append UML:DataType elements to UML:Model elemnet
		int deXmiIdIndex = appendUMLDataTypeElementsToUMLModel(document, namespaceOwnedElement,
				name_IdMap);

		return deXmiIdIndex;
	}

	private int appendUMLDataTypeElementsToUMLModel(Document document, Element umlModelPackage,
			HashMap<String, String> name_IdMap) throws DynamicExtensionsApplicationException
	{
		int index = 0;
		String deXmiId = null;

		deXmiId = "dexmiid" + index++;
		Element numberDataType = UMLElementGenerator.generateUMLDataTypeElement(document, deXmiId,
				"NUMBER");
		name_IdMap.put("NUMBER", deXmiId);
		umlModelPackage.appendChild(numberDataType);

		deXmiId = "dexmiid" + index++;
		Element varchar2DataType = UMLElementGenerator.generateUMLDataTypeElement(document,
				deXmiId, "VARCHAR2");
		name_IdMap.put("VARCHAR2", deXmiId);
		umlModelPackage.appendChild(varchar2DataType);

		deXmiId = "dexmiid" + index++;
		Element date = UMLElementGenerator.generateUMLDataTypeElement(document, deXmiId, "DATE");
		name_IdMap.put("DATE", deXmiId);
		umlModelPackage.appendChild(date);

		return index;
	}

	private Element generateLogicalViewTree(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element namespaceOwnedElement = null;
		LinkedHashMap<String, String> propertyMap = null;
		String modelElement = null;

		// Create UML:Package element for "Logical View"
		Element logicalViewPackage = UMLElementGenerator.generateUMLPackageElement(document,
				"Logical View", false);
		namespaceOwnedElement = appendNamespace_OwnedElement(document, logicalViewPackage, true);

		// Append UML:TaggedValues for "Logical View" UML:Pcakage
		propertyMap = createPropertiesForLogicalView(document);
		modelElement = logicalViewPackage.getAttribute("xmi.id");
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		// Create UML:Package element for "Logical Model"
		Element logicalModelPackage = generateLoigcalModelTree(document, name_IdMap);
		namespaceOwnedElement.appendChild(logicalModelPackage);

		return logicalViewPackage;
	}

	private Element generateLoigcalModelTree(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element namespaceOwnedElement = null;
		LinkedHashMap<String, String> propertyMap = null;
		String modelElement = null;

		// Create UML:Package element for "Logical Model"
		Element logicalModelPackage = UMLElementGenerator.generateUMLPackageElement(document,
				"Logical Model", false);
		namespaceOwnedElement = appendNamespace_OwnedElement(document, logicalModelPackage, true);

		// Append UML:TaggedValues for "Logical Model" UML:Pcakage
		propertyMap = createPropertiesForLogicalModel(document);
		modelElement = logicalModelPackage.getAttribute("xmi.id");
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, modelElement);

		// Create UML:Package element for "java"
		Element javaPackage = generateJavaPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(javaPackage);

		return logicalModelPackage;
	}

	private Element generateJavaPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		// Create UML:Package for 'java'
		Element javaPackage = UMLElementGenerator.generateUMLPackageElement(document, "java", true);
		Element namespaceOwnedElement = appendNamespace_OwnedElement(document, javaPackage, true);

		// Create UML:Package for 'lang'
		Element langPackage = generateLangPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(langPackage);

		// Create UML:Pacakage for 'util'
		Element utilPackage = generateUtilPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(utilPackage);

		// Create UML:Pacakage for 'sql'
		Element sqlPackage = generateSqlPackage(document, name_IdMap);
		namespaceOwnedElement.appendChild(sqlPackage);

		return javaPackage;
	}

	private Element generateLangPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element langPackage = UMLElementGenerator.generateUMLPackageElement(document, "lang", true);
		Element namespaceOwnedElement = appendNamespace_OwnedElement(document, langPackage, true);

		boolean createUMLTaggedValues = true, isDataModel = false;
		Element longClass = UMLElementGenerator.generateUMLClassElement(document, "Long",
				langPackage, createUMLTaggedValues, isDataModel);
		namespaceOwnedElement.appendChild(longClass);
		name_IdMap.put(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE, longClass
				.getAttribute("xmi.id"));

		Element booleanClass = UMLElementGenerator.generateUMLClassElement(document, "Boolean",
				langPackage, createUMLTaggedValues, isDataModel);
		namespaceOwnedElement.appendChild(booleanClass);
		name_IdMap.put(EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE, booleanClass
				.getAttribute("xmi.id"));

		Element stringClass = UMLElementGenerator.generateUMLClassElement(document, "String",
				langPackage, createUMLTaggedValues, isDataModel);
		namespaceOwnedElement.appendChild(stringClass);
		name_IdMap.put(EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE, stringClass
				.getAttribute("xmi.id"));

		Element integerClass = UMLElementGenerator.generateUMLClassElement(document, "Integer",
				langPackage, createUMLTaggedValues, isDataModel);
		namespaceOwnedElement.appendChild(integerClass);
		name_IdMap.put(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE, integerClass
				.getAttribute("xmi.id"));

		Element doubleClass = UMLElementGenerator.generateUMLClassElement(document, "Double",
				langPackage, createUMLTaggedValues, isDataModel);
		namespaceOwnedElement.appendChild(doubleClass);
		name_IdMap.put(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE, doubleClass
				.getAttribute("xmi.id"));

		Element floatClass = UMLElementGenerator.generateUMLClassElement(document, "Float",
				langPackage, createUMLTaggedValues, isDataModel);
		namespaceOwnedElement.appendChild(floatClass);
		name_IdMap.put(EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE, floatClass
				.getAttribute("xmi.id"));

		return langPackage;
	}

	private Element generateSqlPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element sqlPackage = UMLElementGenerator.generateUMLPackageElement(document, "sql", true);
		Element namespaceOwnedElement = appendNamespace_OwnedElement(document, sqlPackage, true);

		Element blobInterface = UMLElementGenerator.generateUMLInterfaceElement(document, "Blob",
				sqlPackage, true);
		namespaceOwnedElement.appendChild(blobInterface);
		name_IdMap.put(EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE, blobInterface
				.getAttribute("xmi.id"));

		return sqlPackage;
	}

	private Element generateUtilPackage(Document document, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		Element utilPackage = UMLElementGenerator.generateUMLPackageElement(document, "util", true);
		Element namespaceOwnedElement = appendNamespace_OwnedElement(document, utilPackage, true);

		Element dateClass = UMLElementGenerator.generateUMLClassElement(document, "Date",
				utilPackage, true, false);
		namespaceOwnedElement.appendChild(dateClass);
		name_IdMap.put(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE, dateClass
				.getAttribute("xmi.id"));
		name_IdMap.put(EntityManagerConstantsInterface.DATE_TIME_ATTRIBUTE_TYPE, dateClass
				.getAttribute("xmi.id"));

		return utilPackage;
	}

	private void generateDynamicXMISection(Document document, EntityGroupInterface entityGroup,
			HashMap<String, String> name_IdMap, int deXmiIdIndex)
			throws DynamicExtensionsApplicationException
	{
		Element namespaceOwnedElement = null;
		// Retrieve Logical Model UML:Package element
		Element logicalModelPackage = XMIBuilderUtil.getElementByTagAndName(document,
				"UML:Package", "Logical Model");
		namespaceOwnedElement = (Element) logicalModelPackage.getFirstChild();

		HashMap<String, Element> classNameElementMap = new HashMap<String, Element>();
		// Generate UML:Package the EntityGroup 
		Element groupElement = gernerateXMIFromEntityGroup(document, entityGroup, name_IdMap,
				classNameElementMap);
		namespaceOwnedElement.appendChild(groupElement);

		// Retrieve Logical View UML:Package element
		Element logicalViewPackage = XMIBuilderUtil.getElementByTagAndName(document, "UML:Package",
				"Logical View");
		namespaceOwnedElement = (Element) logicalViewPackage.getFirstChild();

		HashMap<String, Element> tableNameElementMap = new HashMap<String, Element>();
		// Generate XMI of DataModel for the EntityGroup 
		Element dataModelPackage = gernerateDataModelPackage(document, entityGroup, name_IdMap,
				classNameElementMap, tableNameElementMap);
		namespaceOwnedElement.appendChild(dataModelPackage);

		appendStereotypeTags(document, deXmiIdIndex);
	}

	private Element gernerateXMIFromEntityGroup(Document document,
			EntityGroupInterface entityGroup, HashMap<String, String> name_IdMap,
			HashMap<String, Element> classNameElementMap)
			throws DynamicExtensionsApplicationException
	{
		Element groupPackageElement = null;
		if (entityGroup != null)
		{
			String groupName = XMIBuilderUtil.rectifyElementName(entityGroup.getName(),
					ELEMENT_PACKAGE);

			// Create UML:Package element for the Group
			groupPackageElement = UMLElementGenerator.generateUMLPackageElement(document,
					groupName, true);
			Element namespaceOwnedElement = appendNamespace_OwnedElement(document,
					groupPackageElement, true);

			Element entityClassElement = null;

			Collection<EntityInterface> entityCollection = entityGroup.getEntityCollection();
			for (EntityInterface entity : entityCollection)
			{
				String entityName = XMIBuilderUtil.rectifyElementName(entity.getName(),
						ELEMENT_CLASS);
				entityClassElement = classNameElementMap.get(entityName);
				if (entityClassElement == null)
				{
					entityClassElement = generateClassElementFormEntity(document,
							groupPackageElement, name_IdMap, classNameElementMap, entity);
					namespaceOwnedElement.appendChild(entityClassElement);
					classNameElementMap.put(entityName, entityClassElement);
				}
			}
		}
		return groupPackageElement;
	}

	private Element gernerateDataModelPackage(Document document, EntityGroupInterface entityGroup,
			HashMap<String, String> name_IdMap, HashMap<String, Element> classNameElementMap,
			HashMap<String, Element> tableNameElementMap)
			throws DynamicExtensionsApplicationException
	{
		// Create UML:Package element for "Data Model"
		Element dataModelPackage = UMLElementGenerator.generateUMLPackageElement(document,
				"Data Model", true);
		Element namespaceOwnedElement = appendNamespace_OwnedElement(document, dataModelPackage,
				true);

		if (entityGroup != null)
		{
			Element entityClassElement = null;
			Element dependencyElement = null;
			String tableName = null;
			String dataModelClassId = null;
			String logicalModelClassId = null;
			String logicalModelClassName = null;

			Collection<EntityInterface> entityCollection = entityGroup.getEntityCollection();
			String packageName = XMIBuilderUtil.rectifyElementName(entityGroup.getName(),
					ELEMENT_PACKAGE);
			for (EntityInterface entity : entityCollection)
			{
				logicalModelClassName = XMIBuilderUtil.rectifyElementName(entity.getName(),
						ELEMENT_CLASS);
				TablePropertiesInterface tableProperties = entity.getTableProperties();
				tableName = tableProperties.getName();

				entityClassElement = tableNameElementMap.get(tableName);
				if (entityClassElement == null)
				{
					// Append Class element
					entityClassElement = generateClassElementForDataModel(document,
							dataModelPackage, name_IdMap, tableNameElementMap, packageName, entity);
					namespaceOwnedElement.appendChild(entityClassElement);
					tableNameElementMap.put(tableName, entityClassElement);

					// Append Dependency element
					dataModelClassId = entityClassElement.getAttribute("xmi.id");
					Element logicalModelClassElement = classNameElementMap
							.get(logicalModelClassName);
					logicalModelClassId = logicalModelClassElement.getAttribute("xmi.id");

					dependencyElement = generateDependencyTree(document, dataModelClassId,
							logicalModelClassId);
					namespaceOwnedElement.appendChild(dependencyElement);

					appendStereotypeForTable(document, dataModelClassId);
				}
			}
		}
		return dataModelPackage;
	}

	private void appendStereotypeForTable(Document document, String tableId)
			throws DynamicExtensionsApplicationException
	{
		Element xmiContentElement = XMIBuilderUtil.getElementByTagAndName(document,
				ELEMENT_TYPE_XMI_CONTENT, null);
		ArrayList<String> xmiIdList = new ArrayList<String>();
		xmiIdList.add(tableId);

		String xmiId = tableId + "_fix_ster_0";
		Element umlStereotypeDataSource = UMLElementGenerator.generateUMLStereotypeElement(
				document, xmiId, "table", ELEMENT_CLASS, xmiIdList, true, true);
		xmiContentElement.appendChild(umlStereotypeDataSource);
	}

	private void appendStereotypeForCloumn(Document document, String columnId)
			throws DynamicExtensionsApplicationException
	{
		Element xmiContentElement = XMIBuilderUtil.getElementByTagAndName(document,
				ELEMENT_TYPE_XMI_CONTENT, null);
		ArrayList<String> xmiIdList = new ArrayList<String>();
		xmiIdList.add(columnId);

		String xmiId = columnId + "_fix_ster_0";
		Element umlStereotypeDataSource = UMLElementGenerator.generateUMLStereotypeElement(
				document, xmiId, "table", ELEMENT_ATTRIBUTE, xmiIdList, true, true);
		xmiContentElement.appendChild(umlStereotypeDataSource);
	}

	private void appendStereotypeForPrimaryKey()
	{

	}

	private Element generateDependencyTree(Document document, String dataModelClassId,
			String logicalModelClassId) throws DynamicExtensionsApplicationException
	{
		Element umlDependencyElement = UMLElementGenerator.generateUMLDependencyElement(document);
		String dependencyId = umlDependencyElement.getAttribute("xmi.id");

		Element foundation_Core_ModelElement = null;

		// Create UML:Dependency.client
		Element umlDependency_ClientElement = UMLElementGenerator
				.generateUMLDependency_ClientElement(document, dependencyId);
		umlDependencyElement.appendChild(umlDependency_ClientElement);
		String dependency_ClientId = umlDependency_ClientElement.getAttribute("xmi.id");
		// Append Client 
		foundation_Core_ModelElement = UMLElementGenerator.generateFoundation_Core_ModelElement(
				document, dependency_ClientId, dataModelClassId);
		umlDependency_ClientElement.appendChild(foundation_Core_ModelElement);

		//	Create UML:Dependency.supplier
		Element umlDependency_SupplierElement = UMLElementGenerator
				.generateUMLDependency_SupplierElement(document, dependencyId);
		umlDependencyElement.appendChild(umlDependency_SupplierElement);
		String dependency_SupplierId = umlDependency_SupplierElement.getAttribute("xmi.id");
		// Append Supplier
		foundation_Core_ModelElement = UMLElementGenerator.generateFoundation_Core_ModelElement(
				document, dependency_SupplierId, logicalModelClassId);
		umlDependency_SupplierElement.appendChild(foundation_Core_ModelElement);

		return umlDependencyElement;
	}

	private Element generateClassElementFormEntity(Document document, Element groupPackageElement,
			HashMap<String, String> name_IdMap, HashMap<String, Element> classNameElementMap,
			EntityInterface entity) throws DynamicExtensionsApplicationException
	{
		String entityName = entity.getName();
		String groupPackageId = groupPackageElement.getAttribute("xmi.id");
		String groupPackageName = groupPackageElement.getAttribute("name");
		String entityClassName = XMIBuilderUtil.rectifyElementName(entityName, ELEMENT_CLASS);

		// Create UML:Class element for the Entity
		Element entityClassElement = UMLElementGenerator.generateUMLClassElement(document,
				entityClassName, groupPackageElement, false, false);
		String entityClassId = entityClassElement.getAttribute("xmi.id");

		// Append UML:TaggedValue for the Entity UML:Class
		LinkedHashMap<String, String> propertyMap = createPropertiesForUMLClass(entity,
				groupPackageId, groupPackageName);
		XMIBuilderUtil.appendUMLTaggedValuesT0XMIContent(document, propertyMap, entityClassId);

		// Create UML:ClassifierFeature element and append it to the UML:Class element
		Element classifierFeatureElement = UMLElementGenerator.generateClassifierFeatureElement(
				document, entityClassId);
		entityClassElement.appendChild(classifierFeatureElement);
		String classifierFeatureId = classifierFeatureElement.getAttribute("xmi.id");

		// Iterate through all attributes of the Entity and create UML:Attribute element for each.
		// Append each UML:Attribute element to UML:ClassifierFeature element.
		Collection<AbstractAttributeInterface> abstractAttributeCollection = entity
				.getAbstractAttributeCollection();
		int attributeCount = 0, associationCount = 0;
		for (AbstractAttributeInterface abstractAttribute : abstractAttributeCollection)
		{
			if (abstractAttribute instanceof AttributeInterface)
			{
				Element attributeElement = generateUMLAttributeForLogcalModelClass(document,
						name_IdMap, classifierFeatureId, abstractAttribute, attributeCount++);
				classifierFeatureElement.appendChild(attributeElement);
			}
			else if (abstractAttribute instanceof AssociationInterface)
			{
				// Create UML:Association element
				Element associationElement = generateClassAssociationElement(document,
						groupPackageElement, classNameElementMap, name_IdMap, entityClassName,
						entityClassId, abstractAttribute, associationCount++);
				// Append UML:Association element to the Group Package.
				Element namespaceOwnedElement = (Element) groupPackageElement.getFirstChild();
				namespaceOwnedElement.appendChild(associationElement);
			}
		}

		// Handling of Inheritance
		addInheritanceInformation(document, entity, groupPackageElement, entityClassElement,
				classNameElementMap, name_IdMap);

		return entityClassElement;
	}

	private Element generateClassElementForDataModel(Document document,
			Element groupPackageElement, HashMap<String, String> name_IdMap,
			HashMap<String, Element> classNameElementMap, String packageName, EntityInterface entity)
			throws DynamicExtensionsApplicationException
	{
		TablePropertiesInterface tableProperties = entity.getTableProperties();
		String tableName = tableProperties.getName();

		// Create UML:Class element for the Entity
		Element entityClassElement = UMLElementGenerator.generateUMLClassElement(document,
				tableName, groupPackageElement, true, true);
		String entityClassId = entityClassElement.getAttribute("xmi.id");

		// Create UML:ClassifierFeature element and append it to the UML:Class element
		Element classifierFeatureElement = UMLElementGenerator.generateClassifierFeatureElement(
				document, entityClassId);
		entityClassElement.appendChild(classifierFeatureElement);
		String classifierFeatureId = classifierFeatureElement.getAttribute("xmi.id");

		// Iterate through all attributes of the Entity and create UML:Attribute element for each. Then append every 
		// UML:Attribute element to UML:ClassifierFeature element.
		Collection<AbstractAttributeInterface> abstractAttributeCollection = entity
				.getAbstractAttributeCollection();

		String className = XMIBuilderUtil.rectifyElementName(entity.getName(), ELEMENT_CLASS);
		String attributeName = "";
		String logicalModelAttributePath = null;

		int attributeCount = 0;
		for (AbstractAttributeInterface abstractAttribute : abstractAttributeCollection)
		{
			attributeName = XMIBuilderUtil.rectifyElementName(abstractAttribute.getName(),
					ELEMENT_ATTRIBUTE);
			logicalModelAttributePath = packageName + "." + className + "." + attributeName;
			if (abstractAttribute instanceof AttributeInterface)
			{
				Element attributeElement = generateUMLAttributeForDataModelClass(document,
						name_IdMap, classifierFeatureId, abstractAttribute,
						logicalModelAttributePath, attributeCount++);
				classifierFeatureElement.appendChild(attributeElement);

				appendStereotypeForCloumn(document, attributeElement.getAttribute("xmi.id"));
			}
		}

		return entityClassElement;
	}

	private void addInheritanceInformation(Document document, EntityInterface entity,
			Element groupPackageElement, Element entityClassElement,
			HashMap<String, Element> classNameElementMap, HashMap<String, String> name_IdMap)
			throws DynamicExtensionsApplicationException
	{
		EntityInterface parentEntity = entity.getParentEntity();
		if (parentEntity != null)
		{
			String parentName = XMIBuilderUtil.rectifyElementName(parentEntity.getName(),
					ELEMENT_CLASS);
			Element parentClassElement = classNameElementMap.get(parentName);
			if (parentClassElement == null)
			{
				parentClassElement = generateClassElementFormEntity(document, groupPackageElement,
						name_IdMap, classNameElementMap, parentEntity);
				Element namespaceOwnedElement = (Element) groupPackageElement.getFirstChild();
				namespaceOwnedElement.appendChild(parentClassElement);
				classNameElementMap.put(parentName, parentClassElement);
			}
			updateParentAndChildElement(document, parentClassElement, entityClassElement);
		}
	}

	private void updateParentAndChildElement(Document document, Element parentClassElement,
			Element childClassElement) throws DynamicExtensionsApplicationException
	{
		int nextChildId = XMIBuilderUtil.getNextIdForChild(parentClassElement);
		String parentClassId = parentClassElement.getAttribute("xmi.id");
		String childClassId = childClassElement.getAttribute("xmi.id");

		// Update Parent Class
		LinkedHashMap<String, String> elementAttributeMap = null;
		elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", parentClassId + "_fix_" + nextChildId);
		Element namespaceOwnedElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_NAMESPACE_OWNEDELEMENT, elementAttributeMap, null);

		elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("child", childClassId);
		elementAttributeMap.put("parent", parentClassId);
		elementAttributeMap.put("xmi.id", CLASS_ID_PREFIX + UniqueIDGenerator.getId());
		elementAttributeMap.put("visibility", VISIBILITY_PUBLIC);
		Element umlGeneralizationElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_UML_GENERALIZATION, elementAttributeMap, null);

		namespaceOwnedElement.appendChild(umlGeneralizationElement);
		parentClassElement.appendChild(namespaceOwnedElement);

		// Update Child Class
		elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.id", childClassId + "_fix_0");
		Element umlGeneralizableElement_GeneralizationElement = XMIBuilderUtil.createElementNode(
				document, ELEMENT_TYPE_UML_GENERALIZABLEENELEMENT_GENERALIZATION,
				elementAttributeMap, null);

		elementAttributeMap = new LinkedHashMap<String, String>();
		elementAttributeMap.put("xmi.idref", umlGeneralizationElement.getAttribute("xmi.id"));
		elementAttributeMap.put("xmi.id", umlGeneralizableElement_GeneralizationElement
				.getAttribute("xmi.id")
				+ "_fix_0");
		Element foundation_Core_GeneralizationElement = XMIBuilderUtil.createElementNode(document,
				ELEMENT_TYPE_FOUNDATION_CORE_GENERALIZATION, elementAttributeMap, null);

		umlGeneralizableElement_GeneralizationElement
				.appendChild(foundation_Core_GeneralizationElement);
		childClassElement.appendChild(umlGeneralizableElement_GeneralizationElement);
	}

	private Element generateUMLAttributeForLogcalModelClass(Document document,
			HashMap<String, String> name_IdMap, String classifierFeatureId,
			AbstractAttributeInterface abstractAttribute, int attributeCount)
			throws DynamicExtensionsApplicationException
	{
		AttributeInterface attribute = (AttributeInterface) abstractAttribute;

		String attributeName = XMIBuilderUtil.rectifyElementName(attribute.getName(),
				ELEMENT_ATTRIBUTE);
		String defaultValue = ControlsUtility.getDefaultValue(abstractAttribute);
		String description = abstractAttribute.getDescription();
		boolean isCollection = attribute.getIsCollection();

		AttributeTypeInformationInterface attributeTypeInformation = attribute
				.getAttributeTypeInformation();
		String dataType = attributeTypeInformation.getDataType();
		String deXmiId = name_IdMap.get(dataType);
		String type = XMIBuilderUtil.getAttributeType(dataType);

		// Create UML:Attribute element
		Element attributeElement = generateUMLAttributeTree(document, attributeName,
				classifierFeatureId, attributeCount++, defaultValue, isCollection, deXmiId,
				description, type, "", false);

		return attributeElement;
	}

	private Element generateUMLAttributeForDataModelClass(Document document,
			HashMap<String, String> name_IdMap, String classifierFeatureId,
			AbstractAttributeInterface abstractAttribute, String logicalModelAttributePath,
			int attributeCount) throws DynamicExtensionsApplicationException
	{
		AttributeInterface attribute = (AttributeInterface) abstractAttribute;

		ColumnPropertiesInterface columnProperties = attribute.getColumnProperties();
		String columnName = columnProperties.getName();
		String description = abstractAttribute.getDescription();
		boolean isCollection = false;
		String defaultValue = null;

		AttributeTypeInformationInterface attributeTypeInformation = attribute
				.getAttributeTypeInformation();
		String dataType = attributeTypeInformation.getDataType();
		String type = XMIBuilderUtil.getColumnType(dataType);
		String deXmiId = name_IdMap.get(type);

		// Create UML:Attribute element
		Element attributeElement = generateUMLAttributeTree(document, columnName,
				classifierFeatureId, attributeCount++, defaultValue, isCollection, deXmiId,
				description, type, logicalModelAttributePath, true);

		return attributeElement;
	}

	private Element generateUMLAttributeTree(Document document, String columnName,
			String classifierFeatureId, int attributeCount, String defaultValue,
			boolean isCollection, String deXmiId, String description, String type,
			String logicalModelAttributePath, boolean isDataModel)
			throws DynamicExtensionsApplicationException
	{
		Element umlAttributeElement = UMLElementGenerator.generateUMLAttributeElement(document,
				columnName, classifierFeatureId, attributeCount, defaultValue, isCollection,
				deXmiId, description, type, logicalModelAttributePath, isDataModel);
		String umlAttributeXMIId = umlAttributeElement.getAttribute("xmi.id");

		// Create child UML:StructuralFeature.multiplicity element		
		Element umlStructuralFeature_MultiplicityElement = generateUMLStructuralFeature_MultiplicityTree(
				document, umlAttributeXMIId, isCollection);
		umlAttributeElement.appendChild(umlStructuralFeature_MultiplicityElement);

		// Create child UML:Attribute.initialValue
		Element umlAttribute_InitialValueElement = generateUMLAttribute_InitialValueTree(document,
				umlAttributeXMIId, defaultValue);
		umlAttributeElement.appendChild(umlAttribute_InitialValueElement);

		// Create child UML:StructuralFeature.type element
		Element umlStructuralFeature_TypeElement = generateUMLStructuralFeature_TypeTree(document,
				umlAttributeXMIId, deXmiId);
		umlAttributeElement.appendChild(umlStructuralFeature_TypeElement);

		return umlAttributeElement;
	}

	private Element generateUMLStructuralFeature_MultiplicityTree(Document document,
			String umlAttributeXMIId, boolean isCollection)
			throws DynamicExtensionsApplicationException
	{
		Element umlStructuralFeature_MultiplicityElement = UMLElementGenerator
				.generateUMLStructuralFeature_MultiplicityElement(document, umlAttributeXMIId,
						isCollection);

		String structuralFeature_MultiplicityElementId = umlStructuralFeature_MultiplicityElement
				.getAttribute("xmi.id");

		// Create UML:Multiplicity element
		String minCardinality = "1", maxCardinality = "1";
		if (isCollection)
		{
			maxCardinality = "*";
		}

		Element umlMultiplicityElement = generateUMLMultiplicityTree(document,
				structuralFeature_MultiplicityElementId, minCardinality, maxCardinality);
		umlStructuralFeature_MultiplicityElement.appendChild(umlMultiplicityElement);

		return umlStructuralFeature_MultiplicityElement;
	}

	private Element generateUMLMultiplicityTree(Document document, String parentId,
			String minCardinality, String maxCardinality)
			throws DynamicExtensionsApplicationException
	{
		Element umlMultiplicityElement = UMLElementGenerator.generateUMLMultiplicityElement(
				document, parentId, minCardinality, maxCardinality);
		String umlMultiplicityId = umlMultiplicityElement.getAttribute("xmi.id");

		// Create UML:Multiplicity.range element
		Element umlMultiplicity_Range = UMLElementGenerator.generateUMLMultiplicity_RangeElement(
				document, umlMultiplicityId);
		umlMultiplicityElement.appendChild(umlMultiplicity_Range);

		// Create UML:MultiplicityRange element
		Element umlMultiplicityRange = UMLElementGenerator.generateUMLMultiplicityRangeElement(
				document, umlMultiplicityId, minCardinality, maxCardinality);
		umlMultiplicity_Range.appendChild(umlMultiplicityRange);

		return umlMultiplicityElement;
	}

	private Element generateUMLAttribute_InitialValueTree(Document document, String umlAttributeId,
			String defaultValue) throws DynamicExtensionsApplicationException
	{
		Element umlAttribute_InitialValueElement = UMLElementGenerator
				.generateUMLAttribute_InitialValue(document, umlAttributeId);
		String parentId = umlAttribute_InitialValueElement.getAttribute("xmi.id");

		// Create UML:Expression element
		Element umlExpressionElement = UMLElementGenerator.generateUMLExpressionElement(document,
				parentId, defaultValue);
		umlAttribute_InitialValueElement.appendChild(umlExpressionElement);

		return umlAttribute_InitialValueElement;
	}

	private Element generateUMLStructuralFeature_TypeTree(Document document,
			String umlAttributeXMIId, String deXmiId) throws DynamicExtensionsApplicationException
	{
		Element umlStructuralFeature_TypeElement = UMLElementGenerator
				.generateUMLStructuralFeature_Type(document, umlAttributeXMIId, deXmiId);
		String parentId = umlStructuralFeature_TypeElement.getAttribute("xmi.id");

		Element foundation_Core_Classifier = UMLElementGenerator
				.generateFoundation_Core_ClassifierElement(document, parentId, deXmiId);
		umlStructuralFeature_TypeElement.appendChild(foundation_Core_Classifier);

		return umlStructuralFeature_TypeElement;
	}

	private Element generateClassAssociationElement(Document document, Element groupPackageElement,
			HashMap<String, Element> classNameElementMap, HashMap<String, String> name_IdMap,
			String sourceClassName, String sourceClassId,
			AbstractAttributeInterface abstractAttribute, int associationCount)
			throws DynamicExtensionsApplicationException
	{
		AssociationInterface association = (AssociationInterface) abstractAttribute;
		EntityInterface targetEntity = association.getTargetEntity();
		String targetClassName = XMIBuilderUtil.rectifyElementName(targetEntity.getName(),
				ELEMENT_CLASS);

		Element targetClassElement = classNameElementMap.get(targetClassName);
		Element namespaceOwnedElement = (Element) groupPackageElement.getFirstChild();
		if (targetClassElement == null)
		{
			targetClassElement = generateClassElementFormEntity(document, groupPackageElement,
					name_IdMap, classNameElementMap, targetEntity);

			namespaceOwnedElement.appendChild(targetClassElement);
			classNameElementMap.put(targetClassName, targetClassElement);
		}
		String targetClassId = targetClassElement.getAttribute("xmi.id");

		RoleInterface sourceRole = association.getSourceRole();
		RoleInterface targetRole = association.getTargetRole();
		String sourceRoleName = XMIBuilderUtil.rectifyElementName(sourceRole.getName(),
				ELEMENT_ATTRIBUTE);
		String targetRoleName = XMIBuilderUtil.rectifyElementName(targetRole.getName(),
				ELEMENT_ATTRIBUTE);
		Cardinality sourceMinCardinality = sourceRole.getMinimumCardinality();
		Cardinality sourceMaxCardinality = sourceRole.getMaximumCardinality();
		Cardinality targetMinCardinality = targetRole.getMinimumCardinality();
		Cardinality targetMaxCardinality = targetRole.getMaximumCardinality();

		String direction = null;
		AssociationDirection associationDirection = association.getAssociationDirection();
		if (associationDirection.equals(AssociationDirection.BI_DIRECTIONAL))
		{
			direction = DIRECTION_BI;
		}
		else
		{
			direction = DIRECTION_SRC_DEST;
		}

		return generateUMLAssociationTree(document, sourceRoleName, sourceClassId, targetRoleName,
				targetClassId, sourceMinCardinality, sourceMaxCardinality, targetMinCardinality,
				targetMaxCardinality, direction);
	}

	private Element generateUMLAssociationTree(Document document, String sourceRoleName,
			String sourceClassId, String targetRoleName, String targetClassId,
			Cardinality sourceMinCardinality, Cardinality sourceMaxCardinality,
			Cardinality targetMinCardinality, Cardinality targetMaxCardinality, String direction)
			throws DynamicExtensionsApplicationException
	{
		Element umlAssociationElement = UMLElementGenerator.generateUMLAssociationElement(document,
				"", "", direction);
		String parentId = umlAssociationElement.getAttribute("xmi.id");

		// Create UML:Association.connection element
		Element umlAssociation_ConnectionElement = generateUMLAssociation_ConnectionTree(document,
				parentId, sourceRoleName, sourceClassId, targetRoleName, targetClassId,
				sourceMinCardinality, sourceMaxCardinality, targetMinCardinality,
				targetMaxCardinality);
		umlAssociationElement.appendChild(umlAssociation_ConnectionElement);

		return umlAssociationElement;
	}

	private Element generateUMLAssociation_ConnectionTree(Document document, String parentId,
			String sourceRoleName, String sourceClassId, String targetRoleName,
			String targetClassId, Cardinality sourceMinCardinality,
			Cardinality sourceMaxCardinality, Cardinality targetMinCardinality,
			Cardinality targetMaxCardinality) throws DynamicExtensionsApplicationException
	{
		Element umlAssociation_ConnectionElement = UMLElementGenerator
				.generateUMLAssociation_ConnectionElement(document, parentId);
		String association_ConnectionId = umlAssociation_ConnectionElement.getAttribute("xmi.id");

		int index = 0;
		// Create UML:AssociationEnd element for Target
		Element umlAssociationEndTargetElement = generateUMLAssociationEndTree(document,
				targetRoleName, targetClassId, association_ConnectionId, targetMinCardinality,
				targetMaxCardinality, index++);
		umlAssociation_ConnectionElement.appendChild(umlAssociationEndTargetElement);

		// Create UML:AssociationEnd element for Source
		Element umlAssociationEndSourceElement = generateUMLAssociationEndTree(document,
				sourceRoleName, sourceClassId, association_ConnectionId, sourceMinCardinality,
				sourceMaxCardinality, index++);
		umlAssociation_ConnectionElement.appendChild(umlAssociationEndSourceElement);

		return umlAssociation_ConnectionElement;
	}

	private Element generateUMLAssociationEndTree(Document document, String className,
			String classId, String parentId, Cardinality minCardinality,
			Cardinality maxCardinality, int index) throws DynamicExtensionsApplicationException
	{
		Element umlAssociationEndSourceElement = UMLElementGenerator
				.generateUMLAssociationEndElement(document, className, classId, parentId, index);
		String associationEndId = umlAssociationEndSourceElement.getAttribute("xmi.id");

		// UML:AssociationEnd.multiplicity element
		Element umlAssociationEnd_MultiplicityElement = UMLElementGenerator
				.generateUMLAssociationEnd_MultiplicityElement(document, associationEndId);
		umlAssociationEndSourceElement.appendChild(umlAssociationEnd_MultiplicityElement);
		String associationEnd_MultiplicityId = umlAssociationEnd_MultiplicityElement
				.getAttribute("xmi.id");

		// Create UML:Multiplcity element
		String lower = minCardinality.getValue().toString();
		String upper = null;
		if (maxCardinality.equals(Cardinality.MANY))
		{
			upper = "*";
		}
		else
		{
			upper = maxCardinality.getValue().toString();
		}

		Element umlMultiplicityElement = generateUMLMultiplicityTree(document,
				associationEnd_MultiplicityId, lower, upper);
		umlAssociationEnd_MultiplicityElement.appendChild(umlMultiplicityElement);

		return umlAssociationEndSourceElement;
	}

	private void appendStereotypeTags(Document document, int deXmiIdIndex)
			throws DynamicExtensionsApplicationException
	{
		Element xmiContentElement = XMIBuilderUtil.getElementByTagAndName(document, "XMI.content",
				null);
		String deXmiId = null;
		ArrayList<String> xmiIdList = null;

		// Append UML:Stereotype for Class associations
		Element logicalModelPackage = XMIBuilderUtil.getElementByTagAndName(document,
				ELEMENT_TYPE_UML_PACKAGE, "Logical Model");
		xmiIdList = XMIBuilderUtil.getAllXmiIds(logicalModelPackage, ELEMENT_TYPE_UML_ASSOCIATION);
		if (!xmiIdList.isEmpty())
		{
			deXmiId = "dexmiid" + deXmiIdIndex++;
			Element umlStereotypeAssociation = UMLElementGenerator.generateUMLStereotypeElement(
					document, deXmiId, "", ELEMENT_ASSOCIATION, xmiIdList, false, false);
			xmiContentElement.appendChild(umlStereotypeAssociation);
		}

		// Append UML:Stereotype for DataSource (Dependency)
		Element dataModelPackage = XMIBuilderUtil.getElementByTagAndName(document,
				ELEMENT_TYPE_UML_PACKAGE, "Data Model");
		xmiIdList = XMIBuilderUtil.getAllXmiIds(dataModelPackage, ELEMENT_TYPE_UML_DEPENDENCY);
		if (!xmiIdList.isEmpty())
		{
			deXmiId = "dexmiid" + deXmiIdIndex++;
			Element umlStereotypeDataSource = UMLElementGenerator.generateUMLStereotypeElement(
					document, deXmiId, "DataSource", ELEMENT_DEPENDENCY, xmiIdList, false, true);
			xmiContentElement.appendChild(umlStereotypeDataSource);
		}

		// Append UML:Stereotype for FK (Association)

	}

	private Element appendNamespace_OwnedElement(Document document, Element parentElement,
			boolean isPackage) throws DynamicExtensionsApplicationException
	{
		String parentXmiId = parentElement.getAttribute("xmi.id");
		Element namespaceOwnedElement = UMLElementGenerator.generateNamespace_OwnedElement(
				document, parentXmiId, isPackage);
		parentElement.appendChild(namespaceOwnedElement);

		return namespaceOwnedElement;
	}

	private LinkedHashMap<String, String> createPropertiesForLogicalView(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("tpos", "5");
		propertyMap.put("packageFlags", "CRC=0;isModel=1;VICON=3;");
		propertyMap.putAll(UMLElementGenerator.createUMLPackageProperties());

		return propertyMap;
	}

	private LinkedHashMap<String, String> createPropertiesForLogicalModel(Document document)
			throws DynamicExtensionsApplicationException
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		propertyMap.put("tpos", "0");
		propertyMap.putAll(UMLElementGenerator.createUMLPackageProperties());

		return propertyMap;
	}

	private LinkedHashMap<String, String> createPropertiesForUMLClass(EntityInterface entity,
			String packageId, String packageName)
	{
		LinkedHashMap<String, String> propertyMap = new LinkedHashMap<String, String>();
		String description = entity.getDescription();
		propertyMap.put("documentation", description);
		propertyMap.putAll(UMLElementGenerator.createUMLClassProperties(packageId, packageName,
				false));

		return propertyMap;
	}

}
