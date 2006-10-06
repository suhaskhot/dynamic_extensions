package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

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
    public void setRuleCollection(Collection ruleCollection);
   
}
