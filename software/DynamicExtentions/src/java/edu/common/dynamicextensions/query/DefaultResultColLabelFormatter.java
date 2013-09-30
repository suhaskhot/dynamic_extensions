package edu.common.dynamicextensions.query;

public class DefaultResultColLabelFormatter implements ResultColumnLabelFormatter {
	private String separator = ": ";
	
	public DefaultResultColLabelFormatter(String separator) {
		this.separator = separator;
	}

	@Override
	public String format(String[] nodeCaptions, int instance) {
		StringBuilder heading = new StringBuilder(nodeCaptions[0]);
		
		for (int j = 1; j < nodeCaptions.length; ++j) {
			heading.append(separator).append(nodeCaptions[j]);
		}
		
		if (instance > 0) {
			heading.append(" ").append(instance);
		}
		
		return heading.toString();    	
	}
}
