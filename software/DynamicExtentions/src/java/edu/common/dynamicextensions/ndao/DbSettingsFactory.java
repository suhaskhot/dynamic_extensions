package edu.common.dynamicextensions.ndao;

public class DbSettingsFactory {

	private static String product;
	
	public static DbSettings instance;
	
	public static void init (String dbType) {
		product = dbType;
	}

	public static DbSettings getDbSettings() {
		if (product.equals("Oracle")) {
			instance = OracleSettings.getInstance();
		} else if (product.equals("MySQL")) {
			instance = MySqlSettings.getInstance();
		}
		return instance;
	}
	
	public static String getProduct() {
		return product;
	}
	
}
