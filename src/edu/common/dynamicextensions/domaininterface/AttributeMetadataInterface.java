
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 * @author kunal_kamble
 *
 */
public interface AttributeMetadataInterface extends AbstractMetadataInterface
{

	/**
	 * This method returns the default value of the PrimitiveAttribute for displaying in corresponding controls on UI. 
	 * @param abstractAttribute the PrimitiveAttribute
	 * @return the Default Value of the PrimitiveAttribute
	 */
	String getDefaultValue();

	/**
	 * This method returns the length if the attribute is of type
	 * string otherwise returns -1.
	 * @return
	 */
	int getMaxSize();

	/**
	 * This method returns the measurement units of the numeric attribute associated with this Control.
	 * @param abstractAttribute AbstractAttribute whose measurement units are to be known.
	 * @return the measurement units of the numeric attribute associated with this Control.
	 */
	String getMeasurementUnit();

	/**
	 * This method returns the decimal paces of the numeric attribute associated with this Control.
	 * If attribute is not of type double returns -1
	 * @return
	 */
	int getDecimalPlaces();

	/**
	 * This method returns the Collection of rules.
	 * @return Collection the ruleCollection associated with the Attribute.
	 */
	Collection<RuleInterface> getRuleCollection();

	/**
	 * 
	 * @return AttributeTypeInformationInterface
	 */
	AttributeTypeInformationInterface getAttributeTypeInformation();

	/**
	 * This method returns a data element for an attribute.
	 * @return DataElementInterface
	 */
	DataElementInterface getDataElement();

}
