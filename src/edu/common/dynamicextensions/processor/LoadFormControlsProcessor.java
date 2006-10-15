
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.UIControlsConfigurationFactory;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;

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
	public void loadFormControls(AbstractActionForm actionForm,ContainerInterface containerInterface,
            String controlOperation,String selectedControlId,String userSelectedTool) 
	{
		ControlsForm controlsForm = (ControlsForm)actionForm;
        List toolList = getToolsList();
        controlsForm.setToolsList(toolList);
       
       
        if(controlOperation == null || controlOperation.equals("") ||
                controlOperation.equalsIgnoreCase(ProcessorConstants.ADD))
        {
            if(userSelectedTool == null || userSelectedTool.equals(""))
            {
            	userSelectedTool = toolList.get(0).toString();
            }
            controlsForm.setUserSelectedTool(userSelectedTool);
            controlsForm.setDataType("");
            controlsForm.setDisplayChoice("");
            controlsForm.setDataType("String");
            
            controlsForm.setHtmlFile(userSelectedTool+".jsp");
        }
        
        else if(controlOperation.equalsIgnoreCase(ProcessorConstants.EDIT))  
        {
            ControlProcessor controlProcessor = ControlProcessor.getInstance();
          
            ControlInterface controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlId);
            controlProcessor.populateControlInformation(controlInterface,controlsForm);
            
            
            AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
            attributeProcessor.populateAttributeInformation(controlInterface.getAbstractAttribute(), controlsForm);
            
           
           
            System.out.println("After Polulate attribute");
            
        }
        controlsForm.setDataTypeList(getDataTypeList());
       
        controlsForm.setRootName(containerInterface.getCaption());
        controlsForm.setChildList(getChildList(containerInterface));
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
     * 
     * @param containerInterface
     * @return
     */
    private List getChildList(ContainerInterface containerInterface)
    {
        List childList = new ArrayList();
        Collection controlCollection = containerInterface.getControlCollection();
        Iterator controlIterator = controlCollection.iterator();
        ControlInterface controlInterface = null;
        NameValueBean nameValueBean;
        while(controlIterator.hasNext())
        {
            controlInterface =  (ControlInterface) controlIterator.next();
            if(controlInterface.getCaption() != null && !controlInterface.getCaption().equals(""))
            {
                nameValueBean = new NameValueBean(controlInterface.getCaption(),controlInterface.getSequenceNumber());
                childList.add(nameValueBean);
            }
            
        }
        return childList;
    }
    
    /**
     * 
     * @return
     */
    private List getDataTypeList(){
        List dataTypeList = new ArrayList();
        NameValueBean nameValueBean1 = new NameValueBean("String","String");
        dataTypeList.add(nameValueBean1);
        
        NameValueBean nameValueBean2 = new NameValueBean("Number","Number");
        dataTypeList.add(nameValueBean2);
        
        return dataTypeList; 
    }
  
   
}



