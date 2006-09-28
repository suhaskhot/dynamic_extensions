package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 */
public class TextArea extends Control {

	protected Integer columns;
	protected Integer rows;

	public TextArea(){

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