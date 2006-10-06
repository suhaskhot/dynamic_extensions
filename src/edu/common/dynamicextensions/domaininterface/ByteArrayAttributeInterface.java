package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface ByteArrayAttributeInterface extends
        PrimitiveAttributeInterface {
    /**
     * @return Returns the contentType.
     */
    public String getContentType();
    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType);

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException;

}
