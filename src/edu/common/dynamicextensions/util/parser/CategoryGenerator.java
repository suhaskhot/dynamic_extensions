
package edu.common.dynamicextensions.util.parser;

import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.DISPLAY_LABLE;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.FORM_DEFINITION;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.OPTIONS;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.PERMISSIBLE_VALUES;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.PERMISSIBLE_VALUES_FILE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.CategoryHelper;
import edu.common.dynamicextensions.util.CategoryHelperInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.CategoryHelperInterface.ControlEnum;

/**
 * @author kunal_kamble
 * This class creates the catgory/categories defined in 
 * the csv file.
 */
public class CategoryGenerator extends CSVFileReader
{
	private static final String SET = "set";
	
	/**
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 */
	public CategoryGenerator(String filePath) throws DynamicExtensionsSystemException
	{
		super(filePath);
	}

	/**
	 * @param string
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private Map<String, List<String>> getPaths(String[] string) throws DynamicExtensionsSystemException
	{
		Map<String, List<String>> entityNamePath = new LinkedHashMap<String, List<String>>();

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

	/**
	 * @param paths
	 * @param entityGroup
	 * @return
	 */
	private Map<String, List<String>> getAssociationList(Map<String, List<String>> paths, EntityGroupInterface entityGroup)
	{
		Map<String, List<String>> listOfPath = new HashMap<String, List<String>>();

		Set<String> entityNames = paths.keySet();
		for (String entityName : entityNames)
		{
			
			List<String> list = getRelativePath(paths.get(entityName),listOfPath);
			List<String> assocaitionList = new ArrayList<String>();
			Iterator<String> entityNamesIterator = list.iterator();
			
			String sourceEntityName = entityNamesIterator.next();
			EntityInterface sourcEntity = entityGroup.getEntityByName(sourceEntityName);
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

	/**
	 * This method gets the relative path.
	 * @param entityNameList ordered entities names in the path
	 * @param pathMap 
	 * @return
	 */
	private List<String> getRelativePath(List<String> entityNameList, Map<String, List<String>> pathMap)
	{
		List<String> newEntityNameList = new  ArrayList<String>();
		String lastProcessedEntityName = null;
		for(String entityName: entityNameList)
		{
			if(pathMap.get(entityName)== null)
			{
				newEntityNameList.add(entityName);
			}
			else
			{
				lastProcessedEntityName = entityName;
			}
		}
		if(lastProcessedEntityName != null && !newEntityNameList.contains(lastProcessedEntityName))
		{
			newEntityNameList.add(0, lastProcessedEntityName);	
		}
		
		return newEntityNameList;
	}

	/**
	 * @param strings
	 * @return
	 */
	private String getEntityName(String[] strings)
	{
		String entityName = strings[0].split(":")[0];
		return entityName;
	}

	/**
	 * @param strings
	 * @return
	 */
	private String getAttributeName(String[] strings)
	{
		String entityName = strings[0].split(":")[1];
		return entityName;
	}

	/**
	 * @param strings
	 * @return
	 */
	private String getControlType(String[] strings)
	{
		return strings[1];
	}

	/**
	 * @param strings
	 * @return
	 */
	private String getControlCaption(String[] strings)
	{
		return strings[2];
	}

	/**
	 * @param strings
	 * @return
	 */
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

	/**
	 * @param nextLine
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
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

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
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
				List<String> formNameList = new ArrayList<String>();

				//5: get the selected attributes and create the controls for them 
				String displyLabel = null;
				String currentEntityName = null;
				Boolean showCaption = null;

				while ((nextLine = getNextLine(reader)) != null)
				{
					if (FORM_DEFINITION.equals(nextLine[0]))
					{
						break;
					}
					if(nextLine[0].startsWith(DISPLAY_LABLE))
					{
						displyLabel = nextLine[0].split(":")[1];
						showCaption = new Boolean(nextLine[1].split("=")[1]);
						nextLine = getNextLine(reader);
					}
					

					if (nextLine[0].contains("subcategory:"))
					{
						String sourceContainerCaption = displyLabel;

						if (getContainer(containerCollection, sourceContainerCaption) == null)
						{
							entityInterface = getEntity(sourceContainerCaption, entityGroup);
							containerInterface = createCategoryEntityAndContainer(entityInterface, displyLabel, showCaption, containerCollection,
									formNameList);
							containerCollection.add(containerInterface);
						}
						ContainerInterface sourceContainer = getContainer(containerCollection, sourceContainerCaption);

						String targetContainerCaption = nextLine[0].split(":")[1];
						ContainerInterface targetContainer = getContainer(containerCollection, targetContainerCaption);

						String multiplicity = nextLine[0].split(":")[2];

						List<String> associationNameList = getAssociationNameList(associationNamesMap,getContainer(
								containerCollection,targetContainerCaption)); 
						checkForNullRefernce(associationNameList, "Line No:" + lineNumber+ " Does not found entities in the path " +
										"for the category entity " + targetContainerCaption);

						categoryHelper.associateCategoryContainers(sourceContainer, targetContainer, 
								associationNameList,getMultiplicityInNumbers(multiplicity));

					}
					else
					{
						if (!formNameList.contains(displyLabel))
						{
							currentEntityName = getEntityName(nextLine);
							entityInterface = getEntity(currentEntityName, entityGroup);
							containerInterface = createCategoryEntityAndContainer(entityInterface, displyLabel, showCaption, containerCollection,
									formNameList);

						}

						//add control to the container
						List<String> permissibleValues = getPermissibleValues(nextLine);

						ControlInterface control;
						String attributeName = getAttributeName(nextLine);
						checkForNullRefernce(entityInterface.getAttributeByName(attributeName), "Line Number:" + lineNumber + " attribute with name "
								+ attributeName + " in the entity " + entityInterface.getName() + " does not exist!");

						if (permissibleValues != null)
						{
							control = categoryHelper.addControl(entityInterface, attributeName, containerInterface, ControlEnum
									.get(getControlType(nextLine)), getControlCaption(nextLine), permissibleValues);
						}
						else
						{
							checkForNullRefernce(ControlEnum.get(getControlType(nextLine)), "Line No:" + lineNumber + " Illegal control type "
									+ getControlType(nextLine));
							control = categoryHelper.addControl(entityInterface, attributeName, containerInterface, ControlEnum
									.get(getControlType(nextLine)), getControlCaption(nextLine));
						}

						setControlsOptions(control, nextLine);

					}
				}

				ContainerInterface rootContainer = getRootContainer(containerCollection, associationNamesMap, paths);
				
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

	/**
	 * @param associationNamesMap
	 * @param containerInterface
	 * @return
	 */
	private List<String> getAssociationNameList(Map<String, List<String>> associationNamesMap, 
			ContainerInterface containerInterface)
	{
		CategoryEntityInterface categoryEntityInterface = (CategoryEntityInterface) containerInterface.getAbstractEntity();
		return associationNamesMap.get(categoryEntityInterface.getEntity().getName());
		
	}

	/**
	 * @param entityName
	 * @param entityGroup
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private EntityInterface getEntity(String entityName, EntityGroupInterface entityGroup) throws DynamicExtensionsSystemException
	{
		EntityInterface entityInterface = entityGroup.getEntityByName(entityName);
		checkForNullRefernce(entityInterface, "Entity with name " + entityName + " at line " + lineNumber + " does not exist");

		return entityInterface;

	}

	/**
	 * @param multiplicity
	 * @return
	 */
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

	/**
	 * @param containerCollection
	 * @param absolutePath 
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	private ContainerInterface getRootContainer(List<ContainerInterface> containerCollection, 
			Map<String, List<String>> paths, Map<String, List<String>> absolutePath) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		
		
		ContainerInterface rootContainer = null;
		for(ContainerInterface containerInterface: containerCollection)
		{
			CategoryEntityInterface categoryEntityInterface = (CategoryEntityInterface) containerInterface.getAbstractEntity();
			if(categoryEntityInterface.getParentCategoryEntity() == null)
			{
				rootContainer = containerInterface;
				break;
			}
		}
		
		for(String entityName: absolutePath.keySet())
		{
			if(absolutePath.get(entityName).size() == 1)
			{
				CategoryEntityInterface categoryEntityInterface = (CategoryEntityInterface) 
				rootContainer.getAbstractEntity();
				if(!entityName.equals(categoryEntityInterface.getEntity().getName()))
				{
					EntityGroupInterface entityGroup = categoryEntityInterface.getEntity().getEntityGroup();
					EntityInterface entity = getEntity(entityName, entityGroup);
					
					CategoryHelperInterface categoryHelper = new CategoryHelper();
					ContainerInterface newRootContainer = categoryHelper.createCategoryEntityAndContainer
					(entity, null);
					newRootContainer.setAddCaption(false);
					
					categoryHelper.associateCategoryContainers(newRootContainer, rootContainer,
							paths.get(categoryEntityInterface.getEntity().getName()), 1);
					
					rootContainer = newRootContainer;
					
					
				}
			}
		}
		return rootContainer;
	}

	/**
	 * @param containerCollection
	 * @param containerCaption
	 * @return
	 */
	private ContainerInterface getContainer(List<ContainerInterface> containerCollection, String containerCaption)
	{
		ContainerInterface container = null;
		for (ContainerInterface containerInterface : containerCollection)
		{
			if (containerCaption.equals(containerInterface.getCaption()))
			{
				container = containerInterface;
				break;
			}

		}
		return container;
	}

	/**
	 * @param entityInterface
	 * @param displyLabel
	 * @param showCaption
	 * @param containerCollection
	 * @param formNameList
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private ContainerInterface createCategoryEntityAndContainer(EntityInterface entityInterface, String displyLabel, Boolean showCaption,
			Collection<ContainerInterface> containerCollection, List<String> formNameList) throws DynamicExtensionsSystemException
	{
		ContainerInterface containerInterface = null;
		CategoryHelperInterface categoryHelper = new CategoryHelper();

		containerInterface = categoryHelper.createCategoryEntityAndContainer(entityInterface, entityInterface.getName()
				+ " Category Entity Container");
		
		containerInterface.setCaption(displyLabel);
		containerInterface.setAddCaption(showCaption);

		formNameList.add(displyLabel);
		containerCollection.add(containerInterface);

		return containerInterface;
	}

	/**
	 * @param object
	 * @param message
	 * @throws DynamicExtensionsSystemException
	 */
	private void checkForNullRefernce(Object object, String message) throws DynamicExtensionsSystemException
	{
		if (object == null)
		{
			throw new DynamicExtensionsSystemException(message);
		}
	}

	/**
	 * @param control
	 * @param nextLine
	 * @throws DynamicExtensionsSystemException
	 */
	private void setControlsOptions(ControlInterface control, String[] nextLine) throws DynamicExtensionsSystemException
	{
		try
		{
			String[] controlOptions = null;
			for (String string : nextLine)
			{
				if (string.startsWith(OPTIONS + "~"))
				{
					controlOptions = string.split("~")[1].split(":");
				}
			}

			if (controlOptions == null)
			{
				return;
			}
			for (String optionValue : controlOptions)
			{
				String optionString = optionValue.split("=")[0];
				String methodName = SET + optionString;

				Class[] types = getParameterType(methodName, control);
				List<Object> values = new ArrayList<Object>();
				values.add(getFormattedValues(types[0], optionValue.split("=")[1]));

				Method method;

				method = control.getClass().getMethod(methodName, types);

				method.invoke(control, values.toArray());

			}
		}
		catch (SecurityException e)
		{
			throw new DynamicExtensionsSystemException("Please conatct administartor", e);
		}
		catch (NoSuchMethodException e)
		{
			throw new DynamicExtensionsSystemException("Line number:" + lineNumber + "Incorrect option", e);
		}
		catch (IllegalArgumentException e)
		{
			throw new DynamicExtensionsSystemException("Please conatct administartor", e);
		}
		catch (IllegalAccessException e)
		{
			throw new DynamicExtensionsSystemException("Please conatct administartor", e);
		}
		catch (InvocationTargetException e)
		{
			throw new DynamicExtensionsSystemException("Please conatct administartor", e);
		}
		catch (InstantiationException e)
		{
			throw new DynamicExtensionsSystemException("Please conatct administartor", e);
		}

	}

	/**
	 * This meth
	 * @param methodName
	 * @param object
	 * @return
	 */
	private Class[] getParameterType(String methodName, Object object)
	{
		Class[] parameterTypes = new Class[0];
		for (Method method : object.getClass().getMethods())
		{
			if (methodName.equals(method.getName()))
			{
				parameterTypes = method.getParameterTypes();
			}
		}

		return parameterTypes;
	}

	/**
	 * @param type
	 * @param string
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	private Object getFormattedValues(Class type, String string) throws SecurityException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method method = type.getMethod("valueOf", new Class[]{String.class});
		return method.invoke(type, new Object[]{string});
	}

}