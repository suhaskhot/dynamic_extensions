package edu.common.cawebeav.dynamicextensions.validationrules;

import java.util.Collection;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class Rule {

	protected Long id;
	protected String name;
	protected Collection ruleParameterCollection;

	public Rule(){

	}

	public void finalize() throws Throwable {

	}
	
	

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the ruleParameterCollection.
     */
    public Collection getRuleParameterCollection() {
        return ruleParameterCollection;
    }
    /**
     * @param ruleParameterCollection The ruleParameterCollection to set.
     */
    public void setRuleParameterCollection(Collection ruleParameterCollection) {
        this.ruleParameterCollection = ruleParameterCollection;
    }
}