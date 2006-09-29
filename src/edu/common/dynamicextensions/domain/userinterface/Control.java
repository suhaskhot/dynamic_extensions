package edu.common.dynamicextensions.domain.userinterface;
import java.io.Serializable;
import java.util.Map;

import edu.common.dynamicextensions.domain.Attribute;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_CONTROL"
 */
public class Control extends AbstractDomainObject implements Serializable{
    /**
     * Unique identifier for the object
     */
    protected Long id;
    /**
     * The caption of the control.
     */
	protected String caption;
	/**
	 * The css class that is to be used for this control.
	 */
	protected String cssClass;
	/**
	 * whether this attribute should be displayed on screen.
	 */
	protected Boolean isHidden;
	/**
	 * Name of the control.
	 */
	protected String name;
	/**
	 * Sequence number of the control.This governs in which order it will be shown on the UI.
	 */
	protected Integer sequenceNumber;
	/**
	 * Tool tip message for the control.
	 */
	protected String tooltip;
	/**
	 * Attribute to which this control is associated.
	 */
	public Attribute attribute;

	public Control(){

	}

	public void finalize() throws Throwable {

	}
	 /**
     * @return
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_CONTROL_SEQ"
     */
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
	

    /**
     * @hibernate.many-to-one column ="ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.Attribute"
     * @return Returns the attribute.
     *
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
     * @hibernate.property name="caption" type="string" column="CAPTION" 
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
     * @hibernate.property name="cssClass" type="string" column="CSS_CLASS" 
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
     * @hibernate.property name="isHidden" type="boolean" column="HIDDEN" 
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
     * @hibernate.property name="sequenceNumber" type="integer" column="SEQUENCE_NUMBER" 
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
     * @hibernate.property name="tooltip" type="string" column="TOOL_TIP" 
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
    
    public String generateHTML()
    {
    	return null;
    }
    public void populateAttribute(Map propertiesMap) {
	}
}