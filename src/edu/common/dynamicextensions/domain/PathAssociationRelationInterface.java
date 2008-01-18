package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;

/**
 * 
 * @author mandar_shidhore
 *
 */
public interface PathAssociationRelationInterface extends DynamicExtensionBaseDomainObjectInterface, Comparable
{
	/**
	 * 
	 * @return
	 */
	public PathInterface getPath();
	
	/**
	 * 
	 * @param path
	 */
	public void setPath(PathInterface path);
	/**
	 * 
	 * @return
	 */
	public AssociationInterface getAssociation();
	/**
	 * 
	 * @param association
	 */
	public void setAssociation(AssociationInterface association);
	/**
	 * 
	 * @return
	 */
	public int getPathSequenceNumber();
	/**
	 * 
	 * @param pathSequenceNumber
	 */
	public void setPathSequenceNumber(int pathSequenceNumber);

}
