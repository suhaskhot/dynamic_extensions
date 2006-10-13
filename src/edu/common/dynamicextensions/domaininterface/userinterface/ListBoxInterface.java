package edu.common.dynamicextensions.domaininterface.userinterface;

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
  
}
