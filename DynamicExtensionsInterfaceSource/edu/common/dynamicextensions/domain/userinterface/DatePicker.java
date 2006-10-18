package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATEPICKER" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DatePicker extends Control implements DatePickerInterface{

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
		 if(value == null){
	            value="";
	        }
	          
	        String output = "<input class='"+cssClass+"' " +
	        				 " name='"+name+"'  id='"+name+"' "+
	        				 " value='"+value+"'/> " +
	                        "<div id='overDiv' style='position:absolute; visibility:hidden; z-index:1000;'></div>" +
	                        "<a href=\"javascript:show_calendar('forms(0)."+name+"',null,null,'MM-DD-YYYY');\">" +
	                        "<img src='images\\calendar.gif' width=24 height=22 border=0/> (MM-DD-YYYY)" +
	                        "</a> "; 
	        System.out.println("************"+output);

	        
	        return output;
	        
    }
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface) {
		// TODO Auto-generated method stub
		
	}

}