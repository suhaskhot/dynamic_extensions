package edu.common.dynamicextensions.query;	

import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import edu.common.dynamicextensions.nutility.IoUtil;

import au.com.bytecode.opencsv.CSVWriter;

public class QueryResultCsvExporter implements QueryResultExporter {
	
	@Override
	public void export(String exportPath, Query query) {
		export(getCsvWriter(exportPath), query);
	}

	@Override
	public void export(OutputStream out, Query query) {
		export(getCsvWriter(out), query);		
	}
	
	private void export(CSVWriter csvWriter, Query query) {
		try {
			query.columnHeadingSeparator("_");
			QueryResultData data = query.getData();
			
			csvWriter.writeNext(data.headerColumns());
			
			for (int i = 0; i < data.rowCount(); ++i) {
				csvWriter.writeNext(data.stringifiedRow(i));
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error writing query result data to CSV file", e);
		} finally {
			if (csvWriter != null) {
				try { csvWriter.close(); } catch(Exception e) { }				
			}
		}				
	}
	
	private CSVWriter getCsvWriter(String exportPath) {		
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(exportPath);
			return new CSVWriter(fileWriter);
		} catch (Exception e) {
			IoUtil.close(fileWriter);
			throw new RuntimeException("Error opening csv file for writing", e);
		} 
	}
	
	private CSVWriter getCsvWriter(OutputStream out) {
		return new CSVWriter(new OutputStreamWriter(out));
	}
}