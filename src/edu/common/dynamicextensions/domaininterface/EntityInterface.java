
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
    public Collection getAttributeCollection();
    /**
     * @param attributeCollection The attributeCollection to set.
     */
    public void addAttribute(AttributeInterface attributeInterface); 
    
    /**
     * @return Returns the entityGroupCollection.
     */
    public Collection getEntityGroupCollection();
    
    /**
     * @param entityGroupCollection The entityGroupCollection to set.
     */
    public void addEntityGroupInterface(EntityGroupInterface entityGroupInterface);
      
      

}
