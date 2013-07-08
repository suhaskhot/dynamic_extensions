
package edu.common.dynamicextensions.action.core;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.bizlogic.FormObjectGridDataBizLogic;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.newdao.ActionStatus;

public class DisplayFormDataInGridAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		HttpSession session = request.getSession();
		SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(DEConstants.SESSION_DATA);
		try {
			FormObjectGridDataBizLogic bizLogic = new FormObjectGridDataBizLogic();
			Long formContextId = (Long) request.getAttribute(DEConstants.FORM_CONTEXT_ID);
			Long containerId = bizLogic.getContainerIdByFormContextId(formContextId, sessionDataBean);
			final Container containerInterface = Container.getContainer(containerId);
			request.setAttribute(Constants.GRID_HEADERS,
					FormObjectGridDataBizLogic.getDisplayHeader(containerInterface));

			String destination = "/pages/de/DisplayFormDataInGrid.jsp";

			RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
			request.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.SUCCESSFUL);
			rd.forward(request, response);

		} catch (DynamicExtensionsSystemException e) {
			throw new ServletException(e);
		} catch (DAOException e) {
			throw new ServletException(e);
		} catch (JAXBException e) {
			throw new ServletException(e);
		} catch (SAXException e) {
			throw new ServletException(e);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

	}
}