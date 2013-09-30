package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.ast.SelectListNode;

public class Query {
    private JoinTree queryJoinTree;

    private QueryExpressionNode queryExpr;
        
    private boolean wideRows;
		
    public static Query createQuery() {
        return new Query();
    }
    
    private Query() {
    }
        
    public Query wideRows(boolean wideRows) {
    	this.wideRows = wideRows;
    	return this;
    }
  
    public void compile(Long rootFormId, String query) {
    	compile(rootFormId, query, null);
    }
    
    public void compile(Long rootFormId, String query, String restriction) {
        QueryCompiler compiler = new QueryCompiler(rootFormId, query, restriction);
        compiler.compile();
        queryExpr     = compiler.getQueryExpr();
        queryJoinTree = compiler.getQueryJoinTree();    	
    }

    public long getCount() {
        QueryGenerator gen = new QueryGenerator(wideRows);
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
        String dataSql = getDataSql(wideRows, start, numRows);

        JdbcDao jdbcDao = null;
        ResultSet rs = null;        
        try {
            jdbcDao = new JdbcDao();            
            rs = jdbcDao.getResultSet(dataSql, null);
            
            QueryResultData resultData = null;
            if (wideRows) {
            	resultData = getWideRowData(rs);
            	if (resultData == null) {
            		// this will ensure at least the header columns are populated
            		resultData = new QueryResultData(getResultColumns(queryExpr));
            	}
            } else {
            	resultData = getQueryResultData(rs);
            }
            
            return resultData;            
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
    	return getDataSql(false, 0, 0);
    }
    
    private String getDataSql(boolean wideRows, int start, int numRows) {
        QueryGenerator gen = new QueryGenerator(wideRows);
        return gen.getDataSql(queryExpr, queryJoinTree, start, numRows);    	
    }

    private QueryResultData getQueryResultData(ResultSet rs)
    throws Exception {
        List<ResultColumn> columns = getResultColumns(queryExpr);
        int columnCount = columns.size();
        
        QueryResultData queryResult = new QueryResultData(columns);
        while (rs.next()) {
        	Object[] row = new Object[columnCount];
        	for (int i = 0; i < columnCount; ++i) {
        		row[i] = rs.getObject(i + 1);
        	}
        	
        	queryResult.addRow(row);
        }

        return queryResult;
    }
    
    private QueryResultData getWideRowData(ResultSet rs) {
   		WideRowGenerator wideRowGenerator = new WideRowGenerator(queryJoinTree, queryExpr);
   		wideRowGenerator.start();
   		wideRowGenerator.processResultSet(rs);
   		wideRowGenerator.end();
   		return wideRowGenerator.getQueryResultData();
    }
           
    private List<ResultColumn> getResultColumns(QueryExpressionNode queryExpr) {
    	SelectListNode selectList = queryExpr.getSelectList();
    	List<ResultColumn> columns = new ArrayList<ResultColumn>();
    	
    	for (ExpressionNode node : selectList.getElements()) {
    		columns.add(new ResultColumn(node, 0));
    	}
    	
    	return columns;    	
    }    
}