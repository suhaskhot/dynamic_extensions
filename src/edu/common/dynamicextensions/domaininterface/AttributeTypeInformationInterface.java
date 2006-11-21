/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.domaininterface;

public interface AttributeTypeInformationInterface
{

    /**
     * Returns the data element associated with the attribute.The data elment specify the
     * source of permissible values.
     * @return DataElementInterface
     * 
     */
    DataElementInterface getDataElement();

    /**
     * @param dataElementInterface data element interface
     */
    void setDataElement(DataElementInterface dataElementInterface);

    /**
     * @return return the default value for this attribute type. 
     */
    PermissibleValueInterface getDefaultValue();

    /**
     * sets the default value for this attribute type.
     * @param permissibleValueInterface  default value
     */
    void setDefaultValue(PermissibleValueInterface permissibleValueInterface);
}
