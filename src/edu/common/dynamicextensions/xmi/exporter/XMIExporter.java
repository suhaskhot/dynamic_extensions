/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.exporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiReader;
import javax.jmi.xmi.XmiWriter;

import org.apache.commons.lang.StringUtils;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.DataType;
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
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.ui.util.SemanticPropertyBuilderUtil;
import edu.common.dynamicextensions.util.global.Constants;
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
	private static HashMap<String, UmlClass> entityUMLClassMappings = null;

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
			System.out.println("XMI Version " + xmiVersion);
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
		generateUMLModel(entityGroup);
		exportXMI(filename, umlPackage, xmiVersion);
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
			
			/*//GENERALIZATION :CREATE
			Collection<GE> umlAssociations =  getUMLAssociations(entityGroup.getEntityCollection());
			//ASSOCIATIONS :ADD : Add associations to package
			umlGroupPackage.getOwnedElement().addAll(umlAssociations);*/
				
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
				System.out.println(entity.getName() + " -> Parent Entity -> " + parentEntity.getName());
				Generalization generalization = createGeneralization(parentEntity,entity);
				entityUMLRelationships.add(generalization);
			}
			//dependencies
			TablePropertiesInterface entityTable = entity.getTableProperties();
			if(entityTable!=null)
			{
				//Table exists. 
				
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
			taggedValue.setType(null);
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
			typeClass = getOrCreateDataType(entityAttribute.getDataType());
			umlAttribute.setType(typeClass);
			//Tagged Values
			Collection<TaggedValue> attributeTaggedValues = getTaggedValues(entityAttribute);
			umlAttribute.getTaggedValue().addAll(attributeTaggedValues);
			/*if(entityAttribute.getIsPrimaryKey())
			{
				umlAttribute.getTaggedValue().add(createTaggedValue("stereotype", "PK"));
				umlAttribute.getStereotype().addAll(getOrCreateStereotypes("PK", "Attribute"));
			}*/
		}
		return umlAttribute;
	}
	/**
	 * @param corePackage
	 * @param string
	 * @return
	 */
	@SuppressWarnings({"unchecked", "unchecked"})
	private static Classifier getOrCreateDataType(String typeName)
	{
		Object datatype = null; 
		org.omg.uml.modelmanagement.UmlPackage datatypesPackage = getOrCreatePackage("DataTypesPkg",logicalModel);
		if (datatypesPackage != null)
		{
			datatype = umlPackage.getCore().getDataType().createDataType(
					typeName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
			datatypesPackage.getOwnedElement().add(datatype);
		}
		return (DataType)datatype;
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
	{/*
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
				String name = "name";//StringUtils.trimToEmpty(stereotypeNames[ctr]);

				// see if we can find the stereotype first
				Object stereotype = null;//ModelElementFinder.find(this.umlPackage, name);
				if (stereotype == null || !Stereotype.class.isAssignableFrom(stereotype.getClass()))
				{
					Collection baseClasses = new ArrayList();
					baseClasses.add(baseClass);
					stereotype =
						umlPackage.getCore().getStereotype().createStereotype(
								name, VisibilityKindEnum.VK_PUBLIC, false, false, false, false, null, baseClasses);

					//this.model.getOwnedElement().add(stereotype);
				}
				stereotypes.add(stereotype);
			}
		}
		return stereotypes;
	*/
		return null;}
	
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
		Object newPackage = XMIUtilities.find(parentPackage, packageName);
		if (newPackage == null)
		{
			newPackage =
				modelManagement.getUmlPackage().createUmlPackage(
						packageName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
			parentPackage.getOwnedElement().add(newPackage);
		}
		/*		org.omg.uml.modelmanagement.UmlPackage newPackage = modelManagement.getUmlPackage().createUmlPackage(packageName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
		parentPackage.getOwnedElement().add(newPackage);*/
		return (org.omg.uml.modelmanagement.UmlPackage)newPackage;
	}

	public void init() throws CreationFailedException, Exception
	{
		initializeUMLPackage();
		initializeModel();
		initializePackageHierarchy();
		entityUMLClassMappings = new HashMap<String,UmlClass>();
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void initializePackageHierarchy()
	{
		org.omg.uml.modelmanagement.UmlPackage logicalView = getOrCreatePackage("Logical View",umlModel);
		logicalModel = getOrCreatePackage("Logical Model",logicalView);
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
		corePackage.getAClientClientDependency().add(umlPrimaryClass,dependency);
		corePackage.getASupplierSupplierDependency().add(umlDependentClass, dependency);
		return dependency;
	}
	
	/**
     * Gets or creates a stereotypes given the specfied comma seperated list of
     * <code>names</code>. If any of the stereotypes can't be found, they
     * will be created.
     *
     * @param names comma seperated list of stereotype names
     * @param baseClass the base class for which the stereotype applies.
     * @return Collection of Stereotypes
     */
    protected Collection getOrCreateStereotypes(
        CorePackage corePackage,
        String names,
        String baseClass)
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
                Object stereotype = XMIUtilities.find(this.umlPackage, name);
                if (stereotype == null || !Stereotype.class.isAssignableFrom(stereotype.getClass()))
                {
                    Collection baseClasses = new ArrayList();
                    baseClasses.add(baseClass);
                    stereotype =
                        corePackage.getStereotype().createStereotype(
                            name, VisibilityKindEnum.VK_PUBLIC, false, false, false, false, null, baseClasses);
                }
                stereotypes.add(stereotype);
            }
        }
        return stereotypes;
    }
	
	public static void main(String args[])
	{

		XMIExporter e =  new XMIExporter();
		EntityGroup entityGroup = new EntityGroup();
		entityGroup.setName("New Group");
		EntityInterface entity1  = new Entity();
		entity1.setName("Entity One");
		EntityInterface entity2  = new Entity();
		entity2.setName("Entity Two");
		EntityInterface entity3  = new Entity();
		entity3.setName("Entity Three");
		EntityInterface entity4  = new Entity();
		entity4.setName("Entity Four");
		AttributeInterface attribute = DomainObjectFactory.getInstance().createBooleanAttribute();
		attribute.setName("Attrib1");
		attribute.setIsPrimaryKey(true);
		entity1.addAttribute(attribute);
		
		AttributeInterface attribute2 = DomainObjectFactory.getInstance().createDateAttribute();
		attribute2.setName("Attrib2");
		entity2.addAttribute(attribute2);
		
		AttributeInterface attribute3 = DomainObjectFactory.getInstance().createIntegerAttribute();
		attribute3.setName("Attrib3");
		entity3.addAttribute(attribute3);
		AttributeInterface attribute4 = DomainObjectFactory.getInstance().createStringAttribute();
		attribute4.setName("Attrib4");
		entity4.addAttribute(attribute4);

		Association assoc = new Association();
		assoc.setEntity(entity1);
		assoc.setTargetEntity(entity2);

		assoc.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		assoc.setName("Assoc1");
		assoc.setSourceRole(getRole(AssociationType.ASSOCIATION, null,
				Cardinality.ONE, Cardinality.MANY));
		assoc.setTargetRole(getRole(AssociationType.ASSOCIATION, entity2
				.getName(), Cardinality.ONE, Cardinality.MANY));
		entity1.addAssociation(assoc);
		
		entity4.setParentEntity(entity3); //Genrln
		entityGroup.addEntity(entity1);
		entityGroup.addEntity(entity2);
		entityGroup.addEntity(entity3);
		entityGroup.addEntity(entity4);


		try
		{
			e.exportXMI("abc.xmi", entityGroup, "1.1");
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
