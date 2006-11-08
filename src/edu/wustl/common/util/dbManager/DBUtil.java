
package edu.wustl.common.util.dbManager;

import java.io.InputStream;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

import org.w3c.dom.Document;

import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>Title: DBUtil Class>
 * <p>Description:  Utility class provides database specific utilities methods </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
public class DBUtil
{

	//A factory for DB Session which provides the Connection for client. 
	private static SessionFactory sessionFactory;

	//ThreadLocal to hold the Session for the current executing thread. 
	private static final ThreadLocal THREADLOCAL_INSTANCE = new ThreadLocal();
	//Initialize the session Factory in the Static block.

	static
	{
		if (Variables.containerFlag)
		{
			try
			{
				Configuration cfg = new Configuration();
				sessionFactory = cfg.configure().buildSessionFactory();
				HibernateMetaData.initHibernateMetaData(cfg);
			}
			catch (Exception ex)
			{
				Logger.out.debug("Exception: " + ex.getMessage(), ex);
				throw new RuntimeException(ex.getMessage());
			}
		}
		else
		{
			try
			{
				System.out.println("HI2");
				InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
						"TestHibernate.cfg.xml");
				
				System.out.println("is" + inputStream);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				DocumentBuilder docBuilder = null;
				docBuilder = factory.newDocumentBuilder();
				System.out.println("parsing");
				Document doc = docBuilder.parse(inputStream);
				System.out.println("parsed");
				Configuration cfg = new Configuration();
				System.out.println("building");
				sessionFactory = cfg.configure(doc).buildSessionFactory();
				HibernateMetaData.initHibernateMetaData(cfg);
				System.out.println("builed");
			
			}
			catch (Exception ex)
			{
				Logger.out.debug("Exception: " + ex.getMessage(), ex);
				System.out.println(ex.getStackTrace());
				throw new RuntimeException(ex.getMessage());
			}
		}
	}

	/**
	 * Follows the singleton pattern and returns only current opened session.
	 * @return Returns the current db session.
	 * @throws HibernateException HibernateExeption  
	 * */
	public static Session currentSession() throws HibernateException
	{
		Session s = (Session) THREADLOCAL_INSTANCE.get();
		Logger.out.debug("**********************Session in DBUTIL " + s);
		//Open a new Session, if this Thread has none yet
		if (s == null)
		{
			s = sessionFactory.openSession();
			//			try
			//			{
			//			//	s.connection().setAutoCommit(false);
			//			}
			//			catch(SQLException ex)
			//			{
			//				throw new HibernateException(ex.getMessage(),ex);
			//			}
			THREADLOCAL_INSTANCE.set(s);
		}
		return s;
	}

	/**
	 * Close the currently opened session.
	 * @throws HibernateException HibernateException
	 * */
	public static void closeSession() throws HibernateException
	{
		Session s = (Session) THREADLOCAL_INSTANCE.get();
		THREADLOCAL_INSTANCE.set(null);
		if (s != null)
		{
			s.close();
		}
	}

	/**
	 * @return Connection
	 * @throws HibernateException HibernateException
	 */
	public static Connection getConnection() throws HibernateException
	{
		return currentSession().connection();
	}

	/**
	 * @throws HibernateException HibernateException
	 */
	public static void closeConnection() throws HibernateException
	{
		closeSession();
	}
}