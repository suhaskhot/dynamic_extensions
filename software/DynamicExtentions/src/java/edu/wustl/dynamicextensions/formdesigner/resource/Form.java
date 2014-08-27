
package edu.wustl.dynamicextensions.formdesigner.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.common.dynamicextensions.nutility.BOUtil;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.java.common.CacheControl;
import com.java.common.CachePolicy;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;
import edu.common.dynamicextensions.nutility.IoUtil;
import edu.wustl.dynamicextensions.formdesigner.mapper.Properties;
import edu.wustl.dynamicextensions.formdesigner.resource.facade.ContainerFacade;
import edu.wustl.dynamicextensions.formdesigner.usercontext.AppUserContextProvider;
import edu.wustl.dynamicextensions.formdesigner.usercontext.CSDProperties;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;
import edu.wustl.dynamicextensions.formdesigner.utility.Utility;

@Controller
@RequestMapping("/de-forms")
public class Form {

	@Autowired
	private HttpServletRequest request;
	
	private static final String CONTAINER_SESSION_ATTR = "sessionContainer";

	private static AtomicInteger formCnt = new AtomicInteger();

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> createForm(@RequestBody Map<String, Object> propertiesMap) throws JSONException {
		Transaction txn = null;
		Properties formProps = new Properties(propertiesMap);		
		try {
			UserContext userData = CSDProperties.getInstance().getUserContextProvider().getUserContext(request);
			
			ContainerFacade containerFacade = ContainerFacade.createContainer(formProps, userData);
			request.getSession().removeAttribute(CONTAINER_SESSION_ATTR);
			request.getSession().setAttribute(CONTAINER_SESSION_ATTR, containerFacade);
			String save = formProps.getString("save");

			if (save.equalsIgnoreCase("yes")) {
				txn = TransactionManager.getInstance().startTxn();
				containerFacade.persistContainer();
				TransactionManager.getInstance().commit(txn);
			}

			formProps = containerFacade.getProperties();
			formProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
			return formProps.getAllProperties();
		} catch (Exception ex) {
			ex.printStackTrace();
			propertiesMap.put("status", "error");
			if (txn != null) {
				TransactionManager.getInstance().rollback(txn);
			}
			
			return propertiesMap;
		} 
	}

	@RequestMapping(method = RequestMethod.GET, value="{id}/{edit}")
	@ResponseStatus(HttpStatus.OK)
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public Map<String, Object> getForm(
			@PathVariable(value="id") String id,
			@PathVariable(value="edit") boolean edit) {
		
		Transaction txn = null;
		Properties containerProps = null;
		try {
			txn = TransactionManager.getInstance().startTxn();
			UserContext userData = CSDProperties.getInstance().getUserContextProvider().getUserContext(request);
			ContainerFacade container = ContainerFacade.loadContainer(Long.valueOf(id), userData, edit);

			request.getSession().setAttribute(CONTAINER_SESSION_ATTR, container);
			containerProps = container.getProperties();
			if (container.getCreatedBy() != null) {
				if (container.getCreatedBy().equals(userData.getUserId())) {
					edit = true;
				}
			}
			if (edit) {
				containerProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
			} else {
				containerProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_NEW);
			}

			return containerProps.getAllProperties();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.<String, Object>singletonMap("status", "error");
		} finally {
			if (txn != null) {
				TransactionManager.getInstance().rollback(txn);
			}
		}

	}

	
	@RequestMapping(method = RequestMethod.GET, value="preview")
	@ResponseStatus(HttpStatus.OK)
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public String getPreview() {
		try {
			ContainerFacade containerFacade = (ContainerFacade) request.getSession().getAttribute(
					CONTAINER_SESSION_ATTR);

			File previewFile = new File(request.getSession().getServletContext().getRealPath(File.separator)
					+ "csd_web" + File.separator + "pages" + File.separator + "preview.html");

			FileInputStream previewFileInputStream = new FileInputStream(previewFile);

			String previewString = IOUtils.toString(previewFileInputStream, "UTF-8");

			return previewString.replace("{{content}}",
					containerFacade.getHTML(request).replaceAll("images/", "../../images/"));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error";
		} finally {
			//request.getSession().removeAttribute(CONTAINER_SESSION_ATTR);
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object>  editForm(@RequestBody Map<String, Object> propertiesMap) {
		
		Transaction txn = null;
		try {
			Properties formProps = new Properties(propertiesMap);
			String save = formProps.getString("save");
			ContainerFacade container = (ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR);
			container.updateContainer(formProps);
			if (save.equalsIgnoreCase("yes")) {
				txn = TransactionManager.getInstance().startTxn();
				container.persistContainer();
				TransactionManager.getInstance().commit(txn);
			}
			formProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);

            Integer containerId = (Integer) propertiesMap.get("id");
            BOUtil.getInstance().getGenerator().update(containerId.longValue());

            return formProps.getAllProperties();
		} catch (Exception ex) {
			propertiesMap.put("status", "error");
			ex.printStackTrace();
			if (txn != null) {
				TransactionManager.getInstance().rollback(txn);
			}
			return propertiesMap;
		} 
	}

	@RequestMapping(method = RequestMethod.POST, value="permissibleValues", produces="text/plain")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadFile(@PathVariable("file") MultipartFile file) {
		String output = "{\"status\" : \"error\"}";
		try {
			// get temp location programatically.
			if (file.getOriginalFilename() != null) {
				String uploadedFileLocation = "/tmp/" + new Date().getTime() + file.getOriginalFilename();
				Utility.saveStreamToFileInTemp(file.getInputStream(), uploadedFileLocation);
				output = "{\"status\": \"saved\", \"file\" : \"" + uploadedFileLocation + "\"}";
			}
		} catch (Exception ex) {
			return output;
		}

		return output;

	}

	@RequestMapping(method = RequestMethod.GET, value="currentuser")
	@ResponseStatus(HttpStatus.OK)
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public String getCurrentUser() throws Exception {
		AppUserContextProvider contextProvider = CSDProperties.getInstance().getUserContextProvider();
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("userName",
				contextProvider.getUserNameById(contextProvider.getUserContext(request).getUserId()));

		return responseJSON.toString();

	}

	@RequestMapping(method = RequestMethod.GET, value="permissibleValues/{controlName}")
	@ResponseStatus(HttpStatus.OK)
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public void getPermissibleValues(@PathVariable(value="controlName") String controlName,
			HttpServletResponse response) throws IOException {

		ContainerFacade container = (ContainerFacade) request.getSession().getAttribute(CONTAINER_SESSION_ATTR);
		File pvFile = container.getPvFile(controlName);
		
		response.setHeader("Content-Disposition", "attachment;filename=" + pvFile.getName());
			
		InputStream in = null;
		try {
			in = new FileInputStream(pvFile.getPath());
			IoUtil.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="formxml")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String importForm(@PathVariable("file") MultipartFile file) throws JSONException {
		JSONObject output = new JSONObject();

		String tmpDirName = getTmpDirName();
		Boolean createTables = true;		
		Transaction txn = null;
		
		try {

			Utility.downloadFile(file.getInputStream(), tmpDirName, "forms.xml", false);

			//
			// Once the zip is extracted, following will be directory layout
			// temp-dir
			//   |___ form-dir (created from zip)
			//           |____ form1.xml
			//           |____ form2.xml
			//           |____ pvs
			//
			File tmpDir = new File(tmpDirName);
			//for (File formDir : tmpDir.listFiles()) {

			String[] formFileNames = tmpDir.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});

			String formDirPath = new StringBuilder(tmpDir.getAbsolutePath()).append(File.separator).toString();
			String pvDirPath = new StringBuilder(formDirPath).append("pvs").toString();
			List<Long> containerIds = new ArrayList<Long>();
			txn = TransactionManager.getInstance().startTxn();
			for (String formFile : formFileNames) {
				String formFilePath = new StringBuilder(formDirPath).append(formFile).toString();
				UserContext userCtxt = CSDProperties.getInstance().getUserContextProvider().getUserContext(request);
				Long containerId = Container.createContainer(userCtxt, formFilePath, pvDirPath, createTables);
				containerIds.add(containerId);
			}
			TransactionManager.getInstance().commit(txn);
			output.put("status", "success");
			output.put("containerIds", containerIds);

			//	}

		} catch (Exception ex) {
			output.put("status", "error");
			if (txn != null) {
				TransactionManager.getInstance().rollback(txn);
			}
		}

		return output.toString();
	}


	private String getTmpDirName() {
		return new StringBuilder().append(System.getProperty("java.io.tmpdir")).append(File.separator)
				.append(System.currentTimeMillis()).append(formCnt.incrementAndGet()).append("create").toString();
	}
}
