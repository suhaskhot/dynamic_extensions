/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.common.dynamicextensions.action.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.dao.newdao.ActionStatus;


public class DownloadFileAction extends HttpServlet {

	private static final long serialVersionUID = 2241321869463498357L;

	private static final String CONTROL_NAME = "controlName";

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	/**
	 * This method is used to download files saved in database.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Stack<FormData> formDataStack = (Stack<FormData>) CacheManager.getObjectFromCache(req,
				DEConstants.FORM_DATA_STACK);
		String controlName = req.getParameter(CONTROL_NAME);
		ControlValue controlValue = formDataStack.peek().getFieldValueByHTMLName(controlName);
		Long recordIdentifier = Long.valueOf(req.getParameter(DEConstants.RECORD_IDENTIFIER));
		String filename = ((FileControlValue) controlValue.getValue()).getFileName();
		FileUploadControl fileControlValue = (FileUploadControl) controlValue.getControl();
		InputStream byteStream = null;
		try {
			FormDataManagerImpl formDataManagerImpl = new FormDataManagerImpl();
			res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
			res.setContentType("application/x-unknown");
			byteStream = formDataManagerImpl.getFileData(recordIdentifier, fileControlValue)
					.getBinaryStream();

			ServletOutputStream responseOutputStream = res.getOutputStream();
			IOUtils.copy(byteStream, responseOutputStream);
			req.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.SUCCESSFUL);
		} catch (Exception e) {
			new RuntimeException("Problem reading file " + filename, e);
		} finally {
			if (byteStream != null) {
				byteStream.close();
			}
		}
	}

}
