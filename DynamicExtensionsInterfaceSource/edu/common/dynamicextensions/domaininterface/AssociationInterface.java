
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;

/**
 * This interface contains all the information regarding association between the entities.
 * The association may be of different types like one-to-many,many-to-many or from source - destination,
 * bidirectional.Entity object contains association collection.Each object in association collection represents 
 * association of  the entity with other entity.
 *  Using the information of association object different constraints are added in the dynamically created tables.  
 * 
 * @author sujay_narkar
 *
 */
public interface AssociationInterface
{
    /**
     * This is a direction for the association.The direction may be source - destination or
     * bidirectional. 
     * @return Returns the direction.
     */
     String getDirection();
    
    /**
     * @param direction The direction to set.
     */
     void setDirection(String direction);
          
    /**
     * Source entity object in the association.
     * @return EntityInterface
     */
     EntityInterface getSourceEntity();
     
    /**
     * 
     * @param sourceEntityInterface source entity interface to be set.
     */
     void setSourceEntity(EntityInterface sourceEntityInterface);
     
    /**
     * Target entity object for the association
     * @return EntityInterface
     */
     EntityInterface getTargetEntity();
    
    /**
     * 
     * @param targetEntityInterface target entity to be set.
     */
     void setTargetEntity(EntityInterface targetEntityInterface);
       
    /**
     * Source role for the association.Source role stores information such as min cardinality,
     * max cardinality etc information of source entity. 
     * @return RoleInterface
     */
     RoleInterface getSourceRole();
   
    /**
     * 
     * @param sourceRoleInterface source role to be set.
     */
     void setSourceRole(RoleInterface sourceRoleInterface);
        
    /**
     * Target role for the assoiation.target role stores information such as min cardinality,
     * max cardinality etc information of target entity. 
     * @return RoleInterface
     */
     RoleInterface getTargetRole();
    
    /**
     * 
     * @param targetRoleInterface target role to be set.
     */
     void setTargetRole(RoleInterface targetRoleInterface);

    /**
     * Constraint properties store the database information of the dynamically created  tables 
     * for the association. e.g. If the association type is many to many we need to store middle table name
     * and the foreign keys of both the tables. 
     * @return ConstraintPropertiesInterface
     */
    ConstraintPropertiesInterface getConstraintProperties();
    
}
