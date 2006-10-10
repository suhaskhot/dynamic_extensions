/*
 * Created on Oct 5, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.util;

import edu.common.dynamicextensions.domain.userinterface.Control;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JSPInclude extends Control {

	private String jspName = null;
	public String getJspName() {
		return this.jspName;
	}
	public void setJspName(String jspName) {
		this.jspName = jspName;
	}
	public String generateHTML() {
		if(jspName==null)
		{
			jspName="";
		}
		//String htmlString = "<jsp:include page='" + jspName +"' />";
		String htmlString = "<%@ include file='" +  jspName + "' %>";
		return htmlString;
	}
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AttributeInterface attributeInterface) {
		// TODO Auto-generated method stub
		
	}

}
