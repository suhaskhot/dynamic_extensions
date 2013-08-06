package edu.common.dynamicextensions.napi;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.createMock;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.ndao.JdbcDao;

public class FormDataManagerTestUtility {
	
	protected static JdbcDao mockJdbcDao;
	
	public static ResultSet mockEmptyResultSet(String query)
	throws SQLException {
		ResultSet emptyRs = createMock(ResultSet.class);
		expect(emptyRs.next()).andReturn(false);
		expect(mockJdbcDao.getResultSet(eq(query), anyObject(List.class))).andReturn(emptyRs);
		mockJdbcDao.close(emptyRs);
		return emptyRs;
	}
	
	public static ResultSet mockQueryResultSet(String query, List<String> columnNames, List[] rows)
	throws SQLException {
		ResultSet rs = mockResultSet(columnNames, rows);
		expect(mockJdbcDao.getResultSet(eq(query), anyObject(List.class))).andReturn(rs);
		mockJdbcDao.close(rs);
		return rs;
	}
	
	public static ResultSet mockResultSet(List<String> columnNames, List... rows)
	throws SQLException {
		ResultSet rs = createMock(ResultSet.class);
		
		if (rows != null) {
			for (List<Object> row : rows) {
				expect(rs.next()).andReturn(true);				
				for (int i = 0; i < columnNames.size(); ++i) {
					String columnName = columnNames.get(i);					
					if (columnName.equals("IDENTIFIER")) {
						expect(rs.getLong(columnName)).andReturn((Long)row.get(i));
					} else if (columnName.endsWith("_NAME") || columnName.endsWith("_TYPE")){
						expect(rs.getString(columnName)).andReturn(row.get(i).toString());
					} else if (columnName.equals("RECORD_ID")) {
						continue; // we do not expect call to retrieve record_id as rows are selected based on record_id 
					} else if (columnName.endsWith("_CONTENT")) {
						expect(rs.getBlob(columnName)).andReturn((Blob) row.get(i));
					} else {
						expect(rs.getObject(columnName)).andReturn(row.get(i));
					}
				}
			}			
		}
		expect(rs.next()).andReturn(false);		
		return rs;
	}
		
	public static void mockUpdate(String updateSql, int numTimes) {
		mockJdbcDao.executeUpdate(eq(updateSql), anyObject(List.class));
		expectLastCall().times(numTimes);
	}
	
	public static void mockIdGeneration(String tableName, Long id) {
		expect(mockJdbcDao.getNextId(eq(tableName))).andReturn(id);
	}
	
	public static void nullifyId(FormData formData) {
		formData.setRecordId(null);
		for (ControlValue cv : formData.getFieldValues()) {
			if (cv.getControl() instanceof SubFormControl) {
				List<FormData> subForms = (List<FormData>)cv.getValue();
				if (subForms == null) {
					continue;
				}
				
				for (FormData subForm : subForms) {
					nullifyId(subForm);
				}
			}
		}
	}
	
	public static void assertFormDataEquals(FormData expectedFormData, FormData actualFormData) {
		assertEquals(expectedFormData.getRecordId().longValue(), actualFormData.getRecordId().longValue());
		
		for (ControlValue actualVal : actualFormData.getFieldValues()) {
			ControlValue expectedVal = expectedFormData.getFieldValue(actualVal.getControl().getName());
			assertNotNull(expectedVal);
			assertEquals(expectedVal.getControl(), actualVal.getControl());
			
			if (expectedVal.getControl() instanceof SubFormControl) {
				List<FormData> expectedSubFormData = (List<FormData>)expectedVal.getValue();
				List<FormData> actualSubFormData = (List<FormData>)actualVal.getValue();
				assertEquals(expectedSubFormData.size(), actualSubFormData.size());
				
				for (int i = 0; i < expectedSubFormData.size(); ++i) {
					assertFormDataEquals(expectedSubFormData.get(i), actualSubFormData.get(i));
				}
				
			} else if (expectedVal.getValue() instanceof String[]) {
				List<String> expected = new ArrayList<String> (Arrays.asList((String[]) expectedVal.getValue()));
				List<String> actual = new ArrayList<String> (Arrays.asList((String[]) actualVal.getValue()));
				assertEquals(expected, actual);
			} else if (expectedVal.getValue() instanceof FileControlValue) {
				FileControlValue expctedFileCtrVal = (FileControlValue) expectedVal.getValue();
				FileControlValue actualFileCtrVal = (FileControlValue) actualVal.getValue();
				assertEquals(expctedFileCtrVal.getFileName(), actualFileCtrVal.getFileName());
				assertEquals(expctedFileCtrVal.getContentType(), actualFileCtrVal.getContentType());
			} else {
				assertEquals(expectedVal.getValue(), actualVal.getValue());
			}
		}				
	}
	
}
