package edu.common.dynamicextensions.ndao;

public class MySqlSettings implements DbSettings {

	private static MySqlSettings instance = null;

	public static DbSettings getInstance() {
		if (instance == null) {
			instance = new MySqlSettings();
		}
		
		return instance;
	}
	
	@Override
	public String getStringColType() {
		return "VARCHAR(4000)";
	}

	@Override
	public String getFloatColType() {
		return "DECIMAL(19, 6)";
	}

	@Override
	public String getIntegerColType() {
		return "BIGINT";
	}

	@Override
	public String getDateColType() {
		return "DATE";
	}

	@Override
	public String getBlob() {
		return "BLOB";
	}

}
