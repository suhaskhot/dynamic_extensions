
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;

/**
 * These are the attributes in the entities.
 * @author geetika_bangard
 */
public interface AttributeInterface extends AbstractAttributeInterface
{

	/**
	 * This method returns whether the Attribute is a Collection or not.
	 * @return whether the Attribute is a Collection or not.
	 */
	Boolean getIsCollection();

	/**
	 * This method sets whether the Attribute is a Collection or not.
	 * @param isCollection the Boolean value to be set.
	 */
	void setIsCollection(Boolean isCollection);

	/**
	 * This method returns whether the Attribute is identifiable or not.
	 * @return whether the Attribute is identifiable or not.
	 */
	Boolean getIsIdentified();

	/**
	 * This method sets whether the Attribute is identifiable or not.
	 * @param isIdentified the Boolean value to be set.
	 */
	void setIsIdentified(Boolean isIdentified);

	/**
	 * This method retunrs whehter the Attribute is a primary key or not.
	 * @return whehter the Attribute is a primary key or not.
	 */
	Boolean getIsPrimaryKey();

	/**
	 * This method sets whehter the Attribute is a primary key or not.
	 * @param isPrimaryKey the Boolean value to be set.
	 */
	void setIsPrimaryKey(Boolean isPrimaryKey);

	/**
	 * This method retunrs whehter the Attribute is a nullable or not.
	 * @return whehter the Attribute is a nullable or not.
	 */
	Boolean getIsNullable();

	/**
	 * This method sets whehter the Attribute is a nullable or not.
	 * @param isNullable the Boolean value to be set.
	 */
	void setIsNullable(Boolean isNullable);

	/**
	 * This method returns the DataElement associated with the Attribute. 
	 * The data elment specify the source of permissible values.
	 * @return the DataElement associated with the Attribute.
	 */
	DataElementInterface getDataElement();

	/**
	 * This method sets the DataElement of the Attribute.
	 * @param dataElement the DataElement to be set.
	 */
	void setDataElement(DataElementInterface dataElement);

	/**
	 * This method returns the ColumnProperties of the Attribute.
	 * ColumnProperties represents the properties of the Column in the Database. 
	 * @return the ColumnProperties of the Attribute.
	 */
	ColumnPropertiesInterface getColumnProperties();

}
