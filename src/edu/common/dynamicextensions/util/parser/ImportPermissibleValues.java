package edu.common.dynamicextensions.util.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
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
public class ImportPermissibleValues {
	
	private CategoryCSVFileParser categoryCSVFileParser;

	private static final String ENTITY_GROUP = "Entity_Group";

	/**
	 * 
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 * @throws FileNotFoundException 
	 */
	public ImportPermissibleValues(String filePath)
			throws DynamicExtensionsSystemException, FileNotFoundException {
		
		this.categoryCSVFileParser = new CategoryCSVFileParser(filePath); 
	}

	/**
	 * 
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	public void importValues() throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException {

		CategoryHelperInterface categoryHelper = new CategoryHelper();

		try {
			while (categoryCSVFileParser.readNext()) {
				//first line in the categopry file is Category_Definition
				if (ENTITY_GROUP.equals(categoryCSVFileParser.readLine()[0])) {
					continue;
				}
				//1:read the entity group
				EntityGroupInterface entityGroup = DynamicExtensionsUtility
						.retrieveEntityGroup(categoryCSVFileParser.getEntityGroupName());

				EntityInterface currentEntity = null;
				while (categoryCSVFileParser.readNext()) {
					if (ENTITY_GROUP.equals(categoryCSVFileParser.readLine()[0])) {
						break;
					}
					String entityName = categoryCSVFileParser.getEntityName();
					currentEntity = entityGroup.getEntityByName(entityName);
			
					String attributeName = categoryCSVFileParser.getAttributeName();
					
					List<String> pvList = categoryCSVFileParser.getPermissibleValues();
					List<PermissibleValueInterface> list = categoryHelper.createPermissibleValuesList
					(currentEntity,attributeName, pvList);
					
					AttributeTypeInformationInterface attributeTypeInformation = currentEntity.getAttributeByName
						(attributeName).getAttributeTypeInformation();
					UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface) attributeTypeInformation.
						getDataElement();
					
					if(userDefinedDE == null)
					{
						userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
						attributeTypeInformation.setDataElement(userDefinedDE);
						
					}
					
					for(PermissibleValueInterface pv: list)
					{
							userDefinedDE.addPermissibleValue(pv);	
					}

					//set the first value in the list as the default value
					attributeTypeInformation.setDefaultValue(list.get(0));
					
				}
				EntityGroupManager.getInstance().persistEntityGroup(entityGroup);
				
			}
		} catch (IOException e) {
			throw new DynamicExtensionsSystemException("Line number:"+ categoryCSVFileParser.getLineNumber()+"Error while reading csv file " 
					+ categoryCSVFileParser.getFilePath(), e);
		}

	}

	public static void main(String args[]) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			/*if (args.length == 0)
			{
				throw new Exception("Please Specify the path for .csv file");
			}*/
			String filePath = "E:/ClinPortal/models/3-24-08/referringinfo_pv.csv";//args[0];
			System.out.println("---- The .csv file path is " + filePath + " ----");
			ImportPermissibleValues importPermissibleValues = new ImportPermissibleValues(filePath);
			importPermissibleValues.importValues();
			System.out.println("Added permissible values successfully!!!!");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

	}

}
