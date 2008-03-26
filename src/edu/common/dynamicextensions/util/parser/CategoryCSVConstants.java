package edu.common.dynamicextensions.util.parser;

/**
 * @author kunal_kamble
 * This interface defines all the keywords used in the 
 * csv file used for the category creation.
 *
 */
public interface CategoryCSVConstants
{
	/**
	 * This keyword used to mark the begining of 
	 * the new category defination. 
	 */
	String FORM_DEFINITION = "Form_Definition";

	/**
	 * This keyword is used before specifing the permissible values.
	 */
	String PERMISSIBLE_VALUES = "Permissible_Values";

	/**
	 * This keyword is used before specifing the permissible values 
	 * file name, where permissible values are line seperated.
	 */
	String PERMISSIBLE_VALUES_FILE = "Permissible_Values_File";
	
	/**
	 * This keyword is used before defining the options available for 
	 * a control. 
	 */
	String OPTIONS = "options";
	
	/**
	 * This keyword used to define the lable for the form
	 */
	String DISPLAY_LABLE = "Display_Label";
	
	/**
	 * This keyword used to define the lable for the form
	 */
	String SINGLE = "single";
	/**
	 * This keyword used to define the lable for the form
	 */
	String MULTILINE = "multiline";

}
