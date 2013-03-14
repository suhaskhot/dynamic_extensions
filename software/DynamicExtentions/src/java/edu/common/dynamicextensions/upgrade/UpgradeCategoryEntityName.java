/**
 *
 */

package edu.common.dynamicextensions.upgrade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.entitymanager.DynamicExtensionsQueryBuilderConstantsInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerUtil;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.operations.DatabaseOperations;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * this class upgrades the category entity name to the newer names of format
 * format of <root_entity_name>[instance_Number]<entity_name>[instance_Number]
 * @author suhas_khot
 *
 */
public class UpgradeCategoryEntityName implements DynamicExtensionsQueryBuilderConstantsInterface
{

	/**
	 * @param args
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 * @throws DynamicExtensionsApplicationException
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, SQLException,
			DynamicExtensionsApplicationException
	{
		upgradeCategoryEntityNames();

	}

	/**
	 * this method upgrade the older category names to the newer names
	 * @throws DynamicExtensionsSystemException fails to retrieve objects
	 * @throws DAOException fails to execute query
	 * @throws SQLException fails to execute query
	 * @throws DynamicExtensionsApplicationException
	 */
	private static void upgradeCategoryEntityNames() throws DynamicExtensionsSystemException,
			SQLException, DynamicExtensionsApplicationException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<Long> categoryEntityIdColl = entityManager.getAllCategoryEntityId();

		if (categoryEntityIdColl != null && !categoryEntityIdColl.isEmpty())
		{
			StringBuffer query = new StringBuffer();
			query.append(SELECT_KEYWORD).append(WHITESPACE).append(IDENTIFIER);
			query.append(WHITESPACE).append(FROM_KEYWORD).append(WHITESPACE).append(
					DEConstants.PATH_TABLE_NAME).append(WHITESPACE);
			query.append(WHITESPACE).append(WHERE_KEYWORD).append(WHITESPACE).append(
					DEConstants.CATEGORY_ENTITY_ID).append(EQUAL).append(QUESTION_MARK);
			for (Long categoryEntityId : categoryEntityIdColl)
			{
				List<ColumnValueBean> queryDataList = new LinkedList<ColumnValueBean>();
				queryDataList.add(new ColumnValueBean(DEConstants.CATEGORY_ENTITY_ID,
						categoryEntityId));
				List<Long> pathIdColl = new EntityManagerUtil().getResultInList(query.toString(),
						queryDataList);
				queryDataList.clear();
				if (pathIdColl == null || pathIdColl.isEmpty())
				{
					String categoryEntityName = entityManager
							.getCategoryEntityNameByCategoryEntityId(categoryEntityId);
					if (categoryEntityName != null && !"".equals(categoryEntityName.trim()))
					{
						int numberOfOccurances = 0;
						char[] catEntityName = categoryEntityName.toCharArray();
						for (char character : catEntityName)
						{
							if (Character.toString(character).trim().equalsIgnoreCase(
									DEConstants.CLOSING_SQUARE_BRACKET))
							{
								numberOfOccurances = numberOfOccurances + 1;
							}
						}
						if (numberOfOccurances < DEConstants.TWO)
						{
							String newCategoryName = categoryEntityName + categoryEntityName;
							updateCategoryName(newCategoryName, categoryEntityId);
						}
					}
				}
				else
				{
					Long pathId = Long.valueOf(pathIdColl.get(0).toString());
					Collection<Long> pathAssociationRelationIds = entityManager
							.getPathAssociationRelationIds(pathId);
					String categoryEntityName = getCategoryEntityName(pathAssociationRelationIds);
					if (categoryEntityName != null && !"".equals(categoryEntityName.trim()))
					{
						updateCategoryName(categoryEntityName, categoryEntityId);
					}
				}
			}
		}
	}

	/**
	 * this method updates the categoryEntity Name
	 * @param categoryEntityName name of category Entity
	 * @param categoryEntityId Identifier of category Entity
	 * @throws SQLException
	 * @throws DynamicExtensionsSystemException
	 */
	private static void updateCategoryName(String categoryEntityName, Long categoryEntityId)
			throws DynamicExtensionsSystemException, SQLException
	{
		StringBuffer query = new StringBuffer();
		Map<String, LinkedList<ColumnValueBean>> queryVsDatamap = new HashMap<String, LinkedList<ColumnValueBean>>();

		query.append(UPDATE_KEYWORD);
		query.append(WHITESPACE).append(DEConstants.ABSTRACT_METADATA_TABLE_NAME);
		query.append(WHITESPACE).append(SET_KEYWORD).append(WHITESPACE).append(DEConstants.NAME)
				.append(EQUAL).append(QUESTION_MARK).append(WHITESPACE);
		query.append(WHERE_KEYWORD).append(WHITESPACE).append(IDENTIFIER).append(EQUAL).append(
				QUESTION_MARK);
		LinkedList<ColumnValueBean> colValBeanList = new LinkedList<ColumnValueBean>();
		colValBeanList.add(new ColumnValueBean(DEConstants.NAME, categoryEntityName));
		colValBeanList.add(new ColumnValueBean(IDENTIFIER, categoryEntityId));
		queryVsDatamap.put(query.toString(), colValBeanList);
		List<Map<String, LinkedList<ColumnValueBean>>> queryList = new ArrayList<Map<String, LinkedList<ColumnValueBean>>>();
		DatabaseOperations.executeDML(queryList);
	}

	/**
	 * This method retrieves the category names from pathAssociationRelationIds
	 * @param pathAssociationRelationIds
	 * @return category entity name
	 * @throws DynamicExtensionsSystemException fails to gets records
	 */
	private static String getCategoryEntityName(Collection<Long> pathAssociationRelationIds)
			throws DynamicExtensionsSystemException
	{

		StringBuffer categoryEntityName = new StringBuffer();
		if (pathAssociationRelationIds != null && !pathAssociationRelationIds.isEmpty())
		{
			for (Long pathAssociationRelationId : pathAssociationRelationIds)
			{
				EntityManagerInterface entityManager = EntityManager.getInstance();
				Long associationId = entityManager
						.getAssociationIdFrmPathAssoRelationId(pathAssociationRelationId);
				String srcEntityName = entityManager
						.getSrcEntityNameFromAssociationId(associationId);
				String tgtEntityName = entityManager
						.getTgtEntityNameFromAssociationId(associationId);

				Long tgtInstanceId = entityManager
						.getTgtInstanceIdFromAssociationRelationId(pathAssociationRelationId);
				if ("".equals(categoryEntityName.toString().trim())
						&& categoryEntityName.toString().contains(srcEntityName))
				{
					categoryEntityName.append(tgtEntityName).append(DEConstants.OPENING_SQUARE_BRACKET);
					categoryEntityName.append(tgtInstanceId.toString()).append(DEConstants.CLOSING_SQUARE_BRACKET);
				}
				else
				{
					Long srcInstanceId = entityManager
							.getSrcInstanceIdFromAssociationRelationId(pathAssociationRelationId);
					categoryEntityName.append(srcEntityName).append(DEConstants.OPENING_SQUARE_BRACKET);
					categoryEntityName.append(srcInstanceId).append(DEConstants.CLOSING_SQUARE_BRACKET);
					categoryEntityName.append(tgtEntityName).append(DEConstants.OPENING_SQUARE_BRACKET);
					categoryEntityName.append(tgtInstanceId.toString()).append(DEConstants.CLOSING_SQUARE_BRACKET);
				}
			}
		}
		return categoryEntityName.toString();
	}
}
