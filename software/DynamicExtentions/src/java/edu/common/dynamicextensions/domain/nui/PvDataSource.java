package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
