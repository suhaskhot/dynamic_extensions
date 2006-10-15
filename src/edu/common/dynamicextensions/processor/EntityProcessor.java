/**
 *<p>Title: EntityProcessor</p>
 *<p>Description:  This class acts as a utility class which processes tne entity in various ways as needed
 *and provides methods to the UI layer.This processor class is a POJO and not a framework specific class so 
 *it can be used by all types of presentation layers.  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.SemanticProperty;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.EntityInformationInterface;


/**
 *<p>Title: EntityProcessor</p>
 *<p>Description:  This class acts as a utility class which processes tne entity in various ways as needed
 *and provides methods to the UI layer.This processor class is a POJO and not a framework specific class so 
 *it can be used by all types of presentation layers.  </p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
public class EntityProcessor extends BaseDynamicExtensionsProcessor
{

    /**
     * Protected constructor for entity processor
     *
     */
   protected EntityProcessor () {
       
   }
 
   /**
    * this method gets the new instance of the entity processor to the caller.
    * @return EntityProcessor EntityProcessor instance
    */
    public static EntityProcessor getInstance () {
        return new EntityProcessor();
    }
    
    /**
     * This method returns empty domain object of entityInterface.
     * @return EntityInterface Returns new instance of EntityInterface from the domain object Factory.
     */
    public EntityInterface createEntity() {
        return DomainObjectFactory.getInstance().createEntity();
    }
    
    /**
     * This method creates a new instance of the EntityInterface from the domain object factory. After the creation
     * of this instance it populates the entityInterface with the information that is provided through 
     * the entityInformationInterface which is a parameter to the method.
     * @param entityInformationInterface Implementation of entityInformationInterface 
     * which has all the data required for the creation of the entity.
     * @return EntityInterface Returns the unsaved instance of EntityInterface with populated values taken 
     * from the entityInformationInterface.
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public EntityInterface createAndSaveEntity(EntityInformationInterface entityInformationInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        EntityInterface entityInterface = DomainObjectFactory.getInstance().createEntity();
        populateEntity(entityInformationInterface, entityInterface);
        entityInterface = EntityManager.getInstance().createEntity(entityInterface);
        return entityInterface;
    }
    
    /**
     * This method populates the given EntityInterface using the given entityInformationInterface.
     * @param entityInterface Instance of EntityInterface which is populated using the informationInterface.
     * @param entityInformationInterface Instance of EntityInformationInterface which is used to populate the entityInterface.
     */
    public void populateEntity (EntityInformationInterface entityInformationInterface, EntityInterface entityInterface) {
        if (entityInformationInterface != null && entityInterface != null) {
            entityInterface.setName(entityInformationInterface.getFormName());
            entityInterface.setDescription(entityInformationInterface.getFormDescription());
            SemanticPropertyInterface semanticPropertyInterface = new SemanticProperty();
            semanticPropertyInterface.setConceptCode(entityInformationInterface.getConceptCode());
            entityInterface.addSemanticProperty(semanticPropertyInterface);
        }
    }
    
    /**
     * This method will populate the EntityInformationInterface using the EntityInterface so that the 
     * information of the Entity can be shown on the user page using the EntityInformationInterface.
     * @param entityInterface Instance of EntityInterface from which to populate the informationInterface.
     * @param entityInformationInterface Instance of EntityInformationInterface which will be populated using 
     * the first parameter that is EntityInterface.
     */
    public void populateEntityInformation (EntityInterface entityInterface, EntityInformationInterface entityInformationInterface) {
        if (entityInterface != null && entityInformationInterface != null) {
            entityInformationInterface.setFormName(entityInterface.getName());
            entityInformationInterface.setFormDescription(entityInterface.getDescription());
            
        }
    }
    /**
     * This method creates a new instance of the EntityInterface from the domain object factory. After the creation
     * of this instance it populates the entityInterface with the information that is provided through 
     * the entityInformationInterface which is a parameter to the method.
     * @param entityInformationInterface Implementation of entityInformationInterface 
     * which has all the data required for the creation of the entity.
     * @return EntityInterface Returns the unsaved instance of EntityInterface with populated values taken 
     * from the entityInformationInterface.
     * @throws DynamicExtensionsSystemException Exception
     */
    public EntityInterface createAndPopulateEntity(EntityInformationInterface entityInformationInterface) throws DynamicExtensionsSystemException {
        EntityInterface entityInterface = DomainObjectFactory.getInstance().createEntity();
        populateEntity(entityInformationInterface, entityInterface);
        return entityInterface;
    }
    
    //public saveEntity(Enti)
}
