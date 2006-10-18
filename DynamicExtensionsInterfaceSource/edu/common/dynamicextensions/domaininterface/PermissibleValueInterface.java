
package edu.common.dynamicextensions.domaininterface;

/**
 * When the value domain for an attribute is user defined,the data element object is of type CaDSRDE 
 * and this object contains a collection of permissible values.  
 * @author sujay_narkar
 *
 */
public interface PermissibleValueInterface 
{
    
    /**
     * @return Returns the id.
     */
    Long getId();
    public Object getValueAsObject(); 
}
