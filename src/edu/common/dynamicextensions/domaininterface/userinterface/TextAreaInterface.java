package edu.common.dynamicextensions.domaininterface.userinterface;

/**
 * TextAreaInterface stores necessary information for generating TextArea control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface TextAreaInterface extends ControlInterface {
    
    /**
	 * @return Returns the columns.
	 */
	public Integer getColumns();
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(Integer columns);
	/**
	 * @return Returns the rows.
	 */
	public Integer getRows();
	/**
	 * @param rows The rows to set.
	 */
	public void setRows(Integer rows);
 

}
