
package edu.common.dynamicextensions.summary;

import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;

public class DefaultSummaryDataManager extends AbstractSummaryDataManager {

	@Override
	protected void populateHeaderList() {
		headerList.add(new ColumnFormatter(SR_NO, ""));
		ColumnFormatter question = new ColumnFormatter(QUESTION, "Question");
		question.getDataFormatter().put(ALIGN, RIGHT);
		headerList.add(question);
		headerList.add(new ColumnFormatter(RESPONSE, "Response"));

	}

	@Override
	protected void populateRow(ControlValue controlValue, Map<String, String> data) {
		data.put(QUESTION, controlValue.getControl().getCaption());
		data.put(RESPONSE, formatValue(controlValue));

	}

}
