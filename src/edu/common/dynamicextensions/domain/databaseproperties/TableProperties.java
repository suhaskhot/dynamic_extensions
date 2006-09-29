package edu.common.dynamicextensions.domain.databaseproperties;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TABLE_PROPERTIES" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TableProperties extends DatabaseProperties {

    /**
     * Empty constructor.
     */
	public TableProperties(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}