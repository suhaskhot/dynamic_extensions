
package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class SubFormControl extends Control {
	private static final long serialVersionUID = -3383871023946721209L;

	private Container subContainer;

	private int noOfEntries;

	private boolean showAddMoreLink;

	private boolean pasteButtonEnabled;

	private String tableName;
	
	//
	// Parent key column specifies DB column of parent table used to index 
	// into sub-form table
	//
	private String parentKeyColumn  = "IDENTIFIER";
	
	//
	// Foreign key column specifies DB column referring to parent table
	//
	private String foreignKeyColumn = "PARENT_RECORD_ID";

	public Container getSubContainer() {
		return subContainer;
	}

	public void setSubContainer(Container subContainer) {
		this.subContainer = subContainer;
	}

	public boolean showAddMoreLink() {
		return showAddMoreLink;
	}

	public void setShowAddMoreLink(boolean showAddMoreLink) {
		this.showAddMoreLink = showAddMoreLink;
	}

	public int getNoOfEntries() {
		return noOfEntries;
	}

	public void setNoOfEntries(int noOfEntries) {
		this.noOfEntries = noOfEntries;
	}

	public boolean isPasteButtonEnabled() {
		return pasteButtonEnabled;
	}

	public void setPasteButtonEnabled(boolean pasteButtonEnabled) {
		this.pasteButtonEnabled = pasteButtonEnabled;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();
		columnDefs.add(ColumnDef.get("RECORD_ID", "BIGINT"));
		columnDefs.add(ColumnDef.get("SUB_FORM_RECORD_ID", "BIGINT"));
		return columnDefs;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getParentKey() {
		return parentKeyColumn;
	}

	public void setParentKey(String parentKeyColumn) {
		if (parentKeyColumn == null) {
			parentKeyColumn  = "IDENTIFIER";
		}
		
		this.parentKeyColumn = parentKeyColumn;
	}
	
	public String getForeignKey() {
		return foreignKeyColumn;
	}

	public void setForeignKey(String foreignKeyColumn) {
		if (foreignKeyColumn == null) {
			foreignKeyColumn = "PARENT_RECORD_ID";
		}
		
		this.foreignKeyColumn = foreignKeyColumn;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((subContainer == null) ? 0 : subContainer.hashCode());
		result = prime * result + noOfEntries;
		result = prime * result + (showAddMoreLink ? 1231 : 1237);
		result = prime * result + (pasteButtonEnabled ? 1231 : 1237);
		result = prime * result	+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result	+ ((parentKeyColumn == null) ? 0 : parentKeyColumn.hashCode());
		result = prime * result	+ ((foreignKeyColumn == null) ? 0 : foreignKeyColumn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		SubFormControl other = (SubFormControl) obj;
		if ((subContainer == null && other.subContainer != null) ||
			!subContainer.equals(other.subContainer) ||
			noOfEntries != other.noOfEntries ||
			showAddMoreLink != other.showAddMoreLink ||
			pasteButtonEnabled != other.pasteButtonEnabled ||
			!StringUtils.equals(tableName, other.tableName) ||
			!StringUtils.equals(parentKeyColumn, other.parentKeyColumn) ||
			!StringUtils.equals(foreignKeyColumn, other.foreignKeyColumn)) {
			
			return false;
		}
			
		return true;
	}
	
	@Override
	public DataType getDataType() {
		return null;
	}
	
	@Override
	public <T> T fromString(String value) {
		throw new UnsupportedOperationException();
	}
}
