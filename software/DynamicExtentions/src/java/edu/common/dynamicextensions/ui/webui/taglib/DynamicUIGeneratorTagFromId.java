
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.ReadOnlyFormRenderer;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.DEConstants;

public class DynamicUIGeneratorTagFromId extends TagSupport {

	private static final long serialVersionUID = 1L;
	private Long formRecordId;
	private Long containerId;

	public Long getFormRecordId() {
		return formRecordId;
	}

	public void setFormRecordId(Long formRecordId) {
		this.formRecordId = formRecordId;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			final JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Stack<FormData> formDataStack = (Stack<FormData>) CacheManager.getObjectFromCache(request, DEConstants.FORM_DATA_STACK);

			containerId = formDataStack.peek().getContainer().getId();
			
			ReadOnlyFormRenderer formRenderer = new ReadOnlyFormRenderer();
			out.println(formRenderer.render(containerId, formRecordId));
			
		} catch (IOException e) {
			throw new JspException(e);
		}

		return super.doEndTag();
	}
}
