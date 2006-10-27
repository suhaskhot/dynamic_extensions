package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATEPICKER" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DatePicker extends Control implements DatePickerInterface
{

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
	          
	       /* String output = "<input class='"+cssClass+"' " +
	        				" name='"+getHTMLComponentName()+"'  id='"+getHTMLComponentName()+"' "+
	        				" value='"+value+"'/> " +
	                        "<a href=\"javascript:show_calendar('forms(0)."+getHTMLComponentName()+"',null,null,'MM-DD-YYYY');\">" +
	                        "<img src='images\\calendar.gif' width=24 height=22 border=0/> (MM-DD-YYYY)" +
	                        "</a> "; 
	        System.out.println("************"+output);*/
		 String output = "<input class='"+cssClass+"' " +
						" name='"+getHTMLComponentName()+"'  id='"+getHTMLComponentName()+"' "+
						" value='"+value+"'/> " +
						"<A onclick=\"showCalendar('"+getHTMLComponentName()+"',2006,10,26,'MM-dd-yyyy','previewForm','"+getHTMLComponentName()+"',event,1900,2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0></A>"+
						"<DIV id=slcalcod"+getHTMLComponentName()+" style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">"+
						"<SCRIPT>printCalendar('"+getHTMLComponentName()+"',26,10,2006);</SCRIPT>" + 
						"</DIV>" +
						"[MM-DD-YYYY or MM/DD/YYYY]&nbsp;";
	        
	        return output;
	        
    }
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface) {
		// TODO Auto-generated method stub
		
	}
	

}