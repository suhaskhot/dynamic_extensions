package edu.common.dynamicextensions.domaininterface.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;

/**
 * This interface stores necessary informations about the control that gets added 
 * to the Container on dynamically generated User Interface.
 * @author geetika_bangard
 */
public interface ControlInterface {
    
    /**
     * Id of the control
	 * @return
	 */
	public Long getId();
	/**
	 * This can be a primitive type or derived type.
	 * @return Returns the attribute.
	 */
	public AbstractAttributeInterface getAbstractAttribute();
	/**
	 * @param attribute The attribute to set.
	 */
	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface);
	/**
	 * Caption/Title for the control.
	 * @return Returns the caption.
	 */
	public String getCaption();
	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption);
	/**
	 * The css specified for the control by user.
	 * @return Returns the cssClass.
	 */
	public String getCssClass();
	/**
	 * @param cssClass The cssClass to set.
	 */
	public void setCssClass(String cssClass);
	/**
	 * If user has chosen it to be kept hidden.
	 * @return Returns the isHidden.
	 */
	public Boolean getIsHidden();
	/**
	 * @param isHidden The isHidden to set.
	 */
	public void setIsHidden(Boolean isHidden);
	/**
	 * Name of the control.
	 * @return Returns the name.
	 */
	public String getName();
	/**
	 * @param name The name to set.
	 */
	public void setName(String name);
	/**
	 * The sequence Number for setting it at the desired place in the tree and so in the UI.
	 * @return Returns the sequenceNumber.
	 */
	public Integer getSequenceNumber();
	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	public void setSequenceNumber(Integer sequenceNumber);
	/**
	 * Tool tip for the control.
	 * @return Returns the tooltip.
	 */
	public String getTooltip();
	/**
	 * @param tooltip The tooltip to set.
	 */
	public void setTooltip(String tooltip);
		

}
