package edu.common.dynamicextensions.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.query.ast.ExpressionNode;

public class WideRowNode implements Serializable {

    private static final long serialVersionUID = 9096578807871606291L;

    private String alias;
    
    private String id;
    
    private List<ResultColumn> columns;
    
    public Map<String, Map<String, WideRowNode>> childrenRowsMap = 
            new LinkedHashMap<String, Map<String, WideRowNode>>();
    
    public WideRowNode(String alias, String id) {
        this.alias = alias;
        this.id = id;
    }
    
    public List<ResultColumn> getColumns() {
        return columns;
    }
    
    public void setColumns(List<ResultColumn> columns) {
        this.columns = columns;
    }
    
    public Map<String, WideRowNode> getChildrenRows(String alias) {
        return childrenRowsMap.get(alias);
    }
    
    public Map<String, WideRowNode> initChildrenRows(String alias) {
        Map<String, WideRowNode> childrenRows = new LinkedHashMap<String, WideRowNode>();
        childrenRowsMap.put(alias, childrenRows);
        return childrenRows;
    }
    
    public List<List<ResultColumn>> flatten(
    		Map<String, Integer> maxRowCntMap, 
    		Map<String, Boolean> tabFormTypeMap, 
    		Map<String, List<ExpressionNode>> tabFieldsMap) {
    	
    	List<ResultColumn> currentRow = new ArrayList<ResultColumn>();
    	if (this.columns != null) {
    		currentRow.addAll(this.columns);
    	}
    	
    	List<List<ResultColumn>> rows = new ArrayList<List<ResultColumn>>();
    	rows.add(currentRow);
    	
    	for (Map.Entry<String, Map<String, WideRowNode>> childTabRows : childrenRowsMap.entrySet()) {
    		List<List<ResultColumn>> currentRows = new ArrayList<List<ResultColumn>>();
    		
    		if (tabFormTypeMap.get(childTabRows.getKey())) {
    			for (List<ResultColumn> existingRow : rows) {
        			for (Map.Entry<String, WideRowNode> childRow : childTabRows.getValue().entrySet()) {
        				List<List<ResultColumn>> flattenedChildRows = childRow.getValue().flatten(maxRowCntMap, tabFormTypeMap, tabFieldsMap);            				            			
        				for (List<ResultColumn> flattenedChildRow : flattenedChildRows) {
        					List<ResultColumn> row = new ArrayList<ResultColumn>(existingRow);
        					row.addAll(flattenedChildRow);
        					currentRows.add(row);
        				}
        			}
        			
        			if (!childTabRows.getValue().isEmpty()) {
        				continue;
        			}
        			
            		List<ExpressionNode> tabFields = tabFieldsMap.get(childTabRows.getKey());
            		if (tabFields == null) {
            			tabFields = Collections.emptyList();
            		}
            			
            		List<ResultColumn> row = new ArrayList<ResultColumn>(existingRow);
            		for (ExpressionNode fieldExpr : tabFields) {
            			row.add(new ResultColumn(fieldExpr, 0));
            		}                			
            		currentRows.add(row);
    			}
    		} else { // sub-form or multi-valued and deep
    			for (List<ResultColumn> existingRow : rows) {
        			List<ResultColumn> row = new ArrayList<ResultColumn>(existingRow);
        			for (Map.Entry<String, WideRowNode> childRow : childTabRows.getValue().entrySet()) {
        				List<List<ResultColumn>> flattenedChildRows = childRow.getValue().flatten(maxRowCntMap, tabFormTypeMap, tabFieldsMap);
        				for (List<ResultColumn> flattenedChildRow : flattenedChildRows) {
        					row.addAll(flattenedChildRow);
        				}   
        			}
        			
        			int rowCount = childTabRows.getValue().size();
        			Integer maxCount = maxRowCntMap.get(childTabRows.getKey());
        			if (maxCount == null) {
        				maxCount = 0;
        			} else if (maxCount == 0) {
        				maxCount = 1;
        			}
        			            			
        			List<ExpressionNode> tabFields = tabFieldsMap.get(childTabRows.getKey());
        			if (tabFields == null) {
        				tabFields = Collections.emptyList();
        			}
        			            			
            		for (int i = rowCount; i < maxCount; ++i) {
            			for (ExpressionNode fieldExpr : tabFields) {
            				row.add(new ResultColumn(fieldExpr, i));
            			}
            		}
            		
            		currentRows.add(row);        				
    			}
    		}
    		
    		rows = currentRows;
    	}
    	
    	return rows;
    }        
}
