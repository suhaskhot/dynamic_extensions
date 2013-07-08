package edu.common.dynamicextensions.domain.nui;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.util.IdGeneratorUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IdGeneratorUtil.class, Container.class, JdbcDao.class, ResultSet.class, Blob.class})
public class ContainerTest {	
	private Container userProfile;
	
	private Container address;

	protected JdbcDao mockJdbcDao;

	protected UserContext userCtxt;

	@Before
	public void setUp() 
	throws Exception {
		setUpForm();
		
		mockJdbcDao = createMock(JdbcDao.class);
		expectNew(JdbcDao.class).andReturn(mockJdbcDao);
		
		mockJdbcDao.suspendTxn();
		mockJdbcDao.resumeTxn();
		
		mockStatic(IdGeneratorUtil.class);
		
		userCtxt = createUserContext();
	}
	
	
	@Test
	public void testSaveForm() 
	throws Exception {
		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(3L);
		String expectedDdl = "CREATE TABLE DE_E_3 (IDENTIFIER NUMBER PRIMARY KEY, DE_A_1 VARCHAR2(4000), DE_A_2 VARCHAR2(4000), DE_A_3 DATE)";
		mockJdbcDao.executeDDL(expectedDdl);
		
		Long containerId = 5L;
		mockContainerIdGen(containerId);	
		ByteArrayOutputStream outstream = mockContainerInsert();
		
		replayAll();
		
		Long actualContainerId = userProfile.save(userCtxt);
		
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);		
	}
	
	@Test(expected = RuntimeException.class)
	public void testSaveFormException()
	throws Exception {
		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(3L);
		String expectedDdl = "CREATE TABLE DE_E_3 (IDENTIFIER NUMBER PRIMARY KEY, DE_A_1 VARCHAR2(4000), DE_A_2 VARCHAR2(4000), DE_A_3 DATE)";
		mockJdbcDao.executeDDL(expectedDdl);
		expectLastCall().andThrow(new RuntimeException("Error creating table"));

		Long containerId = 5L;
		mockContainerIdGen(containerId);	
		ByteArrayOutputStream outstream = mockContainerInsert();
		
		replayAll();
		
		userProfile.save(userCtxt);
	}
	
	@Test 
	public void testAddNewControl() 
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);
		
		//
		// mock DDL for main form
		//
		mockJdbcDao.executeDDL("ALTER TABLE DE_E_3 ADD (DE_A_4 NUMBER)");
		
		//
		// mock container update
		//
		ByteArrayOutputStream outstream = mockContainerUpdate();
		
		replayAll();

		//
		// add a new control
		//
		NumberField approxSalary = new NumberField();
		approxSalary.setName("approxSalary");
		userProfile.addControl(approxSalary);
		
		Long actualContainerId = userProfile.save(userCtxt);

		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());		
		assertEquals(userProfile.toXml(), xml);		
	}
	
	@Test(expected=RuntimeException.class)
	public void testAddNewControlDuplicateName()
	throws Exception {
		StringTextField firstName = new StringTextField();
		firstName.setName("firstName");
		userProfile.addControl(firstName);
	}
	
	@Test
	public void testAddSubForm()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		//
		// mock DDL for sub form
		//
		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(6L);		
		String expectedSfDdl = "CREATE TABLE DE_E_6 (IDENTIFIER NUMBER PRIMARY KEY, DE_A_1 VARCHAR2(4000), DE_A_2 VARCHAR2(4000), PARENT_RECORD_ID NUMBER)";
		mockJdbcDao.executeDDL(expectedSfDdl);
		
		//
		// mock DDL for multi valued control in sub form
		//
		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(9L);
		String expectedMvDdl = "CREATE TABLE DE_E_9 (VALUE VARCHAR2(4000), RECORD_ID NUMBER)";
		mockJdbcDao.executeDDL(expectedMvDdl);		
		
		//
		// mock sub form id generation
		//
		Long subFormId = 10L;
		mockContainerIdGen(subFormId);
		
		ByteArrayOutputStream outstream = mockContainerUpdate();
		
		replayAll();
				
		//
		// add a new sub form containing multi-valued control
		//
		SubFormControl addressSf = new SubFormControl();
		addressSf.setSubContainer(address);
		addressSf.setName("address");
		userProfile.addControl(addressSf);
		
		Long actualContainerId = userProfile.save(userCtxt);

		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(subFormId.longValue(), address.getId().longValue());		
		assertEquals(userProfile.toXml(), xml);			
	}
	
	@Test(expected=RuntimeException.class)
	public void testEditNonExistingControl()
	throws Exception {
		StringTextField firstName = new StringTextField();
		firstName.setName("Name");
		userProfile.editControl("name", firstName);
	}
	
	@Test
	public void testEditNonPersistedControl()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		mockJdbcDao.executeDDL("ALTER TABLE DE_E_3 ADD (DE_A_4 NUMBER)");		
		ByteArrayOutputStream outstream = mockContainerUpdate();
		
		replayAll();
		
		//
		// Add a new control
		//
		StringTextField approxSalary = new StringTextField();
		approxSalary.setName("approxSalary");
		userProfile.addControl(approxSalary);

		//
		// Change type of existing control that is not yet persisted
		//
		NumberField approxSalaryEdited = new NumberField();
		approxSalaryEdited.setName("approxSalary");
		userProfile.editControl("approxSalary", approxSalaryEdited);
		
		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);		
	}
	
	@Test
	public void testEditPersistedControlOfSameType()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);
		
		// we do not expect any DDLs
		ByteArrayOutputStream outstream = mockContainerUpdate();		
		replayAll();		
		
		//
		// Edit an existing control that was persisted before 
		// change of format here
		//
		DatePicker dateOfBirth = new DatePicker();
		dateOfBirth.setName("dateOfBirth");
		dateOfBirth.setFormat("yyyy-MM-dd");
		userProfile.editControl("dateOfBirth", dateOfBirth);

		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);		
	}
	
	@Test
	public void testEditPersistedControlOfDifferingType()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		mockJdbcDao.executeDDL("ALTER TABLE DE_E_3 ADD (DE_A_4 VARCHAR2(4000))");		
		ByteArrayOutputStream outstream = mockContainerUpdate();		
		replayAll();
		
		//
		// Change type of existing control that was persisted before
		//
		TextArea firstName = new TextArea();
		firstName.setName("firstName");
		userProfile.editControl("firstName", firstName);

		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);		
	}
	
	@Test
	public void testDeletePersistedControl()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		// we do not expect any ddl, as we do not hard delete columns
		ByteArrayOutputStream outstream = mockContainerUpdate();		
		replayAll();
		
		userProfile.deleteControl("firstName");
		
		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);
	}
	
	@Test
	public void testDeleteNonPersistedControl()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		// we do not expect any ddl, as nothing is changed
		ByteArrayOutputStream outstream = mockContainerUpdate();		
		replayAll();
		
		//
		// Add a new control
		//
		NumberField salary = new NumberField();
		salary.setName("salary");
		userProfile.addControl(salary);
		
		//
		// Delete the newly control
		//
		userProfile.deleteControl("salary");
		
		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);		
	}
	
	@Test(expected=RuntimeException.class)
	public void testDeleteNonExistingControl()
	throws Exception {
		userProfile.deleteControl("name");
	}
	
	@Test
	public void testAddMultiSelectCheckBoxControl()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		//
		// mock DDL for numeric multi valued control
		//
		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(9L);
		String expectedMvDdl = "CREATE TABLE DE_E_9 (VALUE NUMBER, RECORD_ID NUMBER)";
		mockJdbcDao.executeDDL(expectedMvDdl);		

		ByteArrayOutputStream outstream = mockContainerUpdate();		
		replayAll();
		
		
		//
		// Add a numeric multi select check box
		//
		PvDataSource pvDataSource = new PvDataSource();
		pvDataSource.setDataType(DataType.INTEGER);
		MultiSelectCheckBox luckyNumbers = new MultiSelectCheckBox();
		luckyNumbers.setPvDataSource(pvDataSource);
		
		userProfile.addControl(luckyNumbers);
		
		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);				
	}
	
	@Test
	public void testAddMultiSelectListBoxControl()
	throws Exception {
		Long containerId = 5L;
		userProfile.clearLogs();
		userProfile.setDbTableName("DE_E_3");
		userProfile.setId(containerId);

		//
		// mock DDL for string multi valued control
		//
		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(9L);
		String expectedMvDdl = "CREATE TABLE DE_E_9 (VALUE VARCHAR2(4000), RECORD_ID NUMBER)";
		mockJdbcDao.executeDDL(expectedMvDdl);		

		ByteArrayOutputStream outstream = mockContainerUpdate();		
		replayAll();
		
		
		//
		// Add a multi select list box
		//
		PvDataSource pvDataSource = new PvDataSource();
		pvDataSource.setDataType(DataType.STRING);
		MultiSelectListBox favoriteCusines = new MultiSelectListBox();
		favoriteCusines.setPvDataSource(pvDataSource);
		
		userProfile.addControl(favoriteCusines);
		
		Long actualContainerId = userProfile.save(userCtxt);
		String xml = new String(outstream.toByteArray());
		assertEquals(containerId.longValue(), actualContainerId.longValue());
		assertEquals(userProfile.toXml(), xml);		
	}
		
	
	@Test
	public void testGetContainer() 
	throws Exception {
		Long containerId = 5L;
		userProfile.setId(5L);
		userProfile.setDbTableName("DE_E_3");
		
		String expectedGetSql = "SELECT XML FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ?";
		Blob mockedBlob = mockBlobForRead(userProfile.toXml());
		
		mockQueryResultSet(expectedGetSql, Arrays.asList("XML"), new List[] { Arrays.asList(mockedBlob) });
		mockJdbcDao.close();
		
		replayAll();
		
		Container actual = Container.getContainer(containerId);
		assertEquals(userProfile.toXml(), actual.toXml());
	}
	
	@Test
	public void testGetNonExistingContainer()
	throws Exception {
		Long containerId = 5L;
		
		String expectedGetSql = "SELECT XML FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ?";
		mockQueryResultSet(expectedGetSql, Arrays.asList("XML"), new List[0]);
		mockJdbcDao.close();
		
		replayAll();
		
		Container actual = Container.getContainer(containerId);
		assertNull(actual);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGetContainerException()
	throws Exception {
		Long containerId = 5L;
		userProfile.setId(5L);
		userProfile.setDbTableName("DE_E_3");
		
		String expectedGetSql = "SELECT XML FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ?";
		Blob mockedBlob = mockBlobForReadException(userProfile.toXml());
		
		mockQueryResultSet(expectedGetSql, Arrays.asList("XML"), new List[] { Arrays.asList(mockedBlob) });
		mockJdbcDao.close();
		
		replayAll();
		
		Container actual = Container.getContainer(containerId);		
	}
	
	protected ResultSet mockQueryResultSet(String query, List<String> columnNames, List[] rows)
	throws SQLException {
		ResultSet rs = mockResultSet(columnNames, rows);
		expect(mockJdbcDao.getResultSet(eq(query), anyObject(List.class))).andReturn(rs);
		mockJdbcDao.close(rs);
		return rs;
	}
	
	private ResultSet mockResultSet(List<String> columnNames, List ... rows) 
	throws SQLException {
		ResultSet rs = createMock(ResultSet.class);
		
		if (rows != null) {
			for (List<Object> row : rows) {
				expect(rs.next()).andReturn(true);				
				for (int i = 0; i < columnNames.size(); ++i) {
					String columnName = columnNames.get(i);					
					if (columnName.equals("IDENTIFIER") || columnName.equals("NEXTVAL")) {
						expect(rs.getLong(columnName)).andReturn((Long)row.get(i));
					} else if (columnName.endsWith("_NAME") || columnName.endsWith("_TYPE")){
						expect(rs.getString(columnName)).andReturn(row.get(i).toString());
					} else if (columnName.equals("RECORD_ID")) {
						continue; // we do not expect call to retrieve record_id as rows are selected based on record_id 
					} else if (columnName.equals("XML")) {
						expect(rs.getBlob("XML")).andReturn((Blob)row.get(i));
					} else {
						expect(rs.getObject(columnName)).andReturn(row.get(i));
					} 
				}
			}			
		}
		expect(rs.next()).andReturn(false);		
		return rs;
	}
	
	
	protected void mockContainerIdGen(Long... ids)
	throws SQLException {
		String expectedIdSql = "SELECT DYEXTN_CONTAINERS_SEQ.NEXTVAL FROM (SELECT LEVEL FROM DUAL CONNECT BY LEVEL <= 1)";
		
		List[] rows = new List[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			rows[i] = Arrays.asList(ids[i]);
		}
		
		mockQueryResultSet(expectedIdSql, Arrays.asList("NEXTVAL"), rows);		
	}
	
	protected ByteArrayOutputStream mockContainerInsert()
	throws Exception {
		mockUpdate("INSERT INTO DYEXTN_CONTAINERS (IDENTIFIER, NAME, CAPTION, CREATED_BY, CREATE_TIME, XML) VALUES(?, ?, ?, ?, ?, empty_blob())", 1);
		ByteArrayOutputStream outstream = mockContainerXmlWrite();
		return outstream;		
	}
	
	protected ByteArrayOutputStream mockContainerUpdate()
	throws Exception {
		mockUpdate("UPDATE DYEXTN_CONTAINERS SET NAME = ?, CAPTION = ?, LAST_MODIFIED_BY = ?, LAST_MODIFY_TIME = ? WHERE IDENTIFIER = ?", 1);
		ByteArrayOutputStream outstream = mockContainerXmlWrite();
		return outstream;
	}

	private ByteArrayOutputStream mockContainerXmlWrite() 
	throws Exception {
		String selectBlobForUpdate = "SELECT XML FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ? FOR UPDATE";
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		Blob blob = mockBlobForWrite(outstream);
		mockQueryResultSet(selectBlobForUpdate, Arrays.asList("XML"), new List[] { Arrays.asList(blob) });
		mockJdbcDao.close();
		
		return outstream;
	}
	
	private Blob mockBlobForWrite(OutputStream out) 
	throws Exception {
		Blob blob = createMock(Blob.class);
		expect(blob.setBinaryStream(0L)).andReturn(out);
		return blob;
	}
	
	public Blob mockBlobForRead(String xml) 
	throws Exception {
		Blob blob = createMock(Blob.class);
		expect(blob.length()).andReturn((long)xml.getBytes().length);
		expect(blob.getBytes(1L, xml.getBytes().length)).andReturn(xml.getBytes());
		return blob;
	}
	
	public Blob mockBlobForReadException(String xml) 
	throws Exception {
		Blob blob = createMock(Blob.class);
		expect(blob.length()).andReturn((long)xml.getBytes().length);
		expect(blob.getBytes(1L, xml.getBytes().length)).andThrow(new SQLException("Error getting blob bytes"));
		return blob;
	}	
	
	private void mockUpdate(String updateSql, int numTimes) {
		mockJdbcDao.executeUpdate(eq(updateSql), anyObject(List.class));
		expectLastCall().times(numTimes);
	}	
	
	private void setUpForm() {
		//
		// 1. Setup a form as below
		//    first name: [ txt field ]
		//    last  name: [ txt field ]
		//    birth date: [ date picker]
		//    hobbies   : [] playing [] reading [] music
		//    { start of sub form
		//      street  :    [ txt field ]
		//      city    :    [ city    \/] drop down
		//      parking :    [ covered car parking
		//                     covered 2 wheeler parking
		//                     transit parking
		//                   ]                 
		//    }
		//
		
		userProfile = new Container();
		userProfile.setName("userProfile");
		userProfile.setCaption("User Profile");
				
		StringTextField firstName = new StringTextField();
		firstName.setName("firstName");
		userProfile.addControl(firstName);
			
		StringTextField lastName = new StringTextField();
		lastName.setName("lastName");
		userProfile.addControl(lastName);
		
		DatePicker dateOfBirth = new DatePicker();
		dateOfBirth.setName("dateOfBirth");
		userProfile.addControl(dateOfBirth);
		
		
		/*MultiSelectCheckBox hobbies = new MultiSelectCheckBox();
		hobbies.setTableName("HOBBIES");
		hobbies.setName("hobbies");
		PvDataSource hobbiesDs = new PvDataSource();
		hobbiesDs.setDataType("string");		
		hobbies.setPvDataSource(hobbiesDs);
		userProfile.addControl(hobbies);*/
		
		
		address = new Container();
		address.setName("address");
		
		StringTextField street = new StringTextField();
		street.setName("street");
		address.addControl(street);
		
		ComboBox city = new ComboBox();
		city.setName("city");		
		PvDataSource cities = new PvDataSource();
		cities.setDataType(DataType.STRING);		
		city.setPvDataSource(cities);
		address.addControl(city);
		
		MultiSelectListBox parkingFacilities = new MultiSelectListBox();
		parkingFacilities.setTableName("PARKING_FACILITIES");
		parkingFacilities.setName("parkingFacilities");
		PvDataSource parkingTypes = new PvDataSource();
		parkingTypes.setDataType(DataType.STRING);		
		parkingFacilities.setPvDataSource(parkingTypes);
		address.addControl(parkingFacilities);		
	}
	
	protected UserContext createUserContext() {
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
}
