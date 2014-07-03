package edu.common.dynamicextensions.query;

import java.util.HashMap;
import java.util.Map;

public class DefaultResultColLabelFormatter implements ResultColumnLabelFormatter {
	private String separator = "# ";

	private Map<String, Integer> instanceCntMap = new HashMap<String, Integer>();
	
	public DefaultResultColLabelFormatter(String separator) {
		this.separator = separator;
	}

	@Override
	public String format(String[] nodeCaptions, int instance) {
		StringBuilder heading = new StringBuilder();		
		for (int j = 0; j < nodeCaptions.length - 1; ++j) {
			heading.append(nodeCaptions[j]).append(separator);
		}		
		heading.append(nodeCaptions[nodeCaptions.length - 1]);

		String headingStr = heading.toString();
		Integer instanceCnt = instanceCntMap.get(headingStr);
		if (instanceCnt == null) {
			instanceCnt = 0;
		}

		String result = headingStr;
		if (instanceCnt > 0) {
			result += separator + instanceCnt;
		}

		instanceCntMap.put(headingStr, instanceCnt + 1);
		return result;
	}
}
