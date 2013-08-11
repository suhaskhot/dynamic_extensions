package edu.common.dynamicextensions.ndao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.VersionedContainerInfo;

public class VersionedContainerDao {
	private JdbcDao jdbcDao;
	
	public VersionedContainerDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	public Long getFormIdByDraftContainerId(Long draftContainerId) {
		ResultSet rs = null;
		Long formId = null;
		
		try {
			List<Long> params = Collections.singletonList(draftContainerId);
			rs = jdbcDao.getResultSet(GET_FORM_ID_BY_DRAFT_CONTAINER_ID_SQL, params);
			if (rs.next()) {
				formId = rs.getLong(1);
			}
			
			return formId;
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining form id: " + draftContainerId, e);
		} finally {
			jdbcDao.close(rs);
		}
	}
	
	
	public VersionedContainerInfo getDraftContainerInfo(Long formId) {
		VersionedContainerInfo draftInfo = null;
		ResultSet rs = null;
		
		try {
			rs = jdbcDao.getResultSet(GET_DRAFT_CONTAINER_INFO_SQL, Collections.singletonList(formId));
			if (rs.next()) {
				draftInfo = getInfo(formId, "draft", rs);
			}
			
			return draftInfo;
		} catch (Exception e) {
			throw new RuntimeException("Error getting draft container info for form: " + formId, e);
		} finally {
			jdbcDao.close(rs);
		}		
	}
	
	public List<VersionedContainerInfo> getPublishedContainersInfo(Long formId) {
		List<VersionedContainerInfo> result = new ArrayList<VersionedContainerInfo>();
		ResultSet rs = null;
		try {
			rs = jdbcDao.getResultSet(GET_PUBLISHED_CONTAINER_INFO_SQL, Collections.singletonList(formId));
			while (rs.next()) {
				result.add(getInfo(formId, "published", rs));
			}
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Error getting published container info: " + formId, e);
		} finally {
			jdbcDao.close(rs);
		}
	}
	
	public void insertVersionedContainerInfo(VersionedContainerInfo info) {
		ResultSet rs = null;
		
		try {
			List<Object> params = new ArrayList<Object>();
			if (info.getFormId() != null) {
				rs = jdbcDao.getResultSet(GET_NEXT_ID_SQL, null);
				if (rs.next()) {
					info.setFormId(rs.getLong(1));
				} else {
					throw new RuntimeException("Failed to obtain next form id");
				}
			}
			
			params.add(info.getFormId());
			params.add(info.getContainerId());
			params.add(info.getActivationDate());
			params.add(info.getCreatedBy());
			params.add(info.getCreationTime());
			params.add(info.getStatus());
			
			jdbcDao.executeUpdate(INSERT_VERSIONED_CONTAINER_INFO_SQL, params);
		} catch (Exception e) {
			throw new RuntimeException("Error inserting versioned container info", e);
		} finally {
			jdbcDao.close(rs);
		}
	}
	
	private VersionedContainerInfo getInfo(Long formId, String status, ResultSet rs) 
	throws Exception {
		VersionedContainerInfo info = new VersionedContainerInfo();
		info.setFormId(formId);
		info.setContainerId(rs.getLong(1));
		info.setActivationDate(rs.getDate(2));
		info.setCreatedBy(rs.getLong(3));
		info.setCreationTime(rs.getDate(4));
		info.setStatus(status);
		return info;		
	}
		
	private final static String GET_DRAFT_CONTAINER_INFO_SQL = 
			"SELECT CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME " +
			"FROM VERSIONED_CONTAINERS " +
			"WHERE IDENTIFIER = ? AND STATUS = 'draft'";
	
	private final static String GET_PUBLISHED_CONTAINER_INFO_SQL =
			"SELECT CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME " +
			"FROM VERSIONED_CONTAINERS " +
			"WHERE IDENTIFIER = ? AND STATUS = 'published' " +
			"ORDER BY ACTIVATION_DATE";
	
	private final static String INSERT_VERSIONED_CONTAINER_INFO_SQL = 
			"INSERT INTO VERSIONED_CONTAINERS (IDENTIFIER, CONTAINER_ID, ACTIVATION_DATE, CREATED_BY, CREATE_TIME, STATUS) " +
			"VALUES (?, ?, ?, ?, ?, ?)";
	
	private final static String GET_FORM_ID_BY_DRAFT_CONTAINER_ID_SQL =
			"SELECT IDENTIFIER FROM VERSIONED_CONTAINERS WHERE CONTAINER_ID = ? AND STATUS = 'draft'";
	
	private final static String GET_NEXT_ID_SQL = 
			"SELECT VERSIONED_CONTAINERS_SEQ.NEXTVAL FROM DUAL";
}
