package edu.common.dynamicextensions.domain;
import java.util.Collection;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 */
public class Attribute extends AbstractMetadata {

	protected Collection ruleCollection;

	public Attribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
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