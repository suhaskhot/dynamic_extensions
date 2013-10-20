
package edu.common.dynamicextensions.napi;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.io.Writer;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import edu.common.dynamicextensions.napi.impl.FormAuditManagerImpl;
import edu.common.dynamicextensions.ndao.JdbcDao;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FormAuditManagerImpl.class, JdbcDao.class, ResultSet.class})
public class FormAuditManagerTest {
	
	//@Rule 
	//public PowerMockRule rule = new PowerMockRule();
			
	@Before
	public void setUp() 
	throws Exception {

		//
		// Setup test data
		//
		setUpForm();
		setUpTestFormData();
		setUpExpectedSqls();
		
		//
		// Mock JDBC DAO
		//
		mockJdbcDao = createMock(JdbcDao.class);
		expectNew(JdbcDao.class).andReturn(mockJdbcDao);
		
		//
		// Unit under test
		//
		formAuditMgr = new FormAuditManagerImpl();		
	}
	
	@After
	public void after() {
	}
	

	/**
	 * TC for retrieving the form record having sub-form records
	 * 
 	 * Expected Result : SUCCESS 
	 * @throws Exception
	 */
	@Test
	public void testAudit()
	throws Exception {

//		mockQueryResultSet(getAuditId, Arrays.asList("NEXTVAL"), new List[] {Arrays.asList(2L)});
		mockQueryResultSet(getAuditXmlById, Arrays.asList("FORM_DATA_XML"),  new List[] { Arrays.asList(mockClobForRead(auditXml), null, 0L)});
		
		mockUpdateAndGetResultSet(auditEventTableInsert, new String[]{"IDENTIFIER"}, new List[] { Arrays.asList(0L)}, 1);
		mockUpdate(formAuditTableInsert, 1);
	
		mockJdbcDao.close();	
		
		replayAll();
		
		formAuditMgr.audit(null, formData, "INSERT");
		
		verifyAll();
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
		firstName.setUserDefinedName("firstName");

		userProfile.addControl(firstName);
			
		StringTextField lastName = new StringTextField();
		lastName.setName("lastName");
		lastName.setUserDefinedName("lastName");

		userProfile.addControl(lastName);
		
		DatePicker dateOfBirth = new DatePicker();
		dateOfBirth.setName("dateOfBirth");
		dateOfBirth.setUserDefinedName("dateOfBirth");

		userProfile.addControl(dateOfBirth);
		
		MultiSelectCheckBox hobbies = new MultiSelectCheckBox();
		hobbies.setTableName("HOBBIES");
		hobbies.setName("hobbies");
		hobbies.setUserDefinedName("hobbies");
		PvDataSource hobbiesDs = new PvDataSource();
		hobbiesDs.setDataType(DataType.STRING);		
		hobbies.setPvDataSource(hobbiesDs);
		userProfile.addControl(hobbies);
		
		
		Container address = new Container();
		address.setName("address");
		address.setDbTableName("USER_ADDRESSES");
		
		StringTextField street = new StringTextField();
		street.setName("street");
		street.setUserDefinedName("street");
		address.addControl(street);
		
		ComboBox city = new ComboBox();
		city.setName("city");		
		city.setUserDefinedName("city");
		PvDataSource cities = new PvDataSource();
		cities.setDataType(DataType.STRING);		
		city.setPvDataSource(cities);
		address.addControl(city);
		
		MultiSelectListBox parkingFacilities = new MultiSelectListBox();
		parkingFacilities.setTableName("PARKING_FACILITIES");
		parkingFacilities.setName("parkingFacilities");
		parkingFacilities.setUserDefinedName("parkingFacilities");
		PvDataSource parkingTypes = new PvDataSource();
		parkingTypes.setDataType(DataType.STRING);		
		parkingFacilities.setPvDataSource(parkingTypes);
		address.addControl(parkingFacilities);
		
		SubFormControl addressSf = new SubFormControl();
		addressSf.setSubContainer(address);
		addressSf.setName("address");
		addressSf.setUserDefinedName("address");

		userProfile.addControl(addressSf);		
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

	private void setUpExpectedSqls() {
		getAuditId				= "SELECT CATISSUE_AUDIT_EVENT_PARAM_SEQ.NEXTVAL FROM DUAL ";		
		getAuditXmlById         = "SELECT FORM_DATA_XML, FORM_NAME, RECORD_ID FROM DYEXTN_AUDIT_EVENT WHERE IDENTIFIER = ? FOR UPDATE";
		auditEventTableInsert   = "INSERT INTO CATISSUE_AUDIT_EVENT(IDENTIFIER, IP_ADDRESS, EVENT_TIMESTAMP, USER_ID, EVENT_TYPE) VALUES(CATISSUE_AUDIT_EVENT_PARAM_SEQ.NEXTVAL, ?, ?, ?, ?)";
		formAuditTableInsert    = "INSERT INTO DYEXTN_AUDIT_EVENT (IDENTIFIER, FORM_NAME, RECORD_ID, FORM_DATA_XML) VALUES(?, ?, ?,  empty_clob())";
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
						expect(rs.getLong(1)).andReturn((Long)row.get(i));
					} else if (columnName.equals("FORM_DATA_XML")) {
						expect(rs.getClob("FORM_DATA_XML")).andReturn((Clob)row.get(i));
					} 
				}
			}			
		}
		return rs;
	}
		
	public static void mockUpdate(String updateSql, int numTimes) {
		mockJdbcDao.executeUpdate(eq(updateSql), anyObject(List.class));
		expectLastCall().times(numTimes);
	}
	public ResultSet mockUpdateAndGetResultSet(String updateSql, String[] returnCols, List[] rows, int numTimes) 
	throws Exception {
		ResultSet rs = mockResultSet(Arrays.asList(returnCols), rows);
		expect(mockJdbcDao.executeUpdateAndGetResultSet(eq(updateSql), anyObject(List.class), anyObject(String[].class))).andReturn(rs);
		mockJdbcDao.close(rs);
		return rs;
	}
	
	private Clob mockClobForRead(String xml) {
		Clob clob = createMock(Clob.class);
		Writer clobOut = createMock(Writer.class);
		try {
			expect(clob.setCharacterStream(0)).andReturn(clobOut);
			clobOut.write(xml);
			clobOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clob;
	}
			
			
	//
	// All uninteresting declarations go here
	//

	private Container userProfile;

	//
	// SELECT SQLs
	//
	private String getAuditId;
	private String getAuditXmlById;

	//
	// Insert SQLs
	//
	private String auditEventTableInsert;
	private String formAuditTableInsert;
	
	
	private static JdbcDao mockJdbcDao;
	
	private String auditXml="<form-submit><name>userProfile</name><user>no-user</user><ip-address>no-ip</ip-address><record-identifier>2</record-identifier><field-set><container-name>userProfile</container-name><container-id>null</container-id><db-table>USER_PROFILES</db-table><field><control-name>firstName</control-name><ui-label>null</ui-label><db-column>DE_A_1</db-column><value>Kareena</value></field><field><control-name>lastName</control-name><ui-label>null</ui-label><db-column>DE_A_2</db-column><value>Williams</value></field><field><control-name>dateOfBirth</control-name><ui-label>null</ui-label><db-column>DE_A_3</db-column><value>08-10-1981</value></field><field><control-name>hobbies</control-name><ui-label>null</ui-label><collection><db-table>HOBBIES</db-table><element><db-column>VALUE</db-column><value>Reading</value></element><element><db-column>VALUE</db-column><value>Music</value></element></collection></field><field><control-name>address</control-name><ui-label>null</ui-label><field-set><container-name>address</container-name><container-id>null</container-id><db-table>USER_ADDRESSES</db-table><field><control-name>street</control-name><ui-label>null</ui-label><db-column>DE_A_1</db-column><value>Burlington Street</value></field><field><control-name>city</control-name><ui-label>null</ui-label><db-column>DE_A_2</db-column><value>New York</value></field><field><control-name>parkingFacilities</control-name><ui-label>null</ui-label><collection><db-table>PARKING_FACILITIES</db-table><element><db-column>VALUE</db-column><value>LMV</value></element><element><db-column>VALUE</db-column><value>SUV</value></element><element><db-column>VALUE</db-column><value>HMV</value></element></collection></field></field-set><field-set><container-name>address</container-name><container-id>null</container-id><db-table>USER_ADDRESSES</db-table><field><control-name>street</control-name><ui-label>null</ui-label><db-column>DE_A_1</db-column><value>Park Avenue</value></field><field><control-name>city</control-name><ui-label>null</ui-label><db-column>DE_A_2</db-column><value>London</value></field><field><control-name>parkingFacilities</control-name><ui-label>null</ui-label><collection><db-table>PARKING_FACILITIES</db-table></collection></field></field-set></field></field-set></form-submit>";

	protected FormAuditManagerImpl formAuditMgr;

	protected FormData formData;

}
