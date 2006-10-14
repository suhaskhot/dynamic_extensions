package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.List;

/**
 * ListBoxInterface stores necessary information for generating ListBox control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface ListBoxInterface extends ControlInterface 
{

    /**
     * @return Returns the isMultiSelect.
     */
    Boolean getIsMultiSelect();
    /**
     * @param isMultiSelect The isMultiSelect to set.
     */
    void setIsMultiSelect(Boolean isMultiSelect) ;
    
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
