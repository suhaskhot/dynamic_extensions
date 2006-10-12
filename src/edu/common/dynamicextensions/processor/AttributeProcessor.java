/*
 * Created on Oct 11, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.ui.interfaces.AttributeInformationInterface;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AttributeProcessor extends BaseDynamicExtensionsProcessor
{
	 /**
     * Protected constructor for attribute processor
     *
     */
   protected AttributeProcessor () {
       
   }
 
   /**
    * this method gets the new instance of the entity processor to the caller.
    * @return EntityProcessor EntityProcessor instance
    */
    public static AttributeProcessor getInstance () {
        return new AttributeProcessor();
    }
    
	/**
	 * @param controlsForm
	 * @return
	 */
	public AttributeInterface createAttribute(String attributeType)
	{
		AttributeInterface attributeInterface = null;
		if (attributeType.equalsIgnoreCase("String")) {
			attributeInterface = DomainObjectFactory.getInstance().createStringAttribute();
		} else if(attributeType.equalsIgnoreCase("Boolean")) {
			attributeInterface = DomainObjectFactory.getInstance().createBooleanAttribute();
		} else if(attributeType.equalsIgnoreCase("Date")) {
			attributeInterface = DomainObjectFactory.getInstance().createDateAttribute();
		}
		return attributeInterface;
	}
	public void populateAttribute(AttributeInformationInterface attributeInformationInterface,AttributeInterface attributeInterface) {
	//	attributeInterface.setCreatedDate(attributeInformationInterface.)))
	}
}
