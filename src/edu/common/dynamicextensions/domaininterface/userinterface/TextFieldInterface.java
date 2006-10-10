package edu.common.dynamicextensions.domaininterface.userinterface;


/**
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
