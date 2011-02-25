package edu.common.dynamicextensions.dem;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class DataAssociationHandler extends AbstractHandler {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {

			initAuditManager();
			initializeParamaterObjectMap(req);
			DyanamicObjectProcessor dyanamicObjectProcessor = new DyanamicObjectProcessor();
			dyanamicObjectProcessor.associateObjects(paramaterObjectMap);


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
}
