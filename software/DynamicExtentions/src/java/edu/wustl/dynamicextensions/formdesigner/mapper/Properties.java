
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.DataType;

public class Properties {

	private Map<String, Object> properties;

	public Properties() {
		properties = new HashMap<String, Object>();
	}

	public Properties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void setProperty(String name, Object value) {
		this.properties.put(name, value);
	}

	public String getString(String key) {
		if (this.properties.containsKey(key) && this.properties.get(key) != null) {
			return this.properties.get(key).toString();
		} else {
			return null;
		}
	}

	public Integer getInteger(String key) {
		Integer integer = null;
		if (this.properties.containsKey(key)) {
			Object number = this.properties.get(key);
			if (number != null) {
				if (isNumber(number.toString())) {
					integer = Integer.parseInt(number.toString().trim());
				}
			}
		}
		return integer;
	}

	public Long getLong(String key) {
		Long _long = null;
		if (this.properties.containsKey(key)) {
			Object number = this.properties.get(key);
			if (number != null) {
				if (isNumber(number.toString())) {
					_long = Long.parseLong(number.toString().trim());
				}
			}
		}
		return _long;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String key) {
		if (this.properties.containsKey(key) && this.properties.get(key) != null) {
			return (Map<String, Object>) this.properties.get(key);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Map<String, Object>> getListOfMap(String key) {
		if (this.properties.containsKey(key) && this.properties.get(key) != null) {
			return (Collection<Map<String, Object>>) this.properties.get(key);
		} else {
			return null;
		}
	}

	public Map<String, Object> getAllProperties() {
		return this.properties;
	}

	public Boolean getBoolean(String key) {
		if (this.properties.containsKey(key) && this.properties.get(key) != null) {
			return (Boolean) this.properties.get(key);
		} else {
			return false;
		}
	}

	public Object get(String key) {
		if (this.properties.containsKey(key) && this.properties.get(key) != null) {
			return this.properties.get(key);
		} else {
			return null;
		}
	}

	public DataType getDataType(String key) {
		if (this.properties.containsKey(key) && this.properties.get(key) != null) {
			if (((String) this.properties.get(key)).equalsIgnoreCase("STRING")) {
				return DataType.STRING;
			} else if (((String) this.properties.get(key)).equalsIgnoreCase("INTEGER")) {
				return DataType.INTEGER;
			} else if (((String) this.properties.get(key)).equalsIgnoreCase("FLOAT")) {
				return DataType.FLOAT;
			} else if (((String) this.properties.get(key)).equalsIgnoreCase("DATE")) {
				return DataType.DATE;
			} else if (((String) this.properties.get(key)).equalsIgnoreCase("BOOLEAN")) {
				return DataType.BOOLEAN;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private boolean isNumber(String number) {
		return number.trim().matches("[0-9]+");
	}
}
