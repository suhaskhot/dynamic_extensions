/**
 * 
 */
package edu.common.dynamicextensions.ui.webui.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.napi.impl.FormRenderer;

/**
 * @author Vinayak Pawar
 *
 */
public class SurveyFormPagesTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5789430389356049678L;
	
	@Override
	public int doEndTag()
	throws JspException {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		try {
			pageContext.getOut().append(new FormRenderer(request).render());
		} catch (Exception e) {
			throw new RuntimeException("Error rendering survey pages", e);
		}		
		return super.doEndTag();
	}
}
