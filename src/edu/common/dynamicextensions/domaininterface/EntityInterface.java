
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;

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
    public void setAttributeCollection(Collection attributeCollection); 
    
    /**
     * @return Returns the entityGroupCollection.
     */
    public Collection getEntityGroupCollection();
    
    /**
     * @param entityGroupCollection The entityGroupCollection to set.
     */
    public void setEntityGroupCollection(Collection entityGroupCollection);
       
    /**
     * 
     * @return
     */
    public TableProperties getTableProperties();
       
    /**
     * 
     * @param sourceEntity
     */
    public void setTableProperties(TableProperties tableProperties);
      

}
