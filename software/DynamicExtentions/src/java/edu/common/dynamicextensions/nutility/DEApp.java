package edu.common.dynamicextensions.nutility;

import java.sql.SQLException;

import javax.sql.DataSource;

import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.TransactionManager;

public class DEApp {
	private static String fileUploadDir;
	
	public static void init(DataSource ds, String fileUploadDir) {
		JdbcDaoFactory.setDataSource(ds);
        TransactionManager.getInstance(ds);
        try {
			String product = ds.getConnection().getMetaData().getDatabaseProductName();
	        DbSettingsFactory.init(product);
		} catch (SQLException e) {
			throw new RuntimeException("Error while retrieving the Db type from Datasource " + e);
		}
        
        DEApp.fileUploadDir = fileUploadDir;
	}
	
	public static String getFileUploadDir() {
		return fileUploadDir;
	}
}
