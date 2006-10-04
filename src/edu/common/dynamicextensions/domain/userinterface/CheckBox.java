package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_CHECK_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class CheckBox extends Control {

    /**
     * 
     *
     */
	public CheckBox(){

	}

	/**
     * 
	 */
	
	public String generateHTML()
    {
    	String htmlString = "<input type='checkbox' " +
    						"class = '" + cssClass + "' " +
    						"name = '" + name + "' " +
    						"value = '" + name + "' " +
    						"id = '" + name + "' " +
    						"title = '" + tooltip + "' " 
    						+">";
    	System.out.println("Returning " + htmlString);
    	return htmlString;				
    }

}