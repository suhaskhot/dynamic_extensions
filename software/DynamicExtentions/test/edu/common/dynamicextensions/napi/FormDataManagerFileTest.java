
package edu.common.dynamicextensions.napi;

import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.assertFormDataEquals;
import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.mockIdGeneration;
import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.mockJdbcDao;
import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.mockQueryResultSet;
import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.mockUpdate;
import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.nullifyId;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.ndao.JdbcDao;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FormDataManagerImpl.class, JdbcDao.class, ResultSet.class})
public class FormDataManagerFileTest {

	@Before
	public void setUp() throws Exception {

		//
		// Setup test data
		//
		setUpForm();
		setUpExpectedSqls();
		setUpTestFormData();
		setUpTableData();

		//
		// Mock JDBC DAO
		//
		mockJdbcDao = createMock(JdbcDao.class);
		expectNew(JdbcDao.class).andReturn(mockJdbcDao);

		mockFileStream = createMock(FileInputStream.class);
		expectNew(FileInputStream.class, "dummyUploadFile.txt").andReturn(mockFileStream);

		//
		// Unit under test
		//
		formDataMgr = new FormDataManagerImpl();
	}

	@After
	public void after() {
	}

	/**
	 * TC getting form data with the file type of control
	 * 
	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testGetFormDataWithFileControl() throws Exception {
		mockQueryResultSet(userTableQuery, userTabColumnNames, userTabRows);
		mockJdbcDao.close();

		replayAll();

		FormData actualFormData = formDataMgr.getFormData(userProfile, 2L);

		assertNotNull(actualFormData);
		assertFormDataEquals(formData, actualFormData);

	}

	/**
	 * TC for getting file content. 
	 * @throws Exception
	 */
	@Test
	public void testGetFileContent() throws Exception {
		String GET_FILE_CONTENT = "SELECT DE_A_2_CONTENT from USER_PROFILES where IDENTIFIER=?";

		byte[] fileContent = "file content".getBytes();
		SerialBlob blob = new SerialBlob(fileContent);
		
		ResultSet resultSet = createMock(ResultSet.class);
		expect(mockJdbcDao.getResultSet(eq(GET_FILE_CONTENT), anyObject(List.class))).andReturn(resultSet);
		expect(resultSet.next()).andReturn(true);
		expect(resultSet.getBlob("DE_A_2_CONTENT")).andReturn(blob);
		mockJdbcDao.close(resultSet);

		replayAll();
		Blob blob2 = formDataMgr.getFileData(2L, (FileUploadControl) userProfile.getControl("reports"));
		byte[] actualByte = blob2.getBytes(1, fileContent.length);
		assertTrue(Arrays.equals(fileContent, actualByte));
	}

	/**
	 * TC for saving form data with file content into database
	 * 
	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testSaveFormWithFileData() throws Exception {
		mockIdGeneration("USER_PROFILES", 9933L);
		mockUpdate(userTableInsertSql, 1);
		mockJdbcDao.close();
		nullifyId(formData);
		mockFileStream.close();

		replayAll();

		Long expectedRecordId = 9933L;
		Long actualRecordId = formDataMgr.saveOrUpdateFormData(null, formData);

		verifyAll();
		assertEquals(actualRecordId, expectedRecordId);

	}

	/**
	 * TC for updating form data with file content in database. 
	 * Expected Result : SUCCESS 
	 * @throws IOException 
	 * @throws Exception
	 */
	@Test
	public void testUpdateFormWithFileData() throws IOException {
		mockUpdate(userTableUpdateSql, 1);
		mockJdbcDao.close();
		mockFileStream.close();
		replayAll();

		Long expectedRecordId = formData.getRecordId();

		Long actualRecordId = formDataMgr.saveOrUpdateFormData(null, formData);

		verifyAll();
		assertEquals(actualRecordId, expectedRecordId);

	}

	private void setUpForm() {
		//
		// 1. Setup a form as below
		//    first name: [ txt field ]
		//	  reports: [file upload control]

		userProfile = new Container();
		userProfile.setName("userProfile");
		userProfile.setCaption("User Profile");
		userProfile.setDbTableName("USER_PROFILES");

		StringTextField firstName = new StringTextField();
		firstName.setName("firstName");
		userProfile.addControl(firstName);

		FileUploadControl reports = new FileUploadControl();
		reports.setName("reports");
		userProfile.addControl(reports);
	}

	private void setUpExpectedSqls() {
		//
		// Given below are list of SQLs expected to be generated by FormDataManagerImpl for
		// Get form data, Insert form data, Update form data 
		//
		userTableQuery = "SELECT DE_A_1, DE_A_2_NAME, DE_A_2_TYPE, IDENTIFIER FROM USER_PROFILES WHERE IDENTIFIER = ?";

		userTableInsertSql = "INSERT INTO USER_PROFILES(DE_A_1, DE_A_2_NAME, DE_A_2_TYPE, DE_A_2_CONTENT, IDENTIFIER) VALUES(?, ?, ?, ?, ?)";

		userTableUpdateSql = "UPDATE USER_PROFILES SET DE_A_1 = ?, DE_A_2_NAME = ?, DE_A_2_TYPE = ?, DE_A_2_CONTENT = ? WHERE IDENTIFIER = ?";
	}

	private void setUpTestFormData() {
		formData = new FormData(userProfile);
		formData.setRecordId(2L);
		formData.addFieldValue(new ControlValue(userProfile.getControl("firstName"), "Kareena"));
		FileControlValue fcValue = new FileControlValue();
		fcValue.setContentType("text/html");
		fcValue.setFileName("report.txt");
		fcValue.setFilePath("dummyUploadFile.txt");
		formData.addFieldValue(new ControlValue(userProfile.getControl("reports"), fcValue));
	}

	private void setUpTableData() {
		userTabColumnNames = Arrays.asList("IDENTIFIER", "DE_A_1", "DE_A_2_NAME", "DE_A_2_TYPE");
		userTabRows = new List[]{Arrays.asList(2L, "Kareena", "report.txt", "text/html")};
	}

	//
	// All uninteresting declarations go here
	//

	private Container userProfile;

	// SELECT SQLs
	private String userTableQuery;

	// Insert SQLs
	private String userTableInsertSql;

	// Update SQLs
	private String userTableUpdateSql;

	//
	// test form data for insert, update and get
	//
	private FormData formData;

	private List<String> userTabColumnNames;

	private List[] userTabRows;
	
	protected FormDataManager formDataMgr;

	protected FileInputStream mockFileStream;
}