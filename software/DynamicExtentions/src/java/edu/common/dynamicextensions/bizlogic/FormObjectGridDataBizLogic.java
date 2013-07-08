
package edu.common.dynamicextensions.bizlogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import net.sf.ehcache.CacheException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.FormGridObject;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.util.SQLQueryManager;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class FormObjectGridDataBizLogic extends DefaultBizLogic {

	private static final Logger logger = Logger.getLogger(FormObjectGridDataBizLogic.class);

	/**
	 * This method will populate list of FormGridObject and is used to create response XML string for the grid
	 * @param formContextId
	 * @param hookEntityId
	 * @param sessionDataBean
	 * @param formUrl
	 * @param deUrl
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws DynamicExtensionsApplicationException
	 * @throws SQLException 
	 * @throws CacheException
	 * @throws BizLogicException
	 */
	public List<FormGridObject> getFormDataForGrid(Long formContextId, String hookEntityId,
			SessionDataBean sessionDataBean, String formUrl, Long hookObjectRecordId)
			throws DynamicExtensionsSystemException, DAOException, JAXBException, SAXException,
			DynamicExtensionsApplicationException, SQLException {
		List<FormGridObject> gridObjectList = new ArrayList<FormGridObject>();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		logger.info(":----------------------------------------------------Within getFormDataForGrid");
		Long containerId = getContainerIdByFormContextId(formContextId, sessionDataBean);
		logger.info(":----------------------------------------------------containerId: " + containerId);

		final Container container = Container.getContainer(containerId);

		RecordEntryBizLogic recordEntryBizLogic = (RecordEntryBizLogic) BizLogicFactory
				.getBizLogic(RecordEntryBizLogic.class.getName());
		ResultSet recordEntryIdResultSet = null;
		JdbcDao dao = new JdbcDao();
		try {
		if (hookObjectRecordId != null) {
			logger.info(":----------------------------------------------------recordEntryIds if hookObjectRecordId is not null");

			recordEntryIdResultSet = recordEntryBizLogic.getRecordEntryId(formContextId, hookObjectRecordId, dao);
		} else {
			logger.info(":----------------------------------------------------recordEntryIds if hookObjectRecordId is  null");

			recordEntryIdResultSet = recordEntryBizLogic.getRecordEntryId(formContextId, dao);
		}

		Map<String, String> headers = getDisplayHeader(container);
		while (recordEntryIdResultSet.next()) {
			logger.info(":----------------------------------------------------Within recordEntries");

			Long recordEntryIdValue = recordEntryIdResultSet.getLong(1);

			logger.info(":----------------------------------------------------Within map");

			FormGridObject gridObject = factory.createFormGridObject();
			gridObject.setRecordEntryId(recordEntryIdValue);
			Long dynamicRecEntryId = recordEntryIdResultSet.getLong(2);
			StringBuilder formUrlString = new StringBuilder(formUrl);
			formUrlString.append(DEConstants.RECORD_ID_URL_PARAM).append(dynamicRecEntryId).append("&recordEntryId=")
					.append(recordEntryIdValue);
			gridObject.setFormURL(formUrlString.toString());//formUrl is used when we need to open the form in edit mode
			StringBuilder deURL = new StringBuilder();
			gridObject.setDeUrl(deURL.append(containerId).append(",").append(dynamicRecEntryId).toString());//deUrl is used when we want to print the form
			gridObject.setColumns(getDisplayValue(dynamicRecEntryId, container));
			gridObject.setHeaders(headers);
			gridObjectList.add(gridObject);

		}
		Collections.sort(gridObjectList);
		} finally {
			dao.close(recordEntryIdResultSet);
		}
		return gridObjectList;
	}

	public Long getContainerIdByFormContextId(Long formContextId, SessionDataBean sessionDataBean)
			throws DynamicExtensionsSystemException, DAOException, JAXBException, SAXException, SQLException {
		JdbcDao dao = new JdbcDao();
		ResultSet rowData = null;
		Long containerId = null;
		try {
			rowData = dao.getResultSet(
					SQLQueryManager.getInstance().getSQL(DEConstants.CONTAINER_ID_FROM_FORM_CONTEXT_ID),
					Collections.singletonList(formContextId));
			
			if (rowData.next()) {
				containerId = rowData.getLong(1);
			}
		} finally {
			dao.close(rowData);
		}

		return containerId;
	}

	/**
	 * This method returns
	 * @param recordIdentifier
	 * @param container
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Map<String, String> getDisplayValue(Long recordIdentifier, Container container)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {

		FormDataManager formDataManager = new FormDataManagerImpl();
		return this.populateDisplayValue(formDataManager.getFormData(container, recordIdentifier));
	}

	/**
	 * populates the map with respective attribute name and it's value. Map<AttributeName,value>
	 * @param formData
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private Map<String, String> populateDisplayValue(FormData formData) {
		Map<String, String> showInGridValues = new HashMap<String, String>();

		for (ControlValue controlValue : formData.getFieldValues()) {

			Control control = controlValue.getControl();
			if (control instanceof SubFormControl) {
				if (!((SubFormControl) control).isCardinalityOneToMany()) {
					List<FormData> formDataList = (List<FormData>) controlValue.getValue();
					if (formDataList.size() > 0) {
						showInGridValues.putAll(populateDisplayValue(formDataList.get(0)));
					}

				}
			} else if (control.showInGrid()) {

				String header = control.getCaption();
				if (controlValue.getValue() instanceof String[]) {
					if (((String[]) controlValue.getValue()).length > 0) {
						showInGridValues.put(header, ((String[])controlValue.getValue())[0]);
					}
				} else {
					showInGridValues.put(header, controlValue.getValue().toString());
				}
			}
		}
		return showInGridValues;
	}

	/**
	 * This will get the attribute name which are marked as showInGrid and will show them as header in the grid
	 * @param container
	 * @return
	 */
	public static Map<String, String> getDisplayHeader(Container container) {
		Map<String, String> showInGridHeaders = new HashMap<String, String>();

		StringBuilder dataType = new StringBuilder();
		for (Control control : container.getControls()) {
			if (!control.showInGrid()) {
				continue;
			}
			dataType.append(control.getDataType().toString());
			if (control instanceof SelectControl) {
				{
					dataType.append(DEConstants.PV_POSTFIX);
				}
			}
			showInGridHeaders.put(control.getCaption(), dataType.toString());

		}

		return showInGridHeaders;
	}

}
