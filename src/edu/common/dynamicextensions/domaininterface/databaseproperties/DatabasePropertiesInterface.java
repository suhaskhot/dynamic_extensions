package edu.common.dynamicextensions.domaininterface.databaseproperties;

/**
 * @author geetika_bangard
 */
public interface DatabasePropertiesInterface {

    /**
     * 
     * @return
     */
    public Long getId();
    /**
     * @param id The id to set.
     */
    public void setId(Long id);
	
    /**
     * @return Returns the name.
     */
    public String getName();
    /**
     * @param name The name to set.
     */
    public void setName(String name);
    
    /**
     * 
     */
    public Long getSystemIdentifier();
    /**
     * 
     */
    public void setSystemIdentifier(Long systemIdentifier);
}
