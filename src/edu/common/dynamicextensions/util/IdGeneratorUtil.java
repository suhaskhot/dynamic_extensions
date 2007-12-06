package edu.common.dynamicextensions.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.common.dynamicextensions.domain.IdGenerator;
import edu.wustl.common.util.dbManager.DBUtil;

/**
 * @author kunal_kamble
 * @version 1.0
 */
public class IdGeneratorUtil 
{

	private static IdGeneratorUtil idGeneratorUtil;

	private static final Log log = LogFactory.getLog(IdGeneratorUtil.class);

	private IdGeneratorUtil() 
    {
	}

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
		Long uniqueId = null;

		try 
        {
			session = DBUtil.currentSession().getSessionFactory().openSession();
			transaction = session.beginTransaction();

			Query query = session.createQuery("from " + IdGenerator.class.getName());
			IdGenerator idGenerator = (IdGenerator) query.list().get(0);
			
			/*Criterion[] critList = new Criterion[1];
			Criteria crit = session.createCriteria(IdGenerator.class);
			for (Criterion c : critList) 
            {
				crit.add(c);
			}
			List<IdGenerator> list = crit.list();
			IdGenerator idGenerator = (IdGenerator) list.get(0);*/
			
			uniqueId = idGenerator.getNextId();
			idGenerator.setNextId(uniqueId + 1);
			
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
				log.error("Could not rollback transaction", rte);
			}
		} 
        finally 
        {
			session.close();
		}

		return uniqueId;
	}
}