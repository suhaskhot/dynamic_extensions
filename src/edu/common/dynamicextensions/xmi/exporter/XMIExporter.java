/*
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
import java.util.List;
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
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Relationship;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.CallConcurrencyKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.DataTypesPackage;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;
import org.openide.util.Lookup;

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
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
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
 */
public class XMIExporter implements XMIExportInterface
{
	//Repository
	private static MDRepository repository = XMIUtilities.getRepository();
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
	private static HashMap<EntityInterface, List<String>> entityForeignKeyAttributes = null;
	private static HashMap<String, String> foreignKeyOperationNameMappings = null;
	private static String groupName = null;

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.xmi.exporter.XMIExportInterface#exportXMI(java.lang.String, javax.jmi.reflect.RefPackage, java.lang.String)
	 */
	public void exportXMI(String filename, RefPackage extent, String xmiVersion) throws IOException, TransformerException
	{
		//get xmi writer
		XmiWriter writer = XMIUtilities.getXMIWriter();
		String outputFilename = filename;
		if(XMIConstants.XMI_VERSION_1_1.equals(xmiVersion))
		{
			//Write to temporary file
			outputFilename = XMIConstants.TEMPORARY_XMI1_1_FILENAME; 
		}
			
		//get output stream for file : appendmode : false
		FileOutputStream outputStream = new FileOutputStream(outputFilename,false);
		repository.beginTrans(true);
		try {
			writer.write(outputStream, extent, xmiVersion);
			if(XMIConstants.XMI_VERSION_1_1.equals(xmiVersion))
			{
				convertXMI(outputFilename,filename);
			}
			System.out.println( "XMI written successfully");
		} finally {
			repository.endTrans(true);
			// shutdown the repository to make sure all caches are flushed to disk
			MDRManager.getDefault().shutdownAll();
			XMIUtilities.cleanUpRepository();
			outputStream.close();
			if((new File(XMIConstants.TEMPORARY_XMI1_1_FILENAME)).exists())
			{
				(new File(XMIConstants.TEMPORARY_XMI1_1_FILENAME)).delete();
			}
		}
	}

	/**
	 * @param srcFilename 
	 * @param targetFilename 
	 * @throws TransformerException 
	 * @throws FileNotFoundException 
	 * 
	 */
	private void convertXMI(String srcFilename, String targetFilename) throws FileNotFoundException, TransformerException
	{
		XMIUtilities.transform(srcFilename, targetFilename);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.xmi.exporter.XMIExportInterface#exportXMI(java.lang.String, edu.common.dynamicextensions.domaininterface.EntityGroupInterface, java.lang.String)
	 */
	public void exportXMI(String filename, EntityGroupInterface entityGroup, String xmiVersion) throws Exception
	{
		init();
		if(entityGroup!=null)
		{
			groupName = entityGroup.getName();
			
			preProcessGroup(entityGroup);
			//UML Model generation
			generateUMLModel(entityGroup);
			//Data Model creation
			generateDataModel(entityGroup);
			exportXMI(filename, umlPackage, xmiVersion);
		}
	} 

	/**
	 * @param entityGroup
	 */
	private void preProcessGroup(EntityGroupInterface entityGroup)
	{
		removeDuplicateAttributes(entityGroup.getEntityCollection());
	}

	/** Remove duplicate attributes introduced due to inheritence
	 * @param entityCollection
	 */
	private void removeDuplicateAttributes(Collection<EntityInterface> entityCollection)
	{
		if(entityCollection!=null)
		{
			Iterator entityIter = entityCollection.iterator();
			while(entityIter.hasNext())
			{
				EntityInterface entity = (EntityInterface)entityIter.next();
				if((entity!=null)&&(entity.getParentEntity()!=null))
				{
					removeParentAttributesInChild(entity,entity.getParentEntity());
				}
			}
		}
	}

	/** Remove duplicate attributes introduced due to inheritence
	 * @param childEntity
	 * @param parentEntity
	 */
	private void removeParentAttributesInChild(EntityInterface childEntity, EntityInterface parentEntity)
	{
		if(childEntity!=null && parentEntity!=null)
		{
			//Compare each child attribute with each parent attribute
			Collection<AttributeInterface> childAttributes = childEntity.getAllAttributes();
			Collection<AttributeInterface> parentAttributes = parentEntity.getAllAttributes();
			for(AttributeInterface childAttribute : childAttributes)
			{
				for(AttributeInterface parentAttribute : parentAttributes)
				{
					if((parentAttribute!=null)&&(parentAttribute.equals(childAttribute)))
					{
						childEntity.removeAttribute(childAttribute);
						break;
					}
				}
			}
		}
	}

	/**
	 * @param entityGroup
	 * @throws DataTypeFactoryInitializationException 
	 */
	private void generateDataModel(EntityGroupInterface entityGroup) throws DataTypeFactoryInitializationException
	{
		if(entityGroup!=null)
		{
			initializeEntityForeignKeysMap(entityGroup.getEntityCollection());
			Collection<UmlClass> sqlTableClasses = getDataClasses(entityGroup.getEntityCollection());
			dataModel.getOwnedElement().addAll(sqlTableClasses);

			//Generate relationships between table classes
			Collection<Relationship> sqlRelationships = getSQLRelationShips(entityGroup.getEntityCollection());
			dataModel.getOwnedElement().addAll(sqlRelationships);
		}
	}

	/**
	 * @param entityCollection
	 */
	private void initializeEntityForeignKeysMap(Collection<EntityInterface> entityCollection)
	{
		entityForeignKeyAttributes = new HashMap<EntityInterface, List<String>>();
		if(entityCollection!=null)
		{
			Iterator entityCollnIter = entityCollection.iterator();
			while(entityCollnIter.hasNext())
			{
				EntityInterface entity = (EntityInterface)entityCollnIter.next();
				if(entity!=null)
				{
					Collection<AssociationInterface> entityAssociations = entity.getAllAssociations();
					if(entityAssociations!=null)
					{
						Iterator entityAssocnCollnIter = entityAssociations.iterator();
						while(entityAssocnCollnIter.hasNext())
						{
							AssociationInterface association = (AssociationInterface)entityAssocnCollnIter.next();
							if((association!=null)&&(association.getConstraintProperties()!=null))
							{
								String associationType = getAssociationType(association);
								String foreignKeyOperationName = null;
								String foreignKey = null;
								//For one-to-one and one-to-many association foreign key is in target entity
								if((associationType.equals(XMIConstants.ASSOC_ONE_MANY))||(associationType.equals(XMIConstants.ASSOC_ONE_ONE)))
								{
									foreignKey = association.getConstraintProperties().getTargetEntityKey();
									setForeignKeyOperationName(association.getTargetEntity(),foreignKey);
									foreignKeyOperationName = generateForeignkeyOperationName(association.getTargetEntity().getName(),association.getEntity().getName());
//									Generate foreign key operation name and add it to foreignKeyOperationNameMappings map
									foreignKeyOperationNameMappings.put(foreignKey, foreignKeyOperationName);
								}
								//For many-to-one association foreign key is in source entity
								else if(associationType.equals(XMIConstants.ASSOC_MANY_ONE))
								{
									foreignKey = association.getConstraintProperties().getSourceEntityKey();
									setForeignKeyOperationName(association.getEntity(),foreignKey);
									foreignKeyOperationName = generateForeignkeyOperationName(association.getEntity().getName(),association.getTargetEntity().getName());
//									Generate foreign key operation name and add it to foreignKeyOperationNameMappings map
									foreignKeyOperationNameMappings.put(foreignKey, foreignKeyOperationName);
								}
								/*//For Many-Many : both entities will have foreign keys
								else if(associationType.equals(XMIConstants.ASSOC_MANY_MANY))
								{
									//for source
									foreignKey = association.getConstraintProperties().getSourceEntityKey();
									setForeignKeyOperationName(association.getEntity(),foreignKey);
									foreignKeyOperationName = generateForeignkeyOperationName(association.getEntity().getName(),association.getConstraintProperties().getName());
//									Generate foreign key operation name and add it to foreignKeyOperationNameMappings map
									foreignKeyOperationNameMappings.put(foreignKey, foreignKeyOperationName);
									//For target entity 
									foreignKey = association.getConstraintProperties().getTargetEntityKey();
									setForeignKeyOperationName(association.getTargetEntity(),foreignKey);
									foreignKeyOperationName = generateForeignkeyOperationName(association.getConstraintProperties().getName(),association.getTargetEntity().getName());
//									Generate foreign key operation name and add it to foreignKeyOperationNameMappings map
									foreignKeyOperationNameMappings.put(foreignKey, foreignKeyOperationName);
								}*/
								
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param targetEntity
	 * @param entity
	 * @return
	 */
	private String generateForeignkeyOperationName(String foreignKeyEntityName, String primaryKeyEntityName)
	{
		return ("FK_" + foreignKeyEntityName + "_" + primaryKeyEntityName);
	}

	/**
	 * @param targetEntity
	 * @param targetEntityKey
	 */
	private void setForeignKeyOperationName(EntityInterface entity, String entityForeignKeyAttribute)
	{
		if((entity!=null)&&(entityForeignKeyAttribute!=null))
		{
			List<String> foreignKeys = entityForeignKeyAttributes.get(entity);
			if(foreignKeys==null)
			{
				foreignKeys=new ArrayList<String>();
			}
			foreignKeys.add(entityForeignKeyAttribute);
			entityForeignKeyAttributes.put(entity, foreignKeys);
		}
	}

	/**
	 * @param entityCollection
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Collection<Relationship> getSQLRelationShips(Collection<EntityInterface> entityCollection) throws DataTypeFactoryInitializationException
	{
		ArrayList<Relationship>  sqlRelationships = new ArrayList<Relationship>();
		if(entityCollection!=null)
		{
			Iterator<EntityInterface> entityCollnIter = entityCollection.iterator();
			while(entityCollnIter.hasNext())
			{
				EntityInterface entity = entityCollnIter.next();
				Collection<Relationship> entitySQLAssociations = createSQLRelationships(entity);
				sqlRelationships.addAll(entitySQLAssociations);
			}
		}
		return sqlRelationships;
	}

	/**
	 * @param entity
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Collection<Relationship> createSQLRelationships(EntityInterface entity) throws DataTypeFactoryInitializationException
	{
		//Associations
		ArrayList<Relationship>  entitySQLRelationships = new ArrayList<Relationship>();
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
					//For each association add the sourceEntityKey as foreign key operation
					UmlAssociation sqlAssociation = createSQLAssociation(association);
					if(sqlAssociation!=null)
					{
						entitySQLRelationships.add(sqlAssociation);
					}
				}
			}
			//Generalization relationships
			if(entity.getParentEntity()!=null)
			{
				UmlClass parentClass = entityDataClassMappings.get(entity.getParentEntity().getName());
				UmlClass childClass = entityDataClassMappings.get(entity.getName());
				Generalization generalization  = createGeneralization(parentClass, childClass);
				entitySQLRelationships.add(generalization);
			}
		}
		return entitySQLRelationships;
	}


	/**
	 * @param association
	 * @param targetEntityAttribute 
	 * @return
	 */
	private Operation createForeignKeyOperation(String foreignKeyAttribute, Attribute foreignKeyColumn)
	{
		String foreignKeyOperationName = getForeignkeyOperationName(foreignKeyAttribute);
		Operation foreignKeyOperation = umlPackage.getCore().getOperation().createOperation(foreignKeyOperationName, VisibilityKindEnum.VK_PUBLIC,false,ScopeKindEnum.SK_INSTANCE, false, CallConcurrencyKindEnum.CCK_SEQUENTIAL,false,false,false,null);

		foreignKeyOperation.getStereotype().addAll(getOrCreateStereotypes(XMIConstants.FOREIGN_KEY, XMIConstants.STEREOTYPE_BASECLASS_ATTRIBUTE));
		foreignKeyOperation.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.FOREIGN_KEY));

		//Return parameter
		Parameter returnParameter = createParameter(null,null,ParameterDirectionKindEnum.PDK_RETURN);
		Parameter inParameter = createParameter(foreignKeyColumn.getName(),foreignKeyColumn.getType(),ParameterDirectionKindEnum.PDK_IN);
		inParameter.getTaggedValue().add(createTaggedValue(XMIConstants.TYPE, foreignKeyColumn.getType().getName()));

		foreignKeyOperation.getParameter().add(returnParameter);
		foreignKeyOperation.getParameter().add(inParameter);
		foreignKeyOperationNameMappings.put(foreignKeyAttribute, foreignKeyOperationName);
		return foreignKeyOperation;
	}

	/**
	 * @param name
	 * @return
	 */
	private String getForeignkeyOperationName(String foreignKeyName)
	{
		if((foreignKeyOperationNameMappings!=null)&&(foreignKeyName!=null))
		{
			return ((String)foreignKeyOperationNameMappings.get(foreignKeyName));
		}
		return null;
	}

	/**
	 * @param association
	 * @return
	 */
	private String getForeignkeyOperationName(AssociationInterface association)
	{
		return ("FK_" + association.getTargetEntity().getName() + "_" + association.getEntity().getName());
	}

	/**
	 * @return
	 */
	private Parameter createParameter(String paramterName,Classifier parameterType,ParameterDirectionKindEnum direction)
	{
		//Return parameter
		Parameter parameter = umlPackage.getCore().getParameter().createParameter(paramterName,VisibilityKindEnum.VK_PUBLIC,false,null,direction);
		parameter.setType(parameterType);
		return parameter;
	}

	/**
	 * @param association
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private UmlAssociation createSQLAssociation(AssociationInterface association) throws DataTypeFactoryInitializationException
	{
		UmlAssociation sqlAssociation  = null;
		CorePackage corePackage = umlPackage.getCore();
		if((association!=null)&&(association.getConstraintProperties()!=null))
		{
			ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties(); 
			String associationName = getAssociationName(association);
			sqlAssociation = corePackage.getUmlAssociation().createUmlAssociation(associationName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
			if(sqlAssociation!=null)
			{
				//Set the ends for the association 
				//End that is on the 'many' side of the association will have foreign key oprn name & on 'one' side will be primary key oprn name
				String sourceEndName = null, targetEndName = null;
				String associationType = getAssociationType(association);
				Classifier sourceSQLClass = getSQLClassForEntity(association.getEntity().getName());
				Classifier targetSQLClass = getSQLClassForEntity(association.getTargetEntity().getName());
				RoleInterface sourceRole = association.getSourceRole();
				RoleInterface targetRole = association.getTargetRole();

				if((associationType.equals(XMIConstants.ASSOC_ONE_ONE))||(associationType.equals(XMIConstants.ASSOC_ONE_MANY)))
				{
//					Add "implements-association tag value to target entity(foreign key entity)
					Attribute foreignKeySQLAttribute = getSQLAttribute(constraintProperties.getTargetEntityKey(),association.getTargetEntity());
					String implementedAssociation = groupName + "."+association.getTargetEntity().getName()+"." +association.getSourceRole().getName();
					foreignKeySQLAttribute.getTaggedValue().add(createTaggedValue("implements-association",implementedAssociation));
					//One-One OR One-Many source will have primary key, target has foreign key
					sourceEndName = getPrimaryKeyOperationName(association.getEntity().getName(),constraintProperties.getSourceEntityKey());
					targetEndName = getForeignkeyOperationName(constraintProperties.getTargetEntityKey());
					sourceRole.setName(sourceEndName);
					targetRole.setName(targetEndName);
					sqlAssociation.getConnection().add(getAssociationEnd(sourceRole,sourceSQLClass));
					sqlAssociation.getConnection().add(getAssociationEnd(targetRole,targetSQLClass));
					
				}
				else if(associationType.equals(XMIConstants.ASSOC_MANY_ONE))
				{
//					Add "implements-association tag value to source entity(foreign key entity)
					Attribute foreignKeySQLAttribute = getSQLAttribute(constraintProperties.getSourceEntityKey(),association.getEntity());
					String implementedAssociation = groupName + "."+association.getEntity().getName()+"." +association.getTargetRole().getName();
					foreignKeySQLAttribute.getTaggedValue().add(createTaggedValue("implements-association",implementedAssociation));
					//Many-One source will have foreign key, target primary key
					sourceEndName = getForeignkeyOperationName(constraintProperties.getTargetEntityKey());
					targetEndName = getPrimaryKeyOperationName(association.getTargetEntity().getName(),constraintProperties.getSourceEntityKey());
					sourceRole.setName(sourceEndName);
					targetRole.setName(targetEndName);
					sqlAssociation.getConnection().add(getAssociationEnd(sourceRole,sourceSQLClass));
					sqlAssociation.getConnection().add(getAssociationEnd(targetRole,targetSQLClass));
					
				}
				else if(associationType.equals(XMIConstants.ASSOC_MANY_MANY))
				{
					handleManyToManyAssociation(association);
					return null;
				}

				//set the direction
				TaggedValue directionTaggedValue =  getDirectionTaggedValue(association);
				if(directionTaggedValue !=null)
				{
					sqlAssociation.getTaggedValue().add(directionTaggedValue);
				}
				sqlAssociation.getStereotype().addAll(getOrCreateStereotypes(XMIConstants.FOREIGN_KEY,"Association"));
				sqlAssociation.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE,XMIConstants.FOREIGN_KEY));
			}
		}
		return sqlAssociation;

	}

	/**
	 * @param constraintProperties
	 * @throws DataTypeFactoryInitializationException 
	 */
	private void handleManyToManyAssociation(AssociationInterface association) throws DataTypeFactoryInitializationException
	{
		//Create corelation table
		UmlClass corelationTable = createCoRelationTable(association);

		if(corelationTable!=null)
		{
			//Relation with source entity
			UmlClass sourceClass = (UmlClass)getSQLClassForEntity(association.getEntity().getName());
			UmlClass targetClass = (UmlClass)getSQLClassForEntity(association.getTargetEntity().getName());
			RoleInterface role = getRole(AssociationType.ASSOCIATION, null,Cardinality.ONE,Cardinality.ONE);
			UmlAssociation srcToCoRelnTable = createAssocForCorelnTable(sourceClass,corelationTable,association.getSourceRole(),role);
			//Create relation with target entity
			UmlAssociation coRelnTableToTarget = createAssocForCorelnTable(corelationTable,targetClass,role,association.getTargetRole());
			//Add to data model
			dataModel.getOwnedElement().add(corelationTable);
			dataModel.getOwnedElement().add(coRelnTableToTarget);
			dataModel.getOwnedElement().add(srcToCoRelnTable);
		}
	}

	/**
	 * @param association
	 * @return
	 */
	private UmlAssociation createAssocForCorelnTable(UmlClass sourceEntity,UmlClass targetEntity,RoleInterface sourceRole,RoleInterface targetRole)
	{
		UmlAssociation umlAssociation = umlPackage.getCore().getUmlAssociation().createUmlAssociation(null, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
		
		AssociationEnd sourceEnd = getAssociationEnd(sourceRole,sourceEntity);
		AssociationEnd targetEnd = getAssociationEnd(targetRole,targetEntity);
		umlAssociation.getConnection().add(targetEnd);
		umlAssociation.getConnection().add(sourceEnd);
		umlAssociation.getTaggedValue().add(createTaggedValue(XMIConstants.TAGGED_NAME_ASSOC_DIRECTION,XMIConstants.TAGGED_VALUE_ASSOC_SRC_DEST));
		return umlAssociation;
	}

	/**
	 * @param association
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private UmlClass createCoRelationTable(AssociationInterface association) throws DataTypeFactoryInitializationException
	{
		ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties();
		if(constraintProperties!=null)
		{
			String coRelationTableName = constraintProperties.getName();
			UmlClass corelationClass = createDataClass(coRelationTableName);
			
			Collection<Feature> coRelationAttributes = createCoRelationTableAttribsAndOperns(coRelationTableName,association);
			//Add to co-relation class
			corelationClass.getFeature().addAll(coRelationAttributes);

			return corelationClass;
		}
		return null;
	}


	/**
	 * @param association
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Collection<Feature> createCoRelationTableAttribsAndOperns(String coRelationTableName,AssociationInterface association) throws DataTypeFactoryInitializationException
	{
		ArrayList<Feature> corelationTableFeatures = new ArrayList<Feature>();
		ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties(); 
//		Create attributes for class
		/*AttributeInterface sourceAttribute = searchAttribute(association.getEntity(), constraintProperties.getSourceEntityKey());
		AttributeInterface targetAttribute = searchAttribute(association.getTargetEntity(), constraintProperties.getTargetEntityKey());*/
		//Search primary keys of tables 
		AttributeInterface sourceAttribute = getPrimaryKeyAttribute(association.getEntity());
		AttributeInterface targetAttribute = getPrimaryKeyAttribute(association.getTargetEntity());
		
		if((sourceAttribute!=null)&&(targetAttribute!=null))
		{
			//Create corelation table attributes
			String corelationTableSrcAttributeName = generateCorelationAttributeName(association.getEntity(),constraintProperties.getSourceEntityKey());
			String corelationTableDestAttributeName = generateCorelationAttributeName(association.getTargetEntity(),constraintProperties.getTargetEntityKey());
			
			Attribute coRelationSourceAttribute = createDataAttribute(corelationTableSrcAttributeName, sourceAttribute.getDataType());
			Attribute coRelationTargetAttribute = createDataAttribute(corelationTableDestAttributeName, targetAttribute.getDataType());
	
			//Add "implements-association tagged value for both
			String srcAttribImplementedAssocn = groupName+"."+ association.getTargetEntity().getName()+"."+association.getSourceRole().getName();
			coRelationSourceAttribute.getTaggedValue().add(createTaggedValue("implements-association",srcAttribImplementedAssocn));
			
			String targetAttribImplementedAssocn = groupName+"."+ association.getEntity().getName()+"."+association.getTargetRole().getName();
			coRelationTargetAttribute.getTaggedValue().add(createTaggedValue("implements-association",targetAttribImplementedAssocn));
			
			corelationTableFeatures.add(coRelationSourceAttribute);
			corelationTableFeatures.add(coRelationTargetAttribute);
		
		
//		Add primary keys to mappings 
		String srcForeignKeyOprName = generateForeignkeyOperationName(association.getEntity().getName(), constraintProperties.getName());
		String targetForeignKeyOprName = generateForeignkeyOperationName(constraintProperties.getName(), association.getTargetEntity().getName());
		foreignKeyOperationNameMappings.put(constraintProperties.getSourceEntityKey(), srcForeignKeyOprName);
		foreignKeyOperationNameMappings.put(constraintProperties.getTargetEntityKey(), targetForeignKeyOprName);
		
		//Add foreign keys
		corelationTableFeatures.add(createForeignKeyOperation(constraintProperties.getSourceEntityKey(), coRelationSourceAttribute));
		corelationTableFeatures.add(createForeignKeyOperation(constraintProperties.getTargetEntityKey(), coRelationTargetAttribute));
		//Add primary keys
		/*String primaryKeyOperationName = getPrimaryKeyOperationName(coRelationTableName, corelationTableSrcAttributeName);
		corelationTableFeatures.add(createPrimaryKeyOperation(primaryKeyOperationName,sourceAttribute.getDataType(),coRelationSourceAttribute));
		
		primaryKeyOperationName = getPrimaryKeyOperationName(coRelationTableName, corelationTableDestAttributeName);
		corelationTableFeatures.add(createPrimaryKeyOperation(primaryKeyOperationName,targetAttribute.getDataType(),coRelationTargetAttribute));*/
		}
		return corelationTableFeatures;
	}

	/**
	 * @param entity
	 * @return
	 */
	private AttributeInterface getPrimaryKeyAttribute(EntityInterface entity)
	{
		if(entity!=null)
		{
			Collection<AttributeInterface> attributes = entity.getAllAttributes();
			if(attributes!=null)
			{
				Iterator attributesIter = attributes.iterator();
				while(attributesIter.hasNext())
				{
					AttributeInterface attribute =(AttributeInterface)attributesIter.next();
					if((attribute!=null)&&(attribute.getIsPrimaryKey()))
					{
						return attribute;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Generates qualified name for the corelation table attributes
	 * Column name generated as <EntityName>_<EntityAttribute>  
	 * @param entity
	 * @param sourceEntityKey
	 * @return
	 */
	private String generateCorelationAttributeName(EntityInterface entity, String sourceEntityKey)
	{
		return entity.getName()+"_"+sourceEntityKey;
	}

	/**
	 * @param corelationAttributeSrc
	 * @param sourceAttribute
	 */
	private void initializeCoRelationAttribute(AttributeInterface corelationAttribute, AttributeInterface attribute)
	{
		if((corelationAttribute!=null)&&(attribute!=null))
		{
			corelationAttribute.setName(attribute.getName());
			corelationAttribute.setAttributeTypeInformation(attribute.getAttributeTypeInformation());
			corelationAttribute.setColumnProperties(attribute.getColumnProperties());
		}
	}

	/**
	 * @param association
	 * @return
	 */
	private TaggedValue getDirectionTaggedValue(AssociationInterface association)
	{
		TaggedValue directionTaggedValue  = null;
		if(association.getAssociationDirection().equals(AssociationDirection.BI_DIRECTIONAL))
		{
			directionTaggedValue =  createTaggedValue(XMIConstants.TAGGED_NAME_ASSOC_DIRECTION, XMIConstants.TAGGED_VALUE_ASSOC_BIDIRECTIONAL);
		}
		else
		{
			directionTaggedValue =  createTaggedValue(XMIConstants.TAGGED_NAME_ASSOC_DIRECTION, XMIConstants.TAGGED_VALUE_ASSOC_SRC_DEST);
		}
		return directionTaggedValue ;
	}

	/**
	 * @param association
	 * @return
	 */
	private String getAssociationName(AssociationInterface association)
	{
		return association.getName();
	}

	/**
	 * @param targetEntityKey
	 * @param targetEntity
	 * @param targetSQLClass
	 * @return
	 */
	private Attribute getSQLAttribute(String attributeName, EntityInterface entity)
	{
		//Steps  
		/*
		 * 1) For given Entity, find the  attribute with given name.
		 * 2) For the found attribute, find the data column name using column properties
		 * 3) For the data column name, find the SQL attribute from the sqlEntity
		 */ 
		if(attributeName!=null)
		{
			AttributeInterface attribute = searchAttribute(entity,attributeName);
			if(attribute!=null)
			{
				Classifier sqlEntity = getSQLClassForEntity(entity.getName());
				ColumnPropertiesInterface colnProperties = attribute.getColumnProperties();
				if(colnProperties!=null)
				{
					String sqlAttributeName = colnProperties.getName();
					return searchAttribute(sqlEntity, sqlAttributeName);
				}
			}
		}
		return null;
	}

	/**
	 * @param targetEntity
	 * @param targetEntityKey
	 * @return
	 */
	private AttributeInterface searchAttribute(EntityInterface entity, String attributeName)
	{
		if((entity!=null)&&(attributeName!=null))
		{
			Collection<AttributeInterface> entityAttributes = entity.getAllAttributes();
			if(entityAttributes!=null)
			{
				Iterator entityAttribIter = entityAttributes.iterator();
				while(entityAttribIter.hasNext())
				{
					AttributeInterface attribute = (AttributeInterface)entityAttribIter.next();
					if((attribute !=null)&&(attribute.getName().equals(attributeName)))
					{
						return attribute;
					}
				}
			}
		}
		return null;

	}

	/**
	 * @param targetEntity
	 * @param targetEntityKey
	 * @return
	 */
	private Attribute searchAttribute(Classifier targetEntityUmlClass, String attributeName)
	{
		if((targetEntityUmlClass!=null)&&(attributeName!=null))
		{
			List entityAttributes = targetEntityUmlClass.getFeature();
			if(entityAttributes!=null)
			{
				Iterator entityAttribIter = entityAttributes.iterator();
				while(entityAttribIter.hasNext())
				{
					Object attribute = entityAttribIter.next();
					if(attribute instanceof Attribute)
					{
						if(((Attribute)attribute).getName().equals(attributeName))
						{
							return (Attribute)attribute;
						}
					}
				}
			}
		}
		return null;
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
				entityDataClass = createDataClass(tableName);

				List<String> foreignKeyAttributes = entityForeignKeyAttributes.get(entity);
				//Entity Attributes & Operations(Primary Key) of data class
				entityDataClass.getFeature().addAll(getSQLClassAttributesAndOperations(entity.getAllAttributes(),foreignKeyAttributes));
				//If entity has parent entity, add operations of parent
				if(entity.getParentEntity()!=null)
				{
					//entityDataClass.getFeature().add(getParentOperations(entity));
				}
			}
		}
		return entityDataClass;
	}



	/**
	 * @param entity
	 * @return
	 */
	private Operation getParentOperations(EntityInterface entity)
	{
		if(entity!=null)
		{
			EntityInterface parentEntity = entity.getParentEntity();
			AttributeInterface parentPrimaryKey = getPrimaryKeyAttribute(parentEntity);
			String parentPrimaryKeyOperationName = getPrimaryKeyOperationName(parentEntity.getName(), parentPrimaryKey.getName());
			String primaryKeyAttributeName = parentPrimaryKey.getColumnProperties().getName();
			Attribute primaryKeyAttribute = getSQLAttribute(parentPrimaryKey.getName(), parentEntity);
			return createPrimaryKeyOperation(parentPrimaryKeyOperationName, parentPrimaryKey.getDataType(), primaryKeyAttribute);
		}
		return null;
	}

	/**
	 * @param tableName
	 * @return
	 */
	private UmlClass createDataClass(String tableName)
	{
		UmlClass dataClass = umlPackage.getCore().getUmlClass().createUmlClass(tableName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false,false );
		//Table stereotype
		dataClass.getStereotype().addAll(getOrCreateStereotypes(XMIConstants.TABLE, XMIConstants.STEREOTYPE_BASECLASS_CLASS));
		dataClass.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.TABLE));
		dataClass.getTaggedValue().add(createTaggedValue("gentype", Variables.databaseName));
		dataClass.getTaggedValue().add(createTaggedValue("product_name",Variables.databaseName));
		return dataClass;
	}

	/**
	 * @param foreignKeyAttributes 
	 * @param allAttributes
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Collection<Feature> getSQLClassAttributesAndOperations(Collection<AttributeInterface> entityAttributes, List<String> entityForeignKeys) throws DataTypeFactoryInitializationException
	{
		//Add attributes and operations
		ArrayList<Feature> classFeatures =  new ArrayList<Feature>();
		//Add Attributes and primary keys as operations
		if(entityAttributes!=null)
		{
			Iterator entityAttributesIter = entityAttributes.iterator();
			while(entityAttributesIter.hasNext())
			{
				AttributeInterface attribute = (AttributeInterface)entityAttributesIter.next();
				Attribute umlAttribute = createDataAttribute(attribute);
				classFeatures.add(umlAttribute);
				//If primary key : add as operation
				if(attribute.getIsPrimaryKey()==true)
				{
					Operation primaryKeyOperationSpecn = createPrimaryKeyOperation(attribute,umlAttribute);
					if(primaryKeyOperationSpecn!=null)
					{
						classFeatures.add(primaryKeyOperationSpecn);	
					}
				}
				
				//If attribute is a foreign key attribute add foreign key operation  
				//elseif  attribute not in foreign key list, add "mapped-attributes" tagged value
				if(isForeignKey(attribute.getName(),entityForeignKeys))
				{
					Operation foreignKeyOperationSpecn = createForeignKeyOperation(attribute.getName(),umlAttribute);
					if(foreignKeyOperationSpecn!=null)
					{
						classFeatures.add(foreignKeyOperationSpecn);	
					}
				}
				else
				{
					if(umlAttribute!=null)
					{
						umlAttribute.getTaggedValue().add(createTaggedValue("mapped-attributes",groupName + "."+attribute.getEntity().getName()+"."+attribute.getName()));
					}
				}
			}
		}
		return classFeatures;
	}

	/**
	 * @param attribute
	 * @param foreignKeyAttributes
	 * @return
	 */
	private boolean isForeignKey(String attribute, List<String> foreignKeyAttributes)
	{
		if((attribute!=null)&&(foreignKeyAttributes!=null))
		{
			Iterator foreignKeyAttributesIter = foreignKeyAttributes.iterator();
			while(foreignKeyAttributesIter.hasNext())
			{
				String foreignKey = (String)foreignKeyAttributesIter.next();
				if(attribute.equals(foreignKey))
				{
					return true;
				}
			}
		}
		return false;
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
			if(columnProperties!=null)
			{
				String columnName = columnProperties.getName();
				dataColumn = createDataAttribute(columnName,entityAttribute.getDataType());
				if(entityAttribute.getIsPrimaryKey())
				{
					dataColumn.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.PRIMARY_KEY));
					dataColumn.getStereotype().addAll(getOrCreateStereotypes("PK", "Attribute"));
				}
			}

		}
		return dataColumn;
	}

	/**
	 * @param columnName
	 * @param dataType
	 * @return
	 * @throws DataTypeFactoryInitializationException 
	 */
	private Attribute createDataAttribute(String columnName, String dataType) throws DataTypeFactoryInitializationException
	{
		Attribute dataColumn =umlPackage.getCore().getAttribute().createAttribute(columnName,VisibilityKindEnum.VK_PUBLIC,
				false,
				ScopeKindEnum.SK_INSTANCE,
				null,
				ChangeableKindEnum.CK_CHANGEABLE,
				ScopeKindEnum.SK_CLASSIFIER,
				OrderingKindEnum.OK_UNORDERED,
				null);
		Classifier typeClass = getOrCreateDataType(DatatypeMappings.get(dataType).getSQLClassMapping());
		dataColumn.setType(typeClass);
		dataColumn.getStereotype().addAll(getOrCreateStereotypes(XMIConstants.COLUMN, XMIConstants.STEREOTYPE_BASECLASS_ATTRIBUTE));
		dataColumn.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.COLUMN));
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
				UmlClass parentClass = entityUMLClassMappings.get(parentEntity.getName());
				UmlClass childClass = entityUMLClassMappings.get(entity.getName());
				Generalization generalization = createGeneralization(parentClass,childClass);
				entityUMLRelationships.add(generalization);
			}
		}
		return entityUMLRelationships;
	}

	/**
	 * @param parentEntity
	 * @param entity
	 */
	private Generalization createGeneralization(UmlClass parentClass, UmlClass childClass)
	{
		Generalization generalization  = umlPackage.getCore().getGeneralization().createGeneralization(null,VisibilityKindEnum.VK_PUBLIC,false,null);
		org.omg.uml.foundation.core.GeneralizableElement parent = (org.omg.uml.foundation.core.GeneralizableElement)parentClass;
		org.omg.uml.foundation.core.GeneralizableElement child = (org.omg.uml.foundation.core.GeneralizableElement)childClass;
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
				AssociationEnd sourceEnd = null;
				EntityInterface sourceEntity = association.getEntity();
				if(sourceEntity!=null)
				{
					Classifier sourceClass = getUMLClassForEntity(sourceEntity.getName());
					sourceEnd = getAssociationEnd(association.getSourceRole(),sourceClass);
				}

				EntityInterface targetEntity = association.getTargetEntity();
				if(targetEntity!=null)
				{
					Classifier targetClass = getUMLClassForEntity(targetEntity.getName());
					AssociationEnd targetEnd = getAssociationEnd(association.getTargetRole(),targetClass);
					umlAssociation.getConnection().add(targetEnd);
				}
				umlAssociation.getConnection().add(sourceEnd);
				//set the direction
				TaggedValue directionTaggedValue =  getDirectionTaggedValue(association);
				if(directionTaggedValue !=null)
				{
					umlAssociation.getTaggedValue().add(directionTaggedValue);
				}
				//If association is many-to-many add "correlation-table" tagged value
				if(XMIConstants.ASSOC_MANY_MANY.equals(getAssociationType(association)))
				{
					if(association.getConstraintProperties()!=null)
					{
						String corelnTableName = association.getConstraintProperties().getName();
						umlAssociation.getTaggedValue().add(createTaggedValue("correlation-table", corelnTableName));
					}
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

	private Classifier getSQLClassForEntity(String entityName)
	{
		if((entityDataClassMappings!=null)&&(entityName!=null))
		{
			return (Classifier)entityDataClassMappings.get(entityName);
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
				OrderingKindEnum.OK_ORDERED,
				AggregationKindEnum.AK_NONE,
				ScopeKindEnum.SK_INSTANCE,
				createMultiplicity(umlPackage.getCore().getDataTypes(),minCardinality,maxCardinality),
				ChangeableKindEnum.CK_CHANGEABLE);
		associationEnd.setParticipant(assocClass);
		//associationEnd.setNavigable(true);
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


	public void init() throws CreationFailedException, Exception
	{
		initializeUMLPackage();
		initializeModel();
		initializePackageHierarchy();
		entityUMLClassMappings = new HashMap<String,UmlClass>();
		entityDataClassMappings = new HashMap<String,UmlClass>();
		foreignKeyOperationNameMappings = new HashMap<String, String>();
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
		corePackage.getAClientClientDependency().add(umlDependentClass,dependency);
		corePackage.getASupplierSupplierDependency().add(umlPrimaryClass, dependency);
		dependency.getStereotype().addAll(getOrCreateStereotypes("DataSource", "Dependency"));
		dependency.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE,"DataSource"));
		return dependency;
	}
	public static void main(String args[]) throws Exception
	{
		Variables.databaseName="MYSQL";
		//test();		//For internal testing
		if(args.length<3)
		{
			throw new Exception("Please specify all parameters. '-Dgroupname <groupname> -Dfilename <export filename> -Dversion <version>'");
		}
		String groupName = args[0];
		if(groupName==null)
		{
			throw new Exception("Please specify groupname to be exported");
		}
		else
		{
			String filename = args[1]; 
			if(filename==null)
			{
				throw new Exception("Kindly specify the filename where XMI should be exported.");
			}
			else
			{
				String xmiVersion = args[2]; 
				if(xmiVersion==null)
				{
					System.out.println("Export version not specified. Exporting as XMI 1.2");
					xmiVersion = XMIConstants.XMI_VERSION_1_2;
				}
				XMIExporter xmiExporter = new XMIExporter();
				EntityGroupInterface entityGroup = xmiExporter.getEntityGroup(groupName);
				if(entityGroup==null)
				{
					throw new Exception("Specified group does not exist. Could not export to XMI");
				}
				else
				{
					xmiExporter.exportXMI(filename, entityGroup, xmiVersion);
				}
			}
			
		}
	}

	/**
	 * @param groupName2
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private EntityGroupInterface getEntityGroup(String groupName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if(groupName!=null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			return entityManager.getEntityGroupByName(groupName);
		}
		return null;
	}

	/**
	 * 
	 */
	private static void test()
	{
		XMIExporter e =  new XMIExporter();
		EntityGroup entityGroup = new EntityGroup();
		entityGroup.setName("newgroup");

		EntityInterface entity1  = new Entity();
		entity1.setName("Person");

		/*EntityInterface entity3  = new Entity();
		entity3.setName("EntityThree");
		EntityInterface entity4  = new Entity();
		entity4.setName("EntityFour");*/


		AttributeInterface attributeId = DomainObjectFactory.getInstance().createIntegerAttribute();
		attributeId.setName("id");
		//attributeId.setIsPrimaryKey(true);
		entity1.addAttribute(attributeId);
		
		AttributeInterface attribute = DomainObjectFactory.getInstance().createStringAttribute();
		attribute.setName("name");
		entity1.addAttribute(attribute);
		
		
		AttributeInterface attribute2 = DomainObjectFactory.getInstance().createStringAttribute();
		attribute2.setName("gender");
		entity1.addAttribute(attribute2);
		
		AttributeInterface attribute3 = DomainObjectFactory.getInstance().createDateAttribute();
		attribute3.setName("dateOfJoining");
		entity1.addAttribute(attribute3);

	

		/*AttributeInterface attribute3 = DomainObjectFactory.getInstance().createLongAttribute();
		attribute3.setName("attrib3");

		entity3.addAttribute(attribute3);
		AttributeInterface attributeId3 = DomainObjectFactory.getInstance().createStringAttribute();
		attributeId3.setName("id3");
		attributeId3.setIsPrimaryKey(true);
		entity3.addAttribute(attributeId3);


		AttributeInterface attribute4 = DomainObjectFactory.getInstance().createLongAttribute();
		attribute4.setName("attrib4");

		entity4.addAttribute(attribute4);
		AttributeInterface attributeId4 = DomainObjectFactory.getInstance().createStringAttribute();
		attributeId4.setName("id4");
		attributeId4.setIsPrimaryKey(true);
		entity4.addAttribute(attributeId4);*/

		//Assocn between entity1 and entity2 : M-M
		/*Association assoc = new Association();
		assoc.setEntity(entity1);
		assoc.setTargetEntity(entity2);

		assoc.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

		assoc.setSourceRole(getRole(AssociationType.ASSOCIATION, "entityOne",
				Cardinality.ONE, Cardinality.MANY));
		assoc.setTargetRole(getRole(AssociationType.ASSOCIATION, "entityTwo", Cardinality.ONE, Cardinality.MANY));
		ConstraintPropertiesInterface cp = new ConstraintProperties();
		cp.setSourceEntityKey(attributeId.getName());
		cp.setTargetEntityKey(attributeId2.getName());
		cp.setName("EntityOneEntityTwo");
		assoc.setConstraintProperties(cp);
		entity1.addAssociation(assoc);
*/
		//Genralizn betn entity3 and entity4
	


		//Data model
		initDataModel(entity1);
	
		/*initDataModel(entity3);
		initDataModel(entity4);*/

		entityGroup.addEntity(entity1);

		/*entityGroup.addEntity(entity3);
		entityGroup.addEntity(entity4);*/



		try
		{
			/*EntityGroupInterface savedUser = entityManager.persistEntityGroup(entityGroup);
			String middleTableName = association.getConstraintProperties().getName();
			System.out.println(middleTableName);*/
			e.exportXMI("abc1-4.xmi", entityGroup, "1.1");
			XMIUtilities.transform("D:/DynamicExtensions/abc1-4.xmi","D:/DynamicExtensions/abc1-3.xmi");
			//XMIUtilities.transform("D:\\DEXMI.xmi","D:/DEXMI1-3.xmi");
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
	public static void initDataModel(EntityInterface e)
	{
		TableProperties tp = new TableProperties();
		tp.setName("table_"+e.getName());
		ColumnProperties cp1 = new ColumnProperties();
		cp1.setName(tp.getName()+ "_COL1");

		ColumnProperties cp2 = new ColumnProperties();
		cp2.setName(tp.getName()+ "_COL2");

		Iterator i = e.getAllAttributes().iterator();
		((AttributeInterface)i.next()).setColumnProperties(cp1);
		((AttributeInterface)i.next()).setColumnProperties(cp2);
		if(i.hasNext())
		{
			ColumnProperties cp3 = new ColumnProperties();
			cp3.setName(tp.getName()+ "_COL3");
			((AttributeInterface)i.next()).setColumnProperties(cp3);
		}
		ColumnProperties cp4 = new ColumnProperties();
		cp4.setName(tp.getName()+ "_COL4");
		((AttributeInterface)i.next()).setColumnProperties(cp4);
		e.setTableProperties(tp);
	}
	public Operation createPrimaryKeyOperation(AttributeInterface attribute, Attribute umlAttribute) throws DataTypeFactoryInitializationException
	{
		//Primary key operation name is generated considering the case that only one primary key will be present.
		String primaryKeyOperationName = getPrimaryKeyOperationName(attribute.getEntity().getName(),attribute.getName());
		return createPrimaryKeyOperation(primaryKeyOperationName,attribute.getDataType(), umlAttribute);
	}

	/**
	 * @param primaryKeyOperationName
	 * @param umlAttribute
	 * @param attribute
	 * @return
	 */
	private Operation createPrimaryKeyOperation(String primaryKeyOperationName, String primaryKeyDataType ,Attribute umlAttribute)
	{
		Operation primaryKeyOperation = umlPackage.getCore().getOperation().createOperation(primaryKeyOperationName, VisibilityKindEnum.VK_PUBLIC,false,ScopeKindEnum.SK_INSTANCE, false, CallConcurrencyKindEnum.CCK_SEQUENTIAL,false,false,false,null);
		primaryKeyOperation.getStereotype().addAll(getOrCreateStereotypes(XMIConstants.PRIMARY_KEY, XMIConstants.STEREOTYPE_BASECLASS_ATTRIBUTE));
		primaryKeyOperation.getTaggedValue().add(createTaggedValue(XMIConstants.STEREOTYPE, XMIConstants.PRIMARY_KEY));

		//Return parameter
		Parameter returnParameter = createParameter(null,null,ParameterDirectionKindEnum.PDK_RETURN);
		Parameter primaryKeyParam = createParameter(umlAttribute.getName(), umlAttribute.getType(), ParameterDirectionKindEnum.PDK_IN); 
	
		primaryKeyParam.getTaggedValue().add(createTaggedValue(XMIConstants.TYPE, primaryKeyDataType));

		primaryKeyOperation.getParameter().add(returnParameter);
		primaryKeyOperation.getParameter().add(primaryKeyParam);
		//Add to map storing AttributeName -> OperationName 
		return primaryKeyOperation;
	}

	/**
	 * @param attribute
	 * @return
	 */
	private String getPrimaryKeyOperationName(String entityName,String attributeName)
	{
		if((entityName!=null)&&(attributeName!=null))
		{
			return ("PK_"+entityName+"_"+attributeName);
		}
		return null;
	}
	private String getAssociationType(AssociationInterface association)
	{
		if(association!=null)
		{
			Cardinality sourceCardinality = association.getSourceRole().getMaximumCardinality();
			Cardinality targetCardinality = association.getTargetRole().getMaximumCardinality();
			if((sourceCardinality.equals(Cardinality.ONE))&&(targetCardinality.equals(Cardinality.ONE)))
			{
				return XMIConstants.ASSOC_ONE_ONE;
			}
			else if((sourceCardinality.equals(Cardinality.ONE))&&(targetCardinality.equals(Cardinality.MANY)))
			{
				return XMIConstants.ASSOC_ONE_MANY;
			}
			if((sourceCardinality.equals(Cardinality.MANY))&&(targetCardinality.equals(Cardinality.ONE)))
			{
				return XMIConstants.ASSOC_MANY_ONE;
			}
			if((sourceCardinality.equals(Cardinality.MANY))&&(targetCardinality.equals(Cardinality.MANY)))
			{
				return XMIConstants.ASSOC_MANY_MANY;
			}
		}
		return null;
	}

}
