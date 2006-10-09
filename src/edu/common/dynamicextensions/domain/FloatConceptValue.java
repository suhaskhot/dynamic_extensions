
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatConceptValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_FLOAT_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 *
 */
public class FloatConceptValue extends Concept implements FloatConceptValueInterface {
    
    /**
    * 
    */
   protected Float value;

   /**
    * 
    */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}


	/**
     * @hibernate.property name="value" type="float" column="VALUE" 
	 * @return Returns the value.
	 */
	public Float getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Float value) {
		this.value = value;
	}
}
