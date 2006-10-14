
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
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
        List controlAttributesList;
       
        if(controlsForm.getControlOperation() == null || 
                controlsForm.getControlOperation().equals(ProcessorConstants.ADD))
        {
            if(controlsForm.getUserSelectedTool() == null || controlsForm.getUserSelectedTool().equals(""))
            {
            	controlsForm.setUserSelectedTool(toolList.get(0).toString());
            }
            controlsForm.setDataType("");
            controlsForm.setDisplayChoice("");
            
            controlAttributesList  = getControlAttributesList(controlsForm.getUserSelectedTool());
            controlsForm.setSelectedControlAttributesList(controlAttributesList);
        }
        else if(controlsForm.getControlOperation().equals(ProcessorConstants.EDIT))  
        {
            ControlProcessor controlProcessor = ControlProcessor.getInstance();
            String selectedControlId = controlsForm.getSelectedControlId();
            ControlInterface controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlId);
            controlProcessor.populateControlInformation(controlInterface,controlsForm);
        }
        
        controlsForm.setRootName("Sujay");
        
        List childList = new ArrayList();
        childList.add("ABC");
        childList.add("PQR");
        childList.add("STV");
        
        controlsForm.setChildList(childList);
        
        
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
    
    /**
     * Returns the selectedControlAttributesList from the xml file depending upon the tool passed.
     * @param userSelectedTool
     * @return
     */
    private List getControlAttributesList(String userSelectedTool)
    {
        UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
        List controlAttributesList = new ArrayList();
    
        controlAttributesList  = uiControlsConfigurationFactory.getConrolAttributesList(userSelectedTool);
        return controlAttributesList;
    }
    
    
   
}



