package edu.common.dynamicextensions.domaininterface.userinterface;

/**
 * TextFieldInterface stores necessary information for generating TextField control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface TextFieldInterface extends ControlInterface
{

    /**
	 * @return Returns the columns.
	 */
	Integer getColumns();
	/**
	 * @param columns The columns to set.
	 */
	void setColumns(Integer columns);
	/**
	 * @return Returns the isPassword.
	 */
	Boolean getIsPassword();
	/**
	 * @param isPassword The isPassword to set.
	 */
	void setIsPassword(Boolean isPassword);

   
}
