package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class ListBoxFactory extends AbstractControlFactory {

	public static ListBoxFactory getInstance() {
		return new ListBoxFactory();
	}
		
	@Override
	public String getType() {
		return "listBox";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		ListBox listBox = null;
		
		boolean isMultiSelect = getBooleanValue(ele, "multiSelect", false);
		boolean autoComplete  = getBooleanValue(ele, "autoCompleteDropdown", false);

		if (!isMultiSelect && autoComplete) {
			throw new RuntimeException("Autocomplete dropdown cannot be used for non-multiselect listBox");
		}
		
		if (isMultiSelect) {
			MultiSelectListBox msLb = new MultiSelectListBox();						
			msLb.setTableName(getTextValue(ele, "table"));
			msLb.setParentKey(getTextValue(ele, "parentKey"));
			msLb.setForeignKey(getTextValue(ele, "foreignKey"));
			listBox = msLb;
		} else {
			listBox = new ListBox();
		}
		
		setSelectProps(listBox, ele, row, xPos, props.getProperty("pvDir"));
				
		Integer minQueryChars = getIntValue(ele, "minQueryChars", null);
		if (minQueryChars != null) {
			listBox.setMinQueryChars(minQueryChars);
		}
		
		Integer noOfRows = getIntValue(ele, "noOfRows", null);
		if (noOfRows != null) {
			listBox.setNoOfRows(noOfRows);
		}
				
		listBox.setAutoCompleteDropdownEnabled(autoComplete);		
		return listBox;	}

}
