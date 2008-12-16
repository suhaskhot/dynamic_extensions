
package edu.common.dynamicextensions.domain.databaseproperties;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TABLE_PROPERTIES"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class TableProperties extends DatabaseProperties implements TablePropertiesInterface
{

	/**
	 * constraintName.
	 *
	 * */
	protected String constraintName;

	/**
	 * Empty constructor.
	 */
	public TableProperties()
	{

	}

	/**
	 * @hibernate.property name="constraintName" type="string" column="CONSTRAINT_NAME"
	 * @return Returns the constraintName.
	 */
	public String getConstraintName()
	{
		return constraintName;
	}

	/**
	 *
	 * @param constraintName
	 */
	public void setConstraintName(String constraintName)
	{
		this.constraintName = constraintName;
	}

}