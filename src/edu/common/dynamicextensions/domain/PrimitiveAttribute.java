package edu.common.dynamicextensions.domain;
import java.util.Collection;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_PRIMITIVE_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
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
     * 
     */
    protected Collection conceptColection;
	
	/**
	 * Column property associated to this primitive attribute.
	 */
	protected Collection columnPropertiesCollection;
	
	/**
	 * Empty constructor.
	 */
	public PrimitiveAttribute(){
		
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
	
	/**
     * @hibernate.set name="conceptColection" table="DYEXTN_CONCEPT"
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="PRIMITIVE_ATTRIBUTE_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Concept" 
	 * @return Returns the conceptColection.
	 */
	public Collection getConceptColection() {
		return conceptColection;
	}
	/**
	 * @param conceptColection The conceptColection to set.
	 */
	public void setConceptColection(Collection conceptColection) {
		this.conceptColection = conceptColection;
	}
	/**
     * @hibernate.set name="columnPropertiesCollection" table="DYEXTN_COLUMN_PROPERTIES"
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="PRIMITIVE_ATTRIBUTE_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties"  
	 * @return Returns the columnPropertiesCollection.
	 */
	public Collection getColumnPropertiesCollection() {
		return columnPropertiesCollection;
	}
	/**
	 * @param columnPropertiesCollection The columnPropertiesCollection to set.
	 */
	public void setColumnPropertiesCollection(
			Collection columnPropertiesCollection) {
		this.columnPropertiesCollection = columnPropertiesCollection;
	}
}