package edu.common.dynamicextensions.domain;
import java.util.Collection;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 *  
 */
public abstract class Attribute extends AbstractMetadata  implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1234567890L;
     /**
      * 
      */   
	protected Collection ruleCollection;
    /**
     * 
     *
     */
	public Attribute(){

	}
    /**
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