package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface ByteArrayConceptValueInterface extends ConceptInterface {

    public void setAllValues(AbstractActionForm arg0) throws AssignDataException;
}
