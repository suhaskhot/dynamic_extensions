
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 * For every abstract metadata object semantic properties are associated.
 * @author sujay_narkar
 */
public interface SemanticPropertyInterface
		extends
			DynamicExtensionBaseDomainObjectInterface,
			Comparable
{

	/**
	 * This method returns the concept code.
	 * @return the concept code.
	 */
	String getConceptCode();

	/**
	 * This method sets the concept code.
	 * @param conceptCode the concept code to be set.
	 */
	void setConceptCode(String conceptCode);

	/**
	 * This method returns the concept preferred Name i.e. concept name.
	 * @return Returns the concept preferred Name i.e. concept name
	 */
	String getConceptPreferredName();

	/**
	 * This method sets the conceptPreferredName.
	 * @param conceptPreferredName the term to be set.
	 */
	void setConceptPreferredName(String conceptPreferredName);

	/**
	 * This method returns the conceptDefinitionSource name.
	 * @return the conceptDefinitionSource name.
	 */
	String getConceptDefinitionSource();

	/**
	 * This method sets the conceptDefinitionSource name.
	 * @param conceptDefinitionSource the conceptDefinitionSource name to be set.
	 */
	void setConceptDefinitionSource(String conceptDefinitionSource);

	/**
	 * @return int
	 */
	long getSequenceNumber();

	/**
	 * @param sequenceNumber int
	 */
	void setSequenceNumber(long sequenceNumber);

	/**
	 * This method returns the tem i.e. concept name.
	 * @return the conceptDefinition
	 */
	String getConceptDefinition();

	/**
	 * @param conceptDefinition the conceptDefinition to set
	 */
	void setConceptDefinition(String conceptDefinition);

	/**
	 * This method gets teh list of Qualifiers for the given concept definition
	 * @return the listOfQualifier
	 */
	Collection<SemanticPropertyInterface> getListOfQualifier();

	/**
	 * This method sets the qualifiers for given concept definition
	 * @param listOfQualifier the listOfQualifier to set
	 */
	void setListOfQualifier(Collection<SemanticPropertyInterface> listOfQualifier);
}
