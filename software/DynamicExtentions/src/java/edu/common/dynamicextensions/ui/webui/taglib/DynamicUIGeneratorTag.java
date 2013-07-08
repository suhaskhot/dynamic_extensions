
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.napi.impl.FormRenderer;
import edu.wustl.common.util.logger.Logger;

public class DynamicUIGeneratorTag extends TagSupport
{

	/**
	 *
	 */

	private static final long serialVersionUID = 1L;
	private Long recordIdentifier;
	private Long containerIdentifier;
	private String mode;

	/**
	 * This method contains no operations.
	 * @return int SKIP_BODY
	 * @since TODO
	 */
	@Override
	public int doStartTag()
	{
		Logger.out.debug("Entering Selector List Tag ...");
		return SKIP_BODY;
	}

	/**
	 *
	 */
	@Override
	public int doEndTag()
	{

		final JspWriter out = pageContext.getOut();
		FormRenderer renderer  = new FormRenderer((HttpServletRequest) pageContext.getRequest());
		try {
			out.println(renderer.render());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return EVAL_PAGE;
	}


	public Long getRecordIdentifier()
	{
		return recordIdentifier;
	}

	public void setRecordIdentifier(Long formRecordId)
	{
		this.recordIdentifier = formRecordId;
	}

	public Long getContainerIdentifier()
	{
		return containerIdentifier;
	}

	public void setContainerIdentifier(Long containerId)
	{
		this.containerIdentifier = containerId;
	}

	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

}
