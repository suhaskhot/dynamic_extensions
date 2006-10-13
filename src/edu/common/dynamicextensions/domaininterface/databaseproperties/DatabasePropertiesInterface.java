package edu.common.dynamicextensions.domaininterface.databaseproperties;

/**
 * The database properties are the properties of the dynamically created tables or
 * columns from those tables.
 * @author geetika_bangard
 */
public interface DatabasePropertiesInterface 
{
    /**
     * 
     * @return Long id
     */
    Long getId();
	
    /**
     * Name of the table or name of the column
     * @return Returns the name.
     */
    String getName();
    /**
     * @param name The name to set.
     */
    void setName(String name);
   
}
