package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Map;

import edu.common.dynamicextensions.domain.Attribute;

/**
 * @author geetika_bangard
 */
public interface ControlInterface {
    
    /**
	 * @return
	 */
	public Long getId();
	

	/**
	 * @return Returns the attribute.
	 *
	 */
	public Attribute getAttribute();
	/**
	 * @param attribute The attribute to set.
	 */
	public void setAttribute(Attribute attribute);
	/**
	 * @return Returns the caption.
	 */
	public String getCaption();
	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption);
	/**
	 * @return Returns the cssClass.
	 */
	public String getCssClass();
	/**
	 * @param cssClass The cssClass to set.
	 */
	public void setCssClass(String cssClass);
	/**
	 * @return Returns the isHidden.
	 */
	public Boolean getIsHidden();
	/**
	 * @param isHidden The isHidden to set.
	 */
	public void setIsHidden(Boolean isHidden);
	/**
	 * @return Returns the name.
	 */
	public String getName();
	/**
	 * @param name The name to set.
	 */
	public void setName(String name);
	/**
	 * @return Returns the sequenceNumber.
	 */
	public Integer getSequenceNumber();
	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	public void setSequenceNumber(Integer sequenceNumber);
	/**
	 * @return Returns the tooltip.
	 */
	public String getTooltip();
	/**
	 * @param tooltip The tooltip to set.
	 */
	public void setTooltip(String tooltip);
	

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#getSystemIdentifier()
	 */
	public Long getSystemIdentifier();

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
	 */
	public void setSystemIdentifier(Long id);

	/**
	 * @return return the HTML string for this type of a object
	 */
	public abstract String generateHTML();
	

	public void populateAttributes(Map propertiesMap);

}
