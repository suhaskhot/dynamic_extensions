package edu.common.dynamicextensions.domain.userinterface;
import java.io.Serializable;

import edu.common.dynamicextensions.domain.Attribute;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 */
public class Control extends AbstractDomainObject implements Serializable{

	protected String caption;
	protected String cssClass;
	/**
	 * whether this attribute should be displayed on screen.
	 */
	protected Boolean isHidden;
	protected String name;
	protected Integer sequenceNumber;
	protected String tooltip;
	public Attribute attribute;

	public Control(){

	}

	public void finalize() throws Throwable {

	}
	

    /**
     * @return Returns the attribute.
     */
    public Attribute getAttribute() {
        return attribute;
    }
    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
    /**
     * @return Returns the caption.
     */
    public String getCaption() {
        return caption;
    }
    /**
     * @param caption The caption to set.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }
    /**
     * @return Returns the cssClass.
     */
    public String getCssClass() {
        return cssClass;
    }
    /**
     * @param cssClass The cssClass to set.
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    /**
     * @return Returns the isHidden.
     */
    public Boolean getIsHidden() {
        return isHidden;
    }
    /**
     * @param isHidden The isHidden to set.
     */
    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }
    /**
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
     * @return Returns the sequenceNumber.
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * @param sequenceNumber The sequenceNumber to set.
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    /**
     * @return Returns the tooltip.
     */
    public String getTooltip() {
        return tooltip;
    }
    /**
     * @param tooltip The tooltip to set.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#getSystemIdentifier()
     */
    public Long getSystemIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
     */
    public void setSystemIdentifier(Long arg0) {
        // TODO Auto-generated method stub
        
    }
}