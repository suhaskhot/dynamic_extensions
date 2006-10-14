package edu.common.dynamicextensions.entitymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.StringAttribute;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;

/**
 * This Class is a mock class to test EntityManager
 * @author chetan_patil
 * @version 1.0
 */
public class MockEntityManager 
{
	/**
	 * This method returns the manually created Entities.
	 * @return The Collection of Entities
	 * @throws DynamicExtensionsApplicationException If Entities can't be created
	 */
	public Collection getAllEntities() throws DynamicExtensionsApplicationException
	{
		/**
		 * This is dummy Entity Class
		 */
		final class DummyEntity
		{
			private String entityName;
			Date createdDate;

			/**
			 * Constructor with arguments
			 * @param entityName Name of an Entity
			 * @param createdDate Date of Entity creation
			 */
			DummyEntity(String entityName, Date createdDate)
			{
				this.entityName = entityName;
				this.createdDate = createdDate;
			}
			
		}
				
		Collection entityCollection = new ArrayList();
		Entity entity = null;
		
		/* Create dummy entities */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DummyEntity[] dummyEntities = null;		
		try 
		{
			dummyEntities = new DummyEntity[] {
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-15 14:15:20")),
					new DummyEntity("Health Annotation", dateFormat.parse("2006-10-16 14:16:21")),
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-17 14:17:22")),
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-18 14:18:23")),
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-19 14:19:24")),
			};
		} 
		catch (ParseException parseException) 
		{
			throw new DynamicExtensionsApplicationException("Cannot create Dummy Entities.", parseException);
		}
		
		/* Populate all dummy entities into a Collection */
		for(int i = 0; i < dummyEntities.length; i++)
		{
			entity = new Entity();
			
			entity.setName(dummyEntities[i].entityName);
			entity.setCreatedDate(dummyEntities[i].createdDate);
			
			entityCollection.add(entity);
		}
		
		return entityCollection;
	}
	/**
	 * This method returns a Dummy Entity populated with a Dummy StringAttribute.
	 */
	public Entity createEntity() {
		Entity entity = new Entity();
		entity.setId(new Long(1));
		entity.setDescription("Dummy description");
		entity.setName("EntityOne");
		Collection abstractAttributeCollection = new HashSet();
		StringAttribute strAttr = new StringAttribute();
		strAttr.setDescription("description");
		strAttr.setEntity(entity);
		abstractAttributeCollection.add(strAttr);
		entity.setAbstractAttributeCollection(abstractAttributeCollection);
		return entity;
	}

}
