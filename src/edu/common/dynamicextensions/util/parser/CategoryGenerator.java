
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

	private long lineNumber = 0;

	private String rootEntityName;

	private static final String FORM_DEFINITION = "Form_Definition";

	private static final String DISPLAY_LABEL = "Display_Label";

	private static final String PERMISSIBLE_VALUES = "Permissible_Values";

	private static final String PERMISSIBLE_VALUES_FILE = "Permissible_Values_File";

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

	private String[] getNextLine(CSVReader reader) throws IOException
	{
		lineNumber++;
		return reader.readNext();
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

		//set the rooEntityName
		//read the first path given and the first entity in that path is the root
		rootEntityName = string[0].split("~")[1].split(":")[0];

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

			while ((nextLine = getNextLine(reader)) != null)
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
				nextLine = getNextLine(reader);
				EntityGroupInterface entityGroup = DynamicExtensionsUtility.retrieveEntityGroup(nextLine[0].trim());

				checkForNullRefernce(entityGroup, "Entity group with name " + nextLine[0].trim() + " at line number " + lineNumber
						+ " does not exist");

				//3:get the path represneted by ordered entity names
				String[] entityNamePath = getNextLine(reader);
				Map<String, List<String>> paths = getPaths(entityNamePath);

				//4:get the association names list
				Map<String, List<String>> associationNamesMap = getAssociationList(paths, entityGroup);

				List<ContainerInterface> containerCollection = new ArrayList<ContainerInterface>();

				ContainerInterface containerInterface = null;
				EntityInterface entityInterface = null;
				List<String> entityNamelist = new ArrayList<String>();

				//5: get the selected attributes and create the controls for them 
				String displyLabel = null;
				String currentEntityName =null;
				while ((nextLine = getNextLine(reader)) != null)
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

						nextLine = getNextLine(reader);
					}
					else
					{
						displyLabel = null;
					}

					if (nextLine[0].contains("subcategory:"))
					{
						String sourceEntityName = nextLine[0].split(":")[0];
						if(getContainer(containerCollection, sourceEntityName) == null)
						{
							containerCollection.add(createCategoryEntityAndContainer
									(entityGroup.getEntityByName(sourceEntityName), displyLabel));
						}
						ContainerInterface sourceContainer = getContainer(containerCollection, sourceEntityName);

						String targetEntityName = nextLine[0].split(":")[2];
						ContainerInterface targetContainer = getContainer(containerCollection, targetEntityName);

						String multiplicity = nextLine[0].split(":")[3];
						
						checkForNullRefernce(associationNamesMap.get(targetEntityName), 
								"Line No:"+lineNumber+" Does not found entities in the path for the category entity "+
								targetEntityName);
						
						categoryHelper.associateCategoryContainers(sourceContainer, targetContainer, associationNamesMap.get(targetEntityName),
								getMultiplicityInNumbers(multiplicity));

					}
					else
					{
						//Entity is not processed: create a new container for its category
						if (!entityNamelist.contains(getEntityName(nextLine)))
						{
							currentEntityName = getEntityName(nextLine);
							entityInterface = entityGroup.getEntityByName(currentEntityName);

							checkForNullRefernce(entityInterface, "Entity with name " + currentEntityName + " at line " + lineNumber
									+ " does not exist");

							entityNamelist.add(currentEntityName);

							containerInterface = createCategoryEntityAndContainer(entityInterface, displyLabel);
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
				}

				
				ContainerInterface rootContainer = getRootContainer(containerCollection);
				categoryHelper.setRootCategoryEntity(rootContainer, category);
		
				categoryList.add(category);
			}

		}

		catch (FileNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig CSV file ", e);
		}
		catch (IOException e)
		{
			throw new DynamicExtensionsSystemException("Error redaring CSV file " + fileName + " at line " + lineNumber, e);
		}
		return categoryList;

	}

	private int getMultiplicityInNumbers(String multiplicity)
	{
		int multiplicityI = 1;
		if (multiplicity.equalsIgnoreCase("multiline"))
		{
			multiplicityI = -1;
		}
		if (multiplicity.equalsIgnoreCase("single"))
		{
			multiplicityI = 1;
		}
		return multiplicityI;
	}

	private ContainerInterface getRootContainer(List<ContainerInterface> containerCollection)
	{
		return getContainer(containerCollection, rootEntityName);
	}

	private ContainerInterface getContainer(List<ContainerInterface> containerCollection, String entityName)
	{
		ContainerInterface container = null;
		for (ContainerInterface containerInterface : containerCollection)
		{
			CategoryEntityInterface categoryEntityInterface = (CategoryEntityInterface) containerInterface.getAbstractEntity();
			if (categoryEntityInterface.getEntity().getName().equals(entityName))
			{
				container = containerInterface;
			}

		}
		return container;
	}

	private ContainerInterface createCategoryEntityAndContainer(EntityInterface entityInterface, String displyLabel)
	{
		ContainerInterface containerInterface = null;
		CategoryHelperInterface categoryHelper = new CategoryHelper();
		if (displyLabel == null)
		{
			containerInterface = categoryHelper.createCategoryEntityAndContainer(entityInterface, entityInterface.getName()
					+ " Category Entity Container");
			containerInterface.setAddCaption(false);
		}
		else
		{
			containerInterface = categoryHelper.createCategoryEntityAndContainer(entityInterface, displyLabel);
		}
		return containerInterface;
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

/*	public static void main(String args[]) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, URISyntaxException,
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

	}
*/
}
