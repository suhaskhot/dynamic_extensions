
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;
import java.util.Date;

/**
 * @author sujay_narkar
 *
 */
 public interface AbstractMetadataInterface {
	
	/**
	 * @return Returns the createdDate.
	 */
	public Date getCreatedDate();
		
	/**
	 * @param createdDate The createdDate to set.
	 */
	public void setCreatedDate(Date createdDate);
		
	/**
	 * @return Returns the description.
	 */
	public String getDescription();
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description);
		
	/**
	 * @return Returns the id.
	 */
	public Long getId();
		
	/**
	 * @return Returns the lastUpdated.
	 */
	public Date getLastUpdated();
		
	/**
	 * @param lastUpdated The lastUpdated to set.
	 */
	public void setLastUpdated(Date lastUpdated);
		
	/**
	 * @return Returns the name.
	 */
	public String getName();
		
	/**
	 * @param name The name to set.
	 */
	public void setName(String name);
		
	/**
	 * @return Returns the semanticPropertyCollection.
	 */
	public Collection getSemanticPropertyCollection();
		
	/**
	 * @param semanticPropertyCollection The semanticPropertyCollection to set.
	 */
	public void addSemanticProperty(SemanticPropertyInterface semanticPropertyInterface); 
		
	
}
