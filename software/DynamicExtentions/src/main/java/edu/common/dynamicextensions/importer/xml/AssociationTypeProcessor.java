
package edu.common.dynamicextensions.importer.xml;

import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.importer.jaxb.Association;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationDirection;
import edu.common.dynamicextensions.util.global.DEConstants.AssociationType;
import edu.common.dynamicextensions.util.global.DEConstants.Cardinality;
import edu.common.dynamicextensions.xmi.XMIConstants;
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

	public void process(List<Association> associationsList) throws DynamicExtensionsSystemException
	{

		for (Association association : associationsList)
		{
			// Step 1: Get the require objects
			ClassMetadata classMetadataImpl = classMetadataMap.getClassMetadata(association
					.getSourceEntityName());
			PropertyMetadata associationMetadata = classMetadataImpl.getAssociation(association
					.getTargetEntityName());

			//Step 2: Create association object
			AssociationInterface associationInterface = DomainObjectFactory.getInstance()
					.createAssociation();
			
			//Step 3: populate association
			associationInterface.setName(associationMetadata.getPropertyName());
			processDirection(association, associationMetadata, associationInterface);
			processConstrainProperties(associationMetadata, associationInterface);
			
			setDefault(associationInterface);

		}
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

	private void processConstrainProperties(PropertyMetadata associationMetadata,
			AssociationInterface associationInterface)
	{
		// TODO Auto-generated method stub
		
	}

	private void processDirection(Association association, PropertyMetadata associationMetadata,
			AssociationInterface associationInterface) throws DynamicExtensionsSystemException
	{

		if ("ManyToOne".equals(associationMetadata.getJoinType()))
		{
			associationInterface.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			associationInterface.setTargetEntity(entityGroup.getEntityByName(association
					.getSourceEntityName()));
			associationInterface.setEntity(entityGroup.getEntityByName(association
					.getTargetEntityName()));
			associationInterface.setSourceRole(getRole(AssociationType.CONTAINTMENT, associationMetadata.getPropertyName(), Cardinality.ONE, Cardinality.MANY));
		}
		if ("ManyToMany".equals(associationMetadata.getJoinType()))
		{
			associationInterface.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
			associationInterface.setEntity(entityGroup.getEntityByName(association
					.getSourceEntityName()));
			associationInterface.setTargetEntity(entityGroup.getEntityByName(association
					.getTargetEntityName()));
		}
		else
		{
			associationInterface.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			associationInterface.setEntity(entityGroup.getEntityByName(association
					.getSourceEntityName()));
			associationInterface.setTargetEntity(entityGroup.getEntityByName(association
					.getTargetEntityName()));
		}
	}

	private void setDefault(AssociationInterface associationInterface)
	{

		associationInterface.setActivityStatus(Constants.ACTIVE);
		associationInterface.setIsSystemGenerated(false);
		associationInterface.setCreatedDate(new Date());
	}

}
