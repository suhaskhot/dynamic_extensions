package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class QueryResultData {
    private String[] headerColumns;
    
    private List<Object[]> rows = new ArrayList<Object[]>();
	
    public QueryResultData(String headerColumns[]) {
        this.headerColumns = headerColumns;
    }

    public String[] headerColumns() {
        return headerColumns;
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
        Object row[] = new Object[headerColumns.length];
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