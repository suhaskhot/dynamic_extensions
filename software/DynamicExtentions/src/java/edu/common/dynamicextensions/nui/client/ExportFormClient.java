package edu.common.dynamicextensions.nui.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.client.AbstractClient;
import edu.common.dynamicextensions.nutility.IoUtil;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DirOperationsUtility;
import edu.common.dynamicextensions.util.global.Variables;
import edu.common.dynamicextensions.utility.HTTPSConnection;

public class ExportFormClient extends AbstractClient {
	
	private static final Logger logger = Logger.getLogger(ExportFormClient.class);
	
	private String downloadPath;
	
	private Long formId;

	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; ++i) { 
			logger.info("Arg[" + (i + 1) + "] :" + args[i]);
		}
				
		try {
			Variables.serverUrl = args[0];
			ExportFormClient createFormClient = new ExportFormClient();
			createFormClient.execute(args);
			
			logger.info("Form is exported successfully!!!");
		} catch (Exception e) {
			logger.error("Encountered error while exporting form!!!", e);
		}
	}


	protected void initializeResources(String[] args) 
	{
		
		try	{
			StringBuilder url = new StringBuilder()
				.append(HTTPSConnection.getCorrectedApplicationURL(args[0]))
				.append(WebUIManagerConstants.EXPORT_FORM_ACTION)
				.append("?containerId=").append(args[1]);
			
			formId = Long.parseLong(args[1]);
			if (args.length < 3 || args[2] == null || args[2].isEmpty()) {
				downloadPath = ".";
			} else {
				downloadPath = args[2];
			}
			
			this.serverUrl = new URL(url.toString());
		} catch (MalformedURLException e) {
			logger.error("Encountered error while creating server URL", e);
			throw new RuntimeException("Encountered error while creating server URL", e);
		}
	}

	/**
	 * It will validate weather the correct number of arguments are passed or not & then throw
	 * exception accordingly.
	 * @param args arguments
	 * @throws DynamicExtensionsSystemException exception
	 */
	protected void validate(String[] args) 
	{
		if (args.length < 2 || args[1] == null || args[1].trim().isEmpty()) {
			throw new RuntimeException(
					"Please specify identifier of form that is to be created");
		}
	}

	@Override
	protected void processResponse(URLConnection servletConnection)
	{
		String formZipName = new StringBuilder("form-").append(formId).append(".zip").toString();
		downloadZipFile(servletConnection, downloadPath, formZipName);
	}
	
	public static void downloadZipFile(
			URLConnection servletConnection, String tempDirName, String fileName) 
	{
		
		BufferedInputStream reader = null;
		BufferedOutputStream fileWriter = null;
		
		try	{
			DirOperationsUtility.getInstance().createTempDirectory(tempDirName);
			String exportedFormZipFilePath = new StringBuilder(tempDirName)
				.append(File.separator).append(fileName)
				.toString();

			reader = new BufferedInputStream(servletConnection.getInputStream());
			fileWriter = new BufferedOutputStream(new FileOutputStream(exportedFormZipFilePath));

			IoUtil.copy(reader, fileWriter);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error occured while downloading exported form zip from server", e);
		} finally {
			IoUtil.close(fileWriter);
			IoUtil.close(reader);
		}
	}	
}