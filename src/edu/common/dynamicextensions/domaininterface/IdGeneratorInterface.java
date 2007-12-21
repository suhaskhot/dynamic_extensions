package edu.common.dynamicextensions.domaininterface;

/**
 * @author kunal_kamble
 *
 */
public interface IdGeneratorInterface extends DynamicExtensionBaseDomainObjectInterface{
	
	/**
	 * @return the next available unique id 
	 */
	public Long getNextAvailableId();
	
	/**
	 * @param nextAvailableId 
	 */
	public void setNextAvailableId(Long nextAvailableId);
	
	/**
	 * @param id
	 */
	public void setId(Long id) ;

}
