package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;

/**
 * @author geetika_bangard
 */
public interface PrimitiveAttributeInterface extends AttributeInterface {

    /**
	 * @return Returns the columnProperties.
	 */
	public ColumnProperties getColumnProperties();
	/**
	 * @param columnProperties The columnProperties to set.
	 */
	public void setColumnProperties(ColumnProperties columnProperties);
	/**
	 * @return Returns the isCollection.
	 */
	public Boolean getIsCollection() ;
	/**
	 * @param isCollection The isCollection to set.
	 */
	public void setIsCollection(Boolean isCollection);
	/**
	 * @return Returns the isIdentified.
	 */
	public Boolean getIsIdentified();
	/**
	 * @param isIdentified The isIdentified to set.
	 */
	public void setIsIdentified(Boolean isIdentified);
	/**
	 * @return Returns the isPrimaryKey.
	 */
	public Boolean getIsPrimaryKey();
	/**
	 * @param isPrimaryKey The isPrimaryKey to set.
	 */
	public void setIsPrimaryKey(Boolean isPrimaryKey);
}
