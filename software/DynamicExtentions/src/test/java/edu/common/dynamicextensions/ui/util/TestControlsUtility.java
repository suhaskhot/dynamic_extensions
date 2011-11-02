
package edu.common.dynamicextensions.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestControlsUtility extends DynamicExtensionsBaseTestCase
{

	public void testConvertDateToStringWithPattern() throws ParseException
	{
		Date inputDate = new Date();
		String stringDate = ControlsUtility.convertDateToString(inputDate, "MM-dd-yyyy");
		SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
		Date outputDate = dateformat.parse(stringDate);
		if (!isEqualDate(inputDate, outputDate))
		{
			fail("testConvertDateToStringWithPattern--> failed, dates not equal after parsing.");
		}
	}

	private boolean isEqualDate(Date inputDate, Date outputDate)
	{
		boolean isEqual = false;
		Calendar inputCal = Calendar.getInstance();
		inputCal.setTime(inputDate);
		Calendar outputCal = Calendar.getInstance();
		outputCal.setTime(outputDate);
		if (inputCal.get(Calendar.DAY_OF_MONTH) == outputCal.get(Calendar.DAY_OF_MONTH)
				&& inputCal.get(Calendar.MONTH) == outputCal.get(Calendar.MONTH)
				&& inputCal.get(Calendar.YEAR) == outputCal.get(Calendar.YEAR))
		{
			isEqual = true;
		}
		return isEqual;
	}

	public void testConvertDateToStringWithoutPattern() throws ParseException
	{
		Date inputDate = new Date();
		String stringDate = ControlsUtility.convertDateToString(inputDate, null);
		SimpleDateFormat dateformat = new SimpleDateFormat(ProcessorConstants.DATE_ONLY_FORMAT,
				Locale.getDefault());
		Date outputDate = dateformat.parse(stringDate);
		if (!isEqualDate(inputDate, outputDate))
		{
			fail("testConvertDateToStringWithoutPattern--> failed, dates not equal after parsing.");
		}
	}

	public void testGetFormattedDate()
	{
		Date inputDate = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date outputDate = ControlsUtility.getFormattedDate(dateformat.format(inputDate));
		if (!isEqualDate(inputDate, outputDate))
		{
			fail("testGetFormattedDate--> failed, dates not equal after parsing.");
		}
	}

	public void testGetFormattedDateNullValue()
	{
		Date outputDate = ControlsUtility.getFormattedDate(null);
		if (outputDate != null)
		{
			fail("testGetFormattedDate--> failed, date returned for invalid input.");
		}
	}

	public void testFormatInvalidDate()
	{
		Date outputDate = ControlsUtility.getFormattedDate("abcdefgh");
		if (outputDate != null)
		{
			fail("testFormatInvalidDate--> failed, date returned for invalid input");
		}

	}

	public void testGetControlCaption() throws DynamicExtensionsSystemException
	{
		String caption = ControlsUtility.getControlCaption(ControlConfigurationsFactory
				.getInstance().getControlDisplayLabel("DateControl"));
		if (!caption.equals("Date Picker"))
		{
			fail("testGetControlCaption--> failed, wrong caption returned for DateControl");
		}

	}

	public void testGetChildList()
	{
		try
		{
			CategoryInterface category = CategoryManager.getInstance().getCategoryById(
					getCategoryIdentifier());
			ContainerInterface container = (ContainerInterface) category.getRootCategoryElement()
					.getContainerCollection().iterator().next();
			List childList = ControlsUtility.getChildList(container);
			if (childList.size() != container.getControlCollection().size())
			{
				fail("testGetChildList --> failed, size of the child controls are different");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetChildList --> failed, size of the child controls are different");
		}

		// retireve one container  & give this container to this method.
		//number of controls in this container will be the child list should contain.
	}
}
