
package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible values for an attribute may come from different sources such as caDSR or may be user defnied.
 * If the values will come from caDSR then DataElement is of type CaDSRDE.This interface stores public id from caDSR
 * for the attribute.
 * @author sujay_narkar
 *
 */
public interface CaDSRDEInterface extends DataElementInterface
{
    
    /**
     * Public id from caDSR for the attribute.
     * @return Returns the publicId.
     */
     String getPublicId();
    
    /**
     * @param publicId The publicId to set.
     */
    void setPublicId(String publicId);
}
