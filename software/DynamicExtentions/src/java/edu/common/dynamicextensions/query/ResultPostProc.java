package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.List;

public interface ResultPostProc {
	public int processResultSet(ResultSet rs);
	
	public List<ResultColumn> getResultColumns();
	
	public List<Object[]> getRows();
	
	public void cleanup();
}
