
package edu.common.dynamicextensions.domaininterface.validationrules;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 *
 */
public interface RuleInterface {
    
    /**
     * @return Returns the id. 
     */
    public Long getId();
     
    
    /**
     * @param id The id to set.
     */
    public void setId(Long id);
       
    
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
    
    /**
     * @param arg0
     * @throws AssignDataException
     */
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException; 
           
    /**
     * @return
     */
    public Long getSystemIdentifier();
       
    
    
    /**
     * 
     * @param systemIdentifier
     */
    public void setSystemIdentifier(Long systemIdentifier);
 }
