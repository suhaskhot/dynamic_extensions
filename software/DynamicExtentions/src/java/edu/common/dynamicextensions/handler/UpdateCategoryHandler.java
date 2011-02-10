package edu.common.dynamicextensions.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.common.dynamicextensions.dem.AbstractHandler;
import edu.common.dynamicextensions.dem.DyanamicObjectProcessor;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class UpdateCategoryHandler extends AbstractHandler {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {

			initAuditManager();
			initializeParamaterObjectMap(req);
			updateCategory(paramaterObjectMap);

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DynamicExtensionsApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DynamicExtensionsSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void updateCategory(Map<String, Object> paramaterObjectMap) throws DynamicExtensionsSystemException, DAOException {
		HibernateDAO hibernateDao  = DynamicExtensionsUtility.getHibernateDAO();
		hibernateDao.getSession().merge(paramaterObjectMap.get(WebUIManagerConstants.CATEGORY));
		hibernateDao.commit();
		hibernateDao.closeSession();
	}
}