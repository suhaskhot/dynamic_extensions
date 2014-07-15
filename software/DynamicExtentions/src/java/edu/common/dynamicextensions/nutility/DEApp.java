package edu.common.dynamicextensions.nutility;

import java.sql.SQLException;

import javax.sql.DataSource;

import edu.common.dynamicextensions.ndao.DbSettingsFactory;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.TransactionManager;

public class DEApp {
	private static String fileUploadDir;

	private static String dateFormat;

	public static void init(DataSource ds, String fileUploadDir, String dateFormat) {
		JdbcDaoFactory.setDataSource(ds);
        TransactionManager.getInstance(ds);
        try {
			String product = ds.getConnection().getMetaData().getDatabaseProductName();
	        DbSettingsFactory.init(product);
		} catch (SQLException e) {
			throw new RuntimeException("Error while retrieving the Db type from Datasource " + e);
		}

		DEApp.fileUploadDir = fileUploadDir;
		DEApp.dateFormat = dateFormat != null ? dateFormat : "MM/dd/yyyy";
	}

	public static String getFileUploadDir() {
		return fileUploadDir;
	}

	public static String getDateFormat() {
		return dateFormat;
	}
}
