
package edu.common.dynamicextensions.domain.validationrules;

import java.util.Collection;
import java.util.LinkedHashSet;

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
public class Rule extends AbstractDomainObject implements java.io.Serializable, RuleInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 6495330944005526400L;

	/**
	 * Unique identifier for the object
	 */
	protected Long id;

	/**
	 * Name of the rule.
	 */
	protected String name;

	/**
	 * The Collection of RuleParameter.
	 */
	protected Collection<RuleParameterInterface> ruleParameterCollection;

	/**
	 * Empty Constructor.
	 */
	public Rule()
	{
	}

	/**
	 * This method returns the Unique Identifier of the Object.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_RULE_SEQ"
	 * @return the Unique Identifier of the Object.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method sets the Unique Identifier of the Object.
	 * @param id the Unique Identifer to be set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * This method returns the name of the Rule.
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return the name of the Rule.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * This method sets the name of the Rule.
	 * @param name the name to be set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * This method returns the Collection of RuleParameters.
	 * @hibernate.set name="ruleParameterCollection" table="DYEXTN_RULE_PARAMETER"
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="RULE_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.validationrules.RuleParameter"
	 * @return the Collection of RuleParameters.
	 */
	public Collection<RuleParameterInterface> getRuleParameterCollection()
	{
		return ruleParameterCollection;
	}

	/**
	 * This method sets ruleParameterCollection to the Collection of RuleParmeters.
	 * @param ruleParameterCollection the the Collection of RuleParmeters to be set.
	 */
	public void setRuleParameterCollection(Collection<RuleParameterInterface> ruleParameterCollection)
	{
		this.ruleParameterCollection = ruleParameterCollection;
	}

	/**
	 * This method set all values from the form.
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
	}

	/**
	 * This method returns the System Identifier.
	 * @return the System Identifier
	 */
	public Long getSystemIdentifier()
	{
		return this.id;
	}

	/**
	 * This method sets the System Identifier.
	 * @param systemIdentifier the System Identifer to be set.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.id = systemIdentifier;
	}

	/**
	 * This method adds a RuleParameter to the Collection of RuleParameter.
	 * @param ruleParameter RuleParameter to be added.
	 */
	public void addRuleParameter(RuleParameterInterface ruleParameter)
	{
		if (ruleParameterCollection == null)
		{
			ruleParameterCollection = new LinkedHashSet<RuleParameterInterface>();
		}
		ruleParameterCollection.add(ruleParameter);
	}

}