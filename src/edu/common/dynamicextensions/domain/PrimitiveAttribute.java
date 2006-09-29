package edu.common.dynamicextensions.domain;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class PrimitiveAttribute extends Attribute {

	protected Boolean isCollection;
	protected Boolean isIdentified;
	protected Boolean isPrimaryKey;
	public ColumnProperties columnProperties;

	public PrimitiveAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	

    /**
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