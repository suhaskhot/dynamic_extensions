package edu.common.dynamicextensions.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.ast.SelectListNode;

public class Query {
    private static final Logger logger = Logger.getLogger(Query.class);
    
    private JoinTree queryJoinTree;

    private QueryExpressionNode queryExpr;

    private boolean wideRows;
    
    private boolean ic;
    
    private String dateFormat = "MM-dd-yyyy";
    
    private boolean vcEnabled;
        
    public static Query createQuery() {
        return new Query();
    }
    
    private Query() {
    }
            
    public Query wideRows(boolean wideRows) {
        this.wideRows = wideRows;
        return this;
    }
    
    public Query ic(boolean ic) {
    	this.ic = ic;
    	return this;
    }
    
    public Query dateFormat(String dateFormat) {
    	this.dateFormat = dateFormat;
    	return this;
    }
    
    public Query enableVersionedForms(boolean vcEnabled) {
    	this.vcEnabled = vcEnabled;
    	return this;
    }
  
    public void compile(String rootFormName, String query) {
        compile(rootFormName, query, null);
    }
    
    public void compile(String rootFormName, String query, String restriction) {
        QueryCompiler compiler = new QueryCompiler(rootFormName, query, restriction);
        compiler.enabledVersionedForms(vcEnabled).compile();
        queryExpr     = compiler.getQueryExpr();
        queryJoinTree = compiler.getQueryJoinTree();        
    }

    public long getCount() {
        QueryGenerator gen = new QueryGenerator(wideRows, ic, dateFormat);
        String countSql = gen.getCountSql(queryExpr, queryJoinTree);

        long t1 = System.currentTimeMillis();            
        long count = JdbcDaoFactory.getJdbcDao().getResultSet(countSql, null, new ResultExtractor<Long>() {
        	@Override
        	public Long extract(ResultSet rs) throws SQLException {
        		return rs.next() ? rs.getLong(1) : -1L;
        	}
        });
        
        long t2 = System.currentTimeMillis();
        logger.info("Count SQL: " + countSql + "; Query Exec Time: " + (t2 - t1));            
        return count;
    }

    public QueryResponse getData() {
        return getData(0, 0);
    }

    public QueryResponse getData(int start, int numRows) {
        final String dataSql = getDataSql(wideRows, start, numRows);        
        final long t1 = System.currentTimeMillis();        
        return JdbcDaoFactory.getJdbcDao().getResultSet(dataSql, null, new ResultExtractor<QueryResponse>() {
        	@Override
        	public QueryResponse extract(ResultSet rs)
        	throws SQLException {
        		long t2 = System.currentTimeMillis();        		
        		QueryResultData resultData = wideRows ? getWideRowData(rs) : getQueryResultData(rs);
        		long t3 = System.currentTimeMillis();
        		
        		logger.info("Data SQL: " + dataSql + "; Query Exec Time: " + (t2 - t1) + "; Result Prep Time: " + (t3 - t2));
        		
        		QueryResponse resp = new QueryResponse();
        		resp.setSql(dataSql);
        		resp.setResultData(resultData);
        		resp.setExecutionTime(t2 - t1);
        		resp.setPostExecutionTime(t3 - t2);
        		
        		Calendar cal = Calendar.getInstance();
        		cal.setTimeInMillis(t1);
        		resp.setTimeOfExecution(cal.getTime());
        		return resp;
        	}
        });
    }
            
    public String getDataSql() {
        return getDataSql(false, 0, 0);
    }
    
    private String getDataSql(boolean wideRows, int start, int numRows) {
        QueryGenerator gen = new QueryGenerator(wideRows, ic, dateFormat);
        return gen.getDataSql(queryExpr, queryJoinTree, start, numRows);        
    }

    private QueryResultData getQueryResultData(ResultSet rs)
    throws SQLException {
        QueryResultData queryResult = new QueryResultData(getResultColumns(queryExpr), dateFormat);
        queryResult.dataSource(rs);
        return queryResult;
    }
        
    private QueryResultData getWideRowData(ResultSet rs) {
        ShallowWideRowGenerator wideRowGenerator = new ShallowWideRowGenerator(queryJoinTree, queryExpr);
        wideRowGenerator.start();
        wideRowGenerator.processResultSet(rs);
        wideRowGenerator.end();
        
        QueryResultData qrd = new QueryResultData(wideRowGenerator.getResultColumns(), dateFormat);
        qrd.dataSource(wideRowGenerator);
        return qrd;
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
