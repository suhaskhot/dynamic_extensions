package edu.common.dynamicextensions.query;

import java.sql.ResultSet;

import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.ast.SelectListNode;

public class Query {
    private JoinTree queryJoinTree;

    private QueryExpressionNode queryExpr;
    
    private String separator = ": ";
    
    private boolean wideRows;
		
    public static Query createQuery() {
        return new Query();
    }
    
    private Query() {
    }
    
    public Query columnHeadingSeparator(String separator) {
    	this.separator = separator;
    	return this;
    }
    
    public Query wideRows(boolean wideRows) {
    	this.wideRows = wideRows;
    	return this;
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
        String dataSql = getDataSql(wideRows, start, numRows);

        JdbcDao jdbcDao = null;
        ResultSet rs = null;        
        try {
            jdbcDao = new JdbcDao();            
            rs = jdbcDao.getResultSet(dataSql, null);
            
            QueryResultData resultData = null;
            if (wideRows) {
            	resultData = getWideRowData(rs);
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
        String columnNames[] = getColumnNames(queryExpr);
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
    
    private QueryResultData getWideRowData(ResultSet rs) {
   		WideRowGenerator wideRowGenerator = new WideRowGenerator(queryJoinTree, queryExpr);
   		wideRowGenerator.start();
   		wideRowGenerator.processResultSet(rs);
   		wideRowGenerator.end();
   		return wideRowGenerator.getQueryResultData();
    }
       
    private String[] getColumnNames(QueryExpressionNode queryExpr) {
    	SelectListNode selectList = queryExpr.getSelectList();
    	String columns[] = new String[selectList.getElements().size()];
    	
    	int i = 0;
    	for (ExpressionNode node : selectList.getElements()) {
    		if (node instanceof FieldNode) {
    			columns[i] = getColumnHeading((FieldNode)node);    			
    		} else {
    			columns[i] = "Column " + i; // TODO: for types of expression node
    		}
    		
    		++i;
    	}
    	
    	return columns;
    }
    
    private String getColumnHeading(FieldNode node) {
    	return getColumnHeading(node, separator);
    }
    
    public static String getColumnHeading(FieldNode node, String separator) {
		String[] nodeCaptions = node.getNodeCaptions();
		
		StringBuilder heading = new StringBuilder(nodeCaptions[0]);
		for (int j = 1; j < nodeCaptions.length; ++j) {
			heading.append(separator).append(nodeCaptions[j]);
		}
		
		return heading.toString();    	
    }    
}