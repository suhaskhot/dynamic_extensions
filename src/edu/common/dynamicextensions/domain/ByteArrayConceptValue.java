
package edu.common.dynamicextensions.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_BYTE_ARRAY_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"   
 * @author sujay_narkar
 *
 */
public class ByteArrayConceptValue extends Concept {

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
    

}
