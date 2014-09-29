package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;

public class PvDataSource implements Serializable {
	private static final long serialVersionUID = 276983397161935250L;

	public static enum Ordering {
		NONE, ASC, DESC
	}
	
	private List<PvVersion> pvVersions = new ArrayList<PvVersion>();
	
	private DataType dataType;
	
	private String dateFormat;
	
	private Ordering ordering = Ordering.NONE;
	
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
			pvs = getPvsFromDb(sql);
		} else {
			pvs = getPvVersion(activationDate).getPermissibleValues();
		}
		
		switch (ordering) {
			case ASC:
				Collections.sort(pvs);
				break;
				
			case DESC:
				Collections.sort(pvs, Collections.reverseOrder());
				break;				
				
			default:
				// do nothing
				break;
		}
		
		return pvs;
	}
	
	public PermissibleValue getDefaultValue(Date activationDate) {
		if (sql == null ) {
			return getPvVersion(activationDate).getDefaultValue();
		}
		
		return null;
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
	
	private List<PermissibleValue> getPvsFromDb(String sql) {
		return JdbcDaoFactory.getJdbcDao().getResultSet(sql, null, new ResultExtractor<List<PermissibleValue>>() {
			@Override
			public List<PermissibleValue> extract(ResultSet rs)
			throws SQLException {
				List<PermissibleValue> result = new ArrayList<PermissibleValue>();
					
				while (rs.next()) {
					String value = rs.getString(1);
					if (value == null || value.trim().isEmpty()) {
						continue;
					}
						
					PermissibleValue pv = new PermissibleValue();
					pv.setOptionName(value);
					pv.setValue(value);
					result.add(pv);
				}
					
				return result;
			}
		});
	}
}
