package edu.common.dynamicextensions.domaininterface;



/**
 * These are the attributes in the entities.
 * @author geetika_bangard
 */
public interface AttributeInterface extends AbstractAttributeInterface 
{
   
	/**
     * The attribute can be a co
	 * @return Returns the isCollection.
	 */
	public Boolean getIsCollection() ;
	/**
	 * @param isCollection The isCollection to set.
	 */
	public void setIsCollection(Boolean isCollection);
	/**
	 * @return Returns the isIdentified.
	 */
	public Boolean getIsIdentified();
	/**
	 * @param isIdentified The isIdentified to set.
	 */
	public void setIsIdentified(Boolean isIdentified);
	/**
	 * @return Returns the isPrimaryKey.
	 */
	public Boolean getIsPrimaryKey();
	/**
	 * @param isPrimaryKey The isPrimaryKey to set.
	 */
	public void setIsPrimaryKey(Boolean isPrimaryKey);
    
     /**
      * @return
     */
    public DataElementInterface getDataElement();
     
    /**
     * @param sourceEntity
     */
    public void setDataElement(DataElementInterface dataElementInterface);
  
}
