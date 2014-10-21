package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.nutility.Util;
import edu.common.dynamicextensions.query.ast.FieldNode;

public class QueryResultData {
    private ResultColumnLabelFormatter formatter = new DefaultResultColLabelFormatter("# ");
    
    private List<ResultColumn> resultColumns;
    
    private List<Object[]> rows = null;
    
    private ShallowWideRowGenerator rowGen = null;
            
    private SimpleDateFormat sdf = null;

    private SimpleDateFormat tsf = null;
    
    private QueryResultScreener screener;
    
    private int dbRowsCount;
        	
    public QueryResultData(List<ResultColumn> resultColumns, String dateFormat, String timeFormat) {
        this.resultColumns = resultColumns;
        if (dateFormat != null) {
        	sdf = new SimpleDateFormat(dateFormat);

                if (timeFormat != null) {
                        tsf = new SimpleDateFormat(dateFormat + " " + timeFormat);
                }
        }
    }
    
    public QueryResultScreener getScreener() {
        return screener;
    }

    public void setScreener(QueryResultScreener screener) {
        this.screener = screener;
    }

    public void setColumnLabelFormatter(ResultColumnLabelFormatter formatter) {
    	this.formatter = formatter;
    }

    public Integer[] getColumnIndices(String name) {
    	List<Integer> indices = new ArrayList<Integer>();
    	
    	int i = 0;
    	for (ResultColumn column : getResultColumns()) {
    		if (column.getExpression() instanceof FieldNode) {
    			FieldNode node = (FieldNode)column.getExpression();
    			if (node.getName().equals(name)) {
    				indices.add(i);
    			}    			
    		}
    		
    		++i;
    	}
    	
    	return indices.toArray(new Integer[0]);
    }
    
    public String[] getColumnLabels() {
        List<ResultColumn> screenedCols = resultColumns;
        if (screener != null) {
            screenedCols = screener.getScreenedResultColumns(resultColumns);
        }

        String[] labels = new String[screenedCols.size()];
        int i = 0;
        for (ResultColumn column : screenedCols) {
        	labels[i++] = column.getColumnLabel(formatter);
        }
        
        return labels;
    }
    
    public List<ResultColumn> getResultColumns() {
    	return screener != null ? screener.getScreenedResultColumns(resultColumns) : resultColumns;
    }
    
    public void dataSource(List<Object[]> rows) {
    	this.rows = rows;
    }
       
    public void dataSource(ResultSet rs) {
    	List<Object[]> rows = new ArrayList<Object[]>();
    	
    	try {
        	int columnCount = rs.getMetaData().getColumnCount();
        	if (columnCount == resultColumns.size() + 1 && DbSettingsFactory.isOracle()) {
        		columnCount--;
        	}
        	
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; ++i) {
                    row[i] = rs.getObject(i + 1);
                    if (row[i] instanceof Date) {
                    	row[i] = rs.getTimestamp(i + 1);
                    }
                }
                
                if (screener != null) {
                	row = screener.getScreenedRowData(resultColumns, row);
                }
                
                rows.add(row);
            }    		
    	} catch (Exception e) {
    		throw new RuntimeException("Error traversing result set", e);
    	}
    	
    	this.rows = rows;
    	this.dbRowsCount = this.rows.size();
    }
    
    public void dataSource(ShallowWideRowGenerator rowGen) {
    	this.rowGen = rowGen;
    }

    public List<Object[]> getRows() {
        if (rows != null) {
        	return rows;
        }
        
        List<Object[]> rows = new ArrayList<Object[]>();
        Iterator<Object[]> rowsIter = rowGen.iterator();
        while (rowsIter.hasNext()) {
        	Object[] row = rowsIter.next();
        	if (screener != null) {
        		row = screener.getScreenedRowData(resultColumns, row);
        	}
        	
        	rows.add(row);
        }
        
        return rows;
    }

	public int getDbRowsCount() {
		return dbRowsCount;
	}

	public void setDbRowsCount(int dbRowsCount) {
		this.dbRowsCount = dbRowsCount;
	}

	public List<String[]> getStringifiedRows() {
		List<String[]> rows = new ArrayList<String[]>();
		Iterator<String[]> iter = stringifiedRowIterator();
		while (iter.hasNext()) {
			rows.add(iter.next());
		}

		return rows;
	}
    
    public Iterator<Object[]> rowIterator() {
    	if (rows != null) {
    		return rows.iterator();
    	} else if (rowGen != null) {
    		return rowIterator(rowGen.iterator());
    	}
    	
    	return null;
    }
    
    public Iterator<String[]> stringifiedRowIterator() {
    	Iterator<Object[]> iter = null;
    	if (rows != null) {
    		iter = rows.iterator();
    	} else if (rowGen != null) {
    		iter = rowIterator(rowGen.iterator());
    	}
    	
    	return stringifiedRowIterator(iter);
    }
    
    public void close() {
    	if (rowGen != null) {
    		rowGen.cleanup();
    	}
    	
    	if (rows != null) {
    		rows = null;
    	}
    }
    
    public String[] stringifyRow(Object[] row) {
        String result[] = new String[row.length];
        
        for(int j = 0; j < row.length; j++) {
            if (row[j] == null) {
                result[j] = null;
            } else if (row[j] instanceof Timestamp && tsf != null) {
                result[j] = tsf.format(row[j]);
            } else if (row[j] instanceof Date && sdf != null){
                result[j] = sdf.format(row[j]);
            } else if (Util.isOraTimestamp(row[j])) {
            	Date dateObj = Util.getDateFromOraTimestamp(row[j]);
            	if (tsf != null) {
            		result[j] = tsf.format(dateObj);
            	} else if (sdf != null) {
            		result[j] = sdf.format(dateObj);
            	} else {
            		result[j] = dateObj.toString();
            	}            	            		
            } else {
            	result[j] = row[j].toString();
            }
        }

        return result;
    }
    
    private Iterator<Object[]> rowIterator(final Iterator<Object[]> iter) {
    	return new Iterator<Object[]>() {
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Object[] next() {
				Object[] row = iter.next();
				if (screener != null) {
					row = screener.getScreenedRowData(resultColumns, row);
				}
				
				return row;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();				
			}    		
    	};
    }
    
    private Iterator<String[]> stringifiedRowIterator(final Iterator<Object[]> iter) {
    	return new Iterator<String[]>() {
			@Override
			public boolean hasNext() {					
				return iter.hasNext();
			}

			@Override
			public String[] next() {
				return stringifyRow(iter.next());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}    			
   		};
    }
    
}
