package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ByteArrayAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_BYTE_ARRAY_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ByteArrayAttribute extends Attribute implements ByteArrayAttributeInterface{
    /**
     * Content type for this file.
     */
	protected String contentType;

	/**
	 * Empty Constructor.
	 *
	 */
	public ByteArrayAttribute(){

	}


    /**
     * @hibernate.property name="contentType" type="string" column="CONTENT_TYPE" 
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }
    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}