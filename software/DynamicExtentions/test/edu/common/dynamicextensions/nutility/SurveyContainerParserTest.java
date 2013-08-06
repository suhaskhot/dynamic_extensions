
package edu.common.dynamicextensions.nutility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.DatePicker.DefaultDateType;
import edu.common.dynamicextensions.domain.nui.Page;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SurveyContainer;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContainerParser.class, Container.class})
public class SurveyContainerParserTest {

	
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
	public void testParseSimpleContainer() throws Exception {
		personProfile.addPage(nameDetails);
		personProfile.addPage(personalDetails);

		addControl(personProfile, nameDetails, firstName, 1, 1);
		addControl(personProfile, nameDetails, lastName, 1, 2);
		addControl(personProfile, personalDetails, dateOfBirth, 1, 1);

		String formXml = path + "SurveyForm.xml";
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		SurveyContainer actualContainer = (SurveyContainer) parser.parse();

		assertNotNull(actualContainer);
		assertEquals(personProfile.toXml(), actualContainer.toXml());
	}

	private void addControl(SurveyContainer container, Page page, Control control, int sequenceNumber, int xPos) {
		page.addControl(control);
		control.setSequenceNumber(sequenceNumber);
		control.setxPos(xPos);

	}

	protected void setUpFormControls() {
		personProfile = new SurveyContainer();
		personProfile.setSequenceNo(-2);
		personProfile.setName("PersonProfile");
		personProfile.setCaption("Person Profile");

		firstName = new StringTextField();
		firstName.setName("firstName");
		firstName.setCaption("First Name");
		firstName.setPhi(true);
		firstName.setMandatory(true);
		firstName.setNoOfColumns(15);
		firstName.setLabelPosition(Control.LabelPosition.TOP);
		firstName.setToolTip("Enter your given name as it appears on passport");

		lastName = new StringTextField();
		lastName.setName("lastName");
		lastName.setCaption("Last Name");
		lastName.setPhi(true);
		lastName.setMandatory(true);
		lastName.setNoOfColumns(15);
		lastName.setLabelPosition(Control.LabelPosition.TOP);
		lastName.setToolTip("Enter your surname as it appears on passport");

		dateOfBirth = new DatePicker();
		dateOfBirth.setName("dateOfBirth");
		dateOfBirth.setCaption("Date Of Birth");
		dateOfBirth.setPhi(true);
		dateOfBirth.setMandatory(true);
		dateOfBirth.setFormat("MM-dd-yyyy");
		dateOfBirth.setDefaultDateType(DefaultDateType.CURRENT_DATE);
		dateOfBirth.setLabelPosition(Control.LabelPosition.TOP);

		nameDetails = new Page("nameDetails", "Name Details");
		personalDetails = new Page("personalDetails", "Personal Details");

	}

	//
	// Declaring required Containers & Controls
	//

	private SurveyContainer personProfile;

	private StringTextField firstName;

	private StringTextField lastName;

	private DatePicker dateOfBirth;

	private Page nameDetails;

	private Page personalDetails;

}