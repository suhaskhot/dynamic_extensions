
package edu.wustl.dynamicextensions.formdesigner.resource;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dynamicextensions.formdesigner.mapper.Properties;
import edu.wustl.dynamicextensions.formdesigner.resource.facade.ContainerFacade;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

@Path("/form")
public class Form
{

	private static final String CONTAINER_SESSION_ATTR = "sessionContainer";

	private JDBCDAO jdbcDao;

	private HibernateDAO hibernateDao;

	/**
	 * Create a new Form
	 * @param formJson
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createForm(String formJson, final @Context HttpServletRequest request,
			@Context HttpServletResponse response) throws JSONException
	{
		System.out.println(formJson);
		JSONObject formJSON = new JSONObject();
		String jsonResponseString = "";
		try
		{
			Properties formProps = new Properties(new ObjectMapper().readValue(formJson,
					HashMap.class));

			if (request.getSession().getAttribute(CONTAINER_SESSION_ATTR) == null)
			{
				ContainerFacade form = ContainerFacade.createContainer(formProps);
				request.getSession().setAttribute(CONTAINER_SESSION_ATTR, form);
				/*formJSON.put("status", "success");
				jsonResponseString = formJSON.toString();*/
			}
			/*else
			{*/
			intializeDao();
			ContainerFacade containerFacade = (ContainerFacade) request.getSession().getAttribute(
					CONTAINER_SESSION_ATTR);
			containerFacade.persistContainer();
			commitDao();
			formProps = containerFacade.getProperties();
			formProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
			Writer strWriter = new StringWriter();
			new ObjectMapper().writeValue(strWriter, formProps.getAllProperties());
			jsonResponseString = strWriter.toString();
			//}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			formJSON.put("status", "error");
			jsonResponseString = formJSON.toString();
			closeDao();
		}
		return jsonResponseString;
	}

	/**
	 * Retrieve a Form
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getForm(@PathParam("id") String id, final @Context HttpServletRequest request,
			@Context HttpServletResponse response) throws JSONException
	{
		System.out.println(id);
		try
		{
			intializeDao();
			ContainerFacade container = ContainerFacade.loadContainer(Long.valueOf(id));

			request.getSession().setAttribute(CONTAINER_SESSION_ATTR, container);
			Properties containerProps = container.getProperties();
			containerProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
			Writer strWriter = new StringWriter();
			new ObjectMapper().writeValue(strWriter, containerProps.getAllProperties());
			return strWriter.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return "{'status' : 'error'}";
		}
		finally
		{
			closeDao();
		}

	}

	/**
	 * Edit a Form
	 * @param formJson
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String editForm(String formJson, final @Context HttpServletRequest request,
			@Context HttpServletResponse response)
	{
		try
		{
			intializeDao();
			Properties formProps = new Properties(new ObjectMapper().readValue(formJson,
					HashMap.class));
			((ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR))
					.updateContainer(formProps);
			((ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR))
					.persistContainer();
			commitDao();

			Writer strWriter = new StringWriter();
			new ObjectMapper().writeValue(strWriter, ((ContainerFacade) request.getSession()
					.getAttribute(CONTAINER_SESSION_ATTR)).getProperties().getAllProperties());
			return strWriter.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return "{'status' : 'error'}";
		}
		finally
		{
			closeDao();
		}

	}

	/**
	 * Delete a control in a form
	 * @param name
	 * @param request
	 * @param response
	 * @return
	 */
	@Path("/control/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@DELETE
	public String deleteControl(@PathParam("name") String name,
			final @Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		((ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR))
				.deleteControl(name);
		return "{'status' : 'deleted'}";
	}

	/**
	 * @throws DAOException
	 */
	private void commitDao() throws DAOException
	{
		try
		{
			jdbcDao.commit();
			hibernateDao.commit();
		}
		finally
		{
			closeDao();
		}
	}

	/**
	 * 
	 */
	private void closeDao()
	{
		DynamicExtensionsUtility.closeDAO(jdbcDao, false);
		DynamicExtensionsUtility.closeDAO(hibernateDao, false);
	}

	/**
	 * @throws DynamicExtensionsSystemException
	 */
	@SuppressWarnings("deprecation")
	private void intializeDao() throws DynamicExtensionsSystemException
	{
		jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
		hibernateDao = DynamicExtensionsUtility.getHibernateDAO();
	}

	/*
	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/control")
	public String createControl(String controlJson, @Context HttpServletRequest request)
			throws JSONException
	{
		JSONObject controlJSON = new JSONObject();
		try
		{
			Map<String, Object> controlProps = new ObjectMapper().readValue(controlJson,
					HashMap.class);

			if (request.getSession().getAttribute(CONTAINER_SESSION_ATTR) == null)
			{
				controlJSON.put("status", "failed");
				return controlJSON.toString();
			}
			else
			{
				((ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR))
						.createControl(controlProps);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			controlJSON.put("status", "failed");
			return controlJSON.toString();
		}

		controlJSON.put("status", "success");
		return controlJSON.toString();
	}

	@GET
	@Path("/control/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getControl(@PathParam("name") String name,
			final @Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		System.out.println("findByName " + name);
		try
		{
			if (request.getSession().getAttribute(CONTAINER_SESSION_ATTR) == null)
			{
				return "{'status' : 'error'}";
			}
			else
			{
				Map<String, Object> propsMap = ((ContainerFacade) request.getSession()
						.getAttribute(CONTAINER_SESSION_ATTR)).getControlProperties(name,
						CSDConstants.STRING_TEXT_FIELD);
				propsMap.put("status", "success");
				Writer strWriter = new StringWriter();
				new ObjectMapper().writeValue(strWriter, propsMap);
				return strWriter.toString();
			}
		}
		catch (Exception ex)
		{
			return "{'status' : 'error'}";
		}

	}

	@SuppressWarnings("unchecked")
	@PUT
	@Path("/control/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String editControl(String controlJson, @PathParam("name") String name,
			final @Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		System.out.println("findByName " + name);
		try
		{
			Map<String, Object> controlProps = new ObjectMapper().readValue(controlJson,
					HashMap.class);
			((ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR))
					.editControl(name, controlProps);
			controlProps.put("status", "success");

			Writer strWriter = new StringWriter();
			new ObjectMapper().writeValue(strWriter, ((ContainerFacade) request.getSession()
					.getAttribute(CONTAINER_SESSION_ATTR)).getControlProperties(name,
					CSDConstants.STRING_TEXT_FIELD));
			return strWriter.toString();

		}
		catch (Exception ex)
		{
			return "{'status' : 'error'}";
		}

	}*/
}
