package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATEPICKER" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DatePicker extends Control {

    /**
     * 
     *
     */
	public DatePicker(){

	}
	/**
     * 
	 */
	
	public String generateHTML()
    {
		/*String htmString = "<input class='"+cssClass+"' maxlength='10'  size='10' name='"+name+"' "+
		"id='"+name+"' /> " +
        //"<div id='overDiv' style='position:absolute; visibility:hidden; z-index:1000;'></div>" +
        "<a href=\"javascript:show_calendar('entityDataForm."+name+"',null,null,'MM-DD-YYYY');\">" +
        "<img src='images\\calendar.gif' width=24 height=22 border=0/> (MM-DD-YYYY)" +
        "</a> ";*/
		return "Not Implemented";
    }

}