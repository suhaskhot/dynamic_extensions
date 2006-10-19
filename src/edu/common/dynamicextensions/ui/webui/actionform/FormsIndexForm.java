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
	protected Collection entityCollection = null;

	/**
	 * Values of the Check boxes
	 */
	protected String[] entityCheckBoxes = null;
	/**
	 * mode
	 */
	protected String mode;

	/**
	 * @return the mode
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	/**
	 * @return the entityList
	 */
	public Collection getEntityCollection()
	{
		return entityCollection;
	}

	/**
	 * @param entityList the entityList to set
	 */
	public void setEntityCollection(Collection entityList)
	{
		this.entityCollection = entityList;
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
		entityCollection = null;
	}

	/**
	 * Overrides setAllValues() method of ActionForm
	 * @param abstractDomainObject AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomainObject)
	{

	}

}
