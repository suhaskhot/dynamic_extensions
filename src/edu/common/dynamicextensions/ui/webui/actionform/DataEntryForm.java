
package edu.common.dynamicextensions.ui.webui.actionform;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 *
 */
public class DataEntryForm extends AbstractActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7828307676065035418L;

	/**
	 * 
	 */
	protected String entitySaved;
	/**
	 * 
	 */
	protected String showFormPreview;

	/**
	 * 
	 */
	protected ContainerInterface containerInterface;

	/**
	 * @return int formId
	 */
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param arg0 abstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{
	}

	/**
	 * 
	 */
	protected void reset()
	{
	}

	/**
	 * @return Returns the container.
	 */
	public ContainerInterface getContainerInterface()
	{
		return containerInterface;
	}

	/**
	 * @param containerInterface The container to set.
	 */
	public void setContainerInterface(ContainerInterface containerInterface)
	{
		this.containerInterface = containerInterface;
	}

	/**
	 * 
	 * @return entitySaved
	 */
	public String getEntitySaved()
	{
		return entitySaved;
	}

	/**
	 * 
	 * @param entitySaved entitySaved
	 */
	public void setEntitySaved(String entitySaved)
	{
		this.entitySaved = entitySaved;
	}

	/**
	 * 
	 * @return String showFormPreview 
	 */
	public String getShowFormPreview()
	{
		return showFormPreview;
	}

	/**
	 * 
	 * @param showFormPreview String showFormPreview
	 */
	public void setShowFormPreview(String showFormPreview)
	{
		this.showFormPreview = showFormPreview;
	}

}
