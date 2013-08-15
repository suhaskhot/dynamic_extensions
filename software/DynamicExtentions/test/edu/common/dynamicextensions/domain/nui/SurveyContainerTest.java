//
//package edu.common.dynamicextensions.domain.nui;
//
//import static org.easymock.EasyMock.expect;
//import static org.junit.Assert.assertEquals;
//import static org.powermock.api.easymock.PowerMock.createMock;
//import static org.powermock.api.easymock.PowerMock.expectNew;
//import static org.powermock.api.easymock.PowerMock.mockStatic;
//import static org.powermock.api.easymock.PowerMock.replayAll;
//
//import java.io.ByteArrayOutputStream;
//import java.sql.Blob;
//import java.sql.ResultSet;
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import edu.common.dynamicextensions.ndao.JdbcDao;
//import edu.common.dynamicextensions.util.IdGeneratorUtil;
//import static edu.common.dynamicextensions.domain.nui.ContainerTestUtility.*;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({IdGeneratorUtil.class, Container.class, JdbcDao.class, ResultSet.class, Blob.class})
//public class SurveyContainerTest {
//
//	private SurveyContainer userProfile;
//
//	protected UserContext userCtxt;
//	
//
//	@Before
//	public void setUp() throws Exception {
//		setUpForm();
//
//		mockJdbcDao = createMock(JdbcDao.class);
//		expectNew(JdbcDao.class).andReturn(mockJdbcDao);
//
//		mockJdbcDao.suspendTxn();
//		mockJdbcDao.resumeTxn();
//		userCtxt = createUserContext();
//		mockStatic(IdGeneratorUtil.class);
//	}
//
//	private void setUpForm() {
//		//
//		// 1. Setup a form as below
//		//Page:1
//		//    first name: [ txt field ]
//		//    last  name: [ txt field ]
//		//Page:2
//		//    birth date: [ date picker]
//
//
//		userProfile = new SurveyContainer();
//		userProfile.setName("userProfile");
//		userProfile.setCaption("User Profile");
//
//		Page namePage = new Page("nameDetails", "Name Details");
//		userProfile.addPage(namePage);
//		StringTextField firstName = new StringTextField();
//		firstName.setName("firstName");
//		namePage.addControl(firstName);
//
//		StringTextField lastName = new StringTextField();
//		lastName.setName("lastName");
//		namePage.addControl(lastName);
//
//		Page personalDetails = new Page("personalDetails", "Personal Details");
//		userProfile.addPage(personalDetails);
//		DatePicker dateOfBirth = new DatePicker();
//		dateOfBirth.setName("dateOfBirth");
//		personalDetails.addControl(dateOfBirth);
//	}
//
//	@Test
//	public void testSaveSurveyForm() throws Exception {
//		expect(IdGeneratorUtil.getNextUniqeId()).andReturn(3L);
//		String expectedDdl = "CREATE TABLE DE_E_3 (IDENTIFIER NUMBER PRIMARY KEY, DE_A_1 VARCHAR2(4000), DE_A_2 VARCHAR2(4000), DE_A_3 DATE)";
//		mockJdbcDao.executeDDL(expectedDdl);
//
//		Long containerId = 5L;
//		mockContainerIdGen(containerId);
//		ByteArrayOutputStream outstream = mockContainerInsert();
//
//		replayAll();
//
//		Long actualContainerId = userProfile.save(userCtxt);
//
//		String xml = new String(outstream.toByteArray());
//		assertEquals(containerId.longValue(), actualContainerId.longValue());
//		assertEquals(userProfile.toXml(), xml);
//	}
//
//	@Test
//	public void tesGetSurveyForm() throws Exception
//	{
//		Long containerId = 5L;
//		userProfile.setId(5L);
//		userProfile.setDbTableName("DE_E_3");
//		
//		String expectedGetSql = "SELECT XML, CREATED_BY, CREATE_TIME, LAST_MODIFIED_BY, LAST_MODIFY_TIME FROM DYEXTN_CONTAINERS WHERE IDENTIFIER = ?";
//		Blob mockedBlob = mockBlobForRead(userProfile.toXml());
//
//		mockQueryResultSet(expectedGetSql, Arrays.asList("XML","CREATED_BY","CREATE_TIME","LAST_MODIFIED_BY","LAST_MODIFY_TIME"), 
//				new List[] { Arrays.asList(mockedBlob, 0L, null, 0L, null) });
//		mockJdbcDao.close();
//
//		replayAll();
//
//		Container actual = Container.getContainer(containerId);
//		assertEquals(userProfile.toXml(), actual.toXml());
//
//	}
//
//	@Test
//	public void testAddNewControl() throws Exception {
//		Long containerId = 5L;
//		userProfile.clearLogs();
//		userProfile.setDbTableName("DE_E_3");
//		userProfile.setId(containerId);
//
//		//
//		// mock DDL for main form
//		//
//		mockJdbcDao.executeDDL("ALTER TABLE DE_E_3 ADD (DE_A_4 NUMBER)");
//
//		//
//		// mock container update
//		//
//		ByteArrayOutputStream outstream = mockContainerUpdate();
//
//		replayAll();
//
//		//
//		// add a new control
//		//
//		NumberField approxSalary = new NumberField();
//		approxSalary.setName("approxSalary");
//		userProfile.getPage("personalDetails").addControl(approxSalary);
//
//		Long actualContainerId = userProfile.save(userCtxt);
//
//		String xml = new String(outstream.toByteArray());
//		assertEquals(containerId.longValue(), actualContainerId.longValue());
//		assertEquals(userProfile.toXml(), xml);
//	}
//
//	@Test
//	public void testAddNewPage() throws Exception {
//		Long containerId = 5L;
//		userProfile.clearLogs();
//		userProfile.setDbTableName("DE_E_3");
//		userProfile.setId(containerId);
//
//		//
//		// mock DDL for main form
//		//
//		mockJdbcDao.executeDDL("ALTER TABLE DE_E_3 ADD (DE_A_4 VARCHAR2(4000))");
//
//		//
//		// mock container update
//		//
//		ByteArrayOutputStream outstream = mockContainerUpdate();
//
//		replayAll();
//
//		//Add New Page
//		Page educationDetails = new Page("educationalDetails", "Educational Details");
//		userProfile.addPage(educationDetails);
//		//
//		// add a new control
//		//
//		StringTextField university = new StringTextField();
//		university.setName("university");
//		educationDetails.addControl(university);
//
//		Long actualContainerId = userProfile.save(userCtxt);
//
//		String xml = new String(outstream.toByteArray());
//		assertEquals(containerId.longValue(), actualContainerId.longValue());
//		assertEquals(userProfile.toXml(), xml);
//	}
//
//	@Test
//	public void testDeletePersistedPage() throws Exception {
//		Long containerId = 5L;
//		userProfile.clearLogs();
//		userProfile.setDbTableName("DE_E_3");
//		userProfile.setId(containerId);
//
//		// we do not expect any ddl, as we do not hard delete columns
//		ByteArrayOutputStream outstream = mockContainerUpdate();
//		replayAll();
//
//		userProfile.deletePage("personalDetails");
//
//		Long actualContainerId = userProfile.save(userCtxt);
//		String xml = new String(outstream.toByteArray());
//		assertEquals(containerId.longValue(), actualContainerId.longValue());
//		assertEquals(userProfile.toXml(), xml);
//	}
//	
//	@Test
//	public void testMoveControl() throws Exception {
//		Long containerId = 5L;
//		userProfile.clearLogs();
//		userProfile.setDbTableName("DE_E_3");
//		userProfile.setId(containerId);
//
//		// we do not expect any ddl, as we do not hard delete columns
//		ByteArrayOutputStream outstream = mockContainerUpdate();
//		replayAll();
//
//		userProfile.moveControl("nameDetails", "personalDetails", "lastName");
//
//		Long actualContainerId = userProfile.save(userCtxt);
//		String xml = new String(outstream.toByteArray());
//		assertEquals(containerId.longValue(), actualContainerId.longValue());
//		assertEquals(userProfile.toXml(), xml);
//	}
//
//	@Test(expected = RuntimeException.class)
//	public void testDeleteNonExistantPage() throws Exception {
//		userProfile.deletePage("xname");
//
//	}
//}
