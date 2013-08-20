package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import edu.common.dynamicextensions.ndao.JdbcDao;

public class Query {
    private JoinTree queryJoinTree;

    private QueryExpr queryExpr;
		
    public static Query createQuery() {
        return new Query();
    }
    
    private Query() {
    }
  
    public void compile(Long rootFormId, String query) {
        QueryCompiler compiler = new QueryCompiler(rootFormId, query);
        compiler.compile();
        queryExpr     = compiler.getQueryExpr();
        queryJoinTree = compiler.getQueryJoinTree();
    }

    public long getCount() {
        QueryGenerator gen = new QueryGenerator();
        String countSql = gen.getCountSql(queryExpr, queryJoinTree);

        long count = -1L;
        JdbcDao jdbcDao = null;
        ResultSet rs = null;
        
        try {        	
            jdbcDao = new JdbcDao();
            rs = jdbcDao.getResultSet(countSql, null);
            if (rs.next()) {
                count = rs.getLong(1);
            }  
            
            return count;
        } catch(Exception e) {
            throw new RuntimeException("Error executing count query: " + countSql, e);
        } finally {
            if (jdbcDao != null) {
            	jdbcDao.close(rs);
            	jdbcDao.close();
            }
        }
    }

    public QueryResultData getData() {
        return getData(0, 0);
    }

    public QueryResultData getData(int start, int numRows) {
        QueryGenerator gen = new QueryGenerator();
        String dataSql = gen.getDataSql(queryExpr, queryJoinTree, 0, 0);
        
        JdbcDao jdbcDao = null;
        ResultSet rs = null;
        QueryResultData queryResultdata = null;
        
        try {
            jdbcDao = new JdbcDao();
            rs = jdbcDao.getResultSet(dataSql, null);
            queryResultdata = getQueryResultData(rs);
            return queryResultdata;
        } catch(Exception e) {
            throw new RuntimeException("Error executing data query: " + dataSql, e);
        } finally {
        	if (jdbcDao != null) {
        		jdbcDao.close(rs);
        		jdbcDao.close();
        	}
        }
    }
    
    public String getDataSql() {
        QueryGenerator gen = new QueryGenerator();
        return gen.getDataSql(queryExpr, queryJoinTree, 0, 0);    	
    }

    private QueryResultData getQueryResultData(ResultSet rs)
    throws Exception {
        String columnNames[] = getColumnNames(rs);
        int columnCount = columnNames.length;
        
        QueryResultData queryResult = new QueryResultData(columnNames);
        while (rs.next()) {
        	Object[] row = new Object[columnCount];
        	for (int i = 0; i < columnCount; ++i) {
        		row[i] = rs.getObject(i + 1);
        	}
        	
        	queryResult.addRow(row);
        }

        return queryResult;
    }

    private String[] getColumnNames(ResultSet rs)
    throws Exception {
        ResultSetMetaData rsmd = rs.getMetaData();
        String columns[] = new String[rsmd.getColumnCount()];
        
        for (int i = 0; i < columns.length; i++) {
            columns[i] = rsmd.getColumnLabel(i + 1);
        }

        return columns;
    }
}