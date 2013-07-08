
package edu.common.dynamicextensions.action.core;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.dao.newdao.ActionStatus;

public class UploadFile extends HttpServlet {

	private static final String UPLOADED_FILE_NAME = Variables.fileUploadDir + File.separator + "DE_uploadedFile%d%s";
	private static final long serialVersionUID = -331996445389885677L;

	/**
	 * Execute.
	 *
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 *
	 * @return the action forward
	 *
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			List<DiskFileItem> items = upload.parseRequest(request);
			Iterator<DiskFileItem> iter = items.iterator();
			FileItem item = iter.next();
			String contentType = item.getContentType();
			String fileName = String.format(UPLOADED_FILE_NAME, UUID.randomUUID().getMostSignificantBits(),
					item.getName());
			item.write(new File(fileName));
			updateResponse(response, fileName, contentType);
			request.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.SUCCESSFUL);
		} catch (Exception e) {
			throw new RuntimeException("Exception occured while uploading file.", e);
		}
	}

	/**
	 * Update response.
	 *
	 * @param response the response
	 * @param fileName the fileName
	 * @param responseList the response list
	 * @param contentType the content type
	 * @throws DynamicExtensionsSystemException
	 *
	 */
	private void updateResponse(HttpServletResponse response, String fileName, String contentType) {
		try {
			response.setContentType("text/html");
			response.flushBuffer();

			JSONObject jsonResponse = new JSONObject().put("uploadedFileId", fileName);
			jsonResponse.put("contentType", contentType);

			response.getWriter().write(jsonResponse.toString());
		} catch (Exception e) {
			throw new RuntimeException("Exception occured while uploading file.", e);
		}
	}
}
