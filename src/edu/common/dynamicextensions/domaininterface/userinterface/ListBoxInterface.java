package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Map;

/**
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
    /**
     * 
     */
    public String generateHTML();
    /**
     * 
     */
	public void populateAttributes(Map propertiesMap);
}
