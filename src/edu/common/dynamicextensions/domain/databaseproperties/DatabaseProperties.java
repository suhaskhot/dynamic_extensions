package edu.common.dynamicextensions.domain.databaseproperties;

import java.io.Serializable;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 */
public class DatabaseProperties implements  Serializable{

	protected String name;

	public DatabaseProperties(){

	}

	public void finalize() throws Throwable {

	}
	
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}