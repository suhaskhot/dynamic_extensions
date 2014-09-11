/**
 * 
 */

package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class FileUploadControl extends Control implements Serializable {
	private static final long serialVersionUID = -7611119092154790813L;

	@Override
	public List<ColumnDef> getColumnDefs() {
		List<ColumnDef> columns = new ArrayList<ColumnDef>();
		columns.add(ColumnDef.get(getDbColumnName() + "_NAME", ColumnTypeHelper.getStringColType()));
		columns.add(ColumnDef.get(getDbColumnName() + "_TYPE", ColumnTypeHelper.getStringColType()));
		columns.add(ColumnDef.get(getDbColumnName() + "_ID", ColumnTypeHelper.getStringColType()));
		return columns;
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}

	@Override
	public DataType getDataType() {
		return null;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("type", "fileUpload");		
	}	
}
