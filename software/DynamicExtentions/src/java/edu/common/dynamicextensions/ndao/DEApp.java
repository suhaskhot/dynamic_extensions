package edu.common.dynamicextensions.ndao;

import java.sql.SQLException;

import javax.sql.DataSource;

public class DEApp {
	
	public static void init(DataSource ds) {
		JdbcDaoFactory.setDataSource(ds);
        TransactionManager.getInstance(ds);
        try {
			String product = ds.getConnection().getMetaData().getDatabaseProductName();
	        DbSettingsFactory.init(product);
		} catch (SQLException e) {
			throw new RuntimeException("Error while retrieving the Db type from Datasource " + e);
		}
	}
}
