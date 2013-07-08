
package edu.common.dynamicextensions.upgrade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * This is migrates property based Headings and Notes to control centric design. 
 * After this task heading and notes will be available as Label type of control.
 * @author Kunal
 *
 */
public class UpgradeHeadingAndNotes
{

	private static final String INSERT_CONTROL = "insert into DYEXTN_CONTROL (ACTIVITY_STATUS, SHOW_LABEL, CAPTION, IS_CALCULATED, CSS_CLASS, HIDDEN, NAME, SEQUENCE_NUMBER, TOOLTIP, CONTAINER_ID, BASE_ABST_ATR_ID, READ_ONLY, SKIP_LOGIC, HEADING, yPosition, SKIP_LOGIC_TARGET_CONTROL, IS_PASTE_BUTTON_EANBLED, SOURCE_CONTROL_ID, SHOW_HIDE, SELECTIVE_READ_ONLY, IDENTIFIER) values ('Active', 1, ?, 0, null, null, null, ?, null, ?, null, 0, null, null, 0, 0, 1, null, 0, 0, DYEXTN_CONTROL_SEQ.nextval)";

	private static final String INSERT_TAG = "insert into DYEXTN_TAGGED_VALUE (T_KEY, T_VALUE, IDENTIFIER) values (?, ?, DYEXTN_TAGGED_VALUE_SEQ.nextval)";

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	//	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(UpgradeHeadingAndNotes.class);

	private static final String UPDATE_SEQUENCE_NUMBER = "update DYEXTN_CONTROL set SEQUENCE_NUMBER = (SEQUENCE_NUMBER+1) where CONTAINER_ID=? and SEQUENCE_NUMBER >= ?";
	private static final String GET_HEADING_CONTROLS = "select IDENTIFIER from DYEXTN_CONTROL where HEADING is not null";
	private static final String GET_FN_CONTROLS = "select FORM_CONTROL_ID from DYEXTN_FORM_CTRL_NOTES where FORM_CONTROL_ID is not null";
	private static final String GET_CONTAINER_ID_AND_SEQ_NO = "select CONTAINER_ID,SEQUENCE_NUMBER,HEADING from DYEXTN_CONTROL where IDENTIFIER=?";
	private static final String GET_NOTE = "select NOTE from DYEXTN_FORM_CTRL_NOTES where FORM_CONTROL_ID=?";

	//since there is following validations in jdbc dao, 1=? condition has been added to the query
	//if(!sql.contains("?") || columnValueBeans == null || columnValueBeans.isEmpty())
	private static final String CLEAR_HEADING = "UPDATE DYEXTN_CONTROL set HEADING=null where heading is not null and 1=?";
	private static final String CLEAR_NOTES = "delete from DYEXTN_FORM_CTRL_NOTES where 1=?";

	private static final String GET_CONTROL_ID = "select DYEXTN_CONTROL_SEQ.currval from dual";
	private static final String GET_TAG_ID = "select DYEXTN_TAGGED_VALUE_SEQ.currval from dual";
	private static final String INSERT_LABLE = "insert into DYEXTN_LABEL (IDENTIFIER) values (?)";
	private static final String UPDATE_TAGGED_VALUE = "update DYEXTN_TAGGED_VALUE set TAGGED_VALUE_CONTROL_ID=? where IDENTIFIER=?";

	private JDBCDAO jdbcdao;

	/**
	 * @param args
	 * @throws DynamicExtensionsSystemException 
	 * @throws DAOException 
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, DAOException
	{
		UpgradeHeadingAndNotes upgradeHeadingAndNotes = new UpgradeHeadingAndNotes();
		upgradeHeadingAndNotes.upgrade();

	}

	/**
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	public void upgrade() throws DynamicExtensionsSystemException, DAOException
	{
		//1:Initialise DAO
		LOGGER.info("Initialising resources.");
		jdbcdao = DynamicExtensionsUtility.getJDBCDAO(null);

		//2:upgrade Headings to Label control
		LOGGER.info("Upgrading Headings to Label control.");
		upgradeToLabel(GET_HEADING_CONTROLS, Constants.HEADING);
		//3:upgrade Notes to Label control 
		LOGGER.info("Upgrading Notes to Label control.");
		upgradeToLabel(GET_FN_CONTROLS, Constants.NOTES);

		//4:clear legacy values for headings and notes
		LOGGER.info("Deleting legacy headings and notes....");
		clearLegacyData();

		//5:commit changes
		LOGGER.info("Commiting changes to DB.");
		jdbcdao.commit();
		DynamicExtensionsUtility.closeDAO(jdbcdao);

	}

	/**
	 * @throws DynamicExtensionsSystemException
	 */
	private void clearLegacyData() throws DynamicExtensionsSystemException
	{
		LinkedList<ColumnValueBean> dummy = new LinkedList<ColumnValueBean>();
		dummy.add(new ColumnValueBean(1));
		DynamicExtensionsUtility.executeUpdateQuery(CLEAR_HEADING, null, jdbcdao, dummy);
		DynamicExtensionsUtility.executeUpdateQuery(CLEAR_NOTES, null, jdbcdao, dummy);

	}

	/**
	 * @param queryString
	 * @param controlType
	 * @throws DynamicExtensionsSystemException
	 */
	private void upgradeToLabel(String queryString, String controlType)
			throws DynamicExtensionsSystemException
	{
		ResultSet resultSet = null;
		Long controlID = null;

		try
		{
			resultSet = jdbcdao.getResultSet(queryString, null, null);
			//1: iterate over control list
			while (resultSet.next())
			{
				JDBCDAO jdbcdaoLocal = DynamicExtensionsUtility.getJDBCDAO(null);
				controlID = resultSet.getLong(1);

				//2: for each control get container id, sequence number
				Object[] results = getControlData(controlID, controlType, jdbcdaoLocal);
				Long containerID = (Long) results[0];
				Integer sequenceNumber = (Integer) results[1];
				String caption = (String) results[2];

				//3:update sequence number
				updateSequenceNumber(containerID, sequenceNumber, jdbcdaoLocal);

				//4: create new label control the heading
				Long labelId = createLabel(controlType, caption, containerID, sequenceNumber,
						jdbcdaoLocal);
				LOGGER.info("Generated control " + labelId);
				
				jdbcdaoLocal.commit();
				DynamicExtensionsUtility.closeDAO(jdbcdaoLocal);

			}
		}
		catch (DAOException e)
		{
			throw new RuntimeException("Error processing control with identifier " + controlID, e);
		}
		catch (SQLException e)
		{
			throw new RuntimeException("Error processing control with identifier " + controlID, e);
		}
	}

	/**
	 * @param controlID
	 * @param controlType
	 * @param jdbcdaoLocal 
	 * @return
	 */
	private Object[] getControlData(Long controlID, String controlType, JDBCDAO jdbcdaoLocal)
	{

		List<ColumnValueBean> beans = new ArrayList<ColumnValueBean>();
		beans.add(new ColumnValueBean(controlID));
		Object[] results = new Object[3];
		try
		{
			ResultSet resultSet = jdbcdaoLocal.getResultSet(GET_CONTAINER_ID_AND_SEQ_NO, beans,
					null);
			resultSet.next();
			results[0] = resultSet.getLong(1);
			results[1] = resultSet.getInt(2);
			if (Constants.HEADING.equals(controlType))
			{
				results[2] = resultSet.getString(3);
			}
			else
			{
				List<ColumnValueBean> beans2 = new ArrayList<ColumnValueBean>();
				beans2.add(new ColumnValueBean(controlID));
				ResultSet resultSet2 = jdbcdaoLocal.getResultSet(GET_NOTE, beans2, null);
				resultSet2.next();
				results[2] = resultSet2.getString(1);
				resultSet2.close();
			}
			resultSet.close();

		}
		catch (DAOException e)
		{
			throw new RuntimeException("Error processing control with identifier " + controlID, e);
		}
		catch (SQLException e)
		{
			throw new RuntimeException("Error processing control with identifier " + controlID, e);
		}
		return results;
	}

	/**
	 * @param type
	 * @param caption
	 * @param containerId
	 * @param sequenceNumber 
	 * @param jdbcdaoLocal 
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 * @throws SQLException 
	 * @throws DAOException 
	 */
	private Long createLabel(String type, String caption, Long containerId, Integer sequenceNumber,
			JDBCDAO jdbcdaoLocal) throws DynamicExtensionsSystemException, SQLException,
			DAOException
	{

		LinkedList<ColumnValueBean> controlValue = new LinkedList<ColumnValueBean>();
		controlValue.add(new ColumnValueBean(caption));
		controlValue.add(new ColumnValueBean(sequenceNumber));
		controlValue.add(new ColumnValueBean(containerId));
		DynamicExtensionsUtility.executeUpdateQuery(INSERT_CONTROL, null, jdbcdaoLocal,
				controlValue);

		Long newControlId = getCurrentValue(GET_CONTROL_ID, jdbcdaoLocal);
		LinkedList<ColumnValueBean> labelValue = new LinkedList<ColumnValueBean>();
		labelValue.add(new ColumnValueBean(newControlId));
		DynamicExtensionsUtility.executeUpdateQuery(INSERT_LABLE, null, jdbcdaoLocal, labelValue);

		LinkedList<ColumnValueBean> tagValue = new LinkedList<ColumnValueBean>();
		tagValue.add(new ColumnValueBean(Constants.LABEL_TYPE));
		tagValue.add(new ColumnValueBean(type));
		DynamicExtensionsUtility.executeUpdateQuery(INSERT_TAG, null, jdbcdaoLocal, tagValue);

		Long newTagID = getCurrentValue(GET_TAG_ID, jdbcdaoLocal);
		LinkedList<ColumnValueBean> labelUpdateValue = new LinkedList<ColumnValueBean>();
		labelUpdateValue.add(new ColumnValueBean(newControlId));
		labelUpdateValue.add(new ColumnValueBean(newTagID));
		DynamicExtensionsUtility.executeUpdateQuery(UPDATE_TAGGED_VALUE, null, jdbcdaoLocal,
				labelUpdateValue);

		return newControlId;
	}

	private Long getCurrentValue(String query, JDBCDAO jdbcdaoLocal) throws SQLException
	{
		ResultSet resultSet = null;
		long currentVal;
		try
		{
			resultSet = jdbcdaoLocal.getResultSet(query, null, null);
			resultSet.next();
			currentVal = resultSet.getLong(1);
			resultSet.close();
		}
		catch (DAOException e)
		{
			throw new RuntimeException("Error getting control identifier", e);
		}

		return currentVal;
	}

	/**
	 * Adds 1 to existing sequence number since new control has been added
	 * @param containerID
	 * @param sequenceNumber
	 * @param jdbcdaoLocal2 
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException 
	 */
	private void updateSequenceNumber(Long containerID, Integer sequenceNumber, JDBCDAO jdbcdaoLocal)
			throws DynamicExtensionsSystemException, DAOException
	{

		LinkedList<ColumnValueBean> queryDataList = new LinkedList<ColumnValueBean>();
		queryDataList.add(new ColumnValueBean(containerID));
		queryDataList.add(new ColumnValueBean(sequenceNumber));
		DynamicExtensionsUtility.executeUpdateQuery(UPDATE_SEQUENCE_NUMBER, null, jdbcdaoLocal,
				queryDataList);
	}

}
