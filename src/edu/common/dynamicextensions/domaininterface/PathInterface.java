
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;
import java.util.List;

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

	public List<PathAssociationRelationInterface> getSortedPathAssociationRelationCollection();

}
