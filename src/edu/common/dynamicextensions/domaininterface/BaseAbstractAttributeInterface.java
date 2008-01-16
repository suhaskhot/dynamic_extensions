package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;


/**
 * This is an interface extended by CategoryAttribute, AbstractAttribute.
 * This class stores basic information needed for metadata objects.
 */
public interface BaseAbstractAttributeInterface extends AbstractMetadataInterface
{
	
	/**
	 * This method returns the Collection of rules.
	 * @return Collection the ruleCollection associated with the Attribute.
	 */
	Collection<RuleInterface> getRuleCollection();

}
