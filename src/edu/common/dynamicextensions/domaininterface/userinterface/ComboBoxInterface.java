package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Map;


/**
 * @author geetika_bangard
 */
public interface ComboBoxInterface extends ControlInterface {

    /**
     * 
     */
	public void populateAttributes(Map propertiesMap);
    /**
     * 
     */
	public String generateHTML();
}
