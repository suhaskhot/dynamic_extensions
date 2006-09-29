package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXT_AREA" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextArea extends Control {

    /**
     * Number of columns in the text area.
     */
	protected Integer columns;
	/**
	 * Number of rows in the text area.
	 */
	protected Integer rows;

	public TextArea(){

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
     * @hibernate.property name="rows" type="integer" column="COLUMNS" 
     * @return Returns the rows.
     */
    public Integer getRows() {
        return rows;
    }
    /**
     * @param rows The rows to set.
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }
}