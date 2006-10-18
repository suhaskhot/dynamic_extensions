package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 * This interface is extended by AssociationInterface and PrimitiveAttributeInterface.
 * Associations are also treated as attributes.  
 *      Using the information of Attribute object coulmns are perpared in the dynamically create tables.
 * @author geetika_bangard
 */
public interface AbstractAttributeInterface extends AbstractMetadataInterface
{

    /**
     * Rules are the validation rules associated with attributes.
     * @return Returns the ruleCollection.
     */
     Collection getRuleCollection();
    /**
     * @param ruleInterface The ruleInterface to be set.
     */
    void addRule(RuleInterface  ruleInterface);
    /**
     * 
     * @return EntityInterface entity interface
     */
    EntityInterface getEntity();
    /**
     * Returns an entity associated with the attribute
     * @param entityInterface entity interface
     */
    
    void setEntity(EntityInterface entityInterface);
   
}
