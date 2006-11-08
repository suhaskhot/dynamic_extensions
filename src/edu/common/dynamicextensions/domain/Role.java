
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represents the role of the Association.
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_ROLE"
 */
public class Role extends AbstractDomainObject implements RoleInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 8674217047857771139L;

	/**
	 * Unique identifier for the object
	 */
	protected Long id;

	/**
	 * The association type : containment or linking
	 */
	protected String associationType;

	/**
	 *  Maximum cardinality of this role.
	 */
	protected Integer maxCardinality;

	/**
	 * Minimum cardinality of this role.
	 */
	protected Integer minCardinality;

	/**
	 * Name of the role.
	 */
	protected String name;

	/**
	 * Empty constructor.
	 */
	public Role()
	{
	}

	/**
	 * This method returns the Unique identifier of this Object.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_ROLE_SEQ"
	 * @return the Unique identifier of this Object.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method sets the Unique identifier of this Object.
	 * @param id the Unique identifier to be set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * This method returns the type of Association.
	 * @hibernate.property name="associationType" type="string" column="ASSOCIATION_TYPE"
	 * @return the type of Association. 
	 */
	public String getAssociationType()
	{
		return associationType;
	}

	/**
	 * This method sets the type of Association. 
	 * @param associationType the type of Association to be set.
	 */
	public void setAssociationType(String associationType)
	{
		this.associationType = associationType;
	}

	/**
	 * This method returns the maximum cardinality.
	 * @hibernate.property name="maxCardinality" type="integer" column="MAX_CARDINALITY" 
	 * @return the maximum cardinality.
	 */
	public Integer getMaxCardinality()
	{
		return maxCardinality;
	}

	/**
	 * This method sets the maximum cardinality.
	 * @param maxCardinality the value to be set as maximum cardinality.
	 */
	public void setMaxCardinality(Integer maxCardinality)
	{
		this.maxCardinality = maxCardinality;
	}

	/**
	 * This method returns the minimum cardinality.
	 * @hibernate.property name="minCardinality" type="integer" column="MIN_CARDINALITY" 
	 * @return Returns the minimum cardinality.
	 */
	public Integer getMinCardinality()
	{
		return minCardinality;
	}

	/**
	 * This method sets the minimum cardinality.
	 * @param minCardinality the value to be set as minimum cardinality.
	 */
	public void setMinCardinality(Integer minCardinality)
	{
		this.minCardinality = minCardinality;
	}

	/**
	 * This method returns the name of the role.
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return the name of the role.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * This method sets the name of the role.
	 * @param name the name to be set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Set all values from the form
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
	}

	/**
	 * This method returns the Unique identifier of this Object.
	 * @return the Unique identifier of this Object.
	 */
	public Long getSystemIdentifier()
	{
		return null;
	}

	/**
	 * This method sets the Unique identifier of this Object.
	 * @param id the Unique identifier to be set.
	 */
	public void setSystemIdentifier(Long id)
	{
	}
	
}