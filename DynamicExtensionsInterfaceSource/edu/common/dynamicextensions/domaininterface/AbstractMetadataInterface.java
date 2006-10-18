
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;
import java.util.Date;

/**
 * This is an interface extended by EntityInterface,EntityGroupInterface,AttributeInterface.This interface contains
 * basic information needed for each metadata objects. 
 * @author sujay_narkar
 *
 */
 public interface AbstractMetadataInterface 
 {
	
	/**
     * The date on which object is created
	 * @return Returns the createdDate.
	 */
	 Date getCreatedDate();
		
	/**
	 * @param createdDate The createdDate to set.
	 */
	 void setCreatedDate(Date createdDate);
		
	/**
     * Description of metdata object
	 * @return Returns the description.
	 */
	 String getDescription();
	/**
	 * @param description The description to set.
	 */
	 void setDescription(String description);
		
	/**
     * Uniqueue id for metadata object.
	 * @return Returns the id.
	 */
	 Long getId();
		
	/**
     * The last updated date of metadata object.
	 * @return Returns the lastUpdated.
	 */
	 Date getLastUpdated();
		
	/**
	 * @param lastUpdated The lastUpdated to set.
	 */
	 void setLastUpdated(Date lastUpdated);
		
	/**
     * Name of the metadata object
	 * @return Returns the name.
	 */
	 String getName();
		
	/**
	 * @param name The name to set.
	 */
	 void setName(String name);
		
	/**
     * Semantic property for metadata object.
	 * @return Returns the semanticPropertyCollection.
	 */
	 Collection getSemanticPropertyCollection();
		
	/**
	 * @param semanticPropertyInterface The semanticProperty to set.
	 */
	 void addSemanticProperty(SemanticPropertyInterface semanticPropertyInterface); 
		
	
}
