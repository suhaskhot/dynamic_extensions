package edu.common.dynamicextensions.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.query.ast.CrosstabNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class Crosstab implements ResultPostProc {
	private QueryExpressionNode queryExpr;
	
	private Map<String, Row> rows = new TreeMap<String, Row>();
	
	private Set<Object> dynamicCols = new TreeSet<Object>();
	
	private Set<String> measureCols = new LinkedHashSet<String>();
	
	private boolean rollup;
	
	private boolean numericMeasure;
			
	public Crosstab(QueryExpressionNode queryExpr) {
		this.queryExpr = queryExpr;
		
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();

		String rollupType = ctSpec.getRollupType();
		rollup = rollupType != null && !rollupType.trim().isEmpty();
		
		numericMeasure = true;
		for (int valIdx : ctSpec.getMeasureColumns()) {
			ExpressionNode column = queryExpr
					.getSelectList()
					.getElements()
					.get(valIdx - 1);
			
			if (column.getType() != DataType.INTEGER && column.getType() != DataType.FLOAT) {
				numericMeasure = false;				
			}
			
			measureCols.add(getMeasureColumnLabel(column));
		}
		
		if (rollup && !numericMeasure) {
			throw new IllegalArgumentException("Roll up can be done only on numeric measure columns");			
		}				
	}
	
	@Override
	public int  processResultSet(ResultSet rs) {
		try {
			int rowCount = 0;
						
			while (rs.next()) {
				addRow(rs);
				rowCount++;
			}
			
			return rowCount;			
		} catch (SQLException e) {
			throw new RuntimeException("Error processing result set", e);
		}
	}
	
	@Override
	public List<ResultColumn> getResultColumns() {
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();
		List<ExpressionNode> selectList = queryExpr.getSelectList().getElements();
		
		List<ResultColumn> columns = new ArrayList<ResultColumn>();		
		for (int idx : ctSpec.getRowGroupByColumns()) {
			columns.add(new ResultColumn(selectList.get(idx - 1), 0));			
		}
		
		if (ctSpec.getMeasureColumns().size() > 1) {
			ExpressionNode valueType = new LiteralValueNode(DataType.STRING);			
			valueType.setLabel("Value Type");			
			columns.add(new ResultColumn(valueType, 0));
		}
		
		ExpressionNode colExpr = selectList.get(ctSpec.getColGroupByColumn() - 1);
		String dateFormat = getDateFormat(colExpr);
		SimpleDateFormat sdf = dateFormat != null ? new SimpleDateFormat(dateFormat) : null;

		for (Object colKey : dynamicCols) {
			ExpressionNode colExprClone = colExpr.copy();
			if (sdf != null) {
				colExprClone.setLabel(sdf.format((Date)colKey));
			} else {
				colExprClone.setLabel(colKey.toString());
			}
						
			columns.add(new ResultColumn(colExprClone, 0));
		}
		
		if (rollup) { // TODO: Change this to SumNode
			ExpressionNode total = new LiteralValueNode(DataType.INTEGER);			
			total.setLabel("Total");
			columns.add(new ResultColumn(total, 0));
		}
		
		return columns;		
	}
	
	@Override
	public List<Object[]> getRows() {
		List<Object[]> result = new ArrayList<Object[]>();
		
		for (Row row : rows.values()) {
			for (String measureCol : measureCols) {
				List<Object> values = new ArrayList<Object>(row.getRowKeyValues());
				BigDecimal sum = BigDecimal.ZERO;
				
				if (measureCols.size() > 1) {
					values.add(measureCol);
				}
				
				for (Object colKey : dynamicCols) {
					Map<String, Object> measureMap = row.getColValue(colKey);
					Object measure = measureMap.get(measureCol);
					values.add(measure);
					
					if (numericMeasure) {
						sum = sum.add(new BigDecimal(measure.toString()));
					}
				}
				
				if (rollup) {
					values.add(sum);
				}
				
				result.add(values.toArray(new Object[0]));								
			}
		}
		
		return result;
	}
	
	@Override
	public void cleanup() {
		; // no-op
	}
		
	private void addRow(ResultSet rs) 
	throws SQLException {
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();																									
		
		List<Object> rowKeyValues = new ArrayList<Object>();
		for (int idx : ctSpec.getRowGroupByColumns()) {
			rowKeyValues.add(rs.getObject(idx));
		}
		
		Object colKeyVal = rs.getObject(ctSpec.getColGroupByColumn());
		colKeyVal = colKeyVal == null ? "" : colKeyVal;
		
		Map<String, Object> measureMap = new LinkedHashMap<String, Object>();
		for (int idx : ctSpec.getMeasureColumns()) {
			ExpressionNode node = queryExpr.getSelectList().getElements().get(idx - 1);
			String label = getMeasureColumnLabel(node);
			measureMap.put(label, rs.getObject(idx));
		}
		
		List<List<Object>> rowGrps = new ArrayList<List<Object>>();
		if (!rollup) {
			rowGrps.add(rowKeyValues);
		} else if (ctSpec.getRollupType().equals("powerset")) {
			rowGrps.addAll(powerset(rowKeyValues));
		} else if (ctSpec.getRollupType().equals("rollup")) {
			rowGrps.addAll(rollup(rowKeyValues));
		}

		for (List<Object> rowGrp : rowGrps) {
			String key = getRowKey(rowGrp);
			Row row = rows.get(key);
			if (row == null) {
				row = new Row(key, rowGrp);
				rows.put(key, row);
			}
			
			row.addColValue(colKeyVal, measureMap);					
		}
		
		dynamicCols.add(colKeyVal);
	}
	
	private String getRowKey(List<Object> rowKeyVals) {
		StringBuilder key = new StringBuilder("-##-");
		
		for (Object val : rowKeyVals) {
			key.append(val == null ? "" : val.toString()).append("-##-");
		}
		
		return key.toString();
	}
	
	private class Row {
		private String key;
		
		private List<Object> rowKeyValues;
		
		private Map<Object, Map<String, Object>> colKeyValueMap = new TreeMap<Object, Map<String, Object>>();
		
		public Row(String key, List<Object> rowKeyValues) {
			this.key = key;
			this.rowKeyValues = rowKeyValues;
		}

		public List<Object> getRowKeyValues() {
			return rowKeyValues;
		}

		public void addColValue(Object columnKey, Map<String, Object> measureMap) {
			Map<String, Object> existingMeasures = colKeyValueMap.get(columnKey);
			if (existingMeasures == null || !rollup) {
				colKeyValueMap.put(columnKey, new LinkedHashMap<String, Object>(measureMap));
			} else {
				for (Map.Entry<String, Object> measure : measureMap.entrySet()) {
					BigDecimal existingMeasure = getBigDecimal(existingMeasures.get(measure.getKey()));
					BigDecimal val = getBigDecimal(measure.getValue());
					existingMeasures.put(measure.getKey(), existingMeasure.add(val));					
				}
			}
		}
		
		public Map<String, Object> getColValue(Object columnKey) {
			Map<String, Object> measureMap = colKeyValueMap.get(columnKey);
			if (measureMap == null && numericMeasure) {
				measureMap = new LinkedHashMap<String, Object>();
				for (String measureCol : measureCols) {
					measureMap.put(measureCol, BigDecimal.ZERO);
				}
			}
			
			return measureMap;
		}
	}
	
	private List<List<Object>> rollup(List<Object> input) {
		List<List<Object>> result = new ArrayList<List<Object>>();
		result.add(input);
		
		for (int i = 1; i <= input.size(); ++i) {
			List<Object> elem = new ArrayList<Object>(input.subList(0, input.size() - i));
			for (int j = input.size() - i; j < input.size(); ++j) {
				elem.add(null);
			}
			result.add(elem);
		}
		
		return result;		
	}
	
	private List<List<Object>> powerset(List<Object> input) {
		List<List<Object>> result = new ArrayList<List<Object>>();
		if (input.isEmpty()) {
			result.add(new ArrayList<Object>());
			return result;
		}
		
		Object first = input.get(0);
		List<Object> rest = new ArrayList<Object>(input.subList(1, input.size()));
		for (List<Object> subset : powerset(rest)) {
			List<Object> newList = new ArrayList<Object>();
			newList.add(first);
			newList.addAll(subset);			
			result.add(newList);
			
			subset.add(0, null);
			result.add(subset);
		}
		
		return result;
	}
	
	private boolean isDateField(ExpressionNode expr) {
		if (expr instanceof FieldNode) {
			FieldNode f = (FieldNode)expr;
			return f.getCtrl() instanceof DatePicker;
		}
		
		return false;
	}
	
	private String getDateFormat(ExpressionNode expr) {
		if (expr instanceof FieldNode) {
			FieldNode f = (FieldNode)expr;
			if (f.getCtrl() instanceof DatePicker) {
				DatePicker dp = (DatePicker)f.getCtrl();
				return dp.getFormat();
			}
		}
		
		return null;		
	}
	
	private String getMeasureColumnLabel(ExpressionNode node) {
		if (node.getLabel() != null) {
			return node.getLabel();
		} else if (node instanceof FieldNode) {
			String[] captions = ((FieldNode) node).getNodeCaptions();
			return captions[captions.length - 1];
		} else {
			return "Unknown";
		}
	}
	
	private BigDecimal getBigDecimal(Object val) {
		BigDecimal ret = BigDecimal.ZERO;
		if (val != null) {
			ret = new BigDecimal(val.toString());
		}
		
		return ret;
	}
}
