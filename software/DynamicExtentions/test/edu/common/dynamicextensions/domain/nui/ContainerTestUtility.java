package edu.common.dynamicextensions.domain.nui;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.createMock;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import edu.common.dynamicextensions.ndao.JdbcDao;

public class ContainerTestUtility {
	
	protected static JdbcDao mockJdbcDao;

	protected static  UserContext createUserContext() {
		return new UserContext() {
			
			@Override
			public String getUserName() {
				return "de-unit-tester";
			}
			
			@Override
			public Long getUserId() {
				return 7L;
			}
			
			@Override
			public String getIpAddress() {
				return "192.168.1.2";
			}
		}; 
	}
	
	protected static  void mockContainerIdGen(Long... ids)
	throws SQLException {
		String expectedIdSql = "SELECT DYEXTN_CONTAINERS_SEQ.NEXTVAL FROM (SELECT LEVEL FROM DUAL CONNECT BY LEVEL <= 1)";
		
		List[] rows = new List[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			rows[i] = Arrays.asList(ids[i]);
		}
		
		mockQueryResultSet(expectedIdSql, Arrays.asList("NEXTVAL"), rows);		
	}

	protected static  ByteArrayOutputStream mockContainerInsert()
	throws Exception {
		mockUpdate("INSERT INTO DYEXTN_CONTAINERS (IDENTIFIER, NAME, CAPTION, CREATED_BY, CREATE_TIME, XML) VALUES(?, ?, ?, ?, ?, empty_blob())", 1);
		ByteArrayOutputStream outstream = mockContainerXmlWrite();
		return outstream;		
	}
	
	protected static  Blob mockBlobForRead(String xml) 
	throws Exception {
		Blob blob = createMock(Blob.class);
		expect(blob.length()).andReturn((long)xml.getBytes().length);
		expect(blob.getBytes(1L, xml.getBytes().length)).andReturn(xml.getBytes());
		return blob;
	}
	
	protected static  ResultSet mockQueryResultSet(String query, List<String> columnNames, List[] rows)
	throws SQLException {
		ResultSet rs = mockResultSet(columnNames, rows);
		expect(mockJdbcDao.getResultSet(eq(query), anyObject(List.class))).andReturn(rs);
		mockJdbcDao.close(rs);
		return rs;
	}
	
	protected static  ByteArrayOutputStream mockContainerUpdate()
	throws Exception {
		mockUpdate("UPDATE DYEXTN_CONTAINERS SET NAME = ?, CAPTION = ?, LAST_MODIFIED_BY = ?, LAST_MODIFY_TIME = ?, XML = empty_blob() WHERE IDENTIFIER = ?", 1);

		ByteArrayOutputStream outstream = mockContainerXmlWrite();
		return outstream;
	}
	
	protected static  void mockUpdate(String updateSql, int numTimes) {
		mockJdbcDao.executeUpdate(eq(updateSql), anyObject(List.class));
		expectLastCall().times(numTimes);
	}	
	
	protected static  ByteArrayOutputStream mockContainerXmlWrite() 
	throws Exception {
		String selectBlobForUpdate = "SELECT XML, CREATED_BY, CREATE_TIME, LAST_MODIFIED_BY, LAST_MODIFY_TIME FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ? FOR UPDATE";
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		Blob blob = mockBlobForWrite(outstream);
		mockQueryResultSet(selectBlobForUpdate, Arrays.asList("XML"), new List[] { Arrays.asList(blob) });
		mockJdbcDao.close();
		return outstream;
	}
	
	protected static  ResultSet mockResultSet(List<String> columnNames, List ... rows) 
	throws SQLException {
		ResultSet rs = createMock(ResultSet.class);
		
		if (rows != null) {
			for (List<Object> row : rows) {
				expect(rs.next()).andReturn(true);				
				for (int i = 0; i < columnNames.size(); ++i) {
					String columnName = columnNames.get(i);					
					if (columnName.equals("IDENTIFIER") || columnName.equals("NEXTVAL") 
					|| columnName.endsWith("CREATED_BY") || columnName.endsWith("LAST_MODIFIED_BY")) {
						expect(rs.getLong(columnName)).andReturn((Long)row.get(i));
					} else if (columnName.endsWith("_NAME") || columnName.endsWith("_TYPE")){
						expect(rs.getString(columnName)).andReturn(row.get(i).toString());
					} else if (columnName.equals("RECORD_ID")) {
						continue; // we do not expect call to retrieve record_id as rows are selected based on record_id 
					} else if (columnName.equals("XML")) {
						expect(rs.getBlob("XML")).andReturn((Blob)row.get(i));
					}else if (columnName.equals("CREATE_TIME") || columnName.equals("LAST_MODIFY_TIME")) {
						expect(rs.getTimestamp(columnName)).andReturn((java.sql.Timestamp)row.get(i));
					}
					else {
						expect(rs.getObject(columnName)).andReturn(row.get(i));
					} 
				}
			}			
		}
		expect(rs.next()).andReturn(false);		
		return rs;
	}
	
	protected static  Blob mockBlobForWrite(OutputStream out) 
	throws Exception {
		Blob blob = createMock(Blob.class);
		expect(blob.setBinaryStream(0L)).andReturn(out);
		return blob;
	}
	
	protected static  Blob mockBlobForReadException(String xml) 
	throws Exception {
		Blob blob = createMock(Blob.class);
		expect(blob.length()).andReturn((long)xml.getBytes().length);
		expect(blob.getBytes(1L, xml.getBytes().length)).andThrow(new SQLException("Error getting blob bytes"));
		return blob;
	}	

}