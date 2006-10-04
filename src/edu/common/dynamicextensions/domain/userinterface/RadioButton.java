package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_RADIO_BUTTON" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class RadioButton extends Control {

	public RadioButton(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	public String generateHTML()
	{
		String htmlString = "<input type='radio' " +
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