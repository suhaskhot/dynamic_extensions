package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.Date;

import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.class table="DYEXTN_ABSTRACT_METADATA"
 */
public abstract class AbstractMetadata extends AbstractDomainObject implements java.io.Serializable,AbstractMetadataInterface {
	
	protected static final long serialVersionUID = 1234567890L;
	
	/**
	 * 
	 */
	protected Long id;
	/**
	 * 
	 */
	protected String name;
	/**
	 * 
	 */
	protected String description;
	/**
	 * 
	 */
	protected Date lastUpdated;
	/**
	 * 
	 */
	protected Date createdDate;
    /**
     * 
     */
	
	protected Collection semanticPropertyCollection;
	
	public AbstractMetadata(){
		
	}
	
	/**
	 * @hibernate.property name="createdDate" type="date" column="CREATED_DATE" 
	 * @return Returns the createdDate.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate The createdDate to set.
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @hibernate.property name="description" type="string" column="DESCRIPTION"
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_ABSTRACT_METADATA_SEQ"
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @hibernate.property name="lastUpdated" type="date" column="LAST_UPDATED" 
	 * @return Returns the lastUpdated.
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}
	/**
	 * @param lastUpdated The lastUpdated to set.
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	/**
	 * @hibernate.property name="name" type="string" column="NAME"
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.set name="semanticPropertyCollection" cascade="save-update"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_METADATA_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.SemanticProperty"
	 * @return Returns the semanticPropertyCollection.
	 */
	public Collection getSemanticPropertyCollection() {
		return semanticPropertyCollection;
	}
	/**
	 * @param semanticPropertyCollection The semanticPropertyCollection to set.
	 */
	public void setSemanticPropertyCollection(
			Collection semanticPropertyCollection) {
		this.semanticPropertyCollection = semanticPropertyCollection;
	}
		
	/**
	 * 
	 */
	public Long getSystemIdentifier() {
		return id;
	}
	/**
	 * 
	 */
	public void setSystemIdentifier(Long systemIdentifier) {
		this.id = systemIdentifier; 
		
	}
	
	/**
	 * 
	 */
	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof AbstractMetadata) {
			AbstractMetadata abstractMetadata = (AbstractMetadata) obj;
			Long thisId = getId();
			
			if (thisId != null && thisId.equals(abstractMetadata.getId())) {
				eq = true;
			}
			
		}
		return eq;
	}
	
	/**
	 * 
	 */ 
	public int hashCode() {
		int h = 0;
		
		if (getId() != null) {
			h += getId().hashCode();
		}
		return h;
	}
}