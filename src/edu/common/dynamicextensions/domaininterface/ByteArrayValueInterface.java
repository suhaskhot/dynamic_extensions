package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface ByteArrayValueInterface extends PermissibleValueInterface {

    public void setAllValues(AbstractActionForm arg0) throws AssignDataException;
}
