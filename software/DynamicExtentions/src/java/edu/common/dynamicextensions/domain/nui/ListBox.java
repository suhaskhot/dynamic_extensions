
package edu.common.dynamicextensions.domain.nui;

public class ListBox extends SelectControl {

	private static final long serialVersionUID = 9190611588889454148L;

	private int noOfRows;
	
	private boolean autoCompleteDropdownEnabled;

	private int minQueryChar = 3;

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	
	public boolean isAutoCompleteDropdownEnabled() {
		return autoCompleteDropdownEnabled;
	}
	
	public void setAutoCompleteDropdownEnabled(boolean autoCompleteDropdownEnabled) {
		this.autoCompleteDropdownEnabled = autoCompleteDropdownEnabled;
	}
	
	public int getMinQueryChars() {
		return minQueryChar;
	}
	
	public void setMinQueryChars(int minQueryChars) {
		this.minQueryChar = minQueryChars;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfRows;
		result = prime * result + (autoCompleteDropdownEnabled ? 1231 : 1237);
		result = prime * result + minQueryChar;		
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
		
		ListBox other = (ListBox) obj;
		if (noOfRows != other.noOfRows ||
			autoCompleteDropdownEnabled != other.autoCompleteDropdownEnabled ||
			minQueryChar != other.minQueryChar) {		
			return false;
		}

		return true;
	}
}
