package edu.common.dynamicextensions.util.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author aarti_sharma
 * 
 * */
public class DynamicExtensionsServletContextListener implements ServletContextListener
{
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent sce)
	{
		/**
		 * Getting Application Properties file path
		 */
		String applicationResourcesPath = sce.getServletContext().getRealPath("WEB-INF")
		+ System.getProperty("file.separator")
		+ "classes" + System.getProperty("file.separator")
		+ sce.getServletContext().getInitParameter("applicationproperties");
		
		/**
		 * Initializing ApplicationProperties with the class 
		 * corresponding to resource bundle of the application
		 */
		ApplicationProperties.initBundle(sce.getServletContext().getInitParameter("resourcebundleclass"));
		
		/**
		 * Getting and storing Home path for the application
		 */
		Variables.dynamicExtensionsHome = sce.getServletContext().getRealPath("");
		
		/**
		 * Creating Logs Folder inside catissue home
		 */
		File logfolder=null;
		try {
			logfolder = new File(Variables.dynamicExtensionsHome + "/Logs");
			if (logfolder.exists() == false) {
				logfolder.mkdir();
			}
		}
		catch (Exception e) {
			;
		}
	
		
		/**
		 * setting system property catissue.home which can be ustilized 
		 * by the Logger for creating log file
		 */
		System.setProperty("dynamicExtensions.home",Variables.dynamicExtensionsHome + "/Logs");
		
		/**
		 * Configuring the Logger class so that it can be utilized by
		 * the entire application
		 */
		Logger.configure(applicationResourcesPath);
		
		Logger.out.info(ApplicationProperties.getValue("dynamicExtensions.home")
				+ Variables.dynamicExtensionsHome);
		Logger.out.info(ApplicationProperties.getValue("logger.conf.filename")
				+ applicationResourcesPath);
		
		
		Variables.datePattern = "mm-dd-yyyy";
		Variables.timePattern = "hh-mi-ss";
		Variables.dateFormatFunction="TO_CHAR";
		Variables.timeFormatFunction="TO_CHAR";
		Variables.dateTostrFunction = "TO_CHAR";
		Variables.strTodateFunction = "TO_DATE";
		
	}
	
	/**
     * 
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}