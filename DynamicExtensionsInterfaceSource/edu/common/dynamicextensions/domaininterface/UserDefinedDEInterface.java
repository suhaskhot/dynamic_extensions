
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 *  When the permissible values for an attribute are user defined the data element is of type
 *  UserDefinedDE.This type of data element contains collection of user defined permissible values. 
 * @author sujay_narkar
 *
 */
public interface UserDefinedDEInterface extends DataElementInterface 
{
    
    /**
     * @return Returns the permissibleValueCollection.
     */
     Collection getPermissibleValueCollection();
         
    /**
     * @param permissibleValueInterface The permissibleValueInterface to be added.
     */
    void addPermissibleValue(PermissibleValueInterface permissibleValueInterface); 

}
