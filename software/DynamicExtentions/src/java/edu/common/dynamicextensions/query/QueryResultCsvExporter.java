package edu.common.dynamicextensions.query;	

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import edu.common.dynamicextensions.nutility.IoUtil;

import au.com.bytecode.opencsv.CSVWriter;

public class QueryResultCsvExporter implements QueryResultExporter {
	
	@Override
	public void export(String exportPath, Query query) {
		export(exportPath, query, null);
	}
	
	@Override
	public void export(String exportPath, Query query, QueryResultScreener screener) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(exportPath);
			export(out, query, screener);
		} catch (Exception e) {
			throw new RuntimeException("Error exporting CSV file", e);
		} finally {
			IoUtil.close(out);
		}		
	}
	

	@Override
	public void export(OutputStream out, Query query) {
		export(out, query, null);
	}
	
	@Override
	public void export(OutputStream out, Query query, QueryResultScreener screener) {
		QueryResultData data = null;
		try {
			data = query.getData();
			data.setScreener(screener);
			data.setColumnLabelFormatter(new DefaultResultColLabelFormatter("_"));
			export(out, data);
		} finally {
			if (data != null) {
				data.close();
			}
		}		
	}
	

	@Override
	public void export(OutputStream out, QueryResultData result) {
		CSVWriter csvWriter = null;

		try {
			csvWriter = getCsvWriter(out);			
			csvWriter.writeNext(result.getColumnLabels());

			Iterator<String[]> iterator = result.stringifiedRowIterator();
			while (iterator.hasNext()) {
				csvWriter.writeNext(iterator.next());
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error writing query result data to CSV file", e);
		} finally {
			IoUtil.close(csvWriter);
		}		
	}
	
	private CSVWriter getCsvWriter(OutputStream out) {
		return new CSVWriter(new OutputStreamWriter(out));
	}
}