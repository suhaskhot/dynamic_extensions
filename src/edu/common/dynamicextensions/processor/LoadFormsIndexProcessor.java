
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;

import edu.common.dynamicextensions.entitymanager.MockEntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
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
	 */
	public void populateFormsIndex(FormsIndexForm loadFormIndexForm)
	{
		Collection entityList = null;
		try
		{
			entityList = new MockEntityManager().getAllEntities();
			if (entityList == null)
			{
				entityList = new ArrayList();
			}
			loadFormIndexForm.setEntityList(entityList);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}
	}
}
