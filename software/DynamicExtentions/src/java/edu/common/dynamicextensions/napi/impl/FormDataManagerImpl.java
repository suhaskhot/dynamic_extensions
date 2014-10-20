package edu.common.dynamicextensions.napi.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormAuditManager;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.nutility.DEApp;
import edu.common.dynamicextensions.nutility.IoUtil;

public class FormDataManagerImpl implements FormDataManager {
	private static final String GET_MULTI_SELECT_VALUES_SQL = "SELECT %s FROM %s WHERE RECORD_ID = ?";
	
	private static final String DELETE_MULTI_SELECT_VALUES_SQL = "DELETE FROM %s WHERE RECORD_ID = ?";
	
	private static final String INSERT_MULTI_SELECT_VALUES_SQL = "INSERT INTO %s (RECORD_ID, VALUE) VALUES (?, ?)";
	
	private static final String GET_SUB_FORM_IDS_SQL = "SELECT IDENTIFIER FROM %s WHERE PARENT_RECORD_ID = ?";

	private static final String RECORD_ID_SEQ = "RECORD_ID_SEQ";
	
	private static final String GET_FILE_CONTROL_VALUES = "SELECT %s, %s, %s from %s where IDENTIFIER = ?";

	private boolean auditEnable = true;

	public FormDataManagerImpl(boolean auditEnable) {
		this.auditEnable = auditEnable;
	}
	
	public FormDataManagerImpl() { 
	}
		
	@Override
	public FormData getFormData(Long containerId, Long recordId) {		
		try {
			FormData result = null;
			Container container = Container.getContainer(containerId);
			
			if (container != null) {
				List<FormData> formsData = getFormData(JdbcDaoFactory.getJdbcDao(), container, "IDENTIFIER", recordId);
				if (formsData != null && !formsData.isEmpty()) {
					result = formsData.get(0);
				}
			}
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining form data: [" + containerId + ", " + recordId  + "]", e);
		}
	}
	
	@Override
	public FormData getFormData(Container container, Long recordId) {		
		try {
			FormData result = null;			
			List<FormData> formData = getFormData(JdbcDaoFactory.getJdbcDao(), container, "IDENTIFIER", recordId);
			if (formData != null && !formData.isEmpty()) {
				result = formData.get(0);
			}
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining form data: [" + container.getId() + ", " + recordId  + "]", e);
		}	
	}

	@Override
	public Long saveOrUpdateFormData(UserContext userCtxt, FormData formData) {
		try {
			return saveOrUpdateFormData(userCtxt, formData, JdbcDaoFactory.getJdbcDao());
		} catch (Exception e) {
			throw new RuntimeException("Error saving form data", e);
		} 
	}

	@Override
	public Long saveOrUpdateFormData(UserContext userCtxt, FormData formData, JdbcDao jdbcDao) {
		try {
			String operation = formData.getRecordId() == null ? "INSERT" : "UPDATE";
			Long recordId = saveOrUpdateFormData(jdbcDao, formData, null);
			formData.setRecordId(recordId);
			
			if(auditEnable) {
				FormAuditManager auditManager = new FormAuditManagerImpl();
				auditManager.audit(userCtxt, formData, operation, jdbcDao);
			}
			
			return recordId;
		} catch (Exception e) {
			throw new RuntimeException("Error saving form data", e);
		}
	}
	
	@Override
	public void deleteFormData(UserContext userCtxt, Long containerId, Long recordId) {
		// TODO Auto-generated method stub

	}
	
	private List<FormData> getFormData(final JdbcDao jdbcDao, final Container container, String identifyingColumn, Long identifier) 
	throws Exception {
		final List<Control> simpleCtrls = new ArrayList<Control>();
		final List<Control> multiSelectCtrls = new ArrayList<Control>();
		final List<Control> subFormCtrls = new ArrayList<Control>();
		
		segregateControls(container, simpleCtrls, multiSelectCtrls, subFormCtrls);
								
		String query = buildQuery(simpleCtrls, container.getDbTableName(), identifyingColumn);
		List<FormData> formsData = jdbcDao.getResultSet(query, Collections.singletonList(identifier), new ResultExtractor<List<FormData>>() {
			@Override
			public List<FormData> extract(ResultSet rs) 
			throws SQLException {
				List<FormData> formsData = new ArrayList<FormData>();
					
				while (rs.next()) {
					Long recordId = rs.getLong("IDENTIFIER");
					FormData formData = new FormData(container);
					formData.setRecordId(recordId);
						
					for (Control ctrl : simpleCtrls) {
						ControlValue ctrlValue = null;

						if (ctrl instanceof FileUploadControl) {
							String filename = rs.getString(ctrl.getDbColumnName() + "_NAME");
							if (filename != null) {
								String type = rs.getString(ctrl.getDbColumnName() + "_TYPE");
								String fileId = rs.getString(ctrl.getDbColumnName() + "_ID");
								ctrlValue = new ControlValue(ctrl, new FileControlValue(filename, type, fileId));
							} else {
								ctrlValue = new ControlValue(ctrl, null);
							}
						} else {
							Object rsObj = null;
							if (ctrl instanceof DatePicker) {
								rsObj = rs.getTimestamp(ctrl.getDbColumnName());
							} else {
								rsObj = rs.getObject(ctrl.getDbColumnName());
							}
							
							String value = ctrl.toString(rsObj);
							ctrlValue = new ControlValue(ctrl, value);
						}
							
						formData.addFieldValue(ctrlValue);
					}
						
					for (Control ctrl : multiSelectCtrls) {
						List<String> msValues = getMultiSelectValues(jdbcDao, ctrl, recordId);
						ControlValue ctrlValue = new ControlValue(ctrl, msValues.toArray(new String[0]));					
						formData.addFieldValue(ctrlValue);					
					}
						
					formsData.add(formData);
				}
					
				return formsData;
			}
		});
		
		for (FormData formData : formsData) {
			for (Control ctrl : subFormCtrls) {
				SubFormControl subFormCtrl = (SubFormControl)ctrl;
				List<FormData> subFormData = getFormData(jdbcDao, subFormCtrl.getSubContainer(), "PARENT_RECORD_ID", formData.getRecordId());				
				formData.addFieldValue(new ControlValue(subFormCtrl, subFormData));
			}
		}
		
		return formsData;		
	}

	private String buildQuery(List<Control> simpleCtrls, String tableName, String identifyingColumn) {
		StringBuilder query = new StringBuilder("SELECT ");
		for (Control ctrl : simpleCtrls) {
			if (ctrl instanceof FileUploadControl) {
				query.append(ctrl.getDbColumnName()).append("_NAME, ")
					.append(ctrl.getDbColumnName()).append("_TYPE, ")
					.append(ctrl.getDbColumnName()).append("_ID, ");
			} else {
				query.append(ctrl.getDbColumnName()).append(", ");
			}
		}
		
		query.append("IDENTIFIER FROM ").append(tableName)
			.append(" WHERE ").append(identifyingColumn).append(" = ?");		
		
		return query.toString();
	}
	
	
	private List<String> getMultiSelectValues(final JdbcDao jdbcDao, final Control ctrl, Long recordId) 
	throws SQLException {		
		MultiSelectControl msCtrl = (MultiSelectControl)ctrl;
		String query = String.format(GET_MULTI_SELECT_VALUES_SQL, ctrl.getDbColumnName(), msCtrl.getTableName());
		return jdbcDao.getResultSet(query, Collections.singletonList(recordId), new ResultExtractor<List<String>>() {
			@Override
			public List<String> extract(ResultSet rs) throws SQLException {
				List<String> result = new ArrayList<String>();
				while (rs.next()) {
					result.add(ctrl.toString(rs.getObject("VALUE")));
				}
				
				return result;
			}				
		});
	}
	
	public FileControlValue getFileControlValue(Long formId, Long recordId, String ctrlName) {
		Container form = Container.getContainer(formId);
		if (form == null) {
			return null;
		}
		
		Control ctrl = form.getControl(ctrlName, "\\.");
		if (!(ctrl instanceof FileUploadControl)) {
			return null;
		}
		
		FileUploadControl fileCtrl = (FileUploadControl)ctrl;
		String dbTable = fileCtrl.getContainer().getDbTableName();
		String column = fileCtrl.getDbColumnName();
		
		String query = String.format(GET_FILE_CONTROL_VALUES, column + "_NAME", column +"_TYPE", column + "_ID", dbTable);
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				query, Collections.singletonList(recordId),
				new ResultExtractor<FileControlValue>() {
					@Override
					public FileControlValue extract(ResultSet rs)
					throws SQLException {
						if (!rs.next()) {
							return null;
						}
						
						FileControlValue fcv = new FileControlValue(rs.getString(1), rs.getString(2), rs.getString(3));
						fcv.setPath(DEApp.getFileUploadDir() + File.separator + fcv.getFileId());
						return fcv;
					}					
				});		
	}

	private Long saveOrUpdateFormData(JdbcDao jdbcDao, FormData formData, Long parentRecId)
	throws Exception {
		List<Control> simpleCtrls = new ArrayList<Control>();
		List<Control> multiSelectCtrls = new ArrayList<Control>();
		List<Control> subFormCtrls = new ArrayList<Control>();
		
		Container container = formData.getContainer();
		Long recordId = formData.getRecordId();
		List<InputStream> inputStreams = new ArrayList<InputStream>();
		
		segregateControls(container, simpleCtrls, multiSelectCtrls, subFormCtrls);
		
		String upsertSql = buildUpsertSql(simpleCtrls, container.getDbTableName(), recordId, parentRecId);
		List<Object> params = new ArrayList<Object>();

		try {
			for (Control ctrl : simpleCtrls) {
				ControlValue ctrlValue = formData.getFieldValue(ctrl.getName());

				if (ctrl instanceof FileUploadControl) {
					FileControlValue fcv = ctrlValue != null ? (FileControlValue) ctrlValue.getValue() : null;
					if (fcv == null) {
						params.add(null);
						params.add(null);
						params.add(null);
					} else {
						params.add(fcv.getFilename());
						params.add(fcv.getContentType());
						params.add(fcv.getFileId());
					}
				} else if (ctrlValue == null || ctrlValue.getValue() == null || ctrlValue.getValue().toString().trim().isEmpty()) {
					params.add(null);
				} else {
					Object value = ctrl.fromString(ctrlValue.getValue().toString());
					params.add(value);
				}
			}

			if (recordId == null && parentRecId != null) {
				params.add(parentRecId);
			}

			if (recordId == null) {
				recordId = jdbcDao.getNextId(RECORD_ID_SEQ);
				formData.setRecordId(recordId);
			}
			params.add(recordId);

			if (!upsertSql.isEmpty()) {
				//
				// This happens when there are no simple controls in main form
				// and during form edit
				//
				jdbcDao.executeUpdate(upsertSql, params);
			} else {
				assert (recordId != null);
			}

			for (Control msCtrl : multiSelectCtrls) {
				ControlValue ctrlValue = formData.getFieldValue(msCtrl.getName());
				updateMultiSelectValues(jdbcDao, ctrlValue, recordId);
			}

			for (Control sfCtrl : subFormCtrls) {
				SubFormControl subFormCtrl = (SubFormControl) sfCtrl;
				ControlValue subFormVal = formData.getFieldValue(subFormCtrl.getName());
				List<FormData> subFormsData = subFormVal != null ? (List<FormData>) subFormVal.getValue() : null;
				
				if (subFormsData == null) {
					continue;
				}
				String sfTableName = subFormCtrl.getSubContainer().getDbTableName();
				Set<Long> currentSfIds = new HashSet<Long>();
				
				for (FormData subFormData : subFormsData) {
					Long subFormRecId = saveOrUpdateFormData(jdbcDao, subFormData, recordId);
					subFormData.setRecordId(subFormRecId);
					currentSfIds.add(subFormRecId);
				}
		
				Set<Long> persistedSfIds = getPersistedSfIds(jdbcDao, sfTableName, formData.getRecordId());
				List<Long> deletedSfData = new ArrayList<Long>();
				
				for (Long persistedId : persistedSfIds) {
					if (!currentSfIds.contains(persistedId)) {
						deletedSfData.add(persistedId);
					}
				}
				
				if (!deletedSfData.isEmpty()) {
					removeDeletedSfData(jdbcDao, sfTableName, deletedSfData);
				}
			}
		} finally {
			for (InputStream inputStream : inputStreams) {
				IoUtil.close(inputStream);
			}
		}
		
		return recordId;		
	}

	private String buildUpsertSql(List<Control> simpleCtrls, String tableName, Long recordId, Long parentRecId) {
		String sql = null;
		if (recordId == null) {
			sql = buildInsertSql(simpleCtrls, tableName, parentRecId != null);
		} else {
			sql = buildUpdateSql(simpleCtrls, tableName);
		}
		
		return sql;
	}
	
	//
	// take care of file control;
	// 
	private String buildInsertSql(List<Control> simpleCtrls, String tableName, boolean insertParentRecId) {
		StringBuilder columnNames = new StringBuilder();
		StringBuilder bindVars = new StringBuilder();
		
		for (Control ctrl : simpleCtrls) {
			if (ctrl instanceof FileUploadControl) {
				columnNames.append(ctrl.getDbColumnName()).append("_NAME").append(", ");
				bindVars.append("?, ");
				
				columnNames.append(ctrl.getDbColumnName()).append("_TYPE").append(", ");
				bindVars.append("?, ");
				
				columnNames.append(ctrl.getDbColumnName()).append("_ID").append(", ");
				bindVars.append("?, ");
			} else {
				columnNames.append(ctrl.getDbColumnName()).append(", ");
				bindVars.append("?, ");
			}			
		}
		
		if (insertParentRecId) {
			columnNames.append("PARENT_RECORD_ID, ");
			bindVars.append("?, ");
		}
		
		columnNames.append("IDENTIFIER");
		bindVars.append("?");
		
		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO ").append(tableName)
			.append("(").append(columnNames).append(") VALUES(")
			.append(bindVars).append(")");
		
		return insertSql.toString();
	}
	
	private String buildUpdateSql(List<Control> simpleCtrls, String tableName) {
		if (simpleCtrls.isEmpty()) {
			return "";
		}
		
		StringBuilder updateSql = new StringBuilder();		
		updateSql.append("UPDATE ").append(tableName).append(" SET ");
		
		for (Control ctrl : simpleCtrls) {
			if (ctrl instanceof FileUploadControl) {
				updateSql.append(ctrl.getDbColumnName()).append("_NAME = ?, ");
				updateSql.append(ctrl.getDbColumnName()).append("_TYPE = ?, ");
				updateSql.append(ctrl.getDbColumnName()).append("_ID = ?, ");
			} else {
				updateSql.append(ctrl.getDbColumnName()).append(" = ?, ");
			}			
		}		
		updateSql.delete(updateSql.length() - 2, updateSql.length());
		
		updateSql.append(" WHERE IDENTIFIER = ?");
		return updateSql.toString();
	}
	
	private void updateMultiSelectValues(JdbcDao jdbcDao, ControlValue msCtrlValue, Long recordId) 
	throws Exception {
		String[] strValues =  msCtrlValue != null ? (String[])msCtrlValue.getValue() : null;
		MultiSelectControl msCtrl = msCtrlValue != null ? (MultiSelectControl)msCtrlValue.getControl() : null;
			
		if (msCtrl != null) {
			String deleteSql = String.format(DELETE_MULTI_SELECT_VALUES_SQL, msCtrl.getTableName());		
			jdbcDao.executeUpdate(deleteSql, Collections.singletonList(recordId));
		}
		
		if (strValues != null) {
			String insertSql = String.format(INSERT_MULTI_SELECT_VALUES_SQL, msCtrl.getTableName());
			
			List<Object> params = new ArrayList<Object>();
			for (String strValue : strValues) {
				Object value = msCtrl.fromString(strValue);
				
				params.clear();
				params.add(recordId);				
				params.add(value);				
				jdbcDao.executeUpdate(insertSql, params);				
			}
		}		
	}
	
	private void segregateControls(Container container, List<Control> simpleCtrls, List<Control> multiSelectCtrls, List<Control> subFormCtrls) {
		for (Control ctrl : container.getControlsMap().values()) {
			if (ctrl instanceof SubFormControl) {
				subFormCtrls.add(ctrl);
			} else if (ctrl instanceof MultiSelectControl) {
				multiSelectCtrls.add(ctrl); 
			} else if (!(ctrl instanceof Label || ctrl instanceof PageBreak)) {
				simpleCtrls.add(ctrl);
			}
		}
	}
	
	private Set<Long> getPersistedSfIds(JdbcDao jdbcDao, String dbTableName, Long parentRecId) {
		String selectSql = String.format(GET_SUB_FORM_IDS_SQL, dbTableName);
		return jdbcDao.getResultSet(selectSql, Collections.singletonList(parentRecId), new ResultExtractor<Set<Long>>() {
			@Override
			public Set<Long> extract(ResultSet rs) throws SQLException {
				Set<Long> sfIds = new HashSet<Long>();
					
				while (rs.next()) {
					sfIds.add(rs.getLong("IDENTIFIER"));
				}
				
				return sfIds;
			}
		});			
	}
	
	private void removeDeletedSfData(JdbcDao jdbcDao, String sfTableName, List<Long> toBeDeletedSfData) {
		StringBuilder deleteSqlBuilder = new StringBuilder().append("DELETE FROM ")
				.append(sfTableName).append(" WHERE IDENTIFIER IN ( ");
		
		for (int i = 0 ; i < toBeDeletedSfData.size() ; i++) {
			deleteSqlBuilder.append(" ?,");
		}
		String deleteSql = deleteSqlBuilder.substring(0, deleteSqlBuilder.lastIndexOf(",")).concat(")");
		jdbcDao.executeUpdate(deleteSql, toBeDeletedSfData);
	}
}
