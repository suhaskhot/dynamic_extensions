
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;

public class EntityManagerUtil implements DynamicExtensionsQueryBuilderConstantsInterface
{

	private static Map<String, Long> idMap = new HashMap<String, Long>();

	/**
	 * @param query
	 * @param useClnSession
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static ResultSet executeQuery(String query, boolean... useClnSession) throws DynamicExtensionsSystemException
	{
		Connection conn = null;
		Session session = null;
		try
		{
			if (useClnSession.length == 1 && useClnSession[0])
			{
				session = DBUtil.getCleanSession();
				conn = session.connection();
			}
			else
			{
				conn = DBUtil.getConnection();
			}
			
			conn.setAutoCommit(false);
			Statement statement = null;
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}
		catch (Exception e)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * @param inputs
	 * @return
	 */
	public static String getListToString(List inputs)
	{
		String query = inputs.toString();
		query = query.replace("[", OPENING_BRACKET);
		query = query.replace("]", CLOSING_BRACKET);

		return query;
	}

	/**
	 * @param query
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static int getNoOfRecord(String query) throws DynamicExtensionsSystemException
	{
		ResultSet resultSet = null;
		try
		{
			resultSet = executeQuery(query);
			resultSet.next();
			return resultSet.getInt(1);
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		finally
		{
			if (resultSet != null)
			{
				try
				{
					resultSet.close();
				}
				catch (SQLException e)
				{
					throw new DynamicExtensionsSystemException(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @param jdbcDAO
	 * @param query query to be executed
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public int executeDML(JDBCDAO jdbcDAO, String query) throws DynamicExtensionsSystemException
	{
		System.out.println(query);
		Connection conn = null;
		try
		{
			conn = jdbcDAO.getConnection();
			Statement statement = null;
			statement = conn.createStatement();
			return statement.executeUpdate(query);
		}
		catch (Exception e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * @param jdbcDAO
	 * @param queries
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public int executeDMLQueryList(JDBCDAO jdbcDAO, List<String> queries) throws DynamicExtensionsSystemException
	{
		int result = -1;
		
		for (String query : queries)
		{
			try
			{
				jdbcDAO.setAutoCommit(false);
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
			result = executeDML(jdbcDAO, query);
		}
		
		return result;
	}

	/**
	 * Method generates the next identifier for the table that stores the value of the passes entity.
	 * @param tableName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	synchronized public static Long getNextIdentifier(String tableName) throws DynamicExtensionsSystemException
	{
		// Query to get next identifier.
		StringBuffer query = new StringBuffer("SELECT MAX(IDENTIFIER) FROM " + tableName);
		try
		{
			Long identifier = null;
			if (idMap.containsKey(tableName))
			{
				Long idntifier = (Long) idMap.get(tableName);
				identifier = idntifier + 1;
			}
			else
			{
				ResultSet resultSet = null;
				try
				{
					resultSet = executeQuery(query.toString());
					resultSet.next();
					identifier = resultSet.getLong(1);
					identifier = identifier + 1;
				}
				finally
				{
					if (resultSet != null)
					{
						try
						{
							resultSet.close();
						}
						catch (SQLException e)
						{
							throw new DynamicExtensionsSystemException(e.getMessage(), e);
						}
					}
				}
			}
			idMap.put(tableName, identifier);
			
			return identifier;
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException("Could not fetch the next identifier for table " + tableName);
		}
	}

	/**
	 * This method is used in case result of the query is multiple records.
	 * @param query
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public List<Long> getResultInList(String query) throws DynamicExtensionsSystemException
	{
		List<Long> results = new ArrayList<Long>();

		Session session = null;
		Connection con = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try
		{
			session = DBUtil.getCleanSession();
			con = session.connection();
			statement = con.createStatement();
			resultSet = statement.executeQuery(query);
			while (resultSet.next())
			{
				Long identifier = resultSet.getLong(1);
				results.add(identifier);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
				session.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
		}

		return results;
	}

	/**
	 * This method returns all the entity groups reachable from given entity.
	 *
	 * @param entity
	 * @param prcesdEntities
	 * @param prcesdEntGroup
	 */
	public static void getAllEntityGroups(EntityInterface entity, Set<EntityInterface> prcesdEntities,
			Set<EntityGroupInterface> prcesdEntGroup)
	{
		if (prcesdEntities.contains(entity))
		{
			return;
		}

		prcesdEntities.add(entity);
	}

	/**
	 * @param attribute
	 * @param value
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static boolean isValuePresent(AttributeInterface attribute, Object value) throws DynamicExtensionsSystemException
	{
		return new DynamicExtensionBaseQueryBuilder().isValuePresent(attribute, value);
	}

	/**
	 * @param attributes
	 * @return
	 */
	public static List<AbstractAttributeInterface> filterSystemAttributes(List<AbstractAttributeInterface> attributes)
	{
		List<AbstractAttributeInterface> attribtes = new ArrayList<AbstractAttributeInterface>();
		for (AbstractAttributeInterface attribute : attributes)
		{
			if (!attribute.getName().equalsIgnoreCase(ID_ATTRIBUTE_NAME))
			{
				attribtes.add(attribute);
			}
		}
		
		return attribtes;
	}

	/**
	 * @param attributes
	 * @return
	 */
	public static Collection<AbstractAttributeInterface> filterSystemAttributes(Collection<AbstractAttributeInterface> attributes)
	{
		return filterSystemAttributes(new ArrayList(attributes));
	}

	/**
	 * This method adds a system generated attribute to the entity.
	 * @param entity
	 */
	public static void addIdAttribute(EntityInterface entity)
	{
		if (!isIdAttributePresent(entity))
		{
			DomainObjectFactory factory = DomainObjectFactory.getInstance();
			AttributeInterface idAttribute = factory.createLongAttribute();
			idAttribute.setName(ID_ATTRIBUTE_NAME);
			idAttribute.setIsPrimaryKey(new Boolean(true));
			idAttribute.setIsNullable(new Boolean(false));
			ColumnPropertiesInterface column = factory.createColumnProperties();
			column.setName(IDENTIFIER);
			idAttribute.setColumnProperties(column);
			entity.addAttribute(idAttribute);
			idAttribute.setEntity(entity);
		}
	}

	/**
	 * This method returns boolean whether the id attribute is present or not.
	 * @param entity
	 * @return boolean
	 */
	public static boolean isIdAttributePresent(EntityInterface entity)
	{
		boolean isAttrPresent = false;
		
		Collection<AbstractAttributeInterface> attributes = entity.getAbstractAttributeCollection();
		if (attributes != null && !attributes.isEmpty())
		{
			for (AbstractAttributeInterface attribute : attributes)
			{
				if (ID_ATTRIBUTE_NAME.equalsIgnoreCase(attribute.getName()))
				{
					isAttrPresent = true;
					break;
				}
			}
		}
		
		return isAttrPresent;
	}

	/**
	 * @param entityGroup
	 * @param revQueries
	 * @param hibernateDAO
	 * @param queries
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public List<String> getDynamicQueryList(EntityGroupInterface entityGroup, List<String> revQueries, HibernateDAO hibernateDAO, List<String> queries)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DynamicExtensionBaseQueryBuilder queryBuilder = QueryBuilderFactory.getQueryBuilder();
		
		List<EntityInterface> entities = DynamicExtensionsUtility.getUnsavedEntities(entityGroup);
		for (EntityInterface entity : entities)
		{
			List<String> createQueries = queryBuilder.getCreateEntityQueryList((Entity) entity, revQueries);
			if (createQueries != null && !createQueries.isEmpty())
			{
				queries.addAll(createQueries);
			}
		}

		List<EntityInterface> savedEntities = DynamicExtensionsUtility.getSavedEntities(entityGroup);
		for (EntityInterface savedEntity : savedEntities)
		{
			Entity dbaseCopy = (Entity) DBUtil.loadCleanObj(Entity.class, savedEntity.getId());

			List<String> updateQueries = queryBuilder.getUpdateEntityQueryList((Entity) savedEntity, (Entity) dbaseCopy, revQueries);
			if (updateQueries != null && !updateQueries.isEmpty())
			{
				queries.addAll(updateQueries);
			}
		}
		
		return queries;
	}

	/**
	 * This method checks the data in the form.
	 * @param tableName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public boolean isDataPresent(String tableName) throws DynamicExtensionsSystemException
	{
		boolean isDataPrsent = false;
		
		try
		{
			DynamicExtensionBaseQueryBuilder queryBuilder = QueryBuilderFactory.getQueryBuilder();
			isDataPrsent = queryBuilder.isDataPresent(tableName);
		}
		finally
		{
			DBUtil.closeConnection();

		}
		
		return isDataPrsent;
	}

	/**
	 * getRole.
	 * @param assoType
	 * @param name
	 * @param minCard
	 * @param maxCard
	 * @return
	 */
	public static RoleInterface getRole(AssociationType assoType, String name, Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(assoType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		
		return role;
	}
	
}
