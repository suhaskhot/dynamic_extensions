/**
 * 
 */
package edu.common.dynamicextensions.ui.webui.actionform;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author chetan_patil
 *
 */
public class FormsIndexForm extends AbstractActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	
	/**
	 * Collection of Entities
	 */
	protected Collection entityList = null;
	
	/**
	 * Values of the Check boxes
	 */
	protected String[] entityCheckBoxes = null;

	/**
	 * @return the entityList
	 */
	public Collection getEntityList() 
	{
		return entityList;
	}

	/**
	 * @param entityList the entityList to set
	 */
	public void setEntityList(Collection entityList) 
	{
		this.entityList = entityList;
	}
	
	/**
	 * @return the entityCheckBoxes
	 */
	public String[] getEntityCheckBoxes() 
	{
		return entityCheckBoxes;
	}

	/**
	 * @param entityCheckBoxes the entityCheckBoxes to set
	 */
	public void setEntityCheckBoxes(String[] entityCheckBoxes) 
	{
		this.entityCheckBoxes = entityCheckBoxes;
	}

	/**
	 * Overrides getFormId() method of ActionForm
	 * @return the form identifier
	 */	
	public int getFormId() 
	{
		return 0;
	}

	/**
	 * Overrides reset() method of ActionForm
	 */
	protected void reset() 
	{
		entityList = null;		
	}

	/**
	 * Overrides setAllValues() method of ActionForm
	 * @param abstractDomainObject AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomainObject) 
	{		
		
	}	
	
}
