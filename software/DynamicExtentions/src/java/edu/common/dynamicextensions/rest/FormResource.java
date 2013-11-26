package edu.common.dynamicextensions.rest;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

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

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.ContainerInfo;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
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
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data/{recordId}")
	public Response getData(@PathParam("formId") Long formId, @PathParam("recordId") Long recordId) {
		FormDataManager formDataMgr = new FormDataManagerImpl();
		FormData formData = formDataMgr.getFormData(formId, recordId);		
		return Response.ok(formData.toJson()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data")
	public Response saveData(@PathParam("formId") Long formId, String formJson) {
		FormData formData = FormData.fromJson(formJson);
		FormDataManager formDataMgr = new FormDataManagerImpl();
		Long recordId = formDataMgr.saveOrUpdateFormData(null, formData);		
		return Response.ok(Collections.singletonMap("id", recordId)).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{formId}/data")
	public Response updateData(@PathParam("formId") Long formId, String formJson) {
		FormData formData = FormData.fromJson(formJson);
		FormDataManager formDataMgr = new FormDataManagerImpl();
		Long recordId = formDataMgr.saveOrUpdateFormData(null, formData);		
		return Response.ok(Collections.singletonMap("id", recordId)).build();
	}
}
