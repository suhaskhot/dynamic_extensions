
package edu.common.dynamicextensions.importer.xml;

import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.importer.jaxb.Attribute;
import edu.common.dynamicextensions.importer.jaxb.Entity;
import edu.common.metadata.ClassMetadataMap;

/**
 * @author Kunal
 * Process the entity tag from simplifiedMetadatXml.xsd, with the metadata information coming from the hbm files
 *
 */
public class EntityTypeProcessor
{

	private ClassMetadataMap classMetadataMap;
	private EntityGroupInterface entityGroup;

	public EntityTypeProcessor(ClassMetadataMap classMetadataMap, EntityGroupInterface entityGroup)
	{
		this.classMetadataMap = classMetadataMap;
		this.entityGroup = entityGroup;

	}

	public void process(Collection<Entity> entities)
	{
		for (Entity entityType : entities)
		{
			//Step 1: create if entity with same name does not exist.
			EntityInterface entity = getEntity(entityType.getName());

			//Step 2: Set default values 
			setDefaultValues(entity);

			//Step 3: Set primitive property of the 
			processPrimitiveProperties(entity);

			//Step 4: Set the table properties, from hbm file
			processTableProperties(entity);

			//Step 5: Process Attribute collection
			processAttributes(entity, entityType.getAttribute());
		}
	}

	private EntityInterface getEntity(String entityName)
	{
		EntityInterface entity = entityGroup.getEntityByName(entityName);
		if (entity == null)
		{
			entity = DomainObjectFactory.getInstance().createEntity();
			entity.setName(entityName);
		}

		return entity;
	}

	private void setDefaultValues(EntityInterface entity)
	{
		/*entity.setInheritanceStrategy(InheritanceStrategy.)*/
	}

	private void processTableProperties(EntityInterface entity)
	{
		TablePropertiesInterface tableProperties = DomainObjectFactory.getInstance()
				.createTableProperties();

		tableProperties.setName(classMetadataMap.getClassMetadata(entity.getName()).getTableName());
		entity.setTableProperties(tableProperties);

	}

	private void processPrimitiveProperties(EntityInterface entity)
	{
		entity.setAbstract(classMetadataMap.getClassMetadata(entity.getName()).isAbstractClass());

	}

	private void processAttributes(EntityInterface entity, List<Attribute> attributeList)
	{
		AttributeTypeProcessor attributeTypeProcessor = new AttributeTypeProcessor(entity,
				classMetadataMap.getClassMetadata(entity.getName()));
		for (Attribute attribute : attributeList)
		{
			attributeTypeProcessor.process(attribute);
		}
	}
}
