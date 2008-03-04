package edu.common.dynamicextensions.util.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.CategoryHelper;
import edu.common.dynamicextensions.util.CategoryHelperInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * @author kunal_kamble
 * This class the imports the permissible values from csv file into the database.
 *
 */
public class ImportPermissibleValues extends CSVFileReader {

	private static final String ENTITY_GROUP = "Entity_Group";

	private static final String PERMISSIBLE_VALUES = "Permissible_Values";

	private static final String PERMISSIBLE_VALUES_FILE = "Permissible_Values_File";

	/**
	 * 
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 */
	public ImportPermissibleValues(String filePath)
			throws DynamicExtensionsSystemException {
		super(filePath);
	}

	/**
	 * 
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	public void importValues() throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException {

		String[] nextLine = null;
		CategoryHelperInterface categoryHelper = new CategoryHelper();

		try {
			while ((nextLine = getNextLine(reader)) != null) {
				//first line in the categopry file is Category_Definition
				if (ENTITY_GROUP.equals(nextLine[0])) {
					continue;
				}
				//1:read the entity group
				EntityGroupInterface entityGroup = DynamicExtensionsUtility
						.retrieveEntityGroup(nextLine[0].trim());

				List<String> entityNameList = new ArrayList<String>();
				EntityInterface currentEntity = null;
				while ((nextLine = getNextLine(reader)) != null) {
					if (ENTITY_GROUP.equals(nextLine[0])) {
						break;
					}

					String entityName = getEntityName(nextLine);
					if (!entityNameList.contains(entityName)) {
						currentEntity = entityGroup.getEntityByName(entityName);
					}

					String attributeName = getAttributeName(nextLine);
					List<String> pvList = getPermissibleValues(nextLine);
					List<PermissibleValueInterface> list = categoryHelper.createPermissibleValuesList
					(currentEntity,attributeName, pvList);
					
					UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface) currentEntity.getAttributeByName(attributeName).
					getAttributeTypeInformation().getDataElement();
					
					if(userDefinedDE == null)
					{
						userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
						currentEntity.getAttributeByName(attributeName).
						getAttributeTypeInformation().setDataElement(userDefinedDE);
					}
					
					for(PermissibleValueInterface pv: list)
					{
							userDefinedDE.addPermissibleValue(pv);	
							
					}
					
					
				}
				EntityGroupManager.getInstance().persistEntityGroup(entityGroup);
				//System.out.println("saved pvs");
			}
		} catch (IOException e) {
			throw new DynamicExtensionsSystemException("Line number:"+ lineNumber+"Error while csv file " 
					+ fileName, e);
		}

	}

	private String getAttributeName(String[] nextLine) {
		return nextLine[0].split(":")[1].trim();
	}

	private String getEntityName(String[] nextLine) {
		return nextLine[0].split(":")[0].trim();
	}

	/**
	 * @param nextLine
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private List<String> getPermissibleValues(String[] nextLine)
			throws DynamicExtensionsSystemException {
		//counter for to locate the start of the permissible values
		int i;
		boolean permissibleValuesPresent = false;
		for (i = 0; i < nextLine.length; i++) {
			if (nextLine[i].startsWith(PERMISSIBLE_VALUES)
					|| nextLine[i].startsWith(PERMISSIBLE_VALUES_FILE)) {
				permissibleValuesPresent = true;
				break;
			}
		}
		if (!permissibleValuesPresent) {
			return null;
		}

		String[] tempString = nextLine[i].split("~");
		String permissibleValueKey = tempString[0];

		List<String> permissibleValues = new ArrayList<String>();
		if (PERMISSIBLE_VALUES.equals(permissibleValueKey)) {
			String[] pv = tempString[1].split(":");
			for (i = 0; i < pv.length; i++) {
				permissibleValues.add(pv[i]);
			}
		} else if (PERMISSIBLE_VALUES_FILE.equals(permissibleValueKey)) {
			String filePath = tempString[1];
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(
								getSystemIndependantFilePath(filePath))));
				String line = null;
				while ((line = reader.readLine()) != null) {
					permissibleValues.add(line.trim());
				}
			} catch (FileNotFoundException e) {
				throw new DynamicExtensionsSystemException(
						"Error while reading permissible values file "
								+ filePath, e);
			} catch (IOException e) {
				throw new DynamicExtensionsSystemException(
						"Error while reading permissible values file "
								+ filePath, e);
			}

		}
		return permissibleValues;
	}
	
	
	public static void main(String args[]) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			if (args.length == 0)
			{
				throw new Exception("Please Specify the path for .csv file");
			}
			String filePath = args[0];
			System.out.println("---- The .csv file path is " + filePath + " ----");
			ImportPermissibleValues importPermissibleValues = new ImportPermissibleValues(filePath);
			importPermissibleValues.importValues();
		}
		catch (Exception ex)
		{
			System.out.println("Exception: " + ex.getMessage());
			throw new RuntimeException(ex);
		}

	}

}
