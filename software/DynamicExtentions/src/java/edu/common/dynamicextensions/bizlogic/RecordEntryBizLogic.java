
package edu.common.dynamicextensions.bizlogic;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.SQLQueryManager;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.common.bizlogic.DefaultBizLogic;

public class RecordEntryBizLogic extends DefaultBizLogic
{

	public ResultSet getRecordEntryId(Long formContextId, JdbcDao dao) throws JAXBException, SAXException
	{

		List<Object> params = new ArrayList<Object>();
		params.add(formContextId);
		params.add(Constants.DISABLED);
		ResultSet rowData = dao.getResultSet(
				SQLQueryManager.getInstance().getSQL(DEConstants.RECORD_ID_FROM_FORM_CONTEXT_ID), params);
		return rowData;
	}

	public ResultSet getRecordEntryId(Long formContextId, Long hookObjectRecordId, JdbcDao dao) throws JAXBException,
			SAXException
	{

		List<Object> params = new ArrayList<Object>();
		params.add(hookObjectRecordId);
		params.add(formContextId);
		params.add(Constants.DISABLED);
		ResultSet rowData = dao.getResultSet(
				SQLQueryManager.getInstance().getSQL(DEConstants.RECORD_ID_FOR_PARTICIPANT_FROM_FORM_CONTEXT_ID),
				params);
		return rowData;

	}

}
