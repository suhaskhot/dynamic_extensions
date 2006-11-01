package edu.common.dynamicextensions.domain.validationrules;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_RULE"
 */
public class Rule extends AbstractDomainObject implements java.io.Serializable,RuleInterface {
    
    /**
     * Unique identifier for the object
     */
	protected Long id;
    /**
     * Name of the rule.
     */
	protected String name;
    /**
     * The rule parameter collection.
     */
	protected Collection ruleParameterCollection;
	/**
	 * Empty Constructor.
	 */
	public Rule(){

	}
    /**
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_RULE_SEQ"
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
     * @hibernate.property name="name" type="string" column="NAME" 
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
     * @hibernate.set name="ruleParameterCollection" table="DYEXTN_RULE_PARAMETER"
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="RULE_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.validationrules.RuleParameter"
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
    /**
     * 
     * @param arg0
     * @throws AssignDataException
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * 
     * @return
     */
	public Long getSystemIdentifier() {
		return this.id;
	}
	
    /**
     * 
     * @param systemIdentifier
     */
	public void setSystemIdentifier(Long systemIdentifier) {
        this.id = systemIdentifier;
	}
    
    /**
     * Adds a rule parameter to this rule
     */
	public void addRuleParameter(RuleParameterInterface ruleParameterInterface) {
		if (ruleParameterCollection == null) {
			ruleParameterCollection = new HashSet();
		}
		ruleParameterCollection.add(ruleParameterInterface);
	}
}