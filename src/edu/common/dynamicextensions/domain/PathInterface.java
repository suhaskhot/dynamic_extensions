
package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;

public interface PathInterface extends DynamicExtensionBaseDomainObjectInterface
{
	//	/**
	//	 * This method adds an AssociationInterface to the Path's Collection of Association.
	//	 * @param associationInterface AssociationInterface to be added.
	//	 */
	//	void addAssociation(AssociationInterface associationInterface);
	//	/**
	//	 * This method removes an AssociationInterface from thePath's Collection of Association.
	//	 * @param associationInterface an AssociationInterface to be removed.
	//	 */
	//	void removeAssociation(AssociationInterface associationInterface);
	//	/**
	//	 * This method removes all the AssociationInterface from thePath's Collection of Association.
	//	 * @param associationInterface an AssociationInterface to be removed.
	//	 */
	//	void removeAllAssociations();

	public Collection<PathAssociationRelationInterface> getPathAssociationRelation();

	public void setPathAssociationRelation(Collection<PathAssociationRelationInterface> pathAssociationRelation);

}
