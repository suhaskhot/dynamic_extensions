
package edu.common.dynamicextensions.domain;

import java.sql.Date;

import edu.common.dynamicextensions.domaininterface.DateConceptValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_DATE_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DateConceptValue extends Concept implements DateConceptValueInterface {
    /**
     * 
     */
    protected Date value;

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
	}
    
  	/**
     * @hibernate.property name="value" type="date" column="VALUE"   
	 * @return Returns the value.
	 */
	public Date getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Date value) {
		this.value = value;
	}
}
