
package edu.common.dynamicextensions.domaininterface;


/**
 * @author sujay_narkar
 *
 */
public interface RoleInterface {
    
    /**
     * @return
     */
    public Long getId();

    /**
     * @return Returns the associationType.
     */
    public String getAssociationType(); 
   
    
    /**
     * @param associationType The associationType to set.
     */
    public void setAssociationType(String associationType);
  
  
    /**
     * @return Returns the maxCardinality.
     */
    public Integer getMaxCardinality();
  
    /**
     * @param maxCardinality The maxCardinality to set.
     */
    public void setMaxCardinality(Integer maxCardinality);
    
    /**
     * @hibernate.property name="minCardinality" type="integer" column="MIN_CARDINALITY" 
     * @return Returns the minCardinality.
     */
    public Integer getMinCardinality();
    
    /**
     * @param minCardinality The minCardinality to set.
     */
    public void setMinCardinality(Integer minCardinality);
        
    /**
     * @return Returns the name.
     */
    public String getName();
        
    
    /**
     * @param name The name to set.
     */
    public void setName(String name);

}
