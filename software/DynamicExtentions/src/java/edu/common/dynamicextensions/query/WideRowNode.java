package edu.common.dynamicextensions.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        			WideRowNode childNode = null;
        			int instance = 0;
        			for (Map.Entry<String, WideRowNode> childRow : childTabRows.getValue().entrySet()) {
        				childNode = childRow.getValue();
        				List<List<ResultColumn>> flattenedChildRows = childNode.flatten(maxRowCntMap, tabFormTypeMap, tabFieldsMap);
        				for (List<ResultColumn> flattenedChildRow : flattenedChildRows) {
        					for (ResultColumn col : flattenedChildRow) {
        						col.setInstance(instance);
        						row.add(col);
        					}
//        					row.addAll(flattenedChildRow);
        				}   
        				
        				++instance;
        			}
        			
        			int rowCount = childTabRows.getValue().size();
        			Integer maxCount = maxRowCntMap.get(childTabRows.getKey());
        			if (maxCount == null) {
        				maxCount = 0;
        			} else if (maxCount == 0) {
        				maxCount = 1;
        			}
        			
        			if (rowCount == maxCount) {
        				currentRows.add(row);
        				continue;
        			}
        			            			        			
        			List<ExpressionNode> tabFields = null;
        			if (childNode != null) {
        				tabFields = getTabFieldsMap(tabFieldsMap, childNode.getAliases());
        			} else {
        				tabFields = tabFieldsMap.get(childTabRows.getKey()); // not sure when this condition occurs
        			}
        			
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
    
    
    private Set<String> getAliases() {   	
		Set<String> aliases = new HashSet<String>();
		aliases.add(alias);

		for (Map<String, WideRowNode> childrenRows : childrenRowsMap.values()) {
			for (Map.Entry<String, WideRowNode> wideRow : childrenRows
					.entrySet()) {
				if (wideRow.getValue() != null) {
					aliases.addAll(wideRow.getValue().getAliases());
					break;
				}
			}
		}

		return aliases;
    }
    
    private List<ExpressionNode> getTabFieldsMap(Map<String, List<ExpressionNode>> tabFieldsMap, Set<String> aliases) {
		List<ExpressionNode> tabFields = new ArrayList<ExpressionNode>();
		for (String alias : aliases) {
			List<ExpressionNode> fields = tabFieldsMap.get(alias);
			if (fields == null) {
				continue;
			}
			tabFields.addAll(fields);
		}

		return tabFields;
    }    
}
