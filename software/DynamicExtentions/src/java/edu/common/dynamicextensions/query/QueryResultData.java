package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class QueryResultData {
    private ResultColumnLabelFormatter formatter = new DefaultResultColLabelFormatter(": ");
    
    private List<ResultColumn> resultColumns;
    
    private List<Object[]> rows = new ArrayList<Object[]>();
	
    public QueryResultData(List<ResultColumn> resultColumns) {
        this.resultColumns = resultColumns;
    }
    
    public void setColumnLabelFormatter(ResultColumnLabelFormatter formatter) {
    	this.formatter = formatter;
    }

    public String[] getColumnLabels() {
        String[] labels = new String[resultColumns.size()];
        int i = 0;
        for (ResultColumn column : resultColumns) {
        	labels[i++] = column.getColumnLabel(formatter);
        }
        
        return labels;
    }
    
    public List<ResultColumn> getResultColumns() {
    	return resultColumns;
    }

    public int rowCount() {
        return rows.size();
    }

    public Object[] row(int i) {
        if(i < 0 || i > rows.size()) {
            throw new IllegalArgumentException("Row index out of bounds: index: " + i + " size: " + rows.size());
        } else {
            return rows.get(i);
        }
    }

    public String[] stringifiedRow(int i) {
        Object row[] = row(i);
        String result[] = new String[row.length];
        
        for(int j = 0; j < row.length; j++) {
            if(row[j] == null) {
                result[j] = null;
            } else {
                result[j] = row[j].toString();
            }
        }

        return result;
    }

    public void createRow() {
        Object row[] = new Object[resultColumns.size()];
        rows.add(row);
    }

    public void addRow(Object row[]) {
        rows.add(row);
    }

    public List<Object[]> getRows() {
        return rows;
    }

    public void setRows(List<Object[]> rows) {
        this.rows = rows;
    }
}