
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ByteArrayValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_BARR_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"   
 * @author sujay_narkar
 *
 */
public class ByteArrayValue extends PermissibleValue implements ByteArrayValueInterface
{
	/**
	 * Sserial Version Unique Identifier
	 */
	private static final long serialVersionUID = -5535531891478617220L;
	
	/**
	 * Set all values from the form
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException 
	{
	}
	
	/**
	 * This method returns the value of ByteArrayValue downcasted to the Object.
	 * @return the value of ByteArrayValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return null;
	}

}
