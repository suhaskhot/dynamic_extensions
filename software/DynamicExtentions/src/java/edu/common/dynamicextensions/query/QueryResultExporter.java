package edu.common.dynamicextensions.query;

import java.io.OutputStream;

public interface QueryResultExporter {
	public void export(String exportPath, Query query);
	
	public void export(OutputStream out, Query query);
}
