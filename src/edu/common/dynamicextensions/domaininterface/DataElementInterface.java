
package edu.common.dynamicextensions.domaininterface;

/**
 * Data element represent the source of the permissible values for a primitive attribute.
 * Data element is an abstract class.The data element may be of type caDSRDE or of type user defined.
 * @author sujay_narkar
 *
 */
public interface DataElementInterface 
{
    
    /**
     * Uniqueue id for the data element. 
     * @return Returns the id.
     */
    Long getId(); 
}
