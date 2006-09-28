package edu.common.dynamicextensions.domain.validationrules;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class RuleParameter {

	protected Long id;
	protected String name;
	protected String value;

	public RuleParameter(){

	}

	public void finalize() throws Throwable {

	}
	
	

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}