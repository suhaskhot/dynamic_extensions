
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
	String entitySaved;
	/**
	 * 
	 */
	String showFormPreview;
	/**
	 * 
	 */
	public int getFormId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 */
	public void setAllValues(AbstractDomainObject arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	protected void reset() {
		// TODO Auto-generated method stub

	}

	protected ContainerInterface containerInterface;

	/**
	 * @return Returns the container.
	 */
	public ContainerInterface getContainerInterface() {
		return containerInterface;
	}
	/**
	 * @param container The container to set.
	 */
	public void setContainerInterface(ContainerInterface containerInterface) {
		this.containerInterface = containerInterface;
	}

	public String getEntitySaved() {
		return entitySaved;
	}

	public void setEntitySaved(String entitySaved) {
		this.entitySaved = entitySaved;
	}

	public String getShowFormPreview() {
		return showFormPreview;
	}

	public void setShowFormPreview(String showFormPreview) {
		this.showFormPreview = showFormPreview;
	}
}
