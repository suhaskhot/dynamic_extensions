
package edu.common.dynamicextensions.domaininterface;

/**
 * @author kunal_kamble
 *
 */
public interface IdGeneratorInterface extends DynamicExtensionBaseDomainObjectInterface
{

	/**
	 * @return the next available unique id 
	 */
	Long getNextAvailableId();

	/**
	 * @param nextAvailableId 
	 */
	void setNextAvailableId(Long nextAvailableId);

	/**
	 * @param id
	 */
	void setId(Long id);

}
