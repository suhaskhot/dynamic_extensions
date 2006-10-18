package edu.common.dynamicextensions.domain.userinterface;
import java.io.Serializable;
import java.util.Map;

import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.webui.util.UIConfigurationConstants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_CONTROL"
 */
public abstract class Control extends AbstractDomainObject implements Serializable,ControlInterface{
	/**
	 * Unique identifier for the object
	 */
	protected Long id = null;
	/**
	 * The caption of the control.
	 */
	protected String caption=null;
	/**
	 * The css class that is to be used for this control.
	 */
	protected String cssClass=null;
	/**
	 * whether this attribute should be displayed on screen.
	 */
	protected Boolean isHidden=null;
	/**
	 * Name of the control.
	 */
	protected String name=null;
	/**
	 * Sequence number of the control.This governs in which order it will be shown on the UI.
	 */
	protected Integer sequenceNumber=null;
	/**
	 * Tool tip message for the control.
	 */
	protected String tooltip=null;
	
	/**
	 * Value to be shown in the control
	 */
	protected String value = null;
	/**
	 * Attribute to which this control is associated.
	 */
	
	public AbstractAttribute abstractAttribute=null;
    
    /**
     * 
     *
     */

	public Control(){

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
	 * @hibernate.property name="tooltip" type="string" column="TOOLTIP" 
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
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
	 */
	public void setSystemIdentifier(Long id) {
	this.id = id;

	}

	/**
	 * @return return the HTML string for this type of a object
	 */
	public abstract String generateHTML();
	

	public void populateAttributes(Map propertiesMap) {
		if(propertiesMap!=null)
		{
			this.caption = (String)propertiesMap.get(UIConfigurationConstants.CAPTION_ATTRIBUTE);
			this.cssClass = (String)propertiesMap.get(UIConfigurationConstants.CSS_CLASSNAME_ATTRIBUTE);
			this.name = (String)propertiesMap.get(UIConfigurationConstants.NAME_ATTRIBUTE);
			this.tooltip = (String)propertiesMap.get(UIConfigurationConstants.TOOLTIP_ATTRIBUTE);
			this.value = (String)propertiesMap.get(UIConfigurationConstants.DEFAULT_VALUE_ATTRIBUTE);
		}
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	/*protected String appendEventHandlers()
	{
		String eventHandlersString = "";
		if(eventHandlers!=null)
		{
			Set eventHandlers.keySet();
		}
		return eventHandlersString;
	}*/
	/**
     * @hibernate.many-to-one column ="ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.AbstractAttribute"
	 * @return Returns the abstractAttribute.
	 */
	public AbstractAttributeInterface getAbstractAttribute() {
		return abstractAttribute;
	}
	/**
	 * @param abstractAttribute The abstractAttribute to set.
	 */
	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface) {
		this.abstractAttribute = (AbstractAttribute)abstractAttributeInterface;
	}
}