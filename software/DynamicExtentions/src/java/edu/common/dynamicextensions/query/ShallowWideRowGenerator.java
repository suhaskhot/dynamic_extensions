package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.cachestore.LinkedEhCacheMap;

public class ShallowWideRowGenerator {
	private static final Comparator<ResultColumn> RESULT_COL_SORTER = 
		new Comparator<ResultColumn>() {
			@Override
			public int compare(ResultColumn arg0, ResultColumn arg1) {
				//return arg0.getExpression().getPos() - arg1.getExpression().getPos();
				
				// fixed
				ExpressionNode expr0 = arg0.getExpression();
				ExpressionNode expr1 = arg1.getExpression();
				
				String[] forms0 = expr0.getFormNames();
				String[] forms1 = expr1.getFormNames();
				
				if (forms0.length != forms1.length || forms0.length != 1 || !forms0[0].equals(forms1[0])) {
					return arg0.getExpression().getPos() - arg1.getExpression().getPos();
				}
				
				int pos0 = getPos(arg0.getInstance(), expr0);
				int pos1 = getPos(arg1.getInstance(), expr1);
				return pos0 - pos1;
			}
			
			private int getPos(int instance, ExpressionNode exprNode) {
				int pos = instance * 100 + exprNode.getPos();
				if (exprNode instanceof FieldNode) {
					FieldNode field = (FieldNode)exprNode;
					if (field.getCtrl() instanceof MultiSelectControl) {
						pos = exprNode.getPos();
					}
				}
				
				return pos;				
			}
    	};
    
    private LinkedEhCacheMap<String, WideRowNode> wideRows = new LinkedEhCacheMap<String, WideRowNode>();
    
    private Map<String, String[]> tabJoinPath = new HashMap<String, String[]>();
    
    private Map<String, Integer> aliasRowCountMap = new HashMap<String, Integer>();
       
    private Map<String, List<ExpressionNode>> tabFieldsMap = new HashMap<String, List<ExpressionNode>>();
   
    // false if form is either subform or multiselect. true otherwise
    private Map<String, Boolean> tabFormTypeMap = new HashMap<String, Boolean>(); 
    
    private String rootTabAlias = null;
    
    private String lastRootId = null;
    
    private QueryExpressionNode queryExpr;
    
    private JoinTree queryJoinTree;
    
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
                        
//                    	if (id == null) {
//                    		break;
//                    	}                        
                                                
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
    
    public void cleanup() {
    	wideRows.destroy();
    }
    
    public List<ResultColumn> getResultColumns() {
    	List<ResultColumn> columns = null;
    	
    	Iterator<WideRowNode> iter = wideRows.iterator();
    	if (iter != null && iter.hasNext()) {
    		List<List<ResultColumn>> resultCols = iter.next().flatten(aliasRowCountMap, tabFormTypeMap, tabFieldsMap);
    		columns = resultCols.iterator().next();
    	} else {
    		columns = getTabColumns(aliasRowCountMap, tabFieldsMap);
    	}

    	Collections.sort(columns, RESULT_COL_SORTER);        
        return columns;    	
    }
    
    public Iterator<Object[]> iterator() {
    	return new Iterator<Object[]>() {
    		private Iterator<WideRowNode> iterator = wideRows.iterator();
    		
    		private Iterator<Object[]> subRowIter = null;
    		
    		private WideRowNode next = null;
    		
    		{
    			ensureNext();
    		}
    		
			@Override
			public boolean hasNext() {			
				return subRowIter != null;
			}

			@Override
			public Object[] next() {
				Object[] row = null;
				if (subRowIter != null) {
					row = subRowIter.next();
					ensureNext();
				}
				
				return row;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();				
			}    		
			
			private void ensureNext() {
				if (subRowIter != null && subRowIter.hasNext()) {
					return;
				}
								
				next = iterator.hasNext() ? iterator.next() : null;
				if (next != null) {
					List<Object[]> rows = flattenedRows(next);
					subRowIter = rows.iterator();
				} else {
					subRowIter = null;
				}				
			}
    	};
    }
    

    private List<Object[]> flattenedRows(WideRowNode wideRow) {
    	List<Object[]> result = new ArrayList<Object[]>();
    	
        List<List<ResultColumn>> rows = wideRow.flatten(aliasRowCountMap, tabFormTypeMap, tabFieldsMap);        
        for (List<ResultColumn> row : rows) {
            Collections.sort(row, RESULT_COL_SORTER);            
            Object[] values = new Object[row.size()];
            int i = 0;
            for (ResultColumn col : row) {
            	values[i++] = col.getValue();
            }
            
            result.add(values);
        }
        
        return result;    	
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
        tabFormTypeMap.put(tabAlias, !(joinTree.isSubFormOrMultiSelect() || joinTree.isExtensionForm()));
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
        if (queryExpr.getLimitExpr() != null && DbSettingsFactory.isOracle()) {
        	numCols--;        	
        }
        
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

}