package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PvDataSource {
	public static enum Ordering {
		ASC, DESC
	}
	
	private List<PvVersion> pvVersions = new ArrayList<PvVersion>();
	
	private DataType dataType;
	
	private String dateFormat;
	
	private Ordering ordering = Ordering.ASC;
	
	private String sql;

	public List<PvVersion> getPvVersions() {
		return pvVersions;
	}

	public void setPvVersions(List<PvVersion> pvVersions) {
		this.pvVersions = pvVersions;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Ordering getOrdering() {
		return ordering;
	}

	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String pvSql) {
		this.sql = pvSql;
	}  
	
	public List<PermissibleValue> getPermissibleValues(Date activationDate) {
		List<PermissibleValue> pvs = null;
		
		if (sql != null) {
			//
			// TODO:
			// execute sql
			// return pv list
			//
		} else {
			pvs = getPvVersion(activationDate).getPermissibleValues();
		}
		
		return pvs;
	}
	
	public PermissibleValue getDefaultValue(Date activationDate) {
		return getPvVersion(activationDate).getDefaultValue();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result	+ ((pvVersions == null) ? 0 : pvVersions.hashCode());
		result = prime * result	+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((dateFormat == null) ? 0 : dateFormat.hashCode());
		result = prime * result	+ ((ordering == null) ? 0 : ordering.hashCode());		
		result = prime * result + ((sql == null) ? 0 : sql.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {		
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
						
		PvDataSource other = (PvDataSource) obj;
		if ((pvVersions == null && other.pvVersions != null) ||
			!pvVersions.equals(other.pvVersions) ||
			dataType != other.dataType ||
			!StringUtils.equals(dateFormat, other.dateFormat) ||
			ordering != other.ordering ||
			!StringUtils.equals(sql, other.sql)) {
			return false;
		} 
		
		return true;
	}

	private PvVersion getPvVersion(Date activationDate) {
		PvVersion result = null;

		for (PvVersion pvVersion : pvVersions) {
			Date versionDate = pvVersion.getActivationDate();
			if (result == null) {
				result = pvVersion;
			} else if (result.getActivationDate() == null || versionDate == null ||
				result.getActivationDate().before(versionDate) && versionDate.before(activationDate)) {
				result = pvVersion;
			}
		}
				
		return result;		
	}	
}
