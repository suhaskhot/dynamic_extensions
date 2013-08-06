
package edu.common.dynamicextensions.napi;

import static edu.common.dynamicextensions.napi.FormDataManagerTestUtility.*;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.ndao.ContainerDao;
import edu.common.dynamicextensions.ndao.JdbcDao;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FormDataManagerImpl.class, JdbcDao.class, ResultSet.class})
public class FormDataManagerTest {
	
	//@Rule 
	//public PowerMockRule rule = new PowerMockRule();
			
	@Before
	public void setUp() 
	throws Exception {

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
		
//		System.err.println(" JdbcDao.class :: "+JdbcDao.class);
//		System.err.println(" JdbcDao.class name :: "+JdbcDao.class.getName());
		mockJdbcDao = createMock(JdbcDao.class);
		expectNew(JdbcDao.class).andReturn(mockJdbcDao);
		
		//
		// Unit under test
		//
		formDataMgr = new FormDataManagerImpl();		
	}
	
	@After
	public void after() {
	}
	
	
	/**
	 * TC for retrieving non-existent form record 
	 * 
	 * Expected Result : SUCCESS (ASSERT NULL)
	 * @throws Exception
	 */
	@Test
	public void testGetFormDataForNonExistingRecord() 
	throws Exception {
		mockEmptyResultSet(userTableQuery);
		mockJdbcDao.close();
		
		replayAll();
		
		FormData formData = formDataMgr.getFormData(userProfile, 1L);		
		
		verifyAll();
		assertNull(formData);	
	}

	
	/**
	 * TC for retrieving form record without any sub-form records
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testGetFormDataWithFileControl()
	throws Exception {		
		mockQueryResultSet(userTableQuery, userTabColumnNames, userTabRows);		
		mockQueryResultSet(hobbiesTableQuery, hobbiesTabColumnNames, hobbiesTabRows);
		mockEmptyResultSet(addressTableQuery);		
		mockJdbcDao.close();		
		
		replayAll();

		FormData actualFormData = formDataMgr.getFormData(userProfile, 2L);
		
		verifyAll();
		assertNotNull(actualFormData);
		
		// set sub form data to empty list of records
		formData.addFieldValue(new ControlValue(userProfile.getControl("address"), Collections.emptyList()));
		assertFormDataEquals(formData, actualFormData);
	}

	/**
	 * TC for retrieving the form record having sub-form records
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testGetFormDataForSubForms()
	throws Exception {
		mockQueryResultSet(userTableQuery, userTabColumnNames, userTabRows);
		mockQueryResultSet(addressTableQuery, addressTabColumnNames, addressTabRows);
		mockQueryResultSet(hobbiesTableQuery, hobbiesTabColumnNames, hobbiesTabRows);
		mockQueryResultSet(parkingFacilitiesTableQuery, parkingFacilitiesColumnNames, parkingFacilitiesTabRows);
		mockEmptyResultSet(parkingFacilitiesTableQuery);
		mockJdbcDao.close();		
		
		replayAll();
		
		FormData actualFormData = formDataMgr.getFormData(userProfile, 2L);
		
		verifyAll();
		assertNotNull(actualFormData);
		assertFormDataEquals(formData, actualFormData);		
	}
	
	/**
	 * TC for retrieving the form record using container id and record id
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testGetFormDataForSubFormsUsingContainerId()
	throws Exception {
		ContainerDao containerDao = createMock(ContainerDao.class);
		expectNew(ContainerDao.class, new Class[] { JdbcDao.class }, mockJdbcDao).andReturn(containerDao);
		expect(containerDao.getById(111L)).andReturn(userProfile);
		
		mockQueryResultSet(userTableQuery, userTabColumnNames, userTabRows);
		mockQueryResultSet(addressTableQuery, addressTabColumnNames, addressTabRows);
		mockQueryResultSet(hobbiesTableQuery, hobbiesTabColumnNames, hobbiesTabRows);
		mockQueryResultSet(parkingFacilitiesTableQuery, parkingFacilitiesColumnNames, parkingFacilitiesTabRows);
		mockEmptyResultSet(parkingFacilitiesTableQuery);
		mockJdbcDao.close();		
		
		replayAll();
		
		FormData actualFormData = formDataMgr.getFormData(111L, 2L);
		
		verifyAll();
		assertNotNull(actualFormData);
		assertFormDataEquals(formData, actualFormData);		
	}

	/**
	 * TC for saving form data into database
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testSaveFormData() {
		mockIdGeneration("USER_PROFILES", 9933L);
		mockUpdate(userTableInsertSql, 1);
		mockUpdate(hobbiesTableDeleteSql, 1);
		mockUpdate(hobbiesTableInsertSql, 2);
				
		mockIdGeneration("USER_ADDRESSES", 11L);
		mockIdGeneration("USER_ADDRESSES", 12L);
		mockUpdate(addressTableInsertSql, 2);
		mockUpdate(parkingFacilitiesTableDeleteSql, 2); // one delete for each instance of sub-form insert
		mockUpdate(parkingFacilitiesTableInsertSql, 3);		
	    mockJdbcDao.close();
	    
	    nullifyId(formData);

	    replayAll();
	    
	    Long expectedRecordId = 9933L;
		Long actualRecordId = formDataMgr.saveOrUpdateFormData(null, formData);
		
		verifyAll();
		assertEquals(actualRecordId, expectedRecordId);
		
		//
		// Verify record ids of sub-form are correctly populated
		//
		ControlValue subFormCtrl = formData.getFieldValue("address");
		List<FormData> subFormData = (List<FormData>)subFormCtrl.getValue();
		assertEquals(subFormData.get(0).getRecordId().longValue(), 11L);
		assertEquals(subFormData.get(1).getRecordId().longValue(), 12L);
	}

	/**
	 * TC for updating form data in database. 
	 * The main form is update; while sub-forms are insert 
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testUpdateParentFormData() {	
		mockUpdate(userTableUpdateSql, 1);
		mockUpdate(hobbiesTableDeleteSql, 1);
		mockUpdate(hobbiesTableInsertSql, 2);
				
		mockIdGeneration("USER_ADDRESSES", 11L);
		mockIdGeneration("USER_ADDRESSES", 12L);
		mockUpdate(addressTableInsertSql, 2);
		mockUpdate(parkingFacilitiesTableDeleteSql, 2); // one delete for each instance of sub-form insert
		mockUpdate(parkingFacilitiesTableInsertSql, 3);		
	    mockJdbcDao.close();
	    
	    nullifyId(formData);

	    replayAll();
	    
	    Long expectedRecordId = 9933L;
	    formData.setRecordId(expectedRecordId);
		Long actualRecordId = formDataMgr.saveOrUpdateFormData(null, formData);
		
		verifyAll();
		assertEquals(actualRecordId, expectedRecordId);
		
		//
		// Verify record ids of sub-form are correctly populated
		//
		ControlValue subFormCtrl = formData.getFieldValue("address");
		List<FormData> subFormData = (List<FormData>)subFormCtrl.getValue();
		assertEquals(subFormData.get(0).getRecordId().longValue(), 11L);
		assertEquals(subFormData.get(1).getRecordId().longValue(), 12L);
	}

	/**
	 * TC for updating form data in database.
	 * Both main form and sub-form instances are update
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testUpdateFormData() {	
		mockUpdate(userTableUpdateSql, 1);
		mockUpdate(hobbiesTableDeleteSql, 1);
		mockUpdate(hobbiesTableInsertSql, 2);
				
		mockUpdate(addressTableUpdateSql, 2);
		mockUpdate(parkingFacilitiesTableDeleteSql, 2); // one delete for each instance of sub-form insert
		mockUpdate(parkingFacilitiesTableInsertSql, 3);		
	    mockJdbcDao.close();
	    
	    replayAll();
	    
	    Long expectedRecordId = formData.getRecordId();
		ControlValue subFormCtrl = formData.getFieldValue("address");
		List<FormData> subFormData = (List<FormData>)subFormCtrl.getValue();
	    
	    long expectedSubFormId1 = subFormData.get(0).getRecordId();
	    long expectedSubFormId2 = subFormData.get(1).getRecordId();
	    
		Long actualRecordId = formDataMgr.saveOrUpdateFormData(null, formData);
		
		verifyAll();
		assertEquals(actualRecordId, expectedRecordId);
		
		//
		// Verify record ids of sub-form are correctly populated
		//
		assertEquals(subFormData.get(0).getRecordId().longValue(), expectedSubFormId1);
		assertEquals(subFormData.get(1).getRecordId().longValue(), expectedSubFormId2);
	}

	protected void assertFormDataEquals(FormData expectedFormData, FormData actualFormData) {
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
		userProfile.setDbTableName("USER_PROFILES");
		
		StringTextField firstName = new StringTextField();
		firstName.setName("firstName");
		userProfile.addControl(firstName);
			
		StringTextField lastName = new StringTextField();
		lastName.setName("lastName");
		userProfile.addControl(lastName);
		
		DatePicker dateOfBirth = new DatePicker();
		dateOfBirth.setName("dateOfBirth");
		userProfile.addControl(dateOfBirth);
		
		MultiSelectCheckBox hobbies = new MultiSelectCheckBox();
		hobbies.setTableName("HOBBIES");
		hobbies.setName("hobbies");
		PvDataSource hobbiesDs = new PvDataSource();
		hobbiesDs.setDataType(DataType.STRING);		
		hobbies.setPvDataSource(hobbiesDs);
		userProfile.addControl(hobbies);
		
		
		Container address = new Container();
		address.setName("address");
		address.setDbTableName("USER_ADDRESSES");
		
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
		
		SubFormControl addressSf = new SubFormControl();
		addressSf.setSubContainer(address);
		addressSf.setName("address");
		userProfile.addControl(addressSf);		
	}
	
	private void setUpExpectedSqls() {
		//
		// Given below are list of SQLs expected to be generated by FormDataManagerImpl for
		// Get form data, Insert form data, Update form data 
		//
		userTableQuery              = "SELECT DE_A_1, DE_A_2, DE_A_3, IDENTIFIER FROM USER_PROFILES WHERE IDENTIFIER = ?";		
		addressTableQuery           = "SELECT DE_A_1, DE_A_2, IDENTIFIER FROM USER_ADDRESSES WHERE PARENT_RECORD_ID = ?";
		hobbiesTableQuery           = "SELECT VALUE FROM HOBBIES WHERE RECORD_ID = ?";
		parkingFacilitiesTableQuery = "SELECT VALUE FROM PARKING_FACILITIES WHERE RECORD_ID = ?";
		
		userTableInsertSql          = "INSERT INTO USER_PROFILES(DE_A_1, DE_A_2, DE_A_3, IDENTIFIER) VALUES(?, ?, ?, ?)";
		addressTableInsertSql       = "INSERT INTO USER_ADDRESSES(DE_A_1, DE_A_2, PARENT_RECORD_ID, IDENTIFIER) VALUES(?, ?, ?, ?)";
		hobbiesTableInsertSql       = "INSERT INTO HOBBIES (RECORD_ID, VALUE) VALUES (?, ?)";
		parkingFacilitiesTableInsertSql = "INSERT INTO PARKING_FACILITIES (RECORD_ID, VALUE) VALUES (?, ?)";
		
		userTableUpdateSql          = "UPDATE USER_PROFILES SET DE_A_1 = ?, DE_A_2 = ?, DE_A_3 = ? WHERE IDENTIFIER = ?";
		addressTableUpdateSql       = "UPDATE USER_ADDRESSES SET DE_A_1 = ?, DE_A_2 = ? WHERE IDENTIFIER = ?";
		hobbiesTableDeleteSql       = "DELETE FROM HOBBIES WHERE RECORD_ID = ?";
		parkingFacilitiesTableDeleteSql = "DELETE FROM PARKING_FACILITIES WHERE RECORD_ID = ?";        		
	}
	
	private void setUpTestFormData() {
		formData = new FormData(userProfile);
		formData.setRecordId(2L);
		formData.addFieldValue(new ControlValue(userProfile.getControl("firstName"), "Kareena"));
		formData.addFieldValue(new ControlValue(userProfile.getControl("lastName"), "Williams"));
		formData.addFieldValue(new ControlValue(userProfile.getControl("dateOfBirth"), "08-10-1981"));
		
		String[] hobbies = {"Reading", "Music"};
		formData.addFieldValue(new ControlValue(userProfile.getControl("hobbies"), hobbies));
		
		SubFormControl addressSf = (SubFormControl)userProfile.getControl("address");
		Container address = addressSf.getSubContainer();
		
		FormData address1 = new FormData(address);
		address1.setRecordId(21L);
		address1.addFieldValue(new ControlValue(address.getControl("street"), "Burlington Street"));
		address1.addFieldValue(new ControlValue(address.getControl("city"), "New York"));
		
		String[] parking = {"LMV", "SUV", "HMV"};
		address1.addFieldValue(new ControlValue(address.getControl("parkingFacilities"), parking));
		
		FormData address2 = new FormData(address);
		address2.setRecordId(22L);
		address2.addFieldValue(new ControlValue(address.getControl("street"), "Park Avenue"));
		address2.addFieldValue(new ControlValue(address.getControl("city"), "London"));
		address2.addFieldValue(new ControlValue(address.getControl("parkingFacilities"), new String[0]));
		List<FormData> addresses = new ArrayList<FormData>();
		addresses.add(address1);
		addresses.add(address2);
		formData.addFieldValue(new ControlValue(addressSf, addresses));
	}

	private void setUpTableData() {
		Calendar cal = Calendar.getInstance();
		cal.set(1981, 7, 10); // 10th aug 1981		
		
		userTabColumnNames = Arrays.asList("IDENTIFIER", "DE_A_1", "DE_A_2", "DE_A_3");
		userTabRows = new List[] {
				Arrays.asList(2L, "Kareena", "Williams", cal.getTime())
		};

		addressTabColumnNames = Arrays.asList("IDENTIFIER", "DE_A_1", "DE_A_2");
		addressTabRows = new List[] {
				Arrays.asList(21L, "Burlington Street", "New York"),
				Arrays.asList(22L, "Park Avenue", "London")
		};
		
		hobbiesTabColumnNames = Arrays.asList("VALUE", "RECORD_ID");
		hobbiesTabRows = new List[] {
				Arrays.asList("Reading", 2L),
				Arrays.asList("Music",2L)
		};
		
		parkingFacilitiesColumnNames = Arrays.asList("VALUE", "RECORD_ID");
		parkingFacilitiesTabRows = new List[] {
				Arrays.asList( "LMV", 21L),
        	    Arrays.asList( "SUV", 21L),
        	    Arrays.asList( "HMV", 21L)
		};
	}
		
	//
	// All uninteresting declarations go here
	//

	private Container userProfile;

	//
	// SELECT SQLs
	//
	private String userTableQuery;
	private String addressTableQuery;
	private String hobbiesTableQuery;
	private String parkingFacilitiesTableQuery;
	
	//
	// Insert SQLs
	//
	private String userTableInsertSql;
	private String addressTableInsertSql;
	private String hobbiesTableInsertSql;
	private String parkingFacilitiesTableInsertSql;
	
	//
	// Update SQLs
	//
	private String userTableUpdateSql;
	private String addressTableUpdateSql;
	
	private String hobbiesTableDeleteSql;
	private String parkingFacilitiesTableDeleteSql;
	
	//
	// test form data for insert, update and get
	//
	protected FormData formData;
	
	protected FormDataManager formDataMgr;
	
	private List<String> userTabColumnNames;
	
	private List[] userTabRows;

	private List<String> addressTabColumnNames;
	
	private List[] addressTabRows;

	private List<String> hobbiesTabColumnNames;
	
	private List[] hobbiesTabRows;
	
	private List<String> parkingFacilitiesColumnNames;
	
	private List[] parkingFacilitiesTabRows;


}
