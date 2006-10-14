package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;

import edu.common.dynamicextensions.entitymanager.MockEntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.ui.webui.actionform.FormsIndexForm;

/**
 * 
 * @author deepti_shelar
 *
 */
public class LoadFormsIndexProcessor extends BaseDynamicExtensionsProcessor {
	/**
	 *
	 */
	protected LoadFormsIndexProcessor() {
		// TODO Auto-generated constructor stub
	}
	/**
	 *
	 */

	public static LoadFormsIndexProcessor getInstance() {
		return new LoadFormsIndexProcessor();
	}
	public void populateFormsIndex(FormsIndexForm loadFormIndexForm ) {
		Collection entityList = null;
		try {
			entityList = new MockEntityManager().getAllEntities();
			if(entityList == null) {
				entityList = new ArrayList();
			}
			loadFormIndexForm.setEntityList(entityList);
		} catch (DynamicExtensionsApplicationException e) {
			e.printStackTrace();
		}
	}
}
