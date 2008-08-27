
package edu.common.dynamicextensions.util.global;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.action.BaseDynamicExtensionsAction;
import edu.common.dynamicextensions.util.DynamicExtensionsCacheManager;
import edu.wustl.common.beans.NameValueBean;

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

				
		List<NameValueBean> nameValueBeanList = null;
		for (ControlInterface control : containerInterface.getControlCollection())
		{
			if (Long.parseLong(controlId) == control.getId())
			{
				nameValueBeanList = ControlsUtility.populateListOfValues(control);
			}
		}

		/*UserDefinedDE definedDE = (UserDefinedDE) controlInterface.getAttibuteMetadataInterface().getDataElement();

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
*/
		//sint count = 0;
		Integer total = limitFetch + startFetch;
		trialJSONObject.put("totalCount", nameValueBeanList.size());
		
		for (int i = startFetch; i < total && i < nameValueBeanList.size(); i++)
		{
			JSONObject trialJSONObjectNewObj = new JSONObject();
			trialJSONObjectNewObj.put("id", nameValueBeanList.get(i).getValue());
			trialJSONObjectNewObj.put("field", nameValueBeanList.get(i).getName());
			trialJSONArray.put(trialJSONObjectNewObj);
		}
		trialJSONObject.put("row", trialJSONArray);
		response.flushBuffer();
		PrintWriter out = response.getWriter();
		out.write(trialJSONObject.toString());

		return null;
	}

}
