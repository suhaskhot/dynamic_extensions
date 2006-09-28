package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class ListBox extends Control {

	private Boolean isMultiSelect;

	public ListBox(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	

    /**
     * @return Returns the isMultiSelect.
     */
    public Boolean getIsMultiSelect() {
        return isMultiSelect;
    }
    /**
     * @param isMultiSelect The isMultiSelect to set.
     */
    public void setIsMultiSelect(Boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
    }
}