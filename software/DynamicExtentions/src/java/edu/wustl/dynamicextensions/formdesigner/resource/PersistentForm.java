
package edu.wustl.dynamicextensions.formdesigner.resource;


import javax.ws.rs.Path;

@Path("/pform")
public class PersistentForm
{

	/*private static final String CONTAINER_SESSION_ATTR = "sessionContainer";
	
	private JDBCDAO jdbcDao;

	private HibernateDAO hibernateDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getForms(final @Context HttpServletRequest request,
			@Context HttpServletResponse response) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("id", "newContianerCollection");
		json.put("status", "successful");
		JSONArray container = new JSONArray();
		for (int i = 0; i < 2; i++)
		{
			JSONObject jsn = new JSONObject();
			jsn.put("id", 24 + i);
			jsn.put("name", "domo_" + i);
			container.put(jsn);
		}
		json.put("containerCollection", container);

		return json.toString();

	}

	

	
	
	private void closeDao()
	{
		DynamicExtensionsUtility.closeDAO(jdbcDao, false);
		DynamicExtensionsUtility.closeDAO(hibernateDao, false);
	}

	@SuppressWarnings("deprecation")
	private void intializeDao() throws DynamicExtensionsSystemException
	{
		jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
		hibernateDao = DynamicExtensionsUtility.getHibernateDAO();
	}
	
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
	}*/
}
