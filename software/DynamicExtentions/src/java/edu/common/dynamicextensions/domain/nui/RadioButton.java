
package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RadioButton extends SelectControl implements Serializable {
	private static final long serialVersionUID = -1336506050252009947L;
	
	private int optionsPerRow = 3;

	public int getOptionsPerRow() {
		return optionsPerRow;
	}

	public void setOptionsPerRow(int optionsPerRow) {
		this.optionsPerRow = optionsPerRow;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + optionsPerRow;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		RadioButton other = (RadioButton) obj;
		if (optionsPerRow != other.optionsPerRow) {
			return false;
		}
		return true;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), getDbType()));
	}	
	
	@Override
	public void getProps(Map<String, Object> props) {
		super.getProps(props);
		props.put("type", "radiobutton");
	}
}
