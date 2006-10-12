package edu.common.dynamicextensions.domaininterface.userinterface;

/**
 * TextFieldInterface stores necessary information for generating TextField control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface TextFieldInterface extends ControlInterface {

    /**
	 * @return Returns the columns.
	 */
	public Integer getColumns();
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(Integer columns);
	/**
	 * @return Returns the isPassword.
	 */
	public Boolean getIsPassword();
	/**
	 * @param isPassword The isPassword to set.
	 */
	public void setIsPassword(Boolean isPassword);

   
}
