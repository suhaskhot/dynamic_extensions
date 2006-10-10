package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 * @author geetika_bangard
 */
public interface AttributeInterface extends AbstractMetadataInterface {

    /**
     * @return Returns the ruleCollection.
     */
    public Collection getRuleCollection();
    /**
     * @param ruleCollection The ruleCollection to set.
     */
    public void addRule(RuleInterface  ruleInterface);
   
}
