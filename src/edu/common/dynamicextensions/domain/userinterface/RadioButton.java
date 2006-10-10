package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_RADIOBUTTON" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class RadioButton extends Control implements RadioButtonInterface{

    /**
     * 
     *
     */
	public RadioButton(){

	}
	
    /**
     * 
     */
	public String generateHTML()
	{
		String isChecked = "";
		if((value!=null)&&(value.equalsIgnoreCase("true")))
		{
			isChecked="checked";
		}
		String htmlString = "<input type='radio' " +
							"class = '" + cssClass + "' " +
							"name = '" + name + "' " +
							"value = '" + name + "' " +
							"id = '" + name + "' " +
							"title = '" + tooltip + "' " +
							isChecked + 
							" >";
		System.out.println("Returning " + htmlString);
		return htmlString;	
	}
	

}