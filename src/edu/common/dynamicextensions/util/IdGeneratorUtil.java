package edu.common.dynamicextensions.util;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.common.dynamicextensions.domain.IdGenerator;
import edu.common.dynamicextensions.domaininterface.IdGeneratorInterface;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * This is singleton util class is used to generate unique ids. 
 * @author kunal_kamble
 * @version 1.0
 */
public class IdGeneratorUtil 
{

	/**
	 * Static instance of the id generator.
	 */
	private static IdGeneratorUtil idGeneratorUtil;

	/**
	 * Private constructor used for making this class singleton
	 */
	private IdGeneratorUtil() 
    {
	}

	/**
	 * Returns the instance of the IdGeneratorUtil class.
	 * @return idGeneratorUtil singleton instance of the IdGeneratorUtil.
	 */
	public static synchronized IdGeneratorUtil getInstance() 
    {
		if (idGeneratorUtil == null) 
        {
			idGeneratorUtil = new IdGeneratorUtil();
		}
		return idGeneratorUtil;
	}

	/**
	 * @return next available id
	 * @throws Exception any exception occured
	 */
	public static synchronized Long getNextUniqeId()
	{
		Session session = null;
		Transaction transaction = null;
		Long nextAvailableId = null;

		try 
        {
			session = DBUtil.currentSession().getSessionFactory().openSession();
			transaction = session.beginTransaction();

			Query query = session.createQuery("from " + IdGenerator.class.getName());
			IdGeneratorInterface idGenerator = (IdGeneratorInterface) query.list().get(0);
		
			nextAvailableId = idGenerator.getNextAvailableId();
			idGenerator.setNextAvailableId(nextAvailableId + 1);
			
			// Remove idGenerator instance from the session cache.
			session.evict(idGenerator);
			session.update(idGenerator);
			transaction.commit();
		} 
        catch (Exception orgExp) 
        {
			try 
            {
				transaction.rollback();
			} 
            catch (RuntimeException rte) 
            {
            	Logger.out.debug("Could not rollback transaction", rte);
			}
		} 
        finally 
        {
			session.close();
		}

		return nextAvailableId;
	}
}