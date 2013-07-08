package edu.common.dynamicextensions.ndao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.transaction.Transaction;

import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.common.dynamicextensions.entitymanager.DynamicExtensionBaseQueryBuilder;
import edu.common.dynamicextensions.entitymanager.QueryBuilderFactory;
import edu.common.dynamicextensions.entitymanager.persister.IdGenerator;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;

public class JdbcDao {
	private JDBCDAO dao;
	
	private Transaction suspendedTxn = null;
	
	private static final String appName = DynamicExtensionDAO.getInstance().getAppName();
	
	public JdbcDao() {
		try {
			dao = DynamicExtensionsUtility.getJDBCDAO(null);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining JDBC DAO", e);
		}		
	}
	
	public JdbcDao(JDBCDAO dao) {
		this.dao = dao;
	}
	
	public ResultSet getResultSet(String query, List<?> params) {
		try {
			List<ColumnValueBean> paramList = getParamList(params);
			return dao.getResultSet(query, paramList, null);
		} catch (Exception e) {
			throw new RuntimeException("Error executing the query: " + query, e);
		}		
	}
	
	public void executeUpdate(String updateSql, List<?> params) {
		try {
			LinkedList<LinkedList<ColumnValueBean>> paramList = new LinkedList<LinkedList<ColumnValueBean>>();
			paramList.add(getParamList(params));
			dao.executeUpdate(updateSql, paramList);
		} catch (Exception e) {
			throw new RuntimeException("Error executing the update dml: " + updateSql);
		}		
	}
	
	public void close() {
		if (dao != null) {
			try {
				dao.closeSession();
			} catch (Exception e) {
				
			}			
		}
	}
	
	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	public void suspendTxn() {
		if (suspendedTxn == null) {
			try {
				suspendedTxn = DAOUtility.getInstance().suspendTransaction();
			} catch (Exception e) {
				throw new RuntimeException("Error suspending transaction", e);
			}			
		}
	}
	
	public void resumeTxn() {
		if (suspendedTxn != null) {
			try {
				DAOUtility.getInstance().resumeTransaction(suspendedTxn);
				suspendedTxn = null;
			} catch (Exception e) {
				throw new RuntimeException("Error resuming transction", e);
			}			
		}
	}
	
	public void executeDDL(String ddl) {
		try {
			DynamicExtensionBaseQueryBuilder queryBuilder = QueryBuilderFactory.getQueryBuilder();
			List<String> queryList = new ArrayList<String>();
			queryList.add(ddl.toString());
			List<String> revQueryList = new ArrayList<String>();

			Stack<String> rlbkQryStack =  new Stack<String>();
			queryBuilder.executeQueries(queryList, revQueryList, rlbkQryStack);
		} catch (DynamicExtensionsSystemException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing DDL query :: "+ddl.toString());
		} 
//		DAOUtility.getInstance().executeDDL(appName, ddl);
	}
	
	public Long getNextId(String tableName) {
		return IdGenerator.getInstance().getNextId(tableName);
	}
	
		
	private LinkedList<ColumnValueBean> getParamList(List<?> params) {
		LinkedList<ColumnValueBean> paramList = new LinkedList<ColumnValueBean>();
		if (params != null) {
			for (Object param : params) {
				paramList.add(new ColumnValueBean(param));
			}
		}
		
		return paramList;		
	}
}
