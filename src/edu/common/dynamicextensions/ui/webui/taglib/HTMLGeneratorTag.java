/*
 * Created on Sep 28, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.domain.userinterface.Control;
import edu.common.dynamicextensions.domain.userinterface.JSPInclude;
import edu.common.dynamicextensions.util.global.Constants;



public class HTMLGeneratorTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private List uiControlsList=null;
	
	public HTMLGeneratorTag() {
		super();
	}
	
	private static final String TEST_JSP = "/WEB-INF/tags/test.jsp";
	
	public int doStartTag() throws JspException {
		Control controlDataType = null;
		String controlHtmlString = null,nameOfControl=null;
		boolean isControlMandatory = false;
		JspWriter jspWriter = pageContext.getOut();
		
		try {
			
			jspWriter.print("<table summary='' cellpadding='3' cellspacing='0' border='1' align='center' width='100%' >");
			
			if(uiControlsList!=null)
			{
				int noOfControls = uiControlsList.size();
				for(int i=0;i<noOfControls;i++)
				{
					controlDataType = (Control)uiControlsList.get(i);
					if(controlDataType!=null)
					{
						controlHtmlString = controlDataType.generateHTML();
						if(controlDataType instanceof JSPInclude)
						{
							JSPInclude jspinclude = (JSPInclude)controlDataType;
							try {
								//Set the JSP Parameters as request parameters
								ServletRequest request = pageContext.getRequest();
								if(request!=null)
								{
									request.setAttribute(Constants.JSP_PARAMS, jspinclude.getJspParams());
								}
								pageContext.include(jspinclude.getJspName());
							} catch (ServletException e) {
								System.out.println("Error while incuding page " + jspinclude.getJspName());
								e.printStackTrace();
							}
						}
						else
						{
							
							nameOfControl = controlDataType.getCaption();
							//isControlMandatory = controlDataType.isRequired();
							
							//Table Row Start
							jspWriter.print("<tr>");
							
							//Required Field Indicator
							jspWriter.print("<td width='10%' >");
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
							jspWriter.print("<td width='40%'>");
							jspWriter.print(nameOfControl);
							jspWriter.print("</td>");
							
							//Corresponding HTML field
							jspWriter.print("<td width='50%'>");
							jspWriter.print(controlHtmlString);
							jspWriter.print("</td>");
							
							//Table row End
							jspWriter.print("</tr>");
						}
					}
				}
			}
			jspWriter.print("</table>");
		} catch (IOException ioe) {
			throw new JspException("Error: IOException while writing to client");
		}
		return EVAL_BODY_AGAIN;
	}
	
	
	public int doEndTag() throws JspException {/*
		System.out.println("In Do End tag");
		 ServletRequest request = pageContext.getRequest();
	        request.setAttribute("selectedControlsAttributeList",uiControlsList);
	        
	        try {
	            pageContext.include(TEST_JSP);
	        } catch (ServletException e) {
	            e.printStackTrace();
	            //throw new CRMRuntimeException();
	        } catch (IOException e) {
	            e.printStackTrace();
	            //throw new CRMRuntimeException();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }*/
	        return EVAL_PAGE;
	}
	
	
	public List getUiControlsList() {
		return uiControlsList;
	}
	
	
	public void setUiControlsList(List uiControlsList) {
		this.uiControlsList = uiControlsList;
	}
	
}
