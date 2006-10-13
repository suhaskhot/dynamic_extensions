
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 * @author sujay_narkar
 *
 */
public interface EntityInterface extends AbstractMetadataInterface {
    
    /**
     * @return Returns the attributeCollection.
     */
    public Collection getAbstractAttributeCollection();
    /**
     * 
     * @return
     */
    public Collection getAttributeCollection();
    /**
     * 
     * @return
     */
    public Collection getAssociationCollection();
    /**
     * @param attributeCollection The attributeCollection to set.
     */
    public void addAbstractAttribute(AbstractAttributeInterface attributeInterface); 
    
    /**
     * @return Returns the entityGroupCollection.
     */
    public Collection getEntityGroupCollection();
    
    /**
     * @param entityGroupCollection The entityGroupCollection to set.
     */
    public void addEntityGroupInterface(EntityGroupInterface entityGroupInterface);
      
      

}
