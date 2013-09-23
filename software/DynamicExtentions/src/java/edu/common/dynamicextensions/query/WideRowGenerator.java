package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class WideRowGenerator {
	private static final String separator = ": ";
	
	private Map<String, WideRowNode> wideRows = new LinkedHashMap<String, WideRowNode>();
	
	private Map<String, String[]> tabJoinPath = new HashMap<String, String[]>();
	
	private Map<String, Integer> aliasRowCountMap = new HashMap<String, Integer>();
	
	private Map<String, Integer> currAliasRowCountMap = new HashMap<String, Integer>(); 
	
	private String rootTabAlias = null;
	
	private String lastRootId = null;
	
	private QueryExpressionNode queryExpr;
	
	public WideRowGenerator(JoinTree queryJoinTree, QueryExpressionNode queryExpr) {
		this.queryExpr = queryExpr;
		this.rootTabAlias = queryJoinTree.getAlias();
		initTableJoinPath(queryJoinTree);
	}
	
	public void start() {
		wideRows.clear();
		aliasRowCountMap.clear();
		currAliasRowCountMap.clear();
		lastRootId = null;
	}
	
	public void processResultSet(ResultSet rs) {
		try {
			while (rs.next()) {
				Map<String, String> tabAliasIdMap = getTabAliasIdMap(rs);
				Map<String, List<Object>> tabAliasColValuesMap = getTabAliasColumnValuesMap(rs);
				
				String rootId = tabAliasIdMap.get(rootTabAlias);
				WideRowNode rootTabRow = wideRows.get(rootId);
				if (lastRootId == null || !lastRootId.equals(rootId)) {
					assert(rootTabRow == null);
					rootTabRow = new WideRowNode(rootTabAlias, rootId);
					rootTabRow.setColumns(tabAliasColValuesMap.get(rootTabAlias));
					wideRows.put(rootId, rootTabRow);
					mergeCounts();
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
						
						String id = tabAliasIdMap.get(joinNodes[j]);
						if (id == null) {
							id = "-1";
						}
						
						WideRowNode childRow = childTabRows.get(id);
						if (childRow == null) {
							childRow = new WideRowNode(joinNodes[j], id);
							childRow.setColumns(tabAliasColValuesMap.get(joinNodes[j]));
							childTabRows.put(id, childRow);
							incrTabRowCnt(joinNodes[j]);
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
		mergeCounts();
	}
	
	public QueryResultData getQueryResultData() {
		QueryResultData resultData = null;
		for (WideRowNode wideRow : wideRows.values()) {
			if (resultData == null) {
				resultData = initQueryResultData(wideRow);
			}
			
			resultData.addRow(wideRow.flatten(aliasRowCountMap).toArray(new Object[0]));
		}
		
		return resultData;		
	}
	
	private void mergeCounts() {
		for (Map.Entry<String, Integer> curAliasCnt : currAliasRowCountMap.entrySet()) {
			Integer count = curAliasCnt.getValue();
			Integer actual = aliasRowCountMap.get(curAliasCnt.getKey());
			if (actual == null || actual < count) {
				aliasRowCountMap.put(curAliasCnt.getKey(), count);
			}
		}
		
		currAliasRowCountMap.clear();
	}
	
	private void incrTabRowCnt(String tabAlias) {
		Integer count = currAliasRowCountMap.get(tabAlias);
		if (count == null) {
			count = 0;
		}
		
		currAliasRowCountMap.put(tabAlias, count + 1);
	}
	
	private void initTableJoinPath(JoinTree queryJoinTree) {
		Map<String, String> pathMap = new HashMap<String, String>();
		initTableJoinPath(queryJoinTree, "" , pathMap);
		
		tabJoinPath.clear();
		for (Map.Entry<String, String> path : pathMap.entrySet()) {
			tabJoinPath.put(path.getKey(), path.getValue().split(","));
		}
	}
	
	private void initTableJoinPath(JoinTree joinTree, String path, Map<String, String> pathMap) {
		String tabAlias = joinTree.getAlias();
		path = path.length() == 0 ? tabAlias : path + "," + tabAlias;
		pathMap.put(tabAlias, path);
		
		for (JoinTree childTree : joinTree.getChildren()) {
			initTableJoinPath(childTree, path, pathMap);
		}
	}
	
	private Map<String, String> getTabAliasIdMap(ResultSet rs) 
	throws Exception {
		Map<String, String> tabAliasIdMap = new LinkedHashMap<String, String>();
		
		List<ExpressionNode> selectElements = queryExpr.getSelectList().getElements();
		int numCols = rs.getMetaData().getColumnCount();
		int startIdx = selectElements.size() + 1;
		
		int cols = 0;
		for (ExpressionNode element : selectElements) {
			++cols;
			
			if (!(element instanceof FieldNode)) {
				continue;
			}
			
			FieldNode field = (FieldNode)element;
			String tabAlias = field.getTabAlias();
			if (field.getCtrl() instanceof MultiSelectControl) {
				tabAliasIdMap.put(tabAlias, rs.getString(cols));
			} else if (!tabAliasIdMap.containsKey(tabAlias)) {
				tabAliasIdMap.put(tabAlias, getTabAliasId(rs, tabAlias, startIdx, numCols));
			}			
		}
		
		return tabAliasIdMap;
	}
	
	private String getTabAliasId(ResultSet rs, String tabAlias, int start, int end) 
	throws Exception {
		String result = null;
		for (int i = start; i <= end; i += 2) {
			if (tabAlias.equals(rs.getString(i))) {
				result = rs.getString(i + 1);
				break;
			}
		}
		
		return result;
	}
	
	private Map<String, List<Object>> getTabAliasColumnValuesMap(ResultSet rs) 
	throws Exception {
		Map<String, List<Object>> aliasColumnValuesMap = new HashMap<String, List<Object>>(); 
				
		int col = 0;
		List<ExpressionNode> selectElements = queryExpr.getSelectList().getElements();		
		for (ExpressionNode element : selectElements) {
			col++;
			
			if (!(element instanceof FieldNode)) {
				continue;
			}
			
			FieldNode field = (FieldNode)element;
			List<Object> columns = aliasColumnValuesMap.get(field.getTabAlias());
			if (columns == null) {
				columns = new ArrayList<Object>();
				aliasColumnValuesMap.put(field.getTabAlias(), columns);
			}
			
			columns.add(rs.getObject(col));			
		}
		
		return aliasColumnValuesMap;
	}
	
	private Map<String, List<FieldNode>> getTabFieldsMap() {
    	Map<String, List<FieldNode>> tabFieldsMap = new HashMap<String, List<FieldNode>>();
    	
    	for (ExpressionNode exprNode : queryExpr.getSelectList().getElements()) {
    		if (!(exprNode instanceof FieldNode)) {
    			continue;
    		}
    		
    		FieldNode field = (FieldNode)exprNode;
    		List<FieldNode> fields = tabFieldsMap.get(field.getTabAlias());
    		if (fields == null) {
    			fields = new ArrayList<FieldNode>();
    			tabFieldsMap.put(field.getTabAlias(), fields);
    		}
    		
    		fields.add(field);
    	}
    	
    	return tabFieldsMap;		
	}
	
	private QueryResultData initQueryResultData(WideRowNode wideRow) {
		Map<String, List<FieldNode>> fieldsMap = getTabFieldsMap();
		List<String> columnHeadings = wideRow.getColumnHeadings(aliasRowCountMap, fieldsMap); 
		return new QueryResultData(columnHeadings.toArray(new String[0]));
	}
		
	private class WideRowNode {
		private String alias;
		
		private String id;
		
		private List<Object> columns;
		
		private Map<String, Map<String, WideRowNode>> childrenRowsMap = 
				new LinkedHashMap<String, Map<String, WideRowNode>>();
		
		public WideRowNode(String alias, String id) {
			this.alias = alias;
			this.id = id;
		}
		
		public List<Object> getColumns() {
			return columns;
		}
		
		public void setColumns(List<Object> columns) {
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
		
    	public List<Object> flatten(Map<String, Integer> tabRowCount) {
    		List<Object> result = new ArrayList<Object>();
    		
    		if (columns != null) {
    			result.addAll(columns);
    		}
    		
   			for (Map.Entry<String, Map<String, WideRowNode>> childTabRows : childrenRowsMap.entrySet()) {
    			assert(!childTabRows.getValue().isEmpty());    				
    			Integer maxRowsCount = tabRowCount.get(childTabRows.getKey());
    			int columnCount = 0, currRowCount = 0;
    			for (WideRowNode childTabRow : childTabRows.getValue().values()) {
    				List<Object> childTabRowColumns = childTabRow.flatten(tabRowCount);
    				columnCount = childTabRowColumns.size();
    				result.addAll(childTabRowColumns);  
    				++currRowCount;
    			}    
    				
    			int remaining = maxRowsCount - currRowCount;
    			for (int j = 0; j < remaining * columnCount; ++j) {
    				result.add(null);
    			}
    		}

    		
    		return result;    		
    	}
    	
    	
    	public List<String> getColumnHeadings(Map<String, Integer> maxCount, Map<String, List<FieldNode>> fieldsMap) {
    		List<String> result = new ArrayList<String>();
    		
    		if (columns != null) {
    			List<FieldNode> fields = fieldsMap.get(alias);
    			for (FieldNode field : fields) {
    				result.add(Query.getColumnHeading(field, separator));
    			}    			
    		}
    		
   			for (Map.Entry<String, Map<String, WideRowNode>> childTabRows : childrenRowsMap.entrySet()) {
    			assert(!childTabRows.getValue().isEmpty());    				
    				
    			WideRowNode childTabRow = childTabRows.getValue().values().iterator().next();
    			List<String> childTabRowColHeading = childTabRow.getColumnHeadings(maxCount, fieldsMap);
    				
    			Integer count = maxCount.get(childTabRows.getKey());
    			for (int i = 0; i < count; ++i) {
    				result.addAll(childTabRowColHeading);
    			}    				
    		}
    		
    		return result;
    	}    	
	}
}