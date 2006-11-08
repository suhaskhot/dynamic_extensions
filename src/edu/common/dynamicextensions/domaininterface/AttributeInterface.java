package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;

/**
 * These are the attributes in the entities.
 * @author geetika_bangard
 */
public interface AttributeInterface extends AbstractAttributeInterface 
{
   
	/**
     * States that if the attribute is a collection
     * @return Returns the isCollection.
	 */
	 Boolean getIsCollection() ;
	/**
	 * @param isCollection The isCollection to set.
	 */
	 void setIsCollection(Boolean isCollection);
     
	/**
     * States that if the attribute is identified 
	 * @return Returns the isIdentified.
	 */
	 Boolean getIsIdentified();
	/**
	 * @param isIdentified The isIdentified to set.
	 */
	 void setIsIdentified(Boolean isIdentified);
	/**
     * States that if the attribute is a primary key
	 * @return Returns the isPrimaryKey.
	 */
	 Boolean getIsPrimaryKey();
	/**
	 * @param isPrimaryKey The isPrimaryKey to set.
	 */
	 void setIsPrimaryKey(Boolean isPrimaryKey);
     /**
     * States that if the attribute is a primary key
     * @return Returns the isPrimaryKey.
     */
     Boolean getIsNullable();
    /**
     * @param isPrimaryKey The isPrimaryKey to set.
     */
     void setIsNullable(Boolean isPrimaryKey);
     
     /**
      * Returns the data element associated with the attribute.The data elment specify the
      * source of permissible values.
      * @return DataElementInterface
     */
     DataElementInterface getDataElement();
     
    /**
     * @param dataElementInterface data element interface
     */
     void setDataElement(DataElementInterface dataElementInterface);
     
     /**
      * Column properties object contains database column name  
      * @return
      */
     public ColumnPropertiesInterface getColumnProperties();
  
}
