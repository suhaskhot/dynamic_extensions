package edu.common.dynamicextensions.domain;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_PRIMITIVE_ATTRIBUTE"
 */
public abstract class PrimitiveAttribute extends Attribute {

    /**
     * Specifies whether this primitive attribute is a collection or not.
     */
	protected Boolean isCollection;
	/**
	 * Specifies whether this is an identified field or not.
	 */
	protected Boolean isIdentified;
	/**
	 * Specifies whether this is a primary key.
	 */
	protected Boolean isPrimaryKey;
	
	/**
	 * Column property associated to this primitive attribute.
	 */
	protected ColumnProperties columnProperties;

	/**
	 * Empty constructor.
	 */
	public PrimitiveAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	

    /**
     * @hibernate.many-to-one column ="COLUMN_PROPERTY_ID" class="edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties"
     * @return Returns the columnProperties.
     */
    public ColumnProperties getColumnProperties() {
        return columnProperties;
    }
    /**
     * @param columnProperties The columnProperties to set.
     */
    public void setColumnProperties(ColumnProperties columnProperties) {
        this.columnProperties = columnProperties;
    }
    /**
     * @hibernate.property name="isCollection" type="boolean" column="IS_COLLECTION" 
     * @return Returns the isCollection.
     */
    public Boolean getIsCollection() {
        return isCollection;
    }
    /**
     * @param isCollection The isCollection to set.
     */
    public void setIsCollection(Boolean isCollection) {
        this.isCollection = isCollection;
    }
    /**
     * @hibernate.property name="isIdentified" type="boolean" column="IS_IDENTIFIED" 
     * @return Returns the isIdentified.
     */
    public Boolean getIsIdentified() {
        return isIdentified;
    }
    /**
     * @param isIdentified The isIdentified to set.
     */
    public void setIsIdentified(Boolean isIdentified) {
        this.isIdentified = isIdentified;
    }
    /**
     * @hibernate.property name="isPrimaryKey" type="boolean" column="IS_PRIMARY_KEY" 
     * @return Returns the isPrimaryKey.
     */
    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }
    /**
     * @param isPrimaryKey The isPrimaryKey to set.
     */
    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}