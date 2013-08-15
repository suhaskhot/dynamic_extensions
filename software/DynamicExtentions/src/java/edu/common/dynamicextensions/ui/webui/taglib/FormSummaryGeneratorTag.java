
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import edu.common.dynamicextensions.summary.AbstractSummaryDataManager;
import edu.common.dynamicextensions.summary.ColumnFormatter;
import edu.common.dynamicextensions.summary.DefaultSummaryDataManager;
import edu.common.dynamicextensions.summary.SurveySummaryDataManager;
import edu.wustl.common.util.logger.Logger;

public class FormSummaryGeneratorTag extends DynamicExtensionsFormBaseTag {

	private static final long serialVersionUID = 1855464217946194360L;

	private static final Logger LOGGER = Logger.getCommonLogger(FormSummaryGeneratorTag.class);

	private AbstractSummaryDataManager dataManager;

	/**
	 * Comma separated list of columns to be excluded
	 */
	private String exclude;

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	@Override
	public int doEndTag() throws JspException {
		render();
		return super.doEndTag();
	}

	private void populateData() {
		if (formData.getContainer() != null && formData.getContainer().isSurveyForm()) {
			dataManager = new SurveySummaryDataManager(formData, pageContext);
		} else {
			dataManager = new DefaultSummaryDataManager();
		}

		if (exclude != null && !exclude.isEmpty()) {
			dataManager.setExcludeColumns(exclude.split(","));
		}
		dataManager.populateData(formData);

	}

	private void render() {
		populateData();
		StringBuffer tableString = new StringBuffer();

		try {
			tableString.append("<table cellspacing='3' cellpadding='3' border='0' width='100%' align='center'>")
					.append("<tr>");

			for (ColumnFormatter columnFmt : dataManager.getHeaderList()) {
				tableString.append("<td class='td_color_6e81a6' align='center'>").append(columnFmt.getHeader())
						.append("</td>");
			}
			tableString.append("</tr>");
			int rowcount = 0;

			for (Map<String, String> map : dataManager.getRowData()) {

				if (rowcount % 2 == 0) {
					tableString.append("<tr class='td_color_f0f2f6'>");
				} else {
					tableString.append("<tr class='formField_withoutBorder'>");
				}
				rowcount++;

				for (ColumnFormatter columnFmt : dataManager.getHeaderList()) {
					String td = "<td %s>";
					tableString.append(String.format(td, columnFmt.getAttributeAsString()))
							.append(map.get(columnFmt.getKey())).append("</td>");
				}
				tableString.append("</tr>");
			}

			tableString.append("</table>");

			pageContext.getOut().append(tableString);

		} catch (IOException e) {
			LOGGER.error("Error generating table.", e);
		}
	}

}
