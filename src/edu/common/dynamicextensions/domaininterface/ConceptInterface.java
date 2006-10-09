package edu.common.dynamicextensions.domaininterface;

/**
 * @author geetika_bangard
 */
public interface ConceptInterface {
    
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
	 * @param id The id to set.
	 */
	public void setId(Long id);
	/**
	 * @return Returns the publicId.
	 */
	public String getPublicId();
	/**
	 * @param publicId The publicId to set.
	 */
	public void setPublicId(String publicId);
	/**
	 * @return Returns the source.
	 */
	public String getSource();
	/**
	 * @param source The source to set.
	 */
	public void setSource(String source);
    
    /**
     * 
     */
    public Long getSystemIdentifier();
    /**
     * 
     */
    public void setSystemIdentifier(Long systemIdentifier);

}
