
package edu.common.dynamicextensions.domaininterface;


/**
 * For every entity association there are two roles invoved.They are source role and target role.
 * @author sujay_narkar
 *
 */
public interface RoleInterface 
{
    
    /**
     * @return  Long id
     */
     Long getId();

    /**
     * The association type may be containment or linking.
     * @return Returns the associationType.
     */
     String getAssociationType(); 
   
    
    /**
     * @param associationType The associationType to set.
     */
     void setAssociationType(String associationType);
  
  
    /**
     * The maximum cardinality for the role. 
     * @return Returns the maxCardinality.
     */
     Integer getMaxCardinality();
  
    /**
     * 
     * @param maxCardinality The maxCardinality to set.
     */
     void setMaxCardinality(Integer maxCardinality);
    
    /**
     * The minimum cardinality for the role
     * @hibernate.property name="minCardinality" type="integer" column="MIN_CARDINALITY" 
     * @return Returns the minCardinality.
     */
     Integer getMinCardinality();
    
    /**
     * @param minCardinality The minCardinality to set.
     */
     void setMinCardinality(Integer minCardinality);
        
    /**
     * name of the role
     * @return Returns the name.
     */
     String getName();
        
    
    /**
     * @param name The name to set.
     */
     void setName(String name);

}
