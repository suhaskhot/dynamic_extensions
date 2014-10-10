package edu.common.dynamicextensions.domain.nui;

import java.util.Properties;

public interface LookupControl {	
	public String getTableName();
	
	public String getParentKey();
	
	public String getLookupKey();

	public String getValueColumn();
	
	public DataType getValueType();

	public Properties getPvSourceProps();
}
