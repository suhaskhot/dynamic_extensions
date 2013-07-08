
package edu.common.dynamicextensions.ui.webui.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;

/**
 * @author Kunal
 * Base tag can be extended by any tag involved with form related functionality
 */
public abstract class DynamicExtensionsFormBaseTag extends TagSupport
{
	private static final long serialVersionUID = 1L;
	
	protected Long containerIdentifier;
	
	protected Long recordIdentifier;
	
	protected FormData formData;

	@Override
	public int doStartTag() throws JspException
	{
		formData = new FormDataManagerImpl().getFormData(containerIdentifier, recordIdentifier);
		return super.doStartTag();
	}

	public Long getContainerIdentifier()
	{
		return containerIdentifier;
	}

	public void setContainerIdentifier(Long containerIdentifier)
	{
		this.containerIdentifier = containerIdentifier;
	}

	public Long getRecordIdentifier()
	{
		return recordIdentifier;
	}

	public void setRecordIdentifier(Long recordIdentifier)
	{
		this.recordIdentifier = recordIdentifier;
	}


}
