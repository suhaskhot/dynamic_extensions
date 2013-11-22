package edu.common.dynamicextensions.ndao;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionManager {
	private static TransactionManager instance = null;
	
	private PlatformTransactionManager txnMgr = null;
	
	public class Transaction {
		private TransactionStatus txnStatus;
		
		public Transaction(TransactionStatus txnStatus) {
			this.txnStatus = txnStatus;
		}
	}
		
	private TransactionManager(DataSource ds) {
		txnMgr = new DataSourceTransactionManager(ds);
	}
	
	public static synchronized TransactionManager getInstance(DataSource ds) {
		if (ds != null && instance == null) {
			instance = new TransactionManager(ds);
		}
		
		return instance;
	}
	
	public static TransactionManager getInstance() {
		return instance;
	}
	
	public Transaction startTxn() {
		DefaultTransactionDefinition txnDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);		
		return new Transaction(txnMgr.getTransaction(txnDef));	
	}
	
	public Transaction newTxn() {
		DefaultTransactionDefinition txnDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return new Transaction(txnMgr.getTransaction(txnDef));
	}
	
	public void commit(Transaction txn) {
		txnMgr.commit(txn.txnStatus);
	}
	
	public void rollback(Transaction txn) {
		txnMgr.rollback(txn.txnStatus);
	}	
}
