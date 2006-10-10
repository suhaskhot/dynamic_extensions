package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface BooleanAttributeInterface extends PrimitiveAttributeInterface {

    /**
     * @return Returns the defaultValue.
     */
    public Boolean getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Boolean defaultValue);

  
}
