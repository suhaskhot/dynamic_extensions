package edu.common.dynamicextensions.domaininterface;


/**
 * This is a boolean type of primitive attribute.
 * @author geetika_bangard
 */
public interface BooleanAttributeInterface extends PrimitiveAttributeInterface
{

    /**
     * This field stores the default value for the boolean attribute.
     * @return Returns the defaultValue.
     */
     Boolean getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    void setDefaultValue(Boolean defaultValue);

  
}
