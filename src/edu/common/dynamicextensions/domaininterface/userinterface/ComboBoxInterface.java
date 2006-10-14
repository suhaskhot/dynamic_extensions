package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.List;


/**
 * ComboBoxInterface stores necessary information for generating ComboBox control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface ComboBoxInterface extends ControlInterface
{
	/**
	 * 
	 * @return List of choices
	 */
	public List getChoiceList();
	
	/**
	 * 
	 * @param list : List of choices
	 */
	public void setChoiceList(List list);
}
