package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXT_FIELD" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextField extends Control {
    /**
     * Size of the text field to be shown on UI.
     */
	protected Integer columns;
	/**
	 * Boolean value indicating whether this text field is password field.
	 */
	protected Boolean isPassword;

	public TextField(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @hibernate.property name="columns" type="integer" column="COLUMNS" 
     * @return Returns the columns.
     */
    public Integer getColumns() {
        return columns;
    }
    /**
     * @param columns The columns to set.
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }
    /**
     * @hibernate.property name="isPassword" type="boolean" column="PASSWORD" 
     * @return Returns the isPassword.
     */
    public Boolean getIsPassword() {
        return isPassword;
    }
    /**
     * @param isPassword The isPassword to set.
     */
    public void setIsPassword(Boolean isPassword) {
        this.isPassword = isPassword;
    }
}