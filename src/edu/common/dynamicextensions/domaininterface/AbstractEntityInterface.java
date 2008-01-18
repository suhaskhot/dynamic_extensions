package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;

public interface AbstractEntityInterface extends AbstractMetadataInterface {

    /**
     * The table properties object contains name of the dynamically created table.
     * @return
     */
    TablePropertiesInterface getTableProperties();

    /**
     * @param tableProperties
     */
    void setTableProperties(TablePropertiesInterface tableProperties);
    /**
    *
    * @return
    */
   public Collection getContainerCollection();
   /**
    *
    * @param containerCollection
    */
   public void setContainerCollection(Collection containerCollection);
   /**
    * 
    * @param containerInterface
    */
   public void addContaier(ContainerInterface containerInterface );


}
