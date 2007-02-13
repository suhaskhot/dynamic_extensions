/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;

/**
 * @author chetan_patil
 *
 */
public class UploadFileAction extends BaseDynamicExtensionsAction
{

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DynamicExtensionsApplicationException
	{
		ControlsForm controlsForm = (ControlsForm) form;
		FormFile file = controlsForm.getCsvFile();

		String returnXML = "";
		try
		{
			if (file != null && file.getFileSize() > 0)
			{
				byte[] fileContents = file.getFileData();
				String fileContentString = new String(fileContents);
				String[] rowsStrings = fileContentString.split("\n");
				for (int i = 0; i < rowsStrings.length; i++)
				{
					returnXML += rowsStrings[i] + "|";
				}
			}
			request.setAttribute("xmlString", returnXML);
			sendResponse(returnXML, response);
		}
		catch (Exception e)
		{
			String actionForwardString = catchException(e, request);
			if ((actionForwardString == null) || (actionForwardString.equals("")))
			{
				return mapping.getInputForward();
			}
			return (mapping.findForward(actionForwardString));
		}
		return mapping.findForward("success");
	}

	/**
	 * 
	 * @param responseXML
	 * @param response
	 * @throws IOException
	 */
	private void sendResponse(String responseXML, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		out.write(responseXML);
	}

}
