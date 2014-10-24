package edu.common.dynamicextensions.domain.nui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.domain.nui.ColumnDef;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.LookupControl;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public abstract class AbstractLookupControl extends Control implements LookupControl {
	private static final long serialVersionUID = 1L;
	
	private static final String LU_KEY_COLUMN = "IDENTIFIER";
	
	private static final String LU_VALUE_COLUMN = "NAME";
	
	@Override
	public DataType getDataType() {
		return DataType.INTEGER;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getIntegerColType()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		return new BigDecimal(value).longValue();
	}

	public abstract void getProps(Map<String, Object> props);
	
	public abstract String getTableName();		

	@Override
	public String getParentKey() {
		return getDbColumnName();
	}

	@Override
	public String getLookupKey() {
		return LU_KEY_COLUMN;
	}

	@Override
	public String getValueColumn() {
		return LU_VALUE_COLUMN;
	}

	@Override
	public DataType getValueType() {
		return DataType.STRING;
	}

	@Override
	public abstract Properties getPvSourceProps();
	
	protected void serializeToXml(String field, Writer writer, Properties props) {
		writeElementStart(writer, field);
		super.serializeToXml(writer, props);
		writeElementEnd(writer, field);						
	}	
}