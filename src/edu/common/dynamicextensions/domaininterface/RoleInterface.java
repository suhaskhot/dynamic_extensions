
package edu.common.dynamicextensions.domaininterface;

/**
 * For every entity association there are two roles invoved.They are source role and target role.
 * @author sujay_narkar
 *
 */
public interface RoleInterface
{

	/**
	 * This method returns the Unique identifier of this Object.
	 * @return the Unique identifier of this Object.
	 */
	Long getId();

	/**
	 * This method returns the type of Association.
	 * @return the type of Association. 
	 */
	String getAssociationType();

	/**
	 * This method sets the type of Association. 
	 * @param associationType the type of Association to be set.
	 */
	void setAssociationType(String associationType);

	/**
	 * This method returns the maximum cardinality.
	 * @return the maximum cardinality.
	 */
	Integer getMaxCardinality();

	/**
	 * This method sets the maximum cardinality.
	 * @param maxCardinality the value to be set as maximum cardinality.
	 */
	void setMaxCardinality(Integer maxCardinality);

	/**
	 * This method returns the minimum cardinality.
	 * @return Returns the minimum cardinality.
	 */
	Integer getMinCardinality();

	/**
	 * This method sets the minimum cardinality.
	 * @param minCardinality the value to be set as minimum cardinality.
	 */
	void setMinCardinality(Integer minCardinality);

	/**
	 * This method returns the name of the role.
	 * @return the name of the role.
	 */
	String getName();

	/**
	 * This method sets the name of the role.
	 * @param name the name to be set.
	 */
	void setName(String name);

}
