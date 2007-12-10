package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;

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


}
