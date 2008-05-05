package edu.common.dynamicextensions.domain.databaseproperties;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_CONSTRAINT_PROPERTIES"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ConstraintProperties extends DatabaseProperties implements ConstraintPropertiesInterface{
    /**
     * The source entity key through which constarint is related.
     * e.g. Used in case of foreign key constraint in one to many relation.
     */
	protected String sourceEntityKey;
	/**
	 * The target entity key through which constraint is related.
	 * Used in case of many to many relation.Both source and target entity key is entered in the intermediate table.
	 */
	protected String targetEntityKey;
    /**
     * The source entity key through which constarint is related.
     * e.g. Used in case of foreign key constraint in one to many relation.
     */
	protected String sourceEntityKeyConstraintName;
	/**
	 * The target entity key through which constraint is related.
	 * Used in case of many to many relation.Both source and target entity key is entered in the intermediate table.
	 */
	protected String targetEntityKeyConstraintName;

	/**
	 * Empty constructor
	 *
	 */
	public ConstraintProperties(){

	}
    /**
     * @hibernate.property name="sourceEntityKey" type="string" column="SOURCE_ENTITY_KEY"
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
     * @hibernate.property name="targetEntityKey" type="string" column="TARGET_ENTITY_KEY"
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
    /**
     * @hibernate.property name="sourceEntityKeyConstraintName" type="string" column="SRC_CONSTRAINT_NAME"
     * @return Returns the targetEntityKey.
     */
	public String getSourceEntityKeyConstraintName()
	{
		return sourceEntityKeyConstraintName;
	}
	/**
	 *
	 * @param sourceEntityKeyConstraintName
	 */
	public void setSourceEntityKeyConstraintName(String sourceEntityKeyConstraintName)
	{
		this.sourceEntityKeyConstraintName = sourceEntityKeyConstraintName;
	}
    /**
     * @hibernate.property name="targetEntityKeyConstraintName" type="string" column="TARGET_CONSTRAINT_NAME"
     * @return Returns the targetEntityKey.
     */
	public String getTargetEntityKeyConstraintName()
	{
		return targetEntityKeyConstraintName;
	}
	/**
	 *
	 * @param targetEntityKeyConstraintName
	 */
	public void setTargetEntityKeyConstraintName(String targetEntityKeyConstraintName)
	{
		this.targetEntityKeyConstraintName = targetEntityKeyConstraintName;
	}

}