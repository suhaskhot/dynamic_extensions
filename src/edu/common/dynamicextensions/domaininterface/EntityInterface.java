
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;

/**
 * Entity object stores information of the entity.For each entity a dynamic table is generated using the metadata
 * information.
 * @author sujay_narkar
 *
 */
public interface EntityInterface extends AbstractMetadataInterface
{

	/**
	 * This method returns the Collection of AbstractAttribute.
	 * @return the Collection of AbstractAttribute.
	 */
	Collection<AbstractAttributeInterface> getAbstractAttributeCollection();

	/**
	 * This method return the Collection of Attributes.
	 * @return the Collection of Attributes.
	 */
	Collection getAttributeCollection();

	/**
	 * This method return the Collection of Association.
	 * @return the Collection of Association.
	 */
	Collection getAssociationCollection();

	/**
	 * The abstractAttributeInterface to be added 
	 * @param abstractAttributeInterface abstract attribute interface 
	 */
	void addAbstractAttribute(AbstractAttributeInterface abstractAttribute);

	/**
	 * Returns a collection of entity groups having this entity. 
	 * @return Returns the entityGroupCollection.
	 */
	Collection<EntityGroupInterface> getEntityGroupCollection();

	/**
	 * Adds an entity group to the entity 
	 * @param entityGroupInterface The entityGroupInterface to be added set.
	 * 
	 */
	void addEntityGroupInterface(EntityGroupInterface entityGroup);

	/**
	 * The table properties object contains name of the dynamically created table.
	 * @return
	 */
	TablePropertiesInterface getTableProperties();

	/**
	 * @param tableProperties
	 */
	void setTableProperties(TablePropertiesInterface tableProperties);

	/**
	 * This method removes an AbstractAttribute from the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute an AbstractAttribute to be removed.
	 */
	void removeAbstractAttribute(AbstractAttributeInterface abstractAttribute);

	/**
	 * This method adds attribute interface to the abstract attribute collection.
	 * @param attributeInterface
	 */
	void addAttribute(AttributeInterface attributeInterface);

	/**
	 * This method adds attribute interface to the abstract attribute collection.
	 * @param attributeInterface
	 */
	void removeAttribute(AttributeInterface attributeInterface);

	/**
	 * This method removes association interface from the abstract attribute collection.
	 * @param associationInterface
	 */
	public void addAssociation(AssociationInterface associationInterface);

	/**
	 * This method removes association interface from the abstract attribute collection.
	 * @param associationInterface
	 */
	public void removeAssociation(AssociationInterface associationInterface);

	/**
	 * This method removes all entity groupa of the entity.
	 *
	 */
	public void removeEntityGroupInterface(EntityGroupInterface entityGroupInterface);

	/**
	 * This method removes all entity groupa of the entity.
	 *
	 */
	public void removeAllEntityGroups();

}
