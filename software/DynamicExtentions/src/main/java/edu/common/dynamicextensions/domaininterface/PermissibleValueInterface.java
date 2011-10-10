
package edu.common.dynamicextensions.domaininterface;

import java.text.ParseException;
import java.util.Collection;

import edu.common.dynamicextensions.domain.SemanticAnnotatableInterface;

/**
 * When the value domain for an attribute is user defined,the data element object is of type CaDSRDE
 * and this object contains a collection of permissible values.
 * @author sujay_narkar

 */
public interface PermissibleValueInterface extends SemanticAnnotatableInterface
{

	/**
	 * This method returns the unique identifier.
	 * @return the unique identifier.
	 */
	Long getId();

	/**
	 * This method returns the value of DateValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	Object getValueAsObject();

	/**
	 * Gets the numeric code.
	 * @return the numericCode
	 */
	Long getNumericCode();

	/**
	 * Sets the numeric code.
	 * @param numericCode the numericCode to set
	 */
	void setNumericCode(Long numericCode);

	/**
	 * Gets the dependent skip logic attributes.
	 * @return the dependent skip logic attributes
	 */
	Collection<SkipLogicAttributeInterface> getDependentSkipLogicAttributes();


	/**
	 * Sets the dependent skip logic attributes.
	 * @param dependentSkipLogicAttributes the new dependent skip logic attributes
	 */
	void setDependentSkipLogicAttributes(
			Collection<SkipLogicAttributeInterface> dependentSkipLogicAttributes);

	/**
	 * This method adds a skip logic attribute.
	 * @param skipLogicAttributeInterface
	 */
	void addDependentSkipLogicAttribute(SkipLogicAttributeInterface skipLogicAttributeInterface);

	/**
	 * This method removes a SkipLogic Attribute.
	 * @param skipLogicAttributeInterface.
	 */
	void removeDependentSkipLogicAttribute(SkipLogicAttributeInterface skipLogicAttributeInterface);

	/**
	 * This method removes all SkipLogic Attributes.
	 */
	void removeAllDependentSkipLogicAttributes();

	/**
	 * @param permissibleValue
	 * @return
	 */
	PermissibleValueInterface getObjectCopy();

	/**
	 * Sets the semantic property collection.
	 * @param semanticPropertyCollection The semanticPropertyCollection to set.
	 */
	void setSemanticPropertyCollection(Collection semanticPropertyCollection);

	/**
	 * Sets the value as string.
	 * @param value the new value as string
	 * @throws ParseException the parse exception
	 */
	void setObjectValue(Object value) throws ParseException;

}
