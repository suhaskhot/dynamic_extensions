package edu.common.dynamicextensions.domain.databaseproperties;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COLUMN_PROPERTIES"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ColumnProperties extends DatabaseProperties implements ColumnPropertiesInterface{
	/**
	 * Name of the constraint property.
	 */
	protected String constraintName;
    /**
     * Empty constructor
     */
	public ColumnProperties(){

	}
    /**
     * @hibernate.property name="constraintName" type="string" column="CONSTRAINT_NAME"
     * @return Returns the name.
     */
	public String getConstraintName()
	{
		return constraintName;
	}
    /**
     * @param name The name to set.
     */
	public void setConstraintName(String constraintName)
	{
		this.constraintName = constraintName;
	}

}