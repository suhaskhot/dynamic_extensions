package edu.common.dynamicextensions.rest;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.ContainerInfo;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.nutility.ContainerJsonSerializer;
import edu.common.dynamicextensions.nutility.ContainerSerializer;


@Path("/forms")
public class FormResource {
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getForms() {
		List<ContainerInfo> forms = Container.getContainerInfo();
		return Response.ok(forms).build();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getForm(@PathParam("id") Long id) {
		Response response = null;
		
		try {
			Container form = Container.getContainer(id);
			if (form == null) {
				response = Response.status(Status.NOT_FOUND).build();
			} else {
				StringWriter writer = new StringWriter();
				
				ContainerSerializer serializer = new ContainerJsonSerializer(form, writer);
				serializer.serialize();
				
				response = Response.ok(writer.toString()).build();
			}			
		} catch (Exception e) {
			response = Response.serverError().build();
		}
		
		return response;
	}
	
	// TODO: Temporary for demo purpose
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data")
	public Response getData(@PathParam("formId") Long formId) {
		Container container = Container.getContainer(formId);
		Collection<List<Control>> ctrls = container.getControlsGroupedByRow();
		
		final Control[] resultCtrls = new Control[3];
		
		int i = 0;
		for (List<Control> row : ctrls) {
			for (Control ctrl : row) {
				if (ctrl instanceof SubFormControl || ctrl instanceof MultiSelectControl || ctrl instanceof Label || ctrl instanceof PageBreak) {
					continue;
				}
				
				resultCtrls[i++] = ctrl;
				
				if (i == 3) {
					break;
				}
			}
			
			if (i == 3) {
				break;
			}
		}
		
		String table = container.getDbTableName();
		StringBuilder cols = new StringBuilder();
		for (int j = 0; j < i; ++j) {
			cols.append(resultCtrls[j].getDbColumnName()).append(",");
		}
		cols.append("identifier");
		
		String sql = "select " + cols.toString() + " from " + table;
		final int numCols = i + 1;
		List<List<Object>> records = JdbcDaoFactory.getJdbcDao().getResultSet(sql, null, new ResultExtractor<List<List<Object>>>() {
			@Override
			public List<List<Object>> extract(ResultSet rs) throws SQLException {
				List<List<Object>> rows = new ArrayList<List<Object>>();
				while (rs.next()) {
					List<Object> row = new ArrayList<Object>();
					row.add(rs.getLong(numCols));
					for (int i = 1; i < numCols; ++i) {
						if (rs.getObject(i) != null) {
							row.add(resultCtrls[i - 1].toString(rs.getObject(i)));
						} else {
							row.add(null);
						}
					}
					
					rows.add(row);
				}
				
				return rows;
			}
		});
		
		String[] heading  = new String[numCols];
		heading[0] = "Record ID";
		for (int j = 0; j < i; ++j) {
			heading[j + 1] = resultCtrls[j].getCaption();
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("caption", container.getCaption());
		result.put("id", container.getId());
		result.put("heading", heading);
		result.put("records", records);
		
		return Response.ok(new Gson().toJson(result)).build();		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data/{recordId}")
	public Response getData(@PathParam("formId") Long formId, @PathParam("recordId") Long recordId) {
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		FormData formData = formDataMgr.getFormData(formId, recordId);		
		return Response.ok(formData.toJson()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data")
	public Response saveData(@PathParam("formId") Long formId, String formJson) {
		FormData formData = FormData.fromJson(formJson);
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		Long recordId = formDataMgr.saveOrUpdateFormData(null, formData);		
		return Response.ok(Collections.singletonMap("id", recordId)).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data/{recordId}")
	public Response updateData(@PathParam("formId") Long formId, String formJson) {
		FormData formData = FormData.fromJson(formJson);
		FormDataManager formDataMgr = new FormDataManagerImpl(false);
		Long recordId = formDataMgr.saveOrUpdateFormData(null, formData);		
		return Response.ok(Collections.singletonMap("id", recordId)).build();
	}
}
