/*
 * Created on Oct 13, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import edu.wustl.common.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProcessorConstants
{
	//Types of controls
	public static final String TEXT_CONTROL  = "TextControl";
	public static final String MULTILINE_CONTROL  = "MultilineControl";
	public static final String COMBOBOX_CONTROL  = "ComboboxControl";
	public static final String LISTBOX_CONTROL  = "ListBoxControl";
	public static final String CHECKBOX_CONTROL  = "CheckboxControl";
	public static final String RADIOBUTTON_CONTROL  = "RadioButtonControl";
	public static final String DATEPICKER_CONTROL  = "DateControl";

	//Datattype constants 
	public static final String DATATYPE_STRING = "String";
	public static final String DATATYPE_DATE = "Date";
	public static final String DATATYPE_BOOLEAN = "Boolean";
	public static final String DATATYPE_NUMBER = "Number";

	//Max number of digits/decimals for various datatypes
	public static final int MAX_NO_OF_DIGITS_SHORT = 5;
	public static final int MAX_NO_OF_DIGITS_INT = 10;
	public static final int MAX_NO_OF_DIGITS_LONG = 15;
	public static final int MAX_NO_OF_DECIMALS_FLOAT = 5;
	public static final int MAX_NO_OF_DECIMALS_DOUBLE = 10;
    
    public static final String ADD = "Add";
    public static final String EDIT = "Edit";
    
    //Date Format
    public static final String DATE_FORMAT = "MM-DD-YYYY"; // Constants.DATE_PATTERN_MM_DD_YYYY;
    
    //Types of permisible value sources for Combobox
    public static final String DISPLAY_CHOICE_USER_DEFINED = "UserDefined";
    
    //Single line / multiline line types
    public static final String LINE_TYPE_MULTILINE = "MultiLine";
    public static final String LINE_TYPE_SINGLELINE = "SingleLine";
    
}
