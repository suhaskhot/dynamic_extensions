package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityInformationInterface;

public class LoadFormDefinitionProcessor extends BaseDynamicExtensionsProcessor {
	 /**
     * Protected constructor for LoadFormDefinitionProcessor
     *
     */
   protected LoadFormDefinitionProcessor () {
       
   }
 
   /**
    * this method gets the new instance of the LoadFormDefinitionProcessor to the caller.
    * @return LoadFormDefinitionProcessor LoadFormDefinitionProcessor instance
    */
    public static LoadFormDefinitionProcessor getInstance () {
        return new LoadFormDefinitionProcessor();
    }
    public void populateEntityInformation(EntityInterface entityInterface,EntityInformationInterface entityInformationInterface) {
    	if(entityInterface != null) {
			EntityProcessor entityProcessor = EntityProcessor.getInstance();
			entityProcessor.populateEntityInformation(entityInterface , entityInformationInterface);
		}
	}
}
