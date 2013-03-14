/**
 *
 */

package edu.common.dynamicextensions.operations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author Gaurav_mehta
 *
 */
public final class DatabaseOperations
{

	private DatabaseOperations()
	{
	}

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(DatabaseOperations.class);

	/**
	 * This method executes the query which are of type update.
	 * @param queryList queries to be executed
	 * @throws DynamicExtensionsSystemException
	 */
	public static void executeDML(List<Map<String, LinkedList<ColumnValueBean>>> queryList)
			throws DynamicExtensionsSystemException
	{
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			LinkedList<LinkedList<ColumnValueBean>> colValBeanList = new LinkedList<LinkedList<ColumnValueBean>>();
			for (Map<String, LinkedList<ColumnValueBean>> query : queryList)
			{
				for (Map.Entry<String, LinkedList<ColumnValueBean>> queryRecord : query.entrySet())
				{
					colValBeanList.add(queryRecord.getValue());
					jdbcDao.executeUpdate(queryRecord.getKey(), colValBeanList);
					colValBeanList.clear();
				}
			}
			jdbcDao.commit();
		}

		catch (DAOException e)
		{
			LOGGER.error(e.getMessage());
			throw new DynamicExtensionsSystemException("Error while inserting the data", e);
		}
		finally
		{
			DynamicExtensionsUtility.closeDAO(jdbcDao);
		}
	}
}
