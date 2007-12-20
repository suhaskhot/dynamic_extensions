package edu.common.dynamicextensions.domaininterface;

import java.util.Set;

import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.Entity;

public interface CategoryEntityInterface extends AbstractMetadataInterface
{
    public Set<CategoryAttribute> getCategoryAttributeCollection();
    
    public void setCategoryAttributeCollection(Set<CategoryAttribute> categoryAttributeCollection);
    
    public Set<CategoryEntity> getChildCategories();
    
    public void setChildCategories(Set<CategoryEntity> childCategories);
    
    public Entity getEntity();
    
    public void setEntity(Entity entity);
}
