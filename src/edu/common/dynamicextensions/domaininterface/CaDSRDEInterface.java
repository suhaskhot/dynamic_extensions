
package edu.common.dynamicextensions.domaininterface;

/**
 * @author sujay_narkar
 *
 */
public interface CaDSRDEInterface extends DataElementInterface {
    
    /**
     * @return Returns the publicId.
     */
    public String getPublicId();
    
    /**
     * @param publicId The publicId to set.
     */
    public void setPublicId(String publicId);
}
