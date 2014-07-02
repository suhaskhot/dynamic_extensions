package edu.common.dynamicextensions.query;

public class DefaultResultColLabelFormatter implements ResultColumnLabelFormatter {
	private String separator = "# ";
	
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
		
		if (instance > 0) {
			heading.append(" ").append(instance);
		}
		
		return heading.toString();    	
	}	
}
