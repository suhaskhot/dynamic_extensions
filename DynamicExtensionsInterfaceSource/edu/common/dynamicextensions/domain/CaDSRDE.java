
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.CaDSRDEInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_CADSRDE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"   
 *
 */
public class CaDSRDE extends DataElement implements CaDSRDEInterface
{
    
    /**
     * 
     */
    
    protected String publicId;
    
    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException 
	{
	}

	/**
     * @hibernate.property name="publicId" type="string" column="PUBLIC_ID" 
	 * @return Returns the publicId.
	 */
	public String getPublicId() 
	{
		return publicId;
	}
	/**
	 * @param publicId The publicId to set.
	 */
	public void setPublicId(String publicId) 
	{
		this.publicId = publicId;
	}
}
