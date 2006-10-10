
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;

/**
 * @author sujay_narkar
 *
 */
public interface AssociationInterface {
    /**
     * @return Returns the direction.
     */
    public String getDirection();
    
    /**
     * @param direction The direction to set.
     */
    public void setDirection(String direction);
          
    /**
     * 
     * @return
     */
    public EntityInterface getSourceEntity();
     
    /**
     * 
     * @param sourceEntity
     */
    public void setSourceEntity(EntityInterface sourceEntityInterface);
     
    /**
     * 
     * @return
     */
    public EntityInterface getTargetEntity();
    
    /**
     * 
     * @param sourceEntity
     */
    public void setTargetEntity(EntityInterface targetEntityInterface);
       
    /**
     * 
     * @return
     */
    public RoleInterface getSourceRole();
   
    /**
     * 
     * @param sourceEntity
     */
    public void setSourceRole(RoleInterface sourceRoleInterface);
        
    /**
     * 
     * @return
     */
    public RoleInterface getTargetRole();
    
    /**
     * 
     * @param sourceEntity
     */
    public void setTargetRole(RoleInterface targetRoleInterface);

    /**
     * 
     * @return
     */
    public ConstraintPropertiesInterface getConstraintProperties();
      
    /**
     * 
     * @param sourceEntity
     */
    public void setConstraintProperties(ConstraintPropertiesInterface constraintPropertiesInterface);
    

}
