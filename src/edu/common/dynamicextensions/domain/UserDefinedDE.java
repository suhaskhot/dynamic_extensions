
package edu.common.dynamicextensions.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_USERDEFINED_DE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 *
 */
public class UserDefinedDE extends DataElement {
    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		
	}
}
