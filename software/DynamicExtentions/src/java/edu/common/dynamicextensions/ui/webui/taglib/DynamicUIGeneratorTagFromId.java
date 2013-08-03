
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.napi.impl.ReadOnlyFormRenderer;

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
			ReadOnlyFormRenderer formRenderer = new ReadOnlyFormRenderer();

			out.println(formRenderer.render(containerId, formRecordId));
		} catch (IOException e) {
			throw new JspException(e);
		}

		return super.doEndTag();
	}

}
