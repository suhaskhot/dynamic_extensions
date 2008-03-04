package edu.common.dynamicextensions.util.parser;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;


/**
 * @author kunal_kamble
 * This class the imports the permissible values from csv file into the database.
 *
 */
public class ImportPermissibleValues extends CSVFileReader
{
	public ImportPermissibleValues(String filePath) throws DynamicExtensionsSystemException
	{
		super(filePath);
	}

}
