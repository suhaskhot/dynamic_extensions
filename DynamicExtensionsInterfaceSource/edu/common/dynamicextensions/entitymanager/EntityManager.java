package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This is a singleton class that manages operations related to dynamic entity creation,attributes creation,
 * adding data into those entities and retrieving data out of them.
 * 
 *  In order to mock  EntityManager class we need to create a a mock class which extends EntityManager class.
 * i.e.We create a class named as EntityManagerMock which will extend from EntityManager.EntityManagerMock 
 * class will override the unimplemented methods from EntityManager.Entity manager is having a method 
 * as setInstance.The application which is using this mock will place the instance of mock class in
 * EntityManager class using setInstancxe method on startup.  
 * 
 *    
 * @author geetika_bangard
 */
public class EntityManager implements EntityManagerInterface
{
	/**
	 * Static instance of the entity manager.
	 */
	private static EntityManager entityManager = null;
	
	/**
	 * Empty Constructor.
	 */
	protected EntityManager()
	{
	}
	
	/**
	 * Returns the instance of the Entity Manager.
	 * @return entityManager singleton instance of the Entity Manager.
	 */
	public static synchronized EntityManager getInstance()
	{
		if (entityManager == null)
		{
			entityManager = new EntityManager();
		}
		return entityManager;
	}
    
    
    
    /**
     * Mock entity manager can be placed in the entity manager using this method.
     * @param entityManager
     */
    public void setInstance(EntityManager entityManager) {
        EntityManager.entityManager = entityManager;
        
    }
		
	/**
	 * Creates an Entity with the given entity information.Entity is registered 
	 * in the metadata and a table is created to store the records.
	 * @param entityInterface the entity to be created.
	 * @throws DynamicExtensionsSystemException system exception 
     * @throws DynamicExtensionsApplicationException application exception
     * @return EntityInterface entity interface
     * 
	 */
	public EntityInterface createEntity(EntityInterface entityInterface)
	throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
	{
		return null;
	}
    
    /**
     * Returns an entity object given the entity name; 
     * @param entityName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public EntityInterface getEntityByName(String entityName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
    	return null;
    }
    
    /**
     * Returns a collection of entities having attribute with the given name  
     * @param attributeName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntitiesByAttributeName(String attributeName) 
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
     return null;   
    }
    
    /**
     *  Returns all entities in the whole system
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    public Collection getAllEntities()
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
     return null;   
    }
	
    /**
     * Returns an attribute given the entity name and attribute name
     * @param entityName
     * @param attributeName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public AttributeInterface getAttribute(String entityName,String attributeName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
     return null;   
    }
    
    /**
     * Returns an association object given the entity name and source role name
     * @param entityName
     * @param sourceRoleName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
	
    public AssociationInterface getAssociation(String entityName,String sourceRoleName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of association objects given the source entity name and
     * target entity name
     * @param sourceEntityName
     * @param targetEntityName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getAssociations(String sourceEntityName,String targetEntityName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of entity objects given the entity description
     * @param entityDescription
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntityByDescription(String entityDescription)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of Entity objects given the attribute description
     * @param attributeDescription
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntitiesByAttributeDescription(String attributeDescription)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     *  Returns a collection of entities given the entity concept code. 
     * @param entityConceptCode
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntitiesByConceptCode(String entityConceptCode)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of entity objects given the entity concept name.
     * @param entityConceptName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntitiesByConceptName(String entityConceptName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of entities given attribute concept code.
     * @param attributeConceptCode
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntitiesByAttributeConceptCode(String attributeConceptCode)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of entities given the attribute concept name
     * @param attributeConceptname
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Collection getEntitiesByAttributeConceptName(String attributeConceptName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
        return null;
    }
    
    /**
     * Returns a collection of entity objects given the entity object with specific criteria. 
     * @param entityInterface
     * @return
     */
    public Collection findEntity(EntityInterface entityInterface) {
        return null;
    }
    	
}
