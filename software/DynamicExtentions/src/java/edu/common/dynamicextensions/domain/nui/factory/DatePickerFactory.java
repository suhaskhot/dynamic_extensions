package edu.common.dynamicextensions.domain.nui.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.DatePicker.DefaultDateType;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class DatePickerFactory extends AbstractControlFactory {

	public static DatePickerFactory getInstance() {
		return new DatePickerFactory();
	}
	
	@Override
	public String getType() {
		return "datePicker";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		DatePicker datePicker = new DatePicker();
		setControlProps(datePicker, ele, row, xPos);
		
		String format = getTextValue(ele, "format", "MM-dd-yyyy");
		datePicker.setFormat(format);
		datePicker.setShowCalendar(getBooleanValue(ele, "showCalendar", true));
		
		String defaultDate = getTextValue(ele, "default", "none");
		if (defaultDate.equals("none")) {
			datePicker.setDefaultDateType(DefaultDateType.NONE);
		} else if (defaultDate.equals("current_date")) {
			datePicker.setDefaultDateType(DefaultDateType.CURRENT_DATE);
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Date date = sdf.parse(defaultDate);
				datePicker.setDefaultDate(date);				
			} catch (Exception e) {
				throw new RuntimeException("Invalid default date: " + defaultDate);
			}
		}
		
		return datePicker;
	}
}
