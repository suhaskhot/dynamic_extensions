package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class DatePicker extends Control implements Serializable {
	private static final long serialVersionUID = 6046956576964435896L;

	private static final String DEFAULT_FORMAT = "MM-dd-yyyy";
	
	public static enum DefaultDateType {
		PREDEFINED,
		CURRENT_DATE,
		NONE
	};

	private String format = DEFAULT_FORMAT;
	
	private boolean showCalendar = true;
	
	private Date defaultDate;
	
	private DefaultDateType defaultDateType = DefaultDateType.NONE;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean showCalendar() {
		return showCalendar;
	}

	public void setShowCalendar(boolean showCalendar) {
		this.showCalendar = showCalendar;
	}

	public Date getDefaultDate() {
		return defaultDate;
	}

	public void setDefaultDate(Date defaultDate) {
		this.defaultDate = defaultDate;
	}

	public DefaultDateType getDefaultDateType() {
		return defaultDateType;
	}

	public void setDefaultDateType(DefaultDateType defaultDateType) {
		this.defaultDateType = defaultDateType;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getDateColType()));
	}
	
	
	@Override
	public DataType getDataType() {
		return DataType.DATE;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Date fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		try {			
			String fmt = format;
			if (fmt == null) {
				fmt = DEFAULT_FORMAT;
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
			simpleDateFormat.setLenient(false);
			return simpleDateFormat.parse(value);
		} catch (Exception e) {
			throw new RuntimeException("Error creating date object from [" + value + "]", e);
		}
	}
	
	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		}
		
		try {
			String fmt = format;
			if (fmt == null) {
				fmt = DEFAULT_FORMAT;
			}
			
			return new SimpleDateFormat(fmt).format((Date)value);
		} catch (Exception e) {
			throw new RuntimeException("Error converting date to string: " + value, e);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + (showCalendar ? 1231 : 1237);
		result = prime * result	+ ((defaultDate == null) ? 0 : defaultDate.hashCode());
		result = prime * result	+ ((defaultDateType == null) ? 0 : defaultDateType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		DatePicker other = (DatePicker) obj;
		if (!StringUtils.equals(format, other.format) ||
			showCalendar != other.showCalendar ||
			(defaultDate == null && other.defaultDate != null) ||
			(defaultDate != null && !defaultDate.equals(other.defaultDate)) ||
			defaultDateType != other.defaultDateType) {
			return false;
		}

		return true;
	}	
}
