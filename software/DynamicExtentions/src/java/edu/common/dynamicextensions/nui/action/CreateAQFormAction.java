package edu.common.dynamicextensions.nui.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;
import edu.common.dynamicextensions.nutility.FormPostProcessor;
import edu.common.dynamicextensions.nutility.FormProperties;
import edu.common.dynamicextensions.util.DirOperationsUtility;
import edu.common.dynamicextensions.util.DownloadUtility;
import edu.wustl.dynamicextensions.formdesigner.usercontext.CSDProperties;

public class CreateAQFormAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(CreateAQFormAction.class);
	
	private static AtomicInteger formCnt = new AtomicInteger();
	
	private static final String SORT_ORDER_PROPERTIES_FILE = "sortOrder.properties";

	protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp)
	throws ServletException, IOException {			
		
		ActionResponse actionResponse = null;
		
		String tmpDirName = getTmpDirName();
		try	{			
			DownloadUtility.downloadZipFile(httpReq, tmpDirName, "forms.zip");
			logger.info("Download input forms zip file to " + tmpDirName);
			String createTablesParam = httpReq.getParameter("create_tables");
			final String loginName = httpReq.getParameter("login_name");

			boolean createTables = true;
			if (createTablesParam != null && createTablesParam.equals("false")) {
				createTables = false;
			}

			//
			// Once the zip is extracted, following will be directory layout
			// temp-dir
			//   |___ form-dir (created from zip)
			//           |____ form1.xml
			//           |____ form2.xml
			//           |____ pvs
			//
			File tmpDir = new File(tmpDirName);	
		
			for (File formDir : tmpDir.listFiles()) {
				if (!formDir.isDirectory()) {
					continue;
				}

				String sortOrderFileName = formDir.getAbsolutePath() + File.separator + SORT_ORDER_PROPERTIES_FILE;
				File sortOrderFile = new File(sortOrderFileName);
				
				if (!sortOrderFile.exists()) {
					throw new RuntimeException("Expected sort order file not found! " + sortOrderFileName);
				}
				
				Properties sortOrderProperties = loadSortOrderProperties(sortOrderFile);
				
				String[] formFileNames = formDir.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".xml");
					}
				});

				String formDirPath = new StringBuilder(formDir.getAbsolutePath()).append(File.separator).toString();
				String pvDirPath = new StringBuilder(formDirPath).append("pvs").toString();
				for(String formFile : formFileNames) {
					logger.info("Create form using definition in " + formFile);
					String formFilePath = new StringBuilder(formDirPath).append(formFile).toString(); 
					UserContext ctxt = CSDProperties.getInstance().getUserContextProvider().getUserContext(loginName);
					Transaction txn = null;
					Long containerId = null;
					
					try {
						txn = TransactionManager.getInstance().newTxn();
						containerId = Container.createContainer(ctxt, formFilePath, pvDirPath, createTables);
						logger.info("Form for definition in " + formFile + " created. Id = " + containerId);
						TransactionManager.getInstance().commit(txn);
					} catch (Exception e) {
						TransactionManager.getInstance().rollback(txn);
					} 
					
					if (containerId != null) {
						FormPostProcessor postProcessor = FormProperties.getInstance().getPostProcessor();								 
						String formName = Container.getContainer(containerId).getName();
						Integer sortOrder;
						
						try {
							String sortOrderId = sortOrderProperties.getProperty(formName).toString().replace(" ", "");
							
							if (sortOrderId == null) {
								throw new RuntimeException("Sort order is not provided for the form: " + formName);
							}
							
							sortOrder = Integer.parseInt(sortOrderId);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						
						logger.info("Adding sort order for the form " + formName + " sort-id is " + sortOrder);
						postProcessor.process(containerId, sortOrder);
					}
					
					logger.info("Form for definition in " + formFile + " created. Id = " + containerId);
				}				
			}
			
			actionResponse = new ActionResponse();
			actionResponse.setSuccess();
		} catch (Exception e){
			logger.error("Error creating forms. None of the forms are created.", e);
			actionResponse = getActionResponse(e);
		} finally {
			sendResponse(httpResp, actionResponse);
			DirOperationsUtility.getInstance().deleteDirectory(new File(tmpDirName));
			
		}
	}
	
	public Properties loadSortOrderProperties(File sortOrderFile) throws Exception {
		Properties properties = new Properties();
		properties.load(new FileReader(sortOrderFile));
		return properties;
	}
	
	private ActionResponse getActionResponse(Exception e) {
		ActionResponse response = new ActionResponse();
		response.setFailure();
		response.setErrorMessage(e.getMessage());
		
		if (e instanceof SAXException) {
			response.setMessage("Input form definition XML is not well formed. Please correct it");
		} else if (e instanceof IOException) {
			response.setMessage("Create form action errored out during IO operation. Please retry again");
		} else if (e instanceof SQLException) {
			response.setMessage("Error doing DB operations. Please contact administrator");
		} else {
			response.setMessage("A runtime error occured. Please contact administrator");
		}
		
		return response;
	}
	
	protected void sendResponse(HttpServletResponse httpResp, ActionResponse resp) {
		ObjectOutputStream objectOutputStream = null;
		
		try	{
			objectOutputStream = new ObjectOutputStream(httpResp.getOutputStream());
			objectOutputStream.writeObject(resp);
		} catch (IOException e)	{
			logger.error("Exception occured while writing action response to client :: " + e.getMessage(), e);
		} finally {
			try	{
				objectOutputStream.flush();
				objectOutputStream.close();
			} catch (IOException e) {
				logger.warn("Exception occured while closing response stream :: " + e.getMessage(), e);
			}
		}
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { 
		throw new RuntimeException("Unsupported operation");
	} 	

	private String getTmpDirName() {
		return new StringBuilder()
			.append(System.getProperty("java.io.tmpdir"))
			.append(File.separator)
			.append(System.currentTimeMillis())
			.append(formCnt.incrementAndGet())
			.append("create")
			.toString();
	}
}
