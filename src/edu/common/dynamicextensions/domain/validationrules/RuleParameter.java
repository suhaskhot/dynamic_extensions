
package edu.common.dynamicextensions.domain.validationrules;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Rule 
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_RULE_PARAMETER"
 */
public class RuleParameter extends AbstractDomainObject implements Serializable, RuleParameterInterface
{
	
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 7900652797513990927L;
	
	/**
	 * Unique identifier for the object
	 */
	protected Long id;
	
	/**
	 * Name of the rule parameter.
	 */
	protected String name;
	
	/**
	 * Value of the rule parameter.
	 */
	protected String value;

	/**
	 * Empty constructor.
	 */
	public RuleParameter()
	{
	}

	/**
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_RULE_PARAMETER_SEQ"
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.property name="value" type="string" column="VALUE" 
	 * @return Returns the value.
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#getSystemIdentifier()
	 */
	public Long getSystemIdentifier()
	{
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.id = systemIdentifier;

	}
}