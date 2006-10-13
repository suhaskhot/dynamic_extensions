package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface StringAttributeInterface extends AttributeInterface {

    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue);
    /**
     * @return Returns the size.
     */
    public Integer getSize();
    /**
     * @param size The size to set.
     */
    public void setSize(Integer size);

    
}
