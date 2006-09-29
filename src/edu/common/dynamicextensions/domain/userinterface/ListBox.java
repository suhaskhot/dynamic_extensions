package edu.common.dynamicextensions.domain.userinterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_LIST_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ListBox extends Control {
	/**
	 * Boolean indicating whether multi selects are allowed in the list box.
	 */
	private Boolean isMultiSelect;

	public ListBox(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	

    /**
     * @hibernate.property name="isMultiSelect" type="boolean" column="MULTISELECT" 
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