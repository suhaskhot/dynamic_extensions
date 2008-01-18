
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
	/**
	 * 
	 * @return
	 */
	public Collection<PathAssociationRelationInterface> getPathAssociationRelationCollection();

	/**
	 * 
	 * @param pathAssociationRelationCollection
	 */
	public void setPathAssociationRelationCollection(Collection<PathAssociationRelationInterface> pathAssociationRelationCollection);

	/**
	 * 
	 * @return
	 */
	public List<PathAssociationRelationInterface> getSortedPathAssociationRelationCollection();
	
	/**
	 * 
	 * @param pathAssociationRelationInterface
	 */
	public void addPathAssociationRelation(PathAssociationRelationInterface pathAssociationRelationInterface);

}
