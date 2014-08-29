package edu.common.dynamicextensions.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	private boolean rollup;
	
	private boolean numericMeasure;
			
	public Crosstab(QueryExpressionNode queryExpr) {
		this.queryExpr = queryExpr;
		
		CrosstabNode ctSpec = queryExpr.getCrosstabSpec();

		String rollupType = ctSpec.getRollupType();
		rollup = rollupType != null && !rollupType.trim().isEmpty();
				
		ExpressionNode column = queryExpr
				.getSelectList()
				.getElements()
				.get(ctSpec.getMeasureColumn() - 1);
		numericMeasure = column.getType() == DataType.INTEGER || column.getType() == DataType.FLOAT;
		
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
			BigDecimal sum = BigDecimal.ZERO;
			
			List<Object> values = new ArrayList<Object>();
			values.addAll(row.getRowKeyValues());
			
			for (Object colKey : dynamicCols) {
				Object measure = row.getColValue(colKey);
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
		
		Object measure = rs.getObject(ctSpec.getMeasureColumn());
		
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
			
			row.addColValue(colKeyVal, measure);					
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
		
		private Map<Object, Object> colKeyValueMap = new TreeMap<Object, Object>();
		
		public Row(String key, List<Object> rowKeyValues) {
			this.key = key;
			this.rowKeyValues = rowKeyValues;
		}

		public List<Object> getRowKeyValues() {
			return rowKeyValues;
		}

		public void addColValue(Object columnKey, Object measure) {
			Object existing = colKeyValueMap.get(columnKey);
			if (existing == null || !rollup) {
				colKeyValueMap.put(columnKey, measure);
			} else {
				BigDecimal val = new BigDecimal(existing.toString());
				val = val.add(measure != null ? new BigDecimal(measure.toString()) : BigDecimal.ZERO);
				colKeyValueMap.put(columnKey, val);
			}
		}
		
		public Object getColValue(Object columnKey) {
			Object measure = colKeyValueMap.get(columnKey);
			if (measure == null && numericMeasure) {
				return BigDecimal.ZERO;
			} 
			
			return measure;
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
}
