
package edu.common.dynamicextensions.util.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.AttributeTypeInformation;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.CategoryHelper;
import edu.common.dynamicextensions.util.CategoryHelperInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.validation.category.CategoryValidator;
import edu.wustl.common.bizlogic.DefaultBizLogic;

/**
 * @author kunal_kamble
 * This class the imports the permissible values from csv file into the database.
 *
 */
public class ImportPermissibleValues
{

	private CategoryCSVFileParser categoryCSVFileParser;

	private static final String ENTITY_GROUP = "Entity_Group";

	/**
	 *
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 * @throws FileNotFoundException
	 */
	public ImportPermissibleValues(String filePath) throws DynamicExtensionsSystemException, FileNotFoundException
	{
		this.categoryCSVFileParser = new CategoryCSVFileParser(filePath);
	}

	/**
	 *
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ParseException
	 */
	public void importValues() throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException, ParseException
	{
		CategoryHelperInterface categoryHelper = new CategoryHelper();

		try
		{
			while (categoryCSVFileParser.readNext())
			{
				//first line in the categopry file is Category_Definition
				if (ENTITY_GROUP.equals(categoryCSVFileParser.readLine()[0]))
				{
					continue;
				}
				//1:read the entity group
				String entityGroupIdHQL = "select id from AbstractMetadata am where am.name = '" +  categoryCSVFileParser.getEntityGroupName() + "'";
				List entityGroupIdList = DynamicExtensionsUtility.executeQuery(entityGroupIdHQL);
				
				CategoryValidator.checkForNullRefernce(entityGroupIdList.get(0), " ERROR AT LINE:" + categoryCSVFileParser.getLineNumber()
						+ " ENTITY GROUP WITH NAME " + categoryCSVFileParser.getEntityGroupName() + " DOES NOT");

				Long entityGroupId = (Long)entityGroupIdList.get(0);
				categoryCSVFileParser.getCategoryValidator().setEntityGroupId(entityGroupId);

				EntityInterface currentEntity = null;
				while (categoryCSVFileParser.readNext())
				{
					boolean overridePv = categoryCSVFileParser.isOverridePermissibleValues();
					if (ENTITY_GROUP.equals(categoryCSVFileParser.readLine()[0]))
					{
						break;
					}
					String entityName = categoryCSVFileParser.getEntityName();
								
					String entityHQL = "select id from Entity entity where entity.entityGroup.id = "+entityGroupId +" and entity.name = '" + entityName +"'";
					List entityIdList = DynamicExtensionsUtility.executeQuery(entityHQL);
										
					CategoryValidator.checkForNullRefernce(entityIdList.get(0), " ERROR AT LINE:" + categoryCSVFileParser.getLineNumber()
							+ " ENTITY WITH NAME " + entityName + " DOES NOT EXIST");

					Long entityId = (Long)entityIdList.get(0);
					
					String attributeName = categoryCSVFileParser.getAttributeName();

					Map<String, Collection<SemanticPropertyInterface>> pvList = categoryCSVFileParser.getPermissibleValues();
					Map<String, Collection<SemanticPropertyInterface>> finalPvList = new HashMap<String, Collection<SemanticPropertyInterface>>();
					
					String attributeHQL = "select id from AbstractAttribute absAttr where absAttr.entity.id = "+entityId +" and absAttr.name = '" + attributeName +"'";
					List attrIdList = DynamicExtensionsUtility.executeQuery(attributeHQL);
										
					CategoryValidator.checkForNullRefernce(attrIdList.get(0), " ERROR AT LINE:"
							+ categoryCSVFileParser.getLineNumber() + " ATTRIBUTE WITH NAME " + attributeName + " DOES NOT EXIST");

					Long attributeId = (Long)attrIdList.get(0);
										
					String attributeTypeHQL = "select attr.attributeTypeInformationCollection from Attribute attr where attr.id = "+attributeId;
					List attrTypeList = DynamicExtensionsUtility.executeQuery(attributeTypeHQL);
					
					AttributeTypeInformationInterface attributeTypeInformation = (AttributeTypeInformationInterface)attrTypeList.get(0);
		
					UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface) attributeTypeInformation.getDataElement();

					if (userDefinedDE == null)
					{
						userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
						attributeTypeInformation.setDataElement(userDefinedDE);
						finalPvList = pvList;
					}
					else
					{
						if (overridePv)
						{
							userDefinedDE.clearPermissibleValues();
						}

						List<String> list = new ArrayList<String>();
						for (PermissibleValueInterface permissibleValue : userDefinedDE.getPermissibleValueCollection())
						{
							list.add(permissibleValue.getValueAsObject().toString());
						}
						Set<String> pvListKeySet = pvList.keySet();
						for (String string : pvListKeySet)
						{
							if (!list.contains(string))
							{
								finalPvList.put(string, pvList.get(string));
							}
						}
					}

					List<PermissibleValueInterface> list = categoryHelper.getPermissibleValueList(attributeTypeInformation, finalPvList);
					
					userDefinedDE.addAllPermissibleValues(list);
									
					if (list != null && !list.isEmpty())
					{
						//set the first value in the list as the default value
						attributeTypeInformation.setDefaultValue(list.get(0));
					}
					EntityManager.getInstance().updateAttributeTypeInfo(attributeTypeInformation);
				}				
			}
		}
		catch (IOException e)
		{
			throw new DynamicExtensionsSystemException("FATAL ERROR AT LINE:" + categoryCSVFileParser.getLineNumber(), e);
		}
		catch (Exception e)
		{
			if (!(e instanceof DynamicExtensionsSystemException))
			{
				throw new DynamicExtensionsSystemException("FATAL ERROR AT LINE: " + categoryCSVFileParser.getLineNumber() + "READING FILE "
						+ categoryCSVFileParser.getFilePath(), e);
			}
			throw new DynamicExtensionsSystemException("", e);
		}

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
			System.out.println("Added permissible values successfully!!!!");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

	}

}
