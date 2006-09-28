package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 */
public class TextField extends Control {

	protected Integer columns;
	protected Boolean isPassword;

	public TextField(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
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