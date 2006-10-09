
package edu.common.dynamicextensions.domaininterface.validationrules;

import java.util.Collection;

/**
 * @author sujay_narkar
 *
 */
public interface RuleInterface {
         
    
    /**
     * @return Returns the name.
     */
    public String getName();
     
    
    /**
     * @param name The name to set.
     */
    public void setName(String name);
    /**
     * @return Returns the ruleParameterCollection.
     */
    public Collection getRuleParameterCollection();
    
    /**
     * @param ruleParameterCollection The ruleParameterCollection to set.
     */
    public void setRuleParameterCollection(Collection ruleParameterCollection);
   
           
   
 }
