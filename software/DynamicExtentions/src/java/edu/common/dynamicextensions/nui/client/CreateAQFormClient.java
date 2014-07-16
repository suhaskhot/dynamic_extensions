package edu.common.dynamicextensions.nui.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.client.AbstractClient;
import edu.common.dynamicextensions.nui.action.ActionResponse;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DirOperationsUtility;
import edu.common.dynamicextensions.util.ZipUtility;
import edu.common.dynamicextensions.util.global.Variables;
import edu.common.dynamicextensions.utility.HTTPSConnection;

public class CreateAQFormClient extends AbstractClient {
	private static final Logger logger = Logger.getLogger(CreateAQFormClient.class);

	public static void main(String[] args) 
	{		
		Variables.serverUrl = args[0];
		CreateAQFormClient createFormClient = new CreateAQFormClient();
		createFormClient.execute(args);
	}

	protected void initializeResources(String[] args) 
	{
		try	{
			long halfGB = 500 * 1024 * 1024L;
			
			//Validate CreateForm Folder that should be uploaded
			DirOperationsUtility.validateFolderSizeForUpload(args[1], halfGB);
			this.zipFile = ZipUtility.zipFolder(args[1], "forms.zip");

			String url = HTTPSConnection.getCorrectedApplicationURL(args[0]) +	WebUIManagerConstants.CREATE_AQ_FORM_ACTION;			

			if (args.length > 2 && !args[2].isEmpty() && !args.equals("${username}")) {
				url = url + "?login_name=" + args[2];
			}
			
			this.serverUrl = new URL(url);			
			logger.info("Invoking create form action @ " + serverUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Please provide correct application URL", e);
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
		if (args.length < 2 || args.length > 3 || args[1] == null || args[1].trim().isEmpty()) {
			throw new RuntimeException(
					"Wrong number of arguments to command");
		}
	}

	@Override
	protected void processResponse(URLConnection servletConnection)
	throws IOException {
		
		ObjectInputStream inputFromServlet = null;
		try	{
			inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
			ActionResponse actionResponse = (ActionResponse)inputFromServlet.readObject();
			
			if (actionResponse.getStatus() == 0) {
				logger.info("Forms created successfully!!!");
			} else {
				logger.info("Encountered errors creating forms");
				logger.info("Message: " + actionResponse.getMessage());
				logger.info("Error:   " + actionResponse.getErrorMessage());
			}
		} catch (Exception e) {
			logger.error("Error reading response from server", e);
			throw new RuntimeException("Error reading response from server", e);
		} finally {
			if (inputFromServlet != null) {
				inputFromServlet.close();
			}
		}
	}

	
	public static class DerivedTrustManager
			implements
				javax.net.ssl.TrustManager,
				javax.net.ssl.X509TrustManager
	{

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
		 */
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return new java.security.cert.X509Certificate[0];
		}

		/**
		 * This method will always return true so that any url is considered to be trusted.
		 * & no certification validation is done.
		 * @param certs certificates.
		 * @return true if certificate is trusted
		 */
		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		/**
		 * This method will always return true so that any url is considered to be trusted.
		 * & no certification validation is done.
		 * @param certs certificates.
		 * @return true if certificate is trusted
		 */
		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
		 */
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException
		{
			// TODO Auto-generated catch block
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
		 */
		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException
		{
			// TODO Auto-generated catch block
		}
	}
}