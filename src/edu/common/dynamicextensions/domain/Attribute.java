package edu.common.dynamicextensions.domain;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 *  
 */
public abstract class Attribute extends AbstractMetadata implements AttributeInterface  {
    
     /**
      * Collection of rules.
      */   
	protected Collection ruleCollection;
    /**
     * Empty constructor
     */
	public Attribute(){

	}
    /**
     * @hibernate.set name="ruleCollection" table="DYEXTN_RULE"
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="ATTRIBUTE_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.validationrules.Rule"
     * @return Returns the ruleCollection.
     */
    public Collection getRuleCollection() {
        return ruleCollection;
    }
    /**
     * @param ruleCollection The ruleCollection to set.
     */
    public void setRuleCollection(Collection ruleCollection) {
        this.ruleCollection = ruleCollection;
    }
   
}