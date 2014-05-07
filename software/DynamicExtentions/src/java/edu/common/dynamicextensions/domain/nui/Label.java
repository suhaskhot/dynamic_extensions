
package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Label extends Control implements Serializable {
	private static final long serialVersionUID = 5717603046743274148L;

	private boolean note;

	private boolean heading;

	public boolean isNote() {
		return note;
	}

	public void setNote(boolean note) {
		this.note = note;
	}

	public boolean isHeading() {
		return heading;
	}

	public void setHeading(boolean heading) {
		this.heading = heading;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.emptyList();
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (heading ? 1231 : 1237);
		result = prime * result + (note ? 1231 : 1237);
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
		
		Label other = (Label) obj;
		if (heading != other.heading || note != other.note) {
			return false;
		}
		
		return true;
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}
}
