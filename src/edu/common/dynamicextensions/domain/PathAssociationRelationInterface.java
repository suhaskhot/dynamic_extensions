package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;

/**
 * 
 * @author mandar_shidhore
 *
 */
public interface PathAssociationRelationInterface extends DynamicExtensionBaseDomainObjectInterface
{
	public Path getPath();
	
	public void setPath(Path path);
	
	public Association getAssociation();
	
	public void setAssociation(Association association);
	
	public int getPathSequenceNumber();
	
	public void setPathSequenceNumber(int pathSequenceNumber);

}
