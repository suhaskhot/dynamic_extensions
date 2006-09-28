package edu.common.dynamicextensions.domain.databaseproperties;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:47:21 PM
 */
public class ConstraintProperties extends DatabaseProperties {

	protected String sourceEntityKey;
	protected String targetEntityKey;

	public ConstraintProperties(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	

    /**
     * @return Returns the sourceEntityKey.
     */
    public String getSourceEntityKey() {
        return sourceEntityKey;
    }
    /**
     * @param sourceEntityKey The sourceEntityKey to set.
     */
    public void setSourceEntityKey(String sourceEntityKey) {
        this.sourceEntityKey = sourceEntityKey;
    }
    /**
     * @return Returns the targetEntityKey.
     */
    public String getTargetEntityKey() {
        return targetEntityKey;
    }
    /**
     * @param targetEntityKey The targetEntityKey to set.
     */
    public void setTargetEntityKey(String targetEntityKey) {
        this.targetEntityKey = targetEntityKey;
    }
}