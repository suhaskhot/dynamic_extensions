/**
 *
 */

package edu.common.dynamicextensions.util;

import static edu.common.dynamicextensions.util.global.DEConstants.FALSE_VALUE_LIST;
import static edu.common.dynamicextensions.util.global.DEConstants.TRUE_VALUE_LIST;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class DynamicExtensionsUtility
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(DynamicExtensionsUtility.class);


	/**
	 * Checks if the given string object is not null and not empty string.
	 * @param value the object name.
	 * @return true, if checks if is not empty string.
	 */
	public static boolean isNotEmptyString(final Object value)
	{
		return value != null && !"".equals(value);
	}

	/**
	 * initialize application variables
	 */
	public static void initialiseApplicationVariables()
	{
//		if (Logger.out == null)
//		{
//
//		}
	}

	/**
	 * This method converts stack trace to the string representation.
	 * @param throwable throwable object
	 * @return String representation  of the stack trace
	 */
	public static String getStackTrace(Throwable throwable)
	{
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * Converts string to integer.
	 * @param string
	 * @return int vale.
	 * @throws DynamicExtensionsApplicationException.
	 */
	public static int convertStringToInt(String string)
			throws DynamicExtensionsApplicationException
	{
		int intValue = 0;
		if (string != null)
		{
			try
			{
				if (string.trim().equals(""))
				{
					intValue = 0; //Assume 0 for blank values
				}
				else
				{
					intValue = Integer.parseInt(string);
				}
			}
			catch (NumberFormatException e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
		}
		return intValue;
	}

	/**
	 * Checks that the input String contains only numeric digits.
	 * @param numString The string whose characters are to be checked.
	 * @return Returns false if the String contains any alphabet else returns true.
	 * */
	public static boolean isNaturalNumber(String numString)
	{
		boolean isNaturalNumber = true;
		try
		{
			if (Double.parseDouble(numString) < 0)
			{
				isNaturalNumber = false;
			}
		}
		catch (NumberFormatException exp)
		{
			isNaturalNumber = false;
		}
		return isNaturalNumber;
	}

	/**
	 *
	 * @param numString Number as string.
	 * @return boolean true if numeric else false.
	 */
	public static boolean isNumeric(String numString)
	{
		boolean isNumeric = true;
		BigDecimal bigDecimal = null;
		try
		{
			bigDecimal = new BigDecimal(numString);
			if (bigDecimal == null)
			{
				isNumeric = false;
			}
		}
		catch (NumberFormatException exp)
		{
			isNumeric = false;
		}
		return isNumeric;
	}

	/**
	 * Checks that the input String contains only numeric digits also allowed zero as valid value.
	 *
	 * @param numString The string whose characters are to be checked.
	 *
	 * @return false if the String contains any alphabet else returns true.
	 */
	public static boolean isNumericDigit(String numString)
	{
		boolean isNumeric = false;
		try
		{
			long longValue = Long.parseLong(numString);
			if (longValue >= 0)
			{
				isNumeric ^= true;
			}
		}
		catch (NumberFormatException exp)
		{
			isNumeric = false;
		}
		return isNumeric;
	}
	
	/**
	 * @return day form Calendar.
	 */
	public static int getCurrentDay()
	{
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return current month.
	 */
	public static int getCurrentMonth()
	{
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * @return current year.
	 */
	public static int getCurrentYear()
	{
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * @return current Hour.
	 */
	public static int getCurrentHours()
	{
		return Calendar.getInstance().get(Calendar.HOUR);
	}

	/**
	 * @return current minutes.
	 */
	public static int getCurrentMinutes()
	{
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/**
	 *
	 * @param originalObject Object
	 * @return Object
	 */
	public static Object cloneObject(Object originalObject)
	{
		Object clonedObject = null;
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(originalObject);
			//retrieve back
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			clonedObject = objectInputStream.readObject();
		}
		catch (IOException e)
		{
			Logger.out.error(e);
		}
		catch (ClassNotFoundException e)
		{
			Logger.out.error(e);
		}

		return clonedObject;
	}

	/**
	 * @param string : string to be checked.
	 * @param list: List that is to be checked if string is contained
	 * @return check if a string is contained in the passed list and return true if yes
	 */
	public static boolean isStringInList(String string, List<String> list)
	{
		boolean isContainedInList = false;
		if (string != null && list != null)
		{
			//String listString = null;
			Iterator<String> iterator = list.iterator();
			while (iterator.hasNext())
			{
				String listString = iterator.next();
				if (string.equals(listString))
				{
					isContainedInList = true;
					break;
				}
			}
		}
		return isContainedInList;
	}

	/**
	 *
	 * @param list list of NameValueBeanObjects
	 */
	public static void sortNameValueBeanListByName(List<NameValueBean> list)
	{
		Collections.sort(list, new Comparator()
		{

			public int compare(Object object1, Object object2)
			{
				String obj1Name = "";
				String obj2Name = "";
				if (object1 != null)
				{
					obj1Name = ((NameValueBean) object1).getName();
				}
				if (object2 != null)
				{
					obj2Name = ((NameValueBean) object2).getName();
				}
				return obj1Name.compareTo(obj2Name);
			}
		});
	}

	/**
	 * @param controlsSeqNumbers : String of controls sequence numbers
	 * @param delimiter Delimiter used in string
	 * @return Integer objecta array.
	 */
	public static Integer[] convertToIntegerArray(String controlsSeqNumbers, String delimiter)
	{
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		if (controlsSeqNumbers != null)
		{
			StringTokenizer strTokenizer = new StringTokenizer(controlsSeqNumbers, delimiter);
			if (strTokenizer != null)
			{
				//String str = null;
				//Integer integer = null;
				while (strTokenizer.hasMoreElements())
				{
					String str = strTokenizer.nextToken();
					if (str != null)
					{
						try
						{
							Integer integer = Integer.valueOf(str);
							integerList.add(integer);
						}
						catch (NumberFormatException e)
						{
							LOGGER.error(e);
						}
					}
				}
			}
		}
		return integerList.toArray(new Integer[integerList.size()]);
	}

	/**
	 * This method determines whether the checkbox is to be checked or not.
	 * @param value the value particular to database
	 * for e.g. oracle - "1" or "0"
	 * mysql "true" or "false"
	 * @return true if checked , false otherwise
	 */
	public static boolean isCheckBoxChecked(String value)
	{
		boolean isChecked = false;
		if (value != null && !"".equals(value.trim())
				&& (value.equals("1") || value.equals("true")))
		{
			isChecked = true;
		}
		return isChecked;
	}

	/**
	 * @param date1 date in string format
	 * @param date2 date in string format
	 * @return
	 */
	private static boolean areBothDatesOfSameFormat(String date1, String date2)
	{
		boolean returnValue = false;
		if (date1.length() != date2.length())
		{
			returnValue = true;
		}
		return returnValue;
	}

	/**
	 * This method returns the format of the date depending upon the the type of the format selected on UI.
	 * @param format Selected format
	 * @return date format
	 */
	public static String getDateFormat(String format)
	{
		String dateFormat = ProcessorConstants.DATE_ONLY_FORMAT;
		if (format != null && format.equals(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME))
		{
			dateFormat = ProcessorConstants.DATE_TIME_FORMAT;
		}
		if (format != null && format.equals(ProcessorConstants.DATE_FORMAT_OPTION_MONTHANDYEAR))
		{
			dateFormat = ProcessorConstants.MONTH_YEAR_FORMAT;
		}
		if (format != null && format.equals(ProcessorConstants.DATE_FORMAT_OPTION_YEARONLY))
		{
			dateFormat = ProcessorConstants.YEAR_ONLY_FORMAT;
		}

		return dateFormat;
	}

	/**
	 * This method returns the sql format of the date depending upon the the type of the format of the Date Attribute.
	 * @param dateFormat format of the Date Attribute
	 * @return SQL date format
	 */
	public static String getSQLDateFormat(String dateFormat)
	{
		CommonServiceLocator locator = CommonServiceLocator.getInstance();
		StringBuffer sqlDateFormat = new StringBuffer(locator.getDatePattern());
		if (dateFormat != null && dateFormat.equals(ProcessorConstants.DATE_TIME_FORMAT))
		{
			sqlDateFormat.append(' ').append(locator.getTimePattern());
		}
		return sqlDateFormat.toString();
	}


	/**
	 * Method to check if data type is numeric i.e long,integer,short,float,double
	 * @param dataType
	 * @return true if data type is numeric
	 */
	public static boolean isDataTypeNumeric(String dataType)
	{
		boolean isDataTypeNumber = false;
		if (dataType.equals(ProcessorConstants.DATATYPE_INTEGER)
				|| dataType.equals(ProcessorConstants.DATATYPE_LONG)
				|| dataType.equals(ProcessorConstants.DATATYPE_FLOAT)
				|| dataType.equals(ProcessorConstants.DATATYPE_DOUBLE)
				|| dataType.equals(ProcessorConstants.DATATYPE_NUMBER))
		{
			isDataTypeNumber = true;
		}

		return isDataTypeNumber;
	}

	/**
	 * This method used to replace escape characters such as single and double
	 * quote.
	 *
	 * @param str
	 *            the str
	 * @param one
	 *            the one
	 * @param another
	 *            the another
	 * @return the string
	 */
	public static String replaceUtil(String str, String one, String another)
	{
		String string = str;
		if (str != null && !"".equals(str))
		{
			StringBuilder res = new StringBuilder();
			int index = str.indexOf(one, 0);
			int lastpos = 0;
			while (index != -1)
			{
				res.append(str.substring(lastpos, index) + another);
				lastpos = index + one.length();
				index = str.indexOf(one, lastpos);
			}
			res.append(str.substring(lastpos));
			string = res.toString();
		}
		return string;
	}






	/**
	 * @param hql Query and returns the results.
	 * @return String query
	 * @throws DAOException Generic DAO Exception
	 * @throws ClassNotFoundException
	 * @throws DynamicExtensionsSystemException
	 */
	public static List executeQuery(String hql) throws DAOException,
			DynamicExtensionsSystemException
	{
		HibernateDAO dao = DynamicExtensionsUtility.getHibernateDAO();
		List list = dao.executeQuery(hql);
		DynamicExtensionsUtility.closeDAO(dao);
		return list;
	}

	/**
	 * This method executes a SQL query.
	 * @param query
	 * @param userId
	 * @param jdbcDao
	 * @param colValBeanList
	 * @throws DynamicExtensionsSystemException
	 */
	public static void executeUpdateQuery(final String query, final Long userId,
			final JDBCDAO jdbcDao, final LinkedList<ColumnValueBean> colValBeanList)
			throws DynamicExtensionsSystemException
	{
		
		JDBCDAO dao = jdbcDao;
		try
		{
			if (dao == null)
			{
				dao = getJDBCDAO();
			}
			final LinkedList<LinkedList<ColumnValueBean>> valueBeanList = new LinkedList<LinkedList<ColumnValueBean>>();
			valueBeanList.add(colValBeanList);
			dao.executeUpdate(query, valueBeanList);
		}
		catch (final DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		finally
		{
			if (jdbcDao == null)
			{
				closeDAO(dao);
			}
		}
	}

	/**
	 * Replace any single and double quotes value with proper escape character	in HTML
	 * @param value
	 * @return String value
	 */
	public static String getEscapedStringValue(String value)
	{
		String originalString = null;
		if (value != null)
		{
			originalString = replaceUtil(value, "'", "&#39");
			originalString = replaceUtil(originalString, "\"", "&#34");
			originalString = originalString.trim();
		}
		return originalString;
	}

	/**
	 * Replace any single and double quotes value with proper escape character	in HTML
	 * @param value
	 * @return String value
	 */
	public static String getUnEscapedStringValue(String value)
	{
		String originalString = null;
		if (value != null)
		{
			originalString = replaceUtil(value, "&#39", "'");
			originalString = replaceUtil(value, "&#34", "\"");
			originalString = originalString.trim();
			
		}
		return originalString;
	}


	/**
	 * @param categoryEntityName
	 * @return categoryEntityName
	 */
	public static String getCategoryEntityName(String categoryEntityName)
	{

		String entityName = categoryEntityName;
		if (entityName != null && entityName.length() > 0)
		{
			Pattern pattern = Pattern.compile("[]]");
			Matcher matcher = pattern.matcher(entityName);
			StringBuffer stringBuff = new StringBuffer();
			boolean result = matcher.find();
			// Loop through and create a new String
			// with the replacements
			while (result)
			{
				matcher.appendReplacement(stringBuff, entityName.subSequence(matcher.start(),
						matcher.end())
						+ " ");
				result = matcher.find();
			}
			//Add the last segment of input to
			//the new String
			matcher.appendTail(stringBuff);

			String[] categoryEntityNameArray = stringBuff.toString().trim().split(" ");
			entityName = categoryEntityNameArray[categoryEntityNameArray.length - 1];
		}
		return entityName;
	}

	/**
	 * this method returns object from database not from current session.
	 * @param sourceObjectName class name.
	 * @param identifier id for object.
	 * @return object from database
	 * @throws DAOException generic DAO exception.
	 */
	public static Object getCleanObject(String sourceObjectName, Long identifier)
			throws DynamicExtensionsSystemException
	{
		HibernateDAO hibernateDao = null;
		try
		{

			hibernateDao = DynamicExtensionsUtility.getHibernateDAO();
			return hibernateDao.retrieveById(sourceObjectName, identifier);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while retrieving the Clean Object", e);
		}
		finally
		{
			DynamicExtensionsUtility.closeDAO(hibernateDao);
		}
	}

	/**
	 * @return jdbcDao
	 * @throws DynamicExtensionsSystemException
	 * 
	 */
	public static JDBCDAO getJDBCDAO() throws DynamicExtensionsSystemException
	{
		return getJDBCDAO(null);
	}

	/**
	 * @param sessionDataBean TODO
	 * @return jdbcDao
	 * @throws DynamicExtensionsSystemException
	 */
	public static JDBCDAO getJDBCDAO(SessionDataBean sessionDataBean)
			throws DynamicExtensionsSystemException
	{
		
		String appName = "";
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = DAOConfigFactory.getInstance().getDAOFactory(appName).getJDBCDAO();
			jdbcDao.openSession(sessionDataBean);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(
					"Error occured while opening the DAO session", e);
		}
		return jdbcDao;
	}

	/**
	 * @return hibernateDao
	 * @throws DynamicExtensionsSystemException
	 */
	public static HibernateDAO getHibernateDAO() throws DynamicExtensionsSystemException
	{
		return getHibernateDAO(null);
	}

	/**
	 * @param sessionDataBean
	 * @return hibernateDao
	 * @throws DynamicExtensionsSystemException
	 */
	public static HibernateDAO getHibernateDAO(SessionDataBean sessionDataBean)
			throws DynamicExtensionsSystemException
	{
		String appName = "";
		HibernateDAO hibernateDao = null;
		try
		{
			hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(appName)
					.getDAO();
			hibernateDao.openSession(sessionDataBean);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(
					"Error occured while opening the DAO session", e);
		}
		return hibernateDao;
	}

	/**
	 * @param jdbcDao DAO object
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException generic DAO exception
	 */
	public static void closeDAO(DAO dao)
	{
		closeDAO(dao, true);
	}
	
	public static void closeDAO(DAO dao, boolean throwException) 
	{
		if (dao != null)
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				if (throwException) {
					throw new RuntimeException("Error closing DAO", e);
				}
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				
			}
		}
	}

	/**
	 * Roll Back DAO.
	 * @param hibernateDAO DAO
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	public static void rollBackDAO(DAO hibernateDAO) throws DynamicExtensionsSystemException
	{
		try
		{
			hibernateDAO.rollback();
		}
		catch (DAOException daoExp)
		{
			throw new DynamicExtensionsSystemException(DEConstants.DATA_INSERTION_ERROR_MESSAGE,
					daoExp);
		}
	}

	public static void executeDML(List<Map<String, LinkedList<ColumnValueBean>>> queryList)
			throws DynamicExtensionsSystemException
	{
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			for (Map<String, LinkedList<ColumnValueBean>> query : queryList)
			{
				for (Map.Entry<String, LinkedList<ColumnValueBean>> queryRecord : query.entrySet())
				{
					LinkedList<LinkedList<ColumnValueBean>> colValBeanList = new LinkedList<LinkedList<ColumnValueBean>>();
					colValBeanList.add(queryRecord.getValue());
					jdbcDao.executeUpdate(queryRecord.getKey(), colValBeanList);
				}
			}
		}

		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while inserting the data", e);
		}
		finally
		{
			try
			{
				jdbcDao.commit();
				DynamicExtensionsUtility.closeDAO(jdbcDao);
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Error while inserting the data", e);
			}
		}
	}

	
	
	/**
	 * @param filePath
	 * @param jsFunctionName
	 * @param jsFunctParameters
	 * @return
	 */
	public static String executeJavaScriptFunc(String filePath, String jsFunctionName,
			Object[] jsFunctParameters)
	{
		String output = null;
		FileReader reader = null;
		try
		{
			ScriptEngineManager manager = new ScriptEngineManager();
			if (manager != null)
			{
				ScriptEngine engine = manager.getEngineByName("javascript");
				reader = new FileReader(filePath);
				if (reader != null && engine != null)
				{
					engine.eval(reader);
					Invocable invokeEngine = (Invocable) engine;
					output = invokeEngine.invokeFunction(jsFunctionName, jsFunctParameters)
							.toString();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}
			}
			catch (IOException e)
			{
				LOGGER.error(e.getMessage());
			}
		}
		return output;
	}


	/**
	 * It will return the List of all  valid Patterns specified in the ValidDatePatterns.xml
	 * @return List of datePatterns.
	 * @throws DynamicExtensionsSystemException
	 */
	public static Map<String, String> getAllValidDatePatterns()
			throws DynamicExtensionsSystemException
	{
		Map<String, String> validDatePatternMap = new HashMap<String, String>();

		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("ValidDatePatterns.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			Document doc = dbf.newDocumentBuilder().parse(inputStream);
			NodeList nodeList = doc.getElementsByTagName("pattern");
			for (int s = 0; s < nodeList.getLength(); s++)
			{
				Node firstNode = nodeList.item(s);

				if (firstNode.getNodeType() == Node.ELEMENT_NODE)
				{
					NamedNodeMap attributeMap = firstNode.getAttributes();
					Node attributeNode = attributeMap.getNamedItem("regex");
					String regex = attributeNode.getNodeValue();
					NodeList fstNm = firstNode.getChildNodes();
					validDatePatternMap.put(fstNm.item(0).getNodeValue(), regex);
				}
			}
		}
		catch (Exception exception)
		{
			throw new DynamicExtensionsSystemException(exception.getMessage(), exception);
		}
		return validDatePatternMap;
	}

	/**
	 * Replace any single and double quotes value with proper escape character	in HTML
	 * @param value
	 * @return String value
	 */
	public static String replaceHTMLSpecialCharacters(String value)
	{
		/*String escapeStringValue = value;
		escapeStringValue = getUnEscapedStringValue(escapeStringValue);*/
		String escapeStringValue = getUnEscapedStringValue(value);
		if (escapeStringValue != null && escapeStringValue.length() > 0)
		{
			escapeStringValue = StringEscapeUtils.escapeHtml(escapeStringValue);
		}
		return escapeStringValue;
	}


	/**
	 * Validate boolean value.
	 *
	 * @param booleanValue the boolean value
	 *
	 * @return true, if successful
	 * @throws DynamicExtensionsSystemException
	 */
	public static boolean validateAndReturnBooleanValue(String booleanValue)
			throws DynamicExtensionsSystemException
	{
		String upperCaseBooleanValue = booleanValue.toUpperCase(Locale.getDefault());
		boolean returnValue = false;
		if (TRUE_VALUE_LIST.contains(upperCaseBooleanValue))
		{
			returnValue = true;
		}
		else if (FALSE_VALUE_LIST.contains(upperCaseBooleanValue))
		{
			returnValue = false;
		}
		else
		{
			throw new DynamicExtensionsSystemException(
					"Incorrect spelling for Paste Button Configuration value");
		}
		return returnValue;
	}
}