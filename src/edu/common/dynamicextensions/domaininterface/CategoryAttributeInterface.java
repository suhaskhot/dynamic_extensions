package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

public interface CategoryAttributeInterface extends BaseAbstractAttributeInterface
{
	/**
	 *
	 * @return
	 */
    public AttributeInterface getAttribute();
    /**
     *
     * @param attribute
     */
    public void setAttribute(AttributeInterface attribute);
    /**
     *
     * @return
     */
    public CategoryEntityInterface getCategoryEntity();
    /**
     *
     * @param categoryEntityInterface
     */
    public void setCategoryEntity(CategoryEntityInterface categoryEntityInterface);
    
    public Collection<PermissibleValueInterface> getDefaultPermissibleValuesCollection();
    
    public void setDefaultPermissibleValuesCollection(Collection<PermissibleValueInterface> defaultPermissibleValuesCollection);
    
    public PermissibleValueInterface getDefaultValue();
    
    public void setDefaultValue(PermissibleValueInterface permissibleValueInterface);
}
