
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ByteArrayConceptValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_BARR_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"   
 * @author sujay_narkar
 *
 */
public class ByteArrayConceptValue extends Concept implements ByteArrayConceptValueInterface{

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
    

}
