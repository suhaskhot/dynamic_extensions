
package edu.common.dynamicextensions.importer.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.importer.jaxb.Association;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationDirection;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationType;
import edu.common.dynamicextensions.util.global.DEConstants.Cardinality;
import edu.common.metadata.ClassMetadata;
import edu.common.metadata.ClassMetadataMap;
import edu.common.metadata.PropertyMetadata;

public class AssociationTypeProcessor
{

	private EntityGroupInterface entityGroup;
	private ClassMetadataMap classMetadataMap;

	public AssociationTypeProcessor(ClassMetadataMap classMetadataMap,
			EntityGroupInterface entityGroup)
	{
		this.entityGroup = entityGroup;
		this.classMetadataMap = classMetadataMap;
	}

	public List<AssociationInterface> process(List<Association> associationsList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		List<AssociationInterface> processedAssociations = new ArrayList<AssociationInterface>();
		for (Association association : associationsList)
		{
			// Step 1: Get the require objects
			ClassMetadata classMetadataImpl = classMetadataMap.getClassMetadata(association
					.getSourceEntityName());
			if(classMetadataImpl == null)
			{
				throw new DynamicExtensionsApplicationException("Missing association form "+association
						.getSourceEntityName()+" to "+association
						.getTargetEntityName()+", check the hbm files.");
			}
			
			List<PropertyMetadata> associationMetadataList = classMetadataImpl.getAssociations(association
					.getTargetEntityName());
			if(associationMetadataList == null)
			{
				throw new DynamicExtensionsApplicationException("Missing association form "+association
						.getSourceEntityName()+" to "+association
						.getTargetEntityName()+", check the hbm files.");
			}
			
			for (PropertyMetadata associationMetadata : associationMetadataList) 
			{
				

				//Step 2: Create association object
				AssociationInterface associationInterface = getAssocition(association,associationMetadata.getPropertyName());
	
				processedAssociations.add(associationInterface);
				if(associationInterface.getId() == null)
				{
					//Step 3: populate association
					associationInterface.setName(associationMetadata.getPropertyName());
					processDirection(association, associationMetadata, associationInterface);
					updateConstraintProperties(associationInterface,associationMetadata);
//					ConstraintPropertiesInterface constraintProperties = DynamicExtensionsUtility
//					.getConstraintPropertiesForAssociation(associationInterface);
//					associationInterface.setConstraintProperties(constraintProperties);
					setDefault(associationInterface);
	
				}
			}
		}
		return processedAssociations;
	}
	
	private void updateConstraintProperties(AssociationInterface associationInterface, PropertyMetadata associationMetadata)
	throws DynamicExtensionsSystemException
	{
		ConstraintPropertiesInterface constraintProperties = DynamicExtensionsUtility
		.getConstraintPropertiesForAssociation(associationInterface);
		associationInterface.setConstraintProperties(constraintProperties);
	
		if(constraintProperties.getSrcEntityConstraintKeyProperties() != null)
		{
			constraintProperties.getSrcEntityConstraintKeyProperties().getTgtForiegnKeyColumnProperties().setName(associationMetadata.getColumnName());
		}
		else
		{
			constraintProperties.getTgtEntityConstraintKeyProperties().getTgtForiegnKeyColumnProperties().setName(associationMetadata.getJoinColumnName());
		}
	}

	private AssociationInterface getAssocition(Association association, String propName)
	{
		for(AssociationInterface associationInterface: entityGroup.getEntityByName(association.getSourceEntityName()).getAssociationCollection())
		{
			if(associationInterface.getTargetEntity().equals(entityGroup.getEntityByName(association.getTargetEntityName()))
					&& propName.equals(associationInterface.getName()))
			{
				return associationInterface;
			}
		}
		return DomainObjectFactory.getInstance()
		.createAssociation();
	}

	private RoleInterface getRole(AssociationType associationType, String name,
			Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}



	private void processDirection(Association association, PropertyMetadata associationMetadata,
			AssociationInterface associationInterface) throws DynamicExtensionsSystemException
	{
		entityGroup.getEntityByName(association
				.getSourceEntityName()).addAssociation(associationInterface);
		associationInterface.setEntity(entityGroup.getEntityByName(association
				.getSourceEntityName()));
		associationInterface.setTargetEntity(entityGroup.getEntityByName(association
				.getTargetEntityName()));

		if ("ManyToOne".equals(associationMetadata.getJoinType()))
		{
			associationInterface.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			associationInterface.setSourceRole(getRole(AssociationType.ASSOCIATION,
					associationMetadata.getPropertyName(), Cardinality.ONE, Cardinality.MANY));
			associationInterface.setTargetRole(getRole(AssociationType.ASSOCIATION,
					associationMetadata.getPropertyName(), Cardinality.ONE, Cardinality.ONE));

		}else if("OneToMany".equals(associationMetadata.getJoinType()))
		{
			associationInterface.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			associationInterface.setSourceRole(getRole(AssociationType.ASSOCIATION,
					associationMetadata.getPropertyName(), Cardinality.ONE, Cardinality.ONE));
			associationInterface.setTargetRole(getRole(AssociationType.ASSOCIATION,
					associationMetadata.getPropertyName(), Cardinality.ONE, Cardinality.MANY));
		}if("OneToOne".equals(associationMetadata.getJoinType()))
		{
			associationInterface.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			associationInterface.setSourceRole(getRole(AssociationType.ASSOCIATION,
					associationMetadata.getPropertyName(), Cardinality.ONE, Cardinality.ONE));

			associationInterface.setTargetRole(getRole(AssociationType.ASSOCIATION,
					associationMetadata.getRHSUniqueKeyPropertyName(), Cardinality.ONE, Cardinality.ONE));
		}
		
	}

	private void setDefault(AssociationInterface associationInterface)
	{

		associationInterface.setActivityStatus(Constants.ACTIVE);
		associationInterface.setIsSystemGenerated(false);
		associationInterface.setCreatedDate(new Date());
	}

}
