
package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class SubFormControl extends Control implements Serializable {
	private static final long serialVersionUID = 8920374924982826593L;

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
	
	//
	// Used in AQ. Specifies whether this sub-form exists purely to specify path
	//
	private boolean pathLink;

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
		columnDefs.add(ColumnDef.get("RECORD_ID", ColumnTypeHelper.getIntegerColType()));
		columnDefs.add(ColumnDef.get("SUB_FORM_RECORD_ID", ColumnTypeHelper.getIntegerColType()));
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
	
	public boolean isPathLink() {
		return pathLink;
	}

	public void setPathLink(boolean pathLink) {
		this.pathLink = pathLink;
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
		result = prime * result + (pathLink ? 1231 : 1237);
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
			!StringUtils.equals(foreignKeyColumn, other.foreignKeyColumn) || 
			pathLink != other.pathLink) {
			
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
