
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;

/**
 * 
 * @author mandar_shidhore
 *
 */
public interface PathInterface extends DynamicExtensionBaseDomainObjectInterface
{
	public Collection<PathAssociationRelationInterface> getPathAssociationRelationCollection();

	public void setPathAssociationRelationCollection(Collection<PathAssociationRelationInterface> pathAssociationRelationCollection);

	public Collection<PathAssociationRelationInterface> getSortedPathAssociationRelationCollection();

}
