
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.UIControlsConfigurationFactory;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * @author sujay_narkar
 *
 */
public class LoadFormControlsProcessor 
{
	
	/**
	 * Protected constructor for entity processor
	 *
	 */
	protected LoadFormControlsProcessor () 
	{
		
	}
	
	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static LoadFormControlsProcessor getInstance () 
	{
		return new LoadFormControlsProcessor();
	}
	
	/**
     * 
	 * @param actionForm
	 * @param containerInterface
	 */
	public void loadFormControls(AbstractActionForm actionForm,ContainerInterface containerInterface) 
	{
		ControlsForm controlsForm = (ControlsForm)actionForm;
        List toolList = getToolsList();
        controlsForm.setToolsList(toolList);
        if(controlsForm.getControlOperation() == null || 
                controlsForm.getControlOperation().equals(ProcessorConstants.ADD))
        {
            actionForm.setUserSelectedTool(toolList.get(0).toString());
            
        }
        else 
        {
            
        }
	}
    
    /**
     * Returns the toolsList from the xml file.
     * @return
     */
    private List getToolsList(){
        List toolsList = new ArrayList();
        UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
        toolsList = uiControlsConfigurationFactory.getControlNames();
        return toolsList;
    }
	
}



