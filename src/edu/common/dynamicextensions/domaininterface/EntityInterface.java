
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.util.global.Constants.InheritanceStrategy;

/**
 * Entity object stores information of the entity.For each entity a dynamic table is generated using the metadata
 * information.
 * @author sujay_narkar
 *
 */
public interface EntityInterface extends AbstractEntityInterface
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
	Collection<AttributeInterface> getAttributeCollection();

	/**
	 * This method return the Collection of Association.
	 * @return the Collection of Association.
	 */
	Collection<AssociationInterface> getAssociationCollection();

	/**
	 * The abstractAttributeInterface to be added
	 * @param abstractAttributeInterface abstract attribute interface
	 */
	void addAbstractAttribute(AbstractAttributeInterface abstractAttribute);

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
	void addAssociation(AssociationInterface associationInterface);

	/**
	 * This method removes association interface from the abstract attribute collection.
	 * @param associationInterface
	 */
	void removeAssociation(AssociationInterface associationInterface);

	/**
	 * @param isAbstract The isAbstract to set.
	 */
	void setAbstract(boolean isAbstract);

	/**
	 * @return Returns the parentEntity.
	 */
	EntityInterface getParentEntity();

	/**
	 * @param parentEntity The parentEntity to set.
	 */
	void setParentEntity(EntityInterface parentEntity);

	/**
	 * This method returns association for all the hierarchy
	 * @return
	 */
	Collection<AssociationInterface> getAllAssociations();

	/**
	 * This method returns attributes for all the hierarchy.
	 * @return  Collection of AttributeInterface
	 */
	Collection<AttributeInterface> getAllAttributes();

	/**
	 * This method returns attributes ONLY for the entity. It does not traverse the heirarchy
	 * @return  Collection of AttributeInterface
	 */
	Collection<AttributeInterface> getEntityAttributes();

	/**
	 * This method returns all the attributes and associations for all the hierarchy.
	 * @return Collection of AbstractAttributeInterface
	 */
	Collection<AbstractAttributeInterface> getAllAbstractAttributes();

	/**
	 * Method returns  attribute based on the id passed.
	 * @param id Long identifier of the abstract attribute
	 * @return
	 */
	AttributeInterface getAttributeByIdentifier(Long id);

	/**
	 * Method returns  association based on the id passed.
	 * @param id Long identifier of the abstract attribute
	 * @return
	 */
	AssociationInterface getAssociationByIdentifier(Long id);

	/**
	 * @return Returns the inheritanceStrategy.
	 */
	InheritanceStrategy getInheritanceStrategy();

	/**
	 *@param inheritanceStrategy The inheritanceStrategy to set.
	 */
	void setInheritanceStrategy(InheritanceStrategy inheritanceStrategy);

	/**
	 *
	 *
	 */
	/*
		void removeAllAbstractAttributes();*/

	/**
	 * @return
	 */
	String getDiscriminatorColumn();

	/**
	 * @param discriminatorColumn
	 */
	void setDiscriminatorColumn(String discriminatorColumn);

	/**
	 * @return
	 */
	String getDiscriminatorValue();

	/**
	 * @param discriminatorValue
	 */
	void setDiscriminatorValue(String discriminatorValue);

	/**
	 * Get all attributes for query
	 */
	Collection<AttributeInterface> getEntityAttributesForQuery();

	/**
	 *
	 * @return
	 */
	int getDataTableState();

	/**
	 *
	 * @param dataTableState
	 */
	void setDataTableState(int dataTableState);

	/**
	 *
	 * @return
	 */
	EntityGroupInterface getEntityGroup();

	/**
	 *
	 * @param entityGroup
	 */
	void setEntityGroup(EntityGroupInterface entityGroup);

	/**
	 *
	 * @param attributeName
	 * @return
	 */
	AttributeInterface getAttributeByName(String attributeName);

	/**
	 *
	 * @param attributeName
	 * @return
	 */
	AbstractAttributeInterface getAbstractAttributeByName(String attributeName);

	/**
	 *
	 * @param attributeName
	 * @return
	 */
	boolean isAttributePresent(String attributeName);

	/**
	 *
	 * @param attributeName
	 * @return
	 */
	boolean isMultiselectAttributePresent(String attributeName);

	/**
	 * This method return the Collection of Association excluding collection attributes.
	 * @return the Collection of Association.
	 */
	Collection<AssociationInterface> getAssociationCollectionExcludingCollectionAttributes();
}
