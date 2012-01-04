
package edu.common.dynamicextensions.importer.xml;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.importer.jaxb.Association;
import edu.common.metadata.ClassMetadata;
import edu.common.metadata.ClassMetadataMap;

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

	public void process(List<Association> associationsList)
	{

		for(Association association:associationsList)
		{
			ClassMetadata classMetadataImpl = classMetadataMap.getClassMetadata(association.getSourceEntityName());
			
		}
	}

}
