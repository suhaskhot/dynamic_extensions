
package edu.common.dynamicextensions.processor;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.FormsIndexForm;

/**
 * Populates  the actonForm with required data eg. entityList. 
 * @author deepti_shelar
 *
 */
public class LoadFormsIndexProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Default Constructor.
	 */
	protected LoadFormsIndexProcessor()
	{
	}

	/**
	 * returns the instance of LoadFormsIndexProcessor.
	 */
	public static LoadFormsIndexProcessor getInstance()
	{
		return new LoadFormsIndexProcessor();
	}

	/**
	 * A call to EntityManager will return the entityList which will then added to actionForm.
	 * @param loadFormIndexForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException
	 */
	public void populateFormsIndex(FormsIndexForm loadFormIndexForm) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Collection entityCollection = null;
		try
		{
			//entityList = new MockEntityManager().getAllEntities();
			EntityManager entityManager = EntityManager.getInstance();
			entityCollection = entityManager.getAllEntities();
			
			for(Iterator iterator = entityCollection.iterator(); iterator.hasNext();)
			{
				EntityInterface entityInterface = (EntityInterface)iterator.next();
				Date date = entityInterface.getCreatedDate();
				System.out.println(date);
			}
			
			if (entityCollection == null)
			{
				entityCollection = new HashSet();
			}
			loadFormIndexForm.setEntityCollection(entityCollection);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			throw e;
		}
		catch (DynamicExtensionsSystemException e)
		{
			throw e;
		}
	}
}
