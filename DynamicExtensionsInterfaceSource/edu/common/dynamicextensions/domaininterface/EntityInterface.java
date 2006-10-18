
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;

/**
 * Entity object stores information of the entity.For each entity a dynamic table is generated using the metadata
 * information.
 * @author sujay_narkar
 *
 */
public interface EntityInterface extends AbstractMetadataInterface 
{
    
    /**
     * Returns the abstract attribute collection
     * AbstractAttributeCollection contains attributes as well as association objects.
     * @return Returns the attributeCollection.
     */
     Collection getAbstractAttributeCollection();
     
    /**
     * Returns the attributes of the entity   
     * @return Collection of AttributeInterface
     */
     Collection getAttributeCollection();
     
    /**
     * Returns the associations of the entity 
     * @return Collection  of AssociationInterface
     */
     Collection getAssociationCollection();
     
    /**
     * The abstractAttributeInterface to be added 
     * @param abstractAttributeInterface abstract attribute interface 
     */
     void addAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface); 
    
    /**
     * Returns a collection of entity groups having this entity. 
     * @return Returns the entityGroupCollection.
     */
     Collection getEntityGroupCollection();
    
    /**
     * Adds an entity group to the entity 
     * @param entityGroupInterface The entityGroupInterface to be added set.
     * 
     */
     void addEntityGroupInterface(EntityGroupInterface entityGroupInterface);
     /**
      * The table properties object contains name of the dynamically created table.
      * @return
      */
     public TablePropertiesInterface getTableProperties();
      
      

}
