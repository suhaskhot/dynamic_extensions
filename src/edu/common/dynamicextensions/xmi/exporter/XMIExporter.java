/*
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiReader;
import javax.jmi.xmi.XmiWriter;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Relationship;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.DataTypesPackage;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;
import org.openide.util.Lookup;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.ui.util.SemanticPropertyBuilderUtil;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.common.dynamicextensions.xmi.XMIConstants;
import edu.common.dynamicextensions.xmi.XMIUtilities;
import edu.wustl.common.util.Utility;

/**
 * @author preeti_lodha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XMIExporter implements XMIExportInterface
{
	//Repository
	private static MDRepository repository = MDRManager.getDefault().getDefaultRepository();
	// name of a UML extent (instance of UML metamodel) that the UML models will be loaded into
	private static final String UML_INSTANCE = "UMLInstance";
	// name of a MOF extent that will contain definition of UML metamodel
	private static final String UML_MM = "UML";

	// UML extent
	private static UmlPackage umlPackage;

	//Model
	private static Model umlModel = null;
	//Leaf package
	private static org.omg.uml.modelmanagement.UmlPackage logicalModel = null;
	private static org.omg.uml.modelmanagement.UmlPackage dataModel = null;
	private static HashMap<String, UmlClass> entityUMLClassMappings = null;
	private static HashMap<String, UmlClass> entityDataClassMappings = null;

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.xmi.exporter.XMIExportInterface#exportXMI(java.lang.String, javax.jmi.reflect.RefPackage, java.lang.String)
	 */
	public void exportXMI(String filename, RefPackage extent, String xmiVersion) throws IOException
	{
		//get xmi writer
		XmiWriter writer = XMIUtilities.getXMIWriter();
		//get output stream for file : appendmode : false
		FileOutputStream outputStream = new FileOutputStream(filename,false);

		repository.beginTrans(true);
		try {
			writer.write(outputStream, extent, xmiVersion);
			System.out.println( "XMI written successfully");
		} finally {

			repository.endTrans(true);
			// shutdown the repository to make sure all caches are flushed to disk
			MDRManager.getDefault().shutdownAll();
			outputStream.close();
		}
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.xmi.exporter.XMIExportInterface#exportXMI(java.lang.String, edu.common.dynamicextensions.domaininterface.EntityGroupInterface, java.lang.String)
	 */
	public void exportXMI(String filename, EntityGroupInterface entityGroup, String xmiVersion) throws Exception
	{
		init();
		//UML Model generation
		generateUMLModel(entityGroup);
		//Data Model creation
		generateDataModel(entityGroup);
		exportXMI(filename, umlPackage, xmiVersion);
	} 

	/**
	 * @param entityGroup
	 * @throws DataTypeFactoryInitializationException 
	 */
	private void generateDataModel(EntityGroupInterface entityGroup) throws DataTypeFactoryInitializationException
	{
		if(entityGroup!=null)
		{
			Collection<UmlClass> sqlTableClasses = getDataClasses(entityGroup.getEntityCollection());
			dataModel.getOwnedElement().addAll(sqlTableClasses);
			//Generate relationships between table classes

		}
	}

	/**
	 * @param entityCollection
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Collection<UmlClass> getDataClasses(Collection<EntityInterface> entityCollection) throws DataTypeFactoryInitializationException
	{
		ArrayList<UmlClass> sqlTableClasses = new ArrayList<UmlClass>();
		if(entityCollection!=null)
		{
			Iterator entityCollectionIter = entityCollection.iterator();
			while(entityCollectionIter.hasNext())
			{
				EntityInterface entity = (EntityInterface)entityCollectionIter.next();
				UmlClass sqlTableClass = createDataClass(entity);
				if(sqlTableClass!=null)
				{
					sqlTableClasses.add(sqlTableClass);
					entityDataClassMappings.put(entity.getName(),sqlTableClass);
					//Create dependency with parent
					UmlClass entityUmlClass =(UmlClass) entityUMLClassMappings.get(entity.getName());
					dataModel.getOwnedElement().add(createDependency(entityUmlClass, sqlTableClass));
				}

			}
		}
		return sqlTableClasses;
	}

	/**
	 * @param entity
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private UmlClass createDataClass(EntityInterface entity) throws DataTypeFactoryInitializationException
	{
		UmlClass entityDataClass = null;
		if(entity!=null)
		{
			TablePropertiesInterface tableProps = entity.getTableProperties();
			if(tableProps!=null)
			{
				String tableName = tableProps.getName();
				entityDataClass = umlPackage.getCore().getUmlClass().createUmlClass(tableName, VisibilityKindEnum.VK_PUBLIC, false, false, false, entity.isAbstract(),false );
				//Table stereotype
				entityDataClass.getStereotype().addAll(getOrCreateStereotypes("table", "Class"));
				entityDataClass.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.TABLE));
				entityDataClass.getTaggedValue().add(createTaggedValue("gentype", "MySql"));
				entityDataClass.getTaggedValue().add(createTaggedValue("product_name","MySql"));
				System.out.println(entityDataClass.getFeature());
				//Entity Attributes as columns of data class
				entityDataClass.getFeature().addAll(getDataAttributes(entity.getAllAttributes()));
				//System.out.println("Class SZ" + entityDataClass.getOwnedElement().size());
			}
		}
		return entityDataClass;
	}

	/**
	 * @param allAttributes
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Collection getDataAttributes(Collection<AttributeInterface> entityAttributes) throws DataTypeFactoryInitializationException
	{
		ArrayList<Attribute> tableColumns =  new ArrayList<Attribute>();
		if(entityAttributes!=null)
		{
			Iterator entityAttributesIter = entityAttributes.iterator();
			while(entityAttributesIter.hasNext())
			{
				AttributeInterface attribute = (AttributeInterface)entityAttributesIter.next();
				//System.out.println("Adding col for " + attribute.getName());
				Attribute umlAttribute = createDataAttribute(attribute);
				System.out.println(umlAttribute);
				tableColumns.add(umlAttribute);
			}
		}
		//System.out.println(tableColumns.size());
		return tableColumns;
	}

	/**
	 * @param attribute
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Attribute createDataAttribute(AttributeInterface entityAttribute) throws DataTypeFactoryInitializationException
	{
		Attribute dataColumn = null;
		if(entityAttribute!=null)
		{
			ColumnPropertiesInterface columnProperties = entityAttribute.getColumnProperties();
			System.out.println("Col Prop" + columnProperties) ;
			if(columnProperties!=null)
			{
				//System.out.println(columnProperties.getName());
				String columnName = columnProperties.getName();
				dataColumn =umlPackage.getCore().getAttribute().createAttribute(columnName,VisibilityKindEnum.VK_PUBLIC,
						false,
						ScopeKindEnum.SK_INSTANCE,
						null,
						ChangeableKindEnum.CK_CHANGEABLE,
						ScopeKindEnum.SK_CLASSIFIER,
						OrderingKindEnum.OK_UNORDERED,
						null);
				Classifier typeClass = getOrCreateDataType(DatatypeMappings.get(entityAttribute.getDataType()).getSQLClassMapping());
				dataColumn.setType(typeClass);
				dataColumn.getStereotype().addAll(getOrCreateStereotypes("column", "Attribute"));
				dataColumn.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.COLUMN));
				if(entityAttribute.getIsPrimaryKey())
				{
					dataColumn.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.PRIMARY_KEY));
					dataColumn.getStereotype().addAll(getOrCreateStereotypes("PK", "Attribute"));
				}
				dataColumn.getTaggedValue().add(createTaggedValue("mapped-attributes","newgroup."+entityAttribute.getEntity().getName()+"."+entityAttribute.getName()));
				/* Type of database attribute*/
				System.out.println("Data Attr " + entityAttribute.getName()+" " + dataColumn);
			}

		}
		return dataColumn;
	}

	/**
	 * @param entityGroup
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void generateUMLModel(EntityGroupInterface entityGroup)
	{
		if(entityGroup!=null)
		{
			//Create package for entity group
			org.omg.uml.modelmanagement.UmlPackage umlGroupPackage = getLeafPackage(entityGroup.getName());

			//CLASSES : CREATE : create classes for entities 
			Collection<UmlClass> umlEntityClasses = createUMLClasses(entityGroup.getEntityCollection());
			//CLASSES : ADD : add entity classes to group package
			umlGroupPackage.getOwnedElement().addAll(umlEntityClasses);

			//Relationships  : ASSOCIATIONS/GENERALIZATION/DEPENDENCIES :CREATE
			Collection<Relationship> umlRelationships =  getUMLRelationships(entityGroup.getEntityCollection());
			//Relationships :ADD : Add relationships to package
			umlGroupPackage.getOwnedElement().addAll(umlRelationships);

			//TAGGED VALUES : Create
			Collection<TaggedValue> groupTaggedValues = getTaggedValues(entityGroup);
			//TAGGED VALUES : Add
			umlGroupPackage.getTaggedValue().addAll(groupTaggedValues);
		}
	}

	/**
	 * @param taggedValueCollection
	 * @return
	 */
	private Collection<TaggedValue> getTaggedValues(AbstractMetadataInterface abstractMetadataObj)
	{
		ArrayList<TaggedValue> taggedValues = new ArrayList<TaggedValue>();
		taggedValues.add(createTaggedValue("description",abstractMetadataObj.getDescription()));
		if(abstractMetadataObj.getId()!=null)
		{
			taggedValues.add(createTaggedValue("id",abstractMetadataObj.getId().toString()));
		}
		taggedValues.add(createTaggedValue("publicId",abstractMetadataObj.getPublicId()));
		taggedValues.add(createTaggedValue("createdDate",Utility.parseDateToString(abstractMetadataObj.getCreatedDate(), Constants.DATE_PATTERN_MM_DD_YYYY)));
		taggedValues.add(createTaggedValue("lastUpdated",Utility.parseDateToString(abstractMetadataObj.getLastUpdated(), Constants.DATE_PATTERN_MM_DD_YYYY)));
		taggedValues.add(createTaggedValue("conceptCodes",SemanticPropertyBuilderUtil.getConceptCodeString(abstractMetadataObj.getOrderedSemanticPropertyCollection())));

		Collection<TaggedValueInterface> taggedValueCollection = abstractMetadataObj.getTaggedValueCollection();
		if(taggedValueCollection!=null)
		{
			Iterator<TaggedValueInterface> taggedValueCollnIter = taggedValueCollection.iterator();
			while(taggedValueCollnIter.hasNext())
			{
				taggedValues.add(createTaggedValue(taggedValueCollnIter.next()));
			}
		}
		return taggedValues;
	}

	/**
	 * @param interface1
	 * @return
	 */
	private TaggedValue createTaggedValue(TaggedValueInterface taggedValueIntf)
	{
		if(taggedValueIntf!=null)
		{
			return createTaggedValue(taggedValueIntf.getKey(), taggedValueIntf.getValue());
		}
		return null;
	}

	/**
	 * @param entityCollection
	 * @return
	 */
	private Collection<Relationship> getUMLRelationships(Collection<EntityInterface> entityCollection)
	{
		ArrayList<Relationship>  umlRelationships = new ArrayList<Relationship>();
		if(entityCollection!=null)
		{
			Iterator<EntityInterface> entityCollnIter = entityCollection.iterator();
			while(entityCollnIter.hasNext())
			{
				EntityInterface entity = entityCollnIter.next();
				Collection<Relationship> entityAssociations = createUMLRelationships(entity);
				umlRelationships.addAll(entityAssociations);
			}
		}
		return umlRelationships;
	}

	/**
	 * @param entity
	 * @return
	 */
	private Collection<Relationship> createUMLRelationships(EntityInterface entity)
	{
		ArrayList<Relationship>  entityUMLRelationships = new ArrayList<Relationship>();
		if(entity!=null)
		{
			//Association relationships
			Collection<AssociationInterface> entityAssociations = entity.getAllAssociations();
			if(entityAssociations!=null)
			{
				Iterator<AssociationInterface> entityAssociationsIter = entityAssociations.iterator();
				while(entityAssociationsIter.hasNext())
				{
					AssociationInterface association = entityAssociationsIter.next();
					UmlAssociation umlAssociation = createUMLAssociation(association);
					if(umlAssociation!=null)
					{
						entityUMLRelationships.add(umlAssociation);
					}
				}
			}
			//generalizations
			EntityInterface parentEntity = entity.getParentEntity();
			if(parentEntity!=null)
			{
				Generalization generalization = createGeneralization(parentEntity,entity);
				entityUMLRelationships.add(generalization);
			}
		}
		return entityUMLRelationships;
	}

	/**
	 * @param parentEntity
	 * @param entity
	 */
	private Generalization createGeneralization(EntityInterface parentEntity, EntityInterface childEntity)
	{
		Generalization generalization  = umlPackage.getCore().getGeneralization().createGeneralization("extends",VisibilityKindEnum.VK_PUBLIC,false,null);
		org.omg.uml.foundation.core.GeneralizableElement parent = (org.omg.uml.foundation.core.GeneralizableElement)entityUMLClassMappings.get(parentEntity.getName());
		org.omg.uml.foundation.core.GeneralizableElement child = (org.omg.uml.foundation.core.GeneralizableElement)entityUMLClassMappings.get(childEntity.getName());
		generalization.setParent(parent);
		generalization.setChild(child);
		return generalization;
	}

	/**
	 * @param association
	 * @return
	 */
	private UmlAssociation createUMLAssociation(AssociationInterface association)
	{
		UmlAssociation umlAssociation  = null;
		CorePackage corePackage = umlPackage.getCore();
		if(association!=null)
		{
			umlAssociation = corePackage.getUmlAssociation().createUmlAssociation(association.getName(), VisibilityKindEnum.VK_PUBLIC, false, false, false, false);

			if(umlAssociation!=null)
			{
				//Set the ends
				EntityInterface sourceEntity = association.getEntity();
				if(sourceEntity!=null)
				{
					Classifier sourceClass = getUMLClassForEntity(sourceEntity.getName());
					AssociationEnd sourceEnd = getAssociationEnd(association.getSourceRole(),sourceClass);
					umlAssociation.getConnection().add(sourceEnd);
				}

				EntityInterface targetEntity = association.getTargetEntity();
				if(targetEntity!=null)
				{
					Classifier targetClass = getUMLClassForEntity(targetEntity.getName());
					AssociationEnd targetEnd = getAssociationEnd(association.getTargetRole(),targetClass);
					umlAssociation.getConnection().add(targetEnd);
				}
				//set the direction
				TaggedValue directionTaggedValue =  null;
				if(association.getAssociationDirection().equals(AssociationDirection.BI_DIRECTIONAL))
				{
					directionTaggedValue =  createTaggedValue(XMIConstants.TAGGED_NAME_ASSOC_DIRECTION, XMIConstants.TAGGED_VALUE_ASSOC_BIDIRECTIONAL);
				}
				else
				{
					directionTaggedValue =  createTaggedValue(XMIConstants.TAGGED_NAME_ASSOC_DIRECTION, XMIConstants.TAGGED_VALUE_ASSOC_SRC_DEST);
				}
				if(directionTaggedValue !=null)
				{
					umlAssociation.getTaggedValue().add(directionTaggedValue);
				}
			}
		}
		return umlAssociation;
	}

	/***
	 * Creates a tagged value given the specfied <code>name</code>.
	 *
	 * @param name the name of the tagged value to create.
	 * @param value the value to populate on the tagged value.
	 * @return returns the new TaggedValue
	 */
	protected static TaggedValue createTaggedValue(String name,String value)
	{
		if(name!=null)
		{
			Collection values = new HashSet();
			if(value!=null)
			{
				values.add(value);
			}

			TaggedValue taggedValue =
				umlPackage.getCore().getTaggedValue().createTaggedValue(name, VisibilityKindEnum.VK_PUBLIC, false, values);
			
			return taggedValue;
		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	private Classifier getUMLClassForEntity(String entityName)
	{
		if((entityUMLClassMappings!=null)&&(entityName!=null))
		{
			return (Classifier)entityUMLClassMappings.get(entityName);
		}
		return null;
	}

	/**
	 * @param role
	 * @return
	 */
	private AssociationEnd getAssociationEnd(RoleInterface role,Classifier assocClass)
	{
		int minCardinality = role.getMinimumCardinality().ordinal();
		int maxCardinality = role.getMaximumCardinality().ordinal();
		// primary end association
		AssociationEnd associationEnd =umlPackage.getCore().getAssociationEnd().createAssociationEnd(
				role.getName(),
				VisibilityKindEnum.VK_PUBLIC,
				false,
				true,
				OrderingKindEnum.OK_UNORDERED,
				AggregationKindEnum.AK_NONE,
				ScopeKindEnum.SK_INSTANCE,
				createMultiplicity(umlPackage.getCore().getDataTypes(),minCardinality,maxCardinality),
				ChangeableKindEnum.CK_CHANGEABLE);
		associationEnd.setParticipant(assocClass);
		associationEnd.setNavigable(true);
		
		return associationEnd;
	}

	/**
	 * @param entityCollection
	 * @return
	 */
	private Collection<UmlClass> createUMLClasses(Collection<EntityInterface> entityCollection)
	{
		ArrayList<UmlClass> umlEntityClasses = new ArrayList<UmlClass>();
		if(entityCollection!=null)
		{
			Iterator entityCollectionIter = entityCollection.iterator();
			while(entityCollectionIter.hasNext())
			{
				EntityInterface entity = (EntityInterface)entityCollectionIter.next();
				UmlClass umlEntityClass = createUMLClass(entity);
				if(umlEntityClass!=null)
				{
					umlEntityClasses.add(umlEntityClass);
					entityUMLClassMappings.put(entity.getName(),umlEntityClass);
				}
			}
		}
		return umlEntityClasses;
	}

	/**
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private UmlClass createUMLClass(EntityInterface entity)
	{
		UmlClass umlEntityClass = null;
		if(entity!=null)
		{
			//Get class name , create uml class 
			String className = XMIUtilities.getClassNameForEntity(entity);  
			CorePackage corePackage = umlPackage.getCore();
			umlEntityClass = corePackage.getUmlClass().createUmlClass(className, VisibilityKindEnum.VK_PUBLIC, false, false, false, entity.isAbstract(),false );
			//Create and add attributes to class
			Collection<Attribute> umlEntityAttributes = createUMLAttributes(entity.getAllAttributes());
			umlEntityClass.getFeature().addAll(umlEntityAttributes);
			//Create and add tagged values to entity
			Collection<TaggedValue> entityTaggedValues = getTaggedValues(entity);
			umlEntityClass.getTaggedValue().addAll(entityTaggedValues);
			umlEntityClass.getTaggedValue().add(createTaggedValue("documentation", entity.getName()));
			umlEntityClass.getTaggedValue().add(createTaggedValue("description", entity.getName()));
		}
		return umlEntityClass;
	}

	/**
	 * @param entity
	 * @return
	 */
	private Collection<Attribute> createUMLAttributes(Collection<AttributeInterface> entityAttributes)
	{
		ArrayList<Attribute> umlAttributes =  new ArrayList<Attribute>();
		if(entityAttributes!=null)
		{
			Iterator entityAttributesIter = entityAttributes.iterator();
			while(entityAttributesIter.hasNext())
			{
				AttributeInterface attribute = (AttributeInterface)entityAttributesIter.next();
				Attribute umlAttribute = createUMLAttribute(attribute);
				umlAttributes.add(umlAttribute);
			}
		}
		return umlAttributes;
	}

	/**
	 * @param entityAttribute
	 * @return
	 */
	private Attribute createUMLAttribute(AttributeInterface entityAttribute)
	{
		Attribute umlAttribute = null;
		if(entityAttribute!=null)
		{
			String attributeName = XMIUtilities.getAttributeName(entityAttribute);
			Classifier typeClass = null;
			CorePackage corePackage = umlPackage.getCore();
			umlAttribute =corePackage.getAttribute().createAttribute(attributeName,VisibilityKindEnum.VK_PUBLIC,
					false,
					ScopeKindEnum.SK_INSTANCE,
					createAttributeMultiplicity(corePackage.getDataTypes(),entityAttribute.getIsNullable()),
					ChangeableKindEnum.CK_CHANGEABLE,
					ScopeKindEnum.SK_CLASSIFIER,
					OrderingKindEnum.OK_UNORDERED,
					null);
			typeClass = getOrCreateDataType(DatatypeMappings.get(entityAttribute.getDataType()).getJavaClassMapping());
			umlAttribute.setType(typeClass) ;
			//Tagged Values
			Collection<TaggedValue> attributeTaggedValues = getTaggedValues(entityAttribute);
			umlAttribute.getTaggedValue().addAll(attributeTaggedValues);
			umlAttribute.getTaggedValue().add(createTaggedValue("type", entityAttribute.getDataType()));
			umlAttribute.getTaggedValue().add(createTaggedValue("description", entityAttribute.getDescription()));
			if(entityAttribute.getIsPrimaryKey())
			{
				umlAttribute.getTaggedValue().add(createTaggedValue("stereotype", "PK"));
				umlAttribute.getStereotype().addAll(getOrCreateStereotypes("PK", "Attribute"));
			}
		}
		return umlAttribute;
	}
	/**
	 * @param corePackage
	 * @param string
	 * @return
	 */
	
	private static Classifier getOrCreateDataType(String type)
	{
		//Object datatype = XMIUtilities.find(umlModel, type); 
		
	/*	if (datatype == null)
		{
			datatype = umlPackage.getCore().getDataType().createDataType(
					type, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
			umlModel.getOwnedElement().add(datatype);
		}*/
		Object datatype = XMIUtilities.find(umlModel, type);
		if (datatype == null )
		{
			String[] names = StringUtils.split(type, '.');
			if (names != null && names.length > 0)
			{
				// the last name is the type name
				String typeName = names[names.length - 1];
				names[names.length - 1] = null;
				String packageName = StringUtils.join(names, ".");
				org.omg.uml.modelmanagement.UmlPackage datatypesPackage =
					getOrCreatePackage(packageName, logicalModel);
				//Create Datatype 
				if (datatypesPackage != null)
				{
					datatype = XMIUtilities.find(datatypesPackage, typeName);
					//Create Datatype Class
					if(datatype==null)
					{
						datatype =
						/*	umlPackage.getCore().getDataType().createDataType(
									typeName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);*/
						umlPackage.getCore().getUmlClass().createUmlClass(typeName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false,false );
						datatypesPackage.getOwnedElement().add(datatype);
					}
				}
				else
				{
					datatype = XMIUtilities.find(umlModel, typeName);
					//Create Datatype Class
					if(datatype==null)
					{
						datatype =
							umlPackage.getCore().getDataType().createDataType(
									typeName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
						umlModel.getOwnedElement().add(datatype);
					}
				}
			}
		}
		return (Classifier)datatype;
	}
	/**
	 * @param dataTypes
	 * @param b
	 * @return
	 */
	private static Multiplicity createAttributeMultiplicity(DataTypesPackage dataTypes, boolean required)
	{
		{
			Multiplicity mult = null;
			if (required)
			{
				mult = createMultiplicity(dataTypes, 1, 1);
			}
			else
			{
				mult = createMultiplicity(dataTypes, 0, 1);
			}
			return mult;
		}

	}
	/**
	 * @param dataTypes
	 * @param i
	 * @param j
	 * @return
	 */
	protected static Multiplicity createMultiplicity(
			DataTypesPackage dataTypes,
			int lower,
			int upper)
	{
		Multiplicity mult = dataTypes.getMultiplicity().createMultiplicity();
		MultiplicityRange range = dataTypes.getMultiplicityRange().createMultiplicityRange(lower, upper);
		mult.getRange().add(range);
		return mult;
	}

	protected static Collection getOrCreateStereotypes(String names,String baseClass)
	{
		Collection stereotypes = new HashSet();
		String[] stereotypeNames = null;
		if (names != null)
		{
			stereotypeNames = names.split(",");
		}
		if (stereotypeNames != null && stereotypeNames.length > 0)
		{
			for (int ctr = 0; ctr < stereotypeNames.length; ctr++)
			{
				String name = StringUtils.trimToEmpty(stereotypeNames[ctr]);

				// see if we can find the stereotype first
				Object stereotype = null;//ModelElementFinder.find(this.umlPackage, name);
				if (stereotype == null || !Stereotype.class.isAssignableFrom(stereotype.getClass()))
				{
					Collection baseClasses = new ArrayList();
					baseClasses.add(baseClass);
					stereotype =
						umlPackage.getCore().getStereotype().createStereotype(
								name, VisibilityKindEnum.VK_PUBLIC, false, false, false, false, null, baseClasses);

					umlModel.getOwnedElement().add(stereotype);
				}
				stereotypes.add(stereotype);
			}
		}
		return stereotypes;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private org.omg.uml.modelmanagement.UmlPackage getLeafPackage(String leafName)
	{
		org.omg.uml.modelmanagement.UmlPackage leafPackage = getOrCreatePackage(leafName,logicalModel);
		return leafPackage;
	}
	/**
	 * @param modelManagement
	 * @param umlModel
	 * @param string
	 * @return
	 */
	private static org.omg.uml.modelmanagement.UmlPackage getOrCreatePackage(String packageName,org.omg.uml.modelmanagement.UmlPackage parentPackage)
	{
		ModelManagementPackage modelManagement = umlPackage.getModelManagement();
		/*Object newPackage = XMIUtilities.find(parentPackage, packageName);
		if (newPackage == null)
		{
			newPackage =
				modelManagement.getUmlPackage().createUmlPackage(
						packageName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
			parentPackage.getOwnedElement().add(newPackage);
		}
		return (org.omg.uml.modelmanagement.UmlPackage)newPackage;*/
		Object newPackage = null;
		packageName = StringUtils.trimToEmpty(packageName);
		if(StringUtils.isNotEmpty(packageName))
		{
			//String[] packages = tokenizeStrings(packageName,".");
			StringTokenizer stringTokenizer = new StringTokenizer(packageName,".");
			if (stringTokenizer!= null )
			{
				while(stringTokenizer.hasMoreTokens())
				{
					String token = stringTokenizer.nextToken();
					newPackage = XMIUtilities.find(parentPackage,token );

					if (newPackage == null)
					{
						newPackage =
							modelManagement.getUmlPackage().createUmlPackage(
									token, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
						parentPackage.getOwnedElement().add(newPackage);
					}
					parentPackage = (org.omg.uml.modelmanagement.UmlPackage)newPackage;
				}
			}
		}
		return (org.omg.uml.modelmanagement.UmlPackage)newPackage;
	}

	/**
	 * @param packageName
	 * @param string
	 * @return
	 */
	private static String[] tokenizeStrings(String packageName, String string)
	{
		//	String[]
		return null;
	}

	public void init() throws CreationFailedException, Exception
	{
		initializeUMLPackage();
		initializeModel();
		initializePackageHierarchy();
		entityUMLClassMappings = new HashMap<String,UmlClass>();
		entityDataClassMappings = new HashMap<String,UmlClass>();
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void initializePackageHierarchy()
	{
		org.omg.uml.modelmanagement.UmlPackage logicalView = getOrCreatePackage("Logical View",umlModel);
		logicalModel = getOrCreatePackage("Logical Model",logicalView);
		dataModel = getOrCreatePackage("Data Model",logicalView);
	}

	/**
	 * 
	 */
	private void initializeModel()
	{
		//Initialize model
		ModelManagementPackage modelManagementPackage = umlPackage.getModelManagement();

		//Create Logical Model
		umlModel = modelManagementPackage.getModel().createModel("EA Model", VisibilityKindEnum.VK_PUBLIC, false, false,false,false); 
	}

	/**
	 * @throws Exception 
	 * @throws CreationFailedException 
	 * 
	 */
	private void initializeUMLPackage() throws CreationFailedException, Exception
	{
		//Get UML Package
		umlPackage = (UmlPackage) repository.getExtent(UML_INSTANCE);
		if (umlPackage == null) {
			// UML extent does not exist -> create it (note that in case one want's to instantiate
			// a metamodel other than MOF, they need to provide the second parameter of the createExtent
			// method which indicates the metamodel package that should be instantiated)
			umlPackage = (UmlPackage) repository.createExtent(UML_INSTANCE, getUmlPackage());
		}
	}

	/** Finds "UML" package -> this is the topmost package of UML metamodel - that's the
	 * package that needs to be instantiated in order to create a UML extent
	 */
	private static MofPackage getUmlPackage() throws Exception {
		// get the MOF extent containing definition of UML metamodel
		ModelPackage umlMM = (ModelPackage) repository.getExtent(UML_MM);
		if (umlMM == null) {
			// it is not present -> create it
			umlMM = (ModelPackage) repository.createExtent(UML_MM);
		}
		// find package named "UML" in this extent
		MofPackage result = getUmlPackage(umlMM);
		if (result == null) {
			// it cannot be found -> UML metamodel is not loaded -> load it from XMI
			XmiReader reader = (XmiReader) Lookup.getDefault().lookup(XmiReader.class);
			reader.read(UmlPackage.class.getResource("resources/01-02-15_Diff.xml").toString(), umlMM);
			// try to find the "UML" package again
			result = getUmlPackage(umlMM);
		}
		return result;
	}
	/** Finds "UML" package in a given extent
	 * @param umlMM MOF extent that should be searched for "UML" package.
	 */
	private static MofPackage getUmlPackage(ModelPackage umlMM) {
		// iterate through all instances of package
		for (Iterator it = umlMM.getMofPackage().refAllOfClass().iterator(); it.hasNext();) {
			MofPackage pkg = (MofPackage) it.next();
			// is the package topmost and is it named "UML"?
			if (pkg.getContainer() == null && "UML".equals(pkg.getName())) {
				// yes -> return it
				return pkg;
			}
		}
		// a topmost package named "UML" could not be found
		return null;
	}
	/**
	 * @param core
	 * @param umlPrimaryClass
	 * @param umlDependentClass
	 * @return
	 */
	private static Dependency createDependency(UmlClass umlPrimaryClass, UmlClass umlDependentClass)
	{
		CorePackage corePackage = umlPackage.getCore();
		Dependency dependency = corePackage.getDependency().createDependency(null, VisibilityKindEnum.VK_PUBLIC, false);
		/*corePackage.getAClientClientDependency().add(umlPrimaryClass,dependency);
		corePackage.getASupplierSupplierDependency().add(umlDependentClass, dependency);*/
		corePackage.getAClientClientDependency().add(umlDependentClass,dependency);
		corePackage.getASupplierSupplierDependency().add(umlPrimaryClass, dependency);
		dependency.getStereotype().addAll(getOrCreateStereotypes("DataSource", "Dependency"));
		dependency.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE,"DataSource"));
		return dependency;
	}


	public static void main(String args[]) throws TransformerException
	{
		Variables.databaseName="MYSQL";
		XMIExporter e =  new XMIExporter();
		EntityGroup entityGroup = new EntityGroup();
		entityGroup.setName("newgroup");
		
		EntityInterface entity1  = new Entity();
		entity1.setName("EntityOne");
		EntityInterface entity2  = new Entity();
		entity2.setName("EntityTwo");

		/*EntityInterface entity3  = new Entity();
		entity3.setName("EntityThree");
		EntityInterface entity4  = new Entity();
		entity4.setName("EntityFour");*/

		
		AttributeInterface attributeId = DomainObjectFactory.getInstance().createStringAttribute();
		attributeId.setName("id");
		
		AttributeInterface attribute = DomainObjectFactory.getInstance().createBooleanAttribute();
		attribute.setName("attrib1");
		attribute.setIsPrimaryKey(true);
		entity1.addAttribute(attribute);
		entity1.addAttribute(attributeId);

		AttributeInterface attribute2 = DomainObjectFactory.getInstance().createDateAttribute();
		attribute2.setName("attrib2");
		attribute2.setIsPrimaryKey(true);
		entity2.addAttribute(attribute2);
		AttributeInterface attributeId2 = DomainObjectFactory.getInstance().createStringAttribute();
		attributeId2.setName("id");
		entity2.addAttribute(attributeId2);
		
		Association assoc = new Association();
		assoc.setEntity(entity1);
		assoc.setTargetEntity(entity2);

		assoc.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		
		assoc.setSourceRole(getRole(AssociationType.ASSOCIATION, "entityOne",
				Cardinality.ONE, Cardinality.ONE));
		assoc.setTargetRole(getRole(AssociationType.ASSOCIATION, "entityTwo", Cardinality.ONE, Cardinality.ONE));
		entity1.addAssociation(assoc);

		//Data model
		TableProperties tp = new TableProperties();
		tp.setName("DE_1");
		ColumnProperties cp = new ColumnProperties();
		cp.setName("DE_1_COL1");
		attribute.setColumnProperties(cp);
		ColumnProperties cp4 = new ColumnProperties();
		cp4.setName("DE_1_COL2");
		attributeId.setColumnProperties(cp4);
		entity1.setTableProperties(tp);

		//Entity2 tables
		TableProperties tp2 = new TableProperties();
		tp2.setName("DE_2");
		ColumnProperties cp2 = new ColumnProperties();
		cp2.setName("DE_2_COL1");
		
		ColumnProperties cp3 = new ColumnProperties();
		cp3.setName("DE_2_COL2");
		attributeId2.setColumnProperties(cp3);
		attribute2.setColumnProperties(cp2);
		entity2.setTableProperties(tp2);

		entityGroup.addEntity(entity1);
		entityGroup.addEntity(entity2);
		//entityGroup.addEntity(entity3);
		//	entityGroup.addEntity(entity4);


		try
		{
			e.exportXMI("abc1-4.xmi", entityGroup, "1.1");
		
		
			XMIUtilities.transform(new File("D:/DynamicExtensions/abc1-4.xmi"),"D:/DynamicExtensions/abc1-3.xmi");
			System.out.println("Transformation OK");
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	private static RoleInterface getRole(AssociationType associationType, String name,
			Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}
}
