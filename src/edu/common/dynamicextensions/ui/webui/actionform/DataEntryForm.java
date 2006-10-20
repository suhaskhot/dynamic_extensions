
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
}
