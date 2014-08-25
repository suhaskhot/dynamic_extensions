package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CrosstabNode implements Node, Serializable {
	private String name;
	
	private List<Integer> rowGroupByColumns = new ArrayList<Integer>();
	
	private int colGroupByColumn = -1;
	
	private int measureColumn = -1;
	
	private String rollupType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getRowGroupByColumns() {
		return rowGroupByColumns;
	}

	public void setRowGroupByColumns(List<Integer> rowGroupByColumns) {
		this.rowGroupByColumns = rowGroupByColumns;
	}

	public int getColGroupByColumn() {
		return colGroupByColumn;
	}

	public void setColGroupByColumn(int colGroupByColumn) {
		this.colGroupByColumn = colGroupByColumn;
	}

	public int getMeasureColumn() {
		return measureColumn;
	}

	public void setMeasureColumn(int measureColumn) {
		this.measureColumn = measureColumn;
	}

	public String getRollupType() {
		return rollupType;
	}

	public void setRollupType(String rollupType) {
		this.rollupType = rollupType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * 1 + colGroupByColumn;
		result = prime * result + measureColumn;
		result = prime * result	+ rowGroupByColumns.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) { 
			return false;
		}

		CrosstabNode other = (CrosstabNode) obj;
		if (colGroupByColumn != other.colGroupByColumn) {
			return false;
		}
		
		if (measureColumn != other.measureColumn) {
			return false;
		}
		
		if (rowGroupByColumns == null) {
			if (other.rowGroupByColumns != null) {
				return false;
			}
		} else if (!rowGroupByColumns.equals(other.rowGroupByColumns)) {
			return false;
		}
		
		return true;
	}
}
