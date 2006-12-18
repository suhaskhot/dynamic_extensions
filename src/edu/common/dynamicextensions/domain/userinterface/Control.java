
package edu.common.dynamicextensions.domain.userinterface;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_CONTROL"
 */
public abstract class Control extends DynamicExtensionBaseDomainObject
		implements
			Serializable,
			ControlInterface
{

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

	/**
	 * The caption of the control.
	 */
	protected String caption = null;

	/**
	 * The css class that is to be used for this control.
	 */
	protected String cssClass = null;

	/**
	 * whether this attribute should be displayed on screen.
	 */
	protected Boolean isHidden = null;

	/**
	 * Name of the control.
	 */
	protected String name = null;

	/**
	 * Sequence number of the control.This governs in which order it will be shown on the UI.
	 */
	protected Integer sequenceNumber = null;

	/**
	 * Tool tip message for the control.
	 */
	protected String tooltip = null;

	/**
	 * Value to be shown in the control
	 */
	protected Object value = null;

	/**
	 * Attribute to which this control is associated.
	 */
	protected AbstractAttributeInterface abstractAttribute;
	/**
	 * 
	 */
	protected Boolean sequenceNumberChanged = false;
	/**
	 * 
	 */
	protected Container parentContainer;

	/**
	 * Empty Constructor
	 */
	public Control()
	{
	}

	/**
	 * @hibernate.property name="caption" type="string" column="CAPTION" 
	 * @return Returns the caption.
	 */
	public String getCaption()
	{
		return caption;
	}

	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	/**
	 * @hibernate.property name="cssClass" type="string" column="CSS_CLASS" 
	 * @return Returns the cssClass.
	 */
	public String getCssClass()
	{
		return cssClass;
	}

	/**
	 * @param cssClass The cssClass to set.
	 */
	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	/**
	 * @hibernate.property name="isHidden" type="boolean" column="HIDDEN" 
	 * @return Returns the isHidden.
	 */
	public Boolean getIsHidden()
	{
		return isHidden;
	}

	/**
	 * @param isHidden The isHidden to set.
	 */
	public void setIsHidden(Boolean isHidden)
	{
		this.isHidden = isHidden;
	}

	/**
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.property name="sequenceNumber" type="integer" column="SEQUENCE_NUMBER" 
	 * @return Returns the sequenceNumber.
	 */
	public Integer getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @hibernate.property name="tooltip" type="string" column="TOOLTIP" 
	 * @return Returns the tooltip.
	 */
	public String getTooltip()
	{
		return tooltip;
	}

	/**
	 * @param tooltip The tooltip to set.
	 */
	public void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}

	/**
	 * @return return the HTML string for this type of a object
	 * @throws DynamicExtensionsSystemException  exception
	 */
	public final String generateHTML() throws DynamicExtensionsSystemException
	{
		String htmlString = "";
		if (getParentContainer().getMode() != null
				&& getParentContainer().getMode().equalsIgnoreCase(WebUIManagerConstants.VIEW_MODE))
		{
			htmlString = generateViewModeHTML();
		}
		else
		{
			htmlString = generateEditModeHTML();
		}
		return htmlString;
	}

	/**
	 * @return String html
	 * @throws DynamicExtensionsSystemException exception
	 */
	protected abstract String generateViewModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * @return String html
	 * @throws DynamicExtensionsSystemException exception
	 */
	protected abstract String generateEditModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * @return
	 */
	public Object getValue()
	{
		return this.value;
	}

	/**
	 * @param value
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * @hibernate.many-to-one  cascade="save-update" column="ABSTRACT_ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.AbstractAttribute" constrained="true"
	 */
	public AbstractAttributeInterface getAbstractAttribute()
	{
		return this.abstractAttribute;
	}

	/**
	 * @param abstractAttribute The abstractAttribute to set.
	 */
	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface)
	{
		this.abstractAttribute = abstractAttributeInterface;
	}

	/**
	 * @return String
	 * @throws DynamicExtensionsSystemException 
	 */
	public String getHTMLComponentName() throws DynamicExtensionsSystemException
	{
		AbstractAttributeInterface abstractAttributeInterface = this.getAbstractAttribute();
		EntityInterface entity = abstractAttributeInterface.getEntity();
		Long entityIdentifier = entity.getId();
		EntityManagerInterface entityManager = EntityManager.getInstance();
		ContainerInterface container = entityManager
				.getContainerByEntityIdentifier(entityIdentifier);
		if (this.getSequenceNumber() != null)
		{
			return "Control_" + container.getId() + "_" + this.getSequenceNumber();
		}
		return null;
	}

	/**
	 * 
	 */
	public Boolean getSequenceNumberChanged()
	{
		return sequenceNumberChanged;
	}

	/**
	 * 
	 * @param sequenceNumberChanged
	 */
	public void setSequenceNumberChanged(Boolean sequenceNumberChanged)
	{
		this.sequenceNumberChanged = sequenceNumberChanged;
	}

	/** 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		Control control = (Control) object;
		Integer thisSequenceNumber = this.sequenceNumber;
		Integer otherSequenceNumber = control.getSequenceNumber();
		return thisSequenceNumber.compareTo(otherSequenceNumber);
	}

	/**
	 *@hibernate.many-to-one column="CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
	 * @return parentContainer
	 */
	public Container getParentContainer()
	{
		return parentContainer;
	}

	/**
	 * @param parentContainer parentContainer
	 */
	public void setParentContainer(Container parentContainer)
	{
		this.parentContainer = parentContainer;
	}
}