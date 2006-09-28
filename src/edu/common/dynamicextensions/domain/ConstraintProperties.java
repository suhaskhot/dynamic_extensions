package edu.common.dynamicextensions.domain;
import edu.common.dynamicextensions.domain.databaseproperties.DatabaseProperties;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 */
public class ConstraintProperties extends DatabaseProperties {

	protected String sourceEntityKey;
	protected String targetEntityKey;

	public ConstraintProperties(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}