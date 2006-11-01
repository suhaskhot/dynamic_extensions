
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * 
 * The methods only work on domain object interfaces and also return domain object interfaces or
 * collection of domain object interfaces.
 * 
 * These methods may throw  DynamicExtensionsApplicationException or DynamicExtensionsSystemException.
 * 
 * DynamicExtensionsApplicationException are application specific exceptions and system can recover from those 
 * exceptions.
 * 
 * DynamicExtensionsSystemException are system specific exceptions and system can not recover from those
 * exception.An error page should be displayed in the case.
 * @author sujay_narkar
 *
 */
public interface EntityManagerInterface
{
    
    /**
     * Sets an instance of entity manager.A mock entity manager can be set using this method.
     * @param entityManager
     */
    void setInstance(EntityManager entityManager);
    
    /**
     * Saves the entity into the database.Also prepares the dynamic tables and associations 
     * between those tables using the metadata information in the entity object.
     * EntityInterface can be obtained from DomainObjectFactory.
     * @param entityInterface
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    EntityInterface createEntity(EntityInterface entityInterface)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns an entity object given the entity name; 
     * @param entityName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    EntityInterface getEntityByName(String entityName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a collection of entities having attribute with the given name  
     * @param attributeName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    Collection getEntitiesByAttributeName(String attributeName) 
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns all entities in the whole system
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    Collection getAllEntities()
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a single  entity for given identifier
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    public EntityInterface getEntityByIdentifier(String identifier)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;

    
    /**
     * Returns an attribute given the entity name and attribute name
     * @param entityName
     * @param attributeName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    AttributeInterface getAttribute(String entityName,String attributeName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    
    /**
     * Returns an association object given the entity name and source role name
     * @param entityName
     * @param sourceRoleName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    
    AssociationInterface getAssociation(String entityName,String sourceRoleName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a collection of association objects given the source entity name and
     * target entity name
     * @param sourceEntityName
     * @param targetEntityName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getAssociations(String sourceEntityName,String targetEntityName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a collection of entity objects given the entity description
     * @param entityDescription
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getEntityByDescription(String entityDescription)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a collection of Entity objects given the attribute description
     * @param attributeDescription
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getEntitiesByAttributeDescription(String attributeDescription)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    
    /**
     * Returns a collection of entities given the entity concept code.
     * @param entityConceptCode
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getEntitiesByConceptCode(String entityConceptCode)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    
    /**
     * Returns a collection of entity objects given the entity concept name.
     * @param entityConceptName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getEntitiesByConceptName(String entityConceptName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a collection of entities given attribute concept code. 
     * @param attributeConceptCode
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getEntitiesByAttributeConceptCode(String attributeConceptCode)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    /**
     * Returns a collection of entities given the attribute concept name
     * @param attributeConceptname
     * @return 
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection getEntitiesByAttributeConceptName(String attributeConceptName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException;
    
    
    /**
     * Returns a collection of entity objects given the entity object with specific criteria. 
     * @param entityInterface
     * @return
     */
    Collection findEntity(EntityInterface entityInterface);

    /**
     * This method is used to save the container into the database.
     * @param containerInterface container to save
     * @return ContainerInterface container Interface that is saved.
     * @throws DynamicExtensionsSystemException Thrown if for any reason operation can not be completed.
     * @throws DynamicExtensionsApplicationException Thrown if the entity name already exists.
     * @throws DynamicExtensionsSystemException 
     */
    ContainerInterface createContainer(
            ContainerInterface containerInterface)
            throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;
    
    /**
     * This method inserts one record for the entity.
     */
    void insertData(EntityInterface entity, Map dataValue)
            throws DynamicExtensionsApplicationException,
            DynamicExtensionsSystemException;

    /**
     * This method is used to update the existing entity into the database. This method compares the edited entity with the database copy of that 
     * entity and checks following differences.
     * <BR>
     * Newly added attributes. <BR>
     * Updated attribute in terms of changed data type <BR> 
     * Updated attributes in terms of changed (added/removed) constraints (UNIQUE, NOT NULL)<BR>
     * @param entityInterface Edited entity interface 
     * @return EntityInterface Saved entity
     * @throws DynamicExtensionsSystemException This exception is thrown in case of any system error
     * @throws DynamicExtensionsApplicationException This exception is thrown in case of any application error
     */
    public EntityInterface editEntity(EntityInterface entityInterface)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException;
    
    /**
     * This method is used to save the container into the database.
     * @param containerInterface container to save
     * @return ContainerInterface container Interface that is saved.
     * @throws DynamicExtensionsSystemException Thrown if for any reason operation can not be completed.
     * @throws DynamicExtensionsApplicationException Thrown if the entity name already exists.
     * @throws DynamicExtensionsSystemException 
     */
    public ContainerInterface editContainer(
            ContainerInterface containerInterface)
            throws DynamicExtensionsApplicationException,
            DynamicExtensionsSystemException;
    
    
    /**
     * Returns a particular record for the given recordId of the given entityId
     * @param entityId
     * @param recordId
     * @return Map key - attribute name 
     *             value - attribute value  
     */
    Map getRecordById(EntityInterface entity, Long recordId) throws DynamicExtensionsSystemException,
    DynamicExtensionsApplicationException;

}
