package edu.common.dynamicextensions.query;

import java.io.OutputStream;

public interface QueryResultExporter {
	public void export(String exportPath, Query query);
	
	public void export(String exportPath, Query query, QueryResultScreener screener);
	
	public void export(OutputStream out, Query query);
	
	public void export(OutputStream out, Query query, QueryResultScreener screener);
	
	public void export(OutputStream out, QueryResultData result);
}
