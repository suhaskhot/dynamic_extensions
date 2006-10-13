
package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_USERDEFINED_DE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 *
 */
public class UserDefinedDE extends DataElement implements UserDefinedDEInterface{
    /**
     * 
     */
    Collection permissibleValueCollection;
    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException 
    {
		
	}
	/**
     * @hibernate.set name="permissibleValueCollection" table="DYEXTN_PERMISSIBLE_VALUE"
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="USER_DEF_DE_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.PermissibleValue" 
	 * @return Returns the permissibleValueCollection.
	 */
	public Collection getPermissibleValueCollection() 
    {
		return permissibleValueCollection;
	}
	/**
	 * @param permissibleValueCollection The permissibleValueCollection to set.
	 */
	public void setPermissibleValueCollection(Collection permissibleValueCollection) 
    {
		this.permissibleValueCollection = permissibleValueCollection;
	}
    
    /**
     * 
     * @param permissibleValue
     */
	public void addPermissibleValue(PermissibleValueInterface permissibleValueInterface) {
		// TODO Auto-generated method stub
		
	}
}
