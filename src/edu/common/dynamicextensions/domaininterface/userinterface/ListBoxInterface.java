package edu.common.dynamicextensions.domaininterface.userinterface;

/**
 * ListBoxInterface stores necessary information for generating ListBox control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface ListBoxInterface extends ControlInterface {

    /**
     * @return Returns the isMultiSelect.
     */
    public Boolean getIsMultiSelect();
    /**
     * @param isMultiSelect The isMultiSelect to set.
     */
    public void setIsMultiSelect(Boolean isMultiSelect) ;
  
}
