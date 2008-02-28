
package edu.common.dynamicextensions.util.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.CategoryHelper;
import edu.common.dynamicextensions.util.CategoryHelperInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.CategoryHelperInterface.ControlEnum;

public class CategoryGenerator
{
	private String fileName;

	private CSVReader reader;

	private static final String FORM_DEFINITION = "Form_Definition";

	private static final String DISPLAY_LABEL = "Display_Label";

	private static final String PERMISSIBLE_VALUES = "Permissible_Values";

	private static final String PERMISSIBLE_VALUES_FILE = "Permissible_Values_File";

	private static final String NOT_APPLICABLE = "N/A";

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String filePath)
	{
		this.fileName = filePath;
	}

	public CategoryGenerator(String filePath) throws DynamicExtensionsSystemException
	{
		this.fileName = filePath;
		try
		{
			reader = new CSVReader(new FileReader(getSystemIndependantFilePath(filePath)));
		}
		catch (FileNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig file " + filePath, e);
		}
	}

	private Map<String, List<String>> getPaths(String[] string) throws DynamicExtensionsSystemException
	{
		Map<String, List<String>> entityNamePath = new HashMap<String, List<String>>();

		for (String entityNameAndPath : string)
		{
			List<String> path = new ArrayList<String>();
			String entityName = entityNameAndPath.split("~")[0];

			StringTokenizer stringTokenizer = new StringTokenizer(entityNameAndPath.split("~")[1], ":");
			while (stringTokenizer.hasMoreTokens())
			{
				path.add(stringTokenizer.nextToken());
			}

			entityNamePath.put(entityName, path);
		}
		return entityNamePath;
	}

	private Map<String, List<String>> getAssociationList(Map<String, List<String>> paths, EntityGroupInterface entityGroup)
	{
		Map<String, List<String>> listOfPath = new HashMap<String, List<String>>();

		Set<String> entityNames = paths.keySet();
		for (String entityName : entityNames)
		{
			List<String> list = paths.get(entityName);
			List<String> assocaitionList = new ArrayList<String>();
			Iterator<String> entityNamesIterator = list.iterator();
			EntityInterface sourcEntity = entityGroup.getEntityByName(entityNamesIterator.next());
			while (entityNamesIterator.hasNext())
			{
				EntityInterface targetEntity = entityGroup.getEntityByName(entityNamesIterator.next());
				for (AssociationInterface associationInterface : sourcEntity.getAssociationCollection())
				{
					if (associationInterface.getTargetEntity() == targetEntity)
					{
						assocaitionList.add(associationInterface.getName());
					}
				}
				sourcEntity = targetEntity;

			}
			listOfPath.put(entityName, assocaitionList);
		}
		return listOfPath;
	}

	/*private Map<String, String> getContainerCaption(String[] strings)
	{
		Map<String, String> entityNameContainerCaptionMap = new HashMap<String, String>();
		for (String entityContainerString : strings)
		{
			entityNameContainerCaptionMap.put(entityContainerString.split(":")[0], entityContainerString.split(":")[1]);
		}
		return entityNameContainerCaptionMap;
	}
*/
	private String getEntityName(String[] strings)
	{
		String entityName = strings[0].split(":")[0];
		return entityName;
	}

	private String getAttributeName(String[] strings)
	{
		String entityName = strings[0].split(":")[1];
		return entityName;
	}

	private String getControlType(String[] strings)
	{
		return strings[1];
	}

	private String getControlCaption(String[] strings)
	{
		return strings[2];
	}

	private Map<String, String> getControlOptions(String[] strings)
	{
		String controlOptions = strings[3];
		String[] options = controlOptions.split(";");

		Map<String, String> optionValueMap = new HashMap<String, String>();
		for (String option : options)
		{
			String optionType = option.split(":")[0];
			String optionValue = option.split(":")[1];

			optionValueMap.put(optionType, optionValue);
		}
		return optionValueMap;

	}

	private List<String> getPermissibleValues(String[] nextLine) throws DynamicExtensionsSystemException
	{
		//counter for to locate the start of the permissible values
		int i;
		boolean permissibleValuesPresent = false;
		for (i = 0; i < nextLine.length; i++)
		{
			if (nextLine[i].startsWith(PERMISSIBLE_VALUES) || nextLine[i].startsWith(PERMISSIBLE_VALUES_FILE))
			{
				permissibleValuesPresent = true;
				break;
			}
		}
		if (!permissibleValuesPresent)
		{
			return null;
		}

		String[] tempString = nextLine[i].split("~");
		String permissibleValueKey = tempString[0];

		
		List<String> permissibleValues = new ArrayList<String>();
		if (PERMISSIBLE_VALUES.equals(permissibleValueKey))
		{
			String[] pv = tempString[1].split(":");
			for (i = 0; i < pv.length; i++)
			{
				permissibleValues.add(pv[i]);
			}
		}
		else if (PERMISSIBLE_VALUES_FILE.equals(permissibleValueKey))
		{
			String filePath = tempString[1];
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getSystemIndependantFilePath(filePath))));
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					permissibleValues.add(line.trim());
				}
			}
			catch (FileNotFoundException e)
			{
				throw new DynamicExtensionsSystemException("Error while reading permissible values file " + filePath, e);
			}
			catch (IOException e)
			{
				throw new DynamicExtensionsSystemException("Error while reading permissible values file " + filePath, e);
			}

		}
		return permissibleValues;
	}

	public List<CategoryInterface> getCategoryList() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		CategoryHelperInterface categoryHelper = new CategoryHelper();
		List<CategoryInterface> categoryList = new ArrayList<CategoryInterface>();
		try
		{
			String[] nextLine = null;

			while ((nextLine = reader.readNext()) != null)
			{
				//first line in the categopry file is Category_Definition
				if (FORM_DEFINITION.equals(nextLine[0]))
				{
					continue;
				}

				//category defination in the file starts
				//1: read the category name
				CategoryInterface category = categoryHelper.createCategory(nextLine[0]);

				//2:read the entity group
				nextLine = reader.readNext();
				EntityGroupInterface entityGroup = DynamicExtensionsUtility.retrieveEntityGroup(nextLine[0].trim());

				checkForNullRefernce(entityGroup, "Entity group with name " + nextLine[0].trim() + " does not exist");

				//3:get the path represneted by ordered entity names
				Map<String, List<String>> paths = getPaths(reader.readNext());

				//get the association names list
				Map<String, List<String>> associationNamesMap = getAssociationList(paths, entityGroup);

				List<ContainerInterface> containerCollection = new ArrayList<ContainerInterface>();

				ContainerInterface containerInterface = null;
				EntityInterface entityInterface = null;
				List<String> entityNamelist = new ArrayList<String>();

				//4: get the continer captions
				//Map<String, String> entityNameContainerCaptionMap = getContainerCaption(reader.readNext());

				//5: get the selected attributes and create the controls for them 
				String displyLabel = null;
				while ((nextLine = reader.readNext()) != null)
				{
					if (nextLine[0].length() == 0)
					{
						continue;
					}
					if (FORM_DEFINITION.equals(nextLine[0]))
					{
						break;
					}
					if (nextLine[0].startsWith(DISPLAY_LABEL))
					{
						displyLabel = nextLine[0].split(":")[1];

						nextLine = reader.readNext();
					}

					//Entity is not processed: create a new container for its category
					if (!entityNamelist.contains(getEntityName(nextLine)))
					{
						String entityName = getEntityName(nextLine);
						entityInterface = entityGroup.getEntityByName(entityName);

						checkForNullRefernce(entityInterface, "Entity with name " + entityName + " does not exist");

						entityNamelist.add(entityName);

						containerInterface = categoryHelper.createCategoryEntityAndContainer(entityInterface, 
								displyLabel);

						if (displyLabel == null)
						{
							containerInterface.setAddCaption(false);
						}

						containerCollection.add(containerInterface);
					}

					//add control to the container
					List<String> permissibleValues = getPermissibleValues(nextLine);

					if (permissibleValues != null)
					{
						categoryHelper.addControl(entityInterface, getAttributeName(nextLine), containerInterface, ControlEnum
								.get(getControlType(nextLine)), getControlCaption(nextLine), permissibleValues);
					}
					else
					{
						categoryHelper.addControl(entityInterface, getAttributeName(nextLine), containerInterface, ControlEnum
								.get(getControlType(nextLine)), getControlCaption(nextLine));
					}

				}

				//get the first entity in the path 
				String entityName = paths.get(entityNamelist.get(0)).get(0);

				// if entity is there in the path but no attributes are selected from it
				// then create the container for that entity
				if (!entityName.equals(entityNamelist.get(0)))
				{
					String caption = (displyLabel == null
							? entityName + " Category Container"
							: displyLabel);
					ContainerInterface rootContainer = categoryHelper.createCategoryEntityAndContainer(entityGroup.getEntityByName(entityName),
							caption);
					containerCollection.add(0, rootContainer);

				}

				categoryHelper.setRootCategoryEntity(containerCollection.get(0), category);

				Iterator<ContainerInterface> containerCollectionIterator = containerCollection.iterator();
				ContainerInterface sourceContainer = sourceContainer = containerCollectionIterator.next();

				while (containerCollectionIterator.hasNext())
				{
					ContainerInterface targetContainer = containerCollectionIterator.next();
					String key = ((CategoryEntityInterface) targetContainer.getAbstractEntity()).getEntity().getName();
					categoryHelper.associateCategoryContainers(sourceContainer, targetContainer, associationNamesMap.get(key), -1);

					sourceContainer = targetContainer;

				}
				categoryList.add(category);
			}

		}

		catch (FileNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig CSV file ", e);
		}
		catch (IOException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig CSV file ", e);
		}
		return categoryList;

	}

	private void checkForNullRefernce(Object object, String message) throws DynamicExtensionsSystemException
	{
		if (object == null)
		{
			throw new DynamicExtensionsSystemException(message);
		}
	}

	private String getSystemIndependantFilePath(String path) throws DynamicExtensionsSystemException
	{
		URI uri = null;
		try
		{
			uri = new URI("file:///" + path);
		}
		catch (URISyntaxException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig CSV file  " + path, e);
		}
		return uri.getPath();
	}

	public static void main(String args[]) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, URISyntaxException,
			IOException
	{
		CategoryGenerator categoryFileParser = new CategoryGenerator("e:/test.csv");
		CategoryHelper categoryHelper = new CategoryHelper();

		List<CategoryInterface> list = categoryFileParser.getCategoryList();
		for (CategoryInterface category : list)
		{
			categoryHelper.saveCategory(category);
			System.out.println("saved category " + category.getName());
		}
		System.out.println("hi");

		/*BufferedReader reader = new BufferedReader(new InputStreamReader
		 (new FileInputStream(categoryFileParser.getSystemIndependantFilePath("e:/pv.txt"))));
		 String newLine = "";
		 while((newLine = reader.readLine()) != null)
		 {
		 System.out.println(newLine);
		 }*/

	}

}
