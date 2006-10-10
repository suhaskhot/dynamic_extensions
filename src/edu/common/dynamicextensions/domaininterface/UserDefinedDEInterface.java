
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 * @author sujay_narkar
 *
 */
public interface UserDefinedDEInterface extends DataElementInterface {
    
    /**
     * @return Returns the permissibleValueCollection.
     */
    public Collection getPermissibleValueCollection();
         
    /**
     * @param permissibleValueCollection The permissibleValueCollection to set.
     */
    public void setPermissibleValueCollection(Collection permissibleValueCollection); 

}
