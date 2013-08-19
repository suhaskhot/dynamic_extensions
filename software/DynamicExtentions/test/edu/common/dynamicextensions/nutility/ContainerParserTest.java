package edu.common.dynamicextensions.nutility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.DatePicker.DefaultDateType;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvVersion;
import edu.common.dynamicextensions.domain.nui.RadioButton;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.TextArea;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContainerParser.class, Container.class})
public class ContainerParserTest {

	protected String path;

	protected String pvDir;
		
	@Before
	public void setUp() {
		setUpFormControls();
		
		Class<?> klass = getClass();
		String pkg = klass.getPackage().getName().replaceAll("\\.", File.separator);

		path = klass.getClassLoader().getResource(pkg).getPath().concat(File.separator);
		path = path.replaceAll("%20", " ").concat("parsertestdata").concat(File.separator);
		pvDir = path + "pvs";
	}
	
	@Test 
	public void testParseSimpleContainer() 
	throws Exception {		
		addControl(personProfile, formHeading,  1, 1);
		addControl(personProfile, firstName,    2, 1);
		addControl(personProfile, lastName,     2, 2);
		addControl(personProfile, dateOfBirth,  3, 1);
		addControl(personProfile, formNote,     4, 1);

		String formXml = path + "SimpleForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container actualContainer = parser.parse();
	
		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}
	

	@Test
	public void testParseContainerWithSelectCtrlsAndInlinePvs()
	throws Exception {		
		addControl(personProfile, gender,          1, 1);
		addControl(personProfile, sourcesOfIncome, 2, 1);
		addControl(personProfile, consent,         3, 1);

		String formXml = path + "SelectControlsWithInlinePvsForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container actualContainer = parser.parse();
	
		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}

	@Test
	public void testParseContainerWithCalculatedControl() 
	throws Exception {		
		addControl(personProfile, firstName,  1, 1);
		addControl(personProfile, lastName,   1, 2);
		addControl(personProfile, income,     2, 1);
		addControl(personProfile, loanAmount, 3, 1);
		
		//
		// Calculated attribute specific values
		//
		loanAmount.setCalculated(true);
		income.setCalculatedSourceControl(true);
		
		String formXml = path + "CalculatedControlForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container actualContainer = parser.parse();
	
		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}
	
	@Test 
	public void testParseContainerWithSelectCtrlsAndFilePvs()
	throws Exception {	
		addControl(personProfile, formHeading,     1, 1);
		addControl(personProfile, gender,          2, 1);
		addControl(personProfile, sourcesOfIncome, 3, 1);
		addControl(personProfile, bankAccounts,    4, 1);

		String formXml = path + "SelectControlsWithFilePvsForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container actualContainer = parser.parse();
			
		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}

	@Test
	public void testParseContainerWithSubForm()
	throws Exception {		
		addControl(personProfile, formHeading,     1, 1);
		addControl(personProfile, firstName,       2, 1);
		addControl(personProfile, lastName,        2, 2);
		addControl(personProfile, sourcesOfIncome, 3, 1);
		addControl(personProfile, addressSf,       4, 1);
		addControl(address,       city,            1, 1);
		addControl(address,       company,         2, 1);
		
		String formXml = path + "SubForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container actualContainer = parser.parse();
	
		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}
	

	@Test
	public void testParseContainerWithSkipLogic() 
	throws Exception {
		
		addControl(personProfile, formHeading,     1, 1);
		addControl(personProfile, gender,          2, 1);
		addControl(personProfile, sourcesOfIncome, 3, 1);
		addControl(personProfile, dateOfBirth,     4, 1);
		addControl(personProfile, addressSf,       5, 1);
		addControl(address,       city,            1, 1);
		addControl(address,       company,         2, 1);
		
		addSkipLogicProps();

		String formXml = path + "SkipLogicForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container actualContainer = parser.parse();
	
		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}
	
	private void addSkipLogicProps() {
		//
		// Add a skip rules
		// gender = Male then 
		//    -> hide sourcesOfIncome
		//    -> show selected pvs for city                         
		//
		SkipRule rule = personProfile.newSkipRule()
			.when().anyOf()
			.eq(gender.getName(), "Male")
			.then().perform()
			.hide("sourcesOfIncome")
			.subsetPv("address.city", getPvs("Paris", "Rome", "New York"), null)
			.then().get();
		personProfile.addSkipRule(rule);	
	}

	protected void setUpFormControls() {
		personProfile = new Container();
		personProfile.useAsDto();																																																																																																																																																																																																																																																																																																																																																																																																																																								
		personProfile.setName("PersonProfile");
		personProfile.setCaption("Person Profile");
		
		formHeading = new Label();
		formHeading.setName("FormHeading");
		formHeading.setHeading(true);
		formHeading.setCaption("Fill in correct personal details for loan processing");
		
		firstName = new StringTextField();
		firstName.setName("firstName");
		firstName.setCaption("First Name");
		firstName.setPhi(true);
		firstName.setMandatory(true);
		firstName.setNoOfColumns(15);
		firstName.setToolTip("Enter your given name as it appears on passport");
				
		lastName = new StringTextField();
		lastName.setName("lastName");
		lastName.setCaption("Last Name");
		lastName.setPhi(true);
		lastName.setMandatory(true);
		lastName.setNoOfColumns(15);
		lastName.setToolTip("Enter your surname as it appears on passport");
				
		gender = new RadioButton();
		gender.setName("gender");
		gender.setCaption("Gender");
		gender.setPhi(true);
		gender.setMandatory(true);
		gender.setPvDataSource(getPvDataSource(
				DataType.STRING, "Male", "Female", "Unknown"));
		
		income = new NumberField();
		income.setName("income");
		income.setCaption("Annual Income");
		income.setMinValue("100000");
		income.setMaxValue("10000000");
		income.setNoOfDigits(19);
	
		loanAmount = new NumberField();
		loanAmount.setName("loanAmount");
		loanAmount.setCaption("Eligible Loan Amount");
		loanAmount.setFormula("income * 5");
		loanAmount.setNoOfDigits(19);
	
		sourcesOfIncome = new MultiSelectCheckBox();
		sourcesOfIncome.setName("sourcesOfIncome");
		sourcesOfIncome.setCaption("Sources of Income");
		sourcesOfIncome.setPvDataSource(getPvDataSource(
				DataType.STRING, 
				"Salary", "Agriculture", "Gambling", "Organized Sector Business", "Others"));
					
		description = new TextArea();
		description.setName("description");
		description.setCaption("Introduction");
		description.setPhi(true);
		description.setMandatory(true);
		description.setNoOfColumns(30);
		description.setNoOfRows(5);
		description.setToolTip("Explain briefly about your other characteristics");
		

		bankAccounts = new MultiSelectListBox();
		bankAccounts.setName("bankAccounts");
		bankAccounts.setCaption("Select banks where you have maintained savings account");		
		bankAccounts.setMandatory(true);
		bankAccounts.setPvDataSource(getPvDataSource(DataType.STRING, 
				"SBI", "SBM", "Canara", "Corp Bank", "Vijaya Bank", "Syndicate Bank"));
	
		dateOfBirth = new DatePicker();
		dateOfBirth.setName("dateOfBirth");
		dateOfBirth.setCaption("Date Of Birth");
		dateOfBirth.setPhi(true);
		dateOfBirth.setMandatory(true);
		dateOfBirth.setFormat("MM-dd-yyyy");
		dateOfBirth.setDefaultDateType(DefaultDateType.CURRENT_DATE);
		
		formNote = new Label();
		formNote.setName("FormNote");
		formNote.setNote(true);
		formNote.setCaption("Information filled here may be used for marketing purpose");
					
		address = new Container();
		address.setName("address");
		address.setCaption("Address");
		address.useAsDto();
		
		addressSf = new SubFormControl();
		addressSf.setName("address");
		addressSf.setCaption("Address");
		addressSf.setSubContainer(address);
		addressSf.setShowAddMoreLink(true);
		addressSf.setNoOfEntries(0);
				
		city = new ComboBox();
		city.setName("city");
		city.setCaption("City");
		city.setMandatory(true);
		city.setPvDataSource(getPvDataSource(
				DataType.STRING, "Hubli", "Dharwad", "Belgaum"));

		company = new StringTextField();
		company.setName("Company");
		company.setCaption("Company");
		company.setPhi(true);
		company.setMandatory(true);
		company.setToolTip("Enter your Company Name");
		company.setNoOfColumns(15);
		
		consent = new CheckBox();
		consent.setName("consent");
		consent.setCaption("I agree to terms and conditions");		
	}
	
	protected PvDataSource getPvDataSource(DataType dataType, String... pvStrs) {
		PvVersion pvVersion = new PvVersion();
		pvVersion.setPermissibleValues(getPvs(pvStrs));		
		
		List<PvVersion> pvVersions = new ArrayList<PvVersion>();
		pvVersions.add(pvVersion);
		
		PvDataSource pvDs = new PvDataSource();
		pvDs.setDataType(dataType);
		pvDs.setPvVersions(pvVersions);
		return pvDs;		
	}
	
	protected List<PermissibleValue> getPvs(String... pvStrs) {
		List<PermissibleValue> pvs = new ArrayList<PermissibleValue>();
		for (String pvStr : pvStrs) {
			PermissibleValue pv = new PermissibleValue();
			pv.setOptionName(pvStr);
			pv.setValue(pvStr);
			pvs.add(pv);			
		}
		
		return pvs;		
	}

	protected void addControl(Container container, Control ctrl, int seqNo, int xPos) {
		ctrl.setSequenceNumber(seqNo);
		ctrl.setxPos(xPos);
		container.addControl(ctrl);		
	}
	
	//
	// Declaring required Containers & Controls
	//
	
	private Container personProfile;
	
	private Container address;
	
	private Label formHeading;
	
	private Label formNote;

	private StringTextField firstName;
	
	private StringTextField lastName;
	
	private StringTextField company;
	
	private NumberField income;
	
	private NumberField loanAmount;
	
	private TextArea description;

	private RadioButton gender;
	
	private MultiSelectCheckBox sourcesOfIncome;
	
	private MultiSelectListBox bankAccounts;
	
	private ComboBox city;
	
	private CheckBox consent;
	
	private DatePicker dateOfBirth;
	
	private SubFormControl addressSf;	
}
