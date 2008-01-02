package edu.common.dynamicextensions.domaininterface;

public interface CategoryAttributeInterface extends AbstractMetadataInterface
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
}
