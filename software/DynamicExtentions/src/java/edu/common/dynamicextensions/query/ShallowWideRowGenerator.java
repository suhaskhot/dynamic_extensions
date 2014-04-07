package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class ShallowWideRowGenerator {     
    private Map<String, WideRowNode> wideRows = new LinkedHashMap<String, WideRowNode>();
    
    private Map<String, String[]> tabJoinPath = new HashMap<String, String[]>();
    
    private Map<String, Integer> aliasRowCountMap = new HashMap<String, Integer>();
       
    private Map<String, List<ExpressionNode>> tabFieldsMap = new HashMap<String, List<ExpressionNode>>();
   
    // false if form is either subform or multiselect. true otherwise
    private Map<String, Boolean> tabFormTypeMap = new HashMap<String, Boolean>(); 
    
    private String rootTabAlias = null;
    
    private String lastRootId = null;
    
    private QueryExpressionNode queryExpr;
    
    private JoinTree queryJoinTree;
    
    private String dateFormat;
    
    public ShallowWideRowGenerator(JoinTree queryJoinTree, QueryExpressionNode queryExpr) {
        this.queryExpr = queryExpr;
        this.queryJoinTree = queryJoinTree;
        this.rootTabAlias = queryJoinTree.getAlias();
        initTableJoinPath(queryJoinTree);
    }
    
    public void start() {
        wideRows.clear();
        aliasRowCountMap.clear();
        aliasRowCountMap.put(rootTabAlias, 1);    
        tabFieldsMap = getTabFieldsMap();
        lastRootId = null;
    }
    
    public ShallowWideRowGenerator dateFormat(String dateFormat) {
    	this.dateFormat = dateFormat;
    	return this;
    }
    
    public void processResultSet(ResultSet rs) {
        try {
            while (rs.next()) {
                Map<String, String> tabAliasIdMap = getTabAliasIdMap(rs);
                Map<String, List<ResultColumn>> tabAliasColValuesMap = getTabAliasColumnValuesMap(rs);
                
                String rootId = tabAliasIdMap.get(rootTabAlias);
                WideRowNode rootTabRow = wideRows.get(rootId);
                if (lastRootId == null || !lastRootId.equals(rootId)) {
                    assert(rootTabRow == null);
                    rootTabRow = new WideRowNode(rootTabAlias, rootId);
                    rootTabRow.setColumns(tabAliasColValuesMap.get(rootTabAlias));
                    wideRows.put(rootId, rootTabRow);
                    if (lastRootId != null) {
                    	mergeCounts(wideRows.get(lastRootId));
                    }                    
                }
                
                for (Map.Entry<String, String> tabAliasId : tabAliasIdMap.entrySet()) {
                    if (tabAliasId.getKey().equals(rootTabAlias)) {
                        continue;
                    }
                    
                    String[] joinNodes = tabJoinPath.get(tabAliasId.getKey());
                    WideRowNode wideRow = rootTabRow;
                    for (int j = 1; j < joinNodes.length; ++j) {
                        Map<String, WideRowNode> childTabRows = wideRow.getChildrenRows(joinNodes[j]);
                        if (childTabRows == null) {
                            childTabRows = wideRow.initChildrenRows(joinNodes[j]);
                        }
                        
                        String id = "-1";
                        if (tabAliasIdMap.containsKey(joinNodes[j])) {
                        	id = tabAliasIdMap.get(joinNodes[j]);
                        }
                        
                    	if (id == null) {
                    		break;
                    	}                        
                                                
                        WideRowNode childRow = childTabRows.get(id);
                        if (childRow == null) {
                            childRow = new WideRowNode(joinNodes[j], id);
                            childRow.setColumns(tabAliasColValuesMap.get(joinNodes[j]));
                            childTabRows.put(id, childRow);
                           	//incrTabRowCnt(joinNodes[j]);                            
                        }
                        
                        wideRow = childRow;
                    }
                }
                
                lastRootId = rootId;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing result for generating wide rows", e);
        }
    }
    
    public void end() {
    	if (lastRootId != null) {
    		mergeCounts(wideRows.get(lastRootId));
    	}        
    }
    
    public QueryResultData getQueryResultData() {
        QueryResultData resultData = null;
        for (WideRowNode wideRow : wideRows.values()) {
            if (resultData == null) {
                resultData = initQueryResultData(wideRow);
            }
            
            List<List<ResultColumn>> rows = wideRow.flatten(aliasRowCountMap);
            for (List<ResultColumn> row : rows) {
                Collections.sort(row, new Comparator<ResultColumn>() {
                	@Override
                	public int compare(ResultColumn arg0, ResultColumn arg1) {
                		return arg0.getExpression().getPos() - arg1.getExpression().getPos();
                	}
                });
                
                Object[] values = new Object[row.size()];
                int i = 0;
                for (ResultColumn col : row) {
                	values[i++] = col.getValue();
                }
                
                resultData.addRow(values);                
            }            
        }
        
        return resultData;      
    }
    
    private void mergeCounts(WideRowNode wideRow) {
    	if (wideRow.childrenRowsMap == null) {
    		return;
    	}
    	
    	for (Map.Entry<String, Map<String, WideRowNode>> childTable : wideRow.childrenRowsMap.entrySet()) {
    		Integer count = childTable.getValue().size();
    		Integer actual = aliasRowCountMap.get(childTable.getKey());
    		if (actual == null || actual < count) {
    			aliasRowCountMap.put(childTable.getKey(), count);
    		}
    		
    		for (WideRowNode childTableRow : childTable.getValue().values()) {
    			mergeCounts(childTableRow);
    		}
    	}
    }
        
    private void initTableJoinPath(JoinTree queryJoinTree) {
        tabJoinPath.clear();
        initTableJoinPath(queryJoinTree, new ArrayList<String>());        
    }
    
    private void initTableJoinPath(JoinTree joinTree, List<String> path) {
        String tabAlias = joinTree.getAlias();
        path = new ArrayList<String>(path);
        path.add(tabAlias);
        
        tabJoinPath.put(tabAlias, path.toArray(new String[0]));
        tabFormTypeMap.put(tabAlias, !joinTree.isSubFormOrMultiSelect());
        for (JoinTree childTree : joinTree.getChildren()) {
            initTableJoinPath(childTree, path);
        }
    }
    
    private Map<String, String> getTabAliasIdMap(ResultSet rs) 
    throws Exception {
        Map<String, String> tabAliasIdMap = new LinkedHashMap<String, String>();
        
        int cols = 0;
        List<ExpressionNode> selectElements = queryExpr.getSelectList().getElements();        
        for (ExpressionNode element : selectElements) {
            ++cols;
            
            if (!(element instanceof FieldNode)) {
                continue;
            }
            
            FieldNode field = (FieldNode)element;
            if (field.getCtrl() instanceof MultiSelectControl) {
                tabAliasIdMap.put(field.getTabAlias(), rs.getString(cols));
            }         
        }
        
        int numCols = rs.getMetaData().getColumnCount();
        for (int i = selectElements.size() + 1; i <= numCols; i += 2) {
            tabAliasIdMap.put(rs.getString(i), rs.getString(i + 1));
        }
        
        return tabAliasIdMap;
    }
        
    private Map<String, List<ResultColumn>> getTabAliasColumnValuesMap(ResultSet rs) 
    throws Exception {
        Map<String, List<ResultColumn>> aliasColumnValuesMap = new HashMap<String, List<ResultColumn>>(); 
                
        int col = 0;
        List<ExpressionNode> selectElements = queryExpr.getSelectList().getElements();      
        for (ExpressionNode element : selectElements) {
            col++;
            
            String[] aliasPk = WideRowUtil.getTabAliasPk(queryJoinTree, element);
            String alias = aliasPk == null ? "literal" : aliasPk[0];
            List<ResultColumn> columns = aliasColumnValuesMap.get(alias);
            if (columns == null) {
                columns = new ArrayList<ResultColumn>();
                aliasColumnValuesMap.put(alias, columns);
            }            
            columns.add(new ResultColumn(element, rs.getObject(col)));         
        }
        
        return aliasColumnValuesMap;
    }
    
    private Map<String, List<ExpressionNode>> getTabFieldsMap() {
        Map<String, List<ExpressionNode>> tabFieldsMap = new LinkedHashMap<String, List<ExpressionNode>>();
        
        int i = -1;
        for (ExpressionNode exprNode : queryExpr.getSelectList().getElements()) {
            String[] aliasPk = WideRowUtil.getTabAliasPk(queryJoinTree, exprNode);
            String tabAlias = aliasPk == null ? "literal" : aliasPk[0];

            List<ExpressionNode> fields = tabFieldsMap.get(tabAlias);
            if (fields == null) {
                fields = new ArrayList<ExpressionNode>();
                tabFieldsMap.put(tabAlias, fields);
                ++i;
            }
            
            if (fields.isEmpty()) {
            	exprNode.setPos(i);
            } else if (i != fields.get(0).getPos()){
            	exprNode.setPos(++i);            	
            } else {
            	exprNode.setPos(i);
            }
            fields.add(exprNode);
        }
        
        return tabFieldsMap;        
    }
    
    private QueryResultData initQueryResultData(WideRowNode wideRow) {
        List<ResultColumn> columns = getTabColumns(aliasRowCountMap, tabFieldsMap);
        Collections.sort(columns, new Comparator<ResultColumn>() {
        	@Override
        	public int compare(ResultColumn arg0, ResultColumn arg1) {
        		return arg0.getExpression().getPos() - arg1.getExpression().getPos();
        	}
        });
                
        return new QueryResultData(columns, dateFormat);
    }
    
    private List<ResultColumn> getTabColumns(Map<String, Integer> maxCount, Map<String, List<ExpressionNode>> tabFieldsMap) {
        List<ResultColumn> resultColumns = new ArrayList<ResultColumn>();
                    
        for (Map.Entry<String, List<ExpressionNode>> tabFields : tabFieldsMap.entrySet()) {
        	String alias = tabFields.getKey();
        	Integer count = null;
        	if (tabFormTypeMap.get(alias)) {
        		count = 1;
        	} else {
        		count = maxCount.get(alias);
        	}
        	
        	if (count == null || count == 0) {
        		count = 1;
        	}
        	
        	List<ExpressionNode> fields = tabFieldsMap.get(alias);
        	for (int i = 0; i < count; ++i) {
        		for (ExpressionNode field : fields) {
        			resultColumns.add(new ResultColumn(field, i));
        		}
        	}
        }
                    
        return resultColumns;
    }
    
    private class WideRowNode {
        private String alias;
        
        private String id;
        
        private List<ResultColumn> columns;
        
        private Map<String, Map<String, WideRowNode>> childrenRowsMap = 
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
        
        public List<List<ResultColumn>> flatten(Map<String, Integer> maxRowCntMap) {        	        	        	
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
            				List<List<ResultColumn>> flattenedChildRows = childRow.getValue().flatten(maxRowCntMap);            				            			
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
            				List<List<ResultColumn>> flattenedChildRows = childRow.getValue().flatten(maxRowCntMap);
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
}