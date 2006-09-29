/*
 * Created on Sep 28, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.domain.userinterface.Control;



public class HTMLGeneratorTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private List uiControlsList=null;
	
	public HTMLGeneratorTag() {
		super();
	}
	
	public int doStartTag() throws JspException {
		Control controlDataType = null;
		String controlHtmlString = null,nameOfControl=null;
		boolean isControlMandatory = false;
		JspWriter jspWriter = pageContext.getOut();
		
		try {
			jspWriter.print("<table summary='' cellpadding='3' cellspacing='0' border='0' align='center' width = '70%'>");
			
			//Heading Row
			jspWriter.print("<tr><td class='formMessage' colspan='3'>* indicates a required field</td></tr>");
			jspWriter.print("<tr><td class='formTitle' height='20' colspan='3'>New Entity</td></tr>");
			
			
			if(uiControlsList!=null)
			{
				int noOfControls = uiControlsList.size();
				for(int i=0;i<noOfControls;i++)
				{
					controlDataType = (Control)uiControlsList.get(i);
					if(controlDataType!=null)
					{
						controlHtmlString = controlDataType.generateHTML();
						nameOfControl = controlDataType.getCaption();
						//isControlMandatory = controlDataType.isRequired();
						
						//Table Row Start
						jspWriter.print("<tr>");
						
						//Required Field Indicator
						jspWriter.print("<td class='formRequiredNotice' >");
						if(isControlMandatory)
						{
							jspWriter.print("*");
						}
						else
						{
							jspWriter.print("&nbsp;");
						}
						jspWriter.print("</td>");
						
						//Name of the control
						jspWriter.print("<td class='formRequiredLabel'>");
						jspWriter.print(nameOfControl);
						jspWriter.print("</td>");
						
						//Corresponding HTML field
						jspWriter.print("<td class='formField'>");
						jspWriter.print(controlHtmlString);
						jspWriter.print("</td>");
						
						//Table row End
						jspWriter.print("</tr>");
					}
				}
			}
			jspWriter.print("</table>");
		} catch (IOException ioe) {
			throw new JspException("Error: IOException while writing to client");
		}
		return EVAL_BODY_INCLUDE;
	}
	
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	
	public List getUiControlsList() {
		return uiControlsList;
	}
	
	
	public void setUiControlsList(List uiControlsList) {
		this.uiControlsList = uiControlsList;
	}
	
}
