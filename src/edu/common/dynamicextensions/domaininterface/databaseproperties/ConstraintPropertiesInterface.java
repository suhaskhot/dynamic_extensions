package edu.common.dynamicextensions.domaininterface.databaseproperties;


/**
 * @author geetika_bangard
 */
public interface ConstraintPropertiesInterface extends DatabasePropertiesInterface {
    
    /**
     * @return Returns the sourceEntityKey.
     */
    public String getSourceEntityKey();
    /**
     * @param sourceEntityKey The sourceEntityKey to set.
     */
    public void setSourceEntityKey(String sourceEntityKey);
    /**
     * @return Returns the targetEntityKey.
     */
    public String getTargetEntityKey();
    /**
     * @param targetEntityKey The targetEntityKey to set.
     */
    public void setTargetEntityKey(String targetEntityKey);

    

}
