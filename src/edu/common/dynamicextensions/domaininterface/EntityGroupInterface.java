
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 * Entities can be grouped in the 
 * @author geetika_bangard
 */
public interface EntityGroupInterface extends AbstractMetadataInterface {

    /**
     * @return Returns the entityCollection.
     */
    public Collection getEntityCollection();
    /**
     * @param entityCollection The entityCollection to set.
     */
    public void addEntity(EntityInterface entityInterface);
   
	/**
	 * @return Returns the longName.
	 */
	public String getLongName();
	/**
	 * @param longName The longName to set.
	 */
	public void setLongName(String longName);
	/**
	 * @return Returns the shortName.
	 */
	public String getShortName();
	/**
	 * @param shortName The shortName to set.
	 */
	public void setShortName(String shortName);
	/**
	 * @return Returns the version.
	 */
	public String getVersion();
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version);
}
