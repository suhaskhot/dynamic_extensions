
package edu.common.dynamicextensions.util.global;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.webui.action.BaseDynamicExtensionsAction;
import edu.common.dynamicextensions.util.DynamicExtensionsCacheManager;

/**
 * @author kunal_kamble
 * This action is called in the auto complete dropdown combo box
 *
 */
public class ComboDataAction extends BaseDynamicExtensionsAction
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String limit = request.getParameter("limit");
		String query = request.getParameter("query");
		String start = request.getParameter("start");
		String controlId = request.getParameter("controlId").split("~")[0].trim();
		String containerId = request.getParameter("controlId").split("~")[1].split("=")[1].trim();

		Integer limitFetch = Integer.parseInt(limit);
		Integer startFetch = Integer.parseInt(start);

		JSONArray trialJSONArray = new JSONArray();
		JSONObject trialJSONObject = new JSONObject();

		DynamicExtensionsCacheManager deCacheManager = DynamicExtensionsCacheManager.getInstance();
		ContainerInterface containerInterface = (ContainerInterface) ((HashMap) deCacheManager.getObjectFromCache(Constants.LIST_OF_CONTAINER))
				.get(Long.parseLong(containerId));

		ControlInterface controlInterface = null;
		for (ControlInterface control : containerInterface.getControlCollection())
		{
			if (Long.parseLong(controlId) == control.getId())
			{
				controlInterface = control;
			}
		}

		UserDefinedDE definedDE = (UserDefinedDE) controlInterface.getAttibuteMetadataInterface().getDataElement();

		Set<PermissibleValueInterface> set = new HashSet<PermissibleValueInterface>(definedDE.getPermissibleValueCollection());

		int count = 0;
		Integer total = limitFetch + startFetch;

		List<String> permissibleValues = new ArrayList<String>();
		for (PermissibleValueInterface permissibleValueInterface : set)
		{
			if (count >= total)
			{
				break;
			}
			if (permissibleValueInterface.getValueAsObject().toString().toLowerCase().startsWith(query.toLowerCase()) || query.length() == 0)
			{
				permissibleValues.add(permissibleValueInterface.getValueAsObject().toString());
				count++;
			}

		}

		trialJSONObject.put("totalCount", set.size());

		for (int i = startFetch; i < total && i < permissibleValues.size(); i++)
		{
			JSONObject trialJSONObjectNewObj = new JSONObject();
			trialJSONObjectNewObj.put("id", new Integer(i));
			trialJSONObjectNewObj.put("field", permissibleValues.get(i));
			trialJSONArray.put(trialJSONObjectNewObj);
		}
		trialJSONObject.put("row", trialJSONArray);

		// System.out.println("----json object---"+trialJSONObject.toString());	

		response.setContentType("text/javascript");
		PrintWriter out = response.getWriter();
		out.write(trialJSONObject.toString());

		return null;
	}

}
