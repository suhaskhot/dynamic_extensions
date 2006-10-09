
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.Role;
import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;

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
     * 
     * @return
     */
    public Entity getSourceEntity();
     
    /**
     * 
     * @param sourceEntity
     */
    public void setSourceEntity(Entity sourceEntity);
     
    /**
     * 
     * @return
     */
    public Entity getTargetEntity();
  
    
    /**
     * 
     * @param sourceEntity
     */
    public void setTargetEntity(Entity targetEntity);
       
    /**
     * 
     * @return
     */
    public Role getSourceRole();
   
    /**
     * 
     * @param sourceEntity
     */
    public void setSourceRole(Role sourceRole);
        
    /**
     * 
     * @return
     */
    public Role getTargetRole();
    
    /**
     * 
     * @param sourceEntity
     */
    public void setTargetRole(Role targetRole);
      
    
    /**
     * 
     * @return
     */
    public ConstraintProperties getConstraintProperties();
      
    
    
    /**
     * 
     * @param sourceEntity
     */
    public void setConstraintProperties(ConstraintProperties constraintProperties);
    

}
