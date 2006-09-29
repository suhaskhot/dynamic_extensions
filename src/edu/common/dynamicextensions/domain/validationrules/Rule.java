package edu.common.dynamicextensions.domain.validationrules;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class Rule extends AbstractDomainObject implements java.io.Serializable {
    
    protected static final long serialVersionUID = 1234567890L;
    /**
     * 
     */
	protected Long id;
    /**
     * 
     */
	protected String name;
    /**
     * 
     */
	protected Collection ruleParameterCollection;
	/**
     * 
	 *
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
}