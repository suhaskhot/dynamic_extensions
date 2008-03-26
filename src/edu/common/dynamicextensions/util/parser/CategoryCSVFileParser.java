
package edu.common.dynamicextensions.util.parser;

import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.DISPLAY_LABLE;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.FORM_DEFINITION;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.OPTIONS;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.PERMISSIBLE_VALUES;
import static edu.common.dynamicextensions.util.parser.CategoryCSVConstants.PERMISSIBLE_VALUES_FILE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author kunal_kamble
 */
public class CategoryCSVFileParser extends CategoryFileParser
{

	protected CSVReader reader;

	private String[] line;

	protected long lineNumber = 0;

	/**
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 * @throws FileNotFoundException 
	 */
	public CategoryCSVFileParser(String filePath) throws DynamicExtensionsSystemException, FileNotFoundException
	{
		super(filePath);
		reader = new CSVReader(new FileReader(filePath));

	}

	/**
	 * @return current line number
	 */
	public long getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * This method reads the next line 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public boolean readNext() throws IOException
	{
		//To skip the blank lines
		while ((line = reader.readNext()) != null)
		{
			lineNumber++;
			if (line[0].length() != 0)
			{
				break;
			}
		}

		if (line == null)
		{
			return false;
		}
		return true;

	}

	/**
	 * @return current line
	 */
	public String[] readLine()
	{
		return line;
	}
	
	/**
	 * @return paths
	 * @throws DynamicExtensionsSystemException
	 */
	public Map<String, List<String>> getPaths() throws DynamicExtensionsSystemException
	{
		Map<String, List<String>> entityNamePath = new LinkedHashMap<String, List<String>>();

		for (String entityNameAndPath : readLine())
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
	 * @return display label for the container
	 */
	public String getDisplyLable()
	{
		return readLine()[0].split(":")[1];
	}

	/**
	 * @return showCaption
	 */
	public boolean isShowCaption()
	{
		return new Boolean(readLine()[1].split("=")[1]);

	}

	public String[] getCategoryPaths()
	{
		readLine()[0] = readLine()[0].replace("instance:", "");
		return readLine();
	}

	
	/**
	 * @return entity name
	 */
	public String getEntityName()
	{
		return readLine()[0].split(":")[0];
	}

	/**
	 * @return attribute name
	 */
	public String getAttributeName()
	{
		return readLine()[0].split(":")[1];
	}

	/**
	 * @return control type
	 */
	public String getControlType()
	{
		return readLine()[1];
	}

	/**
	 * @return control label
	 */
	public String getControlCaption()
	{
		return readLine()[2];
	}


	/**
	 * @return permissible values collection
	 * @throws DynamicExtensionsSystemException
	 */
	public List<String> getPermissibleValues() throws DynamicExtensionsSystemException
	{
		//counter for to locate the start of the permissible values
		String[] nextLine = readLine();
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
			String filePath = getSystemIndependantFilePath(tempString[1]);
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
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

	public boolean hasDisplayLable()
	{
		if (readLine()[0].startsWith(DISPLAY_LABLE))
		{
			return true;
		}
		return false;
	}

	public boolean hasFormDefination()
	{
		if (FORM_DEFINITION.equals(readLine()[0]))
		{
			return true;
		}
		return false;
	}

	public String getCategoryName()
	{
		return readLine()[0];
	}

	public String getEntityGroupName()
	{
		return readLine()[0];
	}

	public boolean hasSubcategory()
	{
		if (readLine()[0].contains("subcategory:"))
		{
			return true;
		}
		return false;
	}

	public String getTargetContainerCaption()
	{
		return readLine()[0].split(":")[1];
	}

	public String getMultiplicity()
	{
		return readLine()[0].split(":")[2];
	}

	public Map<String, String> getControlOptions()
	{
		Map<String, String> controlOptions = new HashMap<String, String>();
		for (String string : readLine())
		{
			if (string.startsWith(OPTIONS + "~"))
			{
				String[] controlOptionsValue = string.split("~")[1].split(":");

				for (String optionValue : controlOptionsValue)
				{
					controlOptions.put(optionValue.split("=")[0], optionValue.split("=")[1]);
				}

			}
		}

		return controlOptions;
	}

	

}
