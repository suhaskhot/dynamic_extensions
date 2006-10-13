package edu.common.dynamicextensions.domaininterface.databaseproperties;


/**
 * These are the data base properties for an association.
 * @author geetika_bangard
 */
public interface ConstraintPropertiesInterface extends DatabasePropertiesInterface
{
    
    /**
     * @return Returns the sourceEntityKey.
     */
    String getSourceEntityKey();
    /**
     * @param sourceEntityKey The sourceEntityKey to set.
     */
    void setSourceEntityKey(String sourceEntityKey);
    /**
     * @return Returns the targetEntityKey.
     */
    String getTargetEntityKey();
    /**
     * @param targetEntityKey The targetEntityKey to set.
     */
    void setTargetEntityKey(String targetEntityKey);

    

}
